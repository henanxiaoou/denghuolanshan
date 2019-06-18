package top.denghuolanshan.controller;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import top.denghuolanshan.beans.PageQuery;
import top.denghuolanshan.common.JsonData;
import top.denghuolanshan.model.SysAcl;
import top.denghuolanshan.model.SysRole;
import top.denghuolanshan.param.AclModuleParam;
import top.denghuolanshan.param.AclParm;
import top.denghuolanshan.service.SysAclService;
import top.denghuolanshan.service.SysRoleService;

import java.util.List;
import java.util.Map;

/**
 * @ClassName SysAclController
 * @Description 权限点接口
 * @Author 小欧
 * @Date 2019/6/7 17:00
 * @Version 1.0
 **/
@Controller
@RequestMapping("/sys/acl")
@Slf4j
public class SysAclController {
    @Autowired
    private SysAclService sysAclService;
    @Autowired
    private SysRoleService sysRoleService;

    @RequestMapping("/save.json")
    @ResponseBody
    public JsonData saveAclModule(AclParm param){
        sysAclService.save(param);
        return JsonData.success();
    }
    @RequestMapping("/update.json")
    @ResponseBody
    public JsonData updateAclModule(AclParm param){
        sysAclService.update(param);
        return JsonData.success();
    }
    @RequestMapping("/page.json")
    @ResponseBody
    public JsonData list(@RequestParam("aclModuleId")Integer aclModuleId,PageQuery pageQuery){
        return JsonData.success(sysAclService.getPageByAclModuleId(aclModuleId,pageQuery));
    }
    @RequestMapping("acls.json")
    @ResponseBody
    public JsonData acls(@RequestParam("aclId") int aclId) {
        Map<String, Object> map = Maps.newHashMap();
        List<SysRole> roleList = sysRoleService.getRoleListByAclId(aclId);
        map.put("roles", roleList);
        map.put("users", sysRoleService.getUserListByRoleList(roleList));
        return JsonData.success(map);
    }
}
