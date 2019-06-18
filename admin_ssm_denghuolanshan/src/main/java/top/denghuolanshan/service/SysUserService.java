package top.denghuolanshan.service;

import com.google.common.base.Preconditions;
import top.denghuolanshan.beans.Mail;
import top.denghuolanshan.beans.PageQuery;
import top.denghuolanshan.beans.PageResult;
import top.denghuolanshan.common.RequestHolder;
import top.denghuolanshan.dao.SysUserMapper;
import top.denghuolanshan.exception.ParamException;
import top.denghuolanshan.model.SysUser;
import top.denghuolanshan.param.UserParam;
import top.denghuolanshan.util.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @ClassName SysUserService
 * @Description 用户操作逻辑
 * @Author 小欧
 * @Date 2019/6/3 13:41
 * @Version 1.0
 **/
@Service
public class SysUserService {
    @Resource
    private SysUserMapper sysUserMapper;
    @Resource
    private SysLogService sysLogService;

    public void save(UserParam param){
        BeanValidator.check(param);
        if (checkTelephoneExist(param.getTelephone(),param.getId())) {
            throw new ParamException("电话被占用");
        }
        if (checkEmailExist(param.getMail(),param.getId())){
            throw new ParamException("邮箱被占用");
        }
        String password = PassWordUtil.randomPassword();
        String encryptedPassword = MD5Util.encrypt(password);
        SysUser user = SysUser.builder().username(param.getUsername())
                .telephone(param.getTelephone()).mail(param.getMail()).password(encryptedPassword)
                .deptId(param.getDeptId()).status(param.getStatus()).remark(param.getRemark()).build();
        user.setOperator(RequestHolder.getCurrentUser().getUsername());
        user.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        user.setOperateTime(new Date());
        Set<String> set = new HashSet<>();
        set.add(param.getMail());
        Mail mail = Mail.builder().subject("获取密码").message("您的密码为:"+password).receivers(set).build();
        MailUtil.send(mail);
        sysUserMapper.insertSelective(user);
        sysLogService.saveUserLog(null,user);
    }

    public void update(UserParam param){
        if (checkTelephoneExist(param.getTelephone(),param.getId())) {
            throw new ParamException("电话被占用");
        }
        if (checkEmailExist(param.getMail(),param.getId())){
            throw new ParamException("邮箱被占用");
        }
        SysUser before = sysUserMapper.selectByPrimaryKey(param.getId());
        Preconditions.checkNotNull(before,"待更新的用户不存在");
        SysUser after = SysUser.builder().id(param.getId()).username(param.getUsername())
                .telephone(param.getTelephone()).mail(param.getMail()).deptId(param.getDeptId())
                .status(param.getStatus()).remark(param.getRemark()).build();
        after.setOperator(RequestHolder.getCurrentUser().getUsername());
        after.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        after.setOperateTime(new Date());
        sysUserMapper.updateByPrimaryKeySelective(after);
        sysLogService.saveUserLog(before,after);
    }

    public boolean checkEmailExist(String mail,Integer userId){
        return sysUserMapper.countByMail(mail,userId) > 0;
    }
    public boolean checkTelephoneExist(String telePhone,Integer userId){
        return sysUserMapper.countByTelephone(telePhone,userId) > 0;
    }

    public SysUser findByKeyword(String keyword) {
        return sysUserMapper.findByKeyword(keyword);
    }
    public PageResult<SysUser> getPageByDeptId(int deptId, PageQuery pageQuery){
        BeanValidator.check(pageQuery);
        int count = sysUserMapper.countByDeptId(deptId);
        if (count > 0){
            // 查询结果大于0时要查询每个部门下的具体用户
            List<SysUser> list = sysUserMapper.getPageByDeptId(deptId, pageQuery);
            return PageResult.<SysUser>builder().total(count).data(list).build();
        }
        return PageResult.<SysUser>builder().build();
    }
    public List<SysUser> getAll(){
        return sysUserMapper.getAll();
    }
}
