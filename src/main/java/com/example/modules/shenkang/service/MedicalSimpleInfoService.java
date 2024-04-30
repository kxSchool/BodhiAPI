package com.example.modules.shenkang.service;

import com.example.modules.shenkang.pojo.DebugLog;
import com.example.modules.shenkang.pojo.MedicalJson;
import com.example.modules.shenkang.pojo.PatientJson;
import com.example.modules.shenkang.pojo.Result;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface MedicalSimpleInfoService {

    /**
     * @author: 邵梦丽 on 2020/8/27 11:22
     * @param:
     * @return:
     * @Description:批量插入数据
     */
    Result pushDatesByState();

    int setWandaRequestStateById(Integer id);

    int setPatientJsomStateById(Integer id);

    List<MedicalJson> getMedicalJsonByWhole(String startTime,String endTime, Integer beginNum, Integer pageSize,String logstate);

    int setMedicalJsonStateById(Integer id);

    DebugLog getLogById(Integer id);

    int setLogStateById(Integer id);

    List<PatientJson> getPatientJsonByWhole(String startTime,String endTime, Integer beginNum, Integer pageSize,String logstate);

    int setAiJsonStateById(Integer id);
    /**
     * @author: 邵梦丽 on 2020/8/27 11:22
     * @param:
     * @return:
     * @Description:存储啷珈患者信息接口返回的报文信息
     */
    int insertPatientJson(PatientJson patientJson);

    /**
     * @author: 邵梦丽 on 2020/8/27 11:22
     * @param:
     * @return:
     * @Description:如果患者信息插入失败，则调用此方法来进行数据信息的补录
     */
    Result addPatients(StringBuffer detalisResp, String updateDateTime);

    /**
     * @author: 邵梦丽 on 2020/8/27 11:22
     * @param:
     * @return:
     * @Description:存储啷珈病理信息接口返回的报文信息
     */
    int insertMedicalJson(MedicalJson medicalJson);

    /**
     * @author: 邵梦丽 on 2020/8/27 11:22
     * @param:
     * @return:
     * @Description:如果批量插入失败，则调用此方法进行信息补录
     */
    Result addMedicalInfos(StringBuffer detalisResp,String updateDateTime);

    List<DebugLog> getLogs(String pathNo, String startTime, String endTime, Integer beginNum, Integer pageSize, String apiName, String state);

    List<Map<String,Object>> getAiJsonList(String pathNo, String startTime, String endTime, Integer beginNum, Integer pageSize, Integer logstate);
}
