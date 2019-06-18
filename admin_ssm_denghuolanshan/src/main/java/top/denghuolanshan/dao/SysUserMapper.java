package top.denghuolanshan.dao;

import org.apache.ibatis.annotations.Param;
import top.denghuolanshan.beans.PageQuery;
import top.denghuolanshan.model.SysUser;

import java.util.List;

public interface SysUserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysUser record);

    int insertSelective(SysUser record);

    SysUser selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysUser record);

    int updateByPrimaryKey(SysUser record);

    /**
     * 根据用户名或邮箱查询用户
     * @param keyword
     * @return
     */
    SysUser findByKeyword(@Param("keyword") String keyword);

    /**
     * 根据邮箱查询用户是否存在,id的作用主要时在执行更新操作时使用此接口加上的，添加用户时不需要
     * @param mail
     * @param id
     * @return
     */
    int countByMail(@Param("mail")String mail,@Param("id")Integer id);

    /**
     * 根据电话查询用户是否存在,id的作用主要时在执行更新操作时使用此接口加上的，添加用户时不需要
     * @param telephone
     * @param id
     * @return
     */
    int countByTelephone(@Param("telephone")String telephone,@Param("id")Integer id);

    /**
     * 根据部门名称查询所有用户
     * @param deptId
     * @return
     */
    int countByDeptId(@Param("deptId") int deptId);

    /**
     * 根据部门id查询出改部门所有用户并分页显示
     * @param deptId
     * @param page
     * @return
     */
    List<SysUser> getPageByDeptId(@Param("deptId") int deptId, @Param("page") PageQuery page);

    /**
     * 根据用户id集合获取用户信息
     * @param idList
     * @return
     */
    List<SysUser> getByIdList(@Param("idList")List<Integer> idList);

    /**
     * 获取所有的用户
     * @return
     */
    List<SysUser> getAll();
}