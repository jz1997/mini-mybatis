package com.mini.mybatis.executor;

import java.sql.PreparedStatement;

public interface ParameterHandler {
    Object getParameterObject();
    void setParameters(PreparedStatement ps);
}
