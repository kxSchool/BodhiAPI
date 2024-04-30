package com.example.modules.wnhis.controller;

import cn.hutool.core.lang.UUID;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.api.CommonResult;
import com.example.modules.wnhis.MecWebservice;
import com.example.modules.wnhis.WebServiceUtil;
import com.example.modules.wnhis.pojo.HisDetails;
import com.example.modules.wnhis.pojo.HisExtractionlog;
import com.example.modules.wnhis.service.HISServiceForDebug;
import com.example.modules.wnhis.service.HisDetailsService;
import com.example.modules.wnhis.service.HisServiceDebug;
import com.example.utils.DateUtils;
import com.example.utils.MD5util;
import com.example.utils.analysis.XMLToMap;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author Toomth
 * @date 2021/3/16 11:06
 * @explain 调试平台 接口
 */
@RestController
@RequestMapping("/hisInterface")
public class HISConntrolForDebug {

    private final  Logger logger=LoggerFactory.getLogger(HISConntrolForDebug.class);

    @Autowired
    private HISServiceForDebug hisServiceForDebug;

    @Autowired
    private WebServiceUtil webServiceUtil;

    @Autowired
    private MecWebservice mecWebservice;

    @Autowired
    private HisDetailsService hisDetailsService;


    @ApiOperation("webService 接口返回")
    @PostMapping("/getWebServiceResult")
    @ResponseBody
    public CommonResult getWebServiceResult(@RequestBody Map<String, Object> param) throws IOException {
        //生成业务编号（调试为 debug 开头）
        String method = (String) param.get("method");
        String request = (String) param.get("requestParam");
        String hisId = (String) param.get("hisId");
        Long now = DateUtils.currentTimestampTime();
        String no = "debug" + MD5util.string2MD5(String.valueOf(now) + UUID.nameUUIDFromBytes(param.toString().getBytes()));
        Map<String, Object> webServiceForDebug;
        if (method.startsWith("TJ")) {
            webServiceForDebug = mecWebservice.getMecWebServiceForDebug(excapeXML(request));
        } else {
            webServiceForDebug = webServiceUtil.getWebServiceForDebug(excapeXML(request));
        }
        HisDetails hisDetails = new HisDetails();
        hisDetails.setNo(no);
        hisDetails.setParam(hisId);
        hisDetails.setMethod(method);
        hisDetails.setReqParam(request);
        hisDetails.setCreateTime(now);

        if (webServiceForDebug != null) {
            if ((Integer) webServiceForDebug.get("code") == 200) {
                hisDetails.setState("0");
                hisDetails.setRespParam((String) webServiceForDebug.get("responseParam"));
            } else {
                hisDetails.setState("2");
            }
        }
        hisDetailsService.save(hisDetails);
        return CommonResult.success(webServiceForDebug);
    }

    @ApiOperation("xml转结构")
    @PostMapping("/ParsingXml")
    @ResponseBody
    public CommonResult parsingXml(@RequestBody Map<String, Object> param) {
        Integer id = (Integer) param.get("id");
        String method = (String) param.get("method");
        String respXML = hisDetailsService.parsingXml(id);
        Map<String, Object> map;
        if (method.startsWith("TJ")) {
            map = mecWebservice.XMLToMap(method,respXML);
        } else {
            map = webServiceUtil.XMLToMap(method, respXML);
        }
        return CommonResult.success(map);
    }

    @ApiOperation("页面请求日志")
    @PostMapping("/getDebugLog")
    @ResponseBody
    public CommonResult getDebugLog(@RequestBody Map<String, Object> param) {
        String method = (String) param.get("method");
        String startTime = (String) param.get("startTime");
        String endTime = (String) param.get("endTime");
        String hisId = (String) param.get("hisId");
        Integer pageNum = (Integer) param.get("pageNum");
        Integer pageSize = (Integer) param.get("pageSize");
        Integer type = (Integer) param.get("type");//1 全部 0 异常
        Integer paN0 = pageNum / pageSize + 1;
        Map<String, Object> hisDetailsPage = hisDetailsService.queryByMethod(type, method, hisId, startTime, endTime, pageSize, paN0);
        return CommonResult.success(hisDetailsPage);
    }

    @ApiOperation("详情展示")
    @GetMapping("/showDetails")
    @ResponseBody
    public Object CommonResultlogetDebugLog( @RequestParam(value = "id") Integer id,
                                                   @RequestParam(value = "type") Integer type) {
        HisDetails byId = hisDetailsService.getById(id);
        String result ="";
        if (type==1){
            result=byId.getReqParam();
        }else if (type==0){
            result=byId.getRespParam();
        }
        return result;
    }

    @ApiOperation("更新state ")
    @PostMapping("/updataState2")
    @ResponseBody
    public CommonResult updataStateById( @RequestBody Map<String, Object> param) {
        System.out.println();
        Integer id = (Integer) param.get("id");
        Integer state=(Integer)param.get("state");
        HisDetails hisDetails=new HisDetails();
        hisDetails.setId(id);
        hisDetails.setState(state.toString());
        boolean b = hisDetailsService.updateById(hisDetails);
        if (b){
            return CommonResult.success(true,"更新成功");
        }else {
            return CommonResult.failed("更新失败");
        }
    }

    @ApiOperation("更新state ")
    @RequestMapping("/updataState2")
    @ResponseBody
    public CommonResult updataStateById( @RequestParam(value = "id") Integer id,
                                         @RequestParam(value = "state") String state) {
        HisDetails hisDetails=new HisDetails();
        hisDetails.setId(id);
        hisDetails.setState(state);
        boolean b = hisDetailsService.updateById(hisDetails);
        if (b){
            return CommonResult.success(true,"更新成功");
        }else {
            return CommonResult.failed("更新失败");
        }
    }

    /**
     * 提取日志列表
     *
     * @param param
     * @return
     */
    @PostMapping("/extractionLog")
    public CommonResult extractionLog(@RequestBody Map<String, Object> param) {
        String startTime = (String) param.get("startTime");
        String endTime = (String) param.get("endTime");
        Integer pageNum = (Integer) param.get("pageNum");
        Integer pageSize = (Integer) param.get("pageSize");
        String hisId = (String) param.get("hisId");
        Page<HisExtractionlog> relist = hisServiceForDebug.list(hisId, startTime, endTime, pageSize, pageNum);
        Map<String, Object> result = new HashMap<>();
        result.put("list", relist);
        return CommonResult.success(result);
    }

    public String excapeXML(String xml){
        String XML="";
        XML=xml.replace("&lt;","<");
        XML=XML.replace("&gt;",">");
        return XML;
    }


}
