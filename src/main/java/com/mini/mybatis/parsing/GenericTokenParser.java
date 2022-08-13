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
            String content = matcher.group();
            String replaceToken = tokenHandler.handleToken(content);
            matcher.appendReplacement(sqlSb, replaceToken);
        }
        matcher.appendTail(sqlSb);
        String sql = sqlSb.toString();
        return StrUtil.isBlank(sql) ? originalSql : sql;
    }
}
