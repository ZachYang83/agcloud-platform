//package com.augurit.agcloud.agcom.agsupport.sc.bim.service.impl;
//
//
//import com.augurit.agcloud.agcom.agsupport.domain.auto.Agcim3dentityA;
//import com.augurit.agcloud.agcom.agsupport.domain.auto.Agcim3dentityAExample;
//import com.augurit.agcloud.agcom.agsupport.mapper.Agcim3dCustomMapper;
//import com.augurit.agcloud.agcom.agsupport.mapper.auto.Agcim3dentityAMapper;
//import com.augurit.agcloud.agcom.agsupport.sc.bim.service.IAgcim3dentityAService;
//import com.augurit.agcloud.framework.ui.pager.PageHelper;
//import com.github.pagehelper.Page;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.util.StringUtils;
//
//import java.util.List;
//
///**
// * Created with IntelliJ IDEA.
// *
// * @Auther: qinyg
// * @Date: 2020/08
// * @Description:
// */
//@Service
//public class Agcim3dentityAServiceImpl implements IAgcim3dentityAService {
//    private static final Logger logger = LoggerFactory.getLogger(Agcim3dentityAServiceImpl.class);
//
//    @Autowired
//    private Agcim3dentityAMapper agcim3dentityAMapper;
//    @Autowired
//    private Agcim3dCustomMapper agcim3dentityACustomMapper;
//
//
//    @Override
//    public List<Agcim3dentityA> list(Agcim3dentityA agcim) {
//        PageHelper.startPage(new Page<>(1, 1000));
//        Agcim3dentityAExample example = new Agcim3dentityAExample();
//        getAgcim3dentityAExample(example, agcim);
//        return agcim3dentityAMapper.selectByExample(example);
//    }
//
//    /**
//     * 所有参数封装
//     * @param agcim
//     * @return
//     */
//    private void getAgcim3dentityAExample(Agcim3dentityAExample example, Agcim3dentityA agcim) {
//        Agcim3dentityAExample.Criteria criteria = example.createCriteria();
//        if(!StringUtils.isEmpty(agcim.getId())){
//            criteria.andIdEqualTo(agcim.getId());
//        }
//        if(!StringUtils.isEmpty(agcim.getObjectid())){
//            criteria.andObjectidEqualTo(agcim.getObjectid());
//        }
//        if(!StringUtils.isEmpty(agcim.getName())){
//            criteria.andNameEqualTo(agcim.getName());
//        }
//        if(!StringUtils.isEmpty(agcim.getVersion())){
//            criteria.andVersionEqualTo(agcim.getVersion());
//        }
//        if(!StringUtils.isEmpty(agcim.getInfotype())){
//            criteria.andInfotypeEqualTo(agcim.getInfotype());
//        }
//        if(!StringUtils.isEmpty(agcim.getProfession())){
//            criteria.andProfessionEqualTo(agcim.getProfession());
//        }
//        if(!StringUtils.isEmpty(agcim.getLevel())){
//            criteria.andLevelEqualTo(agcim.getLevel());
//        }
//        if(!StringUtils.isEmpty(agcim.getCatagory())){
//            criteria.andCatagoryEqualTo(agcim.getCatagory());
//        }
//        if(!StringUtils.isEmpty(agcim.getFamilyname())){
//            criteria.andFamilynameEqualTo(agcim.getFamilyname());
//        }
//        if(!StringUtils.isEmpty(agcim.getFamilytype())){
//            criteria.andFamilytypeEqualTo(agcim.getFamilytype());
//        }
//        if(!StringUtils.isEmpty(agcim.getMaterialid())){
//            criteria.andMaterialidEqualTo(agcim.getMaterialid());
//        }
//        if(!StringUtils.isEmpty(agcim.getElementattributes())){
//            criteria.andElementattributesEqualTo(agcim.getElementattributes());
//        }
//        if(!StringUtils.isEmpty(agcim.getCategorypath())){
//            criteria.andCategorypathEqualTo(agcim.getCategorypath());
//        }
//        if(!StringUtils.isEmpty(agcim.getGeometry())){
//            criteria.andGeometryEqualTo(agcim.getGeometry());
//        }
//        if(!StringUtils.isEmpty(agcim.getTopologyelements())){
//            criteria.andTopologyelementsEqualTo(agcim.getTopologyelements());
//        }
//        if(!StringUtils.isEmpty(agcim.getBoundingbox())){
//            criteria.andBoundingboxEqualTo(agcim.getBoundingbox());
//        }
//    }
//
//
//
//    @Override
//    public List<Agcim3dentityA>  filter(String filterKey, Agcim3dentityA agcim) {
//        PageHelper.startPage(new Page<>(1, 1000));
//        //过滤字段为null，那就是所有的都需要distinct过滤
//        if(StringUtils.isEmpty(filterKey)){
//            return listAgcim3dentityAFilter(agcim);
//        }
//        StringBuffer sb = new StringBuffer();
//        sb.append("select distinct ");
//        for(String key: filterKey.split(",")){
//            getAgcim3dentityAFilterKey(sb, key);
//        }
//        //过滤掉最后两个字符串“， ”逗号和空格
//        sb.delete(sb.length() - 2, sb.length());
//        sb.append(" from agcim3dentity_a ");
//        Agcim3dentityAExample example = new Agcim3dentityAExample();
//        logger.info(sb.toString());
//        //使用orderByClause当做是前面的sql
//        example.setOrderByClause(sb.toString());
//        getAgcim3dentityAExample(example, agcim);
//        List<Agcim3dentityA> list = agcim3dentityACustomMapper.selectByExample(example);
//        return list;
//    }
//
//
//    private void getAgcim3dentityAFilterKey(StringBuffer sb, String key) {
//        if("id".equals(key)){
//            sb.append(key + ", ");
//        }
//        if("objectid".equals(key)){
//            sb.append(key + ", ");
//        }
//        if("name".equals(key)){
//            sb.append(key + ", ");
//        }
//        if("version".equals(key)){
//            sb.append(key + ", ");
//        }
//        if("infotype".equals(key)){
//            sb.append(key + ", ");
//        }
//        if("profession".equals(key)){
//            sb.append(key + ", ");
//        }
//        if("level".equals(key)){
//            sb.append(key + ", ");
//        }
//        if("catagory".equals(key)){
//            sb.append(key + ", ");
//        }
//        if("familyname".equals(key)){
//            sb.append(key + ", ");
//        }
//        if("familytype".equals(key)){
//            sb.append(key + ", ");
//        }
//        if("materialid".equals(key)){
//            sb.append(key + ", ");
//        }
//        if("elementattributes".equals(key)){
//            sb.append(key + ", ");
//        }
//        if("categorypath".equals(key)){
//            sb.append(key + ", ");
//        }
//        if("geometry".equals(key)){
//            sb.append(key + ", ");
//        }
//        if("topologyelements".equals(key)){
//            sb.append(key + ", ");
//        }
//        if("boundingbox".equals(key)){
//            sb.append(key + ", ");
//        }
//    }
//
//    private List<Agcim3dentityA> listAgcim3dentityAFilter(Agcim3dentityA agcim){
//        Agcim3dentityAExample example = new Agcim3dentityAExample();
//        example.setDistinct(true);
//        getAgcim3dentityAExample(example, agcim);
//        return agcim3dentityAMapper.selectByExample(example);
//    }
//
//    private void getAgcim3dentityACountKey(StringBuffer sb,  String key) {
//        if("id".equals(key)){
//            sb.append(" count(" + key + ") as value ");
//        }
//        if("objectid".equals(key)){
//            sb.append(" count(" + key + ") as value ");
//        }
//        if("name".equals(key)){
//            sb.append(" count(" + key + ") as value ");
//        }
//        if("version".equals(key)){
//            sb.append(" count(" + key + ") as value ");
//        }
//        if("infotype".equals(key)){
//            sb.append(" count(" + key + ") as value ");
//        }
//        if("profession".equals(key)){
//            sb.append(" count(" + key + ") as value ");
//        }
//        if("level".equals(key)){
//            sb.append(" count(" + key + ") as value ");
//        }
//        if("catagory".equals(key)){
//            sb.append(" count(" + key + ") as value ");
//        }
//        if("familyname".equals(key)){
//            sb.append(" count(" + key + ") as value ");
//        }
//        if("familytype".equals(key)){
//            sb.append(" count(" + key + ") as value ");
//        }
//        if("materialid".equals(key)){
//            sb.append(" count(" + key + ") as value ");
//        }
//        if("elementattributes".equals(key)){
//            sb.append(" count(" + key + ") as value ");
//        }
//        if("categorypath".equals(key)){
//            sb.append(" count(" + key + ") as value ");
//        }
//        if("geometry".equals(key)){
//            sb.append(" count(" + key + ") as value ");
//        }
//        if("topologyelements".equals(key)){
//            sb.append(" count(" + key + ") as value ");
//        }
//        if("boundingbox".equals(key)){
//            sb.append(" count(" + key + ") as value ");
//        }
//    }
//
//    private void getAgcim3dentityAGroupKey(StringBuffer sb,  String key) {
//        if("id".equals(key)){
//            sb.append(key + " as name, ");
//        }
//        if("objectid".equals(key)){
//            sb.append(key + " as name, ");
//        }
//        if("name".equals(key)){
//            sb.append(key + " as name, ");
//        }
//        if("version".equals(key)){
//            sb.append(key + " as name, ");
//        }
//        if("infotype".equals(key)){
//            sb.append(key + " as name, ");
//        }
//        if("profession".equals(key)){
//            sb.append(key + " as name, ");
//        }
//        if("level".equals(key)){
//            sb.append(key + " as name, ");
//        }
//        if("catagory".equals(key)){
//            sb.append(key + " as name, ");
//        }
//        if("familyname".equals(key)){
//            sb.append(key + " as name, ");
//        }
//        if("familytype".equals(key)){
//            sb.append(key + " as name, ");
//        }
//        if("materialid".equals(key)){
//            sb.append(key + " as name, ");
//        }
//        if("elementattributes".equals(key)){
//            sb.append(key + " as name, ");
//        }
//        if("categorypath".equals(key)){
//            sb.append(key + " as name, ");
//        }
//        if("geometry".equals(key)){
//            sb.append(key + " as name, ");
//        }
//        if("topologyelements".equals(key)){
//            sb.append(key + " as name, ");
//        }
//        if("boundingbox".equals(key)){
//            sb.append(key + " as name, ");
//        }
//    }
//
//    private void getAgcim3dentityAGroupKeyValue(StringBuffer sb,  String key) {
//        if("id".equals(key)){
//            sb.append(key);
//        }
//        if("objectid".equals(key)){
//            sb.append(key);
//        }
//        if("name".equals(key)){
//            sb.append(key);
//        }
//        if("version".equals(key)){
//            sb.append(key);
//        }
//        if("infotype".equals(key)){
//            sb.append(key);
//        }
//        if("profession".equals(key)){
//            sb.append(key);
//        }
//        if("level".equals(key)){
//            sb.append(key);
//        }
//        if("catagory".equals(key)){
//            sb.append(key);
//        }
//        if("familyname".equals(key)){
//            sb.append(key);
//        }
//        if("familytype".equals(key)){
//            sb.append(key);
//        }
//        if("materialid".equals(key)){
//            sb.append(key);
//        }
//        if("elementattributes".equals(key)){
//            sb.append(key);
//        }
//        if("categorypath".equals(key)){
//            sb.append(key);
//        }
//        if("geometry".equals(key)){
//            sb.append(key);
//        }
//        if("topologyelements".equals(key)){
//            sb.append(key);
//        }
//        if("boundingbox".equals(key)){
//            sb.append(key);
//        }
//    }
//
//    private void getAgcim3dentityAWhereAppend(StringBuffer sb,Agcim3dentityA agcim) {
//
//        if(!StringUtils.isEmpty(agcim.getId())){
//            sb.append("and id = '" + agcim.getId() + "'");
//        }
//        if(!StringUtils.isEmpty(agcim.getObjectid())){
//            sb.append("and objectid = '" + agcim.getObjectid() + "'");
//        }
//        if(!StringUtils.isEmpty(agcim.getName())){
//            sb.append("and name = '" + agcim.getName() + "'");
//        }
//        if(!StringUtils.isEmpty(agcim.getVersion())){
//            sb.append("and version = '" + agcim.getVersion() + "'");
//        }
//        if(!StringUtils.isEmpty(agcim.getInfotype())){
//            sb.append("and infotype = '" + agcim.getInfotype() + "'");
//        }
//        if(!StringUtils.isEmpty(agcim.getProfession())){
//            sb.append("and profession = '" + agcim.getProfession() + "'");
//        }
//        if(!StringUtils.isEmpty(agcim.getLevel())){
//            sb.append("and level = '" + agcim.getLevel() + "'");
//        }
//        if(!StringUtils.isEmpty(agcim.getCatagory())){
//            sb.append("and catagory = '" + agcim.getCatagory() + "'");
//        }
//        if(!StringUtils.isEmpty(agcim.getFamilyname())){
//            sb.append("and familyname = '" + agcim.getFamilyname() + "'");
//        }
//        if(!StringUtils.isEmpty(agcim.getFamilytype())){
//            sb.append("and familytype = '" + agcim.getFamilytype() + "'");
//        }
//        if(!StringUtils.isEmpty(agcim.getMaterialid())){
//            sb.append("and materialid = '" + agcim.getMaterialid() + "'");
//        }
//        if(!StringUtils.isEmpty(agcim.getElementattributes())){
//            sb.append("and elementattributes = '" + agcim.getElementattributes() + "'");
//        }
//        if(!StringUtils.isEmpty(agcim.getCategorypath())){
//            sb.append("and categorypath = '" + agcim.getCategorypath() + "'");
//        }
//        if(!StringUtils.isEmpty(agcim.getGeometry())){
//            sb.append("and geometry = '" + agcim.getGeometry() + "'");
//        }
//        if(!StringUtils.isEmpty(agcim.getTopologyelements())){
//            sb.append("and topologyelements = '" + agcim.getTopologyelements() + "'");
//        }
//        if(!StringUtils.isEmpty(agcim.getBoundingbox())){
//            sb.append("and boundingbox = '" + agcim.getBoundingbox() + "'");
//        }
//    }
//
//
//    @Override
//    public Object count(String countKey, String groupKey, Agcim3dentityA param) {
//        StringBuffer sb = new StringBuffer();
//        sb.append("select ");
//        getAgcim3dentityAGroupKey(sb, groupKey);
//        getAgcim3dentityACountKey(sb, countKey);
//        sb.append(" from agcim3dentity_a where 1 = 1 ");
//        getAgcim3dentityAWhereAppend(sb, param);
//        sb.append(" group by ");
//        getAgcim3dentityAGroupKeyValue(sb, groupKey);
//        logger.info(sb.toString());
//        List<Object> list = agcim3dentityACustomMapper.findAllDefineSql(sb.toString());
//        return list;
//    }
//
//
//}

