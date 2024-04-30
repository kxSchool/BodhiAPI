package com.example.modules.shenkang.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.modules.shenkang.mapper.WanDaPushDao;
import com.example.modules.shenkang.pojo.Result;
import com.example.modules.shenkang.pojo.WandaApiReq;
import com.example.modules.shenkang.service.impl.WanDaPushServiceImpl;
import com.example.modules.shenkang.utils.ShenkangUtils;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/AiGetBlLocalService")
public class WandaPushController {
    private final Logger logger= LoggerFactory.getLogger(getClass());

    @Autowired
    private WanDaPushServiceImpl wanDaPushService;

    @Autowired
    private WanDaPushDao wanDaPushDao;

    /**
     * @author: 邵梦丽 on 2020/9/2 17:12
     * @param:
     * @return:
     * @Description:反向校验，接口使用中
     */
    @PostMapping("/api/verify_exam_info")
    @ResponseBody
    @ApiOperation("反向校验")
    public Result getCheck(@RequestBody Map<String, Object> param) {
        Result result=null;
        String logstate;
        try{
            result=wanDaPushService.check((String)param.get("pathological_number"),(List<Map<String,Object>>) param.get("upload_file"),(String)param.get("organization_id"));
            if(result.getMsg().equals("OK")){
                logstate="0";
            }else{
                logstate="2";
            }
            WandaApiReq wanDaAPIReq=new WandaApiReq(JSONObject.toJSONString(param),JSONObject.toJSONString(result),"api_verify_exam_info", ShenkangUtils.getTime(),(String)param.get("pathological_number"),null,ShenkangUtils.getTime(),logstate);
            wanDaPushDao.insertIntoWandaApiReqIDetails(wanDaAPIReq);
        }catch(Exception e){
            e.printStackTrace();
            result=new Result();
            result.setCode(-1);
            result.setMsg("未知异常！");
            logger.error("反向校验异常{}",JSONObject.toJSONString(param));
            logstate="1";
            WandaApiReq wanDaAPIReq=new WandaApiReq(JSONObject.toJSONString(param),JSONObject.toJSONString(result),"api_verify_exam_info",ShenkangUtils.getTime(),"",null,ShenkangUtils.getTime(),logstate);
            wanDaPushDao.insertIntoWandaApiReqIDetails(wanDaAPIReq);
        }
        return result;
    }
}
