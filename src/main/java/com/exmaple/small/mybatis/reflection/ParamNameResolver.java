package com.exmaple.small.mybatis.reflection;

import cn.hutool.core.util.StrUtil;
import com.exmaple.small.mybatis.annotations.Param;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ParamNameResolver {

  private SortedMap<Integer, String> namesMap;
  private boolean hasParamAnnotation = false;

  public ParamNameResolver(Method method) {
    Parameter[] parameters = method.getParameters();
    SortedMap<Integer, String> map = new TreeMap<>();
    for (int i = 0; i < parameters.length; i++) {
      Parameter parameter = parameters[i];
      Param param = parameter.getAnnotation(Param.class);
      String name = null;
      if (param != null) {
        name = param.value();
      }
      if (name == null) {
        name = getParameterActualName(method, i);
      }

      map.put(i, name);
    }

    this.namesMap = Collections.unmodifiableSortedMap(map);
  }

  private String getParameterActualName(Method method, int index) {
    Parameter[] parameters = method.getParameters();
    return parameters[index].getName();
  }

  public Object getNamedParams(Object[] args) {
    if (hasNoneParameter(args)) {
      return null;
    } else if (hasOneParameterWithoutParamAnnotation()) {
      return args[namesMap.firstKey()];
    } else {
      final Map<String, Object> namedParamsMap = new HashMap<>();
      namesMap.forEach(
          (k, v) -> {
            namedParamsMap.put(v, args[k]);
          });
      return namedParamsMap;
    }
  }

  private boolean hasOneParameterWithoutParamAnnotation() {
    return !hasParamAnnotation && namesMap.size() == 1;
  }

  private boolean hasNoneParameter(Object[] args) {
    return args == null || namesMap.size() == 0;
  }
}
