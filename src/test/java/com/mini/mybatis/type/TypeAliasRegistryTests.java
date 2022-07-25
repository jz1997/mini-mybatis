package com.mini.mybatis.type;

import com.mini.mybatis.exception.TypeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TypeAliasRegistryTests {

    TypeAliasRegistry typeAliasRegistry;

    @BeforeEach
    void beforeEach() {
        typeAliasRegistry = new TypeAliasRegistry();
    }

    @Test
    void test_registerAlias_with_null_clazz() {
        assertThrows(TypeException.class, () -> typeAliasRegistry.registerAlias(null));
    }

    @Test
    void test_registerAlias_with_not_exist_clazz() {
        typeAliasRegistry.registerAlias(TypeAliasRegistryTests.class);
        assertNotNull(typeAliasRegistry.resolveAlias("TypeAliasRegistryTests"));
    }

    @Test
    void test_registerAlias_with_exist_clazz() {
        assertThrows(TypeException.class, () -> typeAliasRegistry.registerAlias(String.class));
    }

    @Test
    void test_registerAlias_with_alias_anno_clazz() {
        typeAliasRegistry.registerAlias(AliasAnnoTest.class);
        assertThrows(TypeException.class, () -> typeAliasRegistry.resolveAlias("AliasAnnoTest"));
        assertNotNull(typeAliasRegistry.resolveAlias("AliasAnnotationTest"));
    }


    @Test
    void test_registerAlias_with_null_alias_normal_clazz() {
        assertThrows(TypeException.class, () -> typeAliasRegistry.registerAlias(null, String.class));
    }

    @Test
    void test_registerAlias_with_normal_alias_null_clazz() {
        assertThrows(TypeException.class, () -> typeAliasRegistry.registerAlias("alias", null));
    }


    @Test
    void test_registerAliases_with_packageName() {
        typeAliasRegistry.registerAliases("com.mini.mybatis.type");
        assertNotNull(typeAliasRegistry.resolveAlias("TypeAliasRegistryTests"));
    }

    @Test
    void test_registerAliases_with_null_packageName() {
        assertThrows(TypeException.class, () -> typeAliasRegistry.registerAliases(null));
    }


    @Test
    void test_resolveAlias_with_exist_alias() {
        assertNotNull(typeAliasRegistry.resolveAlias("string"));
    }

    @Test
    void test_resolveAlias_with_not_exist_alias() {
        assertThrows(TypeException.class, () -> typeAliasRegistry.resolveAlias("string2"));
    }

    @Test
    void test_resolveAlias_with_null_alias() {
        assertThrows(TypeException.class, () -> typeAliasRegistry.resolveAlias(null));
    }
}