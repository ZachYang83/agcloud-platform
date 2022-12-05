package com.augurit.agcloud.agcom.agsupport.sc.system.service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 系统service
 * Created by hunter on 2017-11-10
 */
public interface ISystem {

    boolean checkToken(String token);

    String getToken(String loginName) throws Exception;

    boolean check(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
}
