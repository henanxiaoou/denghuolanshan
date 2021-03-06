package top.denghuolanshan.service;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.denghuolanshan.common.RequestHolder;
import top.denghuolanshan.dao.*;
import top.denghuolanshan.exception.ParamException;
import top.denghuolanshan.model.SysRole;
import top.denghuolanshan.model.SysUser;
import top.denghuolanshan.param.RoleParam;
import top.denghuolanshan.util.BeanValidator;
import top.denghuolanshan.util.IpUtil;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName SysRoleService
 * @Description 角色管理业务逻辑
 * @Author 小欧
 * @Date 2019/6/15 10:32
 * @Version 1.0
 **/
@Service
public class SysRoleService {
    @Autowired
    private SysRoleMapper sysRoleMapper;
    @Autowired
    private SysRoleUserMapper sysRoleUserMapper;
    @Autowired
    private SysRoleAclMapper sysRoleAclMapper;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysLogService sysLogService;

    public void save(RoleParam param){
        BeanValidator.check(param);
        if (checkExist(param.getName(),param.getId())){
            throw new ParamException("角色名称已经存在");
        }
        SysRole role = SysRole.builder().name(param.getName()).type(param.getType()).
                status(param.getStatus()).type(param.getType()).remark(param.getRemark()).build();
        role.setOperator(RequestHolder.getCurrentUser().getUsername());
        role.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        role.setOperateTime(new Date());
        sysRoleMapper.insertSelective(role);
        sysLogService.saveRoleLog(null,role);

    }
    public void update(RoleParam param){
        BeanValidator.check(param);
        if (checkExist(param.getName(),param.getId())){
            throw new ParamException("角色名称已经存在");
        }
        SysRole before = sysRoleMapper.selectByPrimaryKey(param.getId());
        Preconditions.checkNotNull(before,"待更新的角色不存在");
        SysRole after = SysRole.builder().id(param.getId()).name(param.getName()).type(param.getType()).
                status(param.getStatus()).type(param.getType()).remark(param.getRemark()).build();
        after.setOperator(RequestHolder.getCurrentUser().getUsername());
        after.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        after.setOperateTime(new Date());
        sysRoleMapper.updateByPrimaryKeySelective(after);
        sysLogService.saveRoleLog(before,after);
    }

    /**
     * 获取所有角色
     * @return
     */
    public List<SysRole> getAll(){
       return sysRoleMapper.getAll();
    }

    /**
     * 检测角色传入的角色是否有重复的名称
     * @param name
     * @param id
     * @return
     */
    private boolean checkExist(String name,Integer id){
        return sysRoleMapper.countByName(name,id) > 0;
    }

    public List<SysRole> getRoleListByUserId(int userId) {
        List<Integer> roleIdList = sysRoleUserMapper.getRoleIdListByUserId(userId);
        if (CollectionUtils.isEmpty(roleIdList)) {
            return Lists.newArrayList();
        }
        return sysRoleMapper.getByIdList(roleIdList);
    }

    public List<SysRole> getRoleListByAclId(int aclId) {
        List<Integer> roleIdList = sysRoleAclMapper.getRoleIdListByAclId(aclId);
        if (CollectionUtils.isEmpty(roleIdList)) {
            return Lists.newArrayList();
        }
        return sysRoleMapper.getByIdList(roleIdList);
    }

    public List<SysUser> getUserListByRoleList(List<SysRole> roleList) {
        if (CollectionUtils.isEmpty(roleList)) {
            return Lists.newArrayList();
        }
        List<Integer> roleIdList = roleList.stream().map(role -> role.getId()).collect(Collectors.toList());
        List<Integer> userIdList = sysRoleUserMapper.getUserIdListByRoleIdList(roleIdList);
        if (CollectionUtils.isEmpty(userIdList)) {
            return Lists.newArrayList();
        }
        return sysUserMapper.getByIdList(userIdList);
    }
}
