package top.denghuolanshan.service;

import com.google.common.base.Preconditions;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.denghuolanshan.common.RequestHolder;
import top.denghuolanshan.dao.SysAclMapper;
import top.denghuolanshan.dao.SysAclModuleMapper;
import top.denghuolanshan.exception.ParamException;
import top.denghuolanshan.model.SysAclModule;
import top.denghuolanshan.model.SysDept;
import top.denghuolanshan.param.AclModuleParam;
import top.denghuolanshan.util.BeanValidator;
import top.denghuolanshan.util.IpUtil;
import top.denghuolanshan.util.LevelUtil;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @ClassName SysAclModuleService
 * @Description 权限逻辑
 * @Author 小欧
 * @Date 2019/6/9 14:39
 * @Version 1.0
 **/
@Service
public class SysAclModuleService {
    @Autowired
    private SysAclModuleMapper sysAclModuleMapper;
    @Autowired
    private SysAclMapper sysAclMapper;
    @Autowired
    private SysLogService sysLogService;

    public void save(AclModuleParam param){
        BeanValidator.check(param);
        if(checkExist(param.getParentId(), param.getName(), param.getId())) {
            throw new ParamException("同一层级下存在相同名称的权限模块");
        }
        SysAclModule aclModule = SysAclModule.builder().name(param.getName()).parentId(param.getParentId()).seq(param.getSeq())
                .status(param.getStatus()).remark(param.getRemark()).build();
        aclModule.setLevel(LevelUtil.calculateLevel(getLevel(param.getParentId()),param.getParentId()));
        aclModule.setOperator(RequestHolder.getCurrentUser().getUsername());
        aclModule.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        aclModule.setOperateTime(new Date());
        sysAclModuleMapper.insertSelective(aclModule);
        sysLogService.saveAclModuleLog(null,aclModule);
    }
    public void update(AclModuleParam param){
        BeanValidator.check(param);
        if(checkExist(param.getParentId(), param.getName(), param.getId())) {
            throw new ParamException("同一层级下存在相同名称的权限模块");
        }
        SysAclModule before = sysAclModuleMapper.selectByPrimaryKey(param.getId());
        Preconditions.checkNotNull(before,"待更新的权限模块不存在");
        SysAclModule after = SysAclModule.builder().id(param.getId()).name(param.getName()).parentId(param.getParentId()).seq(param.getSeq())
                .status(param.getStatus()).remark(param.getRemark()).build();
        after.setLevel(LevelUtil.calculateLevel(getLevel(param.getParentId()),param.getParentId()));
        after.setOperator(RequestHolder.getCurrentUser().getUsername());
        after.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        after.setOperateTime(new Date());
        sysAclModuleMapper.updateByPrimaryKey(after);
        sysLogService.saveAclModuleLog(before,after);
    }

    /**
     * 更新子操作，当上面的父操作更改时使其子操作也执行更改，由事务来负责
     * @param before
     * @param after
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateWithChild(SysAclModule before,SysAclModule after){
        String newLevelPrefix = after.getLevel();
        String oldLevelPrefix = before.getLevel();
        if (!after.getLevel().equals(before.getLevel())) {
            List<SysAclModule> aclModuleList = sysAclModuleMapper.getChildAclModuleListByLevel(before.getLevel());
            if (CollectionUtils.isNotEmpty(aclModuleList)) {
                for (SysAclModule sysAclModule : aclModuleList) {
                    String level = sysAclModule.getLevel();
                    if (level.indexOf(oldLevelPrefix) == 0) {
                        level = newLevelPrefix+level.substring(oldLevelPrefix.length());
                        sysAclModule.setLevel(level);
                    }
                }
                sysAclModuleMapper.batchUpdateLevel(aclModuleList);
            }
        }
        sysAclModuleMapper.updateByPrimaryKey(after);

    }
    private boolean checkExist(Integer parentId, String aclModuleName, Integer deptId){
        return sysAclModuleMapper.countByNameAndParentId(parentId,aclModuleName,deptId) > 0;
    }
    private String getLevel(Integer aclModuId){
        SysAclModule sysAclModule = sysAclModuleMapper.selectByPrimaryKey(aclModuId);
        if (sysAclModule == null){
            return null;
        }
        return sysAclModule.getLevel();
    }
    public void delete(int aclModuleId) {
        // 查询权限是否存在
        SysAclModule aclModule = sysAclModuleMapper.selectByPrimaryKey(aclModuleId);
        Preconditions.checkNotNull(aclModule, "待删除的权限模块不存在，无法删除");
        if(sysAclModuleMapper.countByParentId(aclModule.getId()) > 0) {
            throw new ParamException("当前模块下面有子模块，无法删除");
        }
        if (sysAclMapper.countByAclModuleId(aclModule.getId()) > 0) {
            throw new ParamException("当前模块下面有用户，无法删除");
        }
        sysAclModuleMapper.deleteByPrimaryKey(aclModuleId);
    }
}
