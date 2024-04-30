package com.example.modules.shenkang.mapper;

import com.example.modules.shenkang.pojo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mapper
public interface MedicalSimpleDao {
    /**
     * @author: 邵梦丽 on 2020/8/24 16:46
     * @param:
     * @return:
     * @Description:更新病理信息发送状态以及存放businessid
     */
    Integer updateMedicalInfoStateAndBusinessid(@Param("id")String id,@Param("state") String state,String businessid);//3

    Integer updateFileInfoObservationUID(@Param("pathNo")String pathNo,@Param("observationUID")String observationUID);
    /**
     * @author: 邵梦丽 on 2020/8/24 16:46
     * @param:
     * @return:
     * @Description:根据id获取病理信息(详细信息，涉及到两个表连表查询，目前没有做连接)
     */
    MedicalInfo getMedicalDetailsInfoByHis(@Param("id") String id);//1

    /**
     * @author: 邵梦丽 on 2020/8/24 16:46
     * @param:
     * @return:
     * @Description:根据id设置病理信息的patient_master_id
     */
    Integer updateMedicalMasterById(@Param("patient_master_id")String patient_master_id,@Param("id")String id);

    /**
     * @author: 邵梦丽 on 2020/8/24 16:46
     * @param:
     * @return:
     * @Description:根据id设置患者信息的patient_master_id
     */
    Integer updatePatientMasterById(@Param("patient_master_id")String patient_master_id,@Param("id")String id);

    /**
     * @author: 邵梦丽 on 2020/8/24 16:46
     * @param:
     * @return:
     * @Description:更新病理信息发送状态
     */
    Integer updateMedicalInfo(@Param("id")String id,@Param("state") String state);//3

    MedicalInfo getMedicalInfoByHis(@Param("id") String id);//1

    List<String> getUnpushedMedicalIds();

    Integer selectwalnutNum(@Param("pathNo") String pathNo, @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("beginNum") Integer beginNum, @Param("pageSize") Integer pageSize);

    List<WalnutCommon> selectwalnut(@Param("pathNo") String pathNo, @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("num") Integer num, @Param("size") Integer size);

    List<String> selectErrorInfo(@Param("dbName") String dbName, @Param("logState") String logState);

    WalnutDetail selectInfoByPathId(@Param("pathNo") String pathNo, @Param("dbName") String dbName, @Param("logState") String logState);

    Integer selectwalnutNumByParam(@Param("pathNo") String pathNo, @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("num") Integer num, @Param("size") Integer size, @Param("myList") List<String> myList);

    List<WalnutCommon> selectwalnutByParam(@Param("pathNo") String pathNo, @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("num") Integer num, @Param("size") Integer size, @Param("myList") List<String> myList);

    MedicalInfo getMedicalDetailsInfoByPathNo(@Param("pathNo") String pathNo);

    /**
     * @author: 邵梦丽 on 2020/8/24 16:46
     * @param:
     * @return:
     * @Description:根据病理信息里面的病理号获取病理信息
     */
    Integer selectmedIdByPathNo(@Param("pathological_number")String pathological_number);

    /**
     * @author: 邵梦丽 on 2020/8/24 16:46
     * @param:
     * @return:
     * @Description:根据病理号获取患者信息
     */
    PatientInfo getPatientByPathNo(@Param("pathNo") String pathNo);//2

    @Select("select * from shenkang_wanda_api_req where id=#{id}")
    Map<String, Object> selectWandaById(Integer id);

    Integer queryLogListSizeByDateAndPathno(@Param("type") String type, @Param("logState") String logState,@Param("pathNo")String pathNo,@Param("startTime")String dateStart,@Param("endTime")String dateEnd);

    List<WandaApiReq> selectLogListByDateAndPathno(@Param("type") String type, @Param("size") Integer pageSize, @Param("num") Integer pageNum, @Param("logState") String logState,@Param("pathNo")String pathNo,@Param("startTime")String dateStart,@Param("endTime")String dateEnd);

    Integer queryLogListSizeByCreatimeAndPathno(@Param("type") String type, @Param("logState") String logState,@Param("pathNo")String pathNo,@Param("startTime")String dateStart,@Param("endTime")String dateEnd);

    List<WandaApiReq> selectLogListByCreatimeAndPathno(@Param("type") String type, @Param("size") Integer pageSize, @Param("num") Integer pageNum, @Param("logState") String logState,@Param("pathNo")String pathNo,@Param("startTime")String dateStart,@Param("endTime")String dateEnd);

    List<WandaApiReq> selectLogList1(@Param("type") String type, @Param("size") Integer pageSize, @Param("num") Integer pageNum, @Param("logState") String logState);

    Integer queryLogListSize(@Param("type") String type, @Param("logState") String logState);

    @Update("update shenkang_patient_resp" +
            "        set logstate=#{logstate}" +
            "        where id =#{id}")
    int updatePatientJsonStateById(@Param("id") int id,@Param("logstate") String logstate);

    @Update("update shenkang_wanda_api_req" +
            "        set logstate=#{logstate}" +
            "        where id =#{id}")
    int updateWandaStateById(Integer id, String logstate);

    @Select("select * from shenkang_patient_resp where id=#{id}")
    PatientJson selectPatientJsonById(@Param("id") int id);

