package com.example.modules.shenkang.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.modules.shenkang.mapper.WalnutCommonDao;
import com.example.modules.shenkang.pojo.DebugLog;
import com.example.modules.shenkang.pojo.Result;
import com.example.modules.shenkang.service.LangJiaService;
import com.example.modules.shenkang.service.WalnutCommonServiceDemo;
import com.example.modules.shenkang.utils.ShenkangUtils;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/hetaoInterface")
public class HetaoController {

    @Autowired
    private LangJiaService langJiaService;

    @Autowired
    WalnutCommonDao walnutCommonDao;

    @Autowired
    WalnutCommonServiceDemo walnutCommonServiceDemo;

    /**
     * 核桃调试接口
     *
     * @return
     * @throws InterruptedException
     */
    @PostMapping(value = "/pullWalnutData")
    @ApiOperation("获取数据")
    public Result pullWalnutData(@RequestParam(value = "updateDateTime", required = false) String updateDateTime, @RequestParam(value = "pathNo", required = false) String pathNo) {
        Result result = new Result();
        DebugLog debugLog = new DebugLog();
        HashMap<String, String> paramsList = new HashMap<>();
        Map<String, Object> map = new HashMap<String, Object>();
        String paramNo = "";
        //设置请求参数
        if (updateDateTime != null) {
            paramsList.put("updateDateTime", updateDateTime);
            paramNo = "1";
        } else if (pathNo != null) {
            paramsList.put("pathNo", pathNo);
            paramNo = "2";
        }
        try {
            if (updateDateTime != null) {
                Result ljDataByOneDay = addDate(updateDateTime);
                result.setData(ljDataByOneDay);
            } else if (pathNo != null) {
                result.setData("ss");
            }
            debugLog = new DebugLog(JSONObject.toJSONString(paramsList), JSONObject.toJSONString(result), "walnutPath", "0", pathNo, updateDateTime, ShenkangUtils.currentTimestampTime(), paramNo);
        } catch (Exception e) {
            e.printStackTrace();
            debugLog = new DebugLog(updateDateTime, e.getMessage(), "walnutPath", "1", pathNo, updateDateTime, ShenkangUtils.currentTimestampTime(), paramNo);
            langJiaService.insertDegbugLog(debugLog);
        } finally {
            langJiaService.insertDegbugLog(debugLog);
        }
        return result;
    }

    public Result addDate(String updateDateTime) {
        Result result = new Result();
        boolean dateflag = ShenkangUtils.isRqFormat(updateDateTime);
        if (!dateflag) {//日期非格式化
            result.setCode(300);
            result.setMsg("the parmater [updateDateTime] is not format");
            return result;
        }
        List<Integer> applyids = walnutCommonDao.selectApplyIdlist(updateDateTime);
        List<Object> faildList = new ArrayList<>();
        List<Object> sucessList = new ArrayList<>();
        for (int i = 0; i < applyids.size(); i++) {
            Result result1 = walnutCommonServiceDemo.putData(applyids.get(i), updateDateTime);
            if (result1.getCode() != 2000) {
                faildList.add(result1);
                result.setCode(300);
            } else {
                sucessList.add(result1);
            }
        }
        if (result.getCode() != 2000) {
            result.setMsg("取数失败，失败数据为" + faildList.toString() + ",取数成功数目为：" + sucessList.size());
        } else {
            result.setMsg("取数成功，成功数据条数" + applyids.size());
            result.setCode(200);
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("success", sucessList);
        map.put("faild", faildList);

        result.setData(map);
        return result;
    }

}