package com.augurit.agcloud.agcom.agsupport.sc.address.util;

import com.common.util.Common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.augurit.agcloud.agcom.agsupport.sc.address.config.AgAddressConfig.*;

/**
 * Created by Administrator on 2017-09-25.
 */
public class SegmentUtil {
//    public static String SPLIT_STR = ",";//省字段
//    public static String PROVINCE_COL = "provinceCol";//省字段
//    public static String CITY_COL = "cityCol";//市字段
//    public static String DISTRICT_COL = "districtCol";//区或县字段
//    public static String TOWN_COL = "townCol";//镇字段
//    public static String STREET_COL = "streetCol";//街道字段
//    public static String VILLAGE_COL = "villageCol";//村字段
//    public static String DOORPN_COL = "doorpnCol";//门牌号字段
//    public static String TMP_COL = "tmpCol";//临时字段
//    public static Map<String, String> keywordMap = new HashMap<String, String>();
//
//    /**
//     * 初始化关键字
//     * @return
//     */
//    static {
//        keywordMap.put("省", "省" + SPLIT_STR + PROVINCE_COL);
//        keywordMap.put("市", "市" + SPLIT_STR + CITY_COL);
//        keywordMap.put("区", "区" + SPLIT_STR + DISTRICT_COL);
//        keywordMap.put("县", "县" + SPLIT_STR + DISTRICT_COL);
//
//        keywordMap.put("镇", "镇" + SPLIT_STR + TOWN_COL);
//        keywordMap.put("街", "街道" + SPLIT_STR + TOWN_COL);
//
//        keywordMap.put("社", "社区" + SPLIT_STR + VILLAGE_COL);
//        keywordMap.put("村", "村" + SPLIT_STR + VILLAGE_COL);
//
//        keywordMap.put("巷", "巷" + SPLIT_STR + STREET_COL);
//        keywordMap.put("路", "路" + SPLIT_STR + STREET_COL);
//
//        keywordMap.put("号", "号" + SPLIT_STR + DOORPN_COL);
//    }

    /**
     * 排除特殊字符
     *
     * @return
     */
    public static String exceptSpecialStr(String str) {
        Pattern p = Pattern.compile(",|，|\\.");
        Matcher m = p.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            m.appendReplacement(sb, "");
        }
        m.appendTail(sb);
        return sb.toString();
    }

    /**
     * 初始化词典
     *
     * @return
     */
    public static int initDic(Map dicMap) {
        dicMap.put("香州区", "香州区");
        dicMap.put("金湾区", "金湾区");
        dicMap.put("斗门区", "斗门区");
        dicMap.put("珠海市", "珠海市");
        dicMap.put("湖南", "湖南");
        dicMap.put("湖南省", "湖南省");
        dicMap.put("长沙", "长沙");
        dicMap.put("芙蓉", "芙蓉");
        dicMap.put("芙蓉区", "芙蓉区");
        return 0;
    }

    /**
     * 正向最大匹配法
     *
     * @return
     */
    public static List Fmm(String source, Map dicMap, Map<String, String> keywordMap) {
        String tem = null;
        List<String> fcList = new ArrayList();//fmm算法
        List<Boolean> dxcsxList = new ArrayList();//当下词属性集合
        int MaxLen = source.length();
        int temLen = MaxLen;
        int primarylen = 0;
        while (true) {
            tem = source.substring(primarylen, temLen);
            if (dicMap.containsKey(tem) || temLen - primarylen == 1) {
                primarylen = temLen;
                temLen = MaxLen;
                fcList.add(tem);
                String lastC = tem.substring(tem.length() - 1);
                if (keywordMap.containsKey(lastC)) {
                    dxcsxList.add(true);
//                    String val = keywordMap.get(lastC);
//                    if(val.length()>1){
//
//                    }
                    if (dxcsxList.size() > 1) {
                        dxcsxList.set(dxcsxList.size() - 2, false);
                    }
                } else {
                    dxcsxList.add(false);
                }
            } else {
                temLen--;
            }
            if (primarylen == MaxLen) {
                break;
            }
        }
        String tmpStr = "";
        List<String> rsList = new ArrayList();
        for (int i = 0; i < fcList.size(); i++) {
            String fc = fcList.get(i);
            tmpStr += fc;
            Boolean sx = dxcsxList.get(i);
            if (sx == true) {
                System.out.println(tmpStr);
                rsList.add(tmpStr);
                tmpStr = "";
            }
        }
        return rsList;
    }

    /**
     * 切割地址
     *
     * @return
     */
