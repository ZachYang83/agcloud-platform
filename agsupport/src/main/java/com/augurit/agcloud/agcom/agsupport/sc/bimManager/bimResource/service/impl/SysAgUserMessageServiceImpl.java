package com.augurit.agcloud.agcom.agsupport.sc.bimManager.bimResource.service.impl;

import com.augurit.agcloud.agcom.agsupport.common.exception.SourceException;
import com.augurit.agcloud.agcom.agsupport.domain.auto.AgPermission;
import com.augurit.agcloud.agcom.agsupport.domain.auto.AgPermissionExample;
import com.augurit.agcloud.agcom.agsupport.domain.auto.AgUserMessage;
import com.augurit.agcloud.agcom.agsupport.domain.auto.AgUserMessageExample;
import com.augurit.agcloud.agcom.agsupport.mapper.auto.AgPermissionMapper;
import com.augurit.agcloud.agcom.agsupport.mapper.auto.AgUserMessageMapper;
import com.augurit.agcloud.agcom.agsupport.sc.bimManager.bimResource.service.ISysAgCloudSystemService;
import com.augurit.agcloud.agcom.agsupport.sc.bimManager.bimResource.service.ISysAgUserMessageService;
import com.augurit.agcloud.agcom.agsupport.sc.bimManager.util.*;
import com.common.util.Common;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.ComparatorUtils;
import org.apache.commons.collections.comparators.ComparableComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: qinyg
 * @Date: 2020/08
 * @Description:
 */
@Service
public class SysAgUserMessageServiceImpl implements ISysAgUserMessageService {

    @Autowired
    private AgUserMessageMapper userMessageMapper;
    @Autowired
    private AgPermissionMapper agPermissionMapper;
    @Autowired
    private ISysAgCloudSystemService sysAgCloudSystemService;


    @Override
    public PageInfo<AgUserCustom> list(String userName, Page page) throws Exception {
        //返回值
        PageInfo<AgUserCustom> agUserCustomPageInfo = new PageInfo<AgUserCustom>();

        //对matchUsers做分页处理
        int pageNum = page.getPageNum();
        int pageSize = page.getPageSize();
        //获取用户列表
        List<AgUserCustom> agUsers = sysAgCloudSystemService.getUserList("210");
        if(agUsers != null && agUsers.size() > 0){
            //筛选数据
            if (!StringUtils.isEmpty(userName)) {
                agUsers = agUsers.stream().filter(
                        u -> !Common.isCheckNull(u.getUserName()) && u.getUserName().contains(userName)).collect(Collectors.toList()
                );
            }
            //排序
            sort(agUsers, "createTime", false);
            List<AgUserCustom> resultList = new ArrayList<>();
            int currIdx = (pageNum > 1 ? (pageNum - 1) * pageSize : 0);
            for (int i = 0; i < pageSize && i < agUsers.size() - currIdx; i++) {
                AgUserCustom userCustom = agUsers.get(currIdx + i);
                AgUserMessageExample example = new AgUserMessageExample();
                example.createCriteria().andUserIdEqualTo(userCustom.getUserId());
                //用户信息查询
                List<AgUserMessage> agUserMessages = userMessageMapper.selectByExample(example);
                if (agUserMessages != null && agUserMessages.size() > 0) {
                    userCustom.setUserType(agUserMessages.get(0).getUserType());
                    userCustom.setOrganization(agUserMessages.get(0).getOrganization());
                    userCustom.setRemark(agUserMessages.get(0).getRemark());
                } else {
                    AgUserMessage u = new AgUserMessage();
                    u.setId(UUID.randomUUID().toString());
                    u.setCreateTime(new Date());
                    u.setUserId(userCustom.getUserId());
                    u.setUserType("1");
                    u.setOrganization("奧格");
                    userMessageMapper.insert(u);
                }

                //用户授权数量和列表查询
                AgPermissionExample permissionExample = new AgPermissionExample();
                permissionExample.createCriteria().andSourceIdEqualTo(userCustom.getUserId()).andTypeEqualTo("1");
                List<AgPermission> agUserPermissions = agPermissionMapper.selectByExample(permissionExample);
                if (agUserPermissions != null && agUserPermissions.size() > 0) {
                    userCustom.setAuthsNum(agUserPermissions.size());
                    userCustom.setAuthList(agUserPermissions);
                }
                resultList.add(userCustom);
            }
            agUserCustomPageInfo.setList(resultList);
            agUserCustomPageInfo.setTotal(agUsers.size());
        }
        return agUserCustomPageInfo;
    }

    @Override
    @Transactional
    public void udpateUser(String userId, String auths, String remark) {
        //修改用户备注
        AgUserMessageExample userMessageExample = new AgUserMessageExample();
        userMessageExample.createCriteria().andUserIdEqualTo(userId);
        List<AgUserMessage> agUserMessages = userMessageMapper.selectByExample(userMessageExample);
        if (agUserMessages == null || agUserMessages.size() == 0) {
            throw new SourceException("用户数据不存在");
        }
        AgUserMessage message = agUserMessages.get(0);
        message.setRemark(remark);
        message.setModifyTime(new Date());
        userMessageMapper.updateByPrimaryKey(message);

        //用户访问权限修改，先清空所有的权限，再重新添加
        AgPermissionExample userPermissionExample = new AgPermissionExample();
        userPermissionExample.createCriteria().andSourceIdEqualTo(userId).andTypeEqualTo("1");
        agPermissionMapper.deleteByExample(userPermissionExample);
        //重新添加权限
        if (!StringUtils.isEmpty(auths)) {
            String[] authCodes = auths.split(",");
            for (String code : authCodes) {
                AgPermission permission = new AgPermission();
                permission.setId(UUID.randomUUID().toString());
                permission.setSourceId(userId);
                permission.setCode(code);
                permission.setType("1");
                agPermissionMapper.insert(permission);
            }
        }
    }

    /**
     * @param list      待排序的集合
     * @param fieldName 依据这个字段进行排序
     * @param asc       如果为true，是正序；为false，为倒序
     * @describe 依据某个字段对集合进行排序
     */
    @SuppressWarnings("unchecked")
    public static <T> void sort(List<T> list, String fieldName, boolean asc) {
        Comparator<?> mycmp = ComparableComparator.getInstance();
        mycmp = ComparatorUtils.nullLowComparator(mycmp); // 允许null
        if (!asc) {
            mycmp = ComparatorUtils.reversedComparator(mycmp); // 逆序
        }
        Collections.sort(list, new BeanComparator(fieldName, mycmp));
    }
}
