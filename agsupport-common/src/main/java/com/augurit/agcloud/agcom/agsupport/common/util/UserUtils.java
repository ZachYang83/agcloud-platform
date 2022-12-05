package com.augurit.agcloud.agcom.agsupport.common.util;

import com.common.dbcp.DBHelper;
import com.common.util.Common;

import java.util.Arrays;

public class UserUtils {

    public static String getUserName(String loginName) throws Exception{
        if (Common.isCheckNull(loginName)) return null;
        String sql = "select user_name from ag_user where login_name=?";
        String userName = Common.checkNull(DBHelper.findFirstColum(sql, Arrays.asList(loginName)));
        return userName;
    }
}
