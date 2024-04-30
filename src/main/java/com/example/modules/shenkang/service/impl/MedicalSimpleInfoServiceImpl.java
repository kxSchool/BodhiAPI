package com.example.modules.shenkang.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.util.TypeUtils;
import com.example.modules.shenkang.mapper.MedicalSimpleDao;
import com.example.modules.shenkang.pojo.*;
import com.example.modules.shenkang.service.MedicalSimpleInfoService;
import com.example.modules.shenkang.service.WalnutCommonService;
import com.example.modules.shenkang.service.WanDaPushService;
import com.example.modules.shenkang.utils.ShenkangUtils;
import com.example.modules.simulation.utils.PatientAPIUtils;
import com.example.utils.encrypt.DesUtil;
import org.apache.tomcat.util.buf.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MedicalSimpleInfoServiceImpl implements MedicalSimpleInfoService {
    private final Logger logger= LoggerFactory.getLogger(getClass());

    @Value("${shenkang.hospitalCode}")
    private String hospitalCode;

    @Value("${shenkang.webApiUrl.SliceInfo}")
    private String sliceInfo;

    @Autowired
    MedicalSimpleDao medicalSimpleDao;

    @Autowired
    WanDaPushService wanDaPushService;

    /**
     * @author: 邵梦丽 on 2020/9/2 12:20
     * @param:
     * @return:
     * @Description:根据状态批量向万达推送数据
     */
    public Result pushDatesByState(){
        List<String> infoList =null;

        //对没有推送的数据进行推送
        infoList=medicalSimpleDao.getUnpushedMedicalIds();

        List<String> faildList=new ArrayList<>();
        for(int i=0;i<infoList.size();i++){
            String idTemp=infoList.get(i);
            Result result= new Result();
            try{
                result=pushDate(idTemp);
            }catch(Exception e){
                e.printStackTrace();
                result.setCode(300);
                result.setMsg(idTemp);
            }
            if(result.getCode()!=200){
                faildList.add(idTemp);
            }
        }
        Result result=new Result();
        if(faildList.size()!=0)
        {  result.setCode(300);
            result.setMsg("the operations of pushDates is failed,check the ids of MedicalInfo:["+ StringUtils.join(faildList, ',')+"]");
            logger.error("the operations of pushDates is failed,check the ids of MedicalInfo:["+StringUtils.join(faildList, ',')+"]");
        }else{
            result.setCode(200);
            result.setMsg("ok");
            logger.info("the operations of pushDates is succeed");
        }
        return result;
    }

    /**
     * @author: 邵梦丽 on 2020/9/2 12:01
     * @param:
     * @return:
     * @Description:向万达推送数据，单条处理方式
     */
    public Result pushDate(String idTemp) throws RuntimeException{
        Result result=new Result();
        if(idTemp!=null) {
            //获取根据id获取病理信息
            MedicalInfo medicalInfoTemp = medicalSimpleDao.getMedicalInfoByHis(idTemp);
            if (medicalInfoTemp == null) {
                result.setMsg("无法获取"+idTemp+"病理信息");
                result.setCode(300);
                logger.error("获取数据库病理信息失败！");
                return result;
            }
            MedicalInfo medicalInfo=null;
            //如果病理号里面没有master_id，则发送请求获取患者信息
            PatientInfo patientInfo=null;
            if(medicalInfoTemp.getPatient_master_id()==null||medicalInfoTemp.getPatient_master_id().trim().length()==0){
                patientInfo=medicalSimpleDao.getPatientByPathNo(medicalInfoTemp.getPathNo());
                if(patientInfo==null){
                    logger.error("Unable to get patient information！");
                    result.setMsg(idTemp);
                    result.setCode(300);
                    medicalSimpleDao.updateMedicalInfo(idTemp,"3");//已经发送
                    return result;
                }
                patientInfo.setIdentityID(patientInfo.getId_card_no());
                //患者信息加密
                //patientInfo.setPatient_id(patientInfo.getPathNo());
                PatientInfo patientInfoencry= WalnutCommonService.encryptInfo02(patientInfo);
                //推送患者信息
                logger.info("step1:Start pushing patient information...");
                result=wanDaPushService.registerPatient(patientInfoencry);
                if(result.getCode()==200){
                    logger.info("step1:Push patient information successfully!");
                    //设置患者信息
                    medicalSimpleDao.updateMedicalInfo(idTemp,"1");
                    String patient_master_id= (String) result.getData();
                    if(patient_master_id!=null&&patient_master_id.length()!=0){
                        patientInfo.setPatient_master_id(patient_master_id);
                        //更新患者patient_master_id
                        medicalSimpleDao.updatePatientMasterById(patient_master_id,patientInfo.getId().toString());
                        //更新病理信息中的patient_master_id
                        medicalSimpleDao.updateMedicalMasterById(patient_master_id,medicalInfoTemp.getId().toString());
                    }else{
                        logger.error("获取master_id失败！推送终止！");
                        result.setMsg(idTemp);
                        result.setCode(300);
                        medicalSimpleDao.updateMedicalInfo(idTemp,"3");//已经发送
                        return result;
                    }
                }else{
                    logger.error("患者信息推送失败！");
                    result.setCode(300);
                    medicalSimpleDao.updateMedicalInfo(idTemp,"3");//已经发送
                    return result;
                }
            }
            //获取详细病理信息
            medicalInfo= medicalSimpleDao.getMedicalDetailsInfoByHis(idTemp);
            if(medicalInfo==null){
                System.out.println("获取病理信息失败");
                logger.error("Failed to obtain pathological information");
                result.setMsg(idTemp);
                result.setCode(300);
                medicalSimpleDao.updateMedicalInfo(idTemp,"3");
                return result;
            }
            wanDaPushService.setBased_time(medicalInfo);
            //病理信息加密
            //medicalInfo.setPatient_id(medicalInfo.getPathNo());
            if (medicalInfo.getMed_rec_no() != null && !"".equals(medicalInfo.getMed_rec_no())) {
                medicalInfo.setMed_rec_no(DesUtil.encrypt(medicalInfo.getMed_rec_no()));
            }
            if (medicalInfo.getOut_patient_no() != null && !"".equals(medicalInfo.getOut_patient_no())) {
                medicalInfo.setOut_patient_no(DesUtil.encrypt(medicalInfo.getOut_patient_no() + ""));
            }
            if (medicalInfo.getIn_patient_no() != null && !"".equals(medicalInfo.getIn_patient_no())) {
                medicalInfo.setIn_patient_no(DesUtil.encrypt(medicalInfo.getIn_patient_no() + ""));
            }
            if (medicalInfo.getPlacer_order_no() != null && !"".equals(medicalInfo.getPlacer_order_no())) {
                medicalInfo.setPlacer_order_no(DesUtil.encrypt(medicalInfo.getPlacer_order_no() + ""));
            }
            medicalInfo.setOrganization_id(hospitalCode);
            //推送病理信息
            logger.info("step2:Start pushing pathological information");
            result=wanDaPushService.updataMedicalInfo(medicalInfo);
            String state;
            if(result.getCode()==200){
                state="1";//已经发送
                result.setMsg("发送成功");
                result.setCode(200);
                logger.info("step2:Send pathological information successfully");
                //将businessid存入数据库
                medicalSimpleDao.updateMedicalInfoStateAndBusinessid(idTemp,state,result.getData().toString());
                medicalInfo.setObservationUID(result.getData().toString());
                //将ObservationUID放入fileinfo文件里面
                medicalSimpleDao.updateFileInfoObservationUID(medicalInfo.getPathNo(),result.getData().toString());
                boolean isUploadFile=true;
                if(isUploadFile){
                    //文件传输接口
                    Object idBusinessId=result.getData();
                    logger.info("step3:Start calling file upload interface...");
                    result= wanDaPushService.postFileNew(Integer.parseInt(idTemp),idBusinessId.toString(), medicalInfo.getPathNo());
                    if(result.getCode()==200){
                        logger.info("step4:Check file upload...");
                        //检查文件上传是否成功
                        try{
                            result=wanDaPushService.requestCheck(Integer.parseInt(idTemp));
                        }catch(Exception e){
                            result.setCode(300);
                            result.setMsg(idTemp);
                            medicalSimpleDao.updateMedicalInfo(idTemp,"3");
                        }

                        if(result.getCode()==200){
                            logger.info("step4:Check file upload: file verification successful");
                            state="2";//校验成功
                            medicalSimpleDao.updateMedicalInfo(idTemp,state);
                            result.setCode(200);
                            return result;
                        }else{
                            System.out.println("文件校验失败");
                            logger.error("step4:Check file upload: file verification failed！");
                            state="3";//文件校验失败
                            medicalSimpleDao.updateMedicalInfo(idTemp,state);
                            result.setMsg(idTemp);
                            result.setCode(300);
                            return result;
                        }
                    }else{
                        result.setMsg(idTemp);
                        result.setCode(300);
                        logger.error("step3:File upload failed！");
                        medicalSimpleDao.updateMedicalInfo(idTemp,"3");
                        return result;
                    }
                }
            }else{
                state="3";//已发送,但是失败
                result.setMsg(idTemp);
                result.setCode(300);
                System.out.println("推送病理信息失败");
                logger.error("step2:Failed to push pathological information");
                medicalSimpleDao.updateMedicalInfo(idTemp,state);
                return result;
            }

        }else{
            result.setMsg(idTemp);
            result.setCode(300);
            logger.error("Failed to push pathological information:"+idTemp);
            medicalSimpleDao.updateMedicalInfo(idTemp,"3");
            return result;
        }
        return result;
    }

    public int insertPatientJson(PatientJson patientJson){
        return medicalSimpleDao.insertPatientJson(patientJson);
    }

    @Override
    public DebugLog getLogById(Integer id){
        return medicalSimpleDao.getDebugLogById(id);
    }

    public int setWandaRequestStateById(Integer id){
        Map<String,Object> map=medicalSimpleDao.selectWandaById(id);
        String logstate="";
        Integer logStateTemp=new Integer(map.get("logstate").toString());
        if(logStateTemp.equals(0)){
            return 0;
        }else if(logStateTemp.equals(1)){
            logstate="3";
        }else if(logStateTemp.equals(2)){
            logstate="4";
        }else if(logStateTemp.equals(3)){
            logstate="1";
        }else if(logStateTemp.equals(4)){
            logstate="2";
        }
        return medicalSimpleDao.updateWandaStateById(id,logstate);
    }

    @Override
    public int setPatientJsomStateById(Integer id) {
        PatientJson patientJson=medicalSimpleDao.selectPatientJsonById(id);
        String logstate="";
        Integer logStateTemp=patientJson.getLogstate();
        if(logStateTemp.equals(0)){
            return 0;
        }else if(logStateTemp.equals(1)){
            logstate="3";
        }else if(logStateTemp.equals(2)){
            logstate="4";
        }else if(logStateTemp.equals(3)){
            logstate="1";
        }else if(logStateTemp.equals(4)){
            logstate="2";
        }
        return medicalSimpleDao.updatePatientJsonStateById(id,logstate);
    }

    @Override
    public List<MedicalJson> getMedicalJsonByWhole(String startTime,String endTime,
                                                   Integer beginNum, Integer pageSize,String logstate){
        List<MedicalJson> list= medicalSimpleDao.getMedicalJson(1,startTime,endTime,beginNum,pageSize,logstate);
        return list;
    }

    @Override
    public int setMedicalJsonStateById(Integer id) {
        MedicalJson patientJson=medicalSimpleDao.selectMedicalJsonById(id);
        String logstate="";
        Integer logStateTemp=patientJson.getLogstate();
        if(logStateTemp.equals(0)){
            return 0;
        }else if(logStateTemp.equals(1)){
            logstate="3";
        }else if(logStateTemp.equals(2)){
            logstate="4";
        }else if(logStateTemp.equals(3)){
            logstate="1";
        }else if(logStateTemp.equals(4)){
            logstate="2";
        }
        return medicalSimpleDao.updateMedicalJsonStateById(id,logstate);
    }

    @Override
    public List<PatientJson> getPatientJsonByWhole(String startTime,String endTime,
                                                   Integer beginNum, Integer pageSize,String logstate){
        List<PatientJson> list= medicalSimpleDao.getPatientJson(1,startTime,endTime,beginNum,pageSize,logstate);
        return list;
    }

    @Override
    public int setLogStateById(Integer id) {
        DebugLog debugLog=medicalSimpleDao.getDebugLogById(id);
        String state="";
        String stateTemp=debugLog.getState();
        if(stateTemp.equals("0")){
            return 0;
        }else if(stateTemp.equals("1")){
            state="3";
        }else if(stateTemp.equals("2")){
            state="4";
        }else if(stateTemp.equals("3")){
            state="1";
        }else if(stateTemp.equals("4")){
            state="2";
        }
        return medicalSimpleDao.updateLogStateById(id,state);
    }

    public int setAiJsonStateById(Integer id){
        Map<String,Object> map=medicalSimpleDao.selectAiJsonById(id);
        String logstate="";
        Integer logStateTemp=new Integer(map.get("logstate").toString());
        if(logStateTemp.equals(0)){
            return 0;
        }else if(logStateTemp.equals(1)){
            logstate="3";
        }else if(logStateTemp.equals(2)){
            logstate="4";
        }else if(logStateTemp.equals(3)){
            logstate="1";
        }else if(logStateTemp.equals(4)){
            logstate="2";
        }
        return medicalSimpleDao.updateAiJsonStateById(id,logstate);
    }

    /**
     * @author: 邵梦丽 on 2020/8/27 9:29
     * @param:
     * @return:
     * @Description:处理第三方接口返回信息，将信息解析并存储到数据库中(主要是处理数据插入异常，后续进行补录的）
     */
    @Override
    public Result addPatients(StringBuffer detalisResp, String updateDateTime) {
        Result result = new Result();
        //处理返回信息
        JSONObject jsonParam= null;
        String msg=null;
        JSONObject response=null;
        String code=null;
        JSONArray details=null;
        JSONArray patientList=null;
        try {

            jsonParam = new JSONObject(detalisResp.toString());
            response=jsonParam.getJSONObject("Response");
            System.out.println(response.toString());
            msg=response.getString("Msg");
            code=response.getString("Code");
            //返回请求失败
            if(!code.equals("0"))
            {
                result.setCode(300);
                result.setMsg("the request of 'getPatientRegister' is failed,api's response Msg=["+msg+"]");
                logger.error("the request of 'getPatientRegister' is failed,api's response Msg=[{}]",msg);
            }else{
                //返回请求成功，数据解析
                patientList=response.getJSONArray("data");
                int num= patientList.length();
//                List<Patient> list=new ArrayList<>();
                String createDatatime;
                if(num>0){
                    //逐条解析
                    for (int i=0;i<num;i++){
                        createDatatime= PatientAPIUtils.getTime();
                        String newPatient=patientList.getString(i);
                        Patient newMedicalInfo = JSON.parseObject(newPatient, Patient.class);
                        newMedicalInfo.setCreateTime(createDatatime);
                        //逐条添加记录
                        //查看记录是否存在，如果存在则更新，不存在则新增
                        int countTemp=medicalSimpleDao.getCountPatientByPathNo(newMedicalInfo.getPathNo());
                        if(countTemp==0)
                        {
                            medicalSimpleDao.insertPatient(newMedicalInfo,updateDateTime);
                            logger.info("插入患者成功！pathNo是："+newMedicalInfo.getPathNo());
                        }else{
                            medicalSimpleDao.updatePatient(newMedicalInfo,updateDateTime);
                            logger.info("更新患者成功！pathNo是："+newMedicalInfo.getPathNo());
                        }
                        //记录单条日志
                        PatientJson patientJson=new PatientJson(newMedicalInfo.getPathNo(),updateDateTime, PatientAPIUtils.getTime(), newPatient,0,0);
                        int i1 = insertPatientJson(patientJson);

//                        list.add(newMedicalInfo);
                        System.out.println(newMedicalInfo);
                    }
                    //Integer state=medicalSimpleDao.addPatientsList(list);
                    result.setCode(200);
                    result.setMsg("患者信息存储成功,更新患者信息"+num+"条;");
                    logger.info("患者信息存储成功");

                }else{
                    result.setCode(200);
                    result.setMsg("the request of 'getPatientRegister' does not include data!");
                    logger.info("the request of 'getPatientRegister' does not include data!");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            result.setCode(300);
            result.setMsg("患者信息解析异常:"+e.getMessage());
            logger.error("患者信息解析异常:"+e.getMessage());
            return result;
        }
        return result;
    }

    /**
     * @author: 邵梦丽 on 2020/8/27 11:22
     * @param:
     * @return:
     * @Description:存储啷珈病理信息接口返回的报文信息
     */
    public int insertMedicalJson(MedicalJson medicalJson){
        return medicalSimpleDao.insertMedicalJson(medicalJson);
    }

    /**
     * @author: 邵梦丽 on 2020/8/27 9:29
     * @param:
     * @return:
     * @Description:处理第三方接口返回信息，将信息解析并存储到数据库中(如果之前批量插入失败，则调用此接口进行信息插入的补录)
     */
    @Override
    public Result addMedicalInfos(StringBuffer detalisResp,String updateDateTime) {
        Result result = new Result();
        //处理返回信息
        JSONObject jsonParam= null;
        String msg=null;
        JSONObject response=null;
        String code=null;
        JSONArray details=null;
        JSONArray medicalInfoList=null;
        try {
            jsonParam = new JSONObject(detalisResp.toString());
            response=jsonParam.getJSONObject("Response");
            System.out.println(response.toString());
            msg=response.getString("Msg");
            code=response.getString("Code");
            if(!code.equals("0"))
            {
                result.setCode(300);
                logger.error("the request of 'getExamRequestRegister' is failed,api's response Msg=["+msg+"]");
                result.setMsg("the request of 'getExamRequestRegister' is failed,api's response Msg=["+msg+"]");
                result.setData(details);
            }else{
                medicalInfoList=response.getJSONArray("data");
                int num= medicalInfoList.length();
                if(num>0){
                    //定义主表list
                    List<MedicalSimple> list=new ArrayList<>();
                    //定义子表list
                    List<SpecimenInfo> listSon=new ArrayList<>();
                    //获取创建时间
                    String createDatatime;
                    for (int i=0;i<num;i++){
                        //获取时间
                        createDatatime=PatientAPIUtils.getTime();
                        String newMdeicalInfoTemp=medicalInfoList.getString(i);
                        MedicalSimple newMedicalInfo = JSON.parseObject(newMdeicalInfoTemp, MedicalSimple.class);
                        newMedicalInfo.setCreateTime(createDatatime);
                        //单条数据插入，并返回插入的主键
                        //查询病理信息是否存在，存在则更新，不存在则新增
                        int countTemp=medicalSimpleDao.getCountMedicalByPathNo(newMedicalInfo.getPathNo());
                        Integer newMdeicalId=0;
                        if(countTemp==0){
                            newMdeicalId=medicalSimpleDao.addMedicalInfo(newMedicalInfo,updateDateTime);
                            logger.info("插入病理信息成功，病例号："+newMedicalInfo.getPathNo());
                        }else{
                            newMdeicalId=medicalSimpleDao.updateMedical(newMedicalInfo,updateDateTime);
                            //获取主id
                            Integer id=medicalSimpleDao.getMedicalIdByPathNo(newMedicalInfo.getPathNo());
                            newMedicalInfo.setId(id);
                            logger.info("更新信息成功，病例号："+newMedicalInfo.getPathNo());
                        }

                        //调用接口,查询"切片数据"
                        String pathNo=newMedicalInfo.getPathological_number();
                        //创建请求和设置请求相关
                        HashMap<String,String> paramsList=new HashMap<>();
                        paramsList.put("pathNo",pathNo);
                        paramsList.put("slideNo","");
                        AIJson aiJson=new AIJson();
                        aiJson.setPathNo(pathNo);
                        aiJson.setCreatorTime(PatientAPIUtils.getTime());
                        try {
                            StringBuffer detailsTemp=PatientAPIUtils.getUrlRespDetails(sliceInfo, paramsList);
                            aiJson.setRspJson(detailsTemp.toString());
                            String logstate="0";
                            if(!ShenkangUtils.isLangjiaFormat(detailsTemp)){
                                logstate="1";
                            }
                            aiJson.setLogstate(logstate);
                            medicalSimpleDao.insertAIJson(aiJson);
                        } catch (IOException e) {
                            aiJson.setLogstate("2");
                            medicalSimpleDao.insertAIJson(aiJson);
                            logger.error("获取sliceInfo接口信息失败！"+pathNo);
                        }
                        if(newMdeicalId>=1){
                            //获取子表数据
                            List<SpecimenInfo> specimenInfoList= newMedicalInfo.getSpecimen_list();
                            if(specimenInfoList!=null&&specimenInfoList.size()>0)
                            {
                                for(int j=0;j<specimenInfoList.size();j++){
                                    SpecimenInfo specimenInfotemp=specimenInfoList.get(j);
                                    specimenInfotemp.setMedical_id(newMedicalInfo.getId());
                                    specimenInfotemp.setPathological_number(newMedicalInfo.getPathological_number());
                                    listSon.add(specimenInfotemp);
                                }
                                //批量插入子表
                                Integer stateSon=medicalSimpleDao.insertSpecimenInfos(listSon);
                                listSon.clear();//清空数据
                                if(stateSon<0)
                                {
                                    result.setCode(300);
                                    logger.error("插入病理信息失败！");
                                    result.setMsg("插入病理信息失败！");
                                }else{
                                    result.setCode(200);
                                    result.setMsg("the request of 'ExamRequestRegister' is successed,api's responseMsg:"+msg);
                                    logger.info("插入病理数据成功！");
                                }

                            }else{
                                result.setCode(200);
                                result.setMsg("切片数据为空,api's responseMsg:"+msg);
                                logger.info("切片数据为空！");
                            }
                        }else{
                            result.setCode(300);
                            logger.error("local operation of 'ExamRequestRegister' is failed!");
                            result.setMsg("解析病理数据失败！");
                        }
                        //插入日志
                        MedicalJson medicalJson=new MedicalJson(newMedicalInfo.getPathNo(),updateDateTime, PatientAPIUtils.getTime(), newMdeicalInfoTemp,0,0);
                        insertMedicalJson(medicalJson);

                    }
                    result.setCode(200);
                    result.setMsg("病理信息更新成功，更新数据"+num+"条");
                }else{
                    result.setCode(200);
                    result.setMsg("local operation of 'ExamRequestRegister' does not include data!");
                    logger.info("请求返回病理信息为空！");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            result.setCode(300);
            logger.error("解析病理数据异常:"+e.getMessage());
            result.setMsg("local operation of 'ExamRequestRegister' Mes:"+e.getMessage());
            return result;
        }
        return result;
    }

    public List<DebugLog> getLogs(String pathNo,String startTime,String endTime,Integer beginNum,Integer pageSize,String apiName,String state){
        Long startTimeTemp=null;
        Long endTimeTemp=null;
        if(startTime!=null){
            startTimeTemp = ShenkangUtils.dateParseToLong(startTime);
        }
        if(endTime!=null){
            endTimeTemp= ShenkangUtils.dateParseToLong(endTime);
            if(endTimeTemp!=null)
                endTimeTemp=endTimeTemp+86400000l;
        }
        List<DebugLog> patientLogs=medicalSimpleDao.getDebugLogs(pathNo,startTimeTemp,endTimeTemp,beginNum,pageSize,apiName,state);
        return patientLogs;
    }

    @Override
    public List<Map<String,Object>> getAiJsonList(String pathNo, String startTime, String endTime,
                                                  Integer beginNum, Integer pageSize, Integer logstate){
        List<Map<String,Object>> list= medicalSimpleDao.getAiJson(pathNo,startTime,endTime,beginNum,pageSize,logstate);
        return list;
    }
}
