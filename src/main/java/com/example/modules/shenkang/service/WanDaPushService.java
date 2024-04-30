package com.example.modules.shenkang.service;

import com.example.modules.shenkang.pojo.MedicalInfo;
import com.example.modules.shenkang.pojo.PatientInfo;
import com.example.modules.shenkang.pojo.Result;

import java.util.List;
import java.util.Map;

public interface WanDaPushService {
    Result requestCheck(Integer id);

    Result postFileNew(Integer medId,String BusinessId);
    Result postFileNew(Integer medId,String BusinessId,String PathNo);

    /**
     * @author: 邵梦丽 on 2020/9/1 9:40
     * @param:
     * @return:
     * @Description:推送病理信息接口
     */
    Result updataMedicalInfo(MedicalInfo medicalInfo);

    /**
     * @author: 邵梦丽 on 2020/9/1 9:40
     * @param:
     * @return:
     * @Description:推送患者信息接口
     */
    Result registerPatient(PatientInfo patientInfo);

    /**
     * @author: 邵梦丽 on 2020/9/1 9:40
     * @param:
     * @return:
     * @Description:获取token接口
     */
    String getAccessToken();

    /**
     * @author: 邵梦丽 on 2020/9/1 9:40
     * @param:
     * @return:
     * @Description:设置based_time取材时间，如果based_time有时间，则不做修改，
     * 如果没有时间，则根据reg_time和result_principal_time取值
     * 1.取出reg_time和result_principal_time的平均值
     * 2.取result_principal_time的前十分钟
     * 3.取reg_time的后十分钟
     */
    MedicalInfo setBased_time(MedicalInfo medicalInfo);

    /**
     * @author: 邵梦丽 on 2020/9/1 9:40
     * @param:
     * @return:
     * @Description:提供万达反向校验
     */
    Result check(String pathological_number, List<Map<String, Object>> fileList, String oriId);
}
