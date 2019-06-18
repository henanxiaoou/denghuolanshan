package top.denghuolanshan.controller;

import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import top.denghuolanshan.beans.PageQuery;
import top.denghuolanshan.beans.PageResult;
import top.denghuolanshan.common.JsonData;
import top.denghuolanshan.model.SysUser;
import top.denghuolanshan.param.UserParam;
import top.denghuolanshan.service.SysRoleService;
import top.denghuolanshan.service.SysTreeService;
import top.denghuolanshan.service.SysUserService;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @ClassName SysUserController
 * @Description 用户的基本操作
 * @Author 小欧
 * @Date 2019/6/3 13:31
 * @Version 1.0
 **/
@Controller
@RequestMapping("/sys/user")
public class SysUserController {
    @Resource
    private SysUserService sysUserService;
    @Autowired
    private SysTreeService sysTreeService;
    @Autowired
    private SysRoleService sysRoleService;

    @RequestMapping("/save.json")
    @ResponseBody
    public JsonData saveUser(UserParam param){
        sysUserService.save(param);
        return JsonData.success();
    }
    @RequestMapping("/update.json")
    @ResponseBody
    public JsonData updateUser(UserParam param){
        sysUserService.update(param);
        return JsonData.success();
    }
    @RequestMapping("/page.json")
    @ResponseBody
    public JsonData page(@RequestParam("deptId")int deptId, PageQuery pageQuery){
        PageResult<SysUser> result = sysUserService.getPageByDeptId(deptId, pageQuery);
        return JsonData.success(result);
    }
    @RequestMapping("/acls.json")
    @ResponseBody
    public JsonData acls(@RequestParam("userId") int userId) {
        Map<String, Object> map = Maps.newHashMap();
        map.put("acls", sysTreeService.userAclTree(userId));
        map.put("roles", sysRoleService.getRoleListByUserId(userId));
        return JsonData.success(map);
    }
    @RequestMapping("/noAuth.page")
    public ModelAndView noAuth(){
        return new ModelAndView("noAuth");
    }
}
