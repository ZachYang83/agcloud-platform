package com.augurit.agcloud.agcom.agsupport.sc.address.config;

import com.augurit.agcloud.agcom.agsupport.sc.address.dao.AgAddressDao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017-09-27.
 */
public class AgAddressConfig {

    public static final int MIN_LD = 1;//最小编辑距离
    public static final int MAX_SEMB = 10;//最大相似度
    public static final int QUERY_LIMIT = 1000;//查询数目限制

    public static final String SHAPE_COL = "shape";//未匹配地址

    public static final String SPLIT_STR = ",";//分割符
    public static final String UNDER_STR = "_";//下划线
    public static final String PROVINCE_COL = "province";//省字段
    public static final String CITY_COL = "city";//市字段
    public static final String DISTRICT_COL = "district";//区或县字段
    public static final String TOWN_COL = "town";//镇字段
    public static final String VILLAGE_COL = "village";//村字段
    public static final String STREET_COL = "street";//街道字段
    public static final String DOORPN_COL = "doorpn";//门牌号字段
    public static final int PROVINCE_COL_W = 4;//省字段权重
    public static final int CITY_COL_W = 4;//市字段权重
    public static final int DISTRICT_COL_W = 4;//区或县字段权重
    public static final int TOWN_COL_W = 3;//镇字段权重
    public static final int VILLAGE_COL_W = 3;//村字段权重
    public static final int STREET_COL_W = 2;//街道字段权重
    public static final int DOORPN_COL_W = 2;//门牌号字段权重
    public static final int UNMATCH_COL_W = 1;//未匹配权重
    public static final String ADD_COL = "addname";//地址字段
    public static final String MATCH_COL = "match";//已匹配地址
    public static final String UNMATCH_COL = "unmatch";//未匹配地址
    public static final String SEMB_COL = "semblance_val";//未匹配地址
    private static Map<String, String> dicMap = null;
    public static Map<String, String> keywordMap = null;

    /**
     * 初始化词典
     *
     * @return
     */
    public static void initDic() throws Exception{
        if (dicMap == null) {
            dicMap = new HashMap<String, String>();
            keywordMap = new HashMap<String, String>();
            AgAddressDao agAddressDao = new AgAddressDao();
            Map<String, List<Map>> rsMapProvince = agAddressDao.getDistinctValOfCol(PROVINCE_COL);
            loadDic(rsMapProvince, PROVINCE_COL);
            Map<String, List<Map>> rsMapCity = agAddressDao.getDistinctValOfCol(CITY_COL);
            loadDic(rsMapCity, CITY_COL);
            Map<String, List<Map>> rsMapDistrict = agAddressDao.getDistinctValOfCol(DISTRICT_COL);
            loadDic(rsMapDistrict, DISTRICT_COL);
            Map<String, List<Map>> rsMapTown = agAddressDao.getDistinctValOfCol(TOWN_COL);
            loadDic(rsMapTown, TOWN_COL);
            Map<String, List<Map>> rsMapVillage = agAddressDao.getDistinctValOfCol(VILLAGE_COL);
            loadDic(rsMapVillage, VILLAGE_COL);
            Map<String, List<Map>> rsMapStreet = agAddressDao.getDistinctValOfCol(STREET_COL);
            loadDic(rsMapStreet, STREET_COL);
            Map<String, List<Map>> rsMapDoorpn = agAddressDao.getDistinctValOfCol(DOORPN_COL);
            loadDic(rsMapDoorpn, DOORPN_COL);
            keywordMap.put("省", "省");
            keywordMap.put("市", "市");
            keywordMap.put("区", "区");
            keywordMap.put("县", "县");

            keywordMap.put("镇", "镇");
            keywordMap.put("街道", "街道");

            keywordMap.put("社区", "社区");
            keywordMap.put("村", "村");

            keywordMap.put("巷", "巷");
            keywordMap.put("路", "路");

            //keywordMap.put("号", "号");
        }
    }

    public static Map<String, String> getDicMap() throws Exception{
        initDic();
        return dicMap;
    }

    /**
     * 装载词典
     *
     * @param rsMap   结果集合
     * @param colName 列名
     */
    public static void loadDic(Map<String, List<Map>> rsMap, String colName) {
        for (String key : rsMap.keySet()) {
            List<Map> list = rsMap.get(key);
            for (int i = 0; i < list.size(); i++) {
                Map map = list.get(i);
                String rsStr = map.get(colName) + "";
                if (!rsStr.trim().equals("") && !rsStr.equals("null")) {
                    dicMap.put(rsStr, colName);
                }
            }
        }
    }


}
