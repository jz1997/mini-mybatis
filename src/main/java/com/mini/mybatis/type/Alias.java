package com.mini.mybatis.type;

import java.lang.annotation.*;

/**
 * Alias:
 * 指定类型别名
 *
 * @author jzheng
 * @since 2022/7/25 09:43
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Alias {
    String value();
}
