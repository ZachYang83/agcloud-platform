package com.augurit.agcloud.agcom.agsupport.common.util;

public class UuidChanger {

    private static String uuid = UUIDUtil.getUUID();
    static {
        change();
    }

    public static String getUuid() {
        return uuid;
    }

    public static void change() {
        //循环执行
        final java.util.Timer timer = new java.util.Timer();
        timer.schedule(new java.util.TimerTask() {
            public void run() {
                uuid = UUIDUtil.getUUID();
                //如若想终止，用cancel方法
                //timer.cancel();
            }
        }, 1800000, 1800000);	//每隔0.5小时执行一次
    }
}

