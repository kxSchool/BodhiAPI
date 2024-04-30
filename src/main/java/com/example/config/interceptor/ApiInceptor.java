package com.example.config.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Toomth
 * @date 2021/2/19 10:20
 * @explain
 */
public class ApiInceptor implements HandlerInterceptor{
    /**
     * 在整个请求结束之后被调用，DispatcherServlet 渲染视图之后执行（进行资源清理工作）
     */
    @Override
    public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3) throws Exception {

    }

    /**
     * 请求处理之后进行调用，但是在视图被渲染之前（Controller方法调用之后）
     */
    @Override
    public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3) throws Exception {

    }


    /**
     * 在请求处理之前进行调用（Controller方法调用之前）
     *
     * @return 返回true才会继续向下执行，返回false取消当前请求
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object arg2) throws Exception {

        String rootPath = request.getContextPath();
        //设置前端的全局地址，可以是request.getContextPath(),也可以是包含ip、端口号、项目名等等的全路径
        request.setAttribute("rootPath", rootPath);
        return true;
    }

}
