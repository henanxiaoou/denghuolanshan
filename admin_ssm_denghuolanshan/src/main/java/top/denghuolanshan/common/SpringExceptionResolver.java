package top.denghuolanshan.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import top.denghuolanshan.exception.PermissionException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName SpringExceptionResolver
 * @Description 全局异常类
 * @Author 小欧
 * @Date 2019/6/1 14:58
 * @Version 1.0
 **/
@Slf4j
public class SpringExceptionResolver implements HandlerExceptionResolver {
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse httpServletRequest, Object o, Exception e) {
        String url = request.getRequestURL().toString();
        ModelAndView mav ;
        // 判断是数据请求还是页面请求
        if (url.endsWith(Constants.SUFFIX_JSON)) {
            // json请求
            if (e instanceof PermissionException) {
                JsonData result = JsonData.fail(e.getMessage());
                mav = new ModelAndView("jsonView",result.toMap());
            }else {
                log.error("unknow json exception,url"+url,e);
                JsonData result = JsonData.fail(Constants.DEFAuLT_MSG);
                mav = new ModelAndView("jsonView",result.toMap());
            }
        }else if(url.endsWith(Constants.SUFFIX_PAGE)){
            log.error("unknow page exception,url"+url,e);
            // 页面请求
            JsonData result = JsonData.fail(Constants.DEFAuLT_MSG);
            mav = new ModelAndView("exception",result.toMap());
        }else {
            log.error("unknow exception,url"+url,e);
            JsonData result = JsonData.fail(e.getMessage());
            mav = new ModelAndView("jsonView",result.toMap());
        }
        return mav;
    }
}
