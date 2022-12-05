package com.augurit.agcloud.agcom.agsupport.sc.bimManager.bimCheck.service.impl;

import com.augurit.agcloud.agcom.agsupport.common.exception.SourceException;
import com.augurit.agcloud.agcom.agsupport.domain.auto.AgBimCheckStandard;
import com.augurit.agcloud.agcom.agsupport.domain.auto.AgBimCheckStandardExample;
import com.augurit.agcloud.agcom.agsupport.mapper.AgBimCheckStandardCustomMapper;
import com.augurit.agcloud.agcom.agsupport.mapper.auto.AgBimCheckStandardMapper;
import com.augurit.agcloud.agcom.agsupport.sc.bimManager.bimCheck.service.IAgBimCheckStandardService;
import com.augurit.agcloud.agcom.agsupport.sc.dir.util.StringUtils;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.transform.Source;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName AgBimCheckStandardServiceImpl
 * @Author lizih
 * @Date 2020/12/23 9:22
 * @Version 1.0
 */
@Service
public class AgBimCheckStandardServiceImpl implements IAgBimCheckStandardService {

    @Autowired
    private AgBimCheckStandardMapper agBimCheckStandardMapper;

    @Autowired
    AgBimCheckStandardCustomMapper agBimCheckStandardCustomMapper;

    @Override
    public PageInfo<AgBimCheckStandard> find(String category, Page page) {
        if (StringUtils.isEmpty(category)){
            PageHelper.startPage(page);
            List<AgBimCheckStandard> list = agBimCheckStandardMapper.selectByExample(null);
            return new PageInfo<>(list);
        }
        AgBimCheckStandardExample bimCheckStandardExample = new AgBimCheckStandardExample();
        bimCheckStandardExample.createCriteria().andClauseCategoryEqualTo(category);
        PageHelper.startPage(page);
        List<AgBimCheckStandard> list = agBimCheckStandardMapper.selectByExample(bimCheckStandardExample);
        return new PageInfo<>(list);
    }

    @Override
    public List<AgBimCheckStandard> get(String category) {
        if (StringUtils.isEmpty(category)){
            return agBimCheckStandardMapper.selectByExample(null);
        }
        AgBimCheckStandardExample bimCheckStandardExample = new AgBimCheckStandardExample();
        bimCheckStandardExample.createCriteria().andClauseCategoryEqualTo(category);
        return agBimCheckStandardMapper.selectByExample(bimCheckStandardExample);
    }

    @Override
    public Map<String, Object> tree(String category, String clause) {
        Map<String, Object> resultMap = new HashMap<>();
        AgBimCheckStandardExample bimCheckStandardExample = new AgBimCheckStandardExample();
        bimCheckStandardExample.setOrderByClause("serial asc");
        bimCheckStandardExample.createCriteria().andClauseCategoryEqualTo(category).andClauseEqualTo(clause);
        List<AgBimCheckStandard> agBimCheckStandards = agBimCheckStandardMapper.selectByExample(bimCheckStandardExample);
        if(agBimCheckStandards != null && agBimCheckStandards.size() > 0){
            //将serial转成Tree
            HashMap<String, Object> root = new HashMap<>();
            ArrayList<HashMap<String, Object>> arrayList = new ArrayList<>();
            root.put("content", arrayList);
            for(AgBimCheckStandard standard : agBimCheckStandards){
                addPath(root, standard.getSerial().trim(), standard.getClauseCategory(),standard.getClauseContent());
            }
            arrayList = (ArrayList<HashMap<String, Object>>)root.get("content");
            dealTreeResult(arrayList);
            resultMap.put("tree", arrayList);
        }
        return resultMap;
    }

