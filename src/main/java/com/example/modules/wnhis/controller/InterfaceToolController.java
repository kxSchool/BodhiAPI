package com.example.modules.wnhis.controller;

import com.example.common.api.CommonResult;
import com.example.modules.wnhis.pojo.BG01;
import com.example.modules.wnhis.service.HisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/his")
public class InterfaceToolController {

    @Autowired
    HisService hisService;

    @PostMapping("/reportPush")
    public CommonResult reportPush(@RequestBody Map<String, Object> param) throws IOException {
        BG01 bg01=new BG01();
        bg01.setAge("22");
        bg01.setBlh("blh");
        Map<String, Object> result = hisService.getBG01(bg01);
        return CommonResult.success(result);
    }
}
