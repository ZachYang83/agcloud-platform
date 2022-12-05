package com.augurit.agcloud.service.impl;

import com.augurit.agcloud.domain.OpuRsToolBar;
import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.elementui.ElementUiRsTreeNode;
import com.augurit.agcloud.framework.util.JsonUtils;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.agcloud.mapper.OpuRsToolBarMapper;
import com.augurit.agcloud.opus.common.domain.OpuRsAppSoft;
import com.augurit.agcloud.opus.common.domain.OpuRsFunc;
import com.augurit.agcloud.opus.common.mapper.*;
import com.augurit.agcloud.service.OpuRsToolBarService;
import com.augurit.agcloud.util.ElementUiRsTreeNodeConvert;
import com.augurit.agcloud.util.ElementUiToolBarUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class OpuRsToolBarServiceImpl implements OpuRsToolBarService {

    private static Logger logger = LoggerFactory.getLogger(OpuRsToolBarServiceImpl.class);

    @Autowired
    private OpuRsToolBarMapper opuRsToolBarMapper;

    @Autowired
    private OpuRsFuncMapper opuRsFuncMapper;

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

    @Override
    public List<ElementUiRsTreeNode> lltreeFunc(String appSoftId,String  funcCode,String keyword, boolean isNeedFullPath) {
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

    @Override
    public boolean editPagePosition(String funcId, String pagePosition) {
        return this.opuRsToolBarMapper.editPagePosition(funcId,pagePosition)==1?true:false;
    }


}
