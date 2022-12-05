package com.augurit.agcloud.agcom.agsupport.sc.label.service;

import com.augurit.agcloud.agcom.agsupport.domain.AgLabel;

import java.util.List;

/**
 * Created by Administrator on 2017-05-11.
 */
public interface IAgLabel {
    /**
     * 获取所有书签
     *
     * @return
     * @throws Exception
     */
    List<AgLabel> findAllLabel(String userId) throws Exception;

    /**
     * 保存书签
     *
     * @param agLabel
     * @throws Exception
     */
    void saveLabel(AgLabel agLabel) throws Exception;

    /**
     * 修改书签
     *
     * @param agLabel
     * @throws Exception
     */
    void updateLabel(AgLabel agLabel) throws Exception;

    /**
     * 删除书签
     *
     * @param id
     * @throws Exception
     */
    void deleteLabel(String id) throws Exception;
}