    /**
     * @Author Yingguang, Qin; Modified by Zihui Li
     * @Date: 2020/12/25 15:35
     * @tips:
     * @Param root 根节点
     * @Param path 路径，这里依据serial建立路径
     * @Param category 条文所属类型
     * @Param clauseContent 条文拆解/内容
     * @return void
     */
    private static void addPath(HashMap<String, Object> root, String path, String category, String clauseContent) {
        String url = "";
        String[] pathArr = path.split("\\.");
        for (String pivot : pathArr) {
            url +=  pivot + ".";
            boolean flag = true;
            for (HashMap<String, Object> node : (ArrayList<HashMap<String, Object>>) root.get("content")) {
                if (node.get("treeCheck").equals(pivot)) {
                    root = node;
                    flag = false;
                    break;
                }
            }
            if (flag) {
                HashMap<String, Object> new_node = new HashMap<>();
                new_node.put("treeCheck", pivot);
                new_node.put("category", "");
                new_node.put("clauseContent", "");
                String uPivot = url.substring(0, url.length() - 1);
                new_node.put("serial", uPivot);
                //如果uPivot eq path 说明当前是全路径的，需要添加赋值；如果url eq path，需要添加赋值；
                if(path.equals(uPivot) || path.equals(url)){
                    new_node.put("category", category);
                    new_node.put("clauseContent", clauseContent);
                }
                new_node.put("content", new ArrayList<HashMap<String, Object>>());
                ((ArrayList<HashMap<String, Object>>) root.get("content")).add(new_node);

                // root list 排序，按照hashmap的treeCheck数字版排序
                // 自定义root.get("content") list 的 Comparator
                ((ArrayList<HashMap<String, Object>>) root.get("content")).sort(new Comparator<HashMap<String, Object>>() {
                    @Override
                    public int compare(HashMap<String, Object> o1, HashMap<String, Object> o2) {
                        try{
//                            StringUtils.isNumeric(o1.get("treeCheck").toString());
                            return Integer.compare(Integer.parseInt(o1.get("treeCheck").toString()), Integer.parseInt(o2.get("treeCheck").toString()));
                        } catch (Exception e){
                            String treeCheck1 = o1.get("treeCheck").toString();
                            String treeCheck2 = o2.get("treeCheck").toString();
                            Pattern p = Pattern.compile("[^0-9]");
                            Matcher m1 = p.matcher(treeCheck1);
                            Matcher m2 = p.matcher(treeCheck2);
                            Boolean hasNonNum1 = m1.find();
                            Boolean hasNonNum2 = m2.find();
                            if (hasNonNum1 && hasNonNum2){
                                return Integer.compare(Integer.parseInt(treeCheck1.substring(0,m1.start())),Integer.parseInt(treeCheck2.substring(0,m2.start())));
                            }
                            if (!hasNonNum1 && hasNonNum2){
                                int res = Integer.compare(Integer.parseInt(treeCheck1),Integer.parseInt(treeCheck2.substring(0,m2.start())));
                                if (res == 0){
                                    return -1;
                                }
                                return res;
                            }
                            if (hasNonNum1 && !hasNonNum2){
                                int res = Integer.compare(Integer.parseInt(treeCheck1.substring(0,m1.start())),Integer.parseInt(treeCheck2));
                                if (res == 0){
                                    return 1;
                                }
                                return res;
                            }
                            return treeCheck1.compareTo(treeCheck2);
                        }

                    }
                });

                root = new_node;
            }
        }
    }

    private void dealTreeResult(ArrayList<HashMap<String, Object>> list){
        if(list != null && list.size() > 0){
            for(HashMap<String, Object> map : list){
                ArrayList<HashMap<String, Object>> arrayList = (ArrayList<HashMap<String, Object>>)map.get("content");
                //都去掉treeCheck值
                map.remove("treeCheck");
                //content还有数据，继续循环处理
                if(arrayList != null && arrayList.size() > 0){
                    dealTreeResult(arrayList);
                }
            }
        }
    }

    @Override
    public List<String> getCategories() {
        return agBimCheckStandardCustomMapper.getCategories();
    }

    @Override
    public List<String> getClauses(String category) {
        return agBimCheckStandardCustomMapper.getClauses(category);
    }

    @Override
    public List<AgBimCheckStandard> getClauseContents(String category, String clause) {
        if (StringUtils.isEmpty(category) || StringUtils.isEmpty(clause)){
            throw new SourceException("category 或者 clause不能为空");
        }
        return agBimCheckStandardCustomMapper.getClauseContents(category, clause);
    }
}
