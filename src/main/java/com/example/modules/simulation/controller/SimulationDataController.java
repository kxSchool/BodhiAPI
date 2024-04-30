package com.example.modules.simulation.controller;

import com.example.modules.simulation.service.SimulationDataService;
import com.example.modules.simulation.utils.PatientAPIUtils;
import com.example.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/simulation")
public class SimulationDataController {

    @Value("${shenkang.hospitalCode}")
    private String hospitalCode;

    @Value("${shenkang.hospitalName}")
    private String hospitalName;

    @Autowired
    private SimulationDataService simulationDataService;

    @RequestMapping(value="/tokenSimulationDatas")
    @ResponseBody
    public Map<String,Object> jsonToken(){
        Map<String,Object>  map =new HashMap();
        map.put("access_token","VXdBQEpkUdpN5-gWtr9rXeFTmnmxjGvM4KImI9ODoiMQ7IoJ6-hbH4W Bh_kHZBHYly0ax-DjsW5HLI6YcCORkuYSIXh_XD9D_UfqgQTERu_0SCCCxwuppfxLw0_w QMthE6yuhMagNgXjWH8XPxZQ1m6qKD_kOnz-p2W6brANQdn6SRbwYNMZrfjrnh2_EeXY 47C-EpiOA0RO8uXK5qvJNdpYe547Eg4oEeP7RHVuV3HeVP1rEQ5y8koOei3lhtbuefoUZpYup SzNoj1shzbqiLCvALck0KgXup-OcLo1c4qJFeUs");
        map.put("token_type","bearer");
        map.put("expires_in","604799");
        map.put("refresh_token","d091a527e6ac4c3d88b2b525533fb88f5d44f64e1b2f46c8918c9dd85e58128b");
        return map;
    }
    /**
     * @author: 邵梦丽 on 2020/12/21 13:45
     * @param:
     * @return:
     * @Description:万达接口病理信息返回模拟数据
     */
    @RequestMapping(value="/registerRspSimulationDatas")
    @ResponseBody
    public Map<String,Object> jsonRspRegister(){
        Map<String,Object>  map =new HashMap();
        long t = System.currentTimeMillis();
        Random random=new Random(t);
        int ra=random.nextInt(10)+1;
        if (ra<=8){
            map.put("code",0);
            map.put("msg","OK");
            map.put("data","822573c3-75f6-4522-8ca9-7847c84f73c3");
        }else{
            map.put("code",-1);
            map.put("data","00000000-0000-0000-0000-000000000000");
            map.put("msg","上传患者病理信息失败，患者信息未注册。");
        }
        return map;
    }

    /**
     * @author: 邵梦丽 on 2020/12/21 13:45
     * @param:
     * @return:
     * @Description:文件校验返回模拟数据
     */
    @RequestMapping(value="/filesCheckRspSimulationDatas")
    @ResponseBody
    public Map<String,Object> jsonRspFilesCheck(){
        Map<String,Object>  map =new HashMap();
        long t = System.currentTimeMillis();
        Random random=new Random(t);
        int ra=random.nextInt(10)+1;
        if (ra<=8){
            map.put("code",0);
            map.put("msg","OK");
            List data=new ArrayList();
            map.put("data",data);
        }else{
            map.put("code",-1);
            map.put("data","00000000-0000-0000-0000-000000000000");
            map.put("msg","失败....");
        }
        return map;
    }

    /**
     * @author:
     * @param:
     * @return:
     * @Description:万达接口患者信息返回模拟数据
     */
    @RequestMapping(value="/patientRspSimulationDatas")
    @ResponseBody
    public Map<String,Object> jsonRspPatients(){
        Map<String,Object>  map =new HashMap();
        long t = System.currentTimeMillis();
        Random random=new Random(t);
        int ra=random.nextInt(10)+1;
        if (ra<=8){
            map.put("code",0);
            map.put("msg","OK");
            Map<String,Object>  data =new HashMap();
            data.put("patient_id","40928035");
            data.put("pid_assigning_authority","42507230900.40928035");
            data.put("patient_master_id","e6723c55-7f8e-4c7a-9621-ce229c98172a");
            map.put("data",data);
        }else{
            map.put("code",70001);
            map.put("data",null);
            map.put("msg","Fail");
        }
        return map;
    }

