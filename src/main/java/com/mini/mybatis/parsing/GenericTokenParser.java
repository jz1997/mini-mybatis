package com.mini.mybatis.parsing;

import cn.hutool.core.util.StrUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GenericTokenParser {
    private final Pattern pattern;
    private final TokenHandler tokenHandler;

    public GenericTokenParser(String openToken, String closeToken, TokenHandler tokenHandler) {
        this.tokenHandler = tokenHandler;
        pattern = Pattern.compile(openToken + "(.*?)" + closeToken);
    }

    public String parse(String originalSql) {
        Matcher matcher = pattern.matcher(originalSql);
        StringBuilder sqlSb = new StringBuilder();
        while (matcher.find()) {
            String group = matcher.group();
            // group -> property name
            String propertyName = parsePropertyName(group);
            String replaceToken = tokenHandler.handleToken(propertyName);
            matcher.appendReplacement(sqlSb, replaceToken);
        }
        matcher.appendTail(sqlSb);
        String sql = sqlSb.toString();
        return StrUtil.isBlank(sql) ? originalSql : sql;
    }

    // 从正则表达式的 group 中提取内容
    private static String parsePropertyName(String group) {
        return group.substring(2, group.length() - 1);
    }
}