    List<MedicalJson> getMedicalJson(@Param("state") int state, @Param("startTime") String startTime, @Param("endTime") String endTime,
                                     @Param("beginNum") Integer beginNum,@Param("pageSize") Integer pageSize,@Param("logstate") String logstate);

    List<MedicalSimpleInfo> selectSimpleInfoBySearchTime(@Param("searchTime") String searchTime, @Param("num") Integer num, @Param("size") Integer size);

    Integer selectSimpleInfoNumBySearchTime(@Param("searchTime") String searchTime, @Param("code") String code, @Param("startTime") String startTime, @Param("endTime") String endTime);

    Integer selectSimpleInfoNumBySearchTime02(@Param("searchTime") String searchTime,@Param("startTime") String startTime, @Param("endTime") String endTime);

    Integer selectSimpleInfoNum();

    @Update("update shenkang_ai_resp" +
            "        set logstate=#{logstate}" +
            "        where id =#{id}")
    int updateAiJsonStateById(@Param("id") int id,@Param("logstate") String logstate);

    @Select("select * from shenkang_ai_resp where id=#{id}")
    Map<String,Object> selectAiJsonById(@Param("id") int id);

    String selectLjMById(@Param("id") Integer id, @Param("state") Integer state);

    /**
     * @author: 邵梦丽 on 2020/8/27 0805
     * @param:
     * @return:
     * @Description:向数据表中插入患者信息的请求和返回请求日志
     */
    int insertPatientJson(PatientJson patientJson);

    @Select("select * from shenkang_debug_log where id=#{id}")
    DebugLog getDebugLogById(@Param("id") int id);

    @Select("select * from shenkang_medical_resp where id=#{id}")
    MedicalJson selectMedicalJsonById(@Param("id") int id);

    @Update("update shenkang_medical_resp" +
            "        set logstate=#{logstate}" +
            "        where id =#{id}")
    int updateMedicalJsonStateById(@Param("id") int id,@Param("logstate") String logstate);

    List<PatientJson> getPatientJson(@Param("state") int state, @Param("startTime") String startTime, @Param("endTime") String endTime,
                                     @Param("beginNum") Integer beginNum,@Param("pageSize") Integer pageSize,@Param("logstate") String logstate);

    Integer updateLogStateById(@Param("id") int id,@Param("state") String state);
    /**
     * @author: 邵梦丽 on 2020/10/17 15:23
     * @param:
     * @return:
     * @Description:根据pathNo查看当前患者信息是否已经存在
     */
    Integer getCountPatientByPathNo(@Param("pathNo")String pathNo);

    int insertPatient(@Param("patient") Patient patient, @Param("searchDate")String updateDateTime);

    /**
     * @author: 邵梦丽 on 2020/10/17 15:29
     * @param:
     * @return:
     * @Description:更新病人信息
     */
    int updatePatient(@Param("patient")Patient patient,@Param("searchDate")String updateDateTime);

    /**
     * @author: 邵梦丽 on 2020/8/27 0805
     * @param:
     * @return:
     * @Description:向数据表中插入病理信息的请求和返回请求日志
     */
    int insertMedicalJson(MedicalJson medicalJson);

    /**
     * @author: 邵梦丽 on 2020/10/17 15:38
     * @param:
     * @return:
     * @Description:查看病理信息是否存在
     */
    Integer getCountMedicalByPathNo(@Param("pathNo")String pathNo);

    /**
     * @author: 邵梦丽 on 2020/8/27 0805
     * @param:
     * @return:
     * @Description:查看病理信息是否存在，如果存在则进行插入
     */
    int addMedicalInfo(@Param("medicalInfo") MedicalSimple medicalInfo, @Param("searchDate")String updateDateTime);

    /**
     * @author: 邵梦丽 on 2020/10/17 15:43
     * @param:
     * @return:
     * @Description:更新患者信息
     */
    int updateMedical(@Param("medicalInfo")MedicalSimple medicalInfo,@Param("searchDate")String updateDateTime);

    /**
     * @author: 邵梦丽 on 2020/10/19 17:01
     * @param:
     * @return:
     * @Description:根据病理信息查看id
     */
    Integer getMedicalIdByPathNo(@Param("pathNo")String pathNo);

    /**
     * @author: 邵梦丽 on 2020/8/27 0805
     * @param:
     * @return:
     * @Description:插入表wax_patient_info中单个数据
     */
    int insertAIJson(AIJson aiJson);

    /**
     * @author: 邵梦丽 on 2020/8/24 16:46
     * @param:
     * @return:
     * @Description:插入表specimen_info中多个数据
     */
    int insertSpecimenInfos(List<SpecimenInfo> list);

    List<DebugLog> getDebugLogs(@Param("pathNo") String pathNo,@Param("startTime") Long startTime, @Param("endTime") Long endTime,
                                @Param("beginNum") Integer beginNum,@Param("pageSize") Integer pageSize,
                                @Param("apiName") String apiName,@Param("state") String state);

    List<Map<String,Object>> getAiJson(@Param("pathNo") String pathNo, @Param("startTime") String startTime, @Param("endTime") String endTime,
                                       @Param("beginNum") Integer beginNum, @Param("pageSize") Integer pageSize, @Param("logstate") Integer logstate);
}
