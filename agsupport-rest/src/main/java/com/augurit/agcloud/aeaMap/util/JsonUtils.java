package com.augurit.agcloud.aeaMap.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import net.sf.json.JSONObject;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class JsonUtils {
    /**
     * 获取jsonOjbect对象的字符串类型的属性
     *
     * @param jsonObj
     * @param key
     * @return ""的返回值也是null
     */
    public static String getString(JsonObject jsonObj, String key) {
        String result = null;
        JsonElement je = jsonObj.get(key);

        //处理传递的 JSON 值 是 null
        boolean isNull = je instanceof JsonNull;
        if (je != null && !isNull) {
            String str = je.getAsString();
            if (str != null) {    //空字符串"" 和 "null" 都被当做是 null
                result = (str.equals("") || str.equals("null")) ? null : str;
            }

        }
        return result;
    }

    /**
     * 获取 jsonObject对象的 时间类型的 属性
     *
     * @param jsonObj
     * @param key
     * @param dateFormate 时间格式
     * @return
     * @throws ParseException
     */
    public static Date getDate(JsonObject jsonObj, String key, String dateFormate) throws ParseException {
        JsonElement je = jsonObj.get(key);
        Date date = null;

        //处理传递的 JSON 值 是 null
        boolean isNull = je instanceof JsonNull;
        if (je != null && !isNull) {
            String str = je.getAsString();
            if (str != null && !str.equals("")) {
                SimpleDateFormat formate = new SimpleDateFormat(dateFormate);
                date = formate.parse(str);
            }
        }
        return date;
    }

    /**
     * 获取jsonOjbect对象的BigDecimal类型的属性
     *
     * @param jsonObj
     * @param key
     * @return
     */
    public static BigDecimal getBigDecimal(JsonObject jsonObj, String key) {
        JsonElement je = jsonObj.get(key);
        //处理传递的 JSON 值 是 null
        boolean isNull = je instanceof JsonNull;
        if (je != null && !isNull) {
            return je.getAsBigDecimal();
        } else
            return null;
    }

    /**
     * 获取jsonOjbect对象的Long类型的属性
     *
     * @param jsonObj
     * @param key
     * @return
     */
    public static Long getLong(JsonObject jsonObj, String key) {
        JsonElement je = jsonObj.get(key);
        //处理传递的 JSON 值 是 null
        boolean isNull = je instanceof JsonNull;
        if (je != null && !isNull) {
            return je.getAsLong();
        } else
            return null;
    }

    /**
     * 获取jsonOjbect对象的Long类型的属性
     *
     * @param jsonObj
     * @param key
     * @return
     */
    public static List<Long> getLongArray(JsonObject jsonObj, String key) {
        List<Long> result = null;
        JsonArray array = jsonObj.getAsJsonArray(key);
        //处理传递的 JSON 值 是 null
        if (array != null) {
            result = new ArrayList<Long>();
            Iterator<JsonElement> elemIter = array.iterator();
            while (elemIter.hasNext()) {
                JsonObject je = (JsonObject) elemIter.next();
                result.add(je.getAsLong());
            }
        }
        return result;
    }

    public static Number getNumber(JsonObject jsonObj, String key) {
        JsonElement je = jsonObj.get(key);
        //处理传递的 JSON 值 是 null
        boolean isNull = je instanceof JsonNull;
        if (je != null && !isNull) {
            return je.getAsNumber();
        } else
            return null;
    }

    public static Integer getInteger(JsonObject jsonObj, String key) {
        JsonElement je = jsonObj.get(key);
        //处理传递的 JSON 值 是 null
        boolean isNull = je instanceof JsonNull;
        if (je != null && !isNull) {
            return je.getAsInt();
        } else
            return null;
    }


    /**
     * 获取jsonOjbect对象的Boolean类型的属性
     *
     * @param jsonObj
     * @param key
     * @return
     */
    public static Boolean getBoolean(JsonObject jsonObj, String key) {
        JsonElement je = jsonObj.get(key);
        //处理传递的 JSON 值 是 null
        boolean isNull = je instanceof JsonNull;
        if (je != null && !isNull) {
            return je.getAsBoolean();
        } else
            return null;
    }

    public static JsonArray getJsonArray(JsonObject jsonObj, String key) {
        JsonElement je = jsonObj.get(key);
        //处理传递的 JSON 值 是 null
        boolean isNull = je instanceof JsonNull;
        if (je != null && !isNull) {
            return je.getAsJsonArray();
        } else
            return null;
    }

    public static Double getDouble(JsonObject jsonObject, String key) {
        JsonElement je = jsonObject.get(key);
        //处理传递的 JSON 值 是 null
        boolean isNull = je instanceof JsonNull;
        if (je != null && !isNull) {
            return je.getAsDouble();
        }
        return null;
    }


    /**
     * 给非空的字符串两边加上单引号
     *
     * @param str
     * @return
     */
    public static String warpString(String str) {
        if (str != null) {
            return new StringBuffer("\"").append(str).append("\"").toString();
        }
        return null;
    }

    /**
     * 给非空的字符串两边加上单引号
     *
     * @param str
     * @return
     */
    public static String warpString(Object val) {
        if (val != null) {
            return new StringBuffer("\"").append(val.toString()).append("\"").toString();
        }
        return null;
    }

    public static String warpDate(Date date, String dateFormat) {
        StringBuffer resultBuffer = new StringBuffer();
        if (date != null) {
            SimpleDateFormat format = new SimpleDateFormat(dateFormat);
            resultBuffer.append("\"").append(format.format(date)).append("\"");
        } else {
            resultBuffer.append("null");
        }
        return resultBuffer.toString();
    }

    /**
     * json转hashMap
     *
     * @param json
     * @return
     */
    public static HashMap<String, Object> jsonToHashMap(String json) {
        HashMap<String, Object> data = new HashMap<String, Object>();
        // 将json字符串转换成jsonObject
        JSONObject jsonObject = JSONObject.fromObject(json);
        Iterator it = jsonObject.keys();
        // 遍历jsonObject数据，添加到Map对象
        while (it.hasNext()) {
            String key = String.valueOf(it.next());
            Object value = jsonObject.get(key);
            data.put(key, value);
        }
        return data;
    }

}
