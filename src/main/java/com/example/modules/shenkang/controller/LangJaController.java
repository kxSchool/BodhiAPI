package com.example.modules.shenkang.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.TypeUtils;
import com.example.modules.shenkang.mapper.CommonDao;
import com.example.modules.shenkang.mapper.DebugLogDao;
import com.example.modules.shenkang.mapper.MedicalSimpleDao;
import com.example.modules.shenkang.pojo.*;
import com.example.modules.shenkang.service.LangJiaService;
import com.example.modules.shenkang.service.MedicalSimpleInfoService;
import com.example.modules.shenkang.service.WalnutCommonService;
import com.example.modules.shenkang.service.impl.WanDaPushServiceImpl;
import com.example.modules.shenkang.utils.ShenkangUtils;
import com.example.modules.simulation.utils.PatientAPIUtils;
import io.swagger.annotations.ApiOperation;
import org.json.JSONArray;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/langjiaInterface")
public class LangJaController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${shenkang.webApiUrl.WandaRequestCheckUrl}")
    private String wandaRequestCheckUrl;

    @Value("${shenkang.imageUploadPath}")
    private String imageUploadPath;

    @Autowired
    MedicalSimpleDao medicalSimpleDao;

    @Value("${shenkang.webApiUrl.SliceInfo}")
    private String sliceInfo;

    //获取患者信息接口请求地址
    @Value("${shenkang.webApiUrl.PatientRegisterUrl}")
    private String patientRegisterUrl;

    @Value("${shenkang.cronTime.dayDelay}")
    private int dayDelay;

    //获取检查信息注册请求地址
    @Value("${shenkang.webApiUrl.ExamRequestRegisterUrl}")
    private String examRequestRegisterUrl;

    @Value("${shenkang.webApiUrl.WandaRegisterUrl}")
    private String wandaRegisterUrl;

    @Autowired
    private DebugLogDao debugLogDao;

    @Autowired
    private LangJiaService langJiaService;

    @Autowired
    MedicalSimpleInfoService medicalSimpleInfoService;

    @Autowired
    private WanDaPushServiceImpl wanDaPushService;

    @Value("${shenkang.webApiUrl.WandaRequestTokenUrl}")
    private String wandaRequestTokenUrl;

    @Autowired
    private CommonDao commonDao;

    @RequestMapping(value="/interfaceTestAI")
    @ResponseBody
    public StringBuffer interfaceTestAI(@RequestParam(value="pathNo",required=true) String pathNo, @RequestParam(value="slideNo",required=false) String slideNo){
        HashMap<String,String> paramsList=new HashMap<>();
        //设置请求参数
        paramsList.put("pathNo",pathNo);
        paramsList.put("slideNo",slideNo);
        //获取返回信息
        StringBuffer detalisResp= null;
        try {
            //发送请求,获取患者信息
            detalisResp = PatientAPIUtils.getUrlRespDetails(sliceInfo,paramsList);
        } catch (Exception e) {
            //接口异常记录到日志中
            DebugLog debugLog=new DebugLog(JSONObject.toJSONString(paramsList),e.getMessage(),"getAI","1",pathNo,null, ShenkangUtils.currentTimestampTime(),"2");
            langJiaService.insertDegbugLog(debugLog);
            return new StringBuffer(e.getMessage());
        }
        DebugLog debugLog;
        //判断返回报文是否正确
        if(!ShenkangUtils.isLangjiaFormat(detalisResp)){
            debugLog=new DebugLog(JSONObject.toJSONString(paramsList), JSONObject.toJSONString(com.alibaba.fastjson.JSONObject.parseObject(String.valueOf(detalisResp))),"getAI","2",pathNo,null, ShenkangUtils.currentTimestampTime(),"2");
        }else{
            debugLog=new DebugLog(JSONObject.toJSONString(paramsList), JSONObject.toJSONString(com.alibaba.fastjson.JSONObject.parseObject(String.valueOf(detalisResp))),"getAI","0",pathNo,null, ShenkangUtils.currentTimestampTime(),"2");
        }
        langJiaService.insertDegbugLog(debugLog);
        return detalisResp;
    }

    /**
     * @author: 邵梦丽 on 2020/9/21 9:17
     * @param:
     * @return:
     * @Description:如果数据存在则新增，不存在则新增,关闭请求即可启动获取数据功能，防止恶意请求访问服务，导致日志和数据异常
     */
    @RequestMapping(value = "/getPatientRegister", method = RequestMethod.GET)
    @ApiOperation("获取患者数据")
    public Result getPatientRegister(String updateDateTime) {
        Result result = new Result();
        //如果为空，则返回信息
        if (patientRegisterUrl.length() == 0) {
            result.setCode(300);
            result.setMsg("the request url is null");
            logger.error("the request of getPatient's url is null");
            return result;
        }
        if (updateDateTime != null) {
            //设置当前日期时间
            boolean dateflag = PatientAPIUtils.isRqFormat(updateDateTime);
            if (!dateflag) {//日期非格式化
                result.setCode(300);
                result.setMsg("the parmater [updateDateTime] is not format");
                logger.error("the parmater [updateDateTime] is not format");
                return result;
            }
        } else {
            updateDateTime = PatientAPIUtils.getDelayFormatday(dayDelay);
        }
        //创建请求和设置请求相关
        HashMap<String, String> paramsList = new HashMap<>();
        paramsList.put("updateDateTime", updateDateTime);
        //获取返回信息
        StringBuffer detalisResp = null;
        try {
            //发送请求,获取患者信息
            detalisResp = PatientAPIUtils.getUrlRespDetails(patientRegisterUrl, paramsList);
        } catch (IOException e) {
            e.printStackTrace();
            result.setCode(300);
            result.setMsg(updateDateTime + ":IOException:'getPatientRegister' is failed!");
//            logger.error(result.getMsg());
//            return result;
        }
        //将返回信息写入日志中
        logger.info("apiRequestInfo:{url:" + patientRegisterUrl
                + ",params:{" + "updateDateTime:" + updateDateTime + "},apiResponseInfo:" + JSONObject.toJSON(detalisResp).toString());
        int logstate=0;
        if(!ShenkangUtils.isLangjiaFormat(detalisResp)){
            logstate=2;
        }
        PatientJson patientJson = new PatientJson(updateDateTime, PatientAPIUtils.getTime(), JSONObject.toJSON(detalisResp).toString(),1,logstate);
        medicalSimpleInfoService.insertPatientJson(patientJson);
        //将返回信息插入数据库中
        result = medicalSimpleInfoService.addPatients(detalisResp, updateDateTime);
        return result;
    }

    /**
     * @author: 邵梦丽 on 2020/12/21 16:57
     * @param:
     * @return:
     * @Description:请求患者信息调试入口
     */
    @RequestMapping(value="/interfaceTestPatient")
    @ResponseBody
    public StringBuffer interfaceTestPatient(@RequestParam(value="updateDateTime",required=false) String updateDateTime, @RequestParam(value="pathNo",required=false) String pathNo){
        HashMap<String,String> paramsList=new HashMap<>();
        Map<String, Object> map = new HashMap<String, Object>();
        String paramNo="";
        //设置请求参数
        if(updateDateTime!=null){
            paramsList.put("updateDateTime",updateDateTime);
            paramNo="1";
        }else if(pathNo!=null){
            paramsList.put("pathNo",pathNo);
            paramNo="2";
        }
        //获取返回信息
        StringBuffer detalisResp= null;
        try {
            //发送请求,获取患者信息
            detalisResp = PatientAPIUtils.getUrlRespDetails(patientRegisterUrl,paramsList);
        } catch (Exception e) {
            //接口异常记录到日志中
            DebugLog debugLog=new DebugLog(JSONObject.toJSONString(paramsList),e.getMessage(),"getPatients","1",pathNo,updateDateTime, ShenkangUtils.currentTimestampTime(),paramNo);
            langJiaService.insertDegbugLog(debugLog);
            return new StringBuffer(e.getMessage());
        }
        DebugLog debugLog;
        //判断返回报文是否正确
        if(!ShenkangUtils.isLangjiaFormat(detalisResp)){
            debugLog=new DebugLog(JSONObject.toJSONString(paramsList), JSONObject.toJSONString(com.alibaba.fastjson.JSONObject.parseObject(String.valueOf(detalisResp))),"getPatients","2",pathNo,updateDateTime, ShenkangUtils.currentTimestampTime(),paramNo);
        }else{
            debugLog=new DebugLog(JSONObject.toJSONString(paramsList), JSONObject.toJSONString(com.alibaba.fastjson.JSONObject.parseObject(String.valueOf(detalisResp))),"getPatients","0",pathNo,updateDateTime, ShenkangUtils.currentTimestampTime(),paramNo);
        }
        langJiaService.insertDegbugLog(debugLog);
        return detalisResp;
    }

    @RequestMapping(value="/interfaceTestPatientRegister")
    @ResponseBody
    public StringBuffer interfaceTesPatientRegister(@RequestParam(value="updateDateTime",required=false) String updateDateTime, @RequestParam(value="pathNo",required=false) String pathNo){
        HashMap<String,String> paramsList=new HashMap<>();
        Map<String, Object> map = new HashMap<String, Object>();
        String paramNo="";
        //设置请求参数
        if(updateDateTime!=null){
            paramsList.put("updateDateTime",updateDateTime);
            paramNo="0";
        }else if(pathNo!=null){
            paramsList.put("pathNo",pathNo);
            paramNo="1";
        }
        //获取返回信息
        StringBuffer detalisResp= null;
        try {
            //发送请求,获取患者信息
            detalisResp = PatientAPIUtils.getUrlRespDetails(examRequestRegisterUrl,paramsList);
        } catch (Exception e) {
            //接口异常记录到日志中
            DebugLog debugLog=new DebugLog(JSONObject.toJSONString(paramsList),e.getMessage(),"getMedicals","1",pathNo,updateDateTime, ShenkangUtils.currentTimestampTime(),paramNo);
            langJiaService.insertDegbugLog(debugLog);
            return new StringBuffer(e.getMessage());
        }
        DebugLog debugLog;
        //判断返回报文是否正确
        if(!ShenkangUtils.isLangjiaFormat(detalisResp)){
            debugLog=new DebugLog(JSONObject.toJSONString(paramsList), JSONObject.toJSONString(com.alibaba.fastjson.JSONObject.parseObject(String.valueOf(detalisResp))),"getMedicals","2",pathNo,updateDateTime, ShenkangUtils.currentTimestampTime(),paramNo);
        }else{
            debugLog=new DebugLog(JSONObject.toJSONString(paramsList), JSONObject.toJSONString(com.alibaba.fastjson.JSONObject.parseObject(String.valueOf(detalisResp))),"getMedicals","0",pathNo,updateDateTime, ShenkangUtils.currentTimestampTime(),paramNo);
        }
        langJiaService.insertDegbugLog(debugLog);
        return detalisResp;
    }

    /**
     * @author: 邵梦丽 on 2020/9/21 9:17
     * @param:
     * @return:
     * @Description:如果数据存在则新增，不存在则新增,关闭请求即可启动获取数据功能，防止恶意请求访问服务，导致日志和数据异常
     */
    @RequestMapping(value = "/getExamRequestRegister", method = RequestMethod.GET)
    @ApiOperation("获取病理数据")
    public Result getExamRequestRegister(String updateDateTime) {
        Result result = new Result();
        //如果为空，则返回信息
        if (examRequestRegisterUrl.length() == 0) {
            result.setCode(300);
            result.setMsg("the request url is null");
            logger.error("the request of getPatient's url is null");
            return result;
        }
        if (updateDateTime != null) {
            boolean dateflag = PatientAPIUtils.isRqFormat(updateDateTime);
            if (!dateflag) {//日期非格式化
                result.setCode(300);
                result.setMsg("the parmater [updateDateTime] is not format");
                logger.error("the parmater [updateDateTime] is not format");
                return result;
            }
        } else {
            updateDateTime = PatientAPIUtils.getDelayFormatday(dayDelay);
        }
        //创建请求和设置请求相关
        HashMap<String, String> paramsList = new HashMap<>();
        paramsList.put("updateDateTime", updateDateTime);
        //获取返回信息
        StringBuffer detalisResp = null;
        try {
            detalisResp = PatientAPIUtils.getUrlRespDetails(examRequestRegisterUrl, paramsList);
        } catch (IOException e) {
            e.printStackTrace();
            result.setCode(300);
            result.setMsg("'getExamRequestRegister' is failed!");
            logger.error("'getExamRequestRegister' is failed!");
            return result;
        }
        //将返回信息写入日志中
        logger.info("apiRequestInfo:{url:" + examRequestRegisterUrl
                + ",params:{" + "updateDateTime:" + updateDateTime + "},apiResponseInfo:" + JSONObject.toJSON(detalisResp).toString());
        int logstate=0;
        if(!ShenkangUtils.isLangjiaFormat(detalisResp)){
            logstate=2;
        }
        MedicalJson medicalJson = new MedicalJson(updateDateTime, PatientAPIUtils.getTime(),JSONObject.toJSON(detalisResp).toString(),1,logstate);
        medicalSimpleInfoService.insertMedicalJson(medicalJson);
        result = medicalSimpleInfoService.addMedicalInfos(detalisResp, updateDateTime);
        return result;
    }

    @PostMapping("/patientLogList")
    @ApiOperation("获取调试日志")
    @ResponseBody
    public Result patientLogList(@RequestBody Map<String, Object> param) {
        Result result = new Result();
        try {
            Object beginNum = param.get("offset");
            Object pageSize = param.get("limit");
            Object apiNameTemp = param.get("apiName");
            Object logstateTemp = param.get("state");
            String logstate = null;
            if (logstateTemp != null) {
                logstate = logstateTemp.toString();
            }
            String apiName = apiNameTemp.toString();
            int pageNum = ShenkangUtils.getPageParam(beginNum, 0);
            int pageSizeTemp = ShenkangUtils.getPageParam(pageSize, 10);
            String pathNo= ShenkangUtils.getStringParam(param,"pathNo");
            String dateStart=ShenkangUtils.getStringParam(param,"dateStart");
            String dateEnd=ShenkangUtils.getStringParam(param,"dateEnd");
            int totalNum = medicalSimpleInfoService.getLogs(pathNo,dateStart, dateEnd, null, null, apiName, logstate).size();
            Map<String, Object> data = new ConcurrentHashMap<>(10);
            List<DebugLog> patientJsonList = medicalSimpleInfoService.getLogs(pathNo,dateStart, dateEnd, pageNum, pageSizeTemp, apiName, logstate);
            switch (apiName) {
                case "pushPatient":
                    for (int i = 0; i < patientJsonList.size(); i++) {
                        DebugLog debugLog = patientJsonList.get(i);
                        if (debugLog.getRspjson() != null && debugLog.getRspjson().length() > 150)
                            debugLog.setRspjson(debugLog.getRspjson().substring(0, 150));
                        debugLog.setFormatCreatetime(ShenkangUtils.getDate(debugLog.getCreatetime()));
                        String bodyTemp=debugLog.getReqjson();
                        if(bodyTemp!=null && bodyTemp.length()!=0){
                            try{
                                JSONObject jsonObject=JSONObject.parseObject(bodyTemp);
                                PatientInfo patientInfo = JSONObject.parseObject(jsonObject.getString("body"), PatientInfo.class);
                                if(patientInfo!=null){
                                    patientInfo=ShenkangUtils.descInfo02(patientInfo);
                                    debugLog.setPushJson(JSONObject.toJSONString(patientInfo));
                                    patientJsonList.remove(i);
                                    patientJsonList.add(i,debugLog);
                                }
                            }catch(Exception e){

                            }
                        }
                    }
                    break;
                case "pushMedical":
                    for (int i = 0; i < patientJsonList.size(); i++) {
                        DebugLog debugLog = patientJsonList.get(i);
                        if (debugLog.getRspjson() != null && debugLog.getRspjson().length() > 150)
                            debugLog.setRspjson(debugLog.getRspjson().substring(0, 150));
                        debugLog.setFormatCreatetime(ShenkangUtils.getDate(debugLog.getCreatetime()));
                        String bodyTemp=debugLog.getReqjson();
                        if(bodyTemp!=null && bodyTemp.length()!=0){
                            try{
                                JSONObject jsonObject=JSONObject.parseObject(bodyTemp);
                                MedicalInfo medicalInfo = JSONObject.parseObject(jsonObject.getString("body"), MedicalInfo.class);
                                if(medicalInfo!=null){
                                    medicalInfo=wanDaPushService.desctMedicalInfo(medicalInfo);
                                    debugLog.setPushJson(JSONObject.toJSONString(medicalInfo));
                                    patientJsonList.remove(i);
                                    patientJsonList.add(i,debugLog);
                                }
                            }catch(Exception e){

                            }
                        }
                    }
                    break;
                default:
                    for (int i = 0; i < patientJsonList.size(); i++) {
                        DebugLog debugLog = patientJsonList.get(i);
                        if (debugLog.getRspjson() != null && debugLog.getRspjson().length() > 150)
                            debugLog.setRspjson(debugLog.getRspjson().substring(0, 150));
                        debugLog.setFormatCreatetime(ShenkangUtils.getDate(debugLog.getCreatetime()));
                    }
            }
            data.put("dataList", patientJsonList);
            data.put("total", totalNum);
            result.setCode(200);
            result.setData(data);
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(300);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    @PostMapping("/aiSlideList")
    @ResponseBody
    @ApiOperation("获取ai切片请求日志列表")
    public Result aiSlideList(@RequestBody Map<String, Object> param) {
        Result result = new Result();
        try {
            Object beginNum = param.get("offset");
            Object pageSize = param.get("limit");
            int pageNum = ShenkangUtils.getPageParam(beginNum, 0);
            int pageSizeTemp = ShenkangUtils.getPageParam(pageSize, 10);
            Object logstateTemp = param.get("logstate");
            Integer logstate = null;
            if (logstateTemp != null) {
                logstate = 0;
            }
            String dateStart=ShenkangUtils.getStringParam(param,"dateStart");
            String dateEnd=ShenkangUtils.getStringParam(param,"dateEnd");
            String pathNo= ShenkangUtils.getStringParam(param,"pathNo");
            int totalNum = medicalSimpleInfoService.getAiJsonList(pathNo,dateStart, dateEnd, null, null, logstate).size();
            Map<String, Object> data = new ConcurrentHashMap<>(10);
            List<Map<String, Object>> patientJsonList = medicalSimpleInfoService.getAiJsonList(pathNo,dateStart, dateEnd, pageNum, pageSizeTemp, logstate);
            if (patientJsonList != null && patientJsonList.size() != 0) {
                for (int i = 0; i < patientJsonList.size(); i++) {
                    Map<String, Object> aiJson = patientJsonList.get(i);
                    //判断返回数据是否正常
                    String respJson = aiJson.get("rspJson").toString();
                    if (respJson.length() > 150)
                        aiJson.put("rspJson", respJson.substring(0, 150));
                }
            }
            data.put("dataList", patientJsonList);
            data.put("total", totalNum);
            result.setCode(200);
            result.setData(data);
        } catch (Exception e) {
            result.setMsg(e.getMessage());
            result.setCode(300);
        }
        return result;
    }

    @RequestMapping("/selectJsonById")
    public String json(@RequestParam(value = "id", required = false) Integer id, @RequestParam(value = "state", required = false) Integer state, Model model) {
        String resp = medicalSimpleDao.selectLjMById(id, state);
        model.addAttribute("json", JSON.toJSON(resp));
        return resp;
    }


    @RequestMapping(value = "/ingoreAiError", method = RequestMethod.POST, consumes = "application/json")
    @ApiOperation("设置切片接口日志忽略异常状态")
    @ResponseBody
    public Result ingoreAiError(@RequestBody Map<String, Object> param) {
        Result result = new Result();
        try {
            Object idTemp = param.get("id");
            int id = ShenkangUtils.getPageParam(idTemp, 0);
            int state = medicalSimpleInfoService.setAiJsonStateById(id);
            if (state != 0) {
                result.setCode(200);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(300);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    @RequestMapping(value = "/ingoreLogError", method = RequestMethod.POST, consumes = "application/json")
    @ApiOperation("设置调试日志忽略异常状态")
    @ResponseBody
    public Result updateDebugLogState(@RequestBody Map<String, Object> param) {
        Result result = new Result();
        try {
            Object idTemp = param.get("id");
            int id = ShenkangUtils.getPageParam(idTemp, 0);
            int state = medicalSimpleInfoService.setLogStateById(id);
            if (state != 0) {
                result.setCode(200);
            }
        } catch (Exception e) {
            result.setCode(300);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    @GetMapping("/patientLogById")
    @ApiOperation("获取调试患者信息详细日志")
    @ResponseBody
    public Object patientLogById(@RequestParam(value = "id", required = true) Integer id, @RequestParam(value = "state", required = false) String state) {
        if (id == null || id < 0)
            id = 0;
        DebugLog debugLog = medicalSimpleInfoService.getLogById(id);
        if (state != null && state.equals("反向校验")) {
            return JSONObject.toJSON(debugLog.getReqjson());
        }
        return JSONObject.toJSON(debugLog.getRspjson());
    }

    @RequestMapping(value = "/ingoreMedicalError", method = RequestMethod.POST, consumes = "application/json")
    @ApiOperation("设置患者信息日志忽略异常状态")
    @ResponseBody
    public Result ingoreMedicalError(@RequestBody Map<String, Object> param) {
        Result result = new Result();
        try {
            Object idTemp = param.get("id");
            int id = ShenkangUtils.getPageParam(idTemp, 0);
            int state = medicalSimpleInfoService.setMedicalJsonStateById(id);
            if (state != 0) {
                result.setCode(200);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(300);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    @PostMapping("/detailsForStatistics")
    @ApiOperation("操作统计按钮   ")
    @ResponseBody
    public Result detailsForStatistics(@RequestBody Map<String, Object> param) {
        Result result = new Result();
        try {
            Map<String, Object> data = new ConcurrentHashMap<>(10);
            String searchTime = (String) param.get("searchTime");
            String startTime = (String) param.get("startTime");
            String endTime = (String) param.get("endTime");

            List<MapData> totalList = new ArrayList<>();
            Integer totalNum2 = medicalSimpleDao.selectSimpleInfoNum();
            totalList.add(new MapData("获取总条数", totalNum2 + ""));

            //获取查询总条数
            Integer totalNum = medicalSimpleDao.selectSimpleInfoNumBySearchTime02(searchTime, startTime, endTime);
            //推送未发送条数
            Integer totalNum01 = medicalSimpleDao.selectSimpleInfoNumBySearchTime(searchTime, "0", startTime, endTime);
            //推送已发送条数
            //推送发送成功条数
            Integer totalNum03 = medicalSimpleDao.selectSimpleInfoNumBySearchTime(searchTime, "2", startTime, endTime);
            //发送/校验失败
            Integer totalNum04 = medicalSimpleDao.selectSimpleInfoNumBySearchTime(searchTime, "3", startTime, endTime);
            totalList.add(new MapData(searchTime + "时间段查询总条数", totalNum + ""));
            totalList.add(new MapData(searchTime + "未发送条数", totalNum01 + ""));
            totalList.add(new MapData(searchTime + "发送成功条数", totalNum03 + ""));
            totalList.add(new MapData(searchTime + "发送/校验失败", totalNum04 + ""));


            data.put("dataList", totalList);
            data.put("total", totalList.size());
            result.setData(data);
            result.setCode(200);
        } catch (Exception e) {
            e.printStackTrace();
            result.setData(e.getMessage());
            result.setCode(300);
        }
        return result;
    }

    @PostMapping("/detailsForCommon")
    @ApiOperation("操作详情按钮")
    @ResponseBody
    public Result detailsForCommon(@RequestBody Map<String, Object> param) {
        Result result = new Result();
        try {
            Map<String, Object> data = new ConcurrentHashMap<>(10);
            String searchTime = (String) param.get("searchTime");
            Integer beginNum = (Integer) param.get("offset");
            Integer pageSize = (Integer) param.get("limit");
            int pageNum = ShenkangUtils.getPageParam(beginNum, 0);
            int pageSizeTemp = ShenkangUtils.getPageParam(pageSize, 10);
            List<MedicalSimpleInfo> list = medicalSimpleDao.selectSimpleInfoBySearchTime(searchTime, pageNum, pageSizeTemp);
            Integer totalNum = medicalSimpleDao.selectSimpleInfoNumBySearchTime(searchTime, null, null, null);
            data.put("dataList", list);
            data.put("total", totalNum);
            result.setData(data);
            result.setCode(200);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping(value = "/ingoreError", method = RequestMethod.POST, consumes = "application/json")
    @ApiOperation("设置患者信息日志忽略异常状态")
    @ResponseBody
    public Result updateLogState(@RequestBody Map<String, Object> param) {
        Result result = new Result();
        try {
            Object idTemp = param.get("id");
            int id = ShenkangUtils.getPageParam(idTemp, 0);
            int state = medicalSimpleInfoService.setPatientJsomStateById(id);
            if (state != 0) {
                result.setCode(200);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(300);
            result.setMsg(e.getMessage());
        }
        return result;
    }
    @PostMapping("/medicalList")
    @ResponseBody
    @ApiOperation("获取病理信息请求日志列表")
    public Result medicalList(@RequestBody Map<String, Object> param) {
        Result result = new Result();
        try {
            Object beginNum = param.get("offset");
            Object pageSize = param.get("limit");
            Object logstateTemp = param.get("logstate");
            String logstate = null;
            if (logstateTemp != null) {
                logstate = "0";
            }
            int pageNum = ShenkangUtils.getPageParam(beginNum, 0);
            int pageSizeTemp = ShenkangUtils.getPageParam(pageSize, 10);
            String dateStart=ShenkangUtils.getStringParam(param,"dateStart");
            String dateEnd=ShenkangUtils.getStringParam(param,"dateEnd");
            int totalNum = medicalSimpleInfoService.getMedicalJsonByWhole(dateStart, dateEnd, null, null, logstate).size();
            Map<String, Object> data = new ConcurrentHashMap<>(10);
            List<MedicalJson> medicalJsonList = medicalSimpleInfoService.getMedicalJsonByWhole(dateStart, dateEnd, pageNum, pageSizeTemp, logstate);
            if (medicalJsonList != null && medicalJsonList.size() != 0)
                for (int i = 0; i < medicalJsonList.size(); i++) {
                    MedicalJson medicalJson = medicalJsonList.get(i);
                    //判断返回数据是否正常
                    String respJson = medicalJson.getRspJson();
                    if (respJson != null && respJson.length() > 150)
                        medicalJson.setRspJson(medicalJson.getRspJson().substring(0, 150));
                }
            data.put("dataList", medicalJsonList);
            data.put("total", totalNum);
            result.setCode(200);
            result.setData(data);
        } catch (Exception e) {
            result.setCode(300);
            result.setMsg(e.getMessage());
        }
        return result;
    }


    /**
     * @author: 邵梦丽 on 2020/12/30 9:56
     */
    @PostMapping("/patientList")
    @ResponseBody
    @ApiOperation("获取患者信息请求日志列表")
    public Result patientList(@RequestBody Map<String, Object> param) {
        Result result = new Result();
        try {
            Object beginNum = param.get("offset");
            Object pageSize = param.get("limit");
            Object logstateTemp = param.get("logstate");
            String logstate = null;
            if (logstateTemp != null) {
                logstate = logstateTemp.toString();
            }
            int pageNum = ShenkangUtils.getPageParam(beginNum, 0);
            int pageSizeTemp = ShenkangUtils.getPageParam(pageSize, 10);
            String dateStart=ShenkangUtils.getStringParam(param,"dateStart");
            String dateEnd=ShenkangUtils.getStringParam(param,"dateEnd");
            int totalNum = medicalSimpleInfoService.getPatientJsonByWhole(dateStart, dateEnd, null, null, logstate).size();
            Map<String, Object> data = new ConcurrentHashMap<>(10);
            List<PatientJson> patientJsonList = medicalSimpleInfoService.getPatientJsonByWhole(dateStart, dateEnd, pageNum, pageSizeTemp, logstate);
            if (patientJsonList != null && patientJsonList.size() != 0)
                for (int i = 0; i < patientJsonList.size(); i++) {
                    PatientJson patientJson = patientJsonList.get(i);
                    //判断返回数据是否正常
                    String respJson = patientJson.getRspJson();
                    if (respJson != null && respJson.length() > 150)
                        patientJson.setRspJson(patientJson.getRspJson().substring(0, 150));
                }
            data.put("dataList", patientJsonList);
            data.put("total", totalNum);
            result.setCode(200);
            result.setData(data);
        } catch (Exception e) {
            result.setCode(300);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    @RequestMapping(value="/interfaceTestToken")
    @ResponseBody
    public JSONObject interfaceTestToken(){
        String request = wanDaPushService.HttpPostDataForInterface(wandaRequestTokenUrl, "grant_type=client_credentials");
        com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(request);
        return jsonObject;
    }


    @RequestMapping(value = "/getPatientInfoByPathNo", method = RequestMethod.POST, consumes = "application/json")
    @ApiOperation("根据病理号查询患者信息")
    @ResponseBody
    public Result getPatientInfoByPathNo(@RequestBody Map<String, Object> param) {
        Result result=new Result();
        result.setCode(300);
        Object pathNoTemp = param.get("pathNo");
        String pathNo=pathNoTemp.toString();
        PatientInfo patientInfo=medicalSimpleDao.getPatientByPathNo(pathNo);
        if(patientInfo!=null){
            result.setCode(200);
            Map map = JSON.parseObject(JSON.toJSONString(patientInfo), Map.class);
            if(map.get("id_card_no")!=null){
                map.put("IdentityID",map.get("id_card_no").toString());
                map.remove("id_card_no");
            }
            result.setData(map);
        }else{
            result.setMsg("病理号不存在！");
        }
        return result;
    }

    @RequestMapping(value="/pushPatient")
    @ResponseBody
    public Map<String,Object> pushPatient(@RequestParam(value="toastrOptions",required=true) String toastrOptions){
        TypeUtils.compatibleWithJavaBean = true;
        Map<String,Object> map=new HashMap<>();
        PatientInfo patientinfo = null;
        com.alibaba.fastjson.JSONObject jsonObject1 =null;
        try{
            if(toastrOptions!=null&&toastrOptions.length()!=0){
                toastrOptions=toastrOptions.replace("&nbsp;","");
                patientinfo = JSON.parseObject(toastrOptions, PatientInfo.class);
                patientinfo.setId_card_no(patientinfo.getIdentityID());
                WalnutCommonService.encryptInfo02(patientinfo);
                String url = wandaRegisterUrl;
                com.alibaba.fastjson.JSONObject object = (com.alibaba.fastjson.JSONObject) JSON.toJSON(patientinfo);
                String token = commonDao.selecrToken();
                Map<String,Object> request = wanDaPushService.requestForInterfaceMap(url, object, token,0);
                jsonObject1 = com.alibaba.fastjson.JSONObject.parseObject(request.get("body").toString());
                map.put("patient",patientinfo);
                map.put("descpatient",ShenkangUtils.descInfo02(patientinfo));
                map.put("rsppatient",jsonObject1);
                String logstate="0";
                if(!ShenkangUtils.isWandaFormat(jsonObject1)){
                    logstate="2";
                }
                DebugLog debugLog=new DebugLog(JSONObject.toJSONString(request.get("all")), JSONObject.toJSONString(jsonObject1),"pushPatient",logstate,patientinfo.getPathNo(),null, ShenkangUtils.currentTimestampTime(),"2");
                langJiaService.insertDegbugLog(debugLog);
                return map;
            }else{
                return null;
            }
        }catch(Exception e){
            DebugLog debugLog=new DebugLog(JSONObject.toJSONString(JSON.toJSON(patientinfo)), e.getMessage(),"pushPatient","1",patientinfo.getPathNo(),null, ShenkangUtils.currentTimestampTime(),"2");
            langJiaService.insertDegbugLog(debugLog);
            return null;
        }
    }

    /**
     * @author: 邵梦丽 on 2020/12/24 11:43
     * @param:
     * @return:
     * @Description:根据病历号查询数据库中是否有文件记录，为了提供文件上传功能
     */
    @PostMapping(value="/searchFileInfo")
    @ResponseBody
    public Result patientErrorLogList(@RequestBody Map<String, Object> param) {
        Result result = new Result();
        try{
            Integer beginNum = (Integer) param.get("offset");
            Integer pageSize = (Integer) param.get("limit");
            int pageNum = ShenkangUtils.getPageParam(beginNum, 0);
            int pageSizeTemp = ShenkangUtils.getPageParam(pageSize, 10);
            Map<String, Object> data = new ConcurrentHashMap<>(10);
            String pathNo = ShenkangUtils.getStringParam(param, "pathNo");
            String pushState=ShenkangUtils.getStringParam(param, "pushStateTemp");
            List<HFileInfo> hFileInfos=commonDao.selectFilesIdByMedicalId(pageSize,pageNum,pathNo,pushState,null);
            if(hFileInfos!=null&&hFileInfos.size()!=0){
                for(HFileInfo fileInfo: hFileInfos){
                    if(fileInfo.getFileUID()!=null&&fileInfo.getFileUID().length()!=0){
                        fileInfo.setPushState("1");
                    }else{
                        fileInfo.setPushState("0");
                    }
                }
            }
            List<HFileInfo> hFileInfosAll=commonDao.selectFilesIdByMedicalId(null,null,pathNo,pushState,null);
            data.put("dataList", hFileInfos);
            data.put("total", hFileInfosAll.size());
            //查询总条数
            result.setCode(200);
            result.setData(data);
        }catch (Exception e){
            e.printStackTrace();
            result.setException(e);
            result.setMsg("查询文件失败;");
        }
        return result;
    }

    @PostMapping(value="/pushfile")
    @ResponseBody
    public Result pushfile(@RequestParam(value="fileIdLists",required=true) String fileIdLists)  {
        Result result1=new Result();
        result1.setCode(200);
        result1.setMsg("文件推送成功");
        List<String> failedIds=new LinkedList<>();
        if(fileIdLists!=null&&fileIdLists.trim().length()!=0){
            String[] strArray = null;
            strArray = fileIdLists.replace("\"","").split(",");
            for(int i=0;i<strArray.length;i++){
                if(strArray[i].length()!=0)
                {
                    Result result=wanDaPushService.postFiles(strArray[i]);
                    if(result.getCode()==300){
                        result1.setCode(300);
                        failedIds.add(strArray[i]);
                    }
                }
            }
            if(result1.getCode()==300){
                String citiesCommaSeparated = String.join(",", failedIds);
                result1.setMsg(citiesCommaSeparated);
            }
        }else{
            return null;
        }
        return result1;
    }


    @RequestMapping(value="/upFileByPathNo")
    @ResponseBody
    public Map<String,Object> upFileByPathNo(
            @RequestParam(value="pathNo",required=true) String pathNo,
            @RequestParam(value="fileFatherId",required=true) String fileFatherId,
            @RequestParam(value="filename",required=true) String filename,
            @RequestParam(value="file",required=true) MultipartFile file,
            @RequestParam(value="optionFileType",required=true) String optionFileType,
            @RequestParam(value="observationUID",required=false) String observationUID) throws Exception{
        Map<String,Object> map=new HashMap<>();
        if(pathNo!=null&&pathNo.trim().length()!=0){
            MedicalInfo medicalInfo=debugLogDao.getMedicalidByPathNo(pathNo);
            //上传文件
            String filePath=ShenkangUtils.upFileToLocal(file,pathNo,imageUploadPath);
            String filePathTemp=imageUploadPath+filePath;
            if(medicalInfo==null){//查看数据是否存在
                if(observationUID!=null&&observationUID.trim().length()!=0&&!observationUID.equals("undefined")){
                    if(optionFileType==null)
                    {
                        map.put("state","failed");
                        map.put("reason","optionFileType必须输入！");
                        return map;
                    }
                    //再向子表中存入数据
                    HFileInfo hFileInfo=new HFileInfo(null, Integer.parseInt(fileFatherId), null, filename,
                            filePath, null, optionFileType, null,
                            pathNo, observationUID);
                    File fileTemp=new File(filePathTemp);
                    if(fileTemp.exists()){
                        hFileInfo.setFilesize(fileTemp.length()/1024);
                    }
                    debugLogDao.insertFile(hFileInfo);
                    Map<String,Object> map2= searchFileInfo(pathNo);
                    map.put("state","success");
                    map.put("data",map2.get("hFileInfos"));
                }else{
                    map.put("state","failed");
                    map.put("reason","字段ObservationUID必须输入！");
                    return map;
                }
            }else{
                //子表中插入对应数据和上传文件,将主表对应的id放到子表中
                //查找主表中的observationid,如果为空，则将页面输入的放进去
                if(medicalInfo.getObservationUID()!=null&&medicalInfo.getObservationUID().trim().length()!=0){
                    observationUID=medicalInfo.getObservationUID();
                }else{
                    if(!(observationUID!=null&observationUID.trim().length()!=0)){
                        map.put("state","failed");
                        map.put("reason","该病理号没有ObservationUID,ObservationUID必须输入！");
                        return map;
                    }
                }
                //更新主表的observationUID
                //debugLogDao.updateMedicalInfoObservationUIDByMedicalId(medicalInfo.getId(),observationUID);
                //创建子表数据，并返回查询结果给前台用于刷新表格
                HFileInfo hFileInfo=new HFileInfo(null, Integer.parseInt(fileFatherId), medicalInfo.getId(), filename,
                        filePath, null, optionFileType, null,
                        pathNo, observationUID);
                File fileTemp=new File(filePathTemp);
                if(fileTemp.exists()){
                    hFileInfo.setFilesize(fileTemp.length()/1024);
                }
                debugLogDao.insertFile(hFileInfo);
                Map<String,Object> map2= searchFileInfo(pathNo);
                map.put("state","success");
                map.put("data",map2.get("hFileInfos"));
            }
        }
        return map;
    }

    public Map<String,Object> searchFileInfo(@RequestParam(value="pathNo",required=true) String pathNo){
        Result result=new Result();
        Map<String,Object> map=new HashMap<>();
        List<HFileInfo> hFileInfos=new LinkedList<>();
        if(pathNo!=null&&pathNo.trim().length()!=0){
            hFileInfos=langJiaService.getFileInfoByPathNo(pathNo);
            if(hFileInfos!=null&&hFileInfos.size()!=0){
                for(int i=0;i<hFileInfos.size();i++){
                    HFileInfo hFileInfoTemp=hFileInfos.get(i);
                    hFileInfoTemp.setPathNo(pathNo);
                    hFileInfoTemp.setFilePath(imageUploadPath+hFileInfoTemp.getFilePath());
                }
                map.put("hFileInfos",hFileInfos);
            }else{
                List<HFileInfo> filelist=debugLogDao.getFileInfosByPathNo(pathNo);
                if(filelist!=null&&filelist.size()!=0){
                    for(int i=0;i<filelist.size();i++){
                        HFileInfo hFileInfoTemp=filelist.get(i);
                        hFileInfoTemp.setFilePath(imageUploadPath+hFileInfoTemp.getFilePath());
                    }
                    map.put("hFileInfos",filelist);
                }else{
                    return null;
                }
            }
        }else{
            return null;
        }
        return map;
    }

    @RequestMapping(value="/filecheck")
    @ResponseBody
    public Map<String,Object> filecheck(@RequestParam(value="toastrOptions",required=true) String toastrOptions)  {
        Map<String, Object> re = new HashMap<>();
        String url = wandaRequestCheckUrl;
        Map<String,Object> map=new HashMap<>();
        try{
            re=StringParseToMap(toastrOptions);
            com.alibaba.fastjson.JSONObject object = (com.alibaba.fastjson.JSONObject) JSON.toJSON(re);
            String token = commonDao.selecrToken();
            Map<String,Object> request = wanDaPushService.requestForInterfaceMap(url, object, token,1);
            com.alibaba.fastjson.JSONObject jsonObject1 = com.alibaba.fastjson.JSONObject.parseObject(request.get("body").toString());
            map.put("respfilecheck",jsonObject1);
            String msg=jsonObject1.get("msg").toString();
            String state;
            if(msg.equals("OK"))
                state="0";
            else
                state="2";
            DebugLog debugLog=new DebugLog(JSONObject.toJSONString(request.get("all")), JSONObject.toJSONString(jsonObject1),"filecheck",state,re.get("pathological_number").toString(),null, ShenkangUtils.currentTimestampTime(),"2");
            langJiaService.insertDegbugLog(debugLog);
            return map;
        }catch(Exception e){
            DebugLog debugLog=new DebugLog(JSONObject.toJSONString(JSON.toJSON(re)), e.getMessage(),"filecheck","1","",null, ShenkangUtils.currentTimestampTime(),"0");
            langJiaService.insertDegbugLog(debugLog);
            map.put("respfilecheck",e.getMessage());
            return map;
        }
    }

    public Map<String,Object> StringParseToMap(String toastrOptions) throws JSONException {
        org.json.JSONObject jsonParam=null;
        String pathological_number=null;
        String organization_id=null;
        JSONArray fileList=null;
        List<Map<String,Object>>lists=new ArrayList<>();
        Map<String, Object> re = new HashMap<>();
        Map<String,Object> map=new HashMap<>();
        jsonParam=new org.json.JSONObject(toastrOptions);
        pathological_number=jsonParam.getString("pathological_number");
        organization_id=jsonParam.getString("organization_id");
        fileList=jsonParam.getJSONArray("upload_file");
        int num= fileList.length();
        for (int i=0;i<num;i++){
            String jsonObject= fileList.getString(i);
            Map<String,Object> mapTypes = JSON.parseObject(jsonObject);
            lists.add(mapTypes);
        }
        re.put("pathological_number",pathological_number);
        re.put("organization_id",organization_id);
        re.put("upload_file",lists);
        return re;
    }

    @RequestMapping(value="/localfilecheck")
    @ResponseBody
    public Result localfilecheck(@RequestParam(value="toastrOptions",required=true) String toastrOptions)  {
        Map<String, Object> re = new HashMap<>();
        Result result=new Result();
        Map<String,Object> map=new HashMap<>();
        try{
            re=StringParseToMap(toastrOptions);
            result=wanDaPushService.check((String)re.get("pathological_number"),(List<Map<String,Object>>) re.get("upload_file"),(String)re.get("organization_id"));
            String state;
            if(result.getMsg().equals("OK")){
                state="0";
            }else{
                state="2";
            }
            DebugLog debugLog=new DebugLog(JSONObject.toJSONString(JSON.toJSON(re)), JSONObject.toJSON(result).toString(),"api_verify_exam_info",state,(String)re.get("pathological_number"),null, ShenkangUtils.currentTimestampTime(),"0");
            langJiaService.insertDegbugLog(debugLog);
        }catch(Exception e){
            DebugLog debugLog=new DebugLog(JSONObject.toJSONString(JSON.toJSON(re)), e.getMessage(),"api_verify_exam_info","1","",null, ShenkangUtils.currentTimestampTime(),"0");
            langJiaService.insertDegbugLog(debugLog);
            result.setData(e.getMessage());
            return result;
        }
        return result;
    }
}
