package com.mini.mybatis.executor.parameter;

import java.sql.PreparedStatement;

public interface ParameterHandler {
    Object getParameterObject();
    void setParameters(PreparedStatement ps);
}
