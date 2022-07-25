package com.mini.mybatis.type;

import cn.hutool.core.lang.ClassScanner;
import com.mini.mybatis.exception.TypeException;

import java.util.*;

/**
 *
 */
public class TypeAliasRegistry {
    private final Map<String, Class<?>> typeAliasMap = new HashMap<>();

    /**
     * 注册类型别名, 默认使用 class 的 simple name 全小写, 如果 class 使用 Alias 注解则使用 Alias 注解的 value 作为别名.
     *
     * @param clazz 被注册的类型
     */
    public void registerAlias(Class<?> clazz) {
        String alias = clazz.getSimpleName();
        Alias aliasAnno = clazz.getAnnotation(Alias.class);
        if (aliasAnno != null) {
            alias = aliasAnno.value();
        }
        registerAlias(alias, clazz);
    }

    /**
     * 注册类型别名
     *
     * @param alias 别名
     * @param clazz 类型
     */
    public void registerAlias(String alias, Class<?> clazz) {
        if (null == alias) {
            throw new TypeException("类型别名不能为空");
        }

        String key = alias.toLowerCase(Locale.ENGLISH);
        if (isAlreadyRegister(clazz, key)) {
            throw new TypeException("类型别名 '" + typeAliasMap.get(key).getName() + "' 已经注册.");
        }

        typeAliasMap.put(key, clazz);
    }

    private boolean isAlreadyRegister(Class<?> clazz, String key) {
        return typeAliasMap.containsKey(key) && typeAliasMap.get(key) != null && Objects.equals(typeAliasMap.get(key), clazz);
    }

    /**
     * 注册包路径下全部的类型别名
     *
     * @param packageName 包路径名称
     */
    public void registerAliases(String packageName) {
        this.registerAliases(packageName, Object.class);
    }

    /**
     * 根据父类型注册类型别名
     *
     * @param packageName 包路径名称
     * @param superType   父类型
     */
    public void registerAliases(String packageName, Class<?> superType) {
        Set<Class<?>> classes = ClassScanner.scanPackage(packageName);
        // 过滤掉不继承自 superType 的类型、匿名类、接口、内部类
        classes.stream()
                .filter(c -> superType.isAssignableFrom(c) && !c.isAnonymousClass() && !c.isInterface() && !c.isMemberClass())
                .forEach(this::registerAlias);
    }


    /**
     * 通过别名获取对应的类型
     *
     * @param alias 别名
     * @return /
     */
    @SuppressWarnings("unchecked")
    public <T> Class<T> resolveAlias(String alias) {
        if (null == alias) {
            return null;
        }
        String key = alias.toLowerCase(Locale.ENGLISH);
        Class<?> clazz = this.typeAliasMap.get(key);
        if (clazz == null) {
            throw new TypeException("未知的类型别名 '" + alias + "'");
        }
        return (Class<T>) clazz;
    }

}