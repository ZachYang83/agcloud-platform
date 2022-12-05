package com.augurit.agcloud.agcom.agsupport.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 海贼王に、おれはなる!!!
 * Created by Caip on 2017-05-09.
 */
public class RestUtil {
    private static byte[] getByteArr(InputStream input) throws IOException {
        List<byte[]> list = new ArrayList();
        int total = 0;
        while (true) {
            int size = 64;
            byte[] buf = new byte[size];
            int ind = input.read(buf);
            if (ind == -1) break;
            if (ind < size) {
                byte[] bufnew = new byte[ind];
                System.arraycopy(buf, 0, bufnew, 0, ind);
                list.add(bufnew);
            } else {
                list.add(buf);
            }
            total += ind;
        }
        input.close();
        byte[] bytay = new byte[total];
        for (int i = 0, n = 0; i < list.size(); i++) {
            byte[] byt = list.get(i);
            for (int j = 0; j < byt.length; j++, n++) {
                byte temp = byt[j];
                bytay[n] = temp;
            }
        }
        return bytay;
    }

    public static String pasePostInputStream(InputStream is) {
        String post = null;
        try {
            post = new String(getByteArr(is), "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return post;
    }
}
