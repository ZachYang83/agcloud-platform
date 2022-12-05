package com.augurit.agcloud.agcom.agsupport.sc.bimManager.bimResource.service;


import com.augurit.agcloud.agcom.agsupport.domain.AgComponentCodeCustom;
import com.augurit.agcloud.agcom.agsupport.domain.AgMaterialsComponentCustom;
import com.augurit.agcloud.agcom.agsupport.domain.auto.AgMaterialsComponent;
import com.augurit.agcloud.agcom.agsupport.domain.auto.AgMaterialsComponentWithBLOBs;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;


/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: qinyg
 * @Date: 2020/08
 * @Description:
 */
public interface ISysAgMaterialsComponentService {
    PageInfo<AgMaterialsComponent> list(AgMaterialsComponent entity, Page page);

    void save(AgMaterialsComponentWithBLOBs entity, MultipartFile thumbFile, MultipartFile glbFile);

    void update(AgMaterialsComponentCustom entity, AgComponentCodeCustom param, MultipartFile thumbFile, MultipartFile glbFile);

    void delete(String id);

    void batchDelete(String ids);

    void uploadglb(MultipartFile file);

    Object statistics();

    void saveRfa(MultipartFile modelFile);

    void saveDoorOrWin(AgMaterialsComponentWithBLOBs entity, MultipartFile glbFile, MultipartFile thumbFile);

    void saveGlbZip(MultipartFile modelFile);

    void view(String type, String id, HttpServletResponse response);
}
