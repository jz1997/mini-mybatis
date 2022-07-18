package com.exmaple.small.mybatis.executor;

import com.exmaple.small.mybatis.binding.MappedStatement;
import com.exmaple.small.mybatis.executor.statement.StatementHandler;
import com.exmaple.small.mybatis.session.BoundSql;
import com.exmaple.small.mybatis.session.Configuration;
import com.exmaple.small.mybatis.transaction.Transaction;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class SimpleExecutor extends BaseExecutor {

    public SimpleExecutor(Configuration configuration, Transaction transaction) {
        super(configuration, transaction);
    }

    @Override
    public <E> List<E> doQuery(
            MappedStatement ms, Object parameterObj, ResultHandler<E> resultHandler, BoundSql boundSql)
            throws SQLException {
        Statement statement = null;
        try {
            Configuration msConfiguration = ms.getConfiguration();
            StatementHandler handler =
                    msConfiguration.newStatementHandler(ms, parameterObj, resultHandler);
            statement = handler.prepare(transaction.getConnection());
            handler.parameterize(statement, parameterObj);
            return handler.query(statement, resultHandler);
        } finally {
            closeStatement(statement);
        }
    }

    @Override
    public int doInsert(MappedStatement ms, Object parameter) throws SQLException {
        Statement statement = null;
        try {
            Configuration msConfiguration = ms.getConfiguration();
            StatementHandler handler = msConfiguration.newStatementHandler(ms, parameter, EMPTY_RESULT_HANDLER);
            statement = handler.prepare(transaction.getConnection());
            handler.parameterize(statement, parameter);
            return handler.insert(statement);
        } finally {
            closeStatement(statement);
        }
    }

    @Override
    public int doUpdate(MappedStatement ms, Object parameter) throws SQLException {
        Configuration msConfiguration = ms.getConfiguration();
        StatementHandler handler = msConfiguration.newStatementHandler(ms, parameter, EMPTY_RESULT_HANDLER);
        Statement statement = handler.prepare(transaction.getConnection());
        handler.parameterize(statement, parameter);
        return handler.update(statement);
    }

    @Override
    public int doDelete(MappedStatement ms, Object parameter) throws SQLException {
        Configuration msConfiguration = ms.getConfiguration();
        StatementHandler handler = msConfiguration.newStatementHandler(ms, parameter, EMPTY_RESULT_HANDLER);
        Statement statement = handler.prepare(transaction.getConnection());
        handler.parameterize(statement, parameter);
        return handler.delete(statement);
    }
}
