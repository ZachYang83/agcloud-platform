package com.augurit.agcloud.agcom.agsupport.sc.menuDir.service;

import com.augurit.agcloud.agcom.agsupport.domain.AgMenuDir;
import com.augurit.agcloud.agcom.agsupport.sc.dir.controller.form.DirTree;
import com.augurit.agcloud.agcom.agsupport.sc.dir.util.Tree;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 海贼王に、おれはなる!!!
 * Created by Caip on 2017-11-21.
 */
public interface IAgMenuDir {
	/**
     * 获取目录中图标路径集合
     *
     * @return
     * @throws Exception
     */
    List<Object> getMiddleIcon(HttpServletRequest request) throws Exception;

    List findSoftMenu(String userName) throws Exception;

}
