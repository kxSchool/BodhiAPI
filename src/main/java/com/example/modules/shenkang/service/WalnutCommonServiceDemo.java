package com.example.modules.shenkang.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.modules.shenkang.mapper.CommonDao;
import com.example.modules.shenkang.mapper.WalnutCommonDao;
import com.example.modules.shenkang.pojo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class WalnutCommonServiceDemo {
    private static final Logger logger = LoggerFactory.getLogger(WalnutCommonServiceDemo.class);
    @Autowired
    WalnutCommonDao walnutCommonDao;//查询数据

    @Value("${shenkang.hospitalCode}")
    private String hospitalCode;

    @Autowired
    CommonDao commonDao;//存储

    public Result putData(Integer applyId, String updateDateTime) {
        Result rs = new Result();
        String pathNo = "";
        try {
            //查询pat_no
            String patNo = walnutCommonDao.selectHisIdByApplyId(applyId);
            logger.info("applyid[" + Integer.valueOf(applyId) + "],patNo is " + patNo);
            //查询病理号
            pathNo = walnutCommonDao.selectPathIdByApplyId(applyId);
            logger.info("applyid[" + Integer.valueOf(applyId) + "],pathNo is " + pathNo);
            //上传人员信息
            logger.info("applyid[" + Integer.valueOf(applyId) + "],pathNo is " + pathNo + "开始上传人员信息！");
            Result result = checkUpdateInfoNew(patNo, pathNo, updateDateTime);
            PatientInfo data = (PatientInfo) result.getData();
            //上传 病例信息
            String wdbz = "";
            if (data.getBirth_place() != null && !data.getBirth_place().equals("")) {
                if (data.getBirth_place().contains("上海市")) {
                    wdbz = "0";
                } else if (data.getBirth_place().contains("市")) {
                    wdbz = "1";
                } else {
                    wdbz = "9";
                }
            } else {
                wdbz = "9";
            }
            logger.info("applyid[" + Integer.valueOf(applyId) + "],pathNo is " + pathNo + " 开始上传病理信息！");
            Result result1 = updateInfoNew(applyId, wdbz, updateDateTime);
            rs.setMsg("applyid：" + applyId + ",pathNo：" + pathNo);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("medicatInfo", result1);
            map.put("patientInfo", result);
            rs.setData(map);
        } catch (Exception e) {
            e.printStackTrace();
            rs.setException(e);
            rs.setMsg("applyid：" + applyId + ",pathNo：" + pathNo);
            rs.setCode(9999);
        }
        return rs;
    }

    public Result updateInfoNew(Integer applyId, String wdbz, String updateDateTime) {
        Result result = new Result();
        MedicalJson medicalJson = new MedicalJson();
        String pathNo11 = "";

        try {
            //根据applyId 查询信息
            MedicalInfo medicalInfo = walnutCommonDao.selectMediInfoByApplyId(applyId);

            medicalInfo.setOrganization_id(hospitalCode);
            medicalInfo.setOrganization_name("上海市公共卫生临床中心");
            String patientClass = medicalInfo.getPatient_class();
            switch (patientClass) {
                case "住院":
                    medicalInfo.setPatient_class("02");
                    break;
                case "门诊":
                    medicalInfo.setPatient_class("01");
                    break;
                case "体检":
                    medicalInfo.setPatient_class("04");
                    break;
                default:
                    medicalInfo.setPatient_class("05");
                    break;
            }
            String reType = medicalInfo.getResult_status();
            switch (reType) {
                case "101":
                    medicalInfo.setResult_status("登记完成");
                    medicalInfo.setResult_status_code("1020");
                    break;
                case "102":
                    medicalInfo.setResult_status("已取材");
                    medicalInfo.setResult_status_code("2000");
                    break;
                case "103":
                    medicalInfo.setResult_status("制片中");
                    medicalInfo.setResult_status_code("2100");
                    break;
                case "104":
                    medicalInfo.setResult_status("已制片");
                    medicalInfo.setResult_status_code("2110");
                    break;
                case "105":
                    medicalInfo.setResult_status("报告书写中");
                    medicalInfo.setResult_status_code("3050");
                    break;
                case "107":
                    medicalInfo.setResult_status("报告审核中");
                    medicalInfo.setResult_status_code("3070");
                    break;
                case "108":
                    medicalInfo.setResult_status("审核完成");
                    medicalInfo.setResult_status_code("3080");
                    break;
                case "109":
                    medicalInfo.setResult_status("报告已发布");
                    medicalInfo.setResult_status_code("4040");
                    break;
                case "110":
                    medicalInfo.setResult_status("报告已发布");
                    medicalInfo.setResult_status_code("4040");
                    break;

            }

            //病理诊断0
            String tem = medicalInfo.getProject_text();
            JSONObject rc = JSON.parseObject(medicalInfo.getRecontent());
            switch (tem) {
                case "normal":
                    if (rc != null && rc.containsKey("病理诊断")) {
                        medicalInfo.setPathological_diagnosis(rc.getString("病理诊断"));
                    }
                    medicalInfo.setProject_text("常规病理");
                    medicalInfo.setProject_id("01");
                    break;
                case "cytology":
                    if (rc != null && rc.containsKey("细胞学诊断")) {
                        medicalInfo.setPathological_diagnosis(rc.getString("细胞学诊断"));
                    }
                    medicalInfo.setProject_text("细胞学");
                    medicalInfo.setProject_id("05");
                    break;
                case "gastrointestinal-endoscopy":
                    if (rc != null && rc.containsKey("病理学诊断")) {
                        medicalInfo.setPathological_diagnosis(rc.getString("病理学诊断"));
                    }
                    medicalInfo.setProject_text("常规病理");
                    medicalInfo.setProject_id("01");
                    break;
                case "GSTS":
                    medicalInfo.setProject_text("常规病理");
                    medicalInfo.setProject_id("01");
                    break;
                case "frozen":
                    medicalInfo.setProject_text("冰冻检查");
                    medicalInfo.setProject_id("02");
                    break;
                case "TBS":
                    if (rc != null && rc.containsKey("TBSDiagnose")) {
                        medicalInfo.setPathological_diagnosis(rc.getString("TBSDiagnose"));
                    }
                    medicalInfo.setProject_text("常规病理");
                    medicalInfo.setProject_id("01");
                    break;
                case "autopsy":
                    medicalInfo.setProject_text("特殊染色");
                    medicalInfo.setProject_id("06");
                    break;
            }
            List<SpecimenInfo> specimenInfos = walnutCommonDao.selSpecimenListByApplyId(applyId);
            //根据
            medicalInfo.setSpecimen_list(specimenInfos);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date(System.currentTimeMillis());
            medicalInfo.setCreateTime(formatter.format(date));
            medicalInfo.setPathNo(medicalInfo.getPathological_number());
            medicalInfo.setWdbz(wdbz);
            //查询取材时间
            String ti = walnutCommonDao.selectTime(applyId);
            medicalInfo.setBased_time(ti);
            //查询病理信息是否存在，如果存在则插入，否则更新
            int numTemp = commonDao.selectMedPatient_id(medicalInfo.getPathNo());
            //病历信息加密存储
            List<Integer> waxIds = new ArrayList<>();
            if (specimenInfos.size() > 0) {
                for (SpecimenInfo specimenInfo : specimenInfos) {
                    //查询fileId
                    List<HFileInfo> fileInfos = walnutCommonDao.selectFileInfosByWaxId(Integer.parseInt(specimenInfo.getSpecimen_id()), medicalInfo.getId());
                    specimenInfo.setPathological_number(medicalInfo.getPathological_number());
                }
            }

            result.setData(medicalInfo);
            result.setCode(200);
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(500);
            result.setException(e);
        }
        return result;
    }


    public Result checkUpdateInfoNew(String patNo, String pathNo, String updateDateTime) {//patients 是hisId 返回  data
        Result result = new Result();
        PatientJson patientJson = new PatientJson();
        try {
            PatientInfo patientInfo = walnutCommonDao.selectPatient02(patNo);
            if (patientInfo != null) {
                logger.info("get patient [hisId:" + patNo + "] is sucess! ");
            } else {
                logger.error("get patient [hisId:" + patNo + "] is failed! ");
                result.setCode(300);
                result.setMsg("获取患者信息失败");
                return result;
            }
            if(patientInfo.getPatient_id()==null || patientInfo.getPatient_id().equals("")){
                patientInfo.setPatient_id(patNo);
            }
            if(patientInfo.getSex()!=null){
                switch (patientInfo.getSex()) {
                    case "男":
                        patientInfo.setSex("1");
                        break;
                    case "女":
                        patientInfo.setSex("2");
                        break;
                    default:
                        patientInfo.setSex("0");
                        break;
                }
            }
            String s = "^[0-9a-zA-z].*";
            if (patientInfo.getInsurance_id() != null) {
                if (patientInfo.getInsurance_id().matches(s) && patientInfo.getInsurance_id().length() == 9) {
                    patientInfo.setInsurance_type("0");
                    if (patientInfo.getId_card_no() != null) {
                        patientInfo.setIdentityType("1");
                        patientInfo.setIdentityID(patientInfo.getId_card_no());
                    } else {
                        patientInfo.setIdentityType("1");
                        patientInfo.setIdentityID("000000000000000000");
                    }
                } else {
                    patientInfo.setInsurance_type("9");
                }
            } else {
                patientInfo.setInsurance_type("9");
            }
            if (patientInfo.getId_card_no() != null) {
                patientInfo.setIdentityType("1");
                patientInfo.setIdentityID(patientInfo.getId_card_no());
            } else {
                patientInfo.setIdentityType("1");
                patientInfo.setIdentityID("000000000000000000");
            }

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date(System.currentTimeMillis());
            patientInfo.setPathNo(pathNo);
            patientInfo.setCreateTime(formatter.format(date));
            patientInfo.setOrganization_id(hospitalCode);
            patientInfo.setState(0);

            //查询记录是否存在，如果存在则更新，不存在则新增
            Integer numTepm = commonDao.selectPatPatient_id(patientInfo.getPathNo());
            Integer integer;
            //患者信息加密
            logger.info("查询病人数据成功");
            result.setData(patientInfo);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("更新病人数据失败，pathNo is " + pathNo + ";hisId is " + pathNo + e.toString());
            result.setData(e);
        }
        return result;
    }
}
