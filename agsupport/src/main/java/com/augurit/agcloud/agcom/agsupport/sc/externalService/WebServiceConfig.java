package com.augurit.agcloud.agcom.agsupport.sc.externalService;

import com.augurit.agcloud.agcom.agsupport.sc.externalService.service.TestApiService;
import org.apache.cxf.Bus;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.xml.ws.Endpoint;

//@Configuration
public class WebServiceConfig {
//    @Bean
    public ServletRegistrationBean wsServlet(){
        return new ServletRegistrationBean(new CXFServlet(), "/ws/*");
    }

//    @Autowired
    private TestApiService testApiService;

    //   @Autowired
//    @Qualifier(Bus.DEFAULT_BUS_ID)
    private SpringBus bus;

 //   @Bean
    public Endpoint endpoint(){
        EndpointImpl endpoint = new EndpointImpl(bus, testApiService);
        endpoint.publish("/testApiService");
        return endpoint;
    }
}
