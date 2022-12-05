package com.augurit.agcloud.agcom.agsupport.sc.address.dao;

import com.augurit.agcloud.agcom.agsupport.sc.address.config.AgAddressConfig;
import com.augurit.agcloud.agcom.agsupport.sc.address.domain.AgDmdz;
import com.augurit.agcloud.agcom.agsupport.sc.address.util.AddressMap;
import com.augurit.agcloud.agcom.agsupport.sc.address.util.AddressUtil;
import com.common.dbcp.DBHelper;
import com.common.dbcp.DBPool;
import com.common.thread.Job;
import com.common.util.Common;
import com.common.util.ConfigProperties;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

import static com.augurit.agcloud.agcom.agsupport.sc.spatial.config.SpatialConfig.WKT_COLUMN;

/**
 * Created by Administrator on 2017-07-06.
 */
@Repository
public class AgAddressDao {

    public int save(JSONObject addrJSON) {
        String tablename = ConfigProperties.getByKey("dmdz.tablename");
        String dataSourceId = ConfigProperties.getByKey("dmdz.datasourceid");
        String id = UUID.randomUUID().toString();
        String x = addrJSON.get("x") + "";
        String y = addrJSON.get("y") + "";
        String name = addrJSON.get("name") + "";
        String tel = addrJSON.get("tel") + "";
        String addname = addrJSON.get("addname") + "";
        String city = addrJSON.get("city") + "";
        String province = addrJSON.get("province") + "";
        String wkt = getWktOfPoint(x, y);
        String sql = "insert into " + tablename + "(id,shape,province,city,name,addname,tel,x,y) values('" + id + "',st_geomfromtext('" + wkt + "'),'" +
                province + "','" + city + "','" + name + "','" + addname + "','" + tel + "'," + x + "," + y + ") ";
        Connection con = DBPool.getConnection(dataSourceId);
        PreparedStatement pstm = null;
        try {
            pstm = con.prepareStatement(sql);
            int i = pstm.executeUpdate();
            return i;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (pstm != null) {
                    pstm.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            DBPool.close(con);
        }
        return 0;
    }

    /**
     * 获取点的wkt
     *
     * @param x
     * @param y
     * @return
     */
    public String getWktOfPoint(String x, String y) {
        return "POINT(" + x + " " + y + ")";
    }

    /**
     * 获取某表的坐标系id
     *
     * @param tablename
     * @return
     */
    public String getSrsid(String tablename) {
//        String sql = "select b.SRS_NAME,b.SRS_ID srsid from db2gse.GSE_GEOMETRY_COLUMNS a,db2gse.GSE_SPATIAL_REFERENCE_SYSTEMS b " +
//                "where a.SRS_NAME = b.SRS_NAME and a.table_name='"+tablename.toUpperCase()+"' ";
//        String dataSourceId = "sde_db2";
//        Map data = DBHelper.findFirst(dataSourceId, sql, null);
//        if(data!=null){
//            return data.get("srsid")+"";
//        }
        return null;
    }

    /**
     * 查询地址查相关信息
     *
     * @param keyWord
     * @return
     */
    public List<AgDmdz> search(String keyWord, String cityKey)  throws Exception {
        String tablename = ConfigProperties.getByKey("dmdz.tablename");
        String dataSourceId = ConfigProperties.getByKey("dmdz.datasourceid");
        List<AgDmdz> dmdzList = new ArrayList();
        if (keyWord != null && !keyWord.trim().equals("")) {
            String sql = "select id,province,city,name,addname,tel,shape,x,y from " + tablename +
                    " where name like ? or addname like ? ";
            keyWord = "%" + keyWord + "%";
            keyWord = keyWord.toUpperCase();
            List values = new ArrayList();
            values.add(keyWord);
            values.add(keyWord);
            dmdzList = DBHelper.find(dataSourceId, AgDmdz.class, sql, values);
        }
        return dmdzList;
    }

    public static void main(String[] args) {
        AgAddressDao dd = new AgAddressDao();
    }

    public List findAddr(String name, String addname, String city, String province)  throws Exception {
        String tablename = ConfigProperties.getByKey("dmdz.tablename");
        String dataSourceId = ConfigProperties.getByKey("dmdz.datasourceid");
        String sql = "select id,province,city,name,addname,tel,shape,x,y from " + tablename + " where 1=1 ";
        boolean flag = false;
        if (!name.equals("null")) {
            flag = true;
            sql += "and name='" + name + "'";
        }
        if (!addname.equals("null")) {
            flag = true;
            sql += "and addname='" + addname + "'";
        }
        if (!city.equals("null")) {
            flag = true;
            sql += "and city='" + city + "'";
        }
        if (!province.equals("null")) {
            flag = true;
            sql += "and province='" + province + "'";
        }
        if (!flag) {
            return null;
        }
        List<Map> list = DBHelper.find(dataSourceId, sql, null);
        return list;
    }

    /**
     * 根据列名获取列的不同值
     *
     * @param colName 列名
     * @return
     */
    public Map<String, List<Map>> getDistinctValOfCol(String colName)  throws Exception {
        String tablenames = ConfigProperties.getByKey("dmdz.tablename");
        String dataSourceId = ConfigProperties.getByKey("dmdz.datasourceid");
        String[] tablenameArr = tablenames.split(AgAddressConfig.SPLIT_STR);
        Map<String, List<Map>> rsMap = new HashMap<>();
        for (int i = 0; i < tablenameArr.length; i++) {
            String sql = "select distinct " + colName + " from ";
            sql += tablenameArr[i];
            List<Map> list = DBHelper.find(dataSourceId, sql, null);
            rsMap.put(tablenameArr[i], list);
        }
        return rsMap;
    }

    /**
     * 查找地址信息
     *
     * @param str 需要匹配的地址
     * @return
     */
    public List<Map> findAgDmdz(String str)  throws Exception {
        final Map<String, String> addressMap = AddressUtil.splitAddr(str);
        String tablenames = ConfigProperties.getByKey("dmdz.tablename");
        String dataSourceId = ConfigProperties.getByKey("dmdz.datasourceid");
        String[] tablenameArr = tablenames.split(AgAddressConfig.SPLIT_STR);
        List<Map> resultList = new ArrayList();
        List<AddressMap> rsList = new ArrayList();
        for (int i = 0; i < tablenameArr.length; i++) {
            StringBuffer sql = new StringBuffer(" select sde.st_astext(t.shape) ").append(WKT_COLUMN).append(",t.* from ")
                    .append(tablenameArr[i]).append(" t where 1=1 ");
            if (addressMap.get(AgAddressConfig.PROVINCE_COL) != null) {
                sql.append("and ").append(AgAddressConfig.PROVINCE_COL).append("='").append(addressMap.get(AgAddressConfig.PROVINCE_COL)).append("' ");
            }
            if (addressMap.get(AgAddressConfig.CITY_COL) != null) {
                sql.append("and ").append(AgAddressConfig.CITY_COL).append("='").append(addressMap.get(AgAddressConfig.CITY_COL)).append("' ");
            }
            if (addressMap.get(AgAddressConfig.DISTRICT_COL) != null) {
                sql.append("and ").append(AgAddressConfig.DISTRICT_COL).append("='").append(addressMap.get(AgAddressConfig.DISTRICT_COL)).append("' ");
            }
            if (addressMap.get(AgAddressConfig.TOWN_COL) != null) {
                sql.append("and ").append(AgAddressConfig.TOWN_COL).append("='").append(addressMap.get(AgAddressConfig.TOWN_COL)).append("' ");
            }
            if (addressMap.get(AgAddressConfig.STREET_COL) != null) {
                sql.append("and ").append(AgAddressConfig.STREET_COL).append("='").append(addressMap.get(AgAddressConfig.STREET_COL)).append("' ");
            }
            if (addressMap.get(AgAddressConfig.DOORPN_COL) != null) {
                sql.append("and ").append(AgAddressConfig.DOORPN_COL).append(" like '").append(addressMap.get(AgAddressConfig.DOORPN_COL)).append("%' ");
            }
            //List<Map> dataList = DBHelper.find(dataSourceId, sql.toString(), null);
            DBHelper.each(dataSourceId, sql.toString(), null, AgAddressConfig.QUERY_LIMIT, new Job(rsList) {
                @Override
                public Object execute() {
                    Map data = (Map) getData()[0];
                    if (data.get(AgAddressConfig.SHAPE_COL) != null) {
                        data.put(AgAddressConfig.SHAPE_COL, null);
                    }
                    List<AddressMap> rsList = (List<AddressMap>) getArgs()[0];
                    AddressMap map = new AddressMap(data);
                    if (map.get(AgAddressConfig.ADD_COL) != null) {
                        int semb = 0;
                        String unMatch = Common.checkNull(map.get(AgAddressConfig.ADD_COL));
                        //计算省字段相似度
                        if (addressMap.get(AgAddressConfig.PROVINCE_COL) != null && map.get(AgAddressConfig.PROVINCE_COL) != null) {
                            String v1 = map.get(AgAddressConfig.PROVINCE_COL).toString();
                            String v2 = addressMap.get(AgAddressConfig.PROVINCE_COL).toString();
                            int ld = AddressUtil.ld(v1, v2);
                            semb += ld * AgAddressConfig.PROVINCE_COL_W;
                            unMatch = unMatch.substring(unMatch.indexOf(v1) + v1.length());
                        }
                        //计算市字段相似度
                        if (addressMap.get(AgAddressConfig.CITY_COL) != null && map.get(AgAddressConfig.CITY_COL) != null) {
                            String v1 = map.get(AgAddressConfig.CITY_COL).toString();
                            String v2 = addressMap.get(AgAddressConfig.CITY_COL).toString();
                            int ld = AddressUtil.ld(v1, v2);
                            semb += ld * AgAddressConfig.CITY_COL_W;
                            unMatch = unMatch.substring(unMatch.indexOf(v1) + v1.length());
                        }
                        //计算区或县字段相似度
                        if (addressMap.get(AgAddressConfig.DISTRICT_COL) != null && map.get(AgAddressConfig.DISTRICT_COL) != null) {
                            String v1 = map.get(AgAddressConfig.DISTRICT_COL).toString();
                            String v2 = addressMap.get(AgAddressConfig.DISTRICT_COL).toString();
                            int ld = AddressUtil.ld(v1, v2);
                            semb += ld * AgAddressConfig.DISTRICT_COL_W;
                            unMatch = unMatch.substring(unMatch.indexOf(v1) + v1.length());
                        }
                        //计算镇字段相似度
                        if (addressMap.get(AgAddressConfig.TOWN_COL) != null && map.get(AgAddressConfig.TOWN_COL) != null) {
                            String v1 = map.get(AgAddressConfig.TOWN_COL).toString();
                            String v2 = addressMap.get(AgAddressConfig.TOWN_COL).toString();
                            int ld = AddressUtil.ld(v1, v2);
                            semb += ld * AgAddressConfig.TOWN_COL_W;
                            unMatch = unMatch.substring(unMatch.indexOf(v1) + v1.length());
                        }
                        //计算村字段相似度
                        if (addressMap.get(AgAddressConfig.VILLAGE_COL) != null && map.get(AgAddressConfig.VILLAGE_COL) != null) {
                            String v1 = map.get(AgAddressConfig.VILLAGE_COL).toString();
                            String v2 = addressMap.get(AgAddressConfig.VILLAGE_COL).toString();
                            int ld = AddressUtil.ld(v1, v2);
                            semb += ld * AgAddressConfig.VILLAGE_COL_W;
                            unMatch = unMatch.substring(unMatch.indexOf(v1) + v1.length());
                        }
                        //计算街道字段相似度
                        if (addressMap.get(AgAddressConfig.STREET_COL) != null && map.get(AgAddressConfig.STREET_COL) != null) {
                            String v1 = map.get(AgAddressConfig.STREET_COL).toString();
                            String v2 = addressMap.get(AgAddressConfig.STREET_COL).toString();
                            int ld = AddressUtil.ld(v1, v2);
                            semb += ld * AgAddressConfig.STREET_COL_W;
                            unMatch = unMatch.substring(unMatch.indexOf(v1) + v1.length());
                        }
                        //计算门牌号字段相似度
                        if (addressMap.get(AgAddressConfig.DOORPN_COL) != null && map.get(AgAddressConfig.DOORPN_COL) != null) {
                            String v1 = map.get(AgAddressConfig.DOORPN_COL).toString();
                            String v2 = addressMap.get(AgAddressConfig.DOORPN_COL).toString();
                            int ld = AddressUtil.ld(v1, v2);
                            semb += ld * AgAddressConfig.DOORPN_COL_W;
                            unMatch = unMatch.substring(unMatch.indexOf(v1) + v1.length());
                        }
                        //计算剩余未匹配相似度
                        String unmatch_ = addressMap.get(AgAddressConfig.UNMATCH_COL + AgAddressConfig.UNDER_STR).toString();
                        int ld = AddressUtil.ld(unMatch, unmatch_);
                        semb += ld * AgAddressConfig.UNMATCH_COL_W;
                        map.put(AgAddressConfig.SEMB_COL, semb);
                        if (semb < AgAddressConfig.MAX_SEMB || unMatch.indexOf(unmatch_) >= 0) {
                            rsList.add(map);
                        }
                    }
                    return null;
                }
            });
        }
        Collections.sort(rsList);
        resultList.addAll(rsList);
        return resultList;
    }

    /**
     * 设置编辑距离
     *
     * @return
     */
    public static List<AddressMap> setSemblance(List<Map> dataList, Map<String, String> addressMap, List<AddressMap> resultList) {
        if (dataList != null && dataList.size() > 0) {
            for (int i = 0; i < dataList.size(); i++) {
                Map dataMap = dataList.get(i);
                AddressMap map = new AddressMap(dataMap);
                if (map.get(AgAddressConfig.ADD_COL) != null) {
                    int semb = 0;
                    String unMatch = Common.checkNull(map.get(AgAddressConfig.ADD_COL));
                    //计算省字段相似度
                    if (addressMap.get(AgAddressConfig.PROVINCE_COL) != null && map.get(AgAddressConfig.PROVINCE_COL) != null) {
                        String v1 = map.get(AgAddressConfig.PROVINCE_COL).toString();
                        String v2 = addressMap.get(AgAddressConfig.PROVINCE_COL).toString();
                        int ld = AddressUtil.ld(v1, v2);
                        semb += ld * AgAddressConfig.PROVINCE_COL_W;
                        unMatch = unMatch.substring(unMatch.indexOf(v1) + v1.length());
                    }
                    //计算市字段相似度
                    if (addressMap.get(AgAddressConfig.CITY_COL) != null && map.get(AgAddressConfig.CITY_COL) != null) {
                        String v1 = map.get(AgAddressConfig.CITY_COL).toString();
                        String v2 = addressMap.get(AgAddressConfig.CITY_COL).toString();
                        int ld = AddressUtil.ld(v1, v2);
                        semb += ld * AgAddressConfig.CITY_COL_W;
                        unMatch = unMatch.substring(unMatch.indexOf(v1) + v1.length());
                    }
                    //计算区或县字段相似度
                    if (addressMap.get(AgAddressConfig.DISTRICT_COL) != null && map.get(AgAddressConfig.DISTRICT_COL) != null) {
                        String v1 = map.get(AgAddressConfig.DISTRICT_COL).toString();
                        String v2 = addressMap.get(AgAddressConfig.DISTRICT_COL).toString();
                        int ld = AddressUtil.ld(v1, v2);
                        semb += ld * AgAddressConfig.DISTRICT_COL_W;
                        unMatch = unMatch.substring(unMatch.indexOf(v1) + v1.length());
                    }
                    //计算镇字段相似度
                    if (addressMap.get(AgAddressConfig.TOWN_COL) != null && map.get(AgAddressConfig.TOWN_COL) != null) {
                        String v1 = map.get(AgAddressConfig.TOWN_COL).toString();
                        String v2 = addressMap.get(AgAddressConfig.TOWN_COL).toString();
                        int ld = AddressUtil.ld(v1, v2);
                        semb += ld * AgAddressConfig.TOWN_COL_W;
                        unMatch = unMatch.substring(unMatch.indexOf(v1) + v1.length());
                    }
                    //计算村字段相似度
                    if (addressMap.get(AgAddressConfig.VILLAGE_COL) != null && map.get(AgAddressConfig.VILLAGE_COL) != null) {
                        String v1 = map.get(AgAddressConfig.VILLAGE_COL).toString();
                        String v2 = addressMap.get(AgAddressConfig.VILLAGE_COL).toString();
                        int ld = AddressUtil.ld(v1, v2);
                        semb += ld * AgAddressConfig.VILLAGE_COL_W;
                        unMatch = unMatch.substring(unMatch.indexOf(v1) + v1.length());
                    }
                    //计算街道字段相似度
                    if (addressMap.get(AgAddressConfig.STREET_COL) != null && map.get(AgAddressConfig.STREET_COL) != null) {
                        String v1 = map.get(AgAddressConfig.STREET_COL).toString();
                        String v2 = addressMap.get(AgAddressConfig.STREET_COL).toString();
                        int ld = AddressUtil.ld(v1, v2);
                        semb += ld * AgAddressConfig.STREET_COL_W;
                        unMatch = unMatch.substring(unMatch.indexOf(v1) + v1.length());
                    }
                    //计算门牌号字段相似度
                    if (addressMap.get(AgAddressConfig.DOORPN_COL) != null && map.get(AgAddressConfig.DOORPN_COL) != null) {
                        String v1 = map.get(AgAddressConfig.DOORPN_COL).toString();
                        String v2 = addressMap.get(AgAddressConfig.DOORPN_COL).toString();
                        int ld = AddressUtil.ld(v1, v2);
                        semb += ld * AgAddressConfig.DOORPN_COL_W;
                        unMatch = unMatch.substring(unMatch.indexOf(v1) + v1.length());
                    }
                    //计算剩余未匹配相似度
                    if (addressMap.get(AgAddressConfig.UNMATCH_COL + AgAddressConfig.UNDER_STR) != null) {
                        String unmatch_ = addressMap.get(AgAddressConfig.UNMATCH_COL + AgAddressConfig.UNDER_STR).toString();
                        int ld = AddressUtil.ld(unMatch, unmatch_);
                        semb += ld * AgAddressConfig.UNMATCH_COL_W;
                    }
                    map.put(AgAddressConfig.SEMB_COL, semb);
                    if (semb < AgAddressConfig.MAX_SEMB) {
                        resultList.add(map);
                    }
                }
                if (map.get(AgAddressConfig.SHAPE_COL) != null) {
                    map.put(AgAddressConfig.SHAPE_COL, null);
                }
            }
        }
        Collections.sort(resultList);
        return resultList;
    }
}
