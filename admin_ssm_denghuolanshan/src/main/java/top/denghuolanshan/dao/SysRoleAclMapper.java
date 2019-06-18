package top.denghuolanshan.dao;

import org.apache.ibatis.annotations.Param;
import top.denghuolanshan.model.SysRoleAcl;

import java.util.List;

public interface SysRoleAclMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysRoleAcl record);

    int insertSelective(SysRoleAcl record);

    SysRoleAcl selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysRoleAcl record);

    int updateByPrimaryKey(SysRoleAcl record);

    /**
     * 根据角色id集合获取该角色的权限id集合
     * @param roleIdList
     * @return
     */
    List<Integer> getAclIdListByRoleIdList(@Param("roleIdList") List<Integer> roleIdList);

    void deleteByRoleId(@Param("roleId")int roleId);

    void batchInsert(@Param("roleAclList")List<SysRoleAcl> roleAclList);

    List<Integer> getRoleIdListByAclId(@Param("aclId") int aclId);
}