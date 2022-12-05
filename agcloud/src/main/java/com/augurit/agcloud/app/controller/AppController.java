package com.augurit.agcloud.app.controller;

import com.augurit.agcloud.bsc.domain.BscDicCodeItem;
import com.augurit.agcloud.bsc.sc.rule.code.service.AutoCodeNumberService;
import com.augurit.agcloud.domain.OpuRsToolBar;
import com.augurit.agcloud.domain.OpuRsToolBarVo;
import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.elementui.ElementUiRsTreeNode;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.opus.common.domain.OpuRsAppSoft;
import com.augurit.agcloud.opus.common.domain.OpuRsFunc;
import com.augurit.agcloud.opus.common.domain.OpuRsMenu;
import com.augurit.agcloud.opus.common.service.rs.OpuRsAppService;
import com.augurit.agcloud.opus.common.service.rs.OpuRsMenuService;
import com.augurit.agcloud.opus.common.utils.OpuRsMenuUtils;
import com.augurit.agcloud.service.BscDicCodeAppService;
import com.augurit.agcloud.service.OpuRsToolBarService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
@Api(
        description = "工具栏应用接口",
        tags = {"工具栏应用接口"}
)
@RestController
@RequestMapping("/app")
public class AppController {
    private static final Logger logger = LoggerFactory.getLogger(AppController.class);
    @Autowired
    private OpuRsToolBarService opuRsToolBarService;
    @Autowired
    private AutoCodeNumberService autoCodeNumberService;
    @Autowired
    private OpuRsMenuService opuRsMenuService;
    @Autowired
    private OpuRsAppService opuRsAppService;
    @Autowired
    private BscDicCodeAppService bscDicCodeAppService;

    public AppController() {
    }

    @ApiOperation(
            value = "保存模块功能微件带创建菜单",
            notes = "保存模块功能微件带创建菜单"
    )
    @ApiImplicitParams({@ApiImplicitParam(
            name = "OpuRsToolBarVo",
            value = "模块功能表单数据",
            required = true,
            dataType = "OpuRsToolBarVo",
            paramType = "body"
    )})
    @PostMapping({"/func/menu"})
    public ResultForm saveOpuRsFunc(@RequestBody OpuRsToolBarVo opuRsFuncVo) throws Exception {
        logger.debug("保存模块功能微件带创建菜单");
        OpuRsToolBar opuRsToolBar = new OpuRsToolBar();
        BeanUtils.copyProperties(opuRsToolBar, opuRsFuncVo);
        OpuRsToolBar newFrom = this.opuRsToolBarService.saveToolBar(opuRsToolBar);
        if (opuRsFuncVo != null) {
            this.handleCreateFuncMenu(newFrom, opuRsFuncVo.getIsCreateMenu(), opuRsFuncVo.getCurrNetId(), opuRsFuncVo.getNetTmnId());
        }
        return new ContentResultForm(true, newFrom);
    }

    @GetMapping({"/func/editPagePosition"})
    public ResultForm editPagePosition(String funcId, String pagePosition) {
        if (StringUtils.isBlank(funcId)) {
            throw new InvalidParameterException(new Object[]{"传递参数[funcId]为空！"});
        } else {
            boolean flag = this.opuRsToolBarService.editPagePosition(funcId,pagePosition);
            return new ContentResultForm(flag, flag?"修改成功":"修改失败");
        }
    }

    private void handleCreateFuncMenu(OpuRsFunc func, String isCreateMenu, String currNetId, String netTmnId) throws Exception {
        if (StringUtils.isNotBlank(isCreateMenu) && isCreateMenu.equals("1") && StringUtils.isNotBlank(netTmnId)) {
            OpuRsMenu menu = OpuRsMenuUtils.convertFucnToMenu(func);
            menu.setFuncId(func.getFuncId());
            menu.setIsAdmin((String)null);
            menu.setMenuCode(this.autoCodeNumberService.generate("OPU-RS-MENU", SecurityContext.getCurrentOrgId()));
            menu.setNetTmnId(netTmnId);
            OpuRsMenu searchMenu = new OpuRsMenu();
            searchMenu.setAppSoftId(menu.getAppSoftId());
            searchMenu.setMenuName(menu.getMenuName());
            searchMenu.setNetTmnId(netTmnId);
            List<OpuRsMenu> searchMenus = this.opuRsMenuService.listOpuRsMenus(searchMenu);
            if (searchMenus == null || searchMenus.size() == 0) {
                this.opuRsMenuService.importMenu(currNetId, menu);
            }
        }
    }

