package top.denghuolanshan.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.denghuolanshan.common.RequestHolder;
import top.denghuolanshan.dao.SysRoleUserMapper;
import top.denghuolanshan.dao.SysUserMapper;
import top.denghuolanshan.model.SysRoleUser;
import top.denghuolanshan.model.SysUser;
import top.denghuolanshan.util.IpUtil;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @ClassName SysRoleUserService
 * @Description 角色与用户业务逻辑
 * @Author 小欧
 * @Date 2019/6/15 20:28
 * @Version 1.0
 **/
@Service
public class SysRoleUserService {
    @Autowired
    private SysRoleUserMapper sysRoleUserMapper;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysLogService sysLogService;

    public List<SysUser> getListByRoleId(int roleId){
        // 获取用户id集合
        List<Integer> userIdList = sysRoleUserMapper.getUserIdListByRoleId(roleId);
        if (CollectionUtils.isEmpty(userIdList)) {
            return Lists.newArrayList();
        }
        // 根据用户id集合获取用户信息
        return sysUserMapper.getByIdList(userIdList);
    }

    public void changeRoleUsers(int roleId, List<Integer> userIdList) {
        List<Integer> originUserIdList = sysRoleUserMapper.getUserIdListByRoleId(roleId);
        if (originUserIdList.size() == userIdList.size()) {
            Set<Integer> originUserIdSet = Sets.newHashSet(originUserIdList);
            Set<Integer> userIdSet = Sets.newHashSet(userIdList);
            originUserIdSet.removeAll(userIdSet);
            if (org.apache.commons.collections.CollectionUtils.isEmpty(originUserIdSet)) {
                return;
            }
        }
        updateRoleUsers(roleId, userIdList);
        sysLogService.saveRoleUserLog(roleId,originUserIdList,userIdList);
    }
    @Transactional(rollbackFor = Exception.class)
    public void updateRoleUsers(int roleId, List<Integer> userIdList) {
        sysRoleUserMapper.deleteByRoleId(roleId);
        if (CollectionUtils.isEmpty(userIdList)) {
            return;
        }
        List<SysRoleUser> roleUserList = Lists.newArrayList();
        for (Integer userId : userIdList) {
            SysRoleUser roleUser = SysRoleUser.builder().roleId(roleId).userId(userId).operator(RequestHolder.getCurrentUser().getUsername())
                    .operateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest())).operateTime(new Date()).build();
            roleUserList.add(roleUser);
        }
        sysRoleUserMapper.batchInsert(roleUserList);
    }
}
