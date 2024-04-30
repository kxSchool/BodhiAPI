package com.example.modules.wnhis.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.common.api.CommonResult;
import com.example.modules.wnhis.MecWebservice;
import com.example.modules.wnhis.pojo.*;
import com.example.modules.wnhis.service.HisService;
import com.example.modules.wnhis.service.HisYy01Service;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Toomth
 * @date 2021/3/16 15:59
 * @explain 原 核桃 数据接口 （改造）
 */
@RestController
@RequestMapping("/hisInterface")
public class HISFunctionController {

    @Autowired
    HisService hisService;

    @Autowired
    MecWebservice mecWebservice;
    @Autowired
    HisYy01Service hisYy01Service;

    /**
     * 报告审核
     * @param bg01
     * @return
     * @throws IOException
     */
    @PostMapping("/pushReport")
    @ResponseBody
    public CommonResult pushReport(@RequestBody BG01 bg01) throws IOException {
        Map<String, Object> result = hisService.getBG01(bg01);
        return CommonResult.success(result);
    }


    /**
     * his 提取功能
     *
     * @param param
     * @return
     */
    @PostMapping("/HISExtraction")
    @ResponseBody
    public CommonResult HISExtraction(@RequestBody Map<String, Object> param) throws IOException {
        String hisId = (String) param.get("hisId");
        Integer brlb = (Integer) param.get("brlb");
        String applyNo = (String) param.get("applyNo");
        if (StringUtils.isEmpty(hisId) || StringUtils.isEmpty(applyNo)) {
            return CommonResult.success(-1, "HIS提取失败");
        }
        //判断 brlb
        Map<String, Object> map = new HashMap<>();
        if (brlb == 3) {
            map = TJExtraction(hisId, brlb, applyNo);
        } else {
//            map = ZMExtraction(hisId, brlb, applyNo);
        }
        return CommonResult.success(0, "HIS提取失败");
    }


    /**
     * 住院和门诊提取患者信息
     *
     * @param hisId
     * @param brlb
     * @param applyNo
     * @return
     * @throws IOException
     */
    public Map<String, Object> ZMExtraction(String hisId, Integer brlb, String applyNo, Integer patientType,String[] myApplyNoInMysql,String inspectDoctor,SysUser user) throws IOException {
        Map<String, Object> result = new HashMap<String, Object>();
        List<PatientInfoApply> patientInfoList = new ArrayList<PatientInfoApply>(10000);
        List<String> applyNoList = new ArrayList<String>(10000);
        List<Charge> chargeList = new ArrayList<Charge>(10000);
        PatientInfoApply patientInfo = new PatientInfoApply();
        String code;
        if ((applyNo == null || applyNo == "") && hisId != null && patientType != null) {
            JB01 jb01 = hisService.getJB01(hisId, brlb);
            if (jb01 != null) {
                int jb01notnull = 0;
                if (jb01.getColumn1() != "F" && !"F".equals(jb01.getColumn1())) {
                    List<JB03> jb03List = hisService.getJB03(jb01, brlb);
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
                                String stringSQ03, stringSQ02;
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
                                sq03List = hisService.getSQ03(jb01, brlb);
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
                                                    sq02List = hisService.getSQ02(sq03, brlb);
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
                                                                            code = getContentByCode(sq02.getValue());
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
                                                        sq02List = hisService.getSQ02(sq03, brlb);
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
                                                                                code = getContentByCode(sq02.getValue());
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
                        code = getContentByCode(jb01.getApplyDept());
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
            List<SQ02> list = hisService.getSQ02(applyNo, brlb);
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
                                code = getContentByCode(sq02.getValue());
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
                JB01 jb01 = hisService.getJB01(hisId, brlb);
                if (jb01 != null) {
                    if (jb01.getColumn1() != "F" && !"F".equals(jb01.getColumn1())) {
                        List<JB03> jb03List = hisService.getJB03(jb01, brlb);
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
                                    String stringSQ03, stringSQ02a;
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
                                    sq03List = hisService.getSQ03(jb01, brlb);
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
                                                        sq02List = hisService.getSQ02(sq03, brlb);
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
                                                                                code = getContentByCode(sq02a.getValue());
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
                                                            sq02List = hisService.getSQ02(sq03, brlb);
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
                                                                                    code = getContentByCode(sq02a.getValue());
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
                        code = getContentByCode(jb01.getApplyDept());
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
        }

        return result;
    }


    /**
     * 体检系统提取患者信息
     *
     * @param hisId
     * @param brlb
     * @param applyNo
     * @return
     * @throws IOException
     */
    public Map<String, Object> TJExtraction(String hisId, Integer brlb, String applyNo) throws IOException {
        List<PatientInfoApply> patientInfoList = new ArrayList<PatientInfoApply>(10000);
        List<Charge> chargeList = new ArrayList<Charge>(10000);
        PatientInfoApply patientInfo = new PatientInfoApply();
        Map<String, Object> result = new HashMap<>();
        String method = "TJ_GetPatInfo_NEW";
        Integer codeType = 1; //1：体检编号 2：卡号
        Map<String, Object> mecPatient;
        Map<String, Object> map; //返回的患者基础信息
        String sendBody = getSendBody(method, codeType, hisId);
        mecPatient = mecWebservice.getMecWebService(sendBody, method);
        map = (Map<String, Object>) mecPatient.get("PersonInfo");
        if (map.get("Column1") != null) {
            codeType = 2;
            sendBody = getSendBody(method, codeType, hisId);
            mecPatient = mecWebservice.getMecWebService(sendBody, method);
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
            result.put("code", -1);
            return result;
        }
        result.put("patientInfoList", patientInfoList);
        result.put("chargeList", chargeList);
        result.put("code", 0);
        return result;
    }

    public String getSendBody(String method, Integer codeType, String hisId) {
        String sendBody = "<TJ_GetPatInfo_NEW xmlns=\"http://winning.com.cn/tjgl\">\n" +
                "      <code>" + hisId + "</code>\n" +
                "      <codetype>" + codeType + "</codetype>\n" +
                "      <zxks></zxks>\n" +
                "    </TJ_GetPatInfo_NEW>";
        return sendBody;
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


    public String getContentByCode(String code) throws IOException {
        List<HisYy01> hisYy01s = hisYy01Service.queryNameByCode(code);
        if (hisYy01s.size()<1){
            List<HisYy01> hisyy01 = hisService.getHISYY01();
            Boolean delete = hisYy01Service.delete();
            hisYy01Service.saveBatch(hisyy01);
        }
        List<HisYy01> list = hisYy01Service.queryNameByCode(code);
        if (hisYy01s.size()<1){
          return  code;
        }else {
            return list.get(0).getName();
        }

    }


}
