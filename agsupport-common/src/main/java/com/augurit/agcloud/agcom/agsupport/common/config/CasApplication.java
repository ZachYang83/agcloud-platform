package com.augurit.agcloud.agcom.agsupport.common.config;

import com.augurit.agcloud.agcom.agsupport.common.config.condition.CasCondition;
import com.augurit.agcloud.agcom.agsupport.common.filter.AgCasProxyReceivingTicketValidationFilter;
import com.common.util.Common;
import org.jasig.cas.client.authentication.AuthenticationFilter;
import org.jasig.cas.client.session.SingleSignOutFilter;
import org.jasig.cas.client.session.SingleSignOutHttpSessionListener;
import org.jasig.cas.client.util.AssertionThreadLocalFilter;
import org.jasig.cas.client.util.HttpServletRequestWrapperFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Hunter on 2017-04-09.
 */
@Configuration
@Conditional(CasCondition.class)
public class CasApplication {

    @Value("${server.port}")
    private String port;
    @Value("${server.cas-server-validate-url}")
    private String casServerValidateUrl;
    @Value("${server.cas-server-public-url}")
    private String casServerPublicUrl;
    @Value("${server.cas-client-ip}")
    private String casClientIp;

    public void setPort(String port) {
        this.port = port;
    }

    public void setCasServerValidateUrl(String casServerValidateUrl) {
        this.casServerValidateUrl = casServerValidateUrl;
    }

    public void setCasServerPublicUrl(String casServerPublicUrl) {
        this.casServerPublicUrl = casServerPublicUrl;
    }

    public void setCasClientIp(String casClientIp) {
        this.casClientIp = casClientIp;
    }

    public String getCasServerValidateUrl() {
        if (Common.isCheckNull(casServerValidateUrl) && !Common.isCheckNull(casServerPublicUrl)) {
            return casServerPublicUrl;
        }
        return casServerValidateUrl;
    }

    public String getCasServerPublicUrl() {
        if (Common.isCheckNull(casServerPublicUrl) && !Common.isCheckNull(casServerValidateUrl)) {
            return casServerValidateUrl;
        }
        return casServerPublicUrl;
    }

    public String getServerName() {
        String serverName = "";
        if (!Common.isCheckNull(casClientIp)) {
            if (casClientIp.indexOf(":") > 0) {
                serverName = "http://" + casClientIp;
            } else {
                serverName = "http://" + casClientIp + ":" + port;
            }
        }
        return serverName;
    }

    /**
     * 配置cas单点登录
     */
    @Bean
    public FilterRegistrationBean initFilterRegistrationBean1() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new SingleSignOutFilter());
        registration.addUrlPatterns("/*");
        registration.setName("CAS Single Sign Out Filter");
        registration.setOrder(11);
        return registration;
    }

    @Bean
    public ServletListenerRegistrationBean initServletListenerRegistrationBean() {
        ServletListenerRegistrationBean registration = new ServletListenerRegistrationBean();
        SingleSignOutHttpSessionListener singleSignOutHttpSessionListener = new SingleSignOutHttpSessionListener();
        registration.setListener(singleSignOutHttpSessionListener);
        return registration;
    }

    @Bean
    public FilterRegistrationBean initFilterRegistrationBean2() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new AgCasProxyReceivingTicketValidationFilter());
        registration.addUrlPatterns("*.do", "*.jsp");
        registration.addInitParameter("casServerUrlPrefix", getCasServerValidateUrl());
        registration.addInitParameter("serverName", getServerName());
        registration.addInitParameter("useSession", "true");
        registration.addInitParameter("exceptionOnValidationFailure", "false");
        registration.addInitParameter("redirectAfterValidation", "true");
        registration.setName("CAS Validation Filter");
        registration.setOrder(12);
        return registration;
    }

    @Bean
    public FilterRegistrationBean initFilterRegistrationBean3() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new AuthenticationFilter());
        registration.addUrlPatterns("*.do", "*.jsp");
        registration.addInitParameter("casServerLoginUrl", getCasServerPublicUrl() + "/login");
        registration.addInitParameter("casServerLogoutUrl", getCasServerPublicUrl() + "/logout");
        registration.addInitParameter("casIgnoreUrls", Common.getByKey("server.ignore-urls", ""));
        registration.addInitParameter("serverName", getServerName());
        registration.setName("CAS Authentication Filter");
        registration.setOrder(13);
        return registration;
    }

    @Bean
    public FilterRegistrationBean initFilterRegistrationBean4() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new HttpServletRequestWrapperFilter());
        registration.addUrlPatterns("/*");
        registration.setName("CAS HttpServletRequest Wrapper Filter");
        registration.setOrder(14);
        return registration;
    }

    @Bean
    public FilterRegistrationBean initFilterRegistrationBean5() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new AssertionThreadLocalFilter());
        registration.addUrlPatterns("/*");
        registration.setName("CAS Assertion Thread Local Filter");
        registration.setOrder(15);
        return registration;
    }
}
