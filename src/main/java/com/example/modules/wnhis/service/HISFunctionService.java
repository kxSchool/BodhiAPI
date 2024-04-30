package com.example.modules.wnhis.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.example.modules.wnhis.MecWebservice;
import com.example.modules.wnhis.pojo.JB01;
import com.example.modules.wnhis.pojo.JB03;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Toomth
 * @date 2021/3/16 18:12
 * @explain
 */
@Service
public class HISFunctionService {

    @Autowired
    HisService hisService;

    @Autowired
    MecWebservice mecWebservice;
    /**
     * 根据 JB01 返回
     */
    public Map<String, Object> ZMExtraction(String hisId, Integer brlb, String applyNo, Integer patientType) throws IOException {
        Map<String, Object> result = new HashMap<String, Object>();
        JB01 jb01 = hisService.getJB01(hisId, brlb);
        if (jb01!=null&&!"F".equals(jb01.getColumn1())){
            List<JB03> jb03List = hisService.getJB03(jb01, brlb);
            if (jb03List != null) {
                String stringjb03 = JSONArray.toJSONString(jb03List.get(0));
                JB03 jb03 = JSON.parseObject(stringjb03, JB03.class);
                if (jb03.getColumn1() != "F" && !"F".equals(jb03.getColumn1())) {
                    
                }
            }
        }

        return result;
    }
}