    /**
     * @author: 邵梦丽 on 2020/12/21 13:45
     * @param:
     * @return:
     * @Description:万达接口文件上传返回模拟数据
     */
    @RequestMapping(value="/filesRspSimulationDatas")
    @ResponseBody
    public Map<String,Object> jsonRspFiles(){
        Map<String,Object>  map =new HashMap();
        map.put("ThumbnailViewUrl","");
        map.put("OriginalFileName","P20-2016A.svs");
        map.put("FileDownloadUrl","http://200.200.200.12:8005/DocumentService/Download/Document/?fileUid=95282086-f45f-48ed-bf9d-61a69bfff0b1&pages=&preferredContentType=&token=f43bc4d739ac8798b38ca16bbb121057");
        map.put("BusinessType","Exam");
        map.put("MimeType","multipart/form-data");
        map.put("FileUID","95282086-f45f-48ed-bf9d-61a69bfff0b1");
        map.put("FileBusinessViewUrl","http://200.200.200.12:8005/DocumentService/Viewer?businessID=568307a6-dfd0-4f5c-8acb-c697f24ad910&businessType=Exam&classCode=Exam&typeCode=ExamImage&fileUid=95282086-f45f-48ed-bf9d-61a69bfff0b1&print=0&defaultPrintPageSize=&download=0&token=92507861c0045c0ff921d37e103a8345&businessView=true");
        map.put("FileCreateTime","/Date(1600913023682)/");
        map.put("SlideAssoViewUrl","");
        map.put("UploadTime","/Date(1600913023682)/");
        map.put("CreateOrganizationID","42507230900");
        map.put("TypeCode","ExamImage");
        map.put("FileViewUrl","http://200.200.200.12:8005/DocumentService/Viewer?businessID=568307a6-dfd0-4f5c-8acb-c697f24ad910&businessType=Exam&classCode=Exam&typeCode=ExamImage&fileUid=95282086-f45f-48ed-bf9d-61a69bfff0b1&print=0&defaultPrintPageSize=&download=0&token=92507861c0045c0ff921d37e103a8345&businessView=false\"");
        map.put("Title","P20-2016A");
        map.put("FileSHA","6412e7a58252b989353377b776f72545a2ee39f6");
        map.put("ClassCode","Exam");
        map.put("FormatCode","SECTION");
        map.put("BusinessTime","/Date(1600912968000)/");
        map.put("BusinessID","568307a6-dfd0-4f5c-8acb-c697f24ad910");
        map.put("FileDeleteUrl","http://200.200.200.12:8005/DocumentService/Delete/Document?fileUid=95282086-f45f-48ed-bf9d-61a69bfff0b1&token=f43bc4d739ac8798b38ca16bbb121057");
        map.put("FileSize","167403KB");
        return map;
    }

    @RequestMapping(value="/aiSimulationDatasJson")
    @ResponseBody
    public Map<String,Object> jsonAIs(@RequestParam(value="pathNo",required=false) String pathNo,@RequestParam(value="slideNo",required=false) String slideNo){
        Map<String,Object> map=new HashMap<>();
        map.put("pathNo",pathNo);
        map.put("slideNo",slideNo);
        map=jsonAIs(map);
        return map;
    }

