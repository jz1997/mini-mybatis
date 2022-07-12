package com.exmaple.small.mybatis.parsing;

import cn.hutool.core.util.StrUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GenericTokenParser {
    private final String openToken;
    private final String closeToken;
    private final Pattern pattern;
    private final TokenHandler tokenHandler;

    public GenericTokenParser(String openToken, String closeToken, TokenHandler tokenHandler) {
        this.openToken = openToken;
        this.closeToken = closeToken;
        this.tokenHandler = tokenHandler;
        pattern = Pattern.compile(openToken + "(.*?)" + closeToken);
    }

    public String parse(String originalSql) {
        Matcher matcher = pattern.matcher(originalSql);
        StringBuilder sb = new StringBuilder();
        while (matcher.find()) {
            String group = matcher.group();
            String property = getProperty(group);
            String replacementToken = tokenHandler.handleToken(property);
            matcher.appendReplacement(sb, replacementToken);
        }
        String sql = sb.toString();
        return StrUtil.isBlank(sql) ? originalSql : sql;
    }

    private String getProperty(String group) {
        String property = group.replace(getPureOpenToken(), "");
        property = property.replace(getPureCloseToken(), "");
        return property;
    }

    private String getPureOpenToken() {
        if (this.openToken.contains("\\")) {
            return this.openToken.replace("\\", "");
        }
        return this.openToken;
    }

    private String getPureCloseToken() {
        if (this.closeToken.contains("\\")) {
            return this.closeToken.replace("\\", "");
        }
        return this.closeToken;
    }
}
