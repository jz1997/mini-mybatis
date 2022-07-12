package com.exmaple.small.mybatis.type;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface TypeHandler {
    void setParameter(PreparedStatement ps, int index, Object parameter) throws SQLException;
}
