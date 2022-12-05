package com.augurit.agcloud.agcom.agsupport.sc.bimManager.bimResource.service.impl;

import com.augurit.agcloud.agcom.agsupport.common.util.io.FileUtil;
import com.augurit.agcloud.agcom.agsupport.domain.auto.AgMaterialsComponent;
import com.augurit.agcloud.agcom.agsupport.domain.auto.AgMaterialsComponentWithBLOBs;
import com.augurit.agcloud.agcom.agsupport.mapper.AgMaterialsComponentCustomMapper;
import com.augurit.agcloud.agcom.agsupport.sc.bimManager.bimResource.service.IAgHouseService;
import com.augurit.agcloud.agcom.agsupport.sc.bimManager.bimResource.service.IAgMaterialsComponentService;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: qinyg
 * @Date: 2020/08
 * @Description:
 */
@Service
public class AgMaterialsComponentServiceImpl implements IAgMaterialsComponentService {
    private static final Logger log = LoggerFactory.getLogger(AgMaterialsComponentServiceImpl.class);

    @Autowired
    private AgMaterialsComponentCustomMapper materialsComponentCustomMapper;
    @Autowired
    private IAgHouseService resourceService;

    @Override
    public PageInfo<AgMaterialsComponent> list(AgMaterialsComponent entity, Page page) {
//        //当前用户的权限
//        String userId = SecurityContext.getCurrentUser().getUserId();
//        boolean isHaveFullPermission = resourceService.isHaveFullPermission(userId);
//        //判断
//        if(isHaveFullPermission){
//            //具备所有权限
//            return getAgMaterialsNotCheckPermission(entity, page);
//        }

        return getAgMaterialsNotCheckPermission(entity, page);

//        //自行判断用户的权限
//        List<String> userPermissionParam = resourceService.getUserPermission(userId);
//        //是否分配权限
//        if(userPermissionParam == null){
//            log.info("-----------无权限----------userId=" + userId);
//            return new PageInfo<>();
//        }
//
//        //权限查询
//        return getAgMaterialsCheckPermission(entity, page, userPermissionParam);
    }

    private PageInfo<AgMaterialsComponent> getAgMaterialsCheckPermission(AgMaterialsComponent entity,  Page page, List<String> permisions){
        PageHelper.startPage(page);
        //查询
        List<AgMaterialsComponent> resultList = materialsComponentCustomMapper.unionSelect(entity.getCatagory(), entity.getName(), "create_time desc", permisions, entity.getComponentCode(), entity.getComponentCodeName(), entity.getSpecification());
        return new PageInfo<>(resultList);
    }

    private PageInfo<AgMaterialsComponent> getAgMaterialsNotCheckPermission(AgMaterialsComponent entity,  Page page){
        PageHelper.startPage(page);
        //类型是材料模型type=1
        List<AgMaterialsComponent> resultList = materialsComponentCustomMapper.select(entity.getCatagory(), entity.getName(), "create_time desc", entity.getComponentCode(), entity.getComponentCodeName(), entity.getSpecification());
        return new PageInfo<>(resultList);
    }

    @Override
    public void view(String type, String id, HttpServletResponse response) {
        //type=1图片；type=2glb

        if("1".equals(type)){
            AgMaterialsComponentWithBLOBs withBLOBs = materialsComponentCustomMapper.selectThumb(id);
            FileUtil.writerFile(withBLOBs.getThumb(), StringUtils.isEmpty(withBLOBs.getThumbFileName()) ? UUID.randomUUID().toString() + ".jpg": withBLOBs.getThumbFileName(), false, response);
        }
        if("2".equals(type)){
            AgMaterialsComponentWithBLOBs withBLOBs = materialsComponentCustomMapper.selectGlb(id);
            FileUtil.writerFile(withBLOBs.getGlb(), StringUtils.isEmpty(withBLOBs.getGlbFileName()) ? UUID.randomUUID().toString() + ".glb": withBLOBs.getGlbFileName(), false, response);
        }
    }


}
