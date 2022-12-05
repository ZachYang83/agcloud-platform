package com.augurit;

import com.augurit.agcloud.agcom.agsupport.common.util.LogRestUrl;
import com.augurit.agcloud.agcom.agsupport.common.util.UploadUtil;
import com.augurit.agcloud.framework.ui.pager.PageArgumentResolver;
import com.common.util.Common;
import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.mapping.VendorDatabaseIdProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.mobile.device.DeviceHandlerMethodArgumentResolver;
import org.springframework.mobile.device.DeviceResolverHandlerInterceptor;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;
import java.util.List;
import java.util.Properties;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private static Logger logger = LoggerFactory.getLogger(WebMvcConfig.class);
    @Autowired
    private LogRestUrl logRestUrl;
    @Autowired
    private HttpMessageConverters messageConverters;
    private String welcomeFile = Common.getByKey("server.welcome-file");

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }


    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        logger.debug("初始化 EasyUI 分页请求参数处理器");
        argumentResolvers.add(pageArgumentResolver());
    }

    /**
     * 定义拦截器链
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(deviceResolverHandlerInterceptor());
        //加入日志打印
        registry.addInterceptor(logRestUrl);
    }


    @Bean(name="validator")
    public LocalValidatorFactoryBean getLocalValidatorFactoryBean(){
        return new LocalValidatorFactoryBean();
    }

/*    @Bean
    public InternalResourceViewResolver configureInternalResourceViewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/ui-jsp/agcloud/");
        resolver.setSuffix(".jsp");
        return resolver;
    }*/

    //自动代理
    @Bean
    public DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator daap = new DefaultAdvisorAutoProxyCreator();
        daap.setProxyTargetClass(true);
        return daap;
    }

    @Bean
    public PageArgumentResolver easyuiPageArgumentResolver() {
        return new PageArgumentResolver();
    }


    @Bean
    public PageArgumentResolver pageArgumentResolver() {
        return new PageArgumentResolver();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public com.augurit.agcloud.framework.util.JdkIdGenerator defaultIdGenerator() {
        return new com.augurit.agcloud.framework.util.JdkIdGenerator();
    }

    @Bean
    public RequestContextListener requestContextListener() {
        return new RequestContextListener();
    }

    // Opus子系统配置
    @Bean
    public DeviceResolverHandlerInterceptor deviceResolverHandlerInterceptor() {
        return new DeviceResolverHandlerInterceptor();
    }

    @Bean
    public DeviceHandlerMethodArgumentResolver deviceHandlerMethodArgumentResolver() {
        return new DeviceHandlerMethodArgumentResolver();
    }
   /* @Bean
    public
    ByteArrayHttpMessageConverter byteArrayHttpMessageConverter() {
        return new ByteArrayHttpMessageConverter();
    }*/

    /**
     * 增加mybatis多数据库配置
     * @return
     */
   @Bean
   public DatabaseIdProvider getDatabaseIdProvider() {
       DatabaseIdProvider databaseIdProvider = new VendorDatabaseIdProvider();
       Properties p = new Properties();
       p.setProperty("Oracle", "oracle");
       p.setProperty("MySQL", "mysql");
       p.setProperty("PostgreSQL", "postgresql");
       databaseIdProvider.setProperties(p);
       return databaseIdProvider;
   }
    /**
     * 设置welcome首页
     *
     * @param
     */
   /* @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("forward:" + welcomeFile);
    }*/

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
//		converters.add(new MappingPageInfo2EasyuiPageInfoConverter());
        //converters.add(new ByteArrayHttpMessageConverter());
        converters.addAll(this.messageConverters.getConverters());
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //jar所在目录
        String uploadRelativePath =UploadUtil.getUploadRelativePath();
        String uploadAbsolutePath = UploadUtil.getUploadAbsolutePath();
        File uploadFile = new File(uploadAbsolutePath);
        if(!uploadFile.exists()){
            uploadFile.mkdir();
        }
        registry.addResourceHandler("/"+uploadRelativePath+"**")
                .addResourceLocations("file:"+uploadAbsolutePath);
        /*registry
                .addResourceHandler("/**")
                .addResourceLocations("classpath:/static/")
                .setCachePeriod(31556926);*/
        // swagger 放行
        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}
