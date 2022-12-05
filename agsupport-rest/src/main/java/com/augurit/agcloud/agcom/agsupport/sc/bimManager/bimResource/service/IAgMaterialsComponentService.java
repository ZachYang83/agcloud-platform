package com.augurit.agcloud.agcom.agsupport.sc.bimManager.bimResource.service;

import com.augurit.agcloud.agcom.agsupport.domain.auto.AgMaterialsComponent;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import javax.servlet.http.HttpServletResponse;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: qinyg
 * @Date: 2020/08
 * @Description:
 */
public interface IAgMaterialsComponentService {

    PageInfo<AgMaterialsComponent> list(AgMaterialsComponent entity, Page page);

    void view(String type, String id, HttpServletResponse response);

}
