package com.augurit.agcloud.agcom.agsupport.sc.externalService.service;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

//@WebService(targetNamespace = "http://com.augurit.agcloud.agcom.agsupport/webservice")
public interface TestApiService {
//   @WebMethod
    String getData(String json);

//    @WebMethod
    String receiveData(String json);
}
