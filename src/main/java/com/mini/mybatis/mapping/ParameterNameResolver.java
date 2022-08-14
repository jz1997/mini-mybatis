package com.mini.mybatis.mapping;

import com.mini.mybatis.annotation.Param;
import com.mini.mybatis.exception.MappingException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

public class ParameterNameResolver {
    private Map<Integer, String> indexNameMap = null;
    private boolean hasParamAnno = false;
    private final String PARAM_PREFIX = "param_";

    public ParameterNameResolver(Method method) {
        parseNameMap(method);
    }

    /**
     * 参数列表转化成 index -> parameterName Map
     *
     * @param method {@link Method} /
     */
    private void parseNameMap(Method method) {
        indexNameMap = new HashMap<>();

        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        for (int index = 0; index < parameterAnnotations.length; index++) {
            Annotation[] annotations = parameterAnnotations[index];

            String name = null;
            for (Annotation annotation : annotations) {
                if (annotation instanceof Param) {
                    name = ((Param) annotation).value();
                    hasParamAnno = true;
                }
            }

            if (name == null) {
                name = getParameterName(method, index);
            }

            if (name == null) {
                throw new MappingException("解析方法 " + method.getName() + " 参数名称失败");
            }
            indexNameMap.put(index, name);
        }
    }

    private String getParameterName(Method method, int index) {
        Parameter[] parameters = method.getParameters();
        Parameter parameter = parameters[index];
        return parameter.getName();
    }

    public Object parse(Object[] args) {
        // 没有参数
        if (args == null || args.length == 0) {
            return null;
        }

        else if (args.length == 1 && !hasParamAnno) {
            return args[0];
        }

        // 参数转化成 map
        else {
            final Map<String, Object> resultMap = new HashMap<>();
            for (int index = 0; index < args.length; index++) {
                String key = indexNameMap.get(index);
                resultMap.put(key, args[index]);
            }

            return resultMap;
        }
    }
}