package com.augurit.agcloud.agcom.agsupport.sc.bimManager.bimParamethy.service.impl;

import com.augurit.agcloud.agcom.agsupport.common.exception.SourceException;
import com.augurit.agcloud.agcom.agsupport.domain.Agcim3dentityXCustom;
import com.augurit.agcloud.agcom.agsupport.domain.Agcim3dentityXCustomExample;
import com.augurit.agcloud.agcom.agsupport.mapper.Agcim3dentityXCustomMapper;
import com.augurit.agcloud.agcom.agsupport.sc.bimManager.bimParamethy.service.IAgcim3dentityXService;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: qinyg
 * @Date: 2020/08
 * @Description:
 */
@Service
public class Agcim3dentityXServiceImpl implements IAgcim3dentityXService {
    private static final Logger logger = LoggerFactory.getLogger(Agcim3dentityXServiceImpl.class);

    @Autowired
    private Agcim3dentityXCustomMapper agcim3dentityXCustomMapper;
    //通用校验，表名规则
    private static final String table_name = "agcim3dentity_";//length = 14
    //表名长度显示，不能超过24
    private static final int table_name_max_length = 24;

    @Override
    public List<Agcim3dentityXCustom> list(Agcim3dentityXCustom agcim, String tableName) {
        //表名真实性匹配验证、长度不能超过24
        checkTableName(tableName);
        PageHelper.startPage(new Page<>(1, 1000));
        Agcim3dentityXCustomExample example = new Agcim3dentityXCustomExample();
        //必须设置表名
        example.setTableName(tableName);
        getAgcim3dentityAExample(example, agcim);
        // 排序
        example.setOrderByClause(" name asc");
        return agcim3dentityXCustomMapper.selectByExample(example);
    }

    /**
     * 所有参数封装
     * @param agcim
     * @return
     */
    private void getAgcim3dentityAExample(Agcim3dentityXCustomExample example, Agcim3dentityXCustom agcim) {
        Agcim3dentityXCustomExample.Criteria criteria = example.createCriteria();
        if(!StringUtils.isEmpty(agcim.getId())){
            criteria.andIdEqualTo(agcim.getId());
        }
        if(!StringUtils.isEmpty(agcim.getObjectid())){
//            criteria.andObjectidEqualTo(agcim.getObjectid());
            List<String> objectidList = new ArrayList<>();
            for(String objectid: agcim.getObjectid().split(",")){
                objectidList.add(objectid);
            }
            criteria.andObjectidIn(objectidList);
        }
        if(!StringUtils.isEmpty(agcim.getName())){
            criteria.andNameEqualTo(agcim.getName());
        }
        if(!StringUtils.isEmpty(agcim.getVersion())){
            criteria.andVersionEqualTo(agcim.getVersion());
        }
        if(!StringUtils.isEmpty(agcim.getInfotype())){
            criteria.andInfotypeEqualTo(agcim.getInfotype());
        }
        if(!StringUtils.isEmpty(agcim.getProfession())){
            criteria.andProfessionEqualTo(agcim.getProfession());
        }
        if(!StringUtils.isEmpty(agcim.getLevel())){
            criteria.andLevelEqualTo(agcim.getLevel());
        }
        if(!StringUtils.isEmpty(agcim.getCatagory())){
            criteria.andCatagoryEqualTo(agcim.getCatagory());
        }
        if(!StringUtils.isEmpty(agcim.getFamilyname())){
            criteria.andFamilynameEqualTo(agcim.getFamilyname());
        }
        if(!StringUtils.isEmpty(agcim.getFamilytype())){
            criteria.andFamilytypeEqualTo(agcim.getFamilytype());
        }
        if(!StringUtils.isEmpty(agcim.getMaterialid())){
            criteria.andMaterialidEqualTo(agcim.getMaterialid());
        }
        if(!StringUtils.isEmpty(agcim.getElementattributes())){
            criteria.andElementattributesEqualTo(agcim.getElementattributes());
        }
        if(!StringUtils.isEmpty(agcim.getHost())){
            criteria.andHostEqualTo(agcim.getHost());
        }
        /*if(!StringUtils.isEmpty(agcim.getCategorypath())){
            criteria.andCategorypathEqualTo(agcim.getCategorypath());
        }*/

        if(!StringUtils.isEmpty(agcim.getGeometry())){
            criteria.andGeometryEqualTo(agcim.getGeometry());
        }
        if(!StringUtils.isEmpty(agcim.getTopologyelements())){
            criteria.andTopologyelementsEqualTo(agcim.getTopologyelements());
        }
        if(!StringUtils.isEmpty(agcim.getBoundingbox())){
            criteria.andBoundingboxEqualTo(agcim.getBoundingbox());
        }
    }



