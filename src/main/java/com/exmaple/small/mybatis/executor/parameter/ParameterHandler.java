package com.exmaple.small.mybatis.executor.parameter;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public interface ParameterHandler {
    Object getParameterObject();

    void setParameters(PreparedStatement statement) throws SQLException;
}
