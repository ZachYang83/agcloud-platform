package com.augurit.agcloud.om.service;

import com.augurit.agcloud.opus.common.domain.OpuOmUser;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: qinyg
 * @Date: 2020/08
 * @Description:
 */
public interface ISupportOpuOmUserService {
    void saveOpuOmUser(OpuOmUser opuOmUser, String pId, String type, String paramType) throws Exception;
}
