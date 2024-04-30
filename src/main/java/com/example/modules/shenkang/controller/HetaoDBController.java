package com.example.modules.shenkang.controller;

import com.example.modules.shenkang.mapper.WalnutCommonDao;
import com.example.modules.shenkang.pojo.Result;
import com.example.modules.shenkang.service.WalnutCommonService;
import com.example.modules.shenkang.utils.ShenkangUtils;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/hetaoInterface")
public class HetaoDBController {

    @Autowired
    WalnutCommonDao walnutCommonDao;

    @Autowired
    WalnutCommonService walnutCommonService;

    @Value("${shenkang.cronTime.dayDelay}")
    private int dayDelay;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    //查询日期
    @GetMapping(value = "/pullWalnutData/{startDate}/{endDate}")
    @ApiOperation("获取数据")
    public Result pullData(@PathVariable("startDate") String startDate, @PathVariable("endDate") String endDate) throws InterruptedException {
        Result result = new Result();

        List<String> dateList = new ArrayList<String>();
        Map<String, Object> dateMap = new HashMap<>();
        if (startDate.equals(endDate)) {
            Result ljDataByOneDay = addDate(startDate);
            dateMap.put(startDate, ljDataByOneDay);
        } else {
            boolean temp = true;
            while (temp) {
                if (endDate.equals(ShenkangUtils.addDateForOneDay(startDate))) {
                    temp = false;
                } else {
                    Result ljDataByOneDay = addDate(startDate);
                    dateMap.put(startDate, ljDataByOneDay);
                    startDate = ShenkangUtils.addDateForOneDay(startDate);
                    Thread.sleep(1000);
                }
            }
        }
        result.setData(dateMap);
        result.setCode(200);
        return result;
    }


    @RequestMapping(value = "/getDates", method = RequestMethod.GET)
    @ApiOperation("获取数据")
    public Result addDate(String updateDateTime) {
        Result result = new Result();
        if (updateDateTime != null) {
            boolean dateflag = ShenkangUtils.isRqFormat(updateDateTime);
            if (!dateflag) {//日期非格式化
                result.setCode(300);
                result.setMsg("the parmater [updateDateTime] is not format");
                logger.error("the parmater [updateDateTime] is not format");
                return result;
            }
        } else {
            updateDateTime = ShenkangUtils.getDelayFormatday(dayDelay);
        }
        List<Integer> applyids = walnutCommonDao.selectApplyIdlist(updateDateTime);
        List<Object> faildList = new ArrayList<>();
        List<Object> sucessList = new ArrayList<>();
        for (int i = 0; i < applyids.size(); i++) {
            logger.info("begin get data, applyid is " + String.valueOf(applyids.get(i)));
            Result result1 = walnutCommonService.putData(applyids.get(i), updateDateTime);
            if (result1.getCode() != 2000) {
                faildList.add(result1);
            } else {
                sucessList.add(result1);
            }
        }

        result.setMsg("取数成功，成功数据条数" + applyids.size());
        result.setCode(200);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("success", sucessList);
        map.put("faild", faildList);
        result.setData(map);
        return result;
    }
}
