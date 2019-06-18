package top.denghuolanshan.service;

import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.denghuolanshan.beans.CacheKeyConstants;
import top.denghuolanshan.common.RequestHolder;
import top.denghuolanshan.dao.SysAclMapper;
import top.denghuolanshan.dao.SysRoleAclMapper;
import top.denghuolanshan.dao.SysRoleUserMapper;
import top.denghuolanshan.model.SysAcl;
import top.denghuolanshan.model.SysUser;
import top.denghuolanshan.util.JsonMapper;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @ClassName SysCoreService
 * @Description 用户权限心业务逻辑
 * @Author 小欧
 * @Date 2019/6/15 15:02
 * @Version 1.0
 **/
@Service
public class SysCoreService {
    @Autowired
    private SysAclMapper sysAclMapper;
    @Autowired
    private SysRoleUserMapper sysRoleUserMapper;
    @Autowired
    private SysRoleAclMapper sysRoleAclMapper;
    @Autowired
    private SysCacheService sysCacheService;

    /**
     * 获取当前用户的权限列表
     * @return
     */
    public List<SysAcl> getCurrentUserAclList(){
        int userId = RequestHolder.getCurrentUser().getId();
        return getUserAclList(userId);
    }

    /**
     * 获取当前角色的权限列表
     * @return
     */
    public List<SysAcl> getRoleAclList(int roleId){
        // 获取角色id集合，我这里直接复用了同一个方法，所以需要将roleId放在一个集合中
        List<Integer> aclIdList = sysRoleAclMapper.getAclIdListByRoleIdList(Lists.newArrayList(roleId));
        if (CollectionUtils.isEmpty(aclIdList)){
            return Lists.newArrayList();
        }
        return sysAclMapper.getByIdList(aclIdList);
    }

    /**
     * 通过id查询权限点
     * @param userId
     * @return
     */
    public List<SysAcl> getUserAclList(int userId){
        // 如果当前用户是超级管理员那么直接取出Acl中的数据
        if (isSuperAdmin()){
            sysAclMapper.getAll();
        }
        // 获取当前用户已经分配的角色id
        List<Integer> userRoleIdList = sysRoleUserMapper.getRoleIdListByUserId(userId);
        // 判断当前用户是否已经分配角色
        if (CollectionUtils.isEmpty(userRoleIdList)){
            // 当前用户没有分配角色，直接返回一个空集合
            return Lists.newArrayList();
        }
        // 如果用户有分配角色,获取用户已分配的权限的列表id
        List<Integer> userAclIdList = sysRoleAclMapper.getAclIdListByRoleIdList(userRoleIdList);
        if (CollectionUtils.isEmpty(userAclIdList)){
            return Lists.newArrayList();
        }
        return sysAclMapper.getByIdList(userAclIdList);
    }

    /**
     * 是否是超级用户
     * @return
     */
    public boolean isSuperAdmin(){
        // 这里是我自己定义了一个假的超级管理员规则，实际中要根据项目进行修改
        // 可以是配置文件获取，可以指定某个用户，也可以指定某个角色
        SysUser sysUser = RequestHolder.getCurrentUser();
        if (sysUser.getMail().contains("admin")) {
            return true;
        }
        return false;
    }

    /**
     * 用户是否有权限访问
     * @param url
     * @return
     */
    public boolean hasUrlAcl(String url){
        if (isSuperAdmin()) {
            return true;
        }
        List<SysAcl> aclList = sysAclMapper.getByUrl(url);
        if (CollectionUtils.isEmpty(aclList)){
            return true;
        }
        List<SysAcl> userAclList = getCurrentUserAclListFromCache();
        Set<Integer> userAclIdSet = userAclList.stream().map(acl -> acl.getId()).collect(Collectors.toSet());
        boolean hasValidAcl = false;
        // 规则:只要有一个权限点有权限，那么我们就认为有访问权限
        for (SysAcl acl : aclList) {
            // 判断一个用户是否具有某个权限点的访问权限
            if (acl == null || acl.getStatus() != 1){
                return true;
            }
            hasValidAcl = true;
            if (userAclIdSet.contains(acl.getId())){
                return true;
            }
        }
        if (!hasValidAcl){
            return true;
        }
        return false;
    }
    public List<SysAcl> getCurrentUserAclListFromCache(){
        Integer userId = RequestHolder.getCurrentUser().getId();
        String cacheValue = sysCacheService.getFromCache(CacheKeyConstants.USER_ACLS,String.valueOf(userId));
        if (StringUtils.isBlank(cacheValue)){
            List<SysAcl> aclList = getCurrentUserAclList();
            if (CollectionUtils.isNotEmpty(aclList)) {
                sysCacheService.saveCache(JsonMapper.obj2String(aclList),
                        600,CacheKeyConstants.USER_ACLS,
                        String.valueOf(userId));
            }
            return aclList;
        }
        return JsonMapper.string2obj(cacheValue, new TypeReference<List<SysAcl>>() {
        });
    }
}
