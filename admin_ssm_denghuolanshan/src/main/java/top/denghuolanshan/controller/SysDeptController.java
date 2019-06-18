package top.denghuolanshan.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import top.denghuolanshan.common.JsonData;
import top.denghuolanshan.dto.DeptLevelDto;
import top.denghuolanshan.param.DeptParam;
import top.denghuolanshan.service.SysDeptService;
import top.denghuolanshan.service.SysTreeService;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName SysDeptController
 * @Description 部门控制跳转
 * @Author 小欧
 * @Date 2019/6/1 22:12
 * @Version 1.0
 **/
@Controller
@RequestMapping("/sys/dept")
@Slf4j
public class SysDeptController {
    @Resource
    private SysDeptService sysDeptService;
    @Resource
    private SysTreeService sysTreeService;

    @RequestMapping("/dept.page")
    public ModelAndView page(){
        return new ModelAndView("dept");
    }
    @RequestMapping("/save.json")
    @ResponseBody
    public JsonData saveDept(DeptParam param){
        sysDeptService.save(param);
        return JsonData.success();
    }

    @RequestMapping("/tree.json")
    @ResponseBody
    public JsonData tree(){
        List<DeptLevelDto> dtoList = sysTreeService.deptTree();
        return JsonData.success(dtoList);
    }
    @RequestMapping("/update.json")
    @ResponseBody
    public JsonData updateDept(DeptParam param){
        sysDeptService.update(param);
        return JsonData.success();
    }
    @RequestMapping("/delete.json")
    @ResponseBody
    public JsonData deleteDept(Integer id){
        JsonData jsonData = sysDeptService.delete(id);
        return jsonData;
    }
}
