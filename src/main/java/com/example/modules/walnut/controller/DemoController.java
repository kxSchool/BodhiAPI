package com.example.modules.walnut.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.common.api.CommonResult;
import com.example.modules.walnut.service.DemoService;
import com.example.modules.walnut.service.HISService;
import com.example.modules.wnhis.pojo.SysUser;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

/**
 * @author Toomth
 * @date 2021/3/24 17:06
 * @explain 提供调试接口
 */
@RestController
@RequestMapping("/walnutInterface")
public class DemoController {

    @Autowired
    private DemoService demoService;
    @Autowired
    private HISService hisService;

    @ApiOperation("提取患者信息接口")
    @PostMapping("/extraction")
    @ResponseBody
    public CommonResult extraction(@RequestBody Map<String, Object> param) throws IOException {
//        Integer patientType = (Integer) param.get("patientType")
//        String hisId = (String) param.get("hisId");
//        String applyNo = (String) param.get("applyNo");
//        Integer userId=(Integer) param.get("userId");

        Map<String, Object> hisWebServiceData = hisService.getHISWebServiceData(param);
        return CommonResult.success(hisWebServiceData);
    }
    @ApiOperation("推送报告接口")
    @PostMapping("/pushReport")
    @ResponseBody
    public CommonResult pushReport(@RequestBody Map<String, Object> param) throws IOException {
        Integer applyId = (Integer) param.get("applyId");
        String hisId = (String) param.get("hisId");
        Integer userId=(Integer) param.get("userId");
        Map<String, Object> hisWebServiceData = demoService.reportSaveButton(applyId,userId,hisId);
        return CommonResult.success(hisWebServiceData);
    }

    @ApiOperation("报告取消审核接口")
    @PostMapping("/cancelThAudit")
    @ResponseBody
    public CommonResult cancelThAudit(@RequestBody Map<String, Object> param) throws IOException {
        Integer reId = (Integer) param.get("reId");
        String hisId = (String) param.get("hisId");
        Integer userId=(Integer) param.get("userId");
        Map<String, Object> hisWebServiceData = demoService.cancelThAudit(reId,userId,hisId);
        return CommonResult.success(hisWebServiceData);
    }

    @ApiOperation("住院添加/取消收费项目")
    @PostMapping("/addAndCancelCharge")
    @ResponseBody
    public CommonResult addAndCancelCharge(@RequestBody Map<String, Object> param) throws IOException {
        ArrayList<Map<String, Object>> chIds = (ArrayList<Map<String, Object>>) param.get("charges");
        Integer applyId = (Integer) param.get("applyId");
        Integer type = (Integer) param.get("type");// 0 添加项目 1 取消项目
        String hisId = (String) param.get("hisId");
        Integer userId=(Integer) param.get("userId");
        Map<String, Object> hisWebServiceData = demoService.addAndCancelCharge(chIds, applyId, type,userId,hisId);
        return CommonResult.success(hisWebServiceData);
    }




}