    @Override
    public List<Agcim3dentityXCustom>  filter(String filterKey, Agcim3dentityXCustom agcim, String tableName) {
        //表名真实性匹配验证、长度不能超过24
        checkTableName(tableName);

        PageHelper.startPage(new Page<>(1, 1000));
        //过滤字段为null，那就是所有的都需要distinct过滤
        if(StringUtils.isEmpty(filterKey)){
            return listAgcim3dentityAFilter(agcim, tableName);
        }
        StringBuffer sb = new StringBuffer();
        sb.append("select distinct ");
        for(String key: filterKey.split(",")){
            getAgcim3dentityAFilterKey(sb, key);
        }
        //过滤掉最后两个字符串“， ”逗号和空格
        sb.delete(sb.length() - 2, sb.length());
        sb.append(" from " + tableName);
        Agcim3dentityXCustomExample example = new Agcim3dentityXCustomExample();
        //使用orderByClause当做是前面的sql
        example.setOrderByClause(sb.toString());
        getAgcim3dentityAExample(example, agcim);
        List<Agcim3dentityXCustom> list = agcim3dentityXCustomMapper.filterOrderByClause(example);
        return list;
    }


    private void getAgcim3dentityAFilterKey(StringBuffer sb, String key) {
        if("id".equals(key)){
            sb.append(key + ", ");
        }
        if("objectid".equals(key)){
            sb.append(key + ", ");
        }
        if("name".equals(key)){
            sb.append(key + ", ");
        }
        if("version".equals(key)){
            sb.append(key + ", ");
        }
        if("infotype".equals(key)){
            sb.append(key + ", ");
        }
        if("profession".equals(key)){
            sb.append(key + ", ");
        }
        if("level".equals(key)){
            sb.append(key + ", ");
        }
        if("catagory".equals(key)){
            sb.append(key + ", ");
        }
        if("familyname".equals(key)){
            sb.append(key + ", ");
        }
        if("familytype".equals(key)){
            sb.append(key + ", ");
        }
        if("materialid".equals(key)){
            sb.append(key + ", ");
        }
        if("elementattributes".equals(key)){
            sb.append(key + ", ");
        }
        if("categorypath".equals(key)){
            sb.append(key + ", ");
        }
        if("geometry".equals(key)){
            sb.append(key + ", ");
        }
        if("topologyelements".equals(key)){
            sb.append(key + ", ");
        }
        if("boundingbox".equals(key)){
            sb.append(key + ", ");
        }
    }

