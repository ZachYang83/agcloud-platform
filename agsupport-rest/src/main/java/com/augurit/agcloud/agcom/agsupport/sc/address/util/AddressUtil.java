package com.augurit.agcloud.agcom.agsupport.sc.address.util;

import com.augurit.agcloud.framework.util.JsonUtils;
import com.common.dbcp.DBHelper;
import com.common.util.Common;
import com.common.util.ConfigProperties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.augurit.agcloud.agcom.agsupport.sc.address.config.AgAddressConfig.*;

/**
 * Created by Administrator on 2017-09-27.
 */
public class AddressUtil {

    private static int min(int one, int two, int three) {
        int min = one;
        if (two < min) {
            min = two;
        }
        if (three < min) {
            min = three;
        }
        return min;
    }

    /**
     * 计算编辑距离
     *
     * @return
     */
    public static int ld(String str1, String str2) {
        int d[][]; // 矩阵
        int n = str1.length();
        int m = str2.length();
        int i; // 遍历str1的
        int j; // 遍历str2的
        char ch1; // str1的
        char ch2; // str2的
        int temp; // 记录相同字符,在某个矩阵位置值的增量,不是0就是1
        if (n == 0) {
            return m;
        }
        if (m == 0) {
            return n;
        }
        d = new int[n + 1][m + 1];
        for (i = 0; i <= n; i++) { // 初始化第一列
            d[i][0] = i;
        }
        for (j = 0; j <= m; j++) { // 初始化第一行
            d[0][j] = j;
        }
        for (i = 1; i <= n; i++) { // 遍历str1
            ch1 = str1.charAt(i - 1);
            // 去匹配str2
            for (j = 1; j <= m; j++) {
                ch2 = str2.charAt(j - 1);
                if (ch1 == ch2) {
                    temp = 0;
                } else {
                    temp = 1;
                }
                // 左边+1,上边+1, 左上角+temp取最小
                d[i][j] = min(d[i - 1][j] + 1, d[i][j - 1] + 1, d[i - 1][j - 1] + temp);
            }
        }
        return d[n][m];
    }

    public static Map<String, String> ld(String sourceStr, Map<String, String> dicMap) {
        Map<String, String> minLdResult = new HashMap();
        int minLd = 10;
        String key = "";
        String value = sourceStr;
        for (String k : dicMap.keySet()) {
            int ld = ld(sourceStr, k);
            if (minLd >= ld) {
                minLd = ld;
                key = dicMap.get(k);
                value = k;
            }
        }
        minLdResult.put("key", key);
        minLdResult.put("value", value);
        minLdResult.put("ld", String.valueOf(minLd));
        return minLdResult;
    }

    /**
     * 切割地址
     *
     * @return
     */
    public static Map<String, String> splitAddr(String sourceStr) throws Exception{
        Map<String, String> result = new HashMap<String, String>();
        Map<String, String> dicMap = getDicMap();
        int maxLen = sourceStr.length();
        int begin = 0;
        for (int end = begin + 1; end < maxLen; end++) {
            String keyStr = sourceStr.substring(begin, end);
            if (dicMap.get(keyStr) != null) {
                begin = end;
                result.put(dicMap.get(keyStr), keyStr);
                result.put(dicMap.get(keyStr) + UNDER_STR, keyStr);
            } else {
                for (String key : keywordMap.keySet()) {
                    String k = keyStr + key;
                    if (dicMap.get(k) != null) {
                        String k_ = sourceStr.substring(begin, end + key.length());
                        if (dicMap.get(k_) != null) {
                            end = end + key.length();
                            begin = end;
                            result.put(dicMap.get(k_), k);
                            result.put(dicMap.get(k_) + UNDER_STR, k_);
                        } else {
                            begin = end;
                            result.put(dicMap.get(k), k);
                            result.put(dicMap.get(k) + UNDER_STR, keyStr);
                        }
                        break;
                    }
                }
            }
        }
        result = formatAddress(sourceStr, result);
        result.put(ADD_COL + UNDER_STR, sourceStr);
        return result;
    }

