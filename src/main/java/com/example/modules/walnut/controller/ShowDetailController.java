package com.example.modules.walnut.controller;

import com.example.modules.walnut.model.WalnutHisLog;
import com.example.modules.walnut.model.WalnutLog;
import com.example.modules.walnut.service.WalnutHisLogService;
import com.example.modules.walnut.service.WalnutLogService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Toomth
 * @date 2021/3/26 10:13
 * @explain
 */
@RestController
@RequestMapping("/http")
public class ShowDetailController {
    @Autowired
    private WalnutLogService walnutLogService;


    @Autowired
    private WalnutHisLogService walnutHisLogService;

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