    private List<Agcim3dentityXCustom> listAgcim3dentityAFilter(Agcim3dentityXCustom agcim, String tableName){
        Agcim3dentityXCustomExample example = new Agcim3dentityXCustomExample();
        example.setDistinct(true);
        //必须设置表名
        example.setTableName(tableName);

        getAgcim3dentityAExample(example, agcim);
        return agcim3dentityXCustomMapper.selectByExample(example);
    }

    private void getAgcim3dentityACountKey(StringBuffer sb,  String key) {
        if("id".equals(key)){
            sb.append(" count(" + key + ") as value ");
        }
        if("objectid".equals(key)){
            sb.append(" count(" + key + ") as value ");
        }
        if("name".equals(key)){
            sb.append(" count(" + key + ") as value ");
        }
        if("version".equals(key)){
            sb.append(" count(" + key + ") as value ");
        }
        if("infotype".equals(key)){
            sb.append(" count(" + key + ") as value ");
        }
        if("profession".equals(key)){
            sb.append(" count(" + key + ") as value ");
        }
        if("level".equals(key)){
            sb.append(" count(" + key + ") as value ");
        }
        if("catagory".equals(key)){
            sb.append(" count(" + key + ") as value ");
        }
        if("familyname".equals(key)){
            sb.append(" count(" + key + ") as value ");
        }
        if("familytype".equals(key)){
            sb.append(" count(" + key + ") as value ");
        }
        if("materialid".equals(key)){
            sb.append(" count(" + key + ") as value ");
        }
        if("elementattributes".equals(key)){
            sb.append(" count(" + key + ") as value ");
        }
        if("categorypath".equals(key)){
            sb.append(" count(" + key + ") as value ");
        }
        if("geometry".equals(key)){
            sb.append(" count(" + key + ") as value ");
        }
        if("topologyelements".equals(key)){
            sb.append(" count(" + key + ") as value ");
        }
        if("boundingbox".equals(key)){
            sb.append(" count(" + key + ") as value ");
        }
    }

