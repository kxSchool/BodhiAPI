package com.example.modules.shenkang.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.TypeUtils;
import com.example.modules.shenkang.mapper.CommonDao;
import com.example.modules.shenkang.mapper.MedicalSimpleDao;
import com.example.modules.shenkang.pojo.*;
import com.example.modules.shenkang.service.LangJiaService;
import com.example.modules.shenkang.service.MedicalSimpleInfoService;
import com.example.modules.shenkang.service.impl.WanDaPushServiceImpl;
import com.example.modules.shenkang.utils.ShenkangUtils;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/langjiaInterface")
public class Log2Controller {

    @Autowired
    private CommonDao commonDao;

    @Autowired
    MedicalSimpleDao medicalSimpleDao;

    @Autowired
    private WanDaPushServiceImpl wanDaPushService;

    @Autowired
    MedicalSimpleInfoService medicalSimpleInfoService;

    @Autowired
    private LangJiaService langJiaService;

    @Value("${shenkang.webApiUrl.WandaMdeicalUrl}")
    private String wandaMdeicalUrl;

    //Wanda_api_req 根据type获取日志列表
    public LogApiCommon<WandaApiReq> queryLogList(String type, Integer pageSize, Integer pageNum, String logState) {
        LogApiCommon<WandaApiReq> result = new LogApiCommon<>();
        List<WandaApiReq> list = medicalSimpleDao.selectLogList1(type, pageSize, pageNum, logState);
        Integer num = medicalSimpleDao.queryLogListSize(type, logState);
        result.setList(list);
        result.setTotalNum(num);
        return result;
    }

    public LogApiCommon<WandaApiReq> queryLogListByCreatime(String type, Integer pageSize, Integer pageNum, String logState, String pathNo, String dateStart, String dateEnd) {
        LogApiCommon<WandaApiReq> result = new LogApiCommon<>();
        List<WandaApiReq> list = medicalSimpleDao.selectLogListByCreatimeAndPathno(type, pageSize, pageNum, logState, pathNo, dateStart, dateEnd);
        Integer num = medicalSimpleDao.queryLogListSizeByCreatimeAndPathno(type, logState, pathNo, dateStart, dateEnd);
        result.setList(list);
        result.setTotalNum(num);
        return result;
    }

    public LogApiCommon<WandaApiReq> queryLogList(String type, Integer pageSize, Integer pageNum, String logState, String pathNo, String dateStart, String dateEnd) throws Exception {
        LogApiCommon<WandaApiReq> result = new LogApiCommon<>();
        List<WandaApiReq> list = medicalSimpleDao.selectLogListByDateAndPathno(type, pageSize, pageNum, logState, pathNo, dateStart, dateEnd);
        switch (type) {
            case "pushMedical":
                for (int i = 0; i < list.size(); i++) {
                    WandaApiReq wandaApiReq = list.get(i);
                    String requestdetails = wandaApiReq.getRequestdetails();
                    if (requestdetails != null) {
                        String firstChar = requestdetails.substring(0, 1);
                        String body = null;
                        if (firstChar.equals("<")) {
                            requestdetails = requestdetails.substring(1, requestdetails.length() - 1);
                            body = requestdetails.substring(0, requestdetails.indexOf(",["));
                        } else {
                            JSONObject jsonObject = JSONObject.parseObject(requestdetails);
                            body = jsonObject.getString("body");
                        }
                        if (body != null) {
                            MedicalInfo medicalInfo = JSONObject.parseObject(body, MedicalInfo.class);
                            MedicalInfo medicalInfo1 = wanDaPushService.desctMedicalInfo(medicalInfo);
                            wandaApiReq.setPushJson(JSONObject.toJSONString(medicalInfo1));
                            list.remove(i);
                            list.add(i, wandaApiReq);
                        }
                    }
                }
                break;
            case "pushPatient":
                for (int i = 0; i < list.size(); i++) {
                    WandaApiReq wandaApiReq = list.get(i);
                    String requestdetails = wandaApiReq.getRequestdetails();
                    if (requestdetails != null) {
                        String firstChar = requestdetails.substring(0, 1);
                        String body = null;
                        if (firstChar.equals("<")) {
                            requestdetails = requestdetails.substring(1, requestdetails.length() - 1);
                            body = requestdetails.substring(0, requestdetails.indexOf(",["));
                        } else {
                            JSONObject jsonObject = JSONObject.parseObject(requestdetails);
                            body = jsonObject.getString("body");
                        }
                        if (body != null) {
                            PatientInfo patientInfo = JSONObject.parseObject(body, PatientInfo.class);
                            PatientInfo medicalInfo1 = ShenkangUtils.descInfo02(patientInfo);
                            wandaApiReq.setPushJson(JSONObject.toJSONString(medicalInfo1));
                            list.remove(i);
                            list.add(i, wandaApiReq);
                        }
                    }
                }
                break;
            case "   ":
                break;
            case "    ":
                break;
        }
        Integer num = medicalSimpleDao.queryLogListSizeByDateAndPathno(type, logState, pathNo, dateStart, dateEnd);
        result.setList(list);
        result.setTotalNum(num);
        return result;
    }


