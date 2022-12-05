package com.augurit.agcloud.agcom.agsupport.sc.func.service.impl;

import com.augurit.agcloud.agcom.agsupport.common.config.AgToken;
import com.augurit.agcloud.agcom.agsupport.common.config.OpusRestConstant;
import com.augurit.agcloud.agcom.agsupport.domain.AgFunc;
import com.augurit.agcloud.agcom.agsupport.domain.OpuRsToolBar;
import com.augurit.agcloud.agcom.agsupport.mapper.OpuRsToolBarMapper;
import com.augurit.agcloud.agcom.agsupport.sc.func.convert.ElementUiRsTreeNodeConvert;
import com.augurit.agcloud.agcom.agsupport.sc.func.convert.ElementUiToolBarUtils;
import com.augurit.agcloud.agcom.agsupport.sc.func.convert.AgFuncConverter;
import com.augurit.agcloud.agcom.agsupport.sc.func.service.IAgFunc;
import com.augurit.agcloud.agcom.agsupport.util.HttpClientUtil;
import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.elementui.ElementUiRsTreeNode;
import com.augurit.agcloud.framework.util.JsonUtils;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.agcloud.opus.common.domain.OpuRsAppSoft;
import com.augurit.agcloud.opus.common.domain.OpuRsFunc;
import com.augurit.agcloud.opus.common.mapper.OpuRsFuncMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import java.util.*;

/**
 * Created by Augurit on 2017-05-02.
 */
@Service
public class AgFuncImpl implements IAgFunc {
    private static Logger logger = LoggerFactory.getLogger(AgFuncImpl.class);

    @Value("${agcloud.opus.admin.orgId}")
    private String orgId;
    @Value("${opus-rest-url}")
    private String opusRestUrl;
    @Autowired
    private AgToken agToken;
    @Autowired
    private OpuRsToolBarMapper opuRsToolBarMapper;

    @Autowired
    private OpuRsFuncMapper opuRsFuncMapper;