    private void getAgcim3dentityAGroupKey(StringBuffer sb,  String key) {
        if("id".equals(key)){
            sb.append(key + " as name, ");
        }
        if("objectid".equals(key)){
            sb.append(key + " as name, ");
        }
        if("name".equals(key)){
            sb.append(key + " as name, ");
        }
        if("version".equals(key)){
            sb.append(key + " as name, ");
        }
        if("infotype".equals(key)){
            sb.append(key + " as name, ");
        }
        if("profession".equals(key)){
            sb.append(key + " as name, ");
        }
        if("level".equals(key)){
            sb.append(key + " as name, ");
        }
        if("catagory".equals(key)){
            sb.append(key + " as name, ");
        }
        if("familyname".equals(key)){
            sb.append(key + " as name, ");
        }
        if("familytype".equals(key)){
            sb.append(key + " as name, ");
        }
        if("materialid".equals(key)){
            sb.append(key + " as name, ");
        }
        if("elementattributes".equals(key)){
            sb.append(key + " as name, ");
        }
        if("categorypath".equals(key)){
            sb.append(key + " as name, ");
        }
        if("geometry".equals(key)){
            sb.append(key + " as name, ");
        }
        if("topologyelements".equals(key)){
            sb.append(key + " as name, ");
        }
        if("boundingbox".equals(key)){
            sb.append(key + " as name, ");
        }
    }

    private void getAgcim3dentityAGroupKeyValue(StringBuffer sb,  String key) {
        if("id".equals(key)){
            sb.append(key);
        }
        if("objectid".equals(key)){
            sb.append(key);
        }
        if("name".equals(key)){
            sb.append(key);
        }
        if("version".equals(key)){
            sb.append(key);
        }
        if("infotype".equals(key)){
            sb.append(key);
        }
        if("profession".equals(key)){
            sb.append(key);
        }
        if("level".equals(key)){
            sb.append(key);
        }
        if("catagory".equals(key)){
            sb.append(key);
        }
        if("familyname".equals(key)){
            sb.append(key);
        }
        if("familytype".equals(key)){
            sb.append(key);
        }
        if("materialid".equals(key)){
            sb.append(key);
        }
        if("elementattributes".equals(key)){
            sb.append(key);
        }
        if("categorypath".equals(key)){
            sb.append(key);
        }
        if("geometry".equals(key)){
            sb.append(key);
        }
        if("topologyelements".equals(key)){
            sb.append(key);
        }
        if("boundingbox".equals(key)){
            sb.append(key);
        }
    }

    private void getAgcim3dentityAWhereAppend(StringBuffer sb, Agcim3dentityXCustom agcim) {

        if(!StringUtils.isEmpty(agcim.getId())){
            sb.append("and id = '" + agcim.getId() + "'");
        }
        if(!StringUtils.isEmpty(agcim.getObjectid())){
            sb.append("and objectid = '" + agcim.getObjectid() + "'");
        }
        if(!StringUtils.isEmpty(agcim.getName())){
            sb.append("and name = '" + agcim.getName() + "'");
        }
        if(!StringUtils.isEmpty(agcim.getVersion())){
            sb.append("and version = '" + agcim.getVersion() + "'");
        }
        if(!StringUtils.isEmpty(agcim.getInfotype())){
            sb.append("and infotype = '" + agcim.getInfotype() + "'");
        }
        if(!StringUtils.isEmpty(agcim.getProfession())){
            sb.append("and profession = '" + agcim.getProfession() + "'");
        }
        if(!StringUtils.isEmpty(agcim.getLevel())){
            sb.append("and level = '" + agcim.getLevel() + "'");
        }
        if(!StringUtils.isEmpty(agcim.getCatagory())){
            sb.append("and catagory = '" + agcim.getCatagory() + "'");
        }
        if(!StringUtils.isEmpty(agcim.getFamilyname())){
            sb.append("and familyname = '" + agcim.getFamilyname() + "'");
        }
        if(!StringUtils.isEmpty(agcim.getFamilytype())){
            sb.append("and familytype = '" + agcim.getFamilytype() + "'");
        }
        if(!StringUtils.isEmpty(agcim.getMaterialid())){
            sb.append("and materialid = '" + agcim.getMaterialid() + "'");
        }
        if(!StringUtils.isEmpty(agcim.getElementattributes())){
            sb.append("and elementattributes = '" + agcim.getElementattributes() + "'");
        }
        if(!StringUtils.isEmpty(agcim.getHost())){
            sb.append("and host = '" + agcim.getHost() + "'");
        }
        /*if(!StringUtils.isEmpty(agcim.getCategorypath())){
            sb.append("and categorypath = '" + agcim.getCategorypath() + "'");
        }*/
        if(!StringUtils.isEmpty(agcim.getGeometry())){
            sb.append("and geometry = '" + agcim.getGeometry() + "'");
        }
        if(!StringUtils.isEmpty(agcim.getTopologyelements())){
            sb.append("and topologyelements = '" + agcim.getTopologyelements() + "'");
        }
        if(!StringUtils.isEmpty(agcim.getBoundingbox())){
            sb.append("and boundingbox = '" + agcim.getBoundingbox() + "'");
        }
    }

