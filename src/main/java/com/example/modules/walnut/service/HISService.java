package com.example.modules.walnut.service;

import cn.hutool.core.lang.UUID;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.modules.walnut.mapper.MasterMapper;
import com.example.modules.walnut.domain.*;
import com.example.modules.walnut.domain.HIS.*;

import com.example.modules.walnut.model.WalnutLog;
import com.example.modules.wnhis.pojo.SysUser;
import com.example.utils.DateUtils;
import com.example.utils.MD5util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @author 作者 : 小布
 * @version 创建时间 : 2019年7月29日 上午11:18:30
 * @explain 类说明 :  HIS 相关接口
 */
@Service
public class HISService {

    private Logger logger = LoggerFactory.getLogger(HISService.class);

    @Autowired
    private MasterMapper masterMapper;
    @Autowired
    private WMecWebservice mecWebservice;
    @Autowired
    private WWebServiceUtil webServiceUtil;
    @Autowired
    private WalnutLogService walnutLogService;

    /**
     * 提取his
     *
     * @param param
     * @return
     */
    @SuppressWarnings({"unchecked"})
    @Transactional
    public Map<String, Object> getHISWebServiceData(Map<String, Object> param) {
        Map<String, Object> result = new ConcurrentHashMap<>(10000);
        Long now = DateUtils.currentTimestampTime();
        String bno = "C" + MD5util.string2MD5(String.valueOf(now) + cn.hutool.core.lang.UUID.nameUUIDFromBytes(UUID.randomUUID().toString().getBytes()));
        try {
            Integer userId=(Integer) param.get("userId");
            SysUser user = masterMapper.selUserInfoById(userId);
            List<PatientInfoApply> patientInfoList = new ArrayList<PatientInfoApply>(10000);
            List<String> applyNoList = new ArrayList<String>(10000);
            List<Charge> chargeList = new ArrayList<Charge>(10000);
            PatientInfoApply patientInfo = new PatientInfoApply();
            String code;
            Integer patientType = (Integer) param.get("patientType"), brlb = null;
            String hisId = (String) param.get("hisId");
            String applyNo = (String) param.get("applyNo");
            if (((hisId != null && hisId != "") || (applyNo != null && applyNo != "")) && patientType != null) {
                if (patientType == 501) {//住院
                    brlb = 1;
                } else if (patientType == 502) {//门诊
                    brlb = 0;
                } else if (patientType == 503) {//体检
                    brlb = 3;
                }
                // 体检的单独走体检接口
                if (brlb == 3) {
                    String method = "TJ_GetPatInfo_NEW";
                    Integer codeType = 1; //1：体检编号 2：卡号
                    Map<String, Object> mecPatient;
                    Map<String, Object> map; //返回的患者基础信息

                    String sendBody = getSendBody(method, codeType, hisId);
                    mecPatient = mecWebservice.getMecWebService(sendBody, method,bno,hisId);
                    map = (Map<String, Object>) mecPatient.get("PersonInfo");
                    if (map.get("Column1") != null) {
                        codeType = 2;
                        sendBody = getSendBody(method, codeType, hisId);
                        mecPatient = mecWebservice.getMecWebService(sendBody, method,bno,hisId);
                        map = (Map<String, Object>) mecPatient.get("PersonInfo");
                    }
                    // 查不到该患者
                    if (map.get("Column1") != null && map.get("Column1").equals("F")) {
                        result.put("msg", 1049);
                        return result;
                    }

                    if (map != null) {
                        patientInfo.setHisId(hisId);
                        patientInfo.setPatNo(hisId);
                        patientInfo.setPatientType(503);
                        patientInfo.setCallHis(1);
                        patientInfo.setPatientTypeName("体检");
                        patientInfo.setPatName((String) map.get("PatName"));
                        patientInfo.setPatSex((String) map.get("Sex"));
                        if (map.get("Age") != null) {
                            patientInfo.setPatAge(Integer.parseInt((String) map.get("Age")));
                        }
                        patientInfo.setPatAgeUnit((String) map.get("AgeUnit"));
                        patientInfo.setCardNo((String) map.get("CardNo"));
                        patientInfo.setPatTel((String) map.get("Phone"));
                        patientInfo.setPatAddress((String) map.get("Address"));
                        patientInfo.setNation((String) map.get("Nation"));
                        patientInfo.setQfyy((String) map.get("Qfyy"));
                        patientInfo.setPatIdcard((String) map.get("IDCardNo"));
                        patientInfo.setChargeType((String) map.get("ChargeType"));
                        if (map.get("ApplyDept") != null) {
                            patientInfo.setInspectOffice(((String) map.get("ApplyDept")).trim());
                        }

                        patientInfo.setInspectDoctor((String) map.get("ApplyDoctor"));
                        //根据送检科室为病区赋默认值
                        if (map.get("ApplyDept") != null) {
                            Map<String, Object> mp = getInspectUnit(brlb, ((String) map.get("ApplyDept")).trim());
                            patientInfo.setInspectUnit((Integer) mp.get("inspectUnit"));
                            patientInfo.setDiseaseDistrictName((String) mp.get("diseaseDistrictName"));
                            patientInfo.setDiseaseDistrict((String) mp.get("diseaseDistrict"));
                        }


                        String items = "";
                        if (map.get("Item_0") != null) {
                            items = JSONArray.toJSONString(map.get("Items_0"));
                        }
                        if (!items.equals("")) {
                            JSONObject jsonObject = JSONObject.parseObject(items);
                            Map chargeItems = jsonObject;
                            if (chargeItems != null && chargeItems.size() > 0) {
                                for (int i = 0; i < chargeItems.size(); i++) {
                                    String str = JSONArray.toJSONString(chargeItems.get("Item_" + i));
                                    MECCharge mecCharge = JSON.parseObject(str, MECCharge.class);
                                    applyNo = mecCharge.getLogNo();
                                    patientInfo.setApplyNo((mecCharge.getLogNo()));
                                    Charge charge = new Charge();
                                    charge.setHisId(hisId);
                                    charge.setPatientType(503);
                                    charge.setPatNo(hisId);
                                    charge.setRequireNo(applyNo);
                                    charge.setApplyTime(mecCharge.getApplyTime());//请求日期
                                    charge.setRequireNoItem(String.valueOf(i));
                                    charge.setRequireType(mecCharge.getCustomType());
                                    charge.setItemCode(mecCharge.getItemCode());
                                    charge.setItemName(mecCharge.getItemName());
                                    charge.setItemPrice(mecCharge.getPrice());
                                    charge.setItemQty((Integer.valueOf(mecCharge.getItemQty())));
                                    charge.setItemUnit(mecCharge.getItemUnit());
                                    charge.setChargeFlag(Integer.valueOf(mecCharge.getChargeFlag()));
                                    charge.setStatusRemark(mecCharge.getMemo());
                                    charge.setItemStatus("0");
                                    chargeList.add(charge);
                                    patientInfoList.add(patientInfo);
                                }
                            }
                        }
                        patientInfoList.add(patientInfo);
                    } else {
                        result.put("msg", 1049);
                        return result;
                    }
                } else {
                    if ((applyNo == null || applyNo == "") && hisId != null && patientType != null) {
                        JB01 jb01 = getJB01(hisId, brlb,bno);
                        if (jb01 != null) {
                            int jb01notnull = 0;
                            if (jb01.getColumn1() != "F" && !"F".equals(jb01.getColumn1())) {
                                List<JB03> jb03List = getJB03(jb01, brlb,bno,hisId);
                                String[] myApplyNoInMysql = masterMapper.queryMyApplyNoInMysql(patientType, hisId);
                                int count1 = 0, count2 = 0;
                                if (myApplyNoInMysql != null) {
                                    count1 = myApplyNoInMysql.length;
                                }
                                if (jb03List != null) {
                                    JB03 jb03;
                                    String stringjb03 = JSONArray.toJSONString(jb03List.get(0));
                                    jb03 = JSON.parseObject(stringjb03, JB03.class);
                                    if (jb03.getColumn1() != "F" && !"F".equals(jb03.getColumn1())) {
                                        int jb03Size = jb03List.size();
                                        if (jb03Size > 0) {
                                            List<SQ03> sq03List;
                                            int sq03size;
                                            List<SQ02> sq02List;
                                            int sq02size;
                                            String stringSQ03, stringSQ02, inspectDoctor = masterMapper.queryInspectDoctor(jb01.getApplyDoctor());
                                            SQ03 sq03;
                                            SQ02 sq02;
                                            for (int z = 0; z < jb03Size; z++) {
                                                stringjb03 = JSONArray.toJSONString(jb03List.get(z));
                                                jb03 = JSON.parseObject(stringjb03, JB03.class);
                                                if (jb03.getItemname() != null && (jb03.getItemcode().startsWith("270") || "病理科".equals(jb03.getQqksmc()) || "病理科(市区)".equals(jb03.getQqksmc()))) {
                                                    Charge charge = new Charge();
                                                    charge.setHisId(jb03.getBlh());
                                                    charge.setPatientType(patientType);
                                                    charge.setPatNo(jb03.getPatid());
                                                    charge.setSyxh(jb03.getSyxh());
                                                    charge.setRequireNo(jb03.getQqxh());
                                                    charge.setApplyTime(jb03.getQqrq());//请求日期
                                                    charge.setRequireNoItem(jb03.getQqmxxh());
                                                    charge.setRequireType(jb03.getQqlx());
                                                    charge.setItemCode(jb03.getItemcode());
                                                    charge.setItemName(jb03.getItemname());
                                                    charge.setItemPrice(jb03.getPrice());
                                                    charge.setItemUnit(jb03.getItemunit());
                                                    charge.setItemQty(jb03.getItemqty());
                                                    charge.setItemType(jb03.getItemtype());
                                                    charge.setItemStatus("0");
                                                    charge.setUrl(jb03.getUrl());
                                                    charge.setBabyNo(jb03.getYexh());
                                                    charge.setSpecimenType(jb03.getBBLX());
                                                    charge.setStatus(jb03.getStatus());
                                                    charge.setChargeFlag(jb03.getChargeFlag());
                                                    charge.setAddType(jb03.getAddType());
                                                    chargeList.add(charge);
                                                }
                                            }
                                            sq03List = getSQ03(jb01, brlb,bno,hisId);
                                            if (sq03List != null) {
                                                stringjb03 = JSONArray.toJSONString(sq03List.get(0));
                                                sq03 = JSON.parseObject(stringjb03, SQ03.class);
                                                if (sq03.getColumn1() != "F" && !"F".equals(sq03.getColumn1())) {
                                                    sq03size = sq03List.size();
                                                    count2 = sq03size;
                                                    if (sq03size > 0) {
                                                        if (sq03size > 10) {
                                                            sq03size = 10;
                                                        }
                                                        for (int i = 0; i < sq03size; i++) {
                                                            stringSQ03 = JSONArray.toJSONString(sq03List.get(i));
                                                            sq03 = JSON.parseObject(stringSQ03, SQ03.class);
                                                            if (myApplyNoInMysql == null) {
                                                                sq02List = getSQ02(sq03, brlb,bno,hisId);
                                                                if (sq02List != null) {
                                                                    sq02size = sq02List.size();
                                                                    if (sq02size > 0) {
                                                                        patientInfo = new PatientInfoApply();
                                                                        patientInfo.setApplyNo(sq03.getSqdxh());//申请单号
                                                                        patientInfo.setPatientType(patientType);//病人类型
                                                                        patientInfo.setHisId(hisId);//hisId
                                                                        patientInfo.setSyxh(jb01.getSyxh());
                                                                        patientInfo.setCallHis(1);
                                                                        patientInfo.setCardNo(jb01.getCardNo());
                                                                        patientInfo.setPatNo(jb01.getPatientID());//patId
                                                                        if (jb01.getAge() != null && jb01.getAge() != "") {
                                                                            patientInfo.setPatAge(Integer.parseInt(jb01.getAge().replaceAll("岁", "")));//年龄
                                                                        }
                                                                        patientInfo.setPatAgeUnit(jb01.getAgeUnit());

                                                                        patientInfo.setPatBirthday(formatDate(jb01.getBirthday()));//生日
                                                                        patientInfo.setMaritalStatus(jb01.getMarriage());//婚否
                                                                        patientInfo.setPatTel(jb01.getPhone());//电话
                                                                        patientInfo.setPatIdcard(jb01.getIDNum());//身份证
                                                                        if (jb01.getCardNo() != null) {
                                                                            patientInfo.setCardNo(jb01.getCardNo());
                                                                        }
                                                                        patientInfo.setPatAddress(jb01.getAddress());//地址
                                                                        patientInfo.setInspectOffice(jb01.getApplyDept());//科室
                                                                        patientInfo.setDiseaseDistrict(jb01.getWard());//病区
                                                                        patientInfo.setBedNo(jb01.getBedNo());//床号
                                                                        patientInfo.setChargeType(jb01.getChargeType());//费别
                                                                        patientInfo.setNation(jb01.getNation());//民族
                                                                        patientInfo.setAcceptDoctor(user.getFullName());
                                                                        patientInfo.setMaterialsDoctor(user.getUserId());
                                                                        if (inspectDoctor != null) {
                                                                            patientInfo.setInspectDoctor(inspectDoctor);//送检医生
                                                                        } else {
                                                                            patientInfo.setInspectDoctor(jb01.getToDocName());//送检医生
                                                                        }
                                                                        applyNoList.add(sq03.getSqdxh());
                                                                        for (int j = 0; j < sq02size; j++) {
                                                                            stringSQ02 = JSONArray.toJSONString(sq02List.get(j));
                                                                            sq02 = JSON.parseObject(stringSQ02, SQ02.class);
                                                                            if (sq02 != null) {
                                                                                if ("病历号".equals(sq02.getCaption())) {
                                                                                    patientInfo.setHisId(sq02.getValue());
                                                                                } else if ("病史资料".equals(sq02.getCaption())) {
                                                                                    if (patientInfo.getClinicalData() != null && patientInfo.getClinicalData() != "" && !patientInfo.getClinicalData().equals(null) && !patientInfo.getClinicalData().equals("")) {
                                                                                        patientInfo.setClinicalData(patientInfo.getClinicalData() + "\n" + sq02.getValue());
                                                                                    } else {
                                                                                        patientInfo.setClinicalData(sq02.getValue());
                                                                                    }
                                                                                } else if ("患者姓名".equals(sq02.getCaption())) {
                                                                                    patientInfo.setPatName(sq02.getValue());
                                                                                } else if ("检查部位：".equals(sq02.getCaption())) {

                                                                                } else if ("检查部位0".equals(sq02.getCaption())) {

                                                                                } else if ("检查部位1".equals(sq02.getCaption())) {

                                                                                } else if ("检查部位2".equals(sq02.getCaption())) {

                                                                                } else if ("检查目的".equals(sq02.getCaption())) {
                                                                                    if (patientInfo.getClinicalData() != null && patientInfo.getClinicalData() != "" && !patientInfo.getClinicalData().equals(null) && !patientInfo.getClinicalData().equals("")) {
                                                                                        patientInfo.setClinicalData(patientInfo.getClinicalData() + "\n" + sq02.getValue());
                                                                                    } else {
                                                                                        patientInfo.setClinicalData(sq02.getValue());
                                                                                    }
                                                                                } else if ("检查项目".equals(sq02.getCaption())) {

                                                                                } else if ("科室".equals(sq02.getCaption())) {
                                                                                    patientInfo.setInspectOffice(sq02.getValue());
                                                                                    if (sq02.getValue() != null && sq02.getValue() != "") {
                                                                                        code = masterMapper.queryInspectUnitByInspectOffice(sq02.getValue());
                                                                                        if (code != null) {
                                                                                            Map<String, Object> mp = getInspectUnit(brlb, code);
                                                                                            patientInfo.setInspectUnit((Integer) mp.get("inspectUnit"));
                                                                                            if (brlb == 0 || brlb == 3) {
                                                                                                patientInfo.setDiseaseDistrictName((String) mp.get("diseaseDistrictName"));
                                                                                                patientInfo.setDiseaseDistrict((String) mp.get("diseaseDistrict"));
                                                                                            }

                                                                                        }
                                                                                    }
                                                                                } else if ("临床诊断".equals(sq02.getCaption())) {
                                                                                    if (sq02.getValue() != null) {
                                                                                        patientInfo.setClinicalDiagnosis(sq02.getValue().replace("\\", ""));
                                                                                    } else {
                                                                                        patientInfo.setClinicalDiagnosis(sq02.getValue());
                                                                                    }

                                                                                } else if ("年龄".equals(sq02.getCaption())) {
                                                                                    patientInfo.setPatAge(Integer.parseInt(sq02.getValue().replaceAll("岁", "")));
                                                                                    patientInfo.setPatAgeUnit(sq02.getValue().replaceAll("[0-9]", ""));
                                                                                } else if ("其他标本种类".equals(sq02.getCaption())) {
                                                                                    patientInfo.setSpecimenName(sq02.getValue());
                                                                                } else if ("条形码".equals(sq02.getCaption())) {

                                                                                } else if ("性别".equals(sq02.getCaption())) {
                                                                                    patientInfo.setPatSex(sq02.getValue());
                                                                                } else if ("执行科室".equals(sq02.getCaption())) {

                                                                                } else if ("执行科室代码".equals(sq02.getCaption())) {

                                                                                } else if ("注意事项".equals(sq02.getCaption())) {
                                                                                    if (patientInfo.getClinicalData() != null && patientInfo.getClinicalData() != "" && !patientInfo.getClinicalData().equals(null) && !patientInfo.getClinicalData().equals("")) {
                                                                                        patientInfo.setClinicalData(patientInfo.getClinicalData() + "\n" + sq02.getValue());
                                                                                    } else {
                                                                                        patientInfo.setClinicalData(sq02.getValue());
                                                                                    }
                                                                                } else if ("申请单序号".equals(sq02.getCaption())) {

                                                                                } else if ("诊断".equals(sq02.getCaption())) {
                                                                                    if (patientInfo.getClinicalDiagnosis() != null) {
                                                                                        patientInfo.setClinicalDiagnosis(patientInfo.getClinicalDiagnosis().replace("\\", "") + "\n" + sq02.getValue());
                                                                                    } else {
                                                                                        patientInfo.setClinicalDiagnosis(patientInfo.getClinicalDiagnosis() + "\n" + sq02.getValue());
                                                                                    }

                                                                                } else if ("临床信息".equals(sq02.getCaption())) {
                                                                                    if (patientInfo.getClinicalData() != null && patientInfo.getClinicalData() != "" && !patientInfo.getClinicalData().equals(null) && !patientInfo.getClinicalData().equals("")) {
                                                                                        patientInfo.setClinicalData(patientInfo.getClinicalData() + "\n" + sq02.getValue());
                                                                                    } else {
                                                                                        patientInfo.setClinicalData(sq02.getValue());
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                        patientInfoList.add(patientInfo);
                                                                    }
                                                                }
                                                            } else {
                                                                if (!Arrays.asList(myApplyNoInMysql).contains(sq03.getSqdxh())) {
                                                                    sq02List = getSQ02(sq03, brlb,bno,hisId);
                                                                    if (sq02List != null) {
                                                                        sq02size = sq02List.size();
                                                                        if (sq02size > 0) {
                                                                            patientInfo = new PatientInfoApply();
                                                                            patientInfo.setApplyNo(sq03.getSqdxh());//申请单号
                                                                            patientInfo.setPatientType(patientType);//病人类型
                                                                            patientInfo.setHisId(hisId);//hisId
                                                                            patientInfo.setSyxh(jb01.getSyxh());
                                                                            patientInfo.setCallHis(1);
                                                                            patientInfo.setPatNo(jb01.getPatientID());//patId
                                                                            if (jb01.getAge() != null && jb01.getAge() != "") {
                                                                                patientInfo.setPatAge(Integer.parseInt(jb01.getAge().replaceAll("岁", "")));//年龄
                                                                            }
                                                                            patientInfo.setPatAgeUnit(jb01.getAgeUnit());
                                                                            patientInfo.setPatBirthday(formatDate(jb01.getBirthday()));//生日
                                                                            patientInfo.setMaritalStatus(jb01.getMarriage());//婚否
                                                                            patientInfo.setPatTel(jb01.getPhone());//电话
                                                                            patientInfo.setPatIdcard(jb01.getIDNum());//身份证
                                                                            patientInfo.setPatAddress(jb01.getAddress());//地址
                                                                            patientInfo.setInspectOffice(jb01.getApplyDept());//科室
                                                                            patientInfo.setDiseaseDistrict(jb01.getWard());//病区
                                                                            patientInfo.setBedNo(jb01.getBedNo());//床号
                                                                            patientInfo.setChargeType(jb01.getChargeType());//费别
                                                                            patientInfo.setNation(jb01.getNation());//民族
                                                                            patientInfo.setAcceptDoctor(user.getFullName());
                                                                            patientInfo.setMaterialsDoctor(user.getUserId());
                                                                            if (inspectDoctor != null) {
                                                                                patientInfo.setInspectDoctor(inspectDoctor);//送检医生
                                                                            } else {
                                                                                patientInfo.setInspectDoctor(jb01.getToDocName());//送检医生
                                                                            }
                                                                            if (jb01.getCardNo() != null) {
                                                                                patientInfo.setCardNo(jb01.getCardNo());
                                                                            }
                                                                            applyNoList.add(sq03.getSqdxh());
                                                                            for (int j = 0; j < sq02size; j++) {
                                                                                stringSQ02 = JSONArray.toJSONString(sq02List.get(j));
                                                                                sq02 = JSON.parseObject(stringSQ02, SQ02.class);
                                                                                if (sq02 != null) {
                                                                                    if ("病历号".equals(sq02.getCaption())) {
                                                                                        patientInfo.setHisId(sq02.getValue());
                                                                                    } else if ("病史资料".equals(sq02.getCaption())) {
                                                                                        if (patientInfo.getClinicalData() != null && patientInfo.getClinicalData() != "" && !patientInfo.getClinicalData().equals(null) && !patientInfo.getClinicalData().equals("")) {
                                                                                            patientInfo.setClinicalData(patientInfo.getClinicalData() + "\n" + sq02.getValue());
                                                                                        } else {
                                                                                            patientInfo.setClinicalData(sq02.getValue());
                                                                                        }
                                                                                    } else if ("患者姓名".equals(sq02.getCaption())) {
                                                                                        patientInfo.setPatName(sq02.getValue());
                                                                                    } else if ("检查部位：".equals(sq02.getCaption())) {

                                                                                    } else if ("检查部位0".equals(sq02.getCaption())) {

                                                                                    } else if ("检查部位1".equals(sq02.getCaption())) {

                                                                                    } else if ("检查部位2".equals(sq02.getCaption())) {

                                                                                    } else if ("检查目的".equals(sq02.getCaption())) {
                                                                                        if (patientInfo.getClinicalData() != null && patientInfo.getClinicalData() != "" && !patientInfo.getClinicalData().equals(null) && !patientInfo.getClinicalData().equals("")) {
                                                                                            patientInfo.setClinicalData(patientInfo.getClinicalData() + "\n" + sq02.getValue());
                                                                                        } else {
                                                                                            patientInfo.setClinicalData(sq02.getValue());
                                                                                        }
                                                                                    } else if ("检查项目".equals(sq02.getCaption())) {

                                                                                    } else if ("科室".equals(sq02.getCaption())) {
                                                                                        patientInfo.setInspectOffice(sq02.getValue());
                                                                                        if (sq02.getValue() != null && sq02.getValue() != "") {
                                                                                            code = masterMapper.queryInspectUnitByInspectOffice(sq02.getValue());
                                                                                            if (code != null) {
                                                                                                Map<String, Object> mp = getInspectUnit(brlb, code);
                                                                                                patientInfo.setInspectUnit((Integer) mp.get("inspectUnit"));
                                                                                                if (brlb == 0 || brlb == 3) {
                                                                                                    patientInfo.setDiseaseDistrictName((String) mp.get("diseaseDistrictName"));
                                                                                                    patientInfo.setDiseaseDistrict((String) mp.get("diseaseDistrict"));
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                    } else if ("临床诊断".equals(sq02.getCaption())) {
                                                                                        if (sq02.getValue() != null) {
                                                                                            patientInfo.setClinicalDiagnosis(sq02.getValue().replace("\\", ""));
                                                                                        } else {
                                                                                            patientInfo.setClinicalDiagnosis(sq02.getValue().replace("\\", ""));
                                                                                        }
//                                                                                    patientInfo.setClinicalDiagnosis(sq02.getValue().replace("\\",""));
                                                                                    } else if ("年龄".equals(sq02.getCaption())) {
                                                                                        patientInfo.setPatAge(Integer.parseInt(sq02.getValue().replaceAll("岁", "")));
                                                                                        patientInfo.setPatAgeUnit(sq02.getValue().replaceAll("[0-9]", ""));
                                                                                    } else if ("其他标本种类".equals(sq02.getCaption())) {
                                                                                        patientInfo.setSpecimenName(sq02.getValue());
                                                                                    } else if ("条形码".equals(sq02.getCaption())) {

                                                                                    } else if ("性别".equals(sq02.getCaption())) {
                                                                                        patientInfo.setPatSex(sq02.getValue());
                                                                                    } else if ("执行科室".equals(sq02.getCaption())) {

                                                                                    } else if ("执行科室代码".equals(sq02.getCaption())) {

                                                                                    } else if ("注意事项".equals(sq02.getCaption())) {
                                                                                        if (patientInfo.getClinicalData() != null && patientInfo.getClinicalData() != "" && !patientInfo.getClinicalData().equals(null) && !patientInfo.getClinicalData().equals("")) {
                                                                                            patientInfo.setClinicalData(patientInfo.getClinicalData() + "\n" + sq02.getValue());
                                                                                        } else {
                                                                                            patientInfo.setClinicalData(sq02.getValue());
                                                                                        }
                                                                                    } else if ("申请单序号".equals(sq02.getCaption())) {

                                                                                    } else if ("诊断".equals(sq02.getCaption())) {
                                                                                        if (patientInfo.getClinicalDiagnosis() != null) {
                                                                                            patientInfo.setClinicalDiagnosis(patientInfo.getClinicalDiagnosis().replace("\\", "") + "\n" + sq02.getValue());
                                                                                        } else {
                                                                                            patientInfo.setClinicalDiagnosis(patientInfo.getClinicalDiagnosis() + "\n" + sq02.getValue());
                                                                                        }

                                                                                    } else if ("临床信息".equals(sq02.getCaption())) {
                                                                                        if (patientInfo.getClinicalData() != null && patientInfo.getClinicalData() != "" && !patientInfo.getClinicalData().equals(null) && !patientInfo.getClinicalData().equals("")) {
                                                                                            patientInfo.setClinicalData(patientInfo.getClinicalData() + "\n" + sq02.getValue());
                                                                                        } else {
                                                                                            patientInfo.setClinicalData(sq02.getValue());
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }
                                                                            patientInfoList.add(patientInfo);
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                } else {
                                                    jb01notnull = 1;
                                                }
                                            } else {
                                                jb01notnull = 1;
                                            }
                                        }
                                    } else {
                                        jb01notnull = 1;
                                    }
                                } else {
                                    jb01notnull = 1;
                                }
                                if (count1 == count2 && count1 > 0) {
                                    jb01notnull = 1;
                                }
                            } else {
                                result.put("msg", 1071);
                                return result;
                            }
                            if (jb01notnull == 1) {
                                String inspectDoctor = masterMapper.queryInspectDoctor(jb01.getApplyDoctor());
                                patientInfo.setPatientType(patientType);//病人类型
                                patientInfo.setHisId(hisId);//hisId
                                patientInfo.setSyxh(jb01.getSyxh());
                                patientInfo.setCallHis(0);
                                //patientInfo.setApplyNo(jb01.getSyxh());//申请单
                                patientInfo.setPatName(jb01.getPatName());//名称
                                if (jb01.getSex() != null) {
                                    patientInfo.setPatSex((jb01.getSex() == 2 || jb01.getSex() + "" == "女") ? "女" : "男");//性别
                                }
//                            patientInfo.setPatNo(jb01.getCureNo());//patId
                                patientInfo.setPatNo(jb01.getPatientID());//patId
                                if (jb01.getAge() != null && jb01.getAge() != "") {
                                    patientInfo.setPatAge(Integer.parseInt(jb01.getAge().replaceAll("岁", "")));//年龄
                                }
                                patientInfo.setPatAgeUnit(jb01.getAgeUnit());
                                patientInfo.setPatBirthday(formatDate(jb01.getBirthday()));//生日
                                patientInfo.setMaritalStatus(jb01.getMarriage());//婚否
                                patientInfo.setPatTel(jb01.getPhone());//电话
                                patientInfo.setPatIdcard(jb01.getIDNum());//身份证
                                patientInfo.setPatAddress(jb01.getAddress());//地址
                                if (jb01.getCardNo() != null) {
                                    patientInfo.setCardNo(jb01.getCardNo());
                                }
                                //patientInfo.setInspectOffice(jb01.getApplyDept());//科室
                                patientInfo.setDiseaseDistrict(jb01.getWard());//病区
                                patientInfo.setBedNo(jb01.getBedNo());//床号
                                patientInfo.setChargeType(jb01.getChargeType());//费别
                                patientInfo.setNation(jb01.getNation());//民族
                                if (jb01.getZdmc() != null) {
                                    patientInfo.setClinicalDiagnosis(jb01.getZdmc().replace("\\", ""));//临床诊断
                                } else {
                                    patientInfo.setClinicalDiagnosis(jb01.getZdmc());//临床诊断
                                }
                                patientInfo.setInspectDoctor(inspectDoctor);//送检医生
                                patientInfo.setInspectOffice(jb01.getApplyDept());
                                patientInfo.setAcceptDoctor(user.getFullName());
                                patientInfo.setMaterialsDoctor(user.getUserId());
                                if (jb01.getApplyDept() != null && jb01.getApplyDept() != "") {
                                    code = masterMapper.queryInspectUnitByInspectOffice(jb01.getApplyDept());
                                    if (code != null) {
                                        Map<String, Object> mp = getInspectUnit(brlb, code);
                                        patientInfo.setInspectUnit((Integer) mp.get("inspectUnit"));
                                        if (brlb == 0 || brlb == 3) {
                                            patientInfo.setDiseaseDistrictName((String) mp.get("diseaseDistrictName"));
                                            patientInfo.setDiseaseDistrict((String) mp.get("diseaseDistrict"));
                                        }
                                    }
                                }
                                patientInfoList.add(patientInfo);
                            }
                        }
                    } else if (applyNo != null && applyNo != "" && patientType != null) {
                        List<SQ02> list = getSQ02(applyNo, brlb,bno,hisId);
                        if (list != null) {
                            int size = list.size();
                            String stringSQ02;
                            SQ02 sq02;
                            for (int i = 0; i < size; i++) {
                                stringSQ02 = JSONArray.toJSONString(list.get(i));
                                sq02 = JSON.parseObject(stringSQ02, SQ02.class);
                                if (sq02.getColumn1() != null && sq02.getColumn1().equals("F")) {
                                    result.put("msg", 1071);
                                    return result;
                                }
                                patientInfo.setApplyNo(applyNo);//申请单号
                                patientInfo.setPatientType(patientType);
                                patientInfo.setCallHis(1);
                                patientInfo.setAcceptDoctor(user.getFullName());
                                patientInfo.setAcceptDoctor(user.getFullName());
                                patientInfo.setMaterialsDoctor(user.getUserId());

                                if (sq02 != null) {
                                    if ("病历号".equals(sq02.getCaption())) {
                                        patientInfo.setHisId(sq02.getValue());
                                    } else if ("病史资料".equals(sq02.getCaption())) {
                                        if (patientInfo.getClinicalData() != null && patientInfo.getClinicalData() != "" && !patientInfo.getClinicalData().equals(null) && !patientInfo.getClinicalData().equals("")) {
                                            patientInfo.setClinicalData(patientInfo.getClinicalData() + "\n" + sq02.getValue());
                                        } else {
                                            patientInfo.setClinicalData(sq02.getValue());
                                        }
                                    } else if ("患者姓名".equals(sq02.getCaption())) {
                                        patientInfo.setPatName(sq02.getValue());
                                    } else if ("检查部位：".equals(sq02.getCaption())) {

                                    } else if ("检查部位0".equals(sq02.getCaption())) {

                                    } else if ("检查部位1".equals(sq02.getCaption())) {

                                    } else if ("检查部位2".equals(sq02.getCaption())) {

                                    } else if ("检查目的".equals(sq02.getCaption())) {
                                        if (patientInfo.getClinicalData() != null && patientInfo.getClinicalData() != "" && !patientInfo.getClinicalData().equals(null) && !patientInfo.getClinicalData().equals("")) {
                                            patientInfo.setClinicalData(patientInfo.getClinicalData() + "\n" + sq02.getValue());
                                        } else {
                                            patientInfo.setClinicalData(sq02.getValue());
                                        }
                                    } else if ("检查项目".equals(sq02.getCaption())) {

                                    } else if ("科室".equals(sq02.getCaption())) {
                                        patientInfo.setInspectOffice(sq02.getValue());
                                        if (sq02.getValue() != null && sq02.getValue() != "") {
                                            code = masterMapper.queryInspectUnitByInspectOffice(sq02.getValue());
                                            if (code != null) {
                                                Map<String, Object> mp = getInspectUnit(brlb, code);
                                                patientInfo.setInspectUnit((Integer) mp.get("inspectUnit"));
                                                if (brlb == 0 || brlb == 3) {
                                                    patientInfo.setDiseaseDistrictName((String) mp.get("diseaseDistrictName"));
                                                    patientInfo.setDiseaseDistrict((String) mp.get("diseaseDistrict"));
                                                }
                                            }
                                        }
                                    } else if ("临床诊断".equals(sq02.getCaption())) {
                                        if (sq02.getValue() != null) {
                                            patientInfo.setClinicalDiagnosis(sq02.getValue().replace("\\", ""));
                                        } else {
                                            patientInfo.setClinicalDiagnosis(sq02.getValue());
                                        }

                                    } else if ("年龄".equals(sq02.getCaption())) {
                                        patientInfo.setPatAge(Integer.parseInt(sq02.getValue().replaceAll("岁", "")));
                                        patientInfo.setPatAgeUnit(sq02.getValue().replaceAll("[0-9]", ""));
                                    } else if ("其他标本种类".equals(sq02.getCaption())) {
                                        patientInfo.setSpecimenName(sq02.getValue());
                                    } else if ("条形码".equals(sq02.getCaption())) {

                                    } else if ("性别".equals(sq02.getCaption())) {
                                        patientInfo.setPatSex(sq02.getValue());
                                    } else if ("执行科室".equals(sq02.getCaption())) {

                                    } else if ("执行科室代码".equals(sq02.getCaption())) {

                                    } else if ("注意事项".equals(sq02.getCaption())) {
                                        if (patientInfo.getClinicalData() != null && patientInfo.getClinicalData() != "" && !patientInfo.getClinicalData().equals(null) && !patientInfo.getClinicalData().equals("")) {
                                            patientInfo.setClinicalData(patientInfo.getClinicalData() + "\n" + sq02.getValue());
                                        } else {
                                            patientInfo.setClinicalData(sq02.getValue());
                                        }
                                    } else if ("申请单序号".equals(sq02.getCaption())) {

                                    } else if ("诊断".equals(sq02.getCaption())) {
                                        if (patientInfo.getClinicalDiagnosis() != null) {
                                            patientInfo.setClinicalDiagnosis(patientInfo.getClinicalDiagnosis().replace("\\", "") + "\n" + sq02.getValue());
                                        } else {
                                            patientInfo.setClinicalDiagnosis(patientInfo.getClinicalDiagnosis() + "\n" + sq02.getValue());
                                        }

                                    } else if ("临床信息".equals(sq02.getCaption())) {
                                        if (patientInfo.getClinicalData() != null && patientInfo.getClinicalData() != "" && !patientInfo.getClinicalData().equals(null) && !patientInfo.getClinicalData().equals("")) {
                                            patientInfo.setClinicalData(patientInfo.getClinicalData() + "\n" + sq02.getValue());
                                        } else {
                                            patientInfo.setClinicalData(sq02.getValue());
                                        }
                                    }
                                }
                            }
                            hisId = patientInfo.getHisId();
                            //根据brlb 和 his 反查数据 保证数据完整
                            JB01 jb01 = getJB01(hisId, brlb,bno);
                            if (jb01 != null) {
                                if (jb01.getColumn1() != "F" && !"F".equals(jb01.getColumn1())) {
                                    List<JB03> jb03List = getJB03(jb01, brlb,bno,hisId);
                                    String[] myApplyNoInMysql = masterMapper.queryMyApplyNoInMysql(patientType, hisId);
                                    int count1 = 0, count2 = 0;
                                    if (myApplyNoInMysql != null) {
                                        count1 = myApplyNoInMysql.length;
                                    }
                                    if (jb03List != null) {
                                        JB03 jb03;
                                        String stringjb03 = JSONArray.toJSONString(jb03List.get(0));
                                        jb03 = JSON.parseObject(stringjb03, JB03.class);
                                        if (jb03.getColumn1() != "F" && !"F".equals(jb03.getColumn1())) {
                                            int jb03Size = jb03List.size();
                                            if (jb03Size > 0) {
                                                List<SQ03> sq03List;
                                                int sq03size;
                                                List<SQ02> sq02List;
                                                int sq02size;
                                                String stringSQ03, stringSQ02a, inspectDoctor = masterMapper.queryInspectDoctor(jb01.getApplyDoctor());
                                                SQ03 sq03;
                                                SQ02 sq02a;
                                                for (int z = 0; z < jb03Size; z++) {
                                                    stringjb03 = JSONArray.toJSONString(jb03List.get(z));
                                                    jb03 = JSON.parseObject(stringjb03, JB03.class);
                                                    if (jb03.getItemname() != null && (jb03.getItemcode().startsWith("270") || "病理科".equals(jb03.getQqksmc()))) {
                                                        Charge charge = new Charge();
                                                        charge.setHisId(jb03.getBlh());
                                                        charge.setPatientType(patientType);
                                                        charge.setPatNo(jb03.getPatid());
                                                        charge.setSyxh(jb03.getSyxh());
                                                        charge.setRequireNo(jb03.getQqxh());
                                                        charge.setApplyTime(jb03.getQqrq());//请求日期
                                                        charge.setRequireNoItem(jb03.getQqmxxh());
                                                        charge.setRequireType(jb03.getQqlx());
                                                        //charge.setApplyDept(jb03.get());//申请科室
                                                        //charge.setApplyDoc(jb03.get());//申请医生
                                                        //charge.setApplyDocCode(jb03.get());//申请科室
                                                        //charge.setApplyDeptCode(jb03.get());//申请医生
                                                        //charge.setApplyTime(jb03.get());//申请时间
                                                        charge.setItemCode(jb03.getItemcode());
                                                        charge.setItemName(jb03.getItemname());
                                                        charge.setItemPrice(jb03.getPrice());
                                                        charge.setItemUnit(jb03.getItemunit());
                                                        charge.setItemQty(jb03.getItemqty());
                                                        charge.setItemType(jb03.getItemtype());
                                                        charge.setItemStatus("0");//项目状态
                                                        //charge.setStatusRemark(jb03.get());//His返回信息
                                                        charge.setUrl(jb03.getUrl());
                                                        //charge.setIsEmergency(jb03.get());//急诊标志
                                                        charge.setBabyNo(jb03.getYexh());
                                                        charge.setSpecimenType(jb03.getBBLX());
                                                        charge.setStatus(jb03.getStatus());
                                                        charge.setChargeFlag(jb03.getChargeFlag());
                                                        charge.setAddType(jb03.getAddType());
                                                        chargeList.add(charge);
                                                    }
                                                }
                                                sq03List = getSQ03(jb01, brlb,bno,hisId);
                                                if (sq03List != null) {
                                                    stringjb03 = JSONArray.toJSONString(sq03List.get(0));
                                                    sq03 = JSON.parseObject(stringjb03, SQ03.class);
                                                    if (sq03.getColumn1() != "F" && !"F".equals(sq03.getColumn1())) {
                                                        sq03size = sq03List.size();
                                                        count2 = sq03size;
                                                        if (sq03size > 0) {
                                                            if (sq03size > 10) {
                                                                sq03size = 10;
                                                            }
                                                            for (int n = 0; n < sq03size; n++) {
                                                                stringSQ03 = JSONArray.toJSONString(sq03List.get(n));
                                                                sq03 = JSON.parseObject(stringSQ03, SQ03.class);
                                                                if (myApplyNoInMysql == null) {
                                                                    sq02List = getSQ02(sq03, brlb,bno,hisId);
                                                                    if (sq02List != null) {
                                                                        sq02size = sq02List.size();
                                                                        if (sq02size > 0) {
                                                                            patientInfo = new PatientInfoApply();
                                                                            patientInfo.setApplyNo(sq03.getSqdxh());//申请单号
                                                                            patientInfo.setPatientType(patientType);//病人类型
                                                                            patientInfo.setHisId(hisId);//hisId
                                                                            patientInfo.setSyxh(jb01.getSyxh());
                                                                            patientInfo.setCallHis(1);
                                                                            //patientInfo.setPatNo(jb01.getCureNo());//patId
                                                                            patientInfo.setPatNo(jb01.getPatientID());//patId
                                                                            if (jb01.getAge() != null && jb01.getAge() != "") {
                                                                                patientInfo.setPatAge(Integer.parseInt(jb01.getAge().replaceAll("岁", "")));//年龄
                                                                            }
                                                                            patientInfo.setPatAgeUnit(jb01.getAgeUnit());
                                                                            patientInfo.setPatBirthday(formatDate(jb01.getBirthday()));//生日
                                                                            patientInfo.setMaritalStatus(jb01.getMarriage());//婚否
                                                                            patientInfo.setPatTel(jb01.getPhone());//电话
                                                                            patientInfo.setPatIdcard(jb01.getIDNum());//身份证
                                                                            patientInfo.setPatAddress(jb01.getAddress());//地址
                                                                            patientInfo.setInspectOffice(jb01.getApplyDept());//科室
                                                                            patientInfo.setDiseaseDistrict(jb01.getWard());//病区
                                                                            patientInfo.setBedNo(jb01.getBedNo());//床号
                                                                            patientInfo.setChargeType(jb01.getChargeType());//费别
                                                                            patientInfo.setNation(jb01.getNation());//民族
                                                                            patientInfo.setAcceptDoctor(user.getFullName());
                                                                            patientInfo.setMaterialsDoctor(user.getUserId());
                                                                            if (inspectDoctor != null) {
                                                                                patientInfo.setInspectDoctor(inspectDoctor);//送检医生
                                                                            } else {
                                                                                patientInfo.setInspectDoctor(jb01.getToDocName());//送检医生
                                                                            }
                                                                            applyNoList.add(sq03.getSqdxh());
                                                                            for (int j = 0; j < sq02size; j++) {
                                                                                stringSQ02a = JSONArray.toJSONString(sq02List.get(j));
                                                                                sq02a = JSON.parseObject(stringSQ02a, SQ02.class);
                                                                                if (sq02a != null) {
                                                                                    if ("病历号".equals(sq02a.getCaption())) {
                                                                                        patientInfo.setHisId(sq02a.getValue());
                                                                                    } else if ("病史资料".equals(sq02a.getCaption())) {
                                                                                        if (patientInfo.getClinicalData() != null && patientInfo.getClinicalData() != "" && !patientInfo.getClinicalData().equals(null) && !patientInfo.getClinicalData().equals("")) {
                                                                                            patientInfo.setClinicalData(patientInfo.getClinicalData() + "\n" + sq02a.getValue());
                                                                                        } else {
                                                                                            patientInfo.setClinicalData(sq02a.getValue());
                                                                                        }
                                                                                    } else if ("患者姓名".equals(sq02a.getCaption())) {
                                                                                        patientInfo.setPatName(sq02a.getValue());
                                                                                    } else if ("检查部位：".equals(sq02a.getCaption())) {

                                                                                    } else if ("检查部位0".equals(sq02a.getCaption())) {

                                                                                    } else if ("检查部位1".equals(sq02a.getCaption())) {

                                                                                    } else if ("检查部位2".equals(sq02a.getCaption())) {

                                                                                    } else if ("检查目的".equals(sq02a.getCaption())) {
                                                                                        if (patientInfo.getClinicalData() != null && patientInfo.getClinicalData() != "" && !patientInfo.getClinicalData().equals(null) && !patientInfo.getClinicalData().equals("")) {
                                                                                            patientInfo.setClinicalData(patientInfo.getClinicalData() + "\n" + sq02a.getValue());
                                                                                        } else {
                                                                                            patientInfo.setClinicalData(sq02a.getValue());
                                                                                        }
                                                                                    } else if ("检查项目".equals(sq02a.getCaption())) {

                                                                                    } else if ("科室".equals(sq02a.getCaption())) {
                                                                                        patientInfo.setInspectOffice(sq02a.getValue());
                                                                                        if (sq02a.getValue() != null && sq02a.getValue() != "") {
                                                                                            code = masterMapper.queryInspectUnitByInspectOffice(sq02a.getValue());
                                                                                            if (code != null) {
                                                                                                Map<String, Object> mp = getInspectUnit(brlb, code);
                                                                                                patientInfo.setInspectUnit((Integer) mp.get("inspectUnit"));
                                                                                                if (brlb == 0 || brlb == 3) {
                                                                                                    patientInfo.setDiseaseDistrictName((String) mp.get("diseaseDistrictName"));
                                                                                                    patientInfo.setDiseaseDistrict((String) mp.get("diseaseDistrict"));
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                    } else if ("临床诊断".equals(sq02a.getCaption())) {
                                                                                        if (sq02a.getValue() != null) {
                                                                                            patientInfo.setClinicalDiagnosis(sq02a.getValue().replace("\\", ""));
                                                                                        } else {
                                                                                            patientInfo.setClinicalDiagnosis(sq02a.getValue());
                                                                                        }
                                                                                    } else if ("年龄".equals(sq02a.getCaption())) {
                                                                                        patientInfo.setPatAge(Integer.parseInt(sq02a.getValue().replaceAll("岁", "")));
                                                                                        patientInfo.setPatAgeUnit(String.valueOf(sq02a.getValue().replaceAll("[0-9]", "")));
                                                                                    } else if ("其他标本种类".equals(sq02a.getCaption())) {
                                                                                        patientInfo.setSpecimenName(sq02a.getValue());
                                                                                    } else if ("条形码".equals(sq02a.getCaption())) {

                                                                                    } else if ("性别".equals(sq02a.getCaption())) {
                                                                                        patientInfo.setPatSex(sq02a.getValue());
                                                                                    } else if ("执行科室".equals(sq02a.getCaption())) {

                                                                                    } else if ("执行科室代码".equals(sq02a.getCaption())) {

                                                                                    } else if ("注意事项".equals(sq02a.getCaption())) {
                                                                                        if (patientInfo.getClinicalData() != null && patientInfo.getClinicalData() != "" && !patientInfo.getClinicalData().equals(null) && !patientInfo.getClinicalData().equals("")) {
                                                                                            patientInfo.setClinicalData(patientInfo.getClinicalData() + "\n" + sq02a.getValue());
                                                                                        } else {
                                                                                            patientInfo.setClinicalData(sq02a.getValue());
                                                                                        }
                                                                                    } else if ("申请单序号".equals(sq02a.getCaption())) {

                                                                                    } else if ("诊断".equals(sq02a.getCaption())) {
                                                                                        if (patientInfo.getClinicalDiagnosis() != null) {
                                                                                            patientInfo.setClinicalDiagnosis(patientInfo.getClinicalDiagnosis().replace("\\", "") + "\n" + sq02a.getValue());
                                                                                        } else {
                                                                                            patientInfo.setClinicalDiagnosis(patientInfo.getClinicalDiagnosis() + "\n" + sq02a.getValue());
                                                                                        }

                                                                                    } else if ("临床信息".equals(sq02a.getCaption())) {
                                                                                        if (patientInfo.getClinicalData() != null && patientInfo.getClinicalData() != "" && !patientInfo.getClinicalData().equals(null) && !patientInfo.getClinicalData().equals("")) {
                                                                                            patientInfo.setClinicalData(patientInfo.getClinicalData() + "\n" + sq02a.getValue());
                                                                                        } else {
                                                                                            patientInfo.setClinicalData(sq02a.getValue());
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }
                                                                            patientInfoList.add(patientInfo);
                                                                        }
                                                                    }
                                                                } else {
                                                                    if (!Arrays.asList(myApplyNoInMysql).contains(sq03.getSqdxh())) {
                                                                        sq02List = getSQ02(sq03, brlb,bno,hisId);
                                                                        if (sq02List != null) {
                                                                            sq02size = sq02List.size();
                                                                            if (sq02size > 0) {
                                                                                patientInfo = new PatientInfoApply();
                                                                                patientInfo.setApplyNo(sq03.getSqdxh());//申请单号
                                                                                patientInfo.setPatientType(patientType);//病人类型
                                                                                patientInfo.setHisId(hisId);//hisId
                                                                                patientInfo.setSyxh(jb01.getSyxh());
                                                                                patientInfo.setCallHis(1);
//                                                                        patientInfo.setPatNo(jb01.getCureNo());//patId
                                                                                patientInfo.setPatNo(jb01.getPatientID());//patId
                                                                                if (jb01.getAge() != null && jb01.getAge() != "") {
                                                                                    patientInfo.setPatAge(Integer.parseInt(jb01.getAge().replaceAll("岁", "")));//年龄
                                                                                }
                                                                                if (jb01.getCardNo() != null) {
                                                                                    patientInfo.setCardNo(jb01.getCardNo());
                                                                                }
                                                                                patientInfo.setPatAgeUnit(jb01.getAgeUnit());
                                                                                patientInfo.setPatBirthday(formatDate(jb01.getBirthday()));//生日
                                                                                patientInfo.setMaritalStatus(jb01.getMarriage());//婚否
                                                                                patientInfo.setPatTel(jb01.getPhone());//电话
                                                                                patientInfo.setPatIdcard(jb01.getIDNum());//身份证
                                                                                patientInfo.setPatAddress(jb01.getAddress());//地址
                                                                                patientInfo.setInspectOffice(jb01.getApplyDept());//科室
                                                                                patientInfo.setDiseaseDistrict(jb01.getWard());//病区
                                                                                patientInfo.setBedNo(jb01.getBedNo());//床号
                                                                                patientInfo.setChargeType(jb01.getChargeType());//费别
                                                                                patientInfo.setNation(jb01.getNation());//民族
                                                                                patientInfo.setAcceptDoctor(user.getFullName());
                                                                                patientInfo.setMaterialsDoctor(user.getUserId());
                                                                                if (inspectDoctor != null) {
                                                                                    patientInfo.setInspectDoctor(inspectDoctor);//送检医生
                                                                                } else {
                                                                                    patientInfo.setInspectDoctor(jb01.getToDocName());//送检医生
                                                                                }
                                                                                applyNoList.add(sq03.getSqdxh());
                                                                                for (int j = 0; j < sq02size; j++) {
                                                                                    stringSQ02a = JSONArray.toJSONString(sq02List.get(j));
                                                                                    sq02a = JSON.parseObject(stringSQ02a, SQ02.class);
                                                                                    if (sq02a != null) {
                                                                                        if ("病历号".equals(sq02a.getCaption())) {
                                                                                            patientInfo.setHisId(sq02a.getValue());
                                                                                        } else if ("病史资料".equals(sq02a.getCaption())) {
                                                                                            if (patientInfo.getClinicalData() != null && patientInfo.getClinicalData() != "" && !patientInfo.getClinicalData().equals(null) && !patientInfo.getClinicalData().equals("")) {
                                                                                                patientInfo.setClinicalData(patientInfo.getClinicalData() + "\n" + sq02a.getValue());
                                                                                            } else {
                                                                                                patientInfo.setClinicalData(sq02a.getValue());
                                                                                            }
                                                                                        } else if ("患者姓名".equals(sq02a.getCaption())) {
                                                                                            patientInfo.setPatName(sq02a.getValue());
                                                                                        } else if ("检查部位：".equals(sq02a.getCaption())) {

                                                                                        } else if ("检查部位0".equals(sq02a.getCaption())) {

                                                                                        } else if ("检查部位1".equals(sq02a.getCaption())) {

                                                                                        } else if ("检查部位2".equals(sq02a.getCaption())) {

                                                                                        } else if ("检查目的".equals(sq02a.getCaption())) {
                                                                                            if (patientInfo.getClinicalData() != null && patientInfo.getClinicalData() != "" && !patientInfo.getClinicalData().equals(null) && !patientInfo.getClinicalData().equals("")) {
                                                                                                patientInfo.setClinicalData(patientInfo.getClinicalData() + "\n" + sq02a.getValue());
                                                                                            } else {
                                                                                                patientInfo.setClinicalData(sq02a.getValue());
                                                                                            }
                                                                                        } else if ("检查项目".equals(sq02a.getCaption())) {

                                                                                        } else if ("科室".equals(sq02a.getCaption())) {
                                                                                            patientInfo.setInspectOffice(sq02a.getValue());
                                                                                            if (sq02a.getValue() != null && sq02a.getValue() != "") {
                                                                                                code = masterMapper.queryInspectUnitByInspectOffice(sq02a.getValue());
                                                                                                if (code != null) {
                                                                                                    Map<String, Object> mp = getInspectUnit(brlb, code);
                                                                                                    patientInfo.setInspectUnit((Integer) mp.get("inspectUnit"));
                                                                                                    if (brlb == 0 || brlb == 3) {
                                                                                                        patientInfo.setDiseaseDistrictName((String) mp.get("diseaseDistrictName"));
                                                                                                        patientInfo.setDiseaseDistrict((String) mp.get("diseaseDistrict"));
                                                                                                    }
                                                                                                }
                                                                                            }
                                                                                        } else if ("临床诊断".equals(sq02a.getCaption())) {
                                                                                            if (sq02a.getValue() != null) {
                                                                                                patientInfo.setClinicalDiagnosis(sq02a.getValue().replace("\\", ""));
                                                                                            } else {
                                                                                                patientInfo.setClinicalDiagnosis(sq02a.getValue());
                                                                                            }

                                                                                        } else if ("年龄".equals(sq02a.getCaption())) {
                                                                                            patientInfo.setPatAge(Integer.parseInt(sq02a.getValue().replaceAll("岁", "")));
                                                                                            patientInfo.setPatAgeUnit(sq02a.getValue().replaceAll("[0-9]", ""));
                                                                                        } else if ("其他标本种类".equals(sq02a.getCaption())) {
                                                                                            patientInfo.setSpecimenName(sq02a.getValue());
                                                                                        } else if ("条形码".equals(sq02a.getCaption())) {

                                                                                        } else if ("性别".equals(sq02a.getCaption())) {
                                                                                            patientInfo.setPatSex(sq02a.getValue());
                                                                                        } else if ("执行科室".equals(sq02a.getCaption())) {

                                                                                        } else if ("执行科室代码".equals(sq02a.getCaption())) {

                                                                                        } else if ("注意事项".equals(sq02a.getCaption())) {
                                                                                            if (patientInfo.getClinicalData() != null && patientInfo.getClinicalData() != "" && !patientInfo.getClinicalData().equals(null) && !patientInfo.getClinicalData().equals("")) {
                                                                                                patientInfo.setClinicalData(patientInfo.getClinicalData() + "\n" + sq02a.getValue());
                                                                                            } else {
                                                                                                patientInfo.setClinicalData(sq02a.getValue());
                                                                                            }
                                                                                        } else if ("申请单序号".equals(sq02a.getCaption())) {

                                                                                        } else if ("诊断".equals(sq02a.getCaption())) {
                                                                                            if (patientInfo.getClinicalDiagnosis() != null) {
                                                                                                patientInfo.setClinicalDiagnosis(patientInfo.getClinicalDiagnosis().replace("\\", "") + "\n" + sq02a.getValue());
                                                                                            } else {
                                                                                                patientInfo.setClinicalDiagnosis(patientInfo.getClinicalDiagnosis() + "\n" + sq02a.getValue());
                                                                                            }

                                                                                        } else if ("临床信息".equals(sq02a.getCaption())) {
                                                                                            if (patientInfo.getClinicalData() != null && patientInfo.getClinicalData() != "" && !patientInfo.getClinicalData().equals(null) && !patientInfo.getClinicalData().equals("")) {
                                                                                                patientInfo.setClinicalData(patientInfo.getClinicalData() + "\n" + sq02a.getValue());
                                                                                            } else {
                                                                                                patientInfo.setClinicalData(sq02a.getValue());
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                }
                                                                                patientInfoList.add(patientInfo);
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    result.put("msg", 1071);
                                    return result;
                                }
                                String inspectDoctor = masterMapper.queryInspectDoctor(jb01.getApplyDoctor());
                                patientInfo.setPatientType(patientType);//病人类型
                                patientInfo.setHisId(hisId);//hisId
                                patientInfo.setSyxh(jb01.getSyxh());
                                patientInfo.setCallHis(0);
                                //patientInfo.setApplyNo(jb01.getSyxh());//申请单
                                patientInfo.setPatName(jb01.getPatName());//名称
                                if (jb01.getSex() != null) {
                                    patientInfo.setPatSex((jb01.getSex() == 2 || jb01.getSex() + "" == "女") ? "女" : "男");//性别
                                }
//                            patientInfo.setPatNo(jb01.getCureNo());//patId
                                patientInfo.setPatNo(jb01.getPatientID());//patId
                                if (jb01.getAge() != null && jb01.getAge() != "") {
                                    patientInfo.setPatAge(Integer.parseInt(jb01.getAge().replaceAll("岁", "")));//年龄
                                }
                                patientInfo.setPatAgeUnit(jb01.getAgeUnit());
                                patientInfo.setPatBirthday(formatDate(jb01.getBirthday()));//生日
                                patientInfo.setMaritalStatus(jb01.getMarriage());//婚否
                                patientInfo.setPatTel(jb01.getPhone());//电话
                                patientInfo.setPatIdcard(jb01.getIDNum());//身份证
                                patientInfo.setPatAddress(jb01.getAddress());//地址
                                //patientInfo.setInspectOffice(jb01.getApplyDept());//科室
                                patientInfo.setDiseaseDistrict(jb01.getWard());//病区
                                patientInfo.setBedNo(jb01.getBedNo());//床号
                                patientInfo.setChargeType(jb01.getChargeType());//费别
                                patientInfo.setNation(jb01.getNation());//民族
                                if (jb01.getZdmc() != null) {
                                    patientInfo.setClinicalDiagnosis(jb01.getZdmc().replace("\\", ""));//临床诊断
                                } else {
                                    patientInfo.setClinicalDiagnosis(jb01.getZdmc());//临床诊断
                                }
                                if (jb01.getCardNo() != null) {
                                    patientInfo.setCardNo(jb01.getCardNo());
                                }
//                            patientInfo.setClinicalDiagnosis(jb01.getZdmc().replace("\\",""));//临床诊断
                                patientInfo.setInspectDoctor(inspectDoctor);//送检医生
                                patientInfo.setInspectOffice(jb01.getApplyDept());
//                            patientInfo.setAcceptDoctor(user.getFullName());
//                            patientInfo.setMaterialsDoctor(user.getUserId());
                                if (jb01.getApplyDept() != null && jb01.getApplyDept() != "") {
                                    code = masterMapper.queryInspectUnitByInspectOffice(jb01.getApplyDept());
                                    if (code != null) {
                                        Map<String, Object> mp = getInspectUnit(brlb, code);
                                        patientInfo.setInspectUnit((Integer) mp.get("inspectUnit"));
                                        if (brlb == 0 || brlb == 3) {
                                            patientInfo.setDiseaseDistrictName((String) mp.get("diseaseDistrictName"));
                                            patientInfo.setDiseaseDistrict((String) mp.get("diseaseDistrict"));
                                        }
                                    }
                                }
                                patientInfoList.add(patientInfo);
                            }
//                        patientInfoList.add(patientInfo);
                        }
                    }
                }


//                for (int i = 0; i < patientInfoList.size(); i++) {
//                    patientInfoList.set(i, getPatientInfo(patientInfoList.get(i)));
//                }
                result.put("patientInfoList", patientInfoList);
                result.put("applyNoList", applyNoList);
                result.put("chargeList", chargeList);
                result.put("msg", 200);


            } else {
                result.put("msg", 1017);
            }
            WalnutLog walnutLog=new WalnutLog();
            walnutLog.setNo(bno);
            walnutLog.setMethod("提取患者信息");
            walnutLog.setHisId(hisId);
            walnutLog.setReqParam(JSON.toJSONString(param));
            walnutLog.setRespParam(JSON.toJSONString(result));
            walnutLog.setCreatetime(DateUtils.currentTimestampTime());
            walnutLogService.save(walnutLog);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("提取his", e);
        }
        return result;
    }


//    public PatientInfoApply getPatientInfo(PatientInfoApply patientInfo) throws IOException {
//        Map<String, Object> param = new HashMap<>();
//        param.put("diseaseDistrict", patientInfo.getDiseaseDistrict());
//        param.put("inspectOffice", patientInfo.getInspectOffice());
//        Map<String, Object> nameByCode = getNameByCode(param);
//        patientInfo.setDiseaseDistrict((String) nameByCode.get("diseaseDistrict"));
//        patientInfo.setInspectOffice((String) nameByCode.get("inspectOffice"));
//        return patientInfo;
//    }


//    //根据code查询 name
//    public Map<String, Object> getNameByCode(Map<String, Object> param) throws IOException {
//        Map<String, Object> result = new HashMap<String, Object>();
//        String diseaseDistrict = (String) param.get("diseaseDistrict"); // 病区
////        String inspectUnit = (String) param.get("inspectUnit");// 送检单位
//        String inspectOffice = (String) param.get("inspectOffice");//送检科室
//        //查询
//        String diseaseDistrictName = "";
//        if (diseaseDistrict != null && !"".equals(diseaseDistrict)) {
//            diseaseDistrictName = masterMapper.queryDictionaryByCode("Ward", diseaseDistrict);
//        }
////        String inspectUnitName = "";
////        if (inspectUnit != null && !"".equals(inspectUnit)) {
////            inspectUnitName = masterMapper.queryDictionaryByCode("Hospital_district", inspectUnit);
////        }
//        String inspectOfficeName = "";
//        if (inspectOffice != null && !"".equals(inspectOffice)) {
//            List<YY01> yy01 = getYY01();
//            Optional<YY01> first = yy01.stream().filter(item -> item.getId().equals(inspectOffice)).findFirst();
//            if (first.isPresent()) {
//                inspectOfficeName = first.get().getName();
//            }
//        }
//        result.put("diseaseDistrict", diseaseDistrictName);
//        result.put("inspectOffice", inspectOfficeName);
//        return result;
//    }
//
//    public List<YY01> getYY01() throws IOException {
//        Map<String, Object> sendParam = new ConcurrentHashMap<>();
//        sendParam.put("MsgCode", "YY01");
//        Map<String, Object> webServiceData = webServiceUtil.getWebService(sendParam);
//        if (webServiceData != null) {
//            @SuppressWarnings("unchecked")
//            List<YY01> yy01List = (List<YY01>) webServiceData.get("data");
//            return yy01List;
//        }
//        return null;
//    }

    /**
     * 批量添加/取消收费
     *
     * @param chIds
     * @param applyId
     * @param type    住院添加收费和退费
     * @return
     */
    public Map<String, Object> addAndCancelCharge(ArrayList<Map<String, Object>> chIds, Integer applyId, Integer type,Integer userId, String hisId) {
        Map<String, Object> result = new HashMap<>();
        Long now = DateUtils.currentTimestampTime();
        String bno = "C" + MD5util.string2MD5(String.valueOf(now) + cn.hutool.core.lang.UUID.nameUUIDFromBytes(UUID.randomUUID().toString().getBytes()));

        List<String> faile = new ArrayList<>();
        try {
            if (chIds.size() < 0 || applyId == null || type == null) {//判断传入值 是否为空
                result.put("msg", 1017);
                return result;
            }
            Integer button = null;
            TaskAssembly charges = masterMapper.selectAllByapplyId(applyId);

            JB01 jb01 = new JB01();
            if (charges.getHisId() != null || charges.getPatientType() != null) {
                if (charges.getPatientType().equals("501")) {
                    jb01 = getJB01(charges.getHisId(), 1,bno);
                }
            }
            // his 提取不到的患者无法添加收费项目
            if (jb01 == null) {
                result.put("msg", 1049);
                return result;
            }

            SysUser user = masterMapper.selUserInfoById(userId);
            //0门诊，1住院
            Map<String, Object> temp = new HashMap<>();
            BiographyQF01 biographyQF01 = new BiographyQF01();
            List<Charge> chargeList = new ArrayList<>();
            for (Map<String, Object> chId : chIds) {
                Charge ch = new Charge();
                if (chId.get("chargeCode") == null || "".equals(chId.get("chargeCode")) || chId.get("chargePrice") == null || "".equals(chId.get("chargePrice")) || chId.get("chargeNum") == null) {//判断传入值 是否为空
                    result.put("msg", 1017);
                    return result;
                }
                String typeCode = masterMapper.selectYY03ByCode((String) chId.get("chargeCode"));
                switch (type) {

                    case 0://住院添加费用AD02
                        biographyQF01.setBrlb("1");
                        biographyQF01.setPatid(charges.getPatNo());
                        biographyQF01.setCurno(jb01.getCureNo());
                        biographyQF01.setZxksdm("7009");
                        biographyQF01.setZxysdm(user.getJobcode());
                        biographyQF01.setItemcode((String) chId.get("chargeCode"));
                        biographyQF01.setPrice((String) chId.get("chargePrice"));
                        biographyQF01.setItemqty((Integer) chId.get("chargeNum"));
                        if ("yy04".equals(typeCode)) {
                            biographyQF01.setXmlb(0 + "");
                        } else if ("yy03".equals(typeCode)) {
                            biographyQF01.setXmlb(1 + "");
                        }
                        biographyQF01.setQqksdm("7009");
                        biographyQF01.setQqysdm(user.getJobcode());
                        biographyQF01.setDjlb(0 + "");
                        biographyQF01.setTssm("");
                        biographyQF01.setFylb(12 + "");
                        biographyQF01.setSfbz(0 + "");
                        biographyQF01.setTfxh(0 + "");
                        List<AD02> ad02s = getAD02(biographyQF01,bno,hisId);
                        List<AD02NO> chargeNos = new ArrayList<>();
                        List<String> statusRemarks = new ArrayList<>();
                        Integer code = 0;
                        AD02 ad02;
                        if (ad02s.size() > 0) {
                            for (int i = 0; i < ad02s.size(); i++) {
                                String data = JSONArray.toJSONString(ad02s.get(i));
                                ad02 = JSON.parseObject(data, AD02.class);
                                AD02NO ad02NO = new AD02NO();
                                // 收费错误返回 {msg=未找到该病人信息！, qrbz=1}
                                // 收费成功返回 {sfxh=50696230, qfbz=0}
                                if (ad02.getQrbz() == null) {
                                    if (ad02.getQfbz() != null || ad02.getQfbz().equals("0")) {
                                        ad02NO.setSfxh(ad02.getSfxh());
                                        ad02NO.setState("1");
                                        chargeNos.add(ad02NO);
                                        statusRemarks.add(ad02.getMsg());

                                    } else {
                                        code++;
                                    }
                                } else {
                                    result.put("fail", faile);
                                    result.put("msg", 1049);
                                }

                            }
                            // 添加套餐，若出现套餐中一项添加失败，则该套餐其他项也进行退费
                            if (code == 0) {
                                ch.setHisId(charges.getHisId());
                                ch.setPatNo(charges.getPatNo());
                                ch.setPatientType(501);
                                ch.setSyxh(jb01.getSyxh());
                                ch.setApplyDept("7009");
                                ch.setApplyId(applyId);
                                ch.setItemCode((String) chId.get("chargeCode"));
                                ch.setItemName((String) chId.get("ItemName"));
                                ch.setItemPrice((String) chId.get("chargePrice"));
                                ch.setItemQty((Integer) chId.get("chargeNum"));
                                ch.setStatusRemark(JSON.toJSONString(statusRemarks));
                                ch.setItemStatus("1");
                                ch.setChargeNo(JSON.toJSONString(chargeNos));
                                chargeList.add(ch);
                            } else {
                                for (AD02NO ad02NO : chargeNos) {
                                    biographyQF01.setBrlb("1");
                                    biographyQF01.setPatid(charges.getPatNo());
                                    biographyQF01.setCurno(jb01.getCureNo() == null ? "" : jb01.getCureNo());
                                    biographyQF01.setZxksdm("7009");
                                    biographyQF01.setZxysdm(user.getJobcode());
                                    biographyQF01.setItemcode((String) chId.get("chargeCode"));
                                    biographyQF01.setPrice((String) chId.get("chargePrice"));
                                    biographyQF01.setItemqty((Integer) chId.get("chargeNum"));
                                    biographyQF01.setXmlb(1 + "");
                                    biographyQF01.setQqksdm("7009");
                                    biographyQF01.setQqysdm(user.getJobcode());
                                    biographyQF01.setDjlb(0 + "");
                                    biographyQF01.setTssm("");
                                    biographyQF01.setFylb(12 + "");
                                    biographyQF01.setSfbz(1 + "");
                                    biographyQF01.setTfxh(ad02NO.getSfxh());
                                    List<AD02> ad02List02 = getAD02(biographyQF01,bno,hisId);
                                    if (ad02List02.get(0).getQfbz() == "0" && ad02List02.get(0).getSfxh() != null && !ad02List02.get(0).getSfxh().equals("")) {
                                        ad02NO.setState("3");
                                        ad02NO.setTfxh(ad02List02.get(0).getSfxh());
                                        chargeNos.add(ad02NO);
                                    } else {
                                        ad02NO.setState("2");//退费失败
                                        chargeNos.add(ad02NO);
                                        faile.add(ad02NO.getSfxh());
                                    }
                                }
                                ch.setHisId(charges.getHisId());
                                ch.setPatNo(charges.getPatNo());
                                ch.setSyxh(jb01.getSyxh());
                                ch.setApplyDept("7009");
                                ch.setApplyId(applyId);
                                ch.setItemCode((String) chId.get("chargeCode"));
                                ch.setItemPrice((String) chId.get("chargePrice"));
                                ch.setItemQty((Integer) chId.get("chargeNum"));
                                ch.setStatusRemark(JSON.toJSONString(statusRemarks));
                                ch.setItemStatus(3 + "");
                                ch.setChargeNo(JSON.toJSONString(chargeNos));
                                chargeList.add(ch);
                            }
                        }
                        break;
                    case 1://住院取消
                        List<AD02NO> chargeNos2 = new ArrayList<>();
                        List<String> statusRemarks2 = new ArrayList<>();
                        //根据applyId查询代码
                        String chargeNo = masterMapper.selChargeNoByInfo((Integer) chId.get("chargeId"));
                        ArrayList<AD02NO> ad02NOS = (ArrayList<AD02NO>) JSON.parseArray(chargeNo, AD02NO.class);
                        for (AD02NO ad02NO : ad02NOS) {
                            biographyQF01.setBrlb("1");
                            biographyQF01.setPatid(charges.getPatNo());
                            biographyQF01.setCurno(jb01.getCureNo() == null ? "" : jb01.getCureNo());
                            biographyQF01.setZxksdm("7009");
                            biographyQF01.setZxysdm(user.getJobcode());
                            biographyQF01.setItemcode((String) chId.get("chargeCode"));
                            biographyQF01.setPrice((String) chId.get("chargePrice"));
                            biographyQF01.setItemqty((Integer) (chId.get("chargeNum")));
                            if ("yy04".equals(typeCode)) {
                                biographyQF01.setXmlb(0 + "");
                            } else if ("yy03".equals(typeCode)) {
                                biographyQF01.setXmlb(1 + "");
                            }
                            biographyQF01.setQqksdm("7009");
                            biographyQF01.setQqysdm(user.getJobcode());
                            biographyQF01.setDjlb(0 + "");
                            biographyQF01.setTssm("");
                            biographyQF01.setFylb(12 + "");
                            biographyQF01.setSfbz(1 + "");
                            biographyQF01.setTfxh(ad02NO.getSfxh());
                            List<AD02> ad02List02 = getAD02(biographyQF01,bno,hisId);
                            if (ad02List02.size() > 0) {
                                if (("0").equals(ad02List02.get(0).getQfbz()) && ad02List02.get(0).getSfxh() != null && !ad02List02.get(0).getSfxh().equals("")) {
                                    ad02NO.setState("3");
                                    ad02NO.setTfxh(ad02List02.get(0).getSfxh());
                                    chargeNos2.add(ad02NO);
                                    statusRemarks2.add(ad02List02.get(0).getMsg());
                                } else {
                                    ad02NO.setState("2");//退费失败
                                    chargeNos2.add(ad02NO);
                                    faile.add(ad02NO.getSfxh());
                                    statusRemarks2.add(ad02List02.get(0).getColumn1());
                                }
                                ch.setStatusRemark(JSON.toJSONString(statusRemarks2));
                                ch.setItemStatus(3 + "");
                                ch.setChargeNo(JSON.toJSONString(chargeNos2));
                                ch.setChId((Integer) chId.get("chargeId"));
                                chargeList.add(ch);
                            }
                        }
                        break;
                }
            }
            result.put("chargeList",chargeList);
            result.put("fail", faile);
            result.put("msg", 200);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("msg", 500);
        }
        return result;
    }


    //收费确认(1 门诊确费 4 门诊取消收费)
    public QF01 getQF01(Integer type, Integer chid, String bno, String hisId) throws IOException {
        QF01 qf01 = new QF01();
        Map<String, Object> sendParam = new ConcurrentHashMap<>(10000);
        Integer xmstatus = null, sfflag = null, brlb = null;
        if (type == 1) {//门诊确认收费
            xmstatus = 1;
            sfflag = 1;
            brlb = 0;
        } else if (type == 0) {//门诊取消
            xmstatus = 3;
            sfflag = 0;
            brlb = 0;
        }
        Charge ch;
        ch = masterMapper.queryQF01ByReId(chid);
        if (ch != null) {
            sendParam.put("MsgCode", "QF01");
            String SendXML;
            SendXML = "<blh>" + ch.getHisId() + "</blh><brlb>" + brlb + "</brlb><xmlb>" + ch.getItemType() + "</xmlb><patid>" + ch.getPatNo() + "</patid><syxh>" + ch.getSyxh() + "</syxh><zxksdm></zxksdm><zxysdm></zxysdm><qqxh>" + ch.getRequireNo() + "</qqxh><qqmxxh>" + ch.getRequireNoItem() + "</qqmxxh><itemcode>" + ch.getItemCode() + "</itemcode><itemname>" + ch.getItemName() + "</itemname><price>" + ch.getItemPrice() + "</price><itemqty>" + ch.getItemQty() + "</itemqty><xmstatus>" + xmstatus + "</xmstatus><sfflag>" + sfflag + "</sfflag><djlb>0</djlb><bgdh></bgdh><bglx></bglx><tssm></tssm><tjrybh></tjrybh>";
            sendParam.put("SendXml", SendXML);
            Map<String, Object> webServiceData = webServiceUtil.getWebService(sendParam,bno,hisId);
            qf01 = JSON.parseObject((String) webServiceData.get("data"), QF01.class);
        }
        return qf01;

    }

    public List<AD02> getAD02(BiographyQF01 biographyQF01, String bno, String hisId) throws IOException {
        List<AD02> ad02List = new ArrayList<>();
        Map<String, Object> sendParam = new HashMap<>();
        Map<String, Object> webServiceData;
        sendParam.put("MsgCode", "AD02");
        String SendXML;
        SendXML = "<brlb>" + biographyQF01.getBrlb() + "</brlb><patid>" + biographyQF01.getPatid() + "</patid>" +
                "<curno>" + biographyQF01.getCurno() + "</curno><zxksdm>" + biographyQF01.getZxksdm() + "</zxksdm>" +
                "<zxysdm>" + biographyQF01.getZxysdm() + "</zxysdm><xmdm>" + biographyQF01.getItemcode() + "</xmdm>" +
                "<xmdj>" + biographyQF01.getPrice() + "</xmdj><xmsl>" + biographyQF01.getItemqty() + "</xmsl>" +
                "<xmlb>" + biographyQF01.getXmlb() + "</xmlb><qqksdm>" + biographyQF01.getQqksdm() + "</qqksdm>" +
                "<qqysdm>" + biographyQF01.getQqysdm() + "</qqysdm><djlb>" + biographyQF01.getDjlb() + "</djlb>" +
                "<tssm>" + biographyQF01.getTssm() + "</tssm><fylb>" + biographyQF01.getFylb() + "</fylb>" +
                "<sfbz>" + biographyQF01.getSfbz() + "</sfbz><tfxh>" + biographyQF01.getTfxh() + "</tfxh>";
        sendParam.put("SendXml", SendXML);
        webServiceData = webServiceUtil.getWebService(sendParam,bno,hisId);
        if (webServiceData.get("data") != null) {
            ad02List = JSON.parseArray((String) webServiceData.get("data"), AD02.class);
        }

        return ad02List;
    }

    /**
     * 提取jb01
     *
     * @param hisId
     * @param brlb  病人类别
     * @return
     */
    public JB01 getJB01(String hisId, Integer brlb,String bno) {
        JB01 jb01 = new JB01();
        try {
            jb01 = getJB01Result(hisId, brlb, "1", bno);
            if (jb01 != null) {
                if ("F".equals(jb01.getColumn1())) {
                    jb01 = getJB01Result(hisId, brlb, "5", bno);
                    if (jb01 != null) {
                        if ("F".equals(jb01.getColumn1())) {
                            jb01 = getJB01Result(hisId, brlb, "2", bno);
                            if (jb01 != null) {
                                if ("F".equals(jb01.getColumn1())) {
                                    jb01 = getJB01Result(hisId, brlb, "3", bno);
                                }
                            } else {
                                return null;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("提取JB01", e);
        }
        return jb01;
    }


    public JB01 getJB01Result(String hisId, Integer brlb, String codeType,String bno) throws IOException {
        JB01 jb01 = new JB01();
        String sendXML;
        Map<String, Object> webServiceData;
        Map<String, Object> send = new HashMap<>();
        send.put("MsgCode", "JB01");
        sendXML = "<codetype>" + codeType + "</codetype><code>" + hisId + "</code><brlb>" + brlb + "</brlb>";
        send.put("SendXml", sendXML);
        webServiceData = webServiceUtil.getWebService(send,bno,hisId);
        jb01 = JSON.parseObject((String) webServiceData.get("data"), JB01.class);
        return jb01;
    }


    //时间转换工具类
    public String formatDate(String old) {
        String newDate = "";
        try {
            SimpleDateFormat format8 = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat format2 = new SimpleDateFormat("yyyyMMdd");
            Date date = format2.parse(old);
            newDate = format8.format(date.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newDate;
    }

    /**
     * 提取jb03数据
     *
     * @param param jb01对象
     * @param brlb  病人类别
     * @return
     */

    public List<JB03> getJB03(JB01 param, Integer brlb, String bno, String hisId) {
        List<JB03> jb03List = new ArrayList<>();
        String sendXML;
        Map<String, Object> webServiceData;
        Map<String, Object> send = new HashMap<>();
        try {
            send.put("MsgCode", "JB03");
            String syxh;
            if (param.getSyxh() == null || param.getSyxh() == "" || param.getSyxh().equals("") || param.getSyxh().equals(null)) {
                syxh = "0";
            } else {
                syxh = param.getSyxh();
            }
            sendXML = "<blh>" + param.getHospNo() + "</blh><brlb>" + brlb + "</brlb><patid>" + param.getPatientID() + "</patid><syxh>" + syxh + "</syxh><qqxh>0</qqxh><tjrybh>0</tjrybh><rq1></rq1><rq2></rq2><zxks></zxks><fph>0</fph><yexh>0</yexh>";
            send.put("SendXml", sendXML);
            webServiceData = webServiceUtil.getWebService(send,bno,hisId);
            jb03List = (List<JB03>) webServiceData.get("data");
//            System.out.println(jb03List.get(0));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("提取JB03", e);
        }
        return jb03List;
    }

    /**
     * 提取SQ03
     *
     * @param param JB01 对象
     * @param brlb  病人类别
     * @return
     */

    public List<SQ03> getSQ03(JB01 param, Integer brlb, String bno, String hisId) {
        List<SQ03> sq03List = new ArrayList<>();
        String sendXML;
        Map<String, Object> webServiceData;
        Map<String, Object> send = new HashMap<>();
        try {
            send.put("MsgCode", "SQ03");
            sendXML = "<brlb>" + brlb + "</brlb><cardtype>0</cardtype><card>" + param.getHospNo() + "</card>";
            send.put("SendXml", sendXML);
            webServiceData = webServiceUtil.getWebService(send,bno,hisId);
            sq03List = (List<SQ03>) webServiceData.get("data");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("提取SQ03", e);
        }
        return sq03List;
    }

    /**
     * 提取SQ02
     *
     * @param param SQ03 对象
     * @param brlb  病人类别
     * @return
     */

    public List<SQ02> getSQ02(SQ03 param, Integer brlb, String bno, String hisId) {
        List<SQ02> sq02List = new ArrayList<>();
        String sendXML;
        Map<String, Object> webServiceData;
        Map<String, Object> send = new HashMap<>();
        try {
            send.put("MsgCode", "SQ02");
            sendXML = "<brlb>" + brlb + "</brlb><sqdxh>" + param.getSqdxh() + "</sqdxh>";
            send.put("SendXml", sendXML);
            webServiceData = webServiceUtil.getWebService(send,bno,hisId);
            sq02List = (List<SQ02>) webServiceData.get("data");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("提取SQ03", e);
        }
        return sq02List;
    }


    public List<SQ02> getSQ02(String applyNo, Integer brlb, String bno, String hisId) {
        List<SQ02> sq02List = new ArrayList<>();
        String sendXML;
        Map<String, Object> webServiceData;
        Map<String, Object> send = new HashMap<>();
        try {
            send.put("MsgCode", "SQ02");
            sendXML = "<brlb>" + brlb + "</brlb><sqdxh>" + applyNo + "</sqdxh>";
            send.put("SendXml", sendXML);
            webServiceData = webServiceUtil.getWebService(send,bno,hisId);
            sq02List = (List<SQ02>) webServiceData.get("data");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("提取SQ03", e);
        }
        return sq02List;
    }


    public BG02 getBG02(Integer reId, String bno, String hisId) throws IOException {
        Map<String, Object> sendParam = new ConcurrentHashMap<>(10000);
        BG02 bg02 = new BG02();
        sendParam.put("MsgCode", "BG02");
        String SendXML = "<repno>" + reId + "</repno><replb>bl</replb><hslb>0</hslb>";
        sendParam.put("SendXml", SendXML);
        Map<String, Object> BG02WebService = webServiceUtil.getWebService(sendParam,bno,hisId);
        if ((Integer) BG02WebService.get("code") == 200) {
            bg02 = JSON.parseObject((String) BG02WebService.get("data"), BG02.class);
        }
        return bg02;

    }


    public Map<String, Object> getBG01(Integer reId, String bno, String hisId) throws IOException {
        Map<String, Object> sendParam = new ConcurrentHashMap<>(10000);
        BG03 bg03 = new BG03();
        BG01 bg01 = masterMapper.queryBG01ByReId(reId);
        sendParam.put("MsgCode", "BG01");
        String SendXML = "<repno>" + bg01.getRepno() + "</repno><reqno>" + bg01.getReqno() + "</reqno>" +
                "<syxh>" + bg01.getSyxh() + "</syxh><patid>" + bg01.getPatid() + "</patid><blh>" + bg01.getBlh() + "</blh>" +
                "<cardno>" + bg01.getCardno() + "</cardno><hzxm>" + bg01.getHzxm() + "</hzxm><sex>" + bg01.getSex() + "</sex>" +
                "<age>" + bg01.getAge() + "</age><sjksdm>" + bg01.getSjksdm() + "</sjksdm><sjksmc>" + bg01.getSjksmc() + "</sjksmc>" +
                "<bqdm>" + bg01.getBqdm() + "</bqdm><bqmc>" + bg01.getBqmc() + "</bqmc><cwdm>" + bg01.getCwdm() + "</cwdm>" +
                "<sjysdm>00</sjysdm><sjysxm>" + bg01.getSjysxm() + "</sjysxm>" +
                "<sjrq>" + bg01.getSjrq() + "</sjrq><replb>bl</replb><replbmc>病理报告</replbmc>" +
                "<reprq>" + bg01.getReprq() + "</reprq><xtbz>" + bg01.getXtbz() + "</xtbz><jcbw>" + bg01.getJcbw() + "</jcbw>" +
                "<jcysdm></jcysdm><jcysxm></jcysxm><jcksdm></jcksdm>" +
                "<cjksmc></cjksmc><pubtime>" + bg01.getPubtime() + "</pubtime><crbz>0</crbz><instname>0</instname>";
        sendParam.put("SendXml", SendXML);
        System.out.println(SendXML);
        Map<String, Object> BG01WebService = webServiceUtil.getWebService(sendParam,bno,hisId);
        BG01WebService.put("name", "bg01");
        return BG01WebService;
    }


    public BG03 getBG03(Integer reId, String template, String reContent, String bno, String hisId) throws IOException {
        Map<String, Object> sendParam = new ConcurrentHashMap<>(10000);

        BG03 bg03 = new BG03();
        String SendXML;
        Map<String, Object> BG01WebService = getBG01(reId,bno,hisId);
        if ((Integer) BG01WebService.get("code") == 200) {
            BG01 bg = JSON.parseObject((String) BG01WebService.get("data"), BG01.class);
            if (bg.getColumn1() != "F" && !"F".equals(bg.getColumn1())) {
                sendParam.put("MsgCode", "BG03");
                Map<String, Object> map = getReport(template, reContent);
                int a = 1;
                for (String s : map.keySet()) {
                    SendXML = "<repno>" + reId + "</repno><replb>bl</replb><xmdm>0006</xmdm><xmmc>" + s + "</xmmc><jgckz>0</jgckz><qqmxxh>0</qqmxxh><xmjg>" + map.get(s) + "</xmjg><xmdw></xmdw><xjbz>0</xjbz><xjmc></xjmc><kssmc></kssmc><gmjg></gmjg><jgxh>" + a + "</jgxh><gdbz></gdbz><crbz></crbz>";
                    sendParam.put("SendXml", SendXML);
                    Map<String, Object> BG03WebService = webServiceUtil.getWebService(sendParam,bno,hisId);
                    BG03WebService.put("name", "BG03" + s);
                    if ((Integer) BG03WebService.get("code") == 200) {
                        bg03 = JSON.parseObject((String) BG03WebService.get("data"), BG03.class);
                    }
                    a++;
                }
            }
        }
        return bg03;

    }

    public Map<String, Object> getInspectUnit(Integer brlb, String code) {
        Map<String, Object> map = new HashMap<>();
        if ("5".equals(code.substring(0, 1)) || code.indexOf("市区") != -1) {
            map.put("inspectUnit", 201); //水电路
            if (brlb == 0) {
                map.put("diseaseDistrict", "CD03");
                map.put("diseaseDistrictName", "水电路门诊");
            } else if (brlb == 3) {
                map.put("diseaseDistrict", "CD04");
                map.put("diseaseDistrictName", "水电路体检");
            }
        } else {
            map.put("inspectUnit", 202); //水电路
            if (brlb == 0) {
                map.put("diseaseDistrict", "CD01");
                map.put("diseaseDistrictName", "金山门诊");
            } else if (brlb == 3) {
                map.put("diseaseDistrict", "CD02");
                map.put("diseaseDistrictName", "金山体检");
            }
        }
        return map;
    }


    public String getSendBody(String method, Integer codeType, String hisId) {
        String sendBody = "<TJ_GetPatInfo_NEW xmlns=\"http://winning.com.cn/tjgl\">\n" +
                "      <code>" + hisId + "</code>\n" +
                "      <codetype>" + codeType + "</codetype>\n" +
                "      <zxks></zxks>\n" +
                "    </TJ_GetPatInfo_NEW>";
        return sendBody;
    }

    //提取报告类容
    public Map<String, Object> getReport(String reportType, String reportContents) {
        String reportContent = reportContents.replace("<", "小于").replace(">", "大于");
        Map<String, Object> report = new HashMap<>();
        Map<String, Object> report02 = new HashMap<>();
        switch (reportType) {
            case "normal":
                report = JSON.parseObject(reportContent, Map.class);
//                System.out.println(report.keySet());
                for (String s : report.keySet()) {
                    if (!"".equals(report.get(s)) && report.get(s) != null) {
                        report02.put(s, report.get(s));
                    }
                }

                break;
            case "cytology":
                List label = Arrays.asList(new String[]{"标本名称", "制片技术", "细胞学诊断", "免疫组化", "分子检测", "建议"});
                report = JSON.parseObject(reportContent);
                for (String s : report.keySet()) {
//                    if ((label.contains(s) && ("".equals(report.get(s)) || report.get(s) == null)) || !label.contains(s)) {
//                        report02.put(s,report.get(s));
//                    }
                    if ((label.contains(s) && !"".equals(report.get(s)) && report.get(s) != null)) {
                        report02.put(s, report.get(s));
                    }
                }
                break;
            case "gastrointestinal-endoscopy":
                JSONObject detail = JSON.parseObject(reportContent);
                for (String info : detail.keySet()) {
                    if (!"镜下所见".equals(info)) {
                        if (!"".equals(detail.getString(info)) && detail.getString(info) != null) {
                            report02.put(info, detail.get(info));
                        }
                    } else if ("镜下所见".equals(info) && !"".equals(detail.getString(info)) || detail.getString(info) != null) {
                        JSONObject jsonObject = JSON.parseObject(detail.getString(info));
                        List<Map> meterList = JSONArray.parseArray(jsonObject.getString("取材部位"), Map.class);
                        List<Map> meterList2 = JSONArray.parseArray(jsonObject.getString("炎症"), Map.class);
                        String list = "";
                        for (int i = 0; i < meterList.size(); i++) {
                            String oneMsg = null;
                            String s = "", s1, s2 = "";
                            Map map = meterList.get(i);
//                            if (map.get("disabled").equals(true)) {
                            s1 = (String) map.get("name");
                            if (s1 != null && !"".equals(s1)) {
                                for (int j = 0; j < meterList2.size(); j++) {
                                    Map map2 = meterList2.get(j);
                                    List<Map> o = JSONArray.parseArray(String.valueOf(map2.get("degrees")), Map.class);
                                    if (o.get(i).get("degree") != null && !o.get(i).get("degree").equals("")) {
                                        if (j == meterList2.size() - 1) {
                                            s2 = s2 + (String) map2.get("name") + "(" + o.get(i).get("degree") + ")；";
                                        } else {
                                            s2 = s2 + (String) map2.get("name") + "(" + o.get(i).get("degree") + ")；";
                                        }
                                    }
                                }
                                if (map.get("count") != null && !"".equals(map.get("count"))) {
                                    s = (s1 + "（" + (String) map.get("count") + "块）:" + s2);
                                } else {
                                    s = (s1 + ":" + s2);
                                }
//                            }
                            }
                            list = list + s;
                        }
                        report02.put("镜下所见", list);
                    }

                }
                break;
            case "GSTS":
                List label02 = Arrays.asList(new String[]{"其他病理特征", "免疫组化", "分子检测", "危险度评估"});
                JSONObject GSTSDetail = JSON.parseObject(reportContent);
//                List<String> list01 = new ArrayList<>();
                String ss = "";
                for (String s : GSTSDetail.keySet()) {
                    if (label02.contains(s)) {
                        if (!"".equals(GSTSDetail.getString(s)) && GSTSDetail.getString(s) != null) {
                            report02.put(s, GSTSDetail.get(s));
                        }
                    } else {
                        if (!"".equals(GSTSDetail.getString(s)) && GSTSDetail.getString(s) != null) {
                            ss += s + ":" + GSTSDetail.get(s) + "；";
//                            list01.add((String) GSTSDetail.get(s));
                        }
                    }
                }
                if (ss.length() > 0) {
                    ss = ss.substring(0, ss.length() - 1);
                }
                report02.put("检查所见", ss);
                break;
            case "frozen":
                report = JSON.parseObject(reportContent);
                for (String s : report.keySet()) {
                    if (!"".equals(report.get(s)) && report.get(s) != null) {
                        report02.put(s, report.get(s));
                    }
                }
                break;
            case "TBS":
                JSONObject TBSDetail = JSON.parseObject(reportContent);
                for (String s : TBSDetail.keySet()) {
                    if (!"".equals(report.get(s)) && report.get(s) != null) {
                        report02.put(s, report.get(s));
                    } else {
                        if ("specimenStatus".equals(s)) {
                            List<Map> meterList02 = JSONArray.parseArray(String.valueOf(TBSDetail.get(s)), Map.class);
                            String sss = "";
                            for (int i = 0; i < meterList02.size(); i++) {
                                if (!("").equals(meterList02.get(i).get("value"))) {
                                    if (i == meterList02.size() - 1) {
                                        sss = sss + meterList02.get(i).get("label") + ":" + meterList02.get(i).get("value") + "。";
                                    } else {
                                        sss = sss + meterList02.get(i).get("label") + ":" + meterList02.get(i).get("value") + "；";
                                    }
                                }
                            }
                            report02.put("检查所见", sss);
                        } else if ("TBSDiagnose".equals(s)) {
                            report02.put("TBS诊断", TBSDetail.get(s));
                        } else if ("advise".equals(s)) {
                            report02.put("建议", TBSDetail.get(s));
                        }
                    }
                }
                break;
            case "autopsy":

                break;
        }
//        System.out.println(report02);
        return report02;
    }

    /**
     * 收费项目==> 住院:添加和退费;体检/门诊确认和取消
     *
     * @param chIds
     * @param applyId
     * @param type
     * @param patientType
     * @return
     * @throws IOException
     */
    public Map<String, Object> sendHisConfirmCharge(ArrayList<Map<String, Object>> chIds, Integer applyId, Integer type, Integer patientType,Integer userId,String hisId) throws IOException {
        Map<String, Object> result = new HashMap<>();
        Long now = DateUtils.currentTimestampTime();
        String bno = "C" + MD5util.string2MD5(String.valueOf(now) + cn.hutool.core.lang.UUID.nameUUIDFromBytes(UUID.randomUUID().toString().getBytes()));

        if (chIds.size() < 0 || applyId == null || type == null) {//判断传入值 是否为空
            result.put("msg", 1017);
            return result;
        }
        List<String> faile = new ArrayList<>();
        SysUser user = masterMapper.selUserInfoById(userId);
        TaskAssembly charges = masterMapper.selectAllByapplyId(applyId);
        List<Charge> chargeList = new ArrayList<>();
        BiographyQF01 biographyQF01 = new BiographyQF01();
        Map<String, Object> sendParam = new HashMap<>();
        try {
            // 住院添加收费和退费
            if (patientType == 501) {
                JB01 jb01 = new JB01();
                if (charges.getHisId() != null || charges.getPatientType() != null) {
                    if (charges.getPatientType().equals("501")) {
                        jb01 = getJB01(charges.getHisId(), 1,bno);
                    }
                }
                // his 提取不到的患者无法添加收费项目
                if (jb01 == null) {
                    result.put("msg", 1049);
                    return result;
                }

                for (Map<String, Object> chId : chIds) {
                    Charge ch = new Charge();
                    if (chId.get("chargeCode") == null || "".equals(chId.get("chargeCode")) || chId.get("chargePrice") == null || "".equals(chId.get("chargePrice")) || chId.get("chargeNum") == null) {//判断传入值 是否为空
                        result.put("msg", 1017);
                        return result;
                    }
                    String typeCode = masterMapper.selectYY03ByCode((String) chId.get("chargeCode"));
                    switch (type) {
                        case 1://住院添加费用AD02
                            biographyQF01.setBrlb("1");
                            biographyQF01.setPatid(charges.getPatNo());
                            biographyQF01.setCurno(jb01.getCureNo());
                            biographyQF01.setZxksdm("7009");
                            biographyQF01.setZxysdm(user.getJobcode());
                            biographyQF01.setItemcode((String) chId.get("chargeCode"));
                            biographyQF01.setPrice((String) chId.get("chargePrice"));
                            biographyQF01.setItemqty((Integer) chId.get("chargeNum"));
                            if ("yy04".equals(typeCode)) {
                                biographyQF01.setXmlb(0 + "");
                            } else if ("yy03".equals(typeCode)) {
                                biographyQF01.setXmlb(1 + "");
                            }
                            biographyQF01.setQqksdm("7009");
                            biographyQF01.setQqysdm(user.getJobcode());
                            biographyQF01.setDjlb(0 + "");
                            biographyQF01.setTssm("");
                            biographyQF01.setFylb(12 + "");
                            biographyQF01.setSfbz(0 + "");
                            biographyQF01.setTfxh(0 + "");
                            List<AD02> ad02s = getAD02(biographyQF01,bno,hisId);
                            List<AD02NO> chargeNos = new ArrayList<>();
                            List<String> statusRemarks = new ArrayList<>();
                            Integer code = 0;
                            AD02 ad02;
                            if (ad02s.size() > 0) {
                                for (int i = 0; i < ad02s.size(); i++) {
                                    String data = JSONArray.toJSONString(ad02s.get(i));
                                    ad02 = JSON.parseObject(data, AD02.class);
                                    AD02NO ad02NO = new AD02NO();
                                    // 收费错误返回 {msg=未找到该病人信息！, qrbz=1}
                                    // 收费成功返回 {sfxh=50696230, qfbz=0}
                                    if (ad02.getQrbz() == null) {
                                        if (ad02.getQfbz() != null || ad02.getQfbz().equals("0")) {
                                            ad02NO.setSfxh(ad02.getSfxh());
                                            ad02NO.setState("1");
                                            chargeNos.add(ad02NO);
                                            statusRemarks.add(ad02.getMsg());

                                        } else {
                                            code++;
                                        }
                                    } else {
                                        result.put("fail", faile);
                                        result.put("msg", 1049);
                                    }

                                }
                                // 添加套餐，若出现套餐中一项添加失败，则该套餐其他项也进行退费
                                if (code == 0) {
                                    ch.setHisId(charges.getHisId());
                                    ch.setPatNo(charges.getPatNo());
                                    ch.setPatientType(501);
                                    ch.setSyxh(jb01.getSyxh());
                                    ch.setApplyDept("7009");
                                    ch.setApplyId(applyId);
                                    ch.setItemCode((String) chId.get("chargeCode"));
                                    ch.setItemName((String) chId.get("ItemName"));
                                    ch.setItemPrice((String) chId.get("chargePrice"));
                                    ch.setItemQty((Integer) chId.get("chargeNum"));
                                    ch.setStatusRemark(JSON.toJSONString(statusRemarks));
                                    ch.setItemStatus(1 + "");
                                    ch.setChargeNo(JSON.toJSONString(chargeNos));
                                    chargeList.add(ch);
                                } else {
                                    for (AD02NO ad02NO : chargeNos) {
                                        biographyQF01.setBrlb("1");
                                        biographyQF01.setPatid(charges.getPatNo());
                                        biographyQF01.setCurno(jb01.getCureNo() == null ? "" : jb01.getCureNo());
                                        biographyQF01.setZxksdm("7009");
                                        biographyQF01.setZxysdm(user.getJobcode());
                                        biographyQF01.setItemcode((String) chId.get("chargeCode"));
                                        biographyQF01.setPrice((String) chId.get("chargePrice"));
                                        biographyQF01.setItemqty((Integer) chId.get("chargeNum"));
                                        biographyQF01.setXmlb(1 + "");
                                        biographyQF01.setQqksdm("7009");
                                        biographyQF01.setQqysdm(user.getJobcode());
                                        biographyQF01.setDjlb(0 + "");
                                        biographyQF01.setTssm("");
                                        biographyQF01.setFylb(12 + "");
                                        biographyQF01.setSfbz(1 + "");
                                        biographyQF01.setTfxh(ad02NO.getSfxh());
                                        List<AD02> ad02List02 = getAD02(biographyQF01,bno,hisId);
                                        if (ad02List02.get(0).getQfbz() == "0" && ad02List02.get(0).getSfxh() != null && !ad02List02.get(0).getSfxh().equals("")) {
                                            ad02NO.setState("3");
                                            ad02NO.setTfxh(ad02List02.get(0).getSfxh());
                                            chargeNos.add(ad02NO);
                                        } else {
                                            ad02NO.setState("2");//退费失败
                                            chargeNos.add(ad02NO);
                                            faile.add(ad02NO.getSfxh());
                                        }
                                    }
                                    ch.setHisId(charges.getHisId());
                                    ch.setPatNo(charges.getPatNo());
                                    ch.setSyxh(jb01.getSyxh());
                                    ch.setApplyDept("7009");
                                    ch.setApplyId(applyId);
                                    ch.setItemCode((String) chId.get("chargeCode"));
                                    ch.setItemPrice((String) chId.get("chargePrice"));
                                    ch.setItemQty((Integer) chId.get("chargeNum"));
                                    ch.setStatusRemark(JSON.toJSONString(statusRemarks));
                                    ch.setItemStatus(3 + "");
                                    ch.setChargeNo(JSON.toJSONString(chargeNos));
                                    chargeList.add(ch);
                                }
//                                masterMapper.insertChargeList(chargeList, applyId);
                            }
                            break;
                        case 0://住院取消
                            List<AD02NO> chargeNos2 = new ArrayList<>();
                            List<String> statusRemarks2 = new ArrayList<>();
                            //根据applyId查询代码
                            String chargeNo = masterMapper.selChargeNoByInfo((Integer) chId.get("chargeId"));
                            ArrayList<AD02NO> ad02NOS = (ArrayList<AD02NO>) JSON.parseArray(chargeNo, AD02NO.class);
                            for (AD02NO ad02NO : ad02NOS) {
                                biographyQF01.setBrlb("1");
                                biographyQF01.setPatid(charges.getPatNo());
                                biographyQF01.setCurno(jb01.getCureNo() == null ? "" : jb01.getCureNo());
                                biographyQF01.setZxksdm("7009");
                                biographyQF01.setZxysdm(user.getJobcode());
                                biographyQF01.setItemcode((String) chId.get("chargeCode"));
                                biographyQF01.setPrice((String) chId.get("chargePrice"));
                                biographyQF01.setItemqty((Integer) (chId.get("chargeNum")));
                                if ("yy04".equals(typeCode)) {
                                    biographyQF01.setXmlb(0 + "");
                                } else if ("yy03".equals(typeCode)) {
                                    biographyQF01.setXmlb(1 + "");
                                }
                                biographyQF01.setQqksdm("7009");
                                biographyQF01.setQqysdm(user.getJobcode());
                                biographyQF01.setDjlb(0 + "");
                                biographyQF01.setTssm("");
                                biographyQF01.setFylb(12 + "");
                                biographyQF01.setSfbz(1 + "");
                                biographyQF01.setTfxh(ad02NO.getSfxh());
                                List<AD02> ad02List02 = getAD02(biographyQF01,bno,hisId);
                                if (ad02List02.size() > 0) {
                                    if (("0").equals(ad02List02.get(0).getQfbz()) && ad02List02.get(0).getSfxh() != null && !ad02List02.get(0).getSfxh().equals("")) {
                                        ad02NO.setState("3");
                                        ad02NO.setTfxh(ad02List02.get(0).getSfxh());
                                        chargeNos2.add(ad02NO);
                                        statusRemarks2.add(ad02List02.get(0).getMsg());
                                    } else {
                                        ad02NO.setState("2");//退费失败
                                        chargeNos2.add(ad02NO);
                                        faile.add(ad02NO.getSfxh());
                                        statusRemarks2.add(ad02List02.get(0).getColumn1());
                                    }
                                    ch.setStatusRemark(JSON.toJSONString(statusRemarks2));
                                    ch.setItemStatus(3 + "");
                                    ch.setChargeNo(JSON.toJSONString(chargeNos2));
                                    ch.setChId((Integer) chId.get("chargeId"));
                                    chargeList.add(ch);
                                }
                            }
                            break;
                    }
                }
                result.put("fail", faile);
                result.put("msg", 200);

            } else if (patientType == 502) {
                // 门诊确认和取消收费
                Charge ch;
                int y = 0, n = 0;
                for (Map<String, Object> chId : chIds) {
                    ch = masterMapper.queryQF01ByReId((Integer) chId.get("chargeId"));
                    if (ch != null) {
                        QF01 qf01 = getQF01(type, (Integer) chId.get("chargeId"),bno,hisId);
                        if (qf01 != null) {
                            if ("F".equals(qf01.getColumn1())) {
                                n++;
                                ch.setStatus(3);
                            } else {
                                y++;
                                ch.setStatus(2);
                                if (type == 1) {
                                    ch.setItemStatus(1 + "");
                                } else if (type == 0) {
                                    ch.setItemStatus(0 + "");
                                }
                            }
                            ch.setStatusRemark(qf01.getColumn1());
                        }
                       chargeList.add(ch);
                    }
                }
                result.put("msg", 200);
                result.put("y", y);
                result.put("n", n);
            } else if (patientType == 503) {
                //体检 确认和取消确认 1:确认， 0 取消
                int y = 0, n = 0;
                Charge ch;
                for (Map<String, Object> chId : chIds) {
                    ch = masterMapper.queryQF01ByReId((Integer) chId.get("chargeId"));
                    Integer reId = masterMapper.searchReportIdByApplyNo(ch.getApplyId());
                    if (ch != null) {
                        sendParam.put("code", ch.getHisId());
                        sendParam.put("logno", ch.getRequireNo());
                        sendParam.put("status", type);
                        sendParam.put("bgdh", reId);
                        String TJresult = mecWebservice.TJ_ConfirmItems(sendParam,bno,hisId);
                        if (TJresult.equals("T")) {
                            y++;
                            ch.setStatus(2);
                            if (type == 1) {
                                ch.setItemStatus(1 + "");
                            } else if (type == 0) {
                                ch.setItemStatus(0 + "");
                            }
                        } else {
                            n++;
                            ch.setStatus(3);
                        }
                        result.put("ChargeList",chargeList);
                        result.put("msg", 200);
                        result.put("y", y);
                        result.put("n", n);
                        chargeList.add(ch);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.put("msg", 500);
        }
        return result;
    }
}
