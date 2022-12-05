package com.augurit.agcloud.agcom.agsupport.sc.label.service.impl;

import com.augurit.agcloud.agcom.agsupport.domain.AgLabel;
import com.augurit.agcloud.agcom.agsupport.mapper.AgLabelMapper;
import com.augurit.agcloud.agcom.agsupport.sc.dir.util.StringUtils;
import com.augurit.agcloud.agcom.agsupport.sc.label.service.IAgLabel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2017-05-11.
 */
@Service
public class AgLabelImpl implements IAgLabel {
    @Autowired
    private AgLabelMapper agLabelMapper;

    @Override
    public List<AgLabel> findAllLabel(String userId) throws Exception {
        if (StringUtils.isEmpty(userId)) return null;
        return agLabelMapper.findList(userId);
    }

    @Override
    public void saveLabel(AgLabel agLabel) throws Exception {
        if (agLabel == null) return;
        agLabelMapper.save(agLabel);
    }

    @Override
    public void updateLabel(AgLabel agLabel) throws Exception {
        if (agLabel == null) return;
        agLabelMapper.update(agLabel);
    }

    @Override
    public void deleteLabel(String id) throws Exception {
        agLabelMapper.delete(id);
    }
}
