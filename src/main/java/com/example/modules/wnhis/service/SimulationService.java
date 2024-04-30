package com.example.modules.wnhis.service;


import com.example.common.api.CommonResult;
import com.example.modules.wnhis.MecWebservice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author Toomth
 * @date 2021/1/18 14:19
 * @explaind
 */
@Service
public class SimulationService {
    private final Logger logger = LoggerFactory.getLogger(SimulationService.class);

    @Autowired
    MecWebservice mecWebservice;

    /**
     * 提取患者信息
     *
     * @param param
     * @return
     */
    public CommonResult getHISWebServiceData(Map<String, Object> param) {
        CommonResult result = new CommonResult();
        try {


        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(-1);
            result.setMessage(e.getMessage());
        }
        return result;
    }

}