    @PostMapping("/LogApis")
    @ApiOperation("获取万达日志")
    @ResponseBody
    public Result patientErrorLogList(@RequestBody Map<String, Object> param) {
        Result result = new Result();
        try {
            Integer beginNum = (Integer) param.get("offset");
            Integer pageSize = (Integer) param.get("limit");
            String type = (String) param.get("type");
            boolean error = (Boolean) param.get("error");
            int pageNum = ShenkangUtils.getPageParam(beginNum, 0);
            int pageSizeTemp = ShenkangUtils.getPageParam(pageSize, 10);
            Map<String, Object> data = new ConcurrentHashMap<>(10);
            Integer totalNum = 0;
            String logState = "";
            if (error) {
                logState = "1";
            } else {
                logState = null;
            }

            String pathNo = ShenkangUtils.getStringParam(param, "pathNo");
            String dateStart = ShenkangUtils.getStringParam(param, "dateStart");
            String dateEnd = ShenkangUtils.getStringParam(param, "dateEnd");

            LogApiCommon<WandaApiReq> temp;
            switch (type) {
                case "LoginInfo":
                    LogApiCommon<WandaApiReq> wandaApiReqLogApiCommon = queryLogList(type, pageSizeTemp, pageNum, logState);
                    data.put("dataList", wandaApiReqLogApiCommon.getList());
                    data.put("total", wandaApiReqLogApiCommon.getTotalNum());
                    break;
                case "    ": //正向校验
                    LogApiCommon<WandaApiReq> ForwardCheck = queryLogList(type, pageSizeTemp, pageNum, logState);
                    data.put("dataList", ForwardCheck.getList());
                    data.put("total", ForwardCheck.getTotalNum());
                    break;
                case "pushMedical"://上传病例信息
                    LogApiCommon<WandaApiReq> PushMedical = queryLogList(type, pageSizeTemp, pageNum, logState, pathNo, dateStart, dateEnd);
                    data.put("dataList", PushMedical.getList());
                    data.put("total", PushMedical.getTotalNum());
                    break;
                case "pushPatient"://上传患者信息
                    LogApiCommon<WandaApiReq> PushPatient = queryLogList(type, pageSizeTemp, pageNum, logState, pathNo, dateStart, dateEnd);
                    List<WandaApiReq> list2 = PushPatient.getList();
                    if (list2 != null) {
                        for (int i = 0; i < list2.size(); i++) {
                            WandaApiReq wandaApiReq = list2.get(i);
                            String requestdetails = wandaApiReq.getRequestdetails();
                            if (requestdetails.length() != 0) {
                                String firstChar = requestdetails.substring(0, 1);
                                if (firstChar.equals("<")) {
                                    requestdetails = requestdetails.substring(1, requestdetails.length() - 1);
                                    String body = requestdetails.substring(0, requestdetails.indexOf(",["));
                                    Map<String, Object> map = new HashMap<>();
                                    map.put("body", JSON.parseObject(body));
                                    wandaApiReq.setRequestdetails(JSONObject.toJSONString(map));
                                }
                            }
                            list2.remove(i);
                            list2.add(i, wandaApiReq);
                        }
                        data.put("dataList", list2);
                    } else {
                        data.put("dataList", PushPatient.getList());
                    }
                    data.put("total", PushPatient.getTotalNum());
                    break;
                case "pushfile"://文件上传
                    temp = queryLogListByCreatime(type, pageSizeTemp, pageNum, logState, pathNo, dateStart, dateEnd);
                    data.put("dataList", temp.getList());
                    data.put("total", temp.getTotalNum());
                    break;
                case "filesCheckRspSimulationDatas":
                    break;
                case "registerSimulationDatas":
                    break;
                case "registerRspSimulationDatas":
                    break;
                case "filecheck":
                    temp = queryLogListByCreatime(type, pageSizeTemp, pageNum, logState, pathNo, dateStart, dateEnd);
                    List<WandaApiReq> list = temp.getList();
                    if (list != null) {
                        for (int i = 0; i < list.size(); i++) {
                            WandaApiReq wandaApiReq = list.get(i);
                            String requestdetails = wandaApiReq.getRequestdetails();
                            if (requestdetails.length() != 0) {
                                String firstChar = requestdetails.substring(0, 1);
                                if (firstChar.equals("<")) {
                                    requestdetails = requestdetails.substring(1, requestdetails.length() - 1);
                                    String body = requestdetails.substring(0, requestdetails.indexOf(",["));
                                    Map<String, Object> map = new HashMap<>();
                                    map.put("body", JSON.parseObject(body));
                                    wandaApiReq.setRequestdetails(JSONObject.toJSONString(map));
                                }
                            }
                            list.remove(i);
                            list.add(i, wandaApiReq);
                        }
                        data.put("dataList", list);
                    } else {
                        data.put("dataList", temp.getList());
                    }
                    data.put("total", temp.getTotalNum());
                    break;
                case "api_verify_exam_info":
                    temp = queryLogListByCreatime(type, pageSizeTemp, pageNum, logState, pathNo, dateStart, dateEnd);
                    data.put("dataList", temp.getList());
                    data.put("total", temp.getTotalNum());
                    break;
                default:
            }
            //查询总条数
            result.setCode(200);
            result.setData(data);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            result.setException(e);
            result.setMsg("获取token日志获取失败；");
        }
        return result;
    }

