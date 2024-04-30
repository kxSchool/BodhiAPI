package com.example.modules.shenkang.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.modules.shenkang.mapper.CommonDao;
import com.example.modules.shenkang.mapper.WalnutCommonDao;
import com.example.modules.shenkang.pojo.*;
import com.example.modules.shenkang.utils.ShenkangUtils;
import com.example.utils.encrypt.DesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class WalnutCommonService {
    private static final Logger logger = LoggerFactory.getLogger(WalnutCommonService.class);

    @Autowired
    CommonDao commonDao;//存储

    @Autowired
    WalnutCommonDao walnutCommonDao;//查询数据

    @Value("${shenkang.hospitalCode}")
    private String hospitalCode;

    @Value("${shenkang.imageUploadPath}")
    private String imageUploadPath;

    @Autowired
    MedicalSimpleInfoService medicalSimpleInfoService;//数据同步

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
            rs.setCode(2000);
        } catch (Exception e) {
            e.printStackTrace();
            rs.setException(e);
            rs.setMsg("applyid：" + applyId + ",pathNo：" + pathNo);
            rs.setCode(9999);
        }
        return rs;
    }

    public Result checkUpdateInfoNew(String patNo, String pathNo, String updateDateTime) {//patients 是hisId 返回  data
        Result result = new Result();
        PatientJson patientJson = new PatientJson();
        try {
            PatientInfo patientInfo2 = walnutCommonDao.selectPatient02(patNo);
            PatientInfo patientInfo=null;
            if (patientInfo2 != null) {
                logger.info("get patient [hisId:" + patNo + "] is sucess! ");
                String string = JSONObject.toJSONString(patientInfo2);
                patientInfo=JSONObject.parseObject(string,PatientInfo.class);
            } else {
                logger.error("get patient [hisId:" + patNo + "] is failed! ");
                result.setCode(300);
                result.setMsg("获取患者信息失败");
                return result;
            }
            if (patientInfo.getPatient_id() == null || patientInfo.getPatient_id().equals("")) {
                patientInfo.setPatient_id(patNo);
            }
            if (patientInfo.getSex() != null) {
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
            } else {
                patientInfo.setSex("0");
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
            //PojoUtil.setPatientInfoDesc(patientInfo);
            if (numTepm != 0) {
                //更新记录
                logger.info("更新患者信息！");
                commonDao.updataPatient(patientInfo, updateDateTime);
            } else {
                //新增记录
                logger.info("向数据库中插入患者信息！");
                integer = commonDao.insertPatient(patientInfo, updateDateTime);
            }
            logger.info("更新病人数据成功");
            result.setData(patientInfo);
            patientJson = new PatientJson(pathNo, updateDateTime, ShenkangUtils.getTime(), JSON.toJSONString(patientInfo), 0, 0);
        } catch (Exception e) {
            e.printStackTrace();
            patientJson = new PatientJson(pathNo, updateDateTime, ShenkangUtils.getTime(), e.getMessage(), 0, 1);
            logger.error("更新病人数据失败，pathNo is " + pathNo + ";hisId is " + pathNo + e.toString());
            result.setData(e);
        } finally {
            medicalSimpleInfoService.insertPatientJson(patientJson);
        }
        return result;
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
//            medicalInfo.setPatient_master_id(ss);
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
//            medicalInfo.setWdbz("09");

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
//            if (ti)
            medicalInfo.setBased_time(ti);
            //查询病理信息是否存在，如果存在则插入，否则更新
            //int numTemp=commonDao.selectMedPatient_id(medicalInfo.getPatient_id());
            int numTemp = commonDao.selectMedPatient_id(medicalInfo.getPathNo());
            //病历信息加密存储
            //PojoUtil.setMedicalInfoDesc(medicalInfo);
            if (numTemp != 0) {
                logger.info("更新病理信息");
                commonDao.updateMedical(medicalInfo, updateDateTime);
                int idTemp = commonDao.selectMed_id(medicalInfo.getPathNo());
                medicalInfo.setId(idTemp);
            } else {
                logger.info("插入病理信息");
                commonDao.insertMedical(medicalInfo, updateDateTime);
            }
//            Integer id = commonDao.insertMedical(medicalInfo);
            List<Integer> waxIds = new ArrayList<>();
            if (specimenInfos.size() > 0) {
                for (SpecimenInfo specimenInfo : specimenInfos) {
                    //查询fileId
                    List<HFileInfo> fileInfos = walnutCommonDao.selectFileInfosByWaxId(Integer.parseInt(specimenInfo.getSpecimen_id()), medicalInfo.getId());
                    specimenInfo.setPathological_number(medicalInfo.getPathological_number());
                    if (numTemp != 0) {
                        commonDao.deleteSpecimenInfo(medicalInfo.getId());
                    }
                    commonDao.insertSpecimenInfo(specimenInfo, medicalInfo.getId());
                    List<HFileInfo> fileInfonew = new ArrayList<>();
                    for (HFileInfo fileInfo : fileInfos) {
                        if (fileInfo.getFileName() != null && !fileInfo.getFileName().equals("") && fileInfo.getFilePath() != null && !fileInfo.getFilePath().equals("")) {
                            String filePath = fileInfo.getFilePath();
                            File fileTemp = new File(filePath);
                            fileInfo.setFilePath(File.separator + fileTemp.getParentFile().getName() + File.separator + fileTemp.getName());
                            String dataFilePath = imageUploadPath + fileInfo.getFilePath();
                            File fileTemp2 = new File(dataFilePath);
                            if (fileTemp2.exists()) {
                                System.out.println(fileTemp2.length());
                                fileInfo.setFilesize(fileTemp2.length() / 1024);
                            }
                            fileInfo.setPathNo(medicalInfo.getPathNo());
                            fileInfonew.add(fileInfo);
                        }
                    }
                    if (fileInfonew.size() > 0) {
                        commonDao.insertFileInfo(fileInfonew);
                    }
                }
            }

            result.setData(medicalInfo);
            pathNo11 = medicalInfo.getPathNo();

            //插入日志数据库
            //插入日志
            medicalJson = new MedicalJson(medicalInfo.getPathNo(), updateDateTime, ShenkangUtils.getTime(), JSON.toJSONString(medicalInfo), 0, 0);

            result.setCode(200);
        } catch (Exception e) {
            medicalJson = new MedicalJson(pathNo11, updateDateTime, ShenkangUtils.getTime(), e.getMessage(), 0, 1);
            e.printStackTrace();
            result.setCode(500);
            result.setException(e);
        } finally {
            medicalSimpleInfoService.insertMedicalJson(medicalJson);
        }
        return result;
    }

    /**
     * @author:
     * @param:
     * @return:
     * @Description:加密患者信息，用于推送万达数据
     */
    public static PatientInfo encryptInfo02(PatientInfo patientInfo) {

        if (patientInfo.getName() != null && !"".equals(patientInfo.getName())) {
            patientInfo.setName(DesUtil.encrypt(patientInfo.getName()));
        }
        if (patientInfo.getName_spell() != null && !"".equals(patientInfo.getName_spell())) {
            patientInfo.setName_spell(DesUtil.encrypt(patientInfo.getName_spell()));
        }
        if (patientInfo.getMother_name() != null && !"".equals(patientInfo.getMother_name())) {
            patientInfo.setMother_name(DesUtil.encrypt(patientInfo.getMother_name()));
        }
        if (patientInfo.getSex() != null && !"".equals(patientInfo.getSex())) {
            switch (patientInfo.getSex()) {
                case "1":
                    patientInfo.setSex(DesUtil.encrypt("1"));
                    break;
                case "2":
                    patientInfo.setSex(DesUtil.encrypt("2"));
                    break;
                default:
                    patientInfo.setSex(DesUtil.encrypt("0"));
                    break;
            }
        } else {
            patientInfo.setSex(DesUtil.encrypt("0"));
        }

        if (patientInfo.getHealth_card_no() != null && !"".equals(patientInfo.getHealth_card_no())) {
            patientInfo.setHealth_card_no(DesUtil.encrypt(patientInfo.getHealth_card_no()));
        }
        if (patientInfo.getContact_phone_no() != null && !"".equals(patientInfo.getContact_phone_no())) {
            patientInfo.setContact_phone_no(DesUtil.encrypt(patientInfo.getContact_phone_no()));
        }
        if (patientInfo.getAddress_detail() != null && !"".equals(patientInfo.getAddress_detail())) {
            patientInfo.setAddress_detail(DesUtil.encrypt(patientInfo.getAddress_detail()));
        }
        if (patientInfo.getWork_unit() != null && !"".equals(patientInfo.getWork_unit())) {
            patientInfo.setWork_unit(DesUtil.encrypt(patientInfo.getWork_unit()));
        }

        if (patientInfo.getInsurance_id() != null && !"".equals(patientInfo.getInsurance_id())) {
            patientInfo.setInsurance_id(DesUtil.encrypt(patientInfo.getInsurance_id()));
        }

        if (patientInfo.getId_card_no() != null && !"".equals(patientInfo.getId_card_no())) {
            patientInfo.setIdentityID(patientInfo.getId_card_no());
        }
        if (patientInfo.getIdentityID() != null && !"".equals(patientInfo.getIdentityID())) {
            patientInfo.setIdentityID(DesUtil.encrypt(patientInfo.getIdentityID()));
            patientInfo.setId_card_no(patientInfo.getIdentityID());
        }

        return patientInfo;
    }
}