    /**
     * @author:
     * @param:
     * @return:
     * @Description:ai欺骗信息模拟数据
     */
    @RequestMapping(value="/aiSimulationDatas")
    @ResponseBody
    public Map<String,Object> jsonAIs(@RequestBody Map<String, Object> param){
        Map<String,Object>  map =new HashMap();
        Map<String,Object>  map2 =new HashMap();
        map.put("Code","0");
        map.put("Msg","查询成功");
        String pathNo=DateUtils.getStringParam(param,"pathNo");
        long t = System.currentTimeMillis();
        Random random=new Random(t);
        int ra=random.nextInt(10)+1;
        if(ra>8){
            map.put("Code","-1");
            map.put("Msg","输入参数异常");
            map.put("data","");
            map2.put("Response",map);
            return map2;
        }

        map.put("pathNo",pathNo);
        map.put("patientId","64719562");
        map.put("outpatientNo","64719562");
        map.put("inpatientNo","430263");
        map.put("patientType","住院");
        map.put("patientName","刘");
        map.put("patientSex","男");
        map.put("patientAge","4岁");
        map.put("IDCard","");
        map.put("inpatientWard","耳鼻咽喉科病区");
        map.put("bedID","19");
        map.put("sendHospitalName","本院");
        map.put("sendDeptName","耳鼻咽喉科病区");
        map.put("sendDcotorName","李瑾");
        map.put("clinicDiagnosis","阻塞性睡眠呼吸暂停综合征");
        map.put("applyDate","2020-09-18 00,00,00");
        map.put( "status","已审核");
        map.put("generalObservation","A“双扁”灰白色组织两块，2*1.5*1.2厘米、2*1.2*1.2厘米，切面灰白色。B“腺样体”灰白色组织一块，0.4厘米。");
        map.put("examRecord","");
        map.put("examDiagnosis","(双扁桃体)慢性炎，肥大。（腺样体）慢性炎。请结合临床。");
        map.put("examTime","2020-09-21 00,00,00");
        map.put("examDocName","何巧");
        List waxInfos=new ArrayList();
        Map<String,String> waxInfo=new HashMap<>();
        waxInfo.put( "waxType","正常");
        waxInfo.put("waxNo",pathNo+"-A");
        waxInfo.put("embedding","沈");
        waxInfo.put("creatorTime","2020-09-21 08:33");
        waxInfo.put("SpecimentPart","");
        waxInfo.put("MaterialsDoctor","何");
        waxInfo.put("MaterialsDate","2020-09-18 14:18");
        waxInfo.put("registrar","金");
        waxInfo.put( "remark","");
        waxInfos.add(waxInfo);
        waxInfo=new HashMap<>();
        waxInfo.put("waxType","正常");
        waxInfo.put( "waxNo",pathNo+"-B");
        waxInfo.put("embedding","沈");
        waxInfo.put("creatorTime","2020-09-21 08:33");
        waxInfo.put("SpecimentPart","");
        waxInfo.put("MaterialsDoctor","何");
        waxInfo.put("MaterialsDate","2020-09-18 14:18");
        waxInfo.put("registrar","金");
        waxInfo.put( "remark","");
        waxInfos.add(waxInfo);
        map.put( "waxInfo",waxInfos);

        List slideInfos=new ArrayList();
        Map<String,String> slideInfo=new HashMap<>();
        slideInfo.put("slideType","正常");
        slideInfo.put("slideNo","00084541");
        slideInfo.put("remark","A");
        slideInfo.put("slideAdvice","沈");
        slideInfo.put("creatorTime","2020-09-21 08:35");
        slideInfo.put("waxNo",pathNo+"-A");
        slideInfos.add(slideInfo);

        slideInfo=new HashMap<>();
        slideInfo.put("slideType","正常");
        slideInfo.put("slideNo","00084542");
        slideInfo.put("remark","B");
        slideInfo.put("slideAdvice","沈");
        slideInfo.put("creatorTime","2020-09-21 08:35");
        slideInfo.put("waxNo",pathNo+"-A");
        slideInfos.add(slideInfo);

        slideInfo=new HashMap<>();
        slideInfo.put("slideType","分子病理");
        slideInfo.put("slideNo","00084574");
        slideInfo.put("remark","EBER");
        slideInfo.put("slideAdvice","沈");
        slideInfo.put("creatorTime","2020-09-21 08:35");
        slideInfo.put("waxNo",pathNo+"-A");
        slideInfos.add(slideInfo);

        map.put("slideInfo",slideInfos);
        map2.put("Response",map);
        return map2;
    }

    @RequestMapping(value="/patientSimulationDatasJson")
    @ResponseBody
    public Map<String,Object> jsonPatients(@RequestParam(value="updateDateTime",required=false) String updateDateTime, @RequestParam(value="pathNo",required=false) String pathNo) {
        Map<String,Object> map=new HashMap<>();
        map.put("updateDateTime",updateDateTime);
        map.put("pathNo",pathNo);
        map=jsonPatients(map);
        return map;
    }