    @RequestMapping(value = "/ingoreWandaError", method = RequestMethod.POST, consumes = "application/json")
    @ApiOperation("设置调用万达交互接口日志忽略异常状态")
    @ResponseBody
    public Result ingoreWandaError(@RequestBody Map<String, Object> param) {
        Result result = new Result();
        try {
            Object idTemp = param.get("id");
            int id = ShenkangUtils.getPageParam(idTemp, 0);
            int state = medicalSimpleInfoService.setWandaRequestStateById(id);
            if (state != 0) {
                result.setCode(200);
            }
        } catch (Exception e) {
            result.setCode(300);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    @RequestMapping(value = "/getMedicalInfoByPathNo", method = RequestMethod.POST, consumes = "application/json")
    @ApiOperation("根据病理号查询病理信息")
    @ResponseBody
    public Result getMedicalInfoByPathNo(@RequestBody Map<String, Object> param) {
        Result result=new Result();
        result.setCode(300);
        Object pathNoTemp = param.get("pathNo");
        String pathNo=pathNoTemp.toString();
        MedicalInfo medicalInfo=medicalSimpleDao.getMedicalDetailsInfoByPathNo(pathNo);
        if(medicalInfo!=null){
            result.setCode(200);
            Map map = JSON.parseObject(JSON.toJSONString(medicalInfo), Map.class);
            result.setData(map);
        }else{
            result.setMsg("病理号不存在！");
        }
        return result;
    }

    @RequestMapping(value="/pushMedical")
    @ResponseBody
    public Map<String,Object> pushMedical(@RequestParam(value="toastrOptions",required=true) String toastrOptions){
        TypeUtils.compatibleWithJavaBean = true;
        Map<String,Object> map=new HashMap<>();
        MedicalInfo medicalInfo = null;
        MedicalInfo medicalInfoTemp = null;
        com.alibaba.fastjson.JSONObject jsonObject1 =null;
        String paramNo="";
        try{
            if(toastrOptions!=null&&toastrOptions.length()!=0){
                toastrOptions=toastrOptions.replace("&nbsp;","");
                medicalInfo = JSON.parseObject(toastrOptions, MedicalInfo.class);
                wanDaPushService.setBased_time(medicalInfo);
                if(medicalInfo.getPathNo()==null){
                    paramNo="0";
                }else{
                    paramNo="1";
                }
                if(medicalInfo.getPathNo()==null||medicalInfo.getPathNo().equals("")){
                    medicalInfo.setPathNo(medicalInfo.getPathological_number());
                }
                medicalInfoTemp=wanDaPushService.encryptMedicalInfo(medicalInfo);

                String url = wandaMdeicalUrl;
                com.alibaba.fastjson.JSONObject object = (com.alibaba.fastjson.JSONObject) JSON.toJSON(medicalInfoTemp);
                String token = commonDao.selecrToken();
                Map<String,Object> request = wanDaPushService.requestForInterfaceMap(url, object, token,0);
                jsonObject1 = com.alibaba.fastjson.JSONObject.parseObject(request.get("body").toString());
                String msg=jsonObject1.get("msg").toString();
                String state;
                if(msg.equals("OK"))
                    state="0";
                else
                    state="2";
                map.put("medicalInfo",medicalInfoTemp);
                map.put("descmedicalInfo",wanDaPushService.desctMedicalInfo(medicalInfoTemp));
                map.put("rspmedicalInfo",jsonObject1);
                DebugLog debugLog=new DebugLog(JSONObject.toJSONString(request.get("all")), JSONObject.toJSONString(jsonObject1),"pushMedical",state,medicalInfoTemp.getPathNo(),null, ShenkangUtils.currentTimestampTime(),paramNo);
                langJiaService.insertDegbugLog(debugLog);
                return map;
            }else{
                return null;
            }
        }catch(Exception e){
            //e.printStackTrace();
            DebugLog debugLog=new DebugLog(JSONObject.toJSONString(JSON.toJSON(medicalInfo)), e.getMessage(),"pushMedical","1",medicalInfo.getPathNo(),null, ShenkangUtils.currentTimestampTime(),paramNo);
            langJiaService.insertDegbugLog(debugLog);
            return null;
        }
    }

    @PostMapping("/LogWalnut")
    @ApiOperation("获取核桃数据")
    @ResponseBody
    public Result LogWalnut(@RequestBody Map<String, Object> param) {
        Result result = new Result();
        try {
            Map<String, Object> data = new ConcurrentHashMap<>(10);
            String pathNo = (String) param.get("pathNo");
            String startTime = (String) param.get("startTime");
            String endTime = (String) param.get("endTime");
            Integer beginNum = (Integer) param.get("offset");
            Integer pageSize = (Integer) param.get("limit");
            boolean type = (boolean) param.get("error");
            String logState = "";

            if (type) {
                //查询是否包含异常的日志
                //1.(两边插入数据不匹配异常)
                List<String> pathNoMList1 = medicalSimpleDao.selectErrorInfo("medical_resp", null);
                List<String> pathNoPList1 = medicalSimpleDao.selectErrorInfo("patient_resp", null);
                List<String> pathNoList = getDiffrent(pathNoPList1, pathNoMList1);
                //2.(日志中包含异常)
                List<String> pathNoMList2 = medicalSimpleDao.selectErrorInfo("medical_resp", "1");
                List<String> pathNoPList2 = medicalSimpleDao.selectErrorInfo("patient_resp", "1");
                //判断异常是否已处理
                List<String> list1 = new ArrayList<>();
                list1.addAll(pathNoPList2);
                list1.addAll(pathNoMList2);
                List<String> list2 = list1.stream().distinct().collect(Collectors.toList());
                for (int i = 0; i < list2.size(); i++) {
                    String s = pathNoList.get(i);
                    WalnutDetail map1 = medicalSimpleDao.selectInfoByPathId(s, "medical_resp", null);
                    WalnutDetail map2 = medicalSimpleDao.selectInfoByPathId(s, "patient_resp", null);
                    if (map1 != null && map2 != null && map1.equals("0") && map2.equals("0")) {
                        list2.remove(i);
                    }
                }
                pathNoList.addAll(list1);

                List<String> myList = pathNoList.stream().distinct().collect(Collectors.toList());
                if (myList.size() > 0) {
                    List<WalnutCommon> walnutCommonList = medicalSimpleDao.selectwalnutByParam(pathNo, startTime, endTime, beginNum, pageSize, myList);
                    Integer total = medicalSimpleDao.selectwalnutNumByParam(pathNo, startTime, endTime, beginNum, pageSize, myList);
                    for (int i = 0; i < walnutCommonList.size(); i++) {
                        WalnutCommon walnutCommon = walnutCommonList.get(i);
                        WalnutDetail map1 = medicalSimpleDao.selectInfoByPathId(walnutCommon.getPathNo(), "medical_resp", null);
                        WalnutDetail map2 = medicalSimpleDao.selectInfoByPathId(walnutCommon.getPathNo(), "patient_resp", null);
                        if (map1 != null) {
                            walnutCommon.setMedicalInfo(map1.getRspJson());
                            walnutCommon.setMedId(map1.getId());
                        }
                        if (map2 != null) {
                            walnutCommon.setPatientInfo(map2.getRspJson());
                            walnutCommon.setPatId(map2.getId());
                        }
                        walnutCommon.setLogstate("1");
                        walnutCommonList.remove(i);
                        walnutCommonList.add(i, walnutCommon);
                    }

                    data.put("dataList", walnutCommonList);
                    data.put("total", total);
                    result.setData(data);
                    result.setCode(200);
                } else {
                    List<WalnutCommon> walnutCommonList = new ArrayList<>();
                    data.put("dataList", walnutCommonList);
                    data.put("total", 0);
                    result.setData(data);
                    result.setCode(200);
                }
                return result;
            } else {
                logState = null;
                List<WalnutCommon> walnutCommonList = medicalSimpleDao.selectwalnut(pathNo, startTime, endTime, beginNum, pageSize);
                Integer total = medicalSimpleDao.selectwalnutNum(pathNo, startTime, endTime, beginNum, pageSize);
                for (int i = 0; i < walnutCommonList.size(); i++) {
                    WalnutCommon walnutCommon = walnutCommonList.get(i);
                    WalnutDetail map1 = medicalSimpleDao.selectInfoByPathId(walnutCommon.getPathNo(), "medical_resp", logState);
                    WalnutDetail map2 = medicalSimpleDao.selectInfoByPathId(walnutCommon.getPathNo(), "patient_resp", logState);
                    String logstate = "";
                    if (map1 != null) {
                        walnutCommon.setMedicalInfo(map1.getRspJson());
                        walnutCommon.setMedId(map1.getId());
                        logstate = map1.getLogstate();
                    } else {
                        walnutCommon.setLogstate("1");
                    }
                    if (map2 != null) {
                        walnutCommon.setPatientInfo(map2.getRspJson());
                        walnutCommon.setPatId(map2.getId());
                        if (logstate.equals("0")) {
                            walnutCommon.setLogstate(map2.getLogstate());
                        } else {
                            walnutCommon.setLogstate(logstate);
                        }
                    } else {
                        walnutCommon.setLogstate("1");
                    }
                    walnutCommonList.remove(i);
                    walnutCommonList.add(i, walnutCommon);
                }

                data.put("dataList", walnutCommonList);
                data.put("total", total);
                result.setData(data);
                result.setCode(200);
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setData(e.getMessage());
            result.setCode(300);
        }
        return result;
    }

    public static List<String> getDiffrent(List<String> list1, List<String> list2) {
        List<String> diff = new ArrayList<String>();
        long start = System.currentTimeMillis();
        Map<String, Integer> map = new HashMap<String, Integer>(list1.size() + list2.size());
        List<String> maxList = list1;
        List<String> minList = list2;
        if (list2.size() > list1.size()) {
            maxList = list2;
            minList = list1;
        }
        for (String string : maxList) {
            map.put(string, 1);
        }
        for (String string : minList) {
            Integer count = map.get(string);
            if (count != null) {
                map.put(string, ++count);
                continue;
            }
            map.put(string, 1);
        }
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            if (entry.getValue() == 1) {
                diff.add(entry.getKey());
            }
        }
        System.out.println("方法4 耗时：" + (System.currentTimeMillis() - start) + " 毫秒");
        return diff;

    }
}
