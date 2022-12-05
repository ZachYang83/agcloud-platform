package com.augurit.agcloud.agcom.agsupport.sc.dir.util;

/**
 * Created by Augurit on 2017-04-21.
 */
public class StringUtils extends org.apache.commons.lang.StringUtils {

    public static final String SEPARATOR_SLASH = "/";
    public static final String SEPARATOR_COMMA = ",";
    public static final String SEPARATOR_DOT = "\\.";
    public static final String SEPARATOR_BLANK_SPACE = " ";
    public static final String SEPARATOR_CHINESE_COMMA = "\u3001";

    public StringUtils() {
    }

    public static String replaceLast(String str, String searchString, String replacement) {
        if (str != null && searchString != null && replacement != null) {
            int pos = str.lastIndexOf(searchString);
            if (pos >= 0) {
                String part1 = str.substring(0, pos);
                String part2 = str.substring(pos + searchString.length(), str.length());
                return (new StringBuilder(String.valueOf(part1))).append(replacement).append(part2).toString();
            }
        }
        return null;
    }
}