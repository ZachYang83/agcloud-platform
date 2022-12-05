package com.augurit.agcloud.om.service.impl;

import com.augurit.agcloud.agcom.agsupport.domain.AgDir;
import com.augurit.agcloud.agcom.agsupport.domain.AgLayer;
import com.augurit.agcloud.agcom.agsupport.domain.AgUserLayer;
import com.augurit.agcloud.agcom.agsupport.sc.dir.service.IAgDir;
import com.augurit.agcloud.agcom.agsupport.sc.field.service.IAgField;
import com.augurit.agcloud.agcom.agsupport.sc.layer.services.IAgLayer;
import com.augurit.agcloud.agcom.agsupport.sc.user.service.IAgUser;
import com.augurit.agcloud.framework.util.CollectionUtils;
import com.augurit.agcloud.framework.util.Md5Utils;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.agcloud.om.service.ISupportOpuOmUserService;
import com.augurit.agcloud.opus.common.domain.*;
import com.augurit.agcloud.opus.common.mapper.*;
import com.augurit.agcloud.opus.common.service.rs.OpuRsAppService;
import com.augurit.agcloud.opus.common.service.rs.OpuRsRoleService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 *  改造原来的代码：com.augurit.agcloud.opus.common.service.om.impl.OpuOmUserServiceImpl
 *
 * @Auther: qinyg
 * @Date: 2020/08
 * @Description:
 */
@Service
public class SupportOpuOmUserServiceImpl implements ISupportOpuOmUserService {
    private static Logger logger = LoggerFactory.getLogger(SupportOpuOmUserServiceImpl.class);
    @Autowired
    private OpuOmUserMapper opuOmUserMapper;
    @Autowired
    private OpuOmUserOrgMapper opuOmUserOrgMapper;
    @Autowired
    private OpuOmUserPosMapper opuOmUserPosMapper;
    @Autowired
    private OpuAcRoleUserMapper acRoleUserMapper;



    @Autowired
    private OpuRsAppService opuRsAppService;
    @Autowired
    private OpuRsRoleService opuRsRoleService;

    @Autowired
    private IAgField iAgField;
    @Autowired
    private IAgUser iAgUser;
    @Autowired
    private IAgLayer iAgLayer;
    @Autowired
    private IAgDir iAgDir;

    @Transactional
    @Override
    //改造原来的代码：com.augurit.agcloud.opus.common.service.om.impl.OpuOmUserServiceImpl
    public void saveOpuOmUser(OpuOmUser opuOmUser, String pId, String type, String paramType) throws Exception {
        opuOmUser.setUserId(UUID.randomUUID().toString());
        Short grade = Short.valueOf((short)1);
        opuOmUser.setPwdStrengthGrade(grade);
        if ("1".equals(opuOmUser.getIsPwdEncrypted())) {
            opuOmUser.setLoginPwd(Md5Utils.hash(opuOmUser.getLoginPwd()));
        }

        opuOmUser.setUserDeleted("0");
        opuOmUser.setIsOriginalAdmin("0");
//        opuOmUser.setCreater(SecurityContext.getCurrentUserName());
        //修改自己注册的用户是当前注册用户名称
        opuOmUser.setCreater(opuOmUser.getLoginName());
        opuOmUser.setCreateTime(new Date());
//        opuOmUser.setRootOrgId(SecurityContext.getCurrentOrgId());
        //修改自己注册的rootOrgId是“A”
        opuOmUser.setRootOrgId("A");
        this.opuOmUserMapper.saveUser(opuOmUser);
        if ("o".equals(type)) {
            String[] pIdArray = pId.split(",");
            String[] var6 = pIdArray;
            int var7 = pIdArray.length;

            for(int var8 = 0; var8 < var7; ++var8) {
                String str = var6[var8];
                OpuOmUserOrg opuOmUserOrg = new OpuOmUserOrg();
                opuOmUserOrg.setUserOrgId(UUID.randomUUID().toString());
                opuOmUserOrg.setOrgId(str);
                opuOmUserOrg.setUserId(opuOmUser.getUserId());
                opuOmUserOrg.setIsMain("0");
//                opuOmUserOrg.setCreater(SecurityContext.getCurrentUserName());
                //修改自己注册的用户是当前注册用户名称
                opuOmUserOrg.setCreater(opuOmUser.getLoginName());
                opuOmUserOrg.setCreateTime(new Date());
                Integer sortNo = this.opuOmUserOrgMapper.getUserMaxSortNoByOrgId(pId);
                if (sortNo == null) {
                    opuOmUserOrg.setSortNo(1);
                } else {
                    opuOmUserOrg.setSortNo(sortNo + 1);
                }

                this.opuOmUserOrgMapper.saveUserOrg(opuOmUserOrg);
            }
        } else if ("p".equals(type)) {
            OpuOmUserPos opuOmUserPos = new OpuOmUserPos();
            opuOmUserPos.setUserPosId(UUID.randomUUID().toString());
            opuOmUserPos.setPosId(pId);
            opuOmUserPos.setUserId(opuOmUser.getUserId());
            opuOmUserPos.setIsMain("0");
//            opuOmUserPos.setCreater(SecurityContext.getCurrentUserName());
            //修改自己注册的用户是当前注册用户名称
            opuOmUserPos.setCreater(opuOmUser.getLoginName());
            opuOmUserPos.setCreateTime(new Date());
            Integer sortNo = this.opuOmUserPosMapper.getUserMaxSortNoByPosId(pId);
            if (sortNo == null) {
                opuOmUserPos.setSortNo(1);
            } else {
                opuOmUserPos.setSortNo(sortNo + 1);
            }
            this.opuOmUserPosMapper.insertUserPos(opuOmUserPos);
        }

        //中大-默认授权
        if("1".equals(paramType)){
            if("210".equals(pId)){
                //对注册用户进行应用管理授权：应用管理>> 中大村屋设计系统>>用户进行授权
                assignRoleToDefaultApplicationForVillageBuilding(opuOmUser.getRootOrgId(), opuOmUser.getUserId(), opuOmUser.getLoginName());
                //对注册用户进行地图资源管理授权：地图资源管理>> 图层树>>中大-村屋设计
                authorLayerToUserForVillageBuilding(opuOmUser.getUserId());
            }
        }
        //自主cim-默认授权
        if("210".equals(pId)){
            //对注册用户进行应用管理授权：应用管理>> CIM基础平台>>用户进行授权
            assignRoleToDefaultApplicationForAgCIM(opuOmUser.getRootOrgId(), opuOmUser.getUserId(), opuOmUser.getLoginName());
            //对注册用户进行地图资源管理授权：地图资源管理>> 图层树>>图层树
            authorLayerToUserForAgCIM(opuOmUser.getUserId());
        }
    }


    private void assignRoleToDefaultApplicationForAgCIM(String orgId, String userId, String loginName){
        List<OpuRsAppSoft> opuRsAppSoftList = this.opuRsAppService.listAppInstByOrgId(orgId);
        String appSoftId = null;
        if(opuRsAppSoftList != null && opuRsAppSoftList.size() > 0){
            for(OpuRsAppSoft soft : opuRsAppSoftList){
                //如果softCode=agcim-map-cesium
                if("agcim-map-cesium".equals(soft.getSoftCode())){
                    appSoftId = soft.getAppSoftId();
                    break;
                }
            }
        }
        //通过appSoftId查找角色id
        String roleIds = null;
        List<OpuRsRole> roles = this.opuRsRoleService.listAllRolesNoPageForUserByAppId(userId, appSoftId, null);
        if(roles != null && roles.size() > 0){
            for(OpuRsRole role: roles){
                //此处就只能默认匹配一个参数：roleName=公众人员
                if("公众人员".equals(role.getRoleName())){
                    roleIds = role.getRoleId();
                    break;
                }
            }
        }
        if(appSoftId != null && roleIds != null){
            assignRoleToUser(userId, appSoftId, roleIds, loginName);
            logger.info("-------pid=210 注册用户默认授权应用softCode=agcim-map-cesium& roleName=公众人员----------");
        }
    }

    private void authorLayerToUserForAgCIM(String userId){
        try{
            AgLayer agLayer = new AgLayer();
            Page page = new Page(1, 1000);

            //默认搜索条件
//            String name = "天河区域A";
//            agLayer.setName(name);

            //需要找到图层名称如下的图层
            String searchNameTarget1 = "天河区域A";
            String searchNameTarget2 = "GD综合大楼";

            //图层dirLayerId
            String searchDirLayerId1 = null;
            String searchDirLayerId2 = null;

            //精确查询获取“图层树”图层树的id
            String agDirId = null;
            List<AgDir> secondDir = iAgDir.findSecondDir();
            if(secondDir == null){
                return ;
            }
            for(AgDir agDir: secondDir){
                if("图层树".equals(agDir.getName())){
                    agDirId = agDir.getId();
                    break;
                }
            }
            //agDirId 必须有数据
            if(agDirId == null){
                return ;
            }

            //查询图层，获取 dirLayerId
            PageInfo<AgLayer> dirLayerListPage = iAgDir.searchLayersByDirId(agLayer, page, agDirId, null, "1", "0");
            if(dirLayerListPage != null && dirLayerListPage.getList() != null && dirLayerListPage.getList().size() > 0){
                for(AgLayer layer: dirLayerListPage.getList()){
                    if(searchNameTarget1.equals(layer.getName())){
                        searchDirLayerId1 = layer.getDirLayerId();
                    }
                    if(searchNameTarget2.equals(layer.getName())){
                        searchDirLayerId2 = layer.getDirLayerId();
                    }
                }
            }
            //找到dirLayerId，进行授权
            if(searchDirLayerId1 != null || searchDirLayerId2 != null){
                String dirLayerIds = "";
                if(searchDirLayerId1 == null){
                    searchDirLayerId1 = "";
                }
                if(searchDirLayerId2 == null){
                    searchDirLayerId2 = "";
                }
                dirLayerIds = searchDirLayerId1 + "," + searchDirLayerId2;
                authorLayer(userId, dirLayerIds);
                saveLayerConfig(userId, dirLayerIds);
            }
        }catch (Exception e){

        }
    }