    @RequestMapping(value="/registerSimulationDatasJson")
    @ResponseBody
    public Map<String,Object> jsonRegister(@RequestParam(value="updateDateTime",required=false) String updateDateTime, @RequestParam(value="pathNo",required=false) String pathNo) {
        Map<String,Object> map=new HashMap<>();
        map.put("updateDateTime",updateDateTime);
        map.put("pathNo",pathNo);
        map=jsonRegister(map);
        return map;
    }

    /**
     * @author:
     * @param:
     * @return:
     * @Description:病理信息模拟数据
     */
    @RequestMapping(value="/registerSimulationDatas")
    @ResponseBody
    public Map<String,Object> jsonRegister(@RequestBody Map<String, Object> param){
        String updateDateTime=DateUtils.getStringParam(param,"updateDateTime");
        String pathNo=DateUtils.getStringParam(param,"pathNo");
        Map<String,Object>  map =new HashMap();
        Map<String,Object>  map2 =new HashMap();
        map.put("Code","0");
        map.put("Msg","查询成功");
        if(updateDateTime==null){
            if(pathNo==null || pathNo.length()==0 ){
                map.put("Code","-1");
                map.put("Msg","输入参数异常");
                map.put("data","");
                map2.put("Response",map);
                return map2;
            }
        }else{
            if(updateDateTime.equals("1991-01-01"))
            {
                map.put("Code","-1");
                map.put("Msg","输入参数异常");
                map.put("data","");
                map2.put("Response",map);
                return map2;
            }
        }
        List<Map<String,Object>> registerInfoList=new LinkedList<>();
        if(pathNo!=null){
            registerInfoList.add(simulationDataService.setRegisterMap("40936793", "", "02", "0",
                    "", "40935793", "437609", "", "",
                    hospitalCode, hospitalName, "33778224", pathNo,
                    "01", "常规病理", "", "-",
                    "", "徐", "", "",
                    "普外病区", "2020-11-19 00:00:00", hospitalCode, hospitalName,
                    "", "", "左肘部肿块 急淋复发 皮下肿物", "",
                    "", "", "", "",
                    "", "", "", "2020-11-19 00:00:00",
                    "", "", "报告已发布", "4040",
                    "2020-11-19 00:00:00", "", "", "2020-11-19 00:00:00",
                    "", "何", "", "",
                    "（左肘部肿瘤）灰白灰褐色碎组织一堆，共3*1.5*0.5厘米，其中最大一块3*1*0.5厘米，表面未见明显包膜。", "", "", "",
                    "2020-11-20 00:00:00", "3272", "张", "3272",
                    "张", "2020-11-23 13:56:31", "", "",
                    "", "（左肘部肿块）符合B淋巴细胞肿瘤浸累。", "", "",
                    "", "2020-11-23 13:56:31", pathNo, null
            ));
            map.put("data",registerInfoList);
            map2.put("Response",map);
            return map2;
        }
        String[] send_dept_name=new String[]{"皮肤门诊","泌尿外科病区","普外病区"};
        String[] dateTimes=new String[]{"2020-11-23 13:57:58","2020-11-23 13:59:32","2020-12-13 14:59:32"};
        String[] clinic_diagnosis=new String[]{"左肘部肿块 急淋复发 皮下肿物","肛门前移位","肝母细胞瘤",
                "颈淋巴结肿大 活检术后","睾丸附件扭转","左肾盂积水","腹痛 阑尾炎可能","右拇指 多指\\\\[趾\\\\]畸形",
                "舌尖部囊肿 舌肿物"};
        String[] names=new String[]{"赵","钱","孙","李","周","吴","郑","王","冯","陈","褚","卫","蒋","沈","韩","杨","朱","秦","尤许何","吕","施","张","孔","曹","严","华","金","魏","陶","姜"};
        String[] autopsy=new String[]{"（左肘部肿瘤）灰白灰褐色碎组织一堆，共3*1.5*0.5厘米，其中最大一块3*1*0.5厘米，表面未见明显包膜。",
                "（直肠粘膜）灰白色组织一块，0.3厘米。",
                "（肝脏肿瘤）灰褐灰黄色碎组织一堆，2*1.5*0.6厘米，其中最大一块1*0.6*0.5厘米，表面部分有包膜。",
                "（左颈淋巴结）灰褐色组织两块，2*1.2*1厘米，2*1.5*1.5厘米，表面部分有包膜，切面灰白色，质中。",
                "（背部）皮肤组织一块，0.3*0.3*0.2厘米，皮肤表面见灰褐色小点两处。",
                "左睾丸附件”灰白色组织一小块，0.2cm。",
                "左UPJ”漏斗样组织一块，长1.5cm，直径0.1-0.2cm，探针可通过。",
                "阑尾一根，长7厘米，直径0.8厘米，浆膜充血。",
                "右手拇指赘生物”带皮组织一块，长1*0.7*0.5cm。",
                "舌囊肿”不规则组织一块，2*1*0.5cm。"};
        String[] pathological_diagnosis=new String[]{"（左肘部肿块）符合B淋巴细胞肿瘤浸累。",
                "（直肠粘膜）活检，见到粘膜下神经节细胞，发育较差。",
                "（肝脏）切除标本，符合混合性上皮和间叶型肝母细胞瘤。",
                "（左颈）淋巴结，B细胞性淋巴瘤，见多灶性浆细胞淋巴瘤图像。\\r\\n请结合临床及原初病理。",
                "（背部）皮肤活检，血管淋巴管瘤。",
                "（左睾丸附件）示小块纤维组织，见内衬柱状上皮腔隙，伴变性。\\r\\n\\r\\n请结合临床。",
                "因（左肾积水）行（左肾盂、输尿管交界处）切除标本：\\r\\n粘膜被覆移行上皮，部分脱落，间质血管扩张、淤血，伴炎细胞浸润。请结合临床。",
                "急性化脓性阑尾炎，阑尾周围炎。",
                "（右手拇指赘生物）符合多指。\\r\\n\\r\\n请结合临床。",
                "（舌囊肿）送检组织被覆复层扁平上皮，内见内衬柱状上皮的腔隙，间质血管扩张、淤血，伴炎细胞浸润。见少量涎腺组织。请结合临床。"};

        registerInfoList.add(simulationDataService.setRegisterMap("40936793", "", "02", "0",
                "", "40935793", "437609", "", "",
                hospitalCode, hospitalName, "33778224", "P20-6205",
                "01", "常规病理", "", "-",
                "", "徐", "", "",
                "普外病区", "2020-11-19 00:00:00", hospitalCode, hospitalName,
                "", "", "左肘部肿块 急淋复发 皮下肿物", "",
                "", "", "", "",
                "", "", "", "2020-11-19 00:00:00",
                "", "", "报告已发布", "4040",
                "2020-11-19 00:00:00", "", "", "2020-11-19 00:00:00",
                "", "何", "", "",
                "（左肘部肿瘤）灰白灰褐色碎组织一堆，共3*1.5*0.5厘米，其中最大一块3*1*0.5厘米，表面未见明显包膜。", "", "", "",
                "2020-11-20 00:00:00", "3272", "张", "3272",
                "张", "2020-11-23 13:56:31", "", "",
                "", "（左肘部肿块）符合B淋巴细胞肿瘤浸累。", "", "",
                "", "2020-11-23 13:56:31", "P20-6205", null
        ));
        registerInfoList.add(simulationDataService.setRegisterMap(
                "63795982", "", "02", "0", "",
                "63794992", "437022", "", "",
                hospitalCode, hospitalName, "33777530", "P20-6207",
                "01", "常规病理", "", "-",
                "", "褚", "", "",
                "普外病区", "2020-11-19 00:00:00", hospitalCode, hospitalName,
                "", "", "肛门前移位", "",
                "", "", "", "",
                "", "", "", "2020-11-19 00:00:00",
                "", "", "报告已发布", "4040",
                "2020-11-19 00:00:00", "", "", "2020-11-19 00:00:00",
                "", "何", "", "",
                "（直肠粘膜）灰白色组织一块，0.3厘米。", "", "", "",
                "2020-11-20 00:00:00", "3819", "马", "3272",
                "张", "2020-11-23 13:56:59", "", "",
                "", "（直肠粘膜）活检，见到粘膜下神经节细胞，发育较差。", "", "",
                "", "2020-11-23 13:56:31", "P20-6207", null
        ));
        registerInfoList.add(simulationDataService.setRegisterMap(
                "64754062", "", "02", "0",
                "", "64754072", "437329", "", "",
                hospitalCode, hospitalName, "33777994", "P20-6208",
                "01", "常规病理", "", "-",
                "", "徐", "", "",
                "普外病区", "2020-11-19 00:00:00", hospitalCode, hospitalName,
                "", "", "肝母细胞瘤", "",
                "", "", "", "",
                "", "", "", "2020-11-19 00:00:00",
                "", "", "报告已发布", "4040",
                "2020-11-19 00:00:00", "", "", "2020-11-19 00:00:00",
                "", "何", "", "",
                "（肝脏肿瘤）灰褐灰黄色碎组织一堆，2*1.5*0.6厘米，其中最大一块1*0.6*0.5厘米，表面部分有包膜。", "", "", "",
                "2020-11-20 00:00:00", "3272", "张", "3272",
                "张", "2020-11-23 13:57:21", "", "",
                "", "（肝脏）切除标本，符合混合性上皮和间叶型肝母细胞瘤。", "", "",
                "", "2020-11-23 13:57:21", "P20-6208", null
        ));
        for(int i=0;i<300;i++){
            registerInfoList.add(simulationDataService.setRegisterMap(
                    "X20-0"+i, "", "02",
                    "0", "med_rec_no"+i, "64754072", "437329",
                    "JZLSH"+i, "order_uid"+i, hospitalCode, hospitalName,
                    "33777994", "P20-7"+i, "01", "常规病理",
                    "procedure_id", "-", "sen_doctor_id"+i, names[i%names.length],
                    "send_doctor_phone"+i, "send_dept_id"+i, send_dept_name[i%send_dept_name.length], dateTimes[i%dateTimes.length],
                    hospitalCode, hospitalName, "symptom"+i, "adverse_reaction"+i, clinic_diagnosis[i%clinic_diagnosis.length],
                    "surgery_info"+i, "relevant_clinical_info"+i, "collector_id"+i, "collercot_name"+i,
                    "collection_dept"+i, "collection_method"+i, "collection_volume"+i,
                    "111", dateTimes[i%dateTimes.length],
                    "specimen_received"+i, "specimen_received_name"+i,
                    "报告已发布", "4040",
                    dateTimes[i%dateTimes.length], "111", "register_name"+i, dateTimes[i%dateTimes.length],
                    "based_id"+i, names[(i+1)%names.length], "based_record_id"+i, "based_record_name"+i,
                    autopsy[i%autopsy.length], "123", "production_id"+i, "production_name"+i,
                    dateTimes[i%dateTimes.length], "3272", names[(i+2)%names.length], "3272",
                    names[(i+3)%names.length], dateTimes[i%dateTimes.length], "abnormal_flags"+i, "critical_value"+i,
                    "scopically_seen"+i, pathological_diagnosis[i%pathological_diagnosis.length],
                    "tumor_classification"+i, "cancer_classification"+i,
                    "cancer_staging"+i, dateTimes[i%dateTimes.length], "P20-7"+i, null
            ));
        }
        map.put("data",registerInfoList);
        map2.put("Response",map);
        return map2;
    }

