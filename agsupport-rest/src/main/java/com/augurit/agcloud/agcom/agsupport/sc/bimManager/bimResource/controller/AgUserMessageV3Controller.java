package com.augurit.agcloud.agcom.agsupport.sc.bimManager.bimResource.controller;

import com.augurit.agcloud.agcom.agsupport.common.exception.SourceException;
import com.augurit.agcloud.agcom.agsupport.domain.auto.AgUserMessage;
import com.augurit.agcloud.agcom.agsupport.sc.bimManager.bimResource.service.IAgUserMessageService;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 *
 * @Author: qinyg
 * @Date: 2020/12/14 14:05
 * @tips:
 *
 */
@RestController
@RequestMapping("/agsupport/bimResource/user")
@Api(value = "用户信息管理接口", description = "用户信息管理接口")
public class AgUserMessageV3Controller {
    private static final Logger logger = LoggerFactory.getLogger(AgUserMessageV3Controller.class);
    @Autowired
    private IAgUserMessageService messageService;

    @ApiOperation(value = "添加用户信息",notes = "添加用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "String", required = true),
            @ApiImplicitParam(name = "organization", value = "机构", dataType = "String"),
            @ApiImplicitParam(name = "userType", value = "用户类型（保存字典code的值）", dataType = "String"),
            @ApiImplicitParam(name = "securityQuestion", value = "密保问题", dataType = "String"),
            @ApiImplicitParam(name = "securityAnswer", value = "密保答案", dataType = "String"),

    })
    @PostMapping(value = "/add")
    public ResultForm add(AgUserMessage user){
        try{
            messageService.add(user);
            return new ResultForm(true, "添加成功");
        }catch (SourceException e){
            logger.info(e.getMessage());
            return new ResultForm(false, e.getMessage());
        }catch (Exception e){
            logger.info(e.getMessage());
            return new ResultForm(false, "添加失败");
        }
    }

}