    /**
     * 对默认图层进行授权，先查询匹配，再授权
     * @param userId
     */
    private void authorLayerToUserForVillageBuilding(String userId){
        try{
            AgLayer agLayer = new AgLayer();
            Page page = new Page(1, 100);

            //默认搜索条件
            String name = "龙山塘";
            agLayer.setName(name);

            //需要找到图层名称如下的图层
            String searchNameTarget1 = "龙山塘村模型";
            String searchNameTarget2 = "龙山塘污水管";

            //图层dirLayerId
            String searchDirLayerId1 = null;
            String searchDirLayerId2 = null;

            //精确查询获取“中大-村屋设计”图层树的id
            String agDirId = null;
            List<AgDir> secondDir = iAgDir.findSecondDir();
            if(secondDir == null){
                return ;
            }
            for(AgDir agDir: secondDir){
                if("中大-村屋设计".equals(agDir.getName())){
                    agDirId = agDir.getId();
                    break;
                }
            }
            //agDirId 必须有数据
            if(agDirId == null){
                return ;
            }

            //查询图层，获取 dirLayerId
            PageInfo<AgLayer> dirLayerListPage = iAgDir.searchLayersByDirId(agLayer, page, agDirId, null, "1", "0");
            if(dirLayerListPage != null && dirLayerListPage.getList() != null && dirLayerListPage.getList().size() > 0){
                for(AgLayer layer: dirLayerListPage.getList()){
                    if(searchNameTarget1.equals(layer.getName())){
                        searchDirLayerId1 = layer.getDirLayerId();
                    }
                    if(searchNameTarget2.equals(layer.getName())){
                        searchDirLayerId2 = layer.getDirLayerId();
                    }
                }
            }
            //找到dirLayerId，进行授权
            if(searchDirLayerId1 != null || searchDirLayerId2 != null){
                String dirLayerIds = "";
                if(searchDirLayerId1 == null){
                    searchDirLayerId1 = "";
                }
                if(searchDirLayerId2 == null){
                    searchDirLayerId2 = "";
                }
                dirLayerIds = searchDirLayerId1 + "," + searchDirLayerId2;
                authorLayer(userId, dirLayerIds);
                saveLayerConfig(userId, dirLayerIds);
            }
        }catch (Exception e){

        }
    }
    //此处复制AgUserController的authorLayer方法
    private void authorLayer(String userIds, String  dirLayerIds) throws Exception {
        String uIds[] = userIds.split(",");
        String dIds[] = dirLayerIds.split(",");

        List<String> listDirLayerId = Arrays.asList(dIds);
        List<AgLayer> listLayer = iAgLayer.findByDirLayerIdsNotWithData(listDirLayerId);
        if (uIds.length>0 && dIds.length>0){
            List<AgUserLayer> list = new ArrayList<>();
            for (String userId:uIds){
                List<AgUserLayer> listUserLayer = iAgUser.findListByUserIdNotWithExtent(userId);
                for (String dirLayerId:dIds){
                    List<AgUserLayer> matchList = listUserLayer.stream().filter(o->o.getDirLayerId().equals(dirLayerId)).collect(Collectors.toList());
                    AgUserLayer temp = matchList.size()>0 ? matchList.get(0):null;
                    if ((temp==null || org.apache.commons.lang.StringUtils.isEmpty(temp.getId())) && org.apache.commons.lang.StringUtils.isNotEmpty(userId) && org.apache.commons.lang.StringUtils.isNotEmpty(dirLayerId)){
                        temp = new AgUserLayer();
                        //temp.setId(UUID.randomUUID().toString());
                        temp.setUserId(userId);
                        temp.setDirLayerId(dirLayerId);
                        AgLayer ag = listLayer.stream().filter(o->o.getDirLayerId().equals(dirLayerId)).findFirst().get();
                        String layerType = ag.getLayerType();
                        // 给可查询图层设置默认值
                        if ("01".equals(layerType.substring(0, 2)) || "020202".equals(layerType.substring(0, 6))|| "03".equals(layerType.substring(0, 6))|| "04".equals(layerType.substring(0, 2)) || "07".equals(layerType.substring(0, 2)) ||"000001".equals(layerType)){
                            temp.setQueryable("1");
                        }
                        temp.setEditable(null);
                        temp.setIsBaseMap(null);
                        list.add(temp);
                    }
                }
            }
            if (list.size() > 0) {
                iAgUser.saveUserLayerBatch(list);
            }
        }

    }
    //此处复制AgFieldController的saveLayerConfig方法
    private void saveLayerConfig(String userIds, String  dirLayerIds){
        AgUserLayer agUserLayer = new AgUserLayer();
        agUserLayer.setQueryable("on");
        agUserLayer.setIsShow("on");

        String[] arrUserId = userIds.split(",");
        String[] arrdirLayerId = dirLayerIds.split(",");
        for(String userid : arrUserId) {
            agUserLayer.setUserId(userid);
            for (String dirLayerId : arrdirLayerId) {
                agUserLayer.setDirLayerId(dirLayerId);
                iAgField.saveLayerConfig(agUserLayer);
            }
        }
    }

