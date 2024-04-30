package com.example.modules.walnut.controller;

import com.example.common.api.CommonResult;
import com.example.modules.walnut.model.WalnutHisLog;
import com.example.modules.walnut.model.WalnutHisLogNode;
import com.example.modules.walnut.model.WalnutLog;
import com.example.modules.walnut.service.WalnutHisLogService;
import com.example.modules.walnut.service.WalnutLogService;
import com.example.modules.wnhis.pojo.HisDetails;
import com.sun.org.apache.regexp.internal.RE;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author Toomth
 * @date 2021/3/25 10:37
 * @explain 核桃页面接口
 */
@RestController
@RequestMapping("/walnutInterface")
public class WalnutLogController {

    @Autowired
    private WalnutLogService walnutLogService;


    @Autowired
    private WalnutHisLogService walnutHisLogService;

    @ApiOperation("查询日志")
    @PostMapping("/getLog")
    @ResponseBody
    public CommonResult getLog(@RequestBody Map<String, Object> param) {
        String method = (String) param.get("method");
        String startTime = (String) param.get("startTime");
        String endTime = (String) param.get("endTime");
        String hisId = (String) param.get("hisId");
        Integer pageNum = (Integer) param.get("pageNum");
        Integer pageSize = (Integer) param.get("pageSize");
        Integer type = (Integer) param.get("type");//1 全部 0 异常
        Integer paN0 = pageNum / pageSize + 1;
        Map<String,Object> re=walnutLogService.getLogList(type, method, hisId, startTime, endTime, pageSize, paN0);
        return CommonResult.success(re);
    }

    @ApiOperation("查询具体XML日志")
    @PostMapping("/getXmlLog")
    @ResponseBody
    public CommonResult getXmlLog(@RequestBody Map<String, Object> param) {
        String bno = (String) param.get("bno");
        String hisId = (String) param.get("hisId");
        List<WalnutHisLogNode> re=walnutHisLogService.getHisLogList(bno,hisId);
        return CommonResult.success(re);
    }

    @ApiOperation("详情展示")
    @GetMapping("/showDetails")
    @ResponseBody
    public Object CommonResultlogetDebugLog( @RequestParam(value = "id") Integer id,
                                             @RequestParam(value = "type") Integer type,
                                             @RequestParam(value = "method") Integer method) {
        String result ="";
        if (method==1){
            WalnutHisLog byId = walnutHisLogService.getById(id);
            if (type==1){
                result=byId.getReqParam();
            }else if (type==0){
                result=byId.getRespParam();
            }
        }else if (method==0){
            WalnutLog byId = walnutLogService.getById(id);
            if (type==1){
                result=byId.getReqParam();
            }else if (type==0){
                result=byId.getRespParam();
            }
        }
        return result;
    }



}
