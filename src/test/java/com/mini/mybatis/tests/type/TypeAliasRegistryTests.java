package com.mini.mybatis.tests.type;

import com.mini.mybatis.exception.TypeException;
import com.mini.mybatis.type.TypeAliasRegistry;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TypeAliasRegistryTests {
    @Test
    public void test_registerAlias_resolveAlias() {
        TypeAliasRegistry typeAliasRegistry = new TypeAliasRegistry();
        typeAliasRegistry.registerAlias(String.class);
        assertNotNull(typeAliasRegistry.resolveAlias("string"));

        typeAliasRegistry.registerAlias("long", Long.class);
        assertNotNull(typeAliasRegistry.resolveAlias("long"));

        assertThrows(TypeException.class, () -> {
            typeAliasRegistry.resolveAlias("long2");
        });
    }

    @Test
    public void test_registerAliases() {
        TypeAliasRegistry typeAliasRegistry = new TypeAliasRegistry();
        typeAliasRegistry.registerAliases("com.mini.mybatis.tests.type");
        assertNotNull(typeAliasRegistry.resolveAlias("TypeAliasRegistryTests"));
    }
}