    //判断当前列是否在数据表
    private boolean checkAgcim3dentityACulumnsIsExist(String key) {
        boolean isExist = false;
        if("id".equals(key)){
            isExist = true;
        }
        if("objectid".equals(key)){
            isExist = true;
        }
        if("name".equals(key)){
            isExist = true;
        }
        if("version".equals(key)){
            isExist = true;
        }
        if("infotype".equals(key)){
            isExist = true;
        }
        if("profession".equals(key)){
            isExist = true;
        }
        if("level".equals(key)){
            isExist = true;
        }
        if("catagory".equals(key)){
            isExist = true;
        }
        if("familyname".equals(key)){
            isExist = true;
        }
        if("familytype".equals(key)){
            isExist = true;
        }
        if("materialid".equals(key)){
            isExist = true;
        }
        if("elementattributes".equals(key)){
            isExist = true;
        }
        if("categorypath".equals(key)){
            isExist = true;
        }
        if("geometry".equals(key)){
            isExist = true;
        }
        if("topologyelements".equals(key)){
            isExist = true;
        }
        if("boundingbox".equals(key)){
            isExist = true;
        }
        return isExist;
    }

    @Override
    public Object statistics(String countKey, String groupKey, Agcim3dentityXCustom param, String tableName) {
        //表名真实性匹配验证、长度不能超过24
        checkTableName(tableName);

        StringBuffer sb = new StringBuffer();
        sb.append("select ");
        getAgcim3dentityAGroupKey(sb, groupKey);
        getAgcim3dentityACountKey(sb, countKey);
        sb.append(" from " + tableName + " where 1 = 1 ");
        getAgcim3dentityAWhereAppend(sb, param);
        sb.append(" group by ");
        getAgcim3dentityAGroupKeyValue(sb, groupKey);
        logger.info(sb.toString());
        List<Object> list = agcim3dentityXCustomMapper.findAllDefineSql(sb.toString());
        return list;
    }

    private void checkTableName(String baseTableName, String targetTableName) {
        //表名真实性匹配验证、长度不能超过24
        checkTableName(baseTableName);
        //表名真实性匹配验证、长度不能超过24
        checkTableName(targetTableName);
    }

    private void checkTableName(String tableName) {
        if (!tableName.startsWith(table_name) || tableName.length() >= table_name_max_length) {
            throw new SourceException("表名不规范------------表名=" + tableName);
        }
    }

