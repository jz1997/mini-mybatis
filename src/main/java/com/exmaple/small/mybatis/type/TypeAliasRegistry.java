package com.exmaple.small.mybatis.type;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.*;

/** 类型注册 */
public class TypeAliasRegistry {

  private final Map<String, Class<?>> TYPE_ALIASES = new HashMap<>();

  public TypeAliasRegistry() {
    registerAlias("string", String.class);
    registerAlias("int", Integer.class);
    registerAlias("long", Long.class);
    registerAlias("float", Float.class);
    registerAlias("double", Double.class);
    registerAlias("boolean", Boolean.class);
    registerAlias("byte", Byte.class);
    registerAlias("short", Short.class);
    registerAlias("char", Character.class);
    registerAlias("date", Date.class);
    registerAlias("timestamp", Timestamp.class);
    registerAlias("time", Time.class);
    registerAlias("java.util.Date", Date.class);
    registerAlias("java.sql.Date", Date.class);
    registerAlias("java.sql.Timestamp", Timestamp.class);
    registerAlias("java.sql.Time", Time.class);
  }

  @SuppressWarnings("unchecked")
  public <T> Class<T> resolveAlias(String alias) {
    if (TYPE_ALIASES.containsKey(alias)) {
      return (Class<T>) TYPE_ALIASES.get(alias);
    }
    throw new IllegalArgumentException("Type alias not registered: " + alias);
  }

  public void registerAlias(String alias, Class<?> clazz) {
    if (TYPE_ALIASES.containsKey(alias)) {
      throw new IllegalArgumentException("Type alias already registered: " + alias);
    }
    TYPE_ALIASES.put(alias, clazz);
  }
}
