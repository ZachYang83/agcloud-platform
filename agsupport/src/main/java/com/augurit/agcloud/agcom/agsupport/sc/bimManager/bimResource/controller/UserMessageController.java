package com.augurit.agcloud.agcom.agsupport.sc.bimManager.bimResource.controller;


import com.augurit.agcloud.agcom.agsupport.common.exception.SourceException;
import com.augurit.agcloud.agcom.agsupport.sc.bimManager.bimResource.service.ISysAgUserMessageService;
import com.augurit.agcloud.agcom.agsupport.sc.bimManager.util.AgUserCustom;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: qinyg
 * @Date: 2020/08
 * @Description:
 */
@RestController
@RequestMapping("/agsupport/BIM/user")
@Api(value = "后台管理-用户相关接口", description = "后台管理-用户相关接口")
public class UserMessageController {
    private static final Logger logger = LoggerFactory.getLogger(UserMessageController.class);

    @Autowired
    private ISysAgUserMessageService userMessageService;

    @GetMapping("/find")
    @ApiOperation(value = "用户列表",notes = "用户列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "分页参数:page=1&rows=10", dataType = "int"),
            @ApiImplicitParam(name = "rows", value = "分页参数:page=1&rows=10", dataType = "int"),
    })
    public ResultForm find(String userName, Page page){
        try{
            PageInfo<AgUserCustom> list = userMessageService.list(userName, page);
            return new ContentResultForm(true, PageHelper.toEasyuiPageInfo(list));
        }catch (SourceException e){
            logger.info(e.getMessage());
            return new ResultForm(false, e.getMessage());
        }catch (Exception e){
            logger.info(e.getMessage());
            return new ResultForm(false, "查询失败");
        }
    }

    @PutMapping("/update")
    @ApiOperation(value = "修改用户授权",notes = "修改用户授权")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "String"),
            @ApiImplicitParam(name = "auths", value = "数据访问权限（保存字典的code值，多个用逗号分隔）", dataType = "String"),
            @ApiImplicitParam(name = "remark", value = "备注", dataType = "String"),
    })
    public ResultForm update(String userId, String auths, String remark){
        if(StringUtils.isEmpty(userId)){
            return new ResultForm(false, "userId不能为空");
        }
        try{
            userMessageService.udpateUser(userId, auths, remark);
            return new ResultForm(true, "修改成功");
        }catch (SourceException e){
            logger.info(e.getMessage());
            return new ResultForm(false, e.getMessage());
        }catch (Exception e){
            logger.info(e.getMessage());
            return new ResultForm(false, "修改失败");
        }
    }
}
