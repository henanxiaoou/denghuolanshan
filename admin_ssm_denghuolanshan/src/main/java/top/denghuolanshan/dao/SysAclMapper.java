package top.denghuolanshan.dao;

import org.apache.ibatis.annotations.Param;
import top.denghuolanshan.beans.PageQuery;
import top.denghuolanshan.model.SysAcl;

import java.util.List;

public interface SysAclMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysAcl record);

    int insertSelective(SysAcl record);

    SysAcl selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysAcl record);

    int updateByPrimaryKey(SysAcl record);

    int countByAclModuleId(@Param("aclModuleId")int aclModuleId);

    List<SysAcl> getPageByAclModuleId(@Param("aclModuleId")int aclModuleId,@Param("page")PageQuery page);

    int countByNameAndAclModuleId(@Param("aclModuleId")int aclModuleId,@Param("name")String name,@Param("id")Integer id);

    /**
     * 获取所有权限
     * @return
     */
    List<SysAcl> getAll();

    /**
     * 根据角色id集合获取权限id集合
     * @param idList
     * @return
     */
    List<SysAcl> getByIdList(@Param("idList")List<Integer> idList);

    List<SysAcl> getByUrl(@Param("url")String url);
}