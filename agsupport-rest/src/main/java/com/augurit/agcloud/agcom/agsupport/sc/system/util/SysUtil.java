package com.augurit.agcloud.agcom.agsupport.sc.system.util;

import com.common.util.Common;

/**
 * 替换字符串中的host
 */
public class SysUtil {

    public static String replaceMenuOut(String str) {
        if (str == null) return null;
        String str_in = Common.getByKey("server.menu.in");
        String str_out = Common.getByKey("server.menu.out");
        if (str_in != null && str_out != null) {
            String[] split = str_in.split(" *,");
            for (String in : split) {
                str = str.replaceAll(in, str_out);
            }
        }
        return str;
    }

    public static String replaceMenuIn(String str) {
        if (str == null) return null;
        String str_in = Common.getByKey("server.menu.in");
        String str_out = Common.getByKey("server.menu.out");
        if (str_in != null && str_out != null) {
            str_in = str_in.split(" *,")[0];
            str = str.replaceAll(str_out, str_in);
        }
        return str;
    }

    public static String replaceLayerOut(String str) {
        if (str == null) return null;
        String str_in = Common.getByKey("server.layer.in");
        String str_out = Common.getByKey("server.layer.out");
        if (str_in != null && str_out != null) {
            String[] split = str_in.split(" *,");
            for (String in : split) {
                str = str.replaceAll(in, str_out);
            }
        }
        return str;
    }

    public static String replaceLayerIn(String str) {
        if (str == null) return null;
        String str_in = Common.getByKey("server.layer.in");
        String str_out = Common.getByKey("server.layer.out");
        if (str_in != null && str_out != null) {
            str_in = str_in.split(" *,")[0];
            str = str.replaceAll(str_out, str_in);
        }
        return str;
    }
}