    @ApiOperation(
            value = "通过应用工程id获取功能集合,树列表结构",
            notes = "通过应用工程id获取功能集合,树列表结构"
    )
    @ApiImplicitParams({@ApiImplicitParam(
            name = "appSoftId",
            value = "必填",
            required = true,
            dataType = "string",
            paramType = "query"
    ), @ApiImplicitParam(
            name = "keyword",
            value = "非必填",
            dataType = "string",
            paramType = "query"
    )})
    @GetMapping({"/func/list/tree"})
    public List<ElementUiRsTreeNode> lltreeFunc(String appSoftId, String keyword) {
        if (StringUtils.isBlank(appSoftId)) {
            throw new InvalidParameterException(new Object[]{"传递参数appSoftId为空！"});
        } else {
            logger.debug("通过应用工程id获取功能集合,树列表结构", appSoftId);
            return this.opuRsToolBarService.lltreeFunc(appSoftId,"" ,keyword, true);
        }
    }

    @GetMapping({"/func"})
    public OpuRsToolBar getOpusRsFuncById(String funcId) {
        if (StringUtils.isBlank(funcId)) {
            throw new InvalidParameterException(new Object[]{"传递参数funcId为空！"});
        } else {
            logger.debug("通过id获取功能数据", funcId);
            return this.opuRsToolBarService.getModuleOrFuncById(funcId);
        }
    }

    @ApiOperation(
            value = "通过应用工程id获取功能集合,树列表结构",
            notes = "通过应用工程id获取功能集合,树列表结构"
    )

    @ApiImplicitParams({@ApiImplicitParam(
            name = "appSoftCode",
            value = "必填",
            required = true,
            dataType = "string",
            paramType = "query"
    ),@ApiImplicitParam(
            name = "funcCode",
            value = "必填",
            required = true,
            dataType = "string",
            paramType = "query"
    )})
    @GetMapping({"/getToolBar"})
    public List<ElementUiRsTreeNode> getToolBarByAppSoftId(String appSoftCode,String funcCode) {
        if (StringUtils.isBlank(appSoftCode) || StringUtils.isBlank(funcCode)) {
            throw new InvalidParameterException(new Object[]{"传递参数appSoftIdfuncCode和为空！"});
        } else {
            logger.info("通过id获取功能数据"+appSoftCode+"=="+funcCode);
            OpuRsAppSoft soft = this.opuRsToolBarService.getAppSoftBySoftCode(appSoftCode);
            logger.info("通过应用工程id获取功能集合,树列表结构,id="+soft.getAppSoftId());
            List<ElementUiRsTreeNode> result =  this.opuRsToolBarService.lltreeFunc(soft.getAppSoftId(),funcCode, "", true);
            List<ElementUiRsTreeNode> treeResult = new ArrayList<>();
            for(ElementUiRsTreeNode node : result){
                OpuRsToolBar bar = (OpuRsToolBar)node.getData();
                if(bar.getFuncCode().equals(funcCode)){
                    treeResult.add(node);
                }
            }
            return treeResult;
        }
    }
    @GetMapping({"/deleteToolBarByName"})
    public void deleteToolBarByName(String name) {
        if (StringUtils.isBlank(name)) {
            throw new InvalidParameterException(new Object[]{"传递参数funcId为空！"});
        } else {
            logger.info("通过id获取功能数据", name);
            List<OpuRsToolBar> result = new ArrayList<>();
            this.opuRsToolBarService.deleteToolBarByName(name);
        }
    }

    @GetMapping({"/widget/type"})
    public List<BscDicCodeItem> listWidgetType(String typeCode) {
        if (StringUtils.isBlank(typeCode)) {
            throw new InvalidParameterException(new Object[]{"传递参数typeCode为空！"});
        } else {
            logger.debug("获取微件类型");
            return bscDicCodeAppService.getRootOrgActiveItemsByTypeCode(typeCode);
        }
    }
}