    @Override
    public PageInfo<Object> compareDoubleTableColumn(String baseTableName, String targetTableName, String columns, Page page) {
        //表名真实性匹配验证、长度不能超过24
        checkTableName(baseTableName, targetTableName);

        //避免sql注入问题，判断columns是否匹配
        for(String column: columns.split(",")){
            boolean isExist = checkAgcim3dentityACulumnsIsExist(column);
            if(!isExist){
                //字段不存在，返回空数据
                throw new SourceException("字段属性不存在------------属性=" + column);
            }
        }

        StringBuffer sql = new StringBuffer();
        sql.append("select * from ");
        sql.append(baseTableName);
        sql.append(" where id in");
        sql.append(" (");
        sql.append(" select unbase.bid");
        sql.append(" from (");

        sql.append(" (select base.id bid,");
        for(String column: columns.split(",")){
            sql.append(" base." + column);
            sql.append(" b");
            sql.append(column);
            sql.append(",");
        }
        //去掉最后一个“，”
        sql.delete(sql.length() - 1, sql.length());
        sql.append(" from ");
        sql.append(baseTableName);
        sql.append(" base ) b2");

        sql.append(" left join");

        sql.append(" (select tar.id tid,");
        for(String column: columns.split(",")){
            sql.append(" tar." + column);
            sql.append(" t");
            sql.append(column);
            sql.append(",");
        }
        //去掉最后一个“，”
        sql.delete(sql.length() - 1, sql.length());
        sql.append(" from ");
        sql.append(targetTableName);
        sql.append(" tar ) t2");

        sql.append(" on b2.bid = t2.tid");
        sql.append(" ) unbase");

        sql.append(" where ");
        for(String column: columns.split(",")){
            sql.append(" unbase.b" + column);
            sql.append(" != ");
            sql.append(" unbase.t" + column);
            sql.append(" or ");
        }
        //去掉最后四个“ or ”字符，注意有空格
        sql.delete(sql.length() - 4, sql.length());

        sql.append(")");


        Page startPage = PageHelper.startPage(page);
        List<Object> list = agcim3dentityXCustomMapper.findAllDefineSql(sql.toString());
        PageInfo<Object> pageInfo = new PageInfo<>(list);
        page.setTotal(startPage.getTotal());
        return pageInfo;
        //-----------示例sql
/**
 select * from agcim3dentity_linkpart where id in
 (
 select unbase.bid
 from (
 (select base.id bid, base.geometry bgeometry, base.elementattributes belementattributes from agcim3dentity_linkpart base ) b2
 left join
 (select tar.id tid, tar.geometry tgeometry,tar.elementattributes telementattributes from agcim3dentity_修改 tar ) t2
 on b2.bid = t2.tid
 ) unbase
 where unbase.belementattributes != unbase.telementattributes
 or unbase.bgeometry != unbase.tgeometry
 )

 */

    }

    @Override
    public PageInfo<Object> compareDoubleTableModify(String baseTableName, String targetTableName, Page page) {
        //表名真实性匹配验证、长度不能超过24
        checkTableName(baseTableName, targetTableName);

        StringBuffer sql = new StringBuffer();
        sql.append("select * from ");
        sql.append(baseTableName);
        sql.append(" where objectid not in(select objectid from ");
        sql.append(targetTableName);
        sql.append(")");

        Page startPage = PageHelper.startPage(page);
        List<Object> list = agcim3dentityXCustomMapper.findAllDefineSql(sql.toString());
        PageInfo<Object> pageInfo = new PageInfo<>(list);
        page.setTotal(startPage.getTotal());
        return pageInfo;

        //-----------示例sql
        /**
         SELECT * from agcim3dentity_linkpart WHERE objectid NOT IN (SELECT objectid from  "agcim3dentity_修改")
         */
    }

    /**
     *
     * @Author: libc
     * @Date: 2020/12/16 11:02
     * @tips: 根据构件分类（catagory字段）值分类统计
     * @return Map
     *             columns:构件分类列表（做表头）
     *             countResult:统计结果（一行数据）
     * @param param 查询条件
     * @param tableName 查询表名称
     */
    @Override
    public Map countForCatagoryType(Agcim3dentityXCustom param, String tableName) {
        Map<String, Object> resultMap = new ConcurrentHashMap<>();
        // 先根据catagory字段分组获取存在的构件分类列表
        StringBuilder groupSqlSb = new StringBuilder();
        groupSqlSb.append("SELECT catagory FROM ").append(tableName).append(" GROUP BY catagory");
        List<Object> catagoryList = agcim3dentityXCustomMapper.findAllDefineSql(groupSqlSb.toString());
        // 过滤空值, 得到新集合catagorys
        List<String> catagorys = new ArrayList<>();
        for (int i = 0; i < catagoryList.size(); i++) {
            Map map = (Map) catagoryList.get(i);
            String catagory = (String) map.get("catagory");
            if (StringUtils.isEmpty(catagory.trim())) {
                continue;
            }

            catagorys.add(catagory);
        }

        // 拼接统计构件分类数量sql
        StringBuffer countSqlSb = new StringBuffer();
        countSqlSb.append("SELECT ");
        for (int i = 0; i < catagorys.size(); i++) {
            String catagoryCol = catagorys.get(i);
            if (StringUtils.isEmpty(catagoryCol)) {
                continue;
            }

            if (i != 0) {
                // 第一个前面不加逗号
                countSqlSb.append(",");
            }
            // 拼接构件分类统计表头
            countSqlSb.append(" COALESCE( SUM (CASE catagory WHEN '")
                    .append(catagoryCol)
                    .append("' THEN 1 ELSE 0 END) ,0) AS ")
                    .append(catagoryCol);
        }
        countSqlSb.append(" FROM ").append(tableName).append(" WHERE 1=1 ");
        // 添加param查询条件过滤
        getAgcim3dentityAWhereAppend(countSqlSb, param);
        // 查询
        List<Object> countList = agcim3dentityXCustomMapper.findAllDefineSql(countSqlSb.toString());

        // 封装返回值
        resultMap.put("columns",catagorys);
        resultMap.put("countResult",countList);
        return resultMap;
    }


}