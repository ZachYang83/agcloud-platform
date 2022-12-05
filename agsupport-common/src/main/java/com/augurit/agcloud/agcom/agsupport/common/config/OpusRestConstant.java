package com.augurit.agcloud.agcom.agsupport.common.config;

/**
 * @author zhangmingyang
 * @Description: opus-rest 接口地址
 * @date 2019-07-16 19:56
 */
public interface OpusRestConstant {
    /*根据机构id查询机构*/
    String getOpuOmOrgAsyncZTree = "/rest/opus/om/getOpuOmOrgAsyncZTree.do";
    /*根据角色ID查询用户*/
    String listOpuOmUserByRoleId = "/rest/opus/om/listOpuOmUserByRoleId.do";
    /*根据多个角色id 查询用户*/
    String listOpuOmUserByRoleIdsNoPage = "/rest/opus/om/listOpuOmUserByRoleIdsNoPage";
    /*根据多个角色id 分页查询查询用户*/
    String listOpuOmUserByRoleIds = "/rest/opus/om/listOpuOmUserByRoleIds";
    /*当前组织下的所有用户信息*/
    String listAllOpuOmUserInfoByOrgId = "/rest/opus/om/listAllOpuOmUserInfoByOrgId.do";
    /*根据登录名查询用户信息*/
    String getOpuOmUserInfoByUserId = "/rest/opus/om/getOpuOmUserInfoByUserId.do";
    /*根据用户id查询用户信息*/
    String listUserByUserIds = "/rest/opus/om/listUserByUserIds.do";
    /* 根据类型编码获取字典子项列表 */
    String lgetItemsByTypeCode = "/rest/bsc/dic/code/lgetItemsByTypeCode.do";
    /* 根据类型编码和子项的名称获取子项 */
    String listItemByTypeCodeAndItemName = "/rest/bsc/dic/code/listItemByTypeCodeAndItemName.do";
    /* 根据用户id获取功能 */
    String listUserFuncs = "/rest/opus/rs/listUserFuncs.do";
    /* 获取某组织某用户的菜单数据 */
    String listUserMenus = "/rest/opus/rs/listUserMenus.do";
    /* 获取某组织某用户的应用数据 */
    String listUserApp = "/rest/opus/rs/listUserApp.do";
    /* 根据组织ID获取组织下所有用户 */
    String listAllOpuOmUserByOrgId = "/rest/opus/om/listAllOpuOmUserByOrgId.do";
    /* 分页获取应用下角色集合 */
    String listAllRolesByAppId = "/rest/opus/rs/listAllRolesByAppId.do";
    /* 根据用户登录名获取用户角色集合 */
    String getRolesByUserName = "/rest/opus/ac/getRolesByUserName.do";
    /*根据ID集合分页查询角色集合*/
    String listRoleByRoleIds = "/rest/opus/rs/listRoleByRoleIds.do";
}
