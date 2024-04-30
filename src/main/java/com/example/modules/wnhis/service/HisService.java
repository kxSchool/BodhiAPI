package com.example.modules.wnhis.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.common.api.CommonResult;
import com.example.modules.wnhis.MecWebservice;
import com.example.modules.wnhis.WebServiceUtil;
import com.example.modules.wnhis.pojo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Toomth
 * @date 2021/1/19 14:34
 * @explain
 */
@Service
public class HisService {
    private final Logger logger = LoggerFactory.getLogger(HisService.class);


    @Autowired
    MecWebservice mecWebservice;

    /**
     * 申请病人信息 JB01
     *
     * @param hisId
     * @param brlb
     * @return
     */
    public JB01 getJB01(String hisId, Integer brlb) {
        JB01 jb01 = new JB01();
        try {
            jb01 = getJB01Result(hisId, brlb, "1");
            if (jb01 != null) {
                if ("F".equals(jb01.getColumn1())) {
                    jb01 = getJB01Result(hisId, brlb, "5");
                    if (jb01 != null) {
                        if ("F".equals(jb01.getColumn1())) {
                            jb01 = getJB01Result(hisId, brlb, "2");
                            if (jb01 != null) {
                                if ("F".equals(jb01.getColumn1())) {
                                    jb01 = getJB01Result(hisId, brlb, "3");
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

    public JB01 getJB01Result(String hisId, Integer brlb, String codeType) throws IOException {
        JB01 jb01 = new JB01();
        String sendXML;
        Map<String, Object> webServiceData;
        Map<String, Object> send = new HashMap<>();
        send.put("MsgCode", "JB01");
        sendXML = "<codetype>" + codeType + "</codetype><code>" + hisId + "</code><brlb>" + brlb + "</brlb>";
        send.put("SendXml", sendXML);
        webServiceData = WebServiceUtil.getWebService(send);
        jb01 = JSON.parseObject((String) webServiceData.get("data"), JB01.class);
        return jb01;
    }

    /**
     * 申请病人列表 jb02
     *
     * @param queryParam
     * @throws IOException
     */
    public List<JB02> getJB02(Map<String, Object> queryParam) throws IOException {

        Map<String, Object> sendParam = new ConcurrentHashMap<>();
        sendParam.put("MsgCode", "JB02");
        String sendXml = "<ksrq>" + queryParam.get("ksrq") + "</ksrq><jsrq>" + queryParam.get("jsrq") + "</jsrq>" +
                "<zxks>" + queryParam.get("zxks") + "</zxks>" +
                "<brlb>" + queryParam.get("brlb") + "</brlb><blh></blh><fph>0</fph>" +
                "<codetype>" + queryParam.get("codetype") + "</codetype>";
        sendParam.put("SendXml", sendXml);

        Map<String, Object> webServiceData = WebServiceUtil.getWebService(sendParam);
        List<JB02> jb02List = new ArrayList<>();
        if (webServiceData != null) {
            jb02List = (List<JB02>) webServiceData.get("data");

        }
        return jb02List;
    }

    /**
     * 申请项目信息 JB03
     *
     * @param param jb01对象
     * @param brlb  病人类别
     * @return
     */
    public List<JB03> getJB03(JB01 param, Integer brlb) {
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
            webServiceData = WebServiceUtil.getWebService(send);
            jb03List = (List<JB03>) webServiceData.get("data");
//            System.out.println(jb03List.get(0));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("提取JB03", e);
        }
        return jb03List;
    }

    /**
     * 电子申请单列表SQ03
     *
     * @param param JB01 对象
     * @param brlb  病人类别
     * @return
     */
    public List<SQ03> getSQ03(JB01 param, Integer brlb) {
        List<SQ03> sq03List = new ArrayList<>();
        String sendXML;
        Map<String, Object> webServiceData;
        Map<String, Object> send = new HashMap<>();
        try {
            send.put("MsgCode", "SQ03");
            sendXML = "<brlb>" + brlb + "</brlb><cardtype>0</cardtype><card>" + param.getHospNo() + "</card>";
            send.put("SendXml", sendXML);
            webServiceData = WebServiceUtil.getWebService(send);
            sq03List = (List<SQ03>) webServiceData.get("data");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("提取SQ03", e);
        }
        return sq03List;
    }

    /**
     * 申请单明细 SQ02
     *
     * @param param SQ03 对象
     * @param brlb  病人类别
     * @return
     */
    public List<SQ02> getSQ02(SQ03 param, Integer brlb) {
        List<SQ02> sq02List = new ArrayList<>();
        String sendXML;
        Map<String, Object> webServiceData;
        Map<String, Object> send = new HashMap<>();
        try {
            send.put("MsgCode", "SQ02");
            sendXML = "<brlb>" + brlb + "</brlb><sqdxh>" + param.getSqdxh() + "</sqdxh>";
            send.put("SendXml", sendXML);
            webServiceData = WebServiceUtil.getWebService(send);
            sq02List = (List<SQ02>) webServiceData.get("data");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("提取SQ03", e);
        }
        return sq02List;
    }

    public List<SQ02> getSQ02(String applyNo, Integer brlb) {
        List<SQ02> sq02List = new ArrayList<>();
        String sendXML;
        Map<String, Object> webServiceData;
        Map<String, Object> send = new HashMap<>();
        try {
            send.put("MsgCode", "SQ02");
            sendXML = "<brlb>" + brlb + "</brlb><sqdxh>" + applyNo + "</sqdxh>";
            send.put("SendXml", sendXML);
            webServiceData = WebServiceUtil.getWebService(send);
            sq02List = (List<SQ02>) webServiceData.get("data");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("提取SQ03", e);
        }
        return sq02List;
    }

    /**
     * 报告回收 BG02
     *
     * @param reId
     * @return
     * @throws IOException
     */
    public BG02 getBG02(Integer reId) throws IOException {
        Map<String, Object> sendParam = new ConcurrentHashMap<>(10000);
        BG02 bg02 = new BG02();
        sendParam.put("MsgCode", "BG02");
        String SendXML = "<repno>" + reId + "</repno><replb>bl</replb><hslb>0</hslb>";
        sendParam.put("SendXml", SendXML);
        Map<String, Object> BG02WebService = WebServiceUtil.getWebService(sendParam);
        if ((Integer) BG02WebService.get("code") == 200) {
            bg02 = JSON.parseObject((String) BG02WebService.get("data"), BG02.class);
        }
        return bg02;

    }

    /**
     * 报告发布 BG01   现用
     *
     * @param bg01
     * @return
     * @throws IOException
     */
    public Map<String, Object> getBG01(BG01 bg01) throws IOException {
        Map<String, Object> sendParam = new ConcurrentHashMap<>(10000);
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
//        System.out.println(SendXML);
        Map<String, Object> BG01WebService = WebServiceUtil.getWebService(sendParam);
        BG01WebService.put("name", "bg01");
        return BG01WebService;
    }

    /**
     * 报告结果回传 BG03
     *
     * @param reId
     * @param template
     * @param reContent
     * @return
     * @throws IOException
     */
    public BG03 getBG03(Integer reId, String template, String reContent, BG01 bg01) throws IOException {
        Map<String, Object> sendParam = new ConcurrentHashMap<>(10000);
        BG03 bg03 = new BG03();
        String SendXML;
        Map<String, Object> BG01WebService = getBG01(bg01);
        if ((Integer) BG01WebService.get("code") == 200) {
            BG01 bg = JSON.parseObject((String) BG01WebService.get("data"), BG01.class);
            if (bg.getColumn1() != "F" && !"F".equals(bg.getColumn1())) {
                sendParam.put("MsgCode", "BG03");
                Map<String, Object> map = getReport(template, reContent);
                int a = 1;
                for (String s : map.keySet()) {
                    SendXML = "<repno>" + reId + "</repno><replb>bl</replb><xmdm>0006</xmdm><xmmc>" + s + "</xmmc><jgckz>0</jgckz><qqmxxh>0</qqmxxh><xmjg>" + map.get(s) + "</xmjg><xmdw></xmdw><xjbz>0</xjbz><xjmc></xjmc><kssmc></kssmc><gmjg></gmjg><jgxh>" + a + "</jgxh><gdbz></gdbz><crbz></crbz>";
                    sendParam.put("SendXml", SendXML);
                    Map<String, Object> BG03WebService = WebServiceUtil.getWebService(sendParam);
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

    /**
     * 发送体检报告
     * @param reId
     * @param template
     * @param reContent
     * @param bg01
     * @param mecPatient
     * @return
     */
    public CommonResult getTJBg(Integer reId, String template, String reContent, BG01 bg01, Map<String, Object> mecPatient) {
        CommonResult result = new CommonResult();
        Map<String, Object> sendParam = new ConcurrentHashMap<>(10000);
        Map<String, Object> map = getReport(template, reContent);
        try {
            String specimenName = "";
            if (mecPatient.get("specimenName") != null) {
                specimenName = (String) mecPatient.get("specimenName");
                specimenName = specimenName.replace("[\"", "").replace("\"]", "").replace("\",\"", "、");
            }

            String jcsj = "";
            String jcjl = "";
            jcsj = map.get("检查所见") == null ? "" : (String) map.get("检查所见");
            if (map.containsKey("TBS诊断")) {
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
            sendParam.put("repno", reId);
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
            sendParam.put("jcysxm", mecPatient.get("reDoctorName"));
            sendParam.put("jcksdm", "7009");
            sendParam.put("jcksmc", "病理科");
            sendParam.put("shysxm", mecPatient.get("auditDoctorName"));
            sendParam.put("shysdm", "");
            sendParam.put("wjbz", "0");
            String SyncResult = mecWebservice.TJ_ReleaseRisReport(sendParam);
            result.setData(SyncResult);
        } catch (Exception e) {
            e.printStackTrace();
            result.setMessage("提交体检报告失败");
            result.setCode(-1);
        }
        result.setMessage("提交体检报告成功");
        result.setCode(0);
        return result;
    }


    //提取报告类容
    public Map<String, Object> getReport(String reportType, String reportContents) {
        String reportContent = reportContents.replace("<", "小于").replace(">", "大于");
        Map<String, Object> report = new HashMap<>();
        Map<String, Object> report02 = new HashMap<>();
        switch (reportType) {
            case "normal":
                report = JSON.parseObject(reportContent, Map.class);
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
                String ss = "";
                for (String s : GSTSDetail.keySet()) {
                    if (label02.contains(s)) {
                        if (!"".equals(GSTSDetail.getString(s)) && GSTSDetail.getString(s) != null) {
                            report02.put(s, GSTSDetail.get(s));
                        }
                    } else {
                        if (!"".equals(GSTSDetail.getString(s)) && GSTSDetail.getString(s) != null) {
                            ss += s + ":" + GSTSDetail.get(s) + "；";
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
        return report02;
    }

    public List<HisYy01> getHISYY01() throws IOException {
        Map<String, Object> sendParam = new ConcurrentHashMap<>();
        sendParam.put("MsgCode", "YY01");
        Map<String, Object> webServiceData = WebServiceUtil.getWebService(sendParam);
        if (webServiceData != null) {
            @SuppressWarnings("unchecked")
            List<HisYy01> result=new ArrayList<>();
            List<YY01> yy01List = (List<YY01>) webServiceData.get("data");
            for (YY01 pa : yy01List) {
                HisYy01 temp=new HisYy01();
                temp.setCode(pa.getId());
                temp.setName(pa.getName());
                temp.setPy(pa.getPy());
                temp.setWb(pa.getWb());
                result.add(temp);
            }
            return  result;
        }
        return null;
    }
}
