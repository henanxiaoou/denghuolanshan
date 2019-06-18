package top.denghuolanshan.service;

import com.google.common.base.Preconditions;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.denghuolanshan.common.JsonData;
import top.denghuolanshan.common.RequestHolder;
import top.denghuolanshan.dao.SysDeptMapper;
import top.denghuolanshan.dao.SysUserMapper;
import top.denghuolanshan.exception.ParamException;
import top.denghuolanshan.model.SysDept;
import top.denghuolanshan.param.DeptParam;
import top.denghuolanshan.util.BeanValidator;
import top.denghuolanshan.util.IpUtil;
import top.denghuolanshan.util.LevelUtil;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @ClassName SysDeptService
 * @Description 部门逻辑
 * @Author 小欧
 * @Date 2019/6/1 22:14
 * @Version 1.0
 **/
@Service
public class SysDeptService {
    @Resource
    private SysDeptMapper sysDeptMapper;
    @Resource
    private SysUserMapper sysUserMapper;
    @Resource
    private SysLogService sysLogService;

    public void save(DeptParam param){
        BeanValidator.check(param);
        if (checkExist(param.getParentId(),param.getName(),param.getId())){
            throw new ParamException("同一层级下存在相同名称的部门");
        }
        SysDept dept = SysDept.builder().name(param.getName()).parentId(param.getParentId()).seq(param.getSeq()).remark(param.getRemark()).build();
        dept.setLevel(LevelUtil.calculateLevel(getLevel(param.getParentId()),param.getParentId()));
        dept.setOperator(RequestHolder.getCurrentUser().getUsername());
        dept.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        dept.setOperateTime(new Date());
        sysDeptMapper.insertSelective(dept);
        sysLogService.saveDeptLog(null,dept);
    }
    private boolean checkExist(Integer parentId,String deptName,Integer deptId){

        return sysDeptMapper.countByNameAndParentId(parentId,deptName,deptId) > 0;
    }

    private String getLevel(Integer deptId){
        SysDept dept = sysDeptMapper.selectByPrimaryKey(deptId);
        if (dept == null){
            return null;
        }
        return dept.getLevel();
    }

    public void update(DeptParam param) {
        BeanValidator.check(param);
        if (checkExist(param.getParentId(),param.getName(),param.getId())){
            throw new ParamException("同一层级下存在相同名称的部门");
        }
        SysDept before = sysDeptMapper.selectByPrimaryKey(param.getId());
        Preconditions.checkNotNull(before,"待更新的部门不存在");
        if (checkExist(param.getParentId(),param.getName(),param.getId())){
            throw new ParamException("同一层级下存在相同名称的部门");
        }
        SysDept after =  SysDept.builder().id(param.getId()).name(param.getName()).parentId(param.getParentId()).seq(param.getSeq()).remark(param.getRemark()).build();
        after.setLevel(LevelUtil.calculateLevel(getLevel(param.getParentId()),param.getParentId()  ));
        after.setOperator(RequestHolder.getCurrentUser().getUsername());
        after.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        after.setOperateTime(new Date());
        updateWithChild(before,after);
        sysLogService.saveDeptLog(before,after);
    }

    /**
     *
     * @param before
     * @param after
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateWithChild(SysDept before,SysDept after){
        String newLevelPrefix = after.getLevel();
        String oldLevelPrefix = before.getLevel();
        if (!after.getLevel().equals(before.getLevel())) {
            List<SysDept> deptList = sysDeptMapper.getChildDeptListByLevel(before.getLevel());
            if (CollectionUtils.isNotEmpty(deptList)) {
                for (SysDept dept : deptList) {
                    String level = dept.getLevel();
                    if (level.indexOf(oldLevelPrefix) == 0) {
                        level = newLevelPrefix+level.substring(oldLevelPrefix.length());
                        dept.setLevel(level);
                    }
                }
                sysDeptMapper.batchUpdateLevel(deptList);
            }
        }
        sysDeptMapper.updateByPrimaryKey(after);
    }

    /**
     * 删除部门
     * @param deptId
     */
    public JsonData delete(Integer deptId) {
        // 根据部门id查询出部门信息
        SysDept dept = sysDeptMapper.selectByPrimaryKey(deptId);
        Preconditions.checkNotNull(dept, "待删除的部门不存在，无法删除");
        if (sysDeptMapper.countByParentId(dept.getId()) > 0) {
            // throw new ParamException("当前部门下面有子部门，无法删除");
            return JsonData.fail("当前部门下面有子部门，无法删除");
        }
        if(sysUserMapper.countByDeptId(dept.getId()) > 0) {
            // throw new ParamException("当前部门下面有用户，无法删除");
            return JsonData.fail("当前部门下面有用户，无法删除");
        }
        sysDeptMapper.deleteByPrimaryKey(deptId);
        return JsonData.success();
    }
}
