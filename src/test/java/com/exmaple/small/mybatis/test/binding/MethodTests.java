package com.exmaple.small.mybatis.test.binding;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class MethodTests {
    @Test
    public void test_isAssignableFrom() {
        log.info(String.valueOf(List.class.isAssignableFrom(ArrayList.class)));
        log.info(String.valueOf(ArrayList.class.isAssignableFrom(List.class)));
    }
}
