package com.augurit.agcloud.agcom.agsupport.common.util;

import com.augurit.agcloud.agcom.agsupport.util.Sim;
import com.common.dbcp.DBHelper;
import com.common.dbcp.DBPool;
import com.common.thread.Job;
import com.common.util.Common;
import net.sf.json.JSONArray;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Pattern;

/**
 * 关联上标准地址库工具类
 * 地址相似度比较
 * 地址分段查询
 * 分页数据查询
 * 多线程数据处理
 * Author : 韩勤
 * CreateDate ：2017-9-3
 * Copyright: 广州奥格智能科技有限公司
 */

/**
 * 补充：修改匹配相似地址的算法，返回前十个匹配率高且去重的结果
 * 此方法用于广州水务公共信息平台
 * czh.2018-1-31
 */
public class SwLocateUtil {

    private String st_table;

    private String st_column;

    private String datasourceId;

    private String srid;

    private static LocatePattern pattern = null;

    public SwLocateUtil(String datasourceId, String st_table, String st_column) {
        this.st_table = st_table;
        this.st_column = st_column;
        this.datasourceId = datasourceId;
        if (pattern == null) {
            pattern = new LocatePattern();
        }
    }

    private void catt(StringBuffer sb, String address, String key, Pattern regex) {
        if (address.contains(key)) {
            String str = regex.matcher(address).replaceAll("$1");
            if (str.length() > 0) {
                sb.append("or " + st_column + " like '%" + str + "%' ");
            }
        }
    }

    /**
     * 分段组装条件语句
     *
     * @return
     */
    private String appendSplitWhere(String address) {
        StringBuffer sb = new StringBuffer();
        for (Iterator it = pattern.iterator(); it.hasNext(); ) {
            Map.Entry entry = (Map.Entry) it.next();
            String key = (String) entry.getKey();
            Object[] array = (Object[]) entry.getValue();
            Pattern pattern = (Pattern) array[0];
            catt(sb, address, key, pattern);
        }
        if (sb.length() > 0) {
            String where = "and (" + sb.substring(2) + ") ";
            return where;
        } else {
            return "";
        }
    }

    /**
     * 改良的找相似地址算法
     *
     * @param address 返回 id、st_address、score的json结构
     */
    public String same(final String address) throws Exception {
        String where = appendSplitWhere(address);
        String sql = "select sde.st_astext(SHAPE) wkt," + st_column + " from " + st_table + " where 1=1 " + where;
        Map result = new HashMap();
        final CopyOnWriteArrayList<Map> list = new CopyOnWriteArrayList<Map>(); //防止多线程对list进行操作抛出异常
        DBHelper.each(datasourceId, sql, null, 1000, new Job(0, result) {
            @Override
            public Object execute() {
                Map data = (Map) this.getData()[0];
                String st_address = Common.checkNull(data.get(st_column));
                int score = Sim.same(address, st_address);
                int minScore = (Integer) getArgs()[0];
                Map maxdata = (Map) getArgs()[1];
                data.put("score", score);
                if (score < minScore || score == 0) return null;
                if (score >= minScore) {
                    for (Map map : list) {
                        if (map.get(st_column).equals(data.get(st_column)))
                            return null;
                    }
                    list.add(data);
                    Collections.sort(list, new Comparator<Map>() {
                        public int compare(Map o1, Map o2) {
                            Integer s1 = (Integer) o1.get("score");
                            Integer s2 = (Integer) o2.get("score");
                            return s2.compareTo(s1);
                        }
                    });
                    int _index = list.size();
                    if (_index > 10) {
                        list.remove(_index - 1);
                        getArgs()[0] = list.get(9).get("score");
                    } else {
                        getArgs()[0] = list.get(_index - 1).get("score");
                    }
                }
                if (score == 100) {
                    getExecuter().setStop(true);
                }
                return null;
            }
        });
        String json = JSONArray.fromObject(list).toString();
        return json;
    }

    /**
     * 根据ID返回Map
     *
     * @return 返回标准地址数据
     * @params address 模糊地址
     * @params 返回条数
     */
    public Map search(String id) throws Exception {
        final String sql = "select * from " + st_table + " where id=?";
        Map map = DBHelper.findFirst(sql, Arrays.asList(id));
        return map;
    }

    public List<Map> searchMap(String address) throws Exception {
        String json = same(address);
        JSONArray array = JSONArray.fromObject(json);
        List<Map> map = JSONArray.toList(array, Map.class);
        return map;
    }

    public static void main(String[] args) {
        try {
            DBPool.isPrintSQL = true;
            SwLocateUtil st = new SwLocateUtil("geo", "BUS", "DZ");
            String address = "萝岗区九龙镇聚龙路27号";
            List<Map> list = st.searchMap(address);
            for (Map map : list) {
                System.out.println(map.get("dz"));
                System.out.println(map.get("wkt"));
            }
        } catch (Exception e) {
        }

    }
}