    /**
     * @author:
     * @param:
     * @return:
     * @Description:患者信息模拟数据
     */
    @RequestMapping(value="/patientSimulationDatas")
    @ResponseBody
    public Map<String,Object> jsonPatients(@RequestBody Map<String, Object> param){
        List<Map<String,String>> patientInfoList=new LinkedList<>();
        Map<String,Object>  map =new HashMap();
        Map<String,Object>  map2 =new HashMap();
        String updateDateTime=DateUtils.getStringParam(param,"updateDateTime");
        String pathNo= DateUtils.getStringParam(param,"pathNo");
        if(updateDateTime==null){
            if(pathNo==null || pathNo.length()==0 ){
                map.put("Code","-1");
                map.put("Msg","输入参数异常");
                map.put("data","");
                map2.put("Response",map);
                return map2;
            }
        }else{
            if(updateDateTime.equals("1991-01-01"))
            {
                map.put("Code","-1");
                map.put("Msg","输入参数异常");
                map.put("data","");
                map2.put("Response",map);
                return map2;
            }
        }

        map.put("Code","0");
        map.put("Msg","查询成功");
        if(pathNo!=null){
            patientInfoList.add(simulationDataService.setPatientsMap("42507231900","40936793","40935893",
                    "0","钱","","","1","",
                    "","","","","1",
                    "000000000000000000","","","","","","",
                    "","","","","","","","",
                    "2020-11-23 13:56:31",pathNo));
            map.put("data",patientInfoList);
            map2.put("Response",map);
            return map2;
        }
        String[] namespells=new String[]{"Zhao","Qian","Sun","Li","Zhou","Wu","Zheng","Wang","Feng","Chen","Chu","Wei","Jiang","Shen","Han","Yang","Zhu","Qin","You","Xu","He","Lu","Shi","Zhang","Koǒng","Cao","Yan","Hua","Jin","Wei","Tao","Jiang"};
        String[] names=new String[]{"赵","钱","孙","李","周","吴","郑","王","冯","陈","褚","卫","蒋","沈","韩","杨","朱","秦","尤许何","吕","施","张","孔","曹","严","华","金","魏","陶","姜"};
        String[] nations=new String[]{"汉","回","藏"};
        String[] maritals=new String[]{"1","2","3","4","9"};
        String[] identityTypes=new String[]{"1","2","3","4","5","6","7","9"};
        String[] id_card_nos=new String[]{"110101199003072914","11010119900307547X","11010119900307387X","110101199003072535","110101199003077950","110101201103074933","110101201103071150","110101201103070393"};
        String[] identityIDs=new String[]{"000000000000000000","111111111111111111"};
        String[] phones=new String[]{"15996242900","15996242901","15996242903"};
        String[] emails=new String[]{"@qq.com","@163.com"};
        String[] provinces=new String[]{"江苏省","上海市","安徽省"};
        String[] postalcodes=new String[]{"210000","200000"};
        String[] updateDateTimes=new String[]{"2020-11-23 13:57:58","2020-11-23 13:59:32"};

        patientInfoList.add(simulationDataService.setPatientsMap("42507231900","40936793","40935893",
                "0","钱","","","1","",
                "","","","","1",
                "000000000000000000","","","","","","",
                "","","","","","","","",
                "2020-11-23 13:56:31","P20-6205"));
        patientInfoList.add(simulationDataService.setPatientsMap("42507260900","63795982","63794782","0",
                "骆琦","","","2",
                "","","","","","1","000000000000000000",
                "","","","","","","","",
                "","","","","","","2020-11-23 13:56:59","P20-6207"));
        patientInfoList.add(simulationDataService.setPatientsMap("42507230900","64754062","64754062","0",
                "罗耀强","","","1",
                "","","","","","1","000000000000000000",
                "","","","","","","","",
                "","","","","","","2020-11-23 13:57:21","P20-6208"));
        for(int i=0;i<300;i++){
            Map<String,String> patientInfo=simulationDataService.setPatientsMap(hospitalCode,
                    "X20-0"+i,
                    "64692"+i,
                    ""+i%9,
                    names[i%names.length],
                    namespells[i%namespells.length],
                    names[i%names.length]+"妈妈",
                    ""+i%3,
                    PatientAPIUtils.getDelayFormatday(i),
                    names[i%names.length]+"的出生地",
                    nations[i%nations.length],
                    "中国",
                    maritals[i%maritals.length],
                    identityTypes[i%identityTypes.length],
                    id_card_nos[i%id_card_nos.length],
                    identityIDs[i%identityIDs.length],
                    identityIDs[i%identityIDs.length]+i,
                    phones[i%phones.length],
                    namespells[i%namespells.length]+i+emails[i%emails.length],
                    provinces[i%provinces.length],
                    provinces[i%provinces.length]+"城市",
                    provinces[i%provinces.length]+i+"县",
                    provinces[i%provinces.length]+i+"县"+i+"街道",
                    provinces[i%provinces.length]+i+"县"+i+"街道"+i+"路",
                    provinces[i%provinces.length]+i+"县"+i+"街道"+i+"路"+i+"号",
                    postalcodes[i%postalcodes.length],
                    "职业"+i,
                    "单位"+i,
                    "外语"+i,
                    updateDateTimes[i%updateDateTimes.length],
                    "P20-7"+i
            );
            patientInfoList.add(patientInfo);
        }
        map.put("data",patientInfoList);
        map2.put("Response",map);
        return map2;
    }
}
