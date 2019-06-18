package top.denghuolanshan.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import top.denghuolanshan.common.JsonData;
import top.denghuolanshan.dao.SysAclModuleMapper;
import top.denghuolanshan.param.AclModuleParam;
import top.denghuolanshan.service.SysAclModuleService;
import top.denghuolanshan.service.SysTreeService;

import javax.annotation.Resource;

/**
 * @ClassName SysAclModuleController
 * @Description TODO
 * @Author 小欧
 * @Date 2019/6/7 17:01
 * @Version 1.0
 **/
@Controller
@RequestMapping("/sys/aclModule")
@Slf4j
public class SysAclModuleController  {
    @Resource
    private SysAclModuleService sysAclModuleService;
    @Resource
    private SysTreeService sysTreeService;

    @RequestMapping("/acl.page")
    public ModelAndView page(){
        return new ModelAndView("acl");
    }

    @RequestMapping("/save.json")
    @ResponseBody
    public JsonData saveAclModule(AclModuleParam param){
        sysAclModuleService.save(param);
        return JsonData.success();
    }
    @RequestMapping("/update.json")
    @ResponseBody
    public JsonData updateAclModule(AclModuleParam param){
        sysAclModuleService.update(param);
        return JsonData.success();
    }
    @RequestMapping("/tree.json")
    @ResponseBody
    public JsonData tree(){
        return JsonData.success(sysTreeService.aclModuleTree());
    }
    @RequestMapping("/delete.json")
    @ResponseBody
    public JsonData delete(@RequestParam("id")int id){
        sysAclModuleService.delete(id);
        return JsonData.success();
    }
}
