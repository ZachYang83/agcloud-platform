package com.augurit.agcloud.agcom.agsupport.common.filter;

import com.augurit.agcloud.agcom.agsupport.util.DomainCheck;
import com.common.util.Common;
import org.jasig.cas.client.validation.Assertion;
import org.jasig.cas.client.validation.Cas20ProxyReceivingTicketValidationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 单点登录处理
 *
 * @author Hunter
 * @Time 2017/11/27
 */
public class AgCasProxyReceivingTicketValidationFilter extends Cas20ProxyReceivingTicketValidationFilter {
    @Override
    protected void onSuccessfulValidation(HttpServletRequest request, HttpServletResponse response, Assertion assertion) {
        super.onSuccessfulValidation(request, response, assertion);
        //登录成功后将该ip设置为白名单
        String ip = Common.getIpAddr(request);
        if (!DomainCheck.allowIpSet.contains(ip)) {
            DomainCheck.allowIpSet.add(ip);
        }
    }
}