    private void assignRoleToDefaultApplicationForVillageBuilding(String orgId, String userId, String loginName){
        List<OpuRsAppSoft> opuRsAppSoftList = this.opuRsAppService.listAppInstByOrgId(orgId);
        String appSoftId = null;
        if(opuRsAppSoftList != null && opuRsAppSoftList.size() > 0){
            for(OpuRsAppSoft soft : opuRsAppSoftList){
                //如果softCode=agcim-villageBuilding
                if("agcim-villageBuilding".equals(soft.getSoftCode())){
                    appSoftId = soft.getAppSoftId();
                    break;
                }
            }
        }
        //通过appSoftId查找角色id
        String roleIds = null;
        List<OpuRsRole> roles = this.opuRsRoleService.listAllRolesNoPageForUserByAppId(userId, appSoftId, null);
        if(roles != null && roles.size() > 0){
            for(OpuRsRole role: roles){
                //此处就只能默认匹配一个参数：roleName=村屋设计系统-外部用户
                if("村屋设计系统-外部用户".equals(role.getRoleName())){
                    roleIds = role.getRoleId();
                    break;
                }
            }
        }
        if(appSoftId != null && roleIds != null){
            assignRoleToUser(userId, appSoftId, roleIds, loginName);
            logger.info("-------pid=210 注册用户默认授权应用softCode=agcim-villageBuilding& roleName=村屋设计系统-外部用户----------");
        }
    }

    public void assignRoleToUser(String userId, String appSoftId, String roleIds, String loginName) {
        bathInsertRoleUser(roleIds, appSoftId, userId, true, loginName);
//        updateUserMenuData(userId);
        if (logger.isDebugEnabled()) {
            logger.debug("成功执行角色和用户的授权，角色IDs为：{},应用为{}。", roleIds, appSoftId);
        }
    }

    public void bathInsertRoleUser(String roleIds, String appId, String userId, boolean isFirstCancel, String loginName) {
        if (isFirstCancel) {
            this.acRoleUserMapper.unAssignAllRoleUser(userId, appId);
        }
        String[] roleIdArray = roleIds.split(",");
        OpuAcRoleUser ru = new OpuAcRoleUser();
        ru.setUserId(userId);
//        ru.setOrgId(SecurityContext.getCurrentOrgId());
        //修改自己注册的rootOrgId是“A”
        ru.setOrgId("A");
//        ru.setCreater(SecurityContext.getCurrentUserName());
        //修改自己注册的用户是当前注册用户名称
        ru.setCreater(loginName);
        ru.setCreateTime(new Date());
        for(int i = 0; i < roleIdArray.length; ++i) {
            String roleId = roleIdArray[i];
            if (StringUtils.isNotBlank(roleId)) {
                List<OpuAcRoleUser> allRoleUserByUserIdAndAppIdAndRoleId = this.acRoleUserMapper.getAllRoleUserByUserIdAndAppIdAndRoleId(userId, appId, roleId);
                if (CollectionUtils.isEmpty(allRoleUserByUserIdAndAppIdAndRoleId)) {
                    this.insertRoleUser(appId, roleId, ru);
                }
            }
        }
    }
    private void insertRoleUser(String appId, String roleId, OpuAcRoleUser ru) {
        ru.setRoleUserId(UUID.randomUUID().toString());
        ru.setRoleId(roleId);
        ru.setAppId(appId);
        this.acRoleUserMapper.insertRoleUser(ru);
    }

}
