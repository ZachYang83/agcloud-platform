package com.augurit.agcloud.om.service.impl;

import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.util.JsonUtils;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.agcloud.om.service.ISupportOpuOmUserInfoService;
import com.augurit.agcloud.opus.common.domain.OpuOmUser;
import com.augurit.agcloud.opus.common.domain.OpuOmUserInfo;
import com.augurit.agcloud.opus.common.mapper.OpuOmUserInfoMapper;
import com.augurit.agcloud.opus.common.sc.scc.api.om.UpdateUserInfoCommand;
import com.augurit.agcloud.opus.common.sc.scc.api.om.UserInfoCreateOpsCommandSupport;
import com.augurit.agcloud.opus.common.sc.scc.runtime.kernal.engine.OpusSccCommandFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 * 改造原来的代码：com.augurit.agcloud.opus.common.service.om.impl.OpuOmUserInfoServiceImpl
 *
 * @Auther: qinyg
 * @Date: 2020/08
 * @Description:
 */
@Service
public class SupportOpuOmUserInfoServiceImpl implements ISupportOpuOmUserInfoService {
    private static Logger logger = LoggerFactory.getLogger(SupportOpuOmUserInfoServiceImpl.class);

    @Autowired
    private OpuOmUserInfoMapper opuOmUserInfoMapper;

    @Override
    public List<OpuOmUserInfo> listOpuOmUserInfoByLoginName(OpuOmUser opuOmUser) {
        return opuOmUser != null && StringUtils.isNotBlank(opuOmUser.getLoginName()) ? this.opuOmUserInfoMapper.listOpuOmUserInfoByLoginName(opuOmUser) : null;
    }

    @Override
    public void saveUserInfo(String orgId, OpuOmUserInfo form) {

        if (form == null) {
            throw new InvalidParameterException(new Object[]{"form"});
        } else {
            if (form.getOpuOmId() != null && form.getOpuOmId().trim().length() > 0) {
                OpuOmUserInfo formold = this.opuOmUserInfoMapper.getUserInfo(form.getOpuOmId());
                ((UpdateUserInfoCommand) OpusSccCommandFactory.getCommand(UpdateUserInfoCommand.class)).execute(orgId, form);
                if (logger.isDebugEnabled()) {
                    logger.debug("成功更用户信息，用户主键为：{}。", form.getOpuOmId());
                }
            } else {
                form.setId(UUID.randomUUID().toString());
                form.setUserDeleted("0");
                //修改自己注册的用户是当前注册用户名称
//                form.setCreater(SecurityContext.getCurrentUserName());
                form.setCreater(form.getLoginName());
                form.setCreateTime(new Date());
                ((UserInfoCreateOpsCommandSupport)OpusSccCommandFactory.getCommand(UserInfoCreateOpsCommandSupport.class)).execute(orgId, form);
                if (logger.isDebugEnabled()) {
                    logger.debug("成功新增用户信息，form对象为：{}。", JsonUtils.toJson(form));
                }
            }

        }

    }
}
