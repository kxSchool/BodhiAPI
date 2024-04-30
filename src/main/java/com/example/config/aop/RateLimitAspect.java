package com.example.config.aop;

import com.example.annotation.RateLimit;
import com.example.common.api.CommonResult;
import com.example.common.api.ResultCode;
import com.example.common.service.impl.RedisServiceImpl;
import com.example.modules.admin.model.UmsAdmin;
import com.example.modules.admin.service.UmsAdminCacheService;
import com.example.security.util.JwtTokenUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.util.concurrent.RateLimiter;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Toomth
 * @date 2021/2/19 14:35
 * @explain   接口限流
 */

@Component
@Scope
@Aspect
public class RateLimitAspect {
    private Logger log = LoggerFactory.getLogger(this.getClass());
    //用来存放不同接口的RateLimiter(key为接口名称，value为RateLimiter)
    private ConcurrentHashMap<String, RateLimiter> map = new ConcurrentHashMap<>();

    private static ObjectMapper objectMapper = new ObjectMapper();

    private RateLimiter rateLimiter;


    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Value("${jwt.tokenHeader}")
    private String tokenHeader;

    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Autowired
    UmsAdminCacheService umsAdminCacheService;
    @Value("${redis.key.admin}")
    private String key;



    @Autowired
    private HttpServletResponse response;


    @Pointcut("@annotation(com.example.annotation.RateLimit)")
    public void serviceLimit() {
    }

    @Around("serviceLimit()")
    public Object around(ProceedingJoinPoint joinPoint) throws NoSuchMethodException {
        Object obj = null;
        //获取拦截的方法名
        Signature sig = joinPoint.getSignature();
        //获取拦截的方法名
        MethodSignature msig = (MethodSignature) sig;
        //返回被织入增加处理目标对象
        Object target = joinPoint.getTarget();
        //为了获取注解信息
        Method currentMethod = target.getClass().getMethod(msig.getName(), msig.getParameterTypes());
        //获取注解信息
        RateLimit annotation = currentMethod.getAnnotation(RateLimit.class);
        double limitNum = annotation.limitNum(); //获取注解每秒加入桶中的token
        String functionName = msig.getName(); // 注解所在方法名区分不同的限流策略

        //获取用户姓名
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String authHeader = request.getHeader(this.tokenHeader);
        String authToken = authHeader.substring(this.tokenHead.length());// The part after "Bearer "
        String username = jwtTokenUtil.getUserNameFromToken(authToken);
        //根据用户名查询该用户所属计数器
        Integer concurrency = umsAdminCacheService.getConcurrencyByUserName(username);

        //获取rateLimiter
        if(map.containsKey(functionName)){
            rateLimiter = map.get(functionName);
        }else {
            map.put(functionName, RateLimiter.create(limitNum));
            rateLimiter = map.get(functionName);
        }

        try {
            if (rateLimiter.tryAcquire()&&concurrency>0) {
                //执行方法
                umsAdminCacheService.delConcurrencyByUserName(username);
                umsAdminCacheService.setConcurrencyByUserName(username,concurrency-1);
                obj = joinPoint.proceed();
                Integer concurrency2 = umsAdminCacheService.getConcurrencyByUserName(username);
                umsAdminCacheService.setConcurrencyByUserName(username,concurrency2+1);

            } else {
                //拒绝了请求（服务降级）
                String result = objectMapper.writeValueAsString(CommonResult.failed(ResultCode.FAILED, functionName+"系统繁忙！"));
                log.info("拒绝了请求：" + result);
                outErrorResult(result);
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        return obj;
    }
    //将结果返回
    public void outErrorResult(String result) {
        response.setContentType("application/json;charset=UTF-8");
        try (ServletOutputStream outputStream = response.getOutputStream()) {
            outputStream.write(result.getBytes("utf-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }


}
