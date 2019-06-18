package top.denghuolanshan.dao;

import com.sun.xml.internal.bind.v2.TODO;
import org.apache.ibatis.annotations.Param;
import top.denghuolanshan.model.SysRole;

import java.util.List;

public interface SysRoleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysRole record);

    int insertSelective(SysRole record);

    SysRole selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysRole record);

    int updateByPrimaryKey(SysRole record);

    /**
     * 获取所有的角色
     * @return
     */
    List<SysRole> getAll();

    int countByName(@Param("name")String name,@Param("id")Integer id);

    List<SysRole> getByIdList(@Param("idList") List<Integer> idList);
}