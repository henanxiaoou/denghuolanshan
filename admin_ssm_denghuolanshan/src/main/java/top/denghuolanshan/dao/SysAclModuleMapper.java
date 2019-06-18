package top.denghuolanshan.dao;

import org.apache.ibatis.annotations.Param;
import top.denghuolanshan.model.SysAclModule;

import java.util.List;

public interface SysAclModuleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysAclModule record);

    int insertSelective(SysAclModule record);

    SysAclModule selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysAclModule record);

    int updateByPrimaryKey(SysAclModule record);

    int countByNameAndParentId(@Param("parendId") Integer parendId,@Param("aclModuleName") String aclModuleName, @Param("id") Integer id);

    List<SysAclModule> getChildAclModuleListByLevel(@Param("level") String level);

    void batchUpdateLevel(@Param("aclModuleList") List<SysAclModule> aclModuleList);

    /**
     * 获取所有权限模块
     * @return
     */
    List<SysAclModule> getAllAclModule();

    int countByParentId(@Param("aclModuleId") int aclModuleId);
}