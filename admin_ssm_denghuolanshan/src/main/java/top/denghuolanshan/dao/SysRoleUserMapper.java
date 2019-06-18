package top.denghuolanshan.dao;

import org.apache.ibatis.annotations.Param;
import top.denghuolanshan.model.SysRoleUser;

import java.util.List;

public interface SysRoleUserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysRoleUser record);

    int insertSelective(SysRoleUser record);

    SysRoleUser selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysRoleUser record);

    int updateByPrimaryKey(SysRoleUser record);

    /**
     * 根据用户id查询角色id列表
     * @param userId
     * @return
     */
    List<Integer> getRoleIdListByUserId(@Param("userId")int userId);

    /**
     * 根据角色id获取对应的用户
     * @param roleId
     * @return
     */
    List<Integer> getUserIdListByRoleId(@Param("roleId") int roleId);

    void deleteByRoleId(@Param("roleId") int roleId);

    void batchInsert(@Param("roleUserList") List<SysRoleUser> roleUserList);

    List<Integer> getUserIdListByRoleIdList(@Param("roleIdList") List<Integer> roleIdList);
}