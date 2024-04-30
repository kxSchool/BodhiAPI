package com.example.modules.wnhis.controller;

import com.example.common.api.CommonResult;
import com.example.modules.wnhis.pojo.JB01;
import com.example.modules.wnhis.service.HISServiceForDebug;
import com.example.modules.wnhis.service.HisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Toomth
 * @date 2021/3/22 10:11
 * @explain 调试平台 接口
 */
@RestController
@RequestMapping("/his")
public class HISConntrolForDebug02 {
    @Autowired
    private HISServiceForDebug hisServiceForDebug;

    @Autowired
    private HisService hisService;

    @PostMapping("/patientInfoJB01")
    public CommonResult JBO1(@RequestBody Map<String, Object> param) throws IOException {
        Map<String, Object> result = new HashMap<>();
        String hisId= (String) param.get("hisId");
        String codeType= (String) param.get("codeType");
        Integer brlb= (Integer) param.get("brlb");
        JB01 jb01Result = hisService.getJB01Result(hisId, brlb, codeType);
        result.put("data",jb01Result);
        return CommonResult.success(result);
    }
}
