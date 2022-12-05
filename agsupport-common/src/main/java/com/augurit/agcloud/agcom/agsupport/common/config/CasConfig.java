package com.augurit.agcloud.agcom.agsupport.common.config;

import java.text.SimpleDateFormat;

/**
 * 海贼王に、おれはなる!!!
 * Created by Caip on 2017-06-14.
 */
public class CasConfig {
    //cas跳过登录记录表
    public static final String CAS_LOGIN_CACHE_TABLE = "cas_login_cache";
    public static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    //一个token的有效使用次数
    public static final int CAS_CHECK_MAX_TIMES = 2;
    //一个token的有效使用时间(单位 毫秒)
    public static final int CAS_CHECK_MAX_TIME = 1000 * 60 * 1;
    public static final String ID_COLUMN = "id";
    public static final String LOGIN_NAME_COLUMN = "login_name";
    public static final String TOKEN_COLUMN = "token";
    public static final String CHECK_TIME_COLUMN = "ckeck_time";
    public static final String CREATE_TIME_COLUMN = "create_time";
}
