package com.augurit.agcloud.om.service;

import com.augurit.agcloud.opus.common.domain.OpuOmUser;
import com.augurit.agcloud.opus.common.domain.OpuOmUserInfo;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: qinyg
 * @Date: 2020/08
 * @Description:
 */
public interface ISupportOpuOmUserInfoService {
    List<OpuOmUserInfo> listOpuOmUserInfoByLoginName(OpuOmUser opuOmUser);

    void saveUserInfo(String orgId, OpuOmUserInfo form);
}
