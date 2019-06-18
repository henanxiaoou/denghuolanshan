package top.denghuolanshan.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import top.denghuolanshan.common.ApplicationContextHelper;
import top.denghuolanshan.common.JsonData;
import top.denghuolanshan.dao.SysAclModuleMapper;
import top.denghuolanshan.model.SysAcl;
import top.denghuolanshan.model.SysAclModule;
import top.denghuolanshan.param.TestVo;
import top.denghuolanshan.util.BeanValidator;
import top.denghuolanshan.util.JsonMapper;

import java.util.Map;

/**
 * @ClassName TestController
 * @Description 测试
 * @Author 小欧
 * @Date 2019/5/31 11:34
 * @Version 1.0
 **/
@Slf4j
@Controller
@RequestMapping(value = "/test")
public class TestController {
    @RequestMapping("/hello.json")
    @ResponseBody
    public JsonData hello(){
        log.info("hello");
        return JsonData.success("hello world!");
    }

    @RequestMapping("/validate.json")
    @ResponseBody
    public JsonData validate(TestVo vo){
        log.info("validate");
        SysAclModuleMapper moduleMapper = ApplicationContextHelper.popBean(SysAclModuleMapper.class);
        SysAclModule module = moduleMapper.selectByPrimaryKey(1);
        log.info(JsonMapper.obj2String(module));
        BeanValidator.check(vo);
        return JsonData.success("test validate!");
    }
}
