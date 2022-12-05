package com.augurit.agcloud.agcom.agsupport.mapper;

import com.augurit.agcloud.agcom.agsupport.domain.AgUser;
import com.augurit.agcloud.agcom.agsupport.domain.AgUserLayer;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Augurit on 2017-04-27.
 */
@Mapper
public interface AgUserMapper {

    /**
     * 查询所有用户
     *
     * @param agUser
     * @return
     * @throws Exception
     */
    List<AgUser> findList(AgUser agUser) throws Exception;

    /**
     * 按角色id获取用户
     *
     * @param agUser
     * @param roleId
     * @return
     * @throws Exception
     */
    List<AgUser> findListByRole(@Param("agUser") AgUser agUser, @Param("roleId") String roleId) throws Exception;

    /**
     * 按多个角色查找用户
     *
     * @param roleIds
     * @return
     * @throws Exception
     */
    List<AgUser> findListByRoles(@Param("roleIds") List<String> roleIds) throws Exception;

    /**
     * 按微件id查找用户
     *
     * @param funcId
     * @return
     * @throws Exception
     */
    List<AgUser> findListByFunc(@Param("funcId") String funcId) throws Exception;

    /**
     * 按角色微件id集合查找用户
     *
     * @param roleFuncId
     * @return
     * @throws Exception
     */
    List<AgUser> findListByRoleFunc(@Param("roleFuncId") String roleFuncId) throws Exception;

    /**
     * 按角色菜单id集合查找用户
     *
     * @param roleMenuId
     * @return
     * @throws Exception
     */
    List<AgUser> findListByRoleMenu(@Param("roleMenuId") String roleMenuId) throws Exception;

    /**
     * 按角色图层id集合查找用户
     *
     * @param roleLayerId
     * @return
     * @throws Exception
     */
    List<AgUser> findListByRoleLayer(@Param("roleLayerId") String roleLayerId) throws Exception;

    /**
     * 按用户图层id集合查找用户
     *
     * @param userLayerId
     * @return
     * @throws Exception
     */
    List<String> findListByUserLayer(@Param("userLayerId") String userLayerId) throws Exception;

    /**
     * 按目录图层id集合查找用户
     *
     * @param dirLayerIds
     * @return
     * @throws Exception
     */
    List<String> findListByDirLayers(@Param("dirLayerIds") List<String> dirLayerIds) throws Exception;

    /**
     * 按图层id查找用户
     *
     * @param layerId
     * @return
     * @throws Exception
     */
    List<String> findListByLayer(@Param("layerId") String layerId) throws Exception;

    /**
     * 查找用户
     *
     * @return
     * @throws Exception
     */
    List<AgUser> findUsers() throws Exception;

    /**
     * 按目录xpath查找用户
     *
     * @param xpath
     * @return
     * @throws Exception
     */
    List<String> findListByDirXpath(@Param("xpath") String xpath) throws Exception;

    List<String> findListByParams(@Param("paramIds") String[] paramIds) throws Exception;

    /**
     * 按登录名称获取用户
     *
     * @param loginName
     * @return
     * @throws Exception
     */
    AgUser findByName(@Param("loginName") String loginName) throws Exception;

    /**
     * 保存
     *
     * @param agUser
     * @throws Exception
     */
    void save(AgUser agUser) throws Exception;

    /**
     * 修改
     *
     * @param agUser
     * @throws Exception
     */
    void update(AgUser agUser) throws Exception;

    /**
     * 删除
     *
     * @param id
     * @throws Exception
     */
    void delete(@Param("id") String id) throws Exception;

    /**
     * 根据id查询用户
     *
     * @param id
     * @return
     * @throws Exception
     */
    AgUser findById(@Param("id") String id) throws Exception;

    /**
     * 根据ids查询
     * @param agUser
     * @param ids
     * @return
     * @throws Exception
     */
    List<AgUser> findListByIds(@Param("agUser") AgUser agUser, @Param("ids") String[] ids) throws Exception;

}