    @Override
    public List<AgFunc> findFuncByUser(String userId, String isMobile, Boolean isTree, String appSoftCode, String isCloudSoft, String isAdmin) throws Exception {
        String token = agToken.checkToken();
        Map map = new HashMap();
        map.put("isTree",String.valueOf(false));
        map.put("orgId",orgId);
        map.put("userId",userId);
        map.put("appSoftCode",appSoftCode);
        map.put("isCloudSoft",isCloudSoft);
        map.put("isAdmin",isAdmin);
        List<OpuRsToolBar> opuRsAppSoftList = opuRsToolBarMapper.getAppSoftBySoftCodeAndIsCloudSoft(orgId,appSoftCode,isCloudSoft,isAdmin);
        String url = opusRestUrl + OpusRestConstant.listUserFuncs;
        String result = HttpClientUtil.getByToken(url,map,token,"utf-8");
        JSONObject json = JSONObject.fromObject(result);
        Object content = json.get("content");
        if (content == null){
            return new ArrayList<AgFunc>();
        }
        JSONArray jsonObject = JSONArray.fromObject(content.toString());
        List<Map> mapList = new ArrayList<Map>();
        for (int i=0; i<jsonObject.size(); i++) {
            JSONObject o = (JSONObject) jsonObject.get(i);
            HashMap m =  new HashMap<>();

            Iterator iterator = o.keys();
            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                Object value = o.get(key);
                m.put(key,value);
                if(key.equals("funcId")){
                    for (int j = 0; j < opuRsAppSoftList.size(); j++) {
                        if(opuRsAppSoftList.get(j).getFuncId().equals(value)){
                            m.put("pagePosition",opuRsAppSoftList.get(j).getPagePosition());
                            break;
                        }
                    }
                }

            }
            mapList.add(m);
        }
        // 过滤掉不需要的字段
        List<AgFunc> agFuncs = new ArrayList<>();
        List<AgFunc> rootFuncs = new ArrayList<>();
        for (Map m : mapList){
            AgFunc agFunc = new AgFuncConverter().convertToAgFunc(m);
            agFuncs.add(agFunc);
            Object parentFuncId = m.get("parentFuncId");
            if (parentFuncId == null  || parentFuncId.equals("null")){
                rootFuncs.add(agFunc);
            }
        }
        // 如果返回树状功能
        if (isTree){
            // 转换成树状结构
            for (AgFunc agFunc : rootFuncs){
                agFunc.setChildrenList(getChildren(agFunc.getId(),agFuncs));
            }
            return rootFuncs;
        }else {
            return agFuncs;
        }
    }

    @Override
    public List<ElementUiRsTreeNode> lltreeFunc(String appSoftId, String  funcCode, String keyword, boolean isNeedFullPath) {
        if (StringUtils.isBlank(appSoftId)) {
            throw new InvalidParameterException(new Object[]{"appSoftId"});
        } else {
            List<ElementUiRsTreeNode> list = new ArrayList();
            List<OpuRsToolBar> funcs = this.opuRsToolBarMapper.listFuncsBySoftIdAndSorting(appSoftId, keyword);
            if (funcs != null && funcs.size() > 0) {
                if (StringUtils.isBlank(keyword)) {
                    list.addAll(ElementUiToolBarUtils.buildTree(funcs, isNeedFullPath));
                } else {
                    list.addAll(ElementUiRsTreeNodeConvert.convertFuncs(funcs, isNeedFullPath));
                }
            }

            return list;
        }
    }
    @Override
    public OpuRsToolBar saveToolBar(OpuRsToolBar form) {
        if(StringUtils.isNotBlank(form.getFuncId())){
            if (form == null) {
                throw new InvalidParameterException(new Object[]{"form"});
            } else {
                form.setModifier(SecurityContext.getCurrentUserId());
                form.setModifyTime(new Date());
                this.opuRsToolBarMapper.updateToolBar(form);
                if (logger.isDebugEnabled()) {
                    logger.debug("成功更新功能对象，form对象为：{}。", JsonUtils.toJson(form));
                }

                return form;
            }
        }else{
            if (form == null) {
                throw new InvalidParameterException(new Object[]{"form"});
            } else {
                form.setFuncId(UUID.randomUUID().toString());
                form.setCreater(SecurityContext.getCurrentUserId());
                form.setCreateTime(new Date());
                form.setFuncSubCount(0);
                form.setFuncLevel(this.getModuleOrFuncLevel(form.getParentFuncId()));
                form.setFuncSortNo(this.getFuncSortNoByParentFunIdAndFoftId(form.getAppSoftId(), form.getParentFuncId()));
                form.setFuncSeq(this.getModuleOrFuncSeq(form.getFuncId(), form.getParentFuncId()));
                this.opuRsToolBarMapper.insertToolBar(form);
                if (logger.isDebugEnabled()) {
                    logger.debug("成功保存功能对象，form对象为：{}。", JsonUtils.toJson(form));
                }

                return form;
            }
        }
    }
    public Integer getModuleOrFuncLevel(String parentMenuId) {
        if (StringUtils.isNotBlank(parentMenuId)) {
            OpuRsFunc func = this.opuRsFuncMapper.getModuleOrFuncById(parentMenuId);
            if (func != null) {
                Integer parentLevel = this.opuRsFuncMapper.getModuleOrFuncById(parentMenuId).getFuncLevel();
                if (logger.isDebugEnabled()) {
                    logger.debug("成功获取ID为" + parentMenuId + "的菜单的层级，获取的层级为：{}。", JsonUtils.toJson(parentLevel));
                }

                return parentLevel == null ? 1 : new Integer(parentLevel) + 1;
            } else {
                return 1;
            }
        } else {
            return 1;
        }
    }
    public String getModuleOrFuncSeq(String funcId, String parentFuncId) {
        if (StringUtils.isBlank(funcId)) {
            throw new InvalidParameterException(new Object[]{"funcId"});
        } else if (StringUtils.isNotBlank(parentFuncId)) {
            OpuRsFunc func = this.opuRsFuncMapper.getModuleOrFuncById(parentFuncId);
            if (func != null) {
                String parentSeq = this.opuRsFuncMapper.getModuleOrFuncById(parentFuncId).getFuncSeq();
                return StringUtils.isNotBlank(parentSeq) ? parentSeq + funcId + "." : "." + funcId + ".";
            } else {
                return "." + funcId + ".";
            }
        } else {
            return "." + funcId + ".";
        }
    }

    public Integer getFuncSortNoByParentFunIdAndFoftId(String appSoftId, String parentFuncId) {
        Integer maxNum = this.opuRsFuncMapper.getFuncSortNoByParentFunIdAndFoftId(appSoftId, parentFuncId);
        if (maxNum == null) {
            maxNum = 0;
        }

        return maxNum + 1;
    }

    private List<AgFunc> getChildren(String id,List<AgFunc> list){
        List<AgFunc> children = new ArrayList<>();
        String parentId;
        for (AgFunc agFunc : list){
            parentId = agFunc.getParentFuncId();
            if (id.equals(parentId)){
                children.add(agFunc);
            }
        }
        for (AgFunc agFunc : children){
            agFunc.setChildrenList(getChildren(agFunc.getId(),list));
        }

        if (children.size() == 0){
            return null;
        }
        return children;
    }

    @Override
    public OpuRsToolBar getModuleOrFuncById(String funcId) {
        if (funcId == null) {
            throw new InvalidParameterException(new Object[]{"funcId"});
        } else {
            if (StringUtils.isBlank(funcId)) {
                throw new InvalidParameterException(new Object[]{funcId});
            } else {
                OpuRsToolBar form = this.opuRsToolBarMapper.getModuleOrFuncById(funcId);
                if (logger.isDebugEnabled()) {
                    logger.debug("成功获取功能对象，应用对象为：{}。", JsonUtils.toJson(form));
                }

                return form;
            }
        }
    }

    @Override
    public List<OpuRsToolBar> getToolBarByAppSoftId(String appSoftCode) {
        return this.opuRsToolBarMapper.getToolBarByAppSoftCode(appSoftCode);
    }

    @Override
    public OpuRsAppSoft getAppSoftBySoftCode(String softCode) {
        return this.opuRsToolBarMapper.getAppSoftBySoftCode(softCode);
    }

    @Override
    public void deleteToolBarByName(String name) {
        this.opuRsToolBarMapper.deleteToolBarByName(name);
    }

}
