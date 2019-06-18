package top.denghuolanshan.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.denghuolanshan.common.RequestHolder;
import top.denghuolanshan.dao.SysRoleAclMapper;
import top.denghuolanshan.model.SysRoleAcl;
import top.denghuolanshan.util.IpUtil;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @ClassName SysRoleAclService
 * @Description 角色与权限业务逻辑
 * @Author 小欧
 * @Date 2019/6/15 20:28
 * @Version 1.0
 **/
@Service
public class SysRoleAclService {
    @Autowired
    private SysRoleAclMapper sysRoleAclMapper;
    @Autowired
    private SysLogService sysLogService;

    public void changeRoleAcls(Integer roleId, List<Integer> aclIdList) {
        List<Integer> originAclIdList = sysRoleAclMapper.getAclIdListByRoleIdList(Lists.newArrayList(roleId));
        if (originAclIdList.size() == aclIdList.size()) {
            Set<Integer> originAclIdSet = Sets.newHashSet(originAclIdList);
            Set<Integer> aclIdSet = Sets.newHashSet(aclIdList);
            originAclIdSet.removeAll(aclIdSet);
            if (CollectionUtils.isEmpty(originAclIdSet)) {
                return;
            }
        }
        updateRoleAcls(roleId, aclIdList);
        sysLogService.saveRoleAclLog(roleId,originAclIdList,aclIdList);
    }
    @Transactional(rollbackFor = Exception.class)
    public void updateRoleAcls(int roleId, List<Integer> aclIdList) {
        sysRoleAclMapper.deleteByRoleId(roleId);

        if (CollectionUtils.isEmpty(aclIdList)) {
            return;
        }
        List<SysRoleAcl> roleAclList = Lists.newArrayList();
        for(Integer aclId : aclIdList) {
            SysRoleAcl roleAcl = SysRoleAcl.builder().roleId(roleId).aclId(aclId).operator(RequestHolder.getCurrentUser().getUsername())
                    .operateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest())).operateTime(new Date()).build();
            roleAclList.add(roleAcl);
        }
        sysRoleAclMapper.batchInsert(roleAclList);
    }
}
