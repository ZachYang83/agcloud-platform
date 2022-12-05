package com.augurit.agcloud.agcom.agsupport.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @Package: testHttpRequest
 * @ClassName: UrlEncode
 * @Description: 将url包含的中文编码
 * @UpdateDate: 2019/12/31 16:54
 */
public class UrlEncode {

    public static String encoder(String str) {
        if (null == str || "".equals(str)) {
            return str;
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < str.length(); i++) {
            char temp = str.charAt(i);
            if (isChinese(temp)) {
                try {
                    sb.append(URLEncoder.encode(new Character(temp).toString(),
                            "utf-8"));
                } catch (UnsupportedEncodingException e) {
                    return str;
                }
            } else {
                sb.append(temp);
            }
        }
        return sb.toString();
    }

    /**
     * @param c 输入的字符
     * @return 是否为中文字符
     * GENERAL_PUNCTUATION 判断中文的"号
     * CJK_SYMBOLS_AND_PUNCTUATION 判断中文的。号
     * HALFWIDTH_AND_FULLWIDTH_FORMS 判断中文的，号
     */

    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);

        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }
}
