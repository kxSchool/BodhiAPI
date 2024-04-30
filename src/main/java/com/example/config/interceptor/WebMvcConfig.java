package com.example.config.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * @author Toomth
 * @date 2021/2/19 10:24
 * @explain
 */
public class WebMvcConfig extends WebMvcConfigurationSupport {
    /**
     * 注入自定义拦截器到该配置类中
     */
    @Autowired
    private ApiInceptor apiInceptor;

    /**
     * 添加自定义拦截器
     */
    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(apiInceptor)
                .addPathPatterns("/**")//拦截的访问路径，拦截所有
                .excludePathPatterns("/static/*");//排除的请求路径，排除静态资源路径
        super.addInterceptors(registry);
    }
}
