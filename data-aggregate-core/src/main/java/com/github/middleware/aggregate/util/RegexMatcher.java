package com.github.middleware.aggregate.util;

import com.google.common.base.Strings;

import java.util.regex.Pattern;
/**
 * @Author: alex
 * @Description:
 * @Date: created in 2019/1/9.
 */
public class RegexMatcher {
    public static boolean match(String text, String regexPattern) {
        if (Strings.isNullOrEmpty(text) || Strings.isNullOrEmpty(regexPattern)) {
            return false;
        }
        return Pattern.matches(regexPattern, text);
    }
}
