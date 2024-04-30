package com.example.modules.walnut.service;

import cn.hutool.core.lang.UUID;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.example.modules.walnut.domain.HIS.*;
import com.example.modules.walnut.domain.Report;
import com.example.modules.walnut.domain.ReportTypeCurrentType;
import com.example.modules.walnut.domain.TaskAssembly;
import com.example.modules.walnut.mapper.MasterMapper;
import com.example.modules.walnut.model.WalnutLog;
import com.example.modules.wnhis.pojo.Charge;
import com.example.modules.wnhis.pojo.SysUser;
import com.example.utils.DateUtils;
import com.example.utils.MD5util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Toomth
 * @date 2021/3/24 17:01
 * @explain
 */
@DS("slave")
@Service
public class DemoService {
    @Autowired
    MasterMapper masterMapper;
    @Autowired
    HISService hisService;
    @Autowired
    WMecWebservice mecWebservice;
    @Autowired
    private WalnutLogService walnutLogService;


    /**
     * 批量添加/取消收费
     *
     * @param chIds
     * @param applyId
     * @param type    住院添加收费和退费
     * @return
     */
    public Map<String, Object> addAndCancelCharge(ArrayList<Map<String, Object>> chIds, Integer applyId, Integer type, Integer userId, String hisId) {
        Map<String, Object> result = new HashMap<>();
        WalnutLog walnutLog=new WalnutLog();
        Map<String, Object> param=new HashMap<>();
        param.put("ArrayList<Map<String, Object>> chIds",chIds);
        param.put("applyId",applyId);
        param.put("type",type);
        param.put("当前操作用户ID",userId);
        param.put("hisId",hisId);
        Long now = DateUtils.currentTimestampTime();
        String bno = "C" + MD5util.string2MD5(String.valueOf(now) + UUID.nameUUIDFromBytes(UUID.randomUUID().toString().getBytes()));

        List<String> faile = new ArrayList<>();
        try {
            TaskAssembly charges = masterMapper.selectAllByapplyId(applyId);
            JB01 jb01 = new JB01();
            if (charges.getHisId() != null || charges.getPatientType() != null) {
                if (charges.getPatientType().equals("501")) {
                    jb01 = hisService.getJB01(charges.getHisId(), 1,bno);
                }
            }
            // his 提取不到的患者无法添加收费项目
            if (jb01 == null) {
                result.put("msg", "his 提取不到的患者无法添加收费项目");
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
                        walnutLog.setMethod("住院添加费用");

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
                        List<AD02> ad02s = hisService.getAD02(biographyQF01,bno,hisId);
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
                                    List<AD02> ad02List02 = hisService.getAD02(biographyQF01,bno,hisId);
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
                            result.put("ChargeList", chargeList);
                        }
                        break;
                    case 1://住院取消
                        List<AD02NO> chargeNos2 = new ArrayList<>();
                        List<String> statusRemarks2 = new ArrayList<>();
                        walnutLog.setMethod("住院取消费用");
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
                            List<AD02> ad02List02 = hisService.getAD02(biographyQF01,bno,hisId);
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
                        result.put("ChargeList", chargeList);
                        break;
                }
            }
            result.put("fail", faile);
            result.put("msg", 200);
            walnutLog.setNo(bno);
            walnutLog.setHisId(hisId);
            walnutLog.setReqParam(JSON.toJSONString(param));
            walnutLog.setRespParam(JSON.toJSONString(result));
            walnutLog.setCreatetime(DateUtils.currentTimestampTime());
            walnutLogService.save(walnutLog);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("msg", 500);
        }
        return result;
    }

    /**
     * 报告推送
     *
     * @param applyId
     * @param userId
     * @return
     * @throws IOException
     */
    public Map<String, Object> reportSaveButton(Integer applyId, Integer userId,String hisId) throws IOException {
        Map<String, Object> mp = new ConcurrentHashMap<>(10);
        WalnutLog walnutLog=new WalnutLog();
        Map<String, Object> param=new HashMap<>();
        param.put("applyId",applyId);
        param.put("当前操作用户ID",userId);
        param.put("hisId",hisId);
        Long now = DateUtils.currentTimestampTime();
        String bno = "C" + MD5util.string2MD5(String.valueOf(now) + UUID.nameUUIDFromBytes(UUID.randomUUID().toString().getBytes()));

        SysUser user = masterMapper.selUserInfoById(userId);
        Report report = masterMapper.queryReportContentByPathId(applyId);
        Map<String, Object> map = hisService.getReport(report.getTemplate(), report.getAuditContent());
        report.setReContent(report.getAuditContent());
        if (report.getReDoctor() == null || report.getReDoctor().equals("")) {
            report.setReDoctor(user.getUserId());
        } else {
            report.setReDoctor(report.getReDoctor());
        }
        report.setAuditDoctor(user.getUserId());

        String result = masterMapper.selectReContByApplyNo(applyId);
        if (result == null || report.equals("")) {
            report.setAuditTime("now()");
        } else {
            String result01 = masterMapper.selectAuditTimeByApplyNo(applyId);
            if (result01 == null || result01 == "") {
                report.setAuditTime("now()");
            } else {
                report.setAuditTime(null);
            }
        }
        report.setCurrentType(4);
        report.setReType(108);
        report.setSend(null);
        report.setVisitDoctor(null);

        Map<String, Object> mecPatient = masterMapper.selectMecPatientInfoByReId(report.getReId());
        Map<String, Object> sendParam = new ConcurrentHashMap<>(10000);
        Integer patientType = 0;
        if (mecPatient != null && mecPatient.size() > 0) {
            patientType = (Integer) mecPatient.get("patientTtype");
        }

        if (patientType == 503) {
            String specimenName = "";
            if (mecPatient.get("specimenName") != null) {
                specimenName = (String) mecPatient.get("specimenName");
                specimenName = specimenName.replace("[\"", "").replace("\"]", "").replace("\",\"", "、");
            }

            String jcsj = "";
            String jcjl = "";
            jcsj = map.get("检查所见") == null ? "" : (String) map.get("检查所见");
            if (map.containsKey("TBS诊断")) {
                //&& !((String) map.get("TBS诊断")).endsWith("。")
                jcjl += "TBS诊断:" + map.get("TBS诊断");
                if (!((String) map.get("TBS诊断")).endsWith("。")) {
                    jcjl += "；";
                }

            }
            if (map.containsKey("建议") && (String) map.get("建议") != null && !map.get("建议").equals("")) {
                jcjl += "建议:" + map.get("建议") + "。";
                if (!((String) map.get("建议")).endsWith("。")) {
                    jcjl += "；";
                }
            }
            sendParam.put("repno", report.getReId());
            sendParam.put("logno", mecPatient.get("logno") == null ? "" : mecPatient.get("logno"));
            sendParam.put("patid", mecPatient.get("patNo"));

            Date date02 = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String reprq = sdf.format(date02);
            //
            Integer melib = (Integer) mecPatient.get("melibId");
            if (melib != null) {
                if (melib == 21) {
                    sendParam.put("lbmc", "TCT");
                } else if (melib == 22) {
                    sendParam.put("lbmc", "脱落细胞");
                } else {
                    sendParam.put("lbmc", "病理报告");
                }
            } else {
                sendParam.put("lbmc", "病理报告");
            }
            sendParam.put("reprq", reprq);
            sendParam.put("jcbw", specimenName);
            sendParam.put("jcsj", jcsj);
            sendParam.put("jcjl", jcjl);
            sendParam.put("jcysdm", "");
            sendParam.put("jcysxm", report.getReDoctorName() == null ? user.getFullName() : report.getReDoctorName());
            sendParam.put("jcksdm", "7009");
            sendParam.put("jcksmc", "病理科");
            sendParam.put("shysxm", report.getAuditDoctorName() == null ? user.getFullName() : report.getAuditDoctorName());
            sendParam.put("shysdm", "");
            sendParam.put("wjbz", "0");
            String SyncResult = mecWebservice.TJ_ReleaseRisReport(sendParam,bno,hisId);

            mp.put("data", SyncResult);
        } else {
            Map<String, Object> BG01WebService = hisService.getBG01(report.getReId(),bno,hisId);
            mp.put("data", BG01WebService);
        }
        mp.put("msg", 200);
        walnutLog.setNo(bno);
        walnutLog.setHisId(hisId);
        walnutLog.setMethod("报告推送");
        walnutLog.setReqParam(JSON.toJSONString(param));
        walnutLog.setRespParam(JSON.toJSONString(result));
        walnutLog.setCreatetime(DateUtils.currentTimestampTime());
        walnutLogService.save(walnutLog);
        return mp;
    }

    /**
     * 报告取消
     *
     * @param reId
     * @param userId
     * @return
     * @throws IOException
     */
    public Map<String, Object> cancelThAudit(Integer reId, Integer userId, String hisId) throws IOException {
        Map<String, Object> mp = new ConcurrentHashMap<>(100);
        WalnutLog walnutLog=new WalnutLog();
        Map<String, Object> param=new HashMap<>();
        param.put("reId",reId);
        param.put("当前操作用户ID",userId);
        param.put("hisId",hisId);
        Long now = DateUtils.currentTimestampTime();
        String bno = "C" + MD5util.string2MD5(String.valueOf(now) + UUID.nameUUIDFromBytes(UUID.randomUUID().toString().getBytes()));

        ReportTypeCurrentType reportTypeCurrentType = masterMapper.queryReCurrentTypeByReId(reId);
        if (reportTypeCurrentType.getReType() > 107) {
            SysUser user = masterMapper.selUserInfoById(userId);
            Map<String, Object> mecPatient = masterMapper.selectMecPatientInfoByReId(reId);
            Map<String, Object> sendParam = new ConcurrentHashMap<>(10000);
            Integer patientType = 0;
            if (mecPatient != null && mecPatient.size() > 0) {
                patientType = (Integer) mecPatient.get("patientTtype");
            }
            if (patientType == 503) {
                // 体检走体检的取消报告接口
                sendParam.put("code", mecPatient.get("patNo"));
                sendParam.put("repno", reId);
                String result = mecWebservice.TJ_RetrieveReport(sendParam,bno,hisId);
                if ("T".equals(result)) {
                    System.out.println(result);
                } else {
                }
            } else {
                //同步his取消报告
                BG02 bg02 = hisService.getBG02(reId,bno,hisId);
                if (bg02.getColumn1() != "F" && !"F".equals(bg02.getColumn1())) {
                    System.out.println(bg02);
                } else {
                }
            }
        }
        walnutLog.setNo(bno);
        walnutLog.setHisId(hisId);
        walnutLog.setMethod("取消审核");
        walnutLog.setReqParam(JSON.toJSONString(param));
        walnutLog.setRespParam(JSON.toJSONString(mp));
        walnutLog.setCreatetime(DateUtils.currentTimestampTime());
        walnutLogService.save(walnutLog);
        return mp;
    }

    public void ss() {
        SysUser sysUser = masterMapper.selUserInfoById(10);
        System.out.println(JSONObject.toJSONString(sysUser));

    }


}
