package com.mini.mybatis.session;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ReflectUtil;
import com.mini.mybatis.mapping.MappedStatement;
import com.mini.mybatis.transaction.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DefaultSqlSession implements SqlSession {
    private static final Logger logger = LoggerFactory.getLogger(DefaultSqlSession.class);

    private final Configuration configuration;
    private final Transaction transaction;

    public DefaultSqlSession(Configuration configuration, Transaction transaction) {
        this.configuration = configuration;
        this.transaction = transaction;
    }

    @Override
    public <T> T selectOne(String statement) {
        return this.selectOne(statement, null);
    }

    @Override
    public <T> T selectOne(String statement, Object parameter) {
        List<T> objects = this.selectList(statement, parameter);
        if (objects.size() > 1) {
            throw new RuntimeException("selectOne 查询出多个结果");
        } else if (objects.size() == 0) {
            return null;
        } else {
            return objects.get(0);
        }
    }

    @Override
    public <E> List<E> selectList(String statement) {
        return this.selectList(statement, null);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <E> List<E> selectList(String statement, Object parameter) {
        logger.info("into selectOne method, statement is '{}'", statement);
        // todo: 执行过程待封装
        MappedStatement ms = configuration.getMapperStatement(statement);
        String sql = ms.getSql();
        sql = sql.replaceAll("#\\{id}", "?");
        logger.info("sql: {}" + sql);
        PreparedStatement ps = null;
        try {
            Connection connection = transaction.getConnection();
            ps = connection.prepareStatement(sql);
            ps.setString(1, "1");
            ps.execute();
            ResultSet resultSet = ps.getResultSet();
            List<Object> objs = handlerResult(ms, resultSet);
            return (List<E>) objs;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            IoUtil.close(ps);
        }
    }

    /**
     * 将 ResultSet 转化成 resultType 类型的对象
     *
     * @param ms        {@link MappedStatement} /
     * @param resultSet {@link ResultSet} /
     * @return /
     */
    private List<Object> handlerResult(MappedStatement ms, ResultSet resultSet) {
        // todo: 结果集处理进行封装
        List<Object> results = new ArrayList<>();

        try {
            String resultType = ms.getResultType();
            Class<?> objClass = Class.forName(resultType);
            Object obj = objClass.getConstructor().newInstance();
            while (resultSet.next()) {
                // 通过反射进行属性的设值
                Field[] fields = ReflectUtil.getFields(objClass);
                for (Field field : fields) {
                    ReflectUtil.setFieldValue(obj, field.getName(), resultSet.getObject(field.getName()));
                }
                results.add(obj);
            }
        } catch (SQLException | ClassNotFoundException | InvocationTargetException | InstantiationException |
                 IllegalAccessException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        } finally {
            IoUtil.close(resultSet);
        }

        return results;
    }

    @Override
    public <T> T getMapper(Class<T> mapperClass) {
        return configuration.getMapper(mapperClass, this);
    }

    @Override
    public Configuration getConfiguration() {
        return configuration;
    }
}
