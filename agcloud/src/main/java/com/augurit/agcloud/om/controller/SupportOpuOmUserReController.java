package com.augurit.agcloud.om.controller;

import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.util.CollectionUtils;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.agcloud.om.service.ISupportOpuOmUserInfoService;
import com.augurit.agcloud.om.service.ISupportOpuOmUserService;
import com.augurit.agcloud.opus.common.domain.OpuOmUser;
import com.augurit.agcloud.opus.common.domain.OpuOmUserInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * 提供用户注册功能，
 * 改造原来的代码：com.augurit.agcloud.opus.admin.om.controller.OpuOmUserReController
 * @Auther: qinyg
 * @Date: 2020/08
 * @Description:
 */
@Api(
        description = "用户相关接口",
        tags = {"用户相关接口"}
)
@RestController
@RequestMapping({"/agsupport/om/users"})
public class SupportOpuOmUserReController {

    @Autowired
    private ISupportOpuOmUserService supportOpuOmUserService;
    @Autowired
    private ISupportOpuOmUserInfoService supportOpuOmUserInfoService;

    @ApiOperation("保存指定组织或岗位下的用户")
    @ApiImplicitParams({@ApiImplicitParam(
            name = "opuOmUserInfo",
            value = "用户信息",
            required = true,
            dataType = "OpuOmUserInfo"
    ), @ApiImplicitParam(
            name = "pId",
            value = "组织id或者岗位id（新增用户时需要传此参数）",
            required = false,
            dataType = "String"
    ), @ApiImplicitParam(
            name = "type",
            value = "类型：o表示组织,p表示岗位（新增用户时需要传此参数）",
            required = false,
            allowableValues = "o,p",
            dataType = "String"
    ) ,@ApiImplicitParam(
            name = "paramType",
            value = "注册类型：1中大项目、2自主cim项目（区分默认授权）",
            required = false,
            allowableValues = "o,p",
            dataType = "String"
    )})
    @RequestMapping(
            value = {"/saveOpuOmUser"},
            method = {RequestMethod.POST}
    )
    public ContentResultForm<OpuOmUser> saveOpuOmUser(@RequestBody OpuOmUserInfo opuOmUserInfo, String pId, String type, @RequestParam(defaultValue = "1") String paramType) throws Exception {
        String pwd = null;
        OpuOmUser paramUser;
        List opuOmUserList;
//        if (StringUtils.isNotBlank(opuOmUserInfo.getUserId())) {
//            paramUser = new OpuOmUser();
//            paramUser.setUserId(opuOmUserInfo.getUserId());
//            paramUser.setLoginName(opuOmUserInfo.getLoginName());
//            opuOmUserList = this.opuOmUserInfoService.listOpuOmUserInfoByLoginName(paramUser);
//            if (CollectionUtils.isNotEmpty(opuOmUserList)) {
//                return new ContentResultForm(false, opuOmUserInfo, "登录名已被占用");
//            }
//
//            this.opuOmUserService.updateOpuOmUser(opuOmUserInfo);
//        } else {
            paramUser = new OpuOmUser();
            paramUser.setLoginName(opuOmUserInfo.getLoginName());
            opuOmUserList = this.supportOpuOmUserInfoService.listOpuOmUserInfoByLoginName(paramUser);
            if (CollectionUtils.isNotEmpty(opuOmUserList)) {
                return new ContentResultForm(false, opuOmUserInfo, "登录名已被占用");
            }

//            if (this.initPwdConfig.getRandomCipher()) {
//                pwd = PwdUtils.generatePwdLeast2(this.initPwdConfig.getPwdLength());
//            } else {
//                pwd = this.initPwdConfig.getFixedPwd();
//            }
//
//            opuOmUserInfo.setLoginPwd(pwd);
            this.supportOpuOmUserService.saveOpuOmUser(opuOmUserInfo, pId, type, paramType);
//        }

        this.supportOpuOmUserInfoService.saveUserInfo((String)null, opuOmUserInfo);
        opuOmUserInfo.setLoginPwd(pwd);
        return new ContentResultForm(true, opuOmUserInfo, "保存成功！");
    }
}
