package top.denghuolanshan.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import top.denghuolanshan.beans.PageQuery;
import top.denghuolanshan.common.JsonData;
import top.denghuolanshan.param.SearchLogParam;
import top.denghuolanshan.service.SysLogService;

import javax.annotation.Resource;

/**
 * @ClassName SysLogController
 * @Description TODO
 * @Author 小欧
 * @Date 2019/6/16 23:06
 * @Version 1.0
 **/
@Controller
@RequestMapping("/sys/log")
public class SysLogController {
    @Resource
    private SysLogService sysLogService;

    @RequestMapping("/log.page")
    public ModelAndView page(){
        return new ModelAndView("log");
    }
    @RequestMapping("/page.json")
    @ResponseBody
    public JsonData page(SearchLogParam param, PageQuery page){
        return JsonData.success(sysLogService.searchPageList(param,page));
    }
    @RequestMapping("/recover.json")
    @ResponseBody
    public JsonData recover(@RequestParam("id") int id){
        sysLogService.recover(id);
        return JsonData.success();
    }
}