//    public static Map cutAddr(String sourceStr) {
//        Map<String, String> colMap = new HashMap<String, String>();
//        int maxLen = sourceStr.length();
//        int k = 0;
//        for (int i = 0; i < maxLen; i++) {
//            String keyStr = sourceStr.charAt(i) + "";
//            if (keywordMap.containsKey(keyStr)) {
//                String val = keywordMap.get(keyStr);
//                String keyword = val.split(SPLIT_STR)[0];
//                String colName = val.split(SPLIT_STR)[1];
//                if (keyword.length() > 1) {
//                    String tmpStr = sourceStr.substring(i, i + keyword.length());
//                    if (tmpStr.equals(keyword)) {
//                        i = i + keyword.length() - 1;
//                    }
//                }
//                if (colMap.get(colName) != null) {
//                    String tmp = colMap.get(colName) + sourceStr.substring(k, i + 1);
//                    colMap.put(colName, tmp);
//                } else {
//                    colMap.put(colName, sourceStr.substring(k, i + 1));
//                }
//                k = i + 1;
//            }
//        }
//        if (k < maxLen) {
//            String str = sourceStr.substring(k);
//            colMap.put(TMP_COL, str);
//        }
//        return colMap;
//    }

    public static void main(String[] args) throws Exception{
        String str = "广珠海市斗门区井岸镇统建社区居委会统建二巷13号";
        //Map dicMap = AgAddressConfig.dicMap;
//        initDic(dicMap);
//        Fmm(str,dicMap,keywordMap);
//        System.out.println(exceptSpecialStr(str));
//        cutAddr(str);
        splitAddr(str);
        String str1 = "镇统建社区居委会";
        System.out.println(str.substring(str.indexOf(str1) + str1.length()));
        System.out.println(str.indexOf("广珠海市斗门区井岸镇统建社区居委会统建"));
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
            Map<String, String> map = SimFeatureUtil.ld(keyStr, dicMap);
            int ld = Integer.valueOf(map.get("ld"));
            if (ld <= MIN_LD) {
                String keyStr_ = sourceStr.substring(begin, end + 1);
                Map<String, String> map_ = SimFeatureUtil.ld(keyStr_, dicMap);
                int ld_ = Integer.valueOf(map_.get("ld"));
                if (ld_ < ld) {
                    end++;
                    begin = end + 1;
                    result.put(map_.get("key"), map_.get("value"));
                } else {
                    begin = end + 1;
                    result.put(map_.get("key"), map_.get("value"));
                }
            }
        }
        result = formatAddress(sourceStr, result);
        return result;
    }

    private static Map<String, String> formatAddress(String sourceStr, Map<String, String> addressMap) {
        String match = Common.checkNull(addressMap.get(PROVINCE_COL)) + Common.checkNull(addressMap.get(CITY_COL))
                + Common.checkNull(addressMap.get(DISTRICT_COL)) + Common.checkNull(addressMap.get(TOWN_COL))
                + Common.checkNull(addressMap.get(STREET_COL));
        String unmatch = sourceStr.substring(sourceStr.indexOf(match) + match.length());
        addressMap.put(MATCH_COL, match);
        addressMap.put(UNMATCH_COL, unmatch);
        return addressMap;
    }


}
