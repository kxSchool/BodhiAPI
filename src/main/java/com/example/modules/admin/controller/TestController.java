package com.example.modules.admin.controller;

import com.example.annotation.RateLimit;
import com.example.common.api.CommonResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author Toomth
 * @date 2021/2/19 14:42
 * @explain
 */
@RestController
@RequestMapping("/static")
public class TestController {

    private  Integer number=0;
    private  Integer number2=0;

    @RateLimit(limitNum = 5.0)
    @PostMapping("/test1")
    public CommonResult test1() throws InterruptedException {
        number++;
        System.out.println("调用了方法test1    次数"+ number);
        Thread.sleep(5000);
        return CommonResult.success("OK","请求成功");
    }
    @PostMapping("/test2")
    @RateLimit(limitNum = 10.0)
    public CommonResult test2() {
        number2++;
        System.out.println("调用了方法test2   次数"+ number2);
        return CommonResult.success("OK","请求成功");
    }

}
