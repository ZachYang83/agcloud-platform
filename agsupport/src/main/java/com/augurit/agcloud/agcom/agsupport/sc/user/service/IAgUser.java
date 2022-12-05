package com.augurit.agcloud.agcom.agsupport.sc.user.service;

import com.augurit.agcloud.agcom.agsupport.domain.*;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

/**
 * Created by Augurit on 2017-04-27.
 */
public interface IAgUser {

    /**
     * 根据机构id查询子机构
     *
     * @return
     * @throws Exception
     */
    String findOrgTreeById(String orgId) throws Exception;
    /**
     * 分页查询用户
     *
     * @return
     * @throws Exception
     */
    PageInfo<AgUser> searchUser(AgUser agUser, Page page) throws Exception;

    /**
     * 分页查询指定机构目录包含的用户
     *
     * @param agUser
     * @param page
     * @param orgId
     * @return
     * @throws Exception
     */
    PageInfo<AgUser> searchUser(AgUser agUser, Page page, String orgId) throws Exception;

    /**
     * 根据用户名获取用名
     * @param userName
     * @return
     * @throws Exception
     */
    List<AgUser> searchUsers(String userName) throws Exception;
    /**
     * 按角色分页查找用户
     *
     * @param agUser
     * @param page
     * @param roleId
     * @return
     * @throws Exception
     */
    PageInfo<AgUser> searchUserByRole(AgUser agUser, Page page, String roleId) throws Exception;

    List<AgUser> findOrgUsers() throws Exception;

    /**
     * 分页获取登录名称或用户名称等于name的用户
     * @param name 登录名称或用户名称
     * @param page 分页信息
     * @return
     * @throws Exception
     */
    PageInfo<AgUser> findOrgUsers(String name,Page page) throws Exception;
    /**
     * 按登录名称查找用户
     *
     * @param loginName
     * @return
     * @throws Exception
     */
    AgUser findUserByName(String loginName) throws Exception;

    /**
     * 按用户Id查找用户
     *
     * @param userId
     * @return
     * @throws Exception
     */
    AgUser findUserById(String userId) throws Exception;

    /**
     * 根据ID集合查询用户集合
     *
     * @param userIds
     * @return
     * @throws Exception
     */
    List<AgUser> findUsersByUserIds(String userIds) throws Exception;
    /**
     * 保存用户
     *
     * @param agUser
     * @throws Exception
     */
    void saveUser(AgUser agUser) throws Exception;

    /**
     * 修改用户
     *
     * @param agUser
     * @throws Exception
     */
    void updateUser(AgUser agUser) throws Exception;

    /**
     * 获取生产库中的所有机构
     *
     * @return
     * @throws Exception
     */
    List<Map> findOrgsFromProd() throws Exception;

    /**
     * 获取生产库中的所有用户
     *
     * @return
     * @throws Exception
     */
    List<Map> findUsersFromProd() throws Exception;

    /**
     * 按图层id查找用户
     *
     * @param layerId
     * @return
     * @throws Exception
     */
    List<String> findUserByLayer(String layerId) throws Exception;

    /**
     * 查找用户
     *
     * @return
     * @throws Exception
     */
    List<AgUser> findUsers() throws Exception;

    /**
     * 生成token 并保存code
     *
     * @param code      code
     * @param loginName 登录名
     * @return
     */
    boolean saveCode(String code, String loginName);

    /**
     * 通过token 获取登录名
     *
     * @param token
     * @return
     */
    String getLoginNameByToken(String token);
    /**
     * 根据用户登录名获取用户角色集合
     * @param userName
     * @return
     */
    List<Map> getRolesByUserName(String userName);

    /**
     * 根据角色ID集合
     * @param roleIds
     * @return
     */
    List<Map> listRoleByRoleIds(String roleIds);
    /**
     * 根据角色id获取用户信息
     * @param roleId
     * @return
     * @throws Exception
     */
    List<AgUser> findUserByRoleId(String roleId) throws Exception;
    /**
     * 根据多个角色id获取所有用户信息
     * @param roleIds
     * @return
     * @throws Exception
     */
    List<AgUser> findUsersByRoleIds(String roleIds) throws Exception;

    /**
     * 根据多个角色id 分页获取用户信息
     * @param roleIds
     * @return
     * @throws Exception
     */
    PageInfo<AgUser> findUsersByRoleIds(String roleIds,String userName, Page page) throws Exception;

    /**
     * 获取用户图层关联
     *
     * @param userId
     * @param dirLayerId
     * @return
     */
    AgUserLayer getUserLayer(String userId, String dirLayerId) throws Exception;

    /**
     * 获取用户图层关联,图层信息不包含Extent字段
     *
     * @param userId
     * @return
     */
    List<AgUserLayer> findListByUserIdNotWithExtent(String userId) throws Exception;

    /**
     * 批量保存用户图层关联
     *
     * @param list
     * @throws Exception
     */
    void saveUserLayerBatch(List<AgUserLayer> list) throws Exception;

    /**
     * 保存用户图层关联
     *
     * @param userLayer
     * @throws Exception
     */
    void saveUserLayer(AgUserLayer userLayer) throws Exception;

    /**
     * 分页查询图层已授权的用户
     *
     * @param agUser
     * @param dirLayerId
     * @param page
     * @return
     * @throws Exception
     */
    PageInfo<AgUser> searchUserByLayer(AgUser agUser, String dirLayerId, Page page) throws Exception;

    /**
     * 批量删除用户图层关联
     *
     * @param ids
     * @throws Exception
     */
    void delUserLayerBatch(String[] ids) throws Exception;

    /**
     * 按id删除用户图层关联
     *
     * @param id
     * @throws Exception
     */
    void deleteUserLayer(String id) throws Exception;
    /**
     * 按dirLayerId查询所有授权的用户
     *
     * @param agDirLayers
     * @throws Exception
     */
    List<AgUser> findAuthorizedUsers(List<AgDirLayer> agDirLayers) throws Exception;

    void delDirAuthor(List<AgDirLayer> agDirLayers,String userIds) throws Exception;
}
