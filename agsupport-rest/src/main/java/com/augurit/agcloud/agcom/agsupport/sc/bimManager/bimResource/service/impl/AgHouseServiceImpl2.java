package com.augurit.agcloud.agcom.agsupport.sc.bimManager.bimResource.service.impl;

import com.augurit.agcloud.agcom.agsupport.domain.auto.*;
import com.augurit.agcloud.agcom.agsupport.mapper.AgHouseCustomMapper;
import com.augurit.agcloud.agcom.agsupport.mapper.auto.AgHouseMapper;
import com.augurit.agcloud.agcom.agsupport.mapper.auto.AgPermissionMapper;
import com.augurit.agcloud.agcom.agsupport.mapper.auto.AgSysSettingMapper;
import com.augurit.agcloud.agcom.agsupport.sc.bimManager.bimResource.service.IAgHouseService;
import com.augurit.agcloud.agcom.agsupport.sc.bimManager.bimResource.service.IAgUserMessageService;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * 用户权限列表判断：1如果是agcloud注册的用户，拥有所有的权限；2如果是通过页面注册的用户（在ag_user_massage里面存在的)就需要判断权限
 *
 * @Auther: qinyg
 * @Date: 2020/07
 * @Description:
 */
@Service
public class AgHouseServiceImpl2 implements IAgHouseService {

    private static final Logger log = LoggerFactory.getLogger(AgHouseServiceImpl2.class);
    private static final String uploadTempPath = "uploadTemp";
    private static final String bim_model_path = "BimModel";

    @Autowired
    private AgHouseMapper houseMapper;
    @Autowired
    private AgSysSettingMapper sysSettingMapper;
    @Autowired
    private AgHouseCustomMapper agHouseCustomMapper;
    @Autowired
    private IAgUserMessageService agUserMessageService;
    @Autowired
    private AgPermissionMapper agPermissionMapper;

    @Value("${upload.filePath}")
    private String filePath;


    @Override
    public PageInfo<AgHouse> list(AgHouse resource, Page page) {
//        //isShow=1默认展示的信息
//
//        //当前用户的权限
//        String userId = SecurityContext.getCurrentUser().getUserId();
//        boolean isHaveFullPermission = isHaveFullPermission(userId);
//        //判断
//        if(isHaveFullPermission){
//            //具备所有权限
//            return getAgResourceNotCheckPermission(resource.getHourseName(), page);
//        }

        return getAgResourceNotCheckPermission(resource.getHourseName(), page);

//        //自行判断用户的权限
//        List<String> userPermissionParam = getUserPermission(userId);
//        //是否分配权限
//        if(userPermissionParam == null){
//            log.info("-----------无权限AgResource----------userId=" + userId);
//            return new PageInfo<AgHouse>();
//        }
//
//        //权限查询
//        return getAgResourcCheckPermission(resource.getHourseName(), page, userPermissionParam);
    }


    /**
     * 是否具有全部权限
     * @param userId
     * @return true具有； false不具有
     */
    @Override
    public boolean isHaveFullPermission(String userId){
        //此列表的都需要判断权限
        Map<String, String> list = agUserMessageService.notPermissioList();
        //数据为null，不在此数据，具备所有的列表权限
        if(StringUtils.isEmpty(list.get(userId))){
            return true;
        }
        return false;
    }

    /**
     * 获取用户已分配的权限
     * @param userId
     * @return 权限code列表
     */
    @Override
    public List<String> getUserPermission(String userId){
        List<String> result = null;
        //查找当前用户的source权限
        AgPermissionExample example = new AgPermissionExample();
        example.createCriteria().andSourceIdEqualTo(userId).andTypeEqualTo("1");
        List<AgPermission> agUserPermissions = agPermissionMapper.selectByExample(example);
        if(agUserPermissions != null && agUserPermissions.size() > 0){
            result = new ArrayList<>();
            for(AgPermission user : agUserPermissions){
                result.add(user.getCode());
            }
        }
        return result;
    }

    /**
     * 直接查询，不用检查权限
     * @param hourseName
     * @param page
     * @return
     */
    private PageInfo<AgHouse> getAgResourceNotCheckPermission(String hourseName, Page page) {
        PageHelper.startPage(page);
        //查询
        List<AgHouse> resultList = agHouseCustomMapper.select(hourseName,  "1", "create_time desc");
        return new PageInfo<AgHouse>(resultList);
    }

    /**
     * 查询，需要权限判断
     * @param hourseName
     * @param page
     * @param permisions
     * @return
     */
    private PageInfo<AgHouse> getAgResourcCheckPermission(String hourseName, Page page, List<String> permisions) {
        PageHelper.startPage(page);
        //查询
        List<AgHouse> resultList = agHouseCustomMapper.unionSelect(hourseName,  "1", "create_time desc", permisions);
        return new PageInfo<AgHouse>(resultList);
    }


    @Override
    public List<AgSysSetting> getAllSysSetting() {
        return sysSettingMapper.selectByExample(null);
    }



    @Override
    public List<AgHouse> findResourceDir(String id) {
        AgHouseExample agResourceExample = new AgHouseExample();
        agResourceExample.createCriteria().andSourceIdEqualTo(id).andTypeEqualTo("2");
        List<AgHouse> resultList = houseMapper.selectByExample(agResourceExample);
        return resultList;
    }



    /**
     * 获取系统文件上传的根路径
     *
     * @return
     * @throws FileNotFoundException
     */
    @Override
    public String getBaseFilePath() throws FileNotFoundException {
//        File basePath = new File(ResourceUtils.getURL("classpath:").getPath());
//        if (!basePath.exists()) {
//            basePath = new File("");
//        }
//        return basePath.getAbsolutePath() + File.separator + filePath;
        return filePath + File.separator + bim_model_path;
    }



}
