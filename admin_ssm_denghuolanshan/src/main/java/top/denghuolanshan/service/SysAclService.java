package top.denghuolanshan.service;

import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.denghuolanshan.beans.PageQuery;
import top.denghuolanshan.beans.PageResult;
import top.denghuolanshan.common.RequestHolder;
import top.denghuolanshan.dao.SysAclMapper;
import top.denghuolanshan.exception.ParamException;
import top.denghuolanshan.model.SysAcl;
import top.denghuolanshan.param.AclParm;
import top.denghuolanshan.util.BeanValidator;
import top.denghuolanshan.util.IpUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @ClassName SysAclService
 * @Description 权限点业务逻辑
 * @Author 小欧
 * @Date 2019/6/10 13:47
 * @Version 1.0
 **/
@Service
public class SysAclService {
    @Autowired
    private SysAclMapper sysAclMapper;
    @Autowired
    private SysLogService sysLogService;

    public void save(AclParm parm){
        BeanValidator.check(parm);
        if (checkExist(parm.getAclModuleId(),parm.getName(),parm.getId())){
            throw new ParamException("当前权限模块下面存在相同名称的权限点");
        }
        SysAcl acl = SysAcl.builder().name(parm.getName()).aclModuleId(parm.getAclModuleId())
                .url(parm.getUrl()).type(parm.getType()).status(parm.getStatus()).seq(parm.getSeq())
                .remark(parm.getRemark()).build();
        acl.setCode(generateCode());
        acl.setOperator(RequestHolder.getCurrentUser().getUsername());
        acl.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        acl.setOperateTime(new Date());
        sysAclMapper.insertSelective(acl);
        sysLogService.saveAclLog(null,acl);
    }

    /**
     * 生成一个code值
     * @return
     */
    public String generateCode(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return dateFormat.format(new Date())+"_"+Math.random()*100;
    }
    public void update(AclParm parm){
        BeanValidator.check(parm);
        if (checkExist(parm.getAclModuleId(),parm.getName(),parm.getId())){
            throw new ParamException("当前权限模块下面存在相同名称的权限点");
        }
        // 获取要更新的类是否存在，如果不存在抛出异常
        SysAcl before = sysAclMapper.selectByPrimaryKey(parm.getId());
        Preconditions.checkNotNull(before,"待更新的权限点不存在");
        // 当要更新的类存在时，执行更新操作
        SysAcl after = SysAcl.builder().id(parm.getId()).name(parm.getName()).aclModuleId(parm.getAclModuleId())
                .url(parm.getUrl()).type(parm.getType()).status(parm.getStatus()).seq(parm.getSeq())
                .remark(parm.getRemark()).build();
        after.setOperator(RequestHolder.getCurrentUser().getUsername());
        after.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        after.setOperateTime(new Date());
        sysAclMapper.updateByPrimaryKeySelective(after);
        sysLogService.saveAclLog(before,after);
    }

    /**
     * 判断当前同一个模块下是否存在相同名称点权限点
     * @param aclModuleId
     * @param name
     * @param id
     * @return
     */
    public boolean checkExist(int aclModuleId,String name,Integer id){
        return sysAclMapper.countByNameAndAclModuleId(aclModuleId,name,id) > 0;
    }
    public PageResult<SysAcl> getPageByAclModuleId(int aclModuleId, PageQuery page){
        BeanValidator.check(page);
        int count = sysAclMapper.countByAclModuleId(aclModuleId);
        if (count > 0){
            List<SysAcl> aclList = sysAclMapper.getPageByAclModuleId(aclModuleId,page);
            return PageResult.<SysAcl>builder().data(aclList).total(count).build();
        }
        return PageResult.<SysAcl>builder().build();
    }
}
