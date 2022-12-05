package com.augurit.agcloud.agcom.agsupport.sc.bimManager.bimParamethy.service;

import com.augurit.agcloud.agcom.agsupport.domain.auto.Agcim3dproject;
import com.augurit.agcloud.agcom.agsupport.sc.bimManager.bimParamethy.domain.Agcim3dprojectCustom;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: qinyg
 * @Date: 2020/08
 * @Description:
 */
public interface IAgcim3dprojectService {

    List<Agcim3dproject> list(Agcim3dproject project);

    PageInfo<Agcim3dproject> list(Agcim3dproject project, Page page);

  /*
    // 没有在用，注释
    Object countCatagory();*/

    /**
     *
     * @Author: libc
     * @Date: 2020/12/10 16:38
     * @tips: 获取BIM项目树结构列表
     * @return List<Agcim3dprojectCustom>
     */
    List<Agcim3dprojectCustom> tree();
}