    private static Map<String, String> formatAddress(String sourceStr, Map<String, String> addressMap) {
        String match = Common.checkNull(addressMap.get(PROVINCE_COL)) + Common.checkNull(addressMap.get(CITY_COL))
                + Common.checkNull(addressMap.get(DISTRICT_COL)) + Common.checkNull(addressMap.get(TOWN_COL))
                + Common.checkNull(addressMap.get(STREET_COL)) + Common.checkNull(addressMap.get(DOORPN_COL));
        String match_ = Common.checkNull(addressMap.get(PROVINCE_COL + UNDER_STR)) + Common.checkNull(addressMap.get(CITY_COL + UNDER_STR))
                + Common.checkNull(addressMap.get(DISTRICT_COL + UNDER_STR)) + Common.checkNull(addressMap.get(TOWN_COL + UNDER_STR))
                + Common.checkNull(addressMap.get(STREET_COL + UNDER_STR)) + Common.checkNull(addressMap.get(DOORPN_COL + UNDER_STR));
        String unmatch = sourceStr.substring(sourceStr.indexOf(match_) + match_.length());
        addressMap.put(MATCH_COL, match);
        addressMap.put(MATCH_COL + UNDER_STR, match_);
        addressMap.put(UNMATCH_COL + UNDER_STR, unmatch);
        return addressMap;
    }

    public static void main(String[] args) throws Exception{
        String str = "香洲拱北九洲大道西1043号北岭社区警务室";
        Map<String, String> addressMap = splitAddr(str);
        String tablenames = ConfigProperties.getByKey("dmdz.tablename");
        String dataSourceId = ConfigProperties.getByKey("dmdz.datasourceid");
        String[] tablenameArr = tablenames.split(",");
        List<Map> result = null;
        List<Map> rsList = new ArrayList();
        for (int i = 0; i < tablenameArr.length; i++) {
            StringBuffer sql = new StringBuffer(" select sde.st_astext(t.shape) wkt,t.* from ").append(tablenameArr[i]).append(" t ").append(" where 1=1 ");
            if (addressMap.get(PROVINCE_COL) != null) {
                sql.append("and ").append(PROVINCE_COL).append("='").append(addressMap.get(PROVINCE_COL)).append("' ");
            }
            if (addressMap.get(CITY_COL) != null) {
                sql.append("and ").append(CITY_COL).append("='").append(addressMap.get(CITY_COL)).append("' ");
            }
            if (addressMap.get(DISTRICT_COL) != null) {
                sql.append("and ").append(DISTRICT_COL).append("='").append(addressMap.get(DISTRICT_COL)).append("' ");
            }
            if (addressMap.get(TOWN_COL) != null) {
                sql.append("and ").append(TOWN_COL).append("='").append(addressMap.get(TOWN_COL)).append("' ");
            }
            if (addressMap.get(STREET_COL) != null) {
                sql.append("and ").append(STREET_COL).append("='").append(addressMap.get(STREET_COL)).append("' ");
            }
            if (addressMap.get(DOORPN_COL) != null) {
                sql.append("and ").append(DOORPN_COL).append(" like '").append(addressMap.get(DOORPN_COL)).append("%' ");
            }
            List<Map> list = DBHelper.find(dataSourceId, sql.toString(), null);
            setLd(list, addressMap, rsList);
        }
        System.out.println(JsonUtils.toJson(addressMap));
    }

    /**
     * 设置编辑距离
     *
     * @return
     */
    public static List<Map> setLd(List<Map> list, Map<String, String> addressMap, List<Map> rsList) {
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                Map map = list.get(i);
                if (map.get(ADD_COL) != null) {
                    String addname = map.get(ADD_COL) + "";
                    String matchCol_ = addressMap.get(MATCH_COL + UNDER_STR);
                    String unMatchCol = addressMap.get(UNMATCH_COL);
                    String matchCol = addressMap.get(MATCH_COL);
                    String tmpStr = addname.substring(addname.indexOf(matchCol) + matchCol.length());
                    int ld = ld(unMatchCol, tmpStr);
                    map.put(SEMB_COL, ld);
                    rsList.add(map);
                }
            }
            return rsList;
        }
        return null;
    }
}
