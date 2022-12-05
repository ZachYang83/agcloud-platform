package com.augurit.agcloud.agcom.agsupport.sc.applyservice.service.impl;

import com.augurit.agcloud.agcom.agsupport.common.util.ProxyUtil;
import com.augurit.agcloud.agcom.agsupport.domain.AgLayer;
import com.augurit.agcloud.agcom.agsupport.domain.AgProxyService;
import com.augurit.agcloud.agcom.agsupport.mapper.AgProxyServiceMapper;
import com.augurit.agcloud.agcom.agsupport.sc.applyservice.service.AgApplyProxyService;
import com.augurit.agcloud.agcom.agsupport.sc.dir.service.IAgDir;
import com.augurit.agcloud.agcom.syslog.service.annotation.SysLog;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.common.util.Md5Utils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author zhangmy
 * @Description: TODO
 * @date 2019-12-24 14:31
 */
@Service
public class AgApplyProxyServiceImpl implements AgApplyProxyService {
    @Autowired
    private AgProxyServiceMapper agProxyServiceMapper;
    @Autowired
    private IAgDir iAgDir;

    @Override
    public void saveProxyService(AgProxyService agProxyService) {
        agProxyServiceMapper.saveProxyService(agProxyService);
    }

    @SysLog(sysName = "地图运维",funcName = "审核代理服务")
    @Override
    public void updateProxyServiceState(String ids,String state,String approveOpinion) {
        String[] split = ids.split(",");
        for (String id : split){
            AgProxyService agProxyService = agProxyServiceMapper.getAgProxyServiceById(id);
            String oldState = agProxyService.getState();
            // 已审核通过的跳过
            if ("1".equals(oldState)){
                continue;
            }
            // 审核通过则生成uuid
            String uuid = "";
            if ("1".equals(state)){
                uuid = UUID.randomUUID().toString();
                uuid = Md5Utils.encrypt16(uuid);
                agProxyService.setUuid(uuid);
            }
            String userName = SecurityContext.getCurrentUser().getUserName();
            agProxyService.setAuditor(userName);
            agProxyService.setApproveOpinion(approveOpinion);
            agProxyService.setAuditTime(new Date());
            agProxyService.setState(state);
            agProxyServiceMapper.updateProxyServiceState(agProxyService);
        }

    }

    @Override
    public AgProxyService getAgProxyServiceById(String id) {
        return agProxyServiceMapper.getAgProxyServiceById(id);
    }

    @Override
    public void deleteBatch(String ids) {
        if (StringUtils.isNotBlank(ids)){
            String[] split = ids.split(",");
            for (String id : split){
                agProxyServiceMapper.delete(id);
            }
        }
    }

    @Override
    public PageInfo<AgProxyService> findList(AgProxyService agProxyService , Page page) throws Exception {
        PageHelper.startPage(page);
        List<AgProxyService> list = agProxyServiceMapper.findList(agProxyService);
        for (AgProxyService proxyService : list){
            String layerId = proxyService.getLayerId();
            AgLayer layer = iAgDir.findLayerByLayerId(layerId);
            String name = layer.getName();
            String url = layer.getUrl();
            String proxyUrl = getProxyUrl(url);
            proxyService.setProxyUrl(proxyUrl);
            proxyService.setServiceName(name);
        }
        return new PageInfo<AgProxyService>(list);
    }

    private String getProxyUrl(String url){
        String preUrl = ProxyUtil.getProxyPreUrl();
        Pattern pattern = Pattern.compile("/");
        Matcher findMatcher = pattern.matcher(url);
        int number = 0;
        while (findMatcher.find()) {
            number++;
            if (number == 3) {//当“/”第3次出现时停止
                break;
            }
        }
        int i = findMatcher.start();// “/” 第3次出现的位置
        String substring = url.substring(i + 1, url.length());
        return preUrl + substring;
    }
}
