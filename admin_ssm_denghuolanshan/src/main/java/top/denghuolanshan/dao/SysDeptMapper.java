package top.denghuolanshan.dao;

import org.apache.ibatis.annotations.Param;
import top.denghuolanshan.dto.DeptLevelDto;
import top.denghuolanshan.model.SysDept;

import java.util.List;

public interface                                                                                                                                                            SysDeptMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysDept record);

    int insertSelective(SysDept record);

    SysDept selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysDept record);

    int updateByPrimaryKey(SysDept record);

    List<SysDept> getAllDept();

    List<SysDept> getChildDeptListByLevel(@Param("level")String level);

    /**
     * 批量更新部门列表
     * @param sysDeptList
     */
    void batchUpdateLevel(@Param("sysDeptList") List<SysDept> sysDeptList);

    int countByNameAndParentId(@Param("parentId")int parentId,@Param("name")String name,@Param("id")Integer id);

    /**
     * 获取所有部门
     * @return
     */
    List<SysDept> findAll();

    List<SysDept> findAllByLevel(@Param("level")Integer level);

    int countByParentId(@Param("deptId") int deptId);
}