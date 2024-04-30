package com.example.modules.shenkang.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.TypeUtils;
import com.example.modules.shenkang.mapper.CommonDao;
import com.example.modules.shenkang.mapper.DebugLogDao;
import com.example.modules.shenkang.mapper.MedicalSimpleDao;
import com.example.modules.shenkang.mapper.WanDaPushDao;
import com.example.modules.shenkang.pojo.*;
import com.example.modules.shenkang.service.RestTemplateToInterface;
import com.example.modules.shenkang.service.WanDaPushService;
import com.example.modules.shenkang.utils.ShenkangUtils;
import com.example.utils.MD5;
import com.example.utils.encrypt.DesUtil;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class WanDaPushServiceImpl implements WanDaPushService {
    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final Logger logger = LoggerFactory.getLogger(RestTemplateToInterface.class);

    @Autowired
    MedicalSimpleDao medicalSimpleDao;


    @Value("${shenkang.hospitalCode}")
    private String hospitalCode;

    @Autowired
    private WanDaPushDao wanDaPushDao;

    @Autowired
    private DebugLogDao debugLogDao;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private CommonDao commonDao;

    @Value("${shenkang.imageUploadPath}")
    private String imageUploadPath;

    @Value("${shenkang.webApiUrl.WandaPushFile}")
    private String wandaPushFile;

    @Value("${shenkang.webApiUrl.WandaRequestTokenUrl}")
    private String wandaRequestTokenUrl;

    @Value("${shenkang.webApiUrl.WandaRegisterUrl}")
    private String wandaRegisterUrl;

    @Value("${shenkang.webApiUrl.WandaMdeicalUrl}")
    private String wandaMdeicalUrl;

    @Value("${shenkang.webApiUrl.WandaRequestCheckUrl}")
    private String wandaRequestCheckUrl;

    public String uoloadFiles(HFileInfo fileInfo, String BusinessId) {
        String filePath = fileInfo.getFilePath();
        Map<String, String> pararm = new HashMap<>();
        pararm.put("BusinessId", BusinessId);
        pararm.put("BusinessType", "Exam");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = dateFormat.format(new Date());
        System.out.println("时间" + format);
        pararm.put("BusinessTime", format);
        pararm.put("ClassCode", "Exam");
        pararm.put("TypeCode", fileInfo.getFileType());
        switch (fileInfo.getFileType()) {
            case "ExamImage":
                pararm.put("FormatCode", "SECTION");
                break;
            case "ExamImageROI":
                pararm.put("FormatCode", "SECTIONROI");
                break;
            default:
                pararm.put("FormatCode", "/");
                break;
        }
        pararm.put("Tile", "");
        if (fileInfo.getFileFid() != 0) {
            //获取
            System.out.println("FileFiD" + fileInfo.getFileFid());
            String ss = commonDao.selectFUID(fileInfo.getFileFid());
            System.out.println(ss + "ParentFileUIDParentFileUIDParentFileUIDParentFileUIDParentFileUIDParentFileUID");
            pararm.put("ParentFileUID", ss);
        } else {
            pararm.put("ParentFileUID", "");
        }

        pararm.put("OrganizationID", hospitalCode);
        pararm.put("DeleteExist", "0");
        pararm.put("Timestamp", new Date().getTime() + "");
        pararm.put("FileName", fileInfo.getFileName());
        pararm.put("Token", MD5.getMD5String(BusinessId + "Exam" + format + "Exam" + "ExamImage" + "SECTION" + "" + "Y0041800100" + 0 + new Date().getTime()));

        Map<String, String> fileMap = new HashMap<String, String>();
        fileMap.put("upfile", filePath);
        logger.info("SSSSSSSSS" + pararm + fileMap);


        System.out.println(com.alibaba.fastjson.JSONObject.toJSONString(pararm));
        String url = wandaPushFile;
        //记录调用接口信息
        logger.info("requestUrl:" + url);
        logger.info("requestParam:" + url);
        WanDaAPIReq_2 wanDaAPIReq = new WanDaAPIReq_2();
        wanDaAPIReq.setRequestDetails(pararm.toString());
        wanDaAPIReq.setWebAPI("pushfile");
        wanDaAPIReq.setCreatime(ShenkangUtils.getTime());
        //将信息插入数据库
        wanDaPushDao.insertIntoWanDaAPIDetails(wanDaAPIReq);
        //开始请求
        String s = null;
        try {
            s = formUpload(url, pararm, fileMap);
        } catch (Exception e) {
            logger.info("requestParam is failed");
            wanDaAPIReq.setResponseDetails("error");
            Integer i = wanDaPushDao.updateWanDaAPIDetails(wanDaAPIReq);
        }
        com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(s);
        //记录返回信息
        logger.info("requestParam:" + jsonObject.toString());
        wanDaAPIReq.setResponseDetails(jsonObject.toJSONString());
        Integer i = wanDaPushDao.updateWanDaAPIDetails(wanDaAPIReq);
        String fileUid = (String) jsonObject.get("FileUID");
        commonDao.upFileUidById(fileInfo.getFileId(), fileUid);
        return fileUid;
    }

    /**
     * @author:
     * @param:
     * @return:
     * @Description:正向数据校验，接口请求地址在wandaRequestCheckUrl里面设置
     */
    public Result requestCheck(Integer id) throws RuntimeException {
        Result result = new Result();
        String url = wandaRequestCheckUrl;
        MedicalInfo medicalInfo = commonDao.selectinfoByMedId(id);
        String pathological_number = medicalInfo.getPathological_number();
        String organization_id = hospitalCode;
        String token = commonDao.selecrToken();
        Map<String, Object> re = new HashMap<>();
        List<Map<String, Object>> fs = new ArrayList<>();
        List<Map<String, Object>> lists = commonDao.selectFileTypeNum(id);
        re.put("pathological_number", pathological_number);
        re.put("organization_id", organization_id);
        re.put("upload_file", lists);
        com.alibaba.fastjson.JSONObject object = (com.alibaba.fastjson.JSONObject) JSON.toJSON(re);
        String request = request(url, object, token, 1,"filecheck");
        com.alibaba.fastjson.JSONObject jsonObject1 = com.alibaba.fastjson.JSONObject.parseObject(request);
        logger.info("校验数据");
        if (jsonObject1.getString("msg").equals("OK")) {
            result.setCode(200);
        }
        result.setMsg(jsonObject1.toString());
        result.setData(re);
        return result;
    }

    /**
     * @author:
     * @param:
     * @return:
     * @Description:调用接口并实现文件上传
     */
    public Result postFileNew(Integer medId, String BusinessId) {
        Result result = new Result();
        try {

            List<String> re = new ArrayList<>();
            List<HFileInfo> fileInfos = commonDao.selectFileIdByApplyID03(medId, "ExamImage");

            Map<Integer, String> fileReMap = new HashMap<>();
            for (int i = 0; i < fileInfos.size(); i++) {
                HFileInfo fileInfo = fileInfos.get(i);
                String s = uoloadFiles(fileInfo, BusinessId);
                fileReMap.put(fileInfo.getFileId(), s);
            }

            List<HFileInfo> fileInfos02 = commonDao.selectFileIdByApplyID03(medId, "ExamImageROI");
            for (int i = 0; i < fileInfos02.size(); i++) {
                HFileInfo fileInfo = fileInfos02.get(i);
                String s = uoloadFiles(fileInfo, BusinessId);
            }
            result.setData(re);
            result.setCode(200);
            return result;
        } catch (Exception e) {
            result.setMsg("未知错误");
            result.setCode(300);
            logger.error("File upload error！");
        }
        return result;
    }

    public Result postFileNew(Integer medId, String BusinessId,String pathNo) {
        Result result = new Result();
        try {
            result.setCode(200);

            List<String> re = new ArrayList<>();
            List<HFileInfo> fileInfos = commonDao.selectFileIdByApplyID03(medId, "ExamImage");

            Map<Integer, String> fileReMap = new HashMap<>();
            for (int i = 0; i < fileInfos.size(); i++) {
                HFileInfo fileInfo = fileInfos.get(i);
                fileInfo.setFilePath(imageUploadPath + fileInfo.getFilePath());
                String s = uoloadFiles(fileInfo, BusinessId,pathNo);
                fileReMap.put(fileInfo.getFileId(), s);
                if(s==null){
                    result.setCode(300);
                }
            }

            List<HFileInfo> fileInfos02 = commonDao.selectFileIdByApplyID03(medId, "ExamImageROI");
            for (int i = 0; i < fileInfos02.size(); i++) {
                HFileInfo fileInfo = fileInfos02.get(i);
                fileInfo.setFilePath(imageUploadPath + fileInfo.getFilePath());
                String s = uoloadFiles(fileInfo, BusinessId,pathNo);
                if(s==null){
                    result.setCode(300);
                }
            }
            result.setData(re);
            return result;
        } catch (Exception e) {
            result.setMsg("未知错误");
            result.setCode(300);
            logger.error("File upload error！");
        }
        return result;
    }


    public String uoloadFiles(HFileInfo fileInfo, String BusinessId,String pathNo) {
        String filePath = fileInfo.getFilePath();
        Map<String, String> pararm = new HashMap<>();
        pararm.put("BusinessId", BusinessId);
        pararm.put("BusinessType", "Exam");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = dateFormat.format(new Date());
        System.out.println("时间" + format);
        pararm.put("BusinessTime", format);
        pararm.put("ClassCode", "Exam");
        pararm.put("TypeCode", fileInfo.getFileType());
        switch (fileInfo.getFileType()) {
            case "ExamImage":
                pararm.put("FormatCode", "SECTION");
                break;
            case "ExamImageROI":
                pararm.put("FormatCode", "SECTIONROI");
                break;
            default:
                pararm.put("FormatCode", "/");
                break;
        }
        pararm.put("Tile", "");
        if (fileInfo.getFileFid() != 0) {
            //获取
            System.out.println("FileFiD" + fileInfo.getFileFid());
            String ss = commonDao.selectFUID(fileInfo.getFileFid());
            //System.out.println(ss + "ParentFileUIDParentFileUIDParentFileUIDParentFileUIDParentFileUIDParentFileUID");
            pararm.put("ParentFileUID", ss);
        } else {
            pararm.put("ParentFileUID", "");
        }

        pararm.put("OrganizationID", hospitalCode);
        pararm.put("DeleteExist", "0");
        pararm.put("Timestamp", new Date().getTime() + "");
        pararm.put("FileName", fileInfo.getFileName());
        pararm.put("Token", MD5.getMD5String(BusinessId + "Exam" + format + "Exam" + "ExamImage" + "SECTION" + "" + "Y0041800100" + 0 + new Date().getTime()));

        Map<String, String> fileMap = new HashMap<String, String>();
        fileMap.put("upfile", filePath);
        logger.info("SSSSSSSSS" + pararm + fileMap);


        //System.out.println(com.alibaba.fastjson.JSONObject.toJSONString(pararm));
        String url = wandaPushFile;
        //记录调用接口信息
        logger.info("requestUrl:" + url);
        logger.info("requestParam:" + url);
        WanDaAPIReq_2 wanDaAPIReq = new WanDaAPIReq_2();
        wanDaAPIReq.setRequestDetails(com.alibaba.fastjson.JSONObject.toJSONString(pararm));
        wanDaAPIReq.setWebAPI("pushfile");
        wanDaAPIReq.setPathId(pathNo);
        wanDaAPIReq.setCreatime(ShenkangUtils.getTime());

        //开始请求
        String s = null;
        try {
            s = formUpload(url, pararm, fileMap);
            com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(s);
            //将信息插入数据库
            logger.info("requestParam:" + jsonObject.toString());
            wanDaAPIReq.setResponseDetails(jsonObject.toJSONString());
            wanDaPushDao.insertIntoWanDaAPIDetails(wanDaAPIReq);
            String fileUid = (String) jsonObject.get("FileUID");
            commonDao.upFileUidById(fileInfo.getFileId(), fileUid);
            return fileUid;
        } catch (Exception e) {
            logger.info("requestParam is failed");
            wanDaAPIReq.setResponseDetails("error");
            wanDaAPIReq.setLogstate("1");
            wanDaPushDao.insertIntoWanDaAPIDetails(wanDaAPIReq);
        }
        return null;
    }


    /**
     * @author:
     * @param:
     * @return:
     * @Description:推送推送病理信息
     */
    public Result updataMedicalInfo(MedicalInfo medicalInfo) {
        TypeUtils.compatibleWithJavaBean = true;
        Result result = new Result();
        try {
            String url = wandaMdeicalUrl;
            com.alibaba.fastjson.JSONObject object = (com.alibaba.fastjson.JSONObject) JSON.toJSON(medicalInfo);
            System.out.println("===========================");
            System.out.println(object);
            System.out.println("===========================");
            String token = commonDao.selecrToken();
            String request = request(url, object, token, 0,"pushMedical");
            com.alibaba.fastjson.JSONObject jsonObject1 = com.alibaba.fastjson.JSONObject.parseObject(request);
            System.out.println(jsonObject1);
            logger.info("病例信息返回接口", jsonObject1);
            if ((Integer) jsonObject1.get("code") == 0 && "OK".equals(jsonObject1.get("msg"))) {
                result.setData(jsonObject1.get("data"));
                result.setCode(200);
                System.out.println();
            } else {
                result.setCode(300);
                logger.error("病理信息获取失败！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(500);
            result.setException(e);
        }
        return result;
    }

    public String request(String url, com.alibaba.fastjson.JSONObject obj, String token, Integer state,String webApiType) {
        TypeUtils.compatibleWithJavaBean = true;
        //header参数
        HttpHeaders headers = new HttpHeaders();
//        headers.add("Authorization", Base64.getUrlEncoder().encodeToString(("default" + ":" + "1D812578F93BE288122CE828AA1FBEFD").getBytes()));
        headers.add("Authorization", "bearer " + token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        //组装
        HttpEntity<com.alibaba.fastjson.JSONObject> request = new HttpEntity<>(obj, headers);
        //记录调用接口信息
        logger.info("requestUrl:" + url);
        String[] urlTemp = url.split("/");
        logger.info("requestParam:" + JSON.toJSONString(request));
        WanDaAPIReq_2 wanDaAPIReq = new WanDaAPIReq_2();
        wanDaAPIReq.setRequestDetails(JSON.toJSONString(request));
        wanDaAPIReq.setWebAPI(webApiType);
        wanDaAPIReq.setCreatime(ShenkangUtils.getTime());
        String pathNo = (String) obj.get("pathNo");
        if(pathNo==null || pathNo.length()==0){
            pathNo = (String) obj.get("pathological_number");
        }
        wanDaAPIReq.setPathId(pathNo);
        wanDaAPIReq.setState(state);
        if (state == 0) {
            //根据pathId 查询searchData
            String time = commonDao.selectSearchTimeByPathNo(pathNo);
            wanDaAPIReq.setReq_updateDateTime(time);
        }
        //将信息插入数据库
        wanDaPushDao.insertIntoWanDaAPIDetails(wanDaAPIReq);
        ResponseEntity<String> responseEntity = null;
        String logstate="0";
        try {
            responseEntity = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
            if(webApiType.equals("pushPatient")||webApiType.equals("pushMedical")||webApiType.equals("filecheck")){
                if(!ShenkangUtils.isWandaFormat(com.alibaba.fastjson.JSONObject.parseObject(responseEntity.getBody().toString()))){
                    logstate="2";
                }
            }
            wanDaAPIReq.setLogstate(logstate);
        } catch (Exception e) {
            logger.error("请求返回异常!" + e.getMessage());
            wanDaAPIReq.setResponseDetails("请求返回异常:" + e.getMessage());
            logstate="1";
            wanDaAPIReq.setLogstate(logstate);
            Integer i = wanDaPushDao.updateWanDaAPIDetails(wanDaAPIReq);
            return "failed";
        }
        String body = responseEntity.getBody();
        logger.info("responseResult:" + body);
        wanDaAPIReq.setResponseDetails(body);
        Integer i = wanDaPushDao.updateWanDaAPIDetails(wanDaAPIReq);
        System.out.println("BODY" + body);
        return body;
    }

    /**
     * @author:
     * @param:
     * @return:
     * @Description:调用接口wandaRegisterUrl，推送患者信息注册
     */
    @Override
    public Result registerPatient(PatientInfo patientInfo) {
        Result result = new Result();
        try {
            String url = wandaRegisterUrl;
            com.alibaba.fastjson.JSONObject object = (com.alibaba.fastjson.JSONObject) JSON.toJSON(patientInfo);

            String token = commonDao.selecrToken();
            String request = request(url, object, token, 0,"pushPatient");
            com.alibaba.fastjson.JSONObject jsonObject1 = com.alibaba.fastjson.JSONObject.parseObject(request);
            System.out.println("===========================");
            System.out.println(jsonObject1);
            System.out.println("===========================");
            logger.info("上传病人信息返回接口:", jsonObject1);
            System.out.println("接收信息$$$$$$$$$" + jsonObject1.toString());
            if ((Integer) jsonObject1.get("code") == 0 && "OK".equals(jsonObject1.get("msg"))) {
                Map<String, String> data = (Map<String, String>) jsonObject1.get("data");
                result.setData(data.get("patient_master_id"));
                System.out.println("sssssssssssssssssssss+" + data);
                result.setCode(200);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(500);
            logger.error("上传病人信息异常！");
            result.setException(e);
        }
        return result;
    }

    /**
     * @author:
     * @param:
     * @return:
     * @Description:获取万达接口中的token数据
     */
    public String getAccessToken() {
        System.out.println(wandaRequestTokenUrl);
        String request = HttpPostData(wandaRequestTokenUrl, "grant_type=client_credentials");
        com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(request);
        commonDao.insertToken((String) jsonObject.get("access_token"));
        return (String) jsonObject.get("access_token");
    }

    public String HttpPostData(String uri, String obj) {
        WanDaAPIReq_2 wanDaAPIReq = new WanDaAPIReq_2();
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(uri);
            //添加http头信息
            httppost.setHeader("Authorization", "Basic " + Base64.getEncoder().encodeToString(("default" + ":" + "1D812578F93BE288122CE828AA1FBEFD").getBytes("UTF-8"))); //认证token
            httppost.setHeader("Content-Type", "text/plain");
            StringEntity myEntity = new StringEntity(obj, ContentType.DEFAULT_TEXT);
            httppost.setEntity(myEntity);
            HttpResponse response;
            response = httpclient.execute(httppost);
            String s = EntityUtils.toString(response.getEntity());
            //检验状态码，如果成功接收数据
            int code = response.getStatusLine().getStatusCode();
            System.out.println(code + "code");
            System.out.println(s + "ssss");
            //记录日志
            Map<String, Object> map = new HashMap<>();
            map.put("header", JSON.toJSON(httppost.getAllHeaders()));
            map.put("Entity", httppost.getEntity());
            wanDaAPIReq.setRequestDetails(map.toString());
            wanDaAPIReq.setResponseDetails(s);
            wanDaAPIReq.setWebAPI("LoginInfo");
            wanDaAPIReq.setCreatime(ShenkangUtils.DataNow());
            String logstate="0";
            if(!ShenkangUtils.isTokenFormat(com.alibaba.fastjson.JSONObject.parseObject(s))){
                logstate="2";
            }
            wanDaAPIReq.setLogstate(logstate);
            return s;
        } catch (ClientProtocolException e) {
            wanDaAPIReq.setResponseDetails(e.getMessage());
        } catch (IOException e) {
            wanDaAPIReq.setResponseDetails(e.getMessage());
        } catch (Exception e) {
            wanDaAPIReq.setResponseDetails(e.getMessage());
        }finally {
            wanDaPushDao.insertIntoWanDaAPIDetails(wanDaAPIReq);
        }
        return null;
    }

    public Map<String, Boolean> fillMapcheckDocument(List<Map<String, Object>> fileList) {
        Map<String, Boolean> map = new HashMap<>();
        map.put("ExamRequest", false);
        map.put("InformedConsent", false);
        map.put("ExamResult", false);
        map.put("ExamGeneral", false);
        map.put("ExamImageROI", false);
        map.put("ExamImage", false);
        if (fileList != null && fileList.size() != 0) {
            for (int i = 0; i < fileList.size(); i++) {
                Map<String, Object> temp02 = fileList.get(i);
                Integer tempnum = (Integer) temp02.get("document_count");
                switch ((String) temp02.get("document_type")) {
                    case "ExamRequest":
                        map.replace("ExamRequest", true);
                        break;
                    case "InformedConsent":
                        map.replace("InformedConsent", true);
                        break;
                    case "ExamResult":
                        map.replace("ExamResult", true);
                        break;
                    case "ExamGeneral":
                        map.replace("ExamGeneral", true);
                        break;
                    case "ExamImageROI":
                        map.replace("ExamImageROI", true);
                        break;
                    case "ExamImage":
                        map.replace("ExamImage", true);
                        break;
                }
            }
        }
        return map;
    }

    /**
     * @author:
     * @param:
     * @return:
     * @Description:反向数据校验，万达调用我们的接口
     */
    public Result check(String pathological_number, List<Map<String, Object>> fileList, String oriId) {
        Result result = new Result();
        if (pathological_number == null) {
            result.setCode(300);
            result.setMsg("参数pathological_number获取失败！");
            return result;
        }
        if (oriId == null) {
            result.setCode(200);
            result.setMsg("参数oriId获取失败！");
            return result;
        }
        if (!oriId.equals(hospitalCode)) {
            result.setCode(200);
            result.setMsg("请验证数据来源");
            return result;
        }
        //获取上传值
        Integer id = medicalSimpleDao.selectmedIdByPathNo(pathological_number);
        if (id == null) {
            result.setCode(200);
            result.setMsg("没有此病例");
            return result;
        }
        List<Map<String, Object>> lists = commonDao.selectFileTypeNum(id);
        long ExamRequestNum = 0, InformedConsentNum = 0, ExamResultNum = 0, ExamGeneralNum = 0, ExamImageROINum = 0, ExamImageNum = 0;
        boolean tempType = true;
        if (lists != null && fileList != null) {
            Map<String, Boolean> map = fillMapcheckDocument(fileList);
            if (lists.size() != 0 && fileList.size() != 0) {
                for (int i = 0; i < lists.size(); i++) {
                    Map<String, Object> temp = lists.get(i);
                    temp.get("document_type");
                    switch ((String) temp.get("document_type")) {
                        case "ExamRequest":
                            if (!map.get("ExamRequest")) {
                                result.setMsg("上传文件不正确");
                                result.setCode(-1);
                                return result;
                            }
                            ExamRequestNum = (Long) temp.get("document_count");
                            break;
                        case "InformedConsent":
                            if (!map.get("InformedConsent")) {
                                result.setMsg("上传文件不正确");
                                result.setCode(-1);
                                return result;
                            }
                            InformedConsentNum = (Long) temp.get("document_count");
                            break;
                        case "ExamResult":
                            if (!map.get("ExamResult")) {
                                result.setMsg("上传文件不正确");
                                result.setCode(-1);
                                return result;
                            }
                            ExamResultNum = (Long) temp.get("document_count");
                            break;
                        case "ExamGeneral":
                            if (!map.get("ExamGeneral")) {
                                result.setMsg("上传文件不正确");
                                result.setCode(-1);
                                return result;
                            }
                            ExamGeneralNum = (Long) temp.get("document_count");
                            break;
                        case "ExamImageROI":
                            if (!map.get("ExamImageROI")) {
                                result.setMsg("上传文件不正确");
                                result.setCode(-1);
                                return result;
                            }
                            ExamImageROINum = (Long) temp.get("document_count");
                            break;
                        case "ExamImage":
                            if (!map.get("ExamImage")) {
                                result.setMsg("上传文件不正确");
                                result.setCode(-1);
                                return result;
                            }
                            ExamImageNum = (Long) temp.get("document_count");
                            break;
                    }
                }
                for (int i = 0; i < fileList.size(); i++) {
                    Map<String, Object> temp02 = fileList.get(i);
                    Integer tempnum = (Integer) temp02.get("document_count");
                    switch ((String) temp02.get("document_type")) {
                        case "ExamRequest":
                            if (ExamRequestNum != tempnum) {
                                tempType = false;
                            }
                            break;
                        case "InformedConsent":
                            if (InformedConsentNum != tempnum) {
                                tempType = false;
                            }
                            break;
                        case "ExamResult":
                            if (ExamResultNum != tempnum) {
                                tempType = false;
                            }
                            break;
                        case "ExamGeneral":
                            if (ExamGeneralNum != tempnum) {
                                tempType = false;
                            }
                            break;
                        case "ExamImageROI":
                            if (ExamImageROINum != tempnum) {
                                tempType = false;
                            }
                            break;
                        case "ExamImage":
                            if (ExamImageNum != tempnum) {
                                tempType = false;
                            }
                            break;
                        default:
                            tempType = false;
                            break;
                    }
                }
                if (tempType) {
                    result.setMsg("OK");
                    result.setCode(0);
                } else {
                    result.setMsg("上传文件不正确");
                    result.setCode(-1);
                }
            } else if (lists.size() == 0 && fileList.size() != 0) {
                tempType = true;
                for (int i = 0; i < fileList.size(); i++) {
                    Map<String, Object> temp02 = fileList.get(i);
                    Integer tempnum = (Integer) temp02.get("document_count");
                    switch ((String) temp02.get("document_type")) {
                        case "ExamRequest":
                        case "InformedConsent":
                        case "ExamResult":
                        case "ExamGeneral":
                        case "ExamImageROI":
                        case "ExamImage":
                        default:
                            if (!tempnum.equals(0)) {
                                tempType = false;
                            }
                            break;
                    }
                }
                if (tempType) {
                    result.setMsg("OK");
                    result.setCode(0);
                } else {
                    result.setMsg("上传文件不正确");
                    result.setCode(-1);
                }
            } else if(lists.size() != 0 && fileList.size() == 0){
                result.setMsg("上传文件不正确");
                result.setCode(-1);
            }else {
                result.setMsg("OK");
                result.setCode(0);
            }
        } else if (lists == null && fileList == null) {
            result.setMsg("OK");
            result.setCode(0);
        } else {
            result.setMsg("上传文件不正确");
            result.setCode(-1);
        }
        return result;
    }

    /**
     * @author: 2020/12/25 10:47
     * @param:
     * @return:
     * @Description:获取文件推送列表推送文件
     */
    public Result postFiles(String strArray){
        Result result=new Result();
        result.setCode(200);
        //查询出当前的文档,判断是否是子文件，如果是子文件则查看父文件是否上传，如果没有上传则上传，如果已经上传则再上传子文件
        //根据id查询出文件信息
        try{
            HFileInfo fileInfo= commonDao.selectFileByFileid(strArray);
            fileInfo.setFilePath(imageUploadPath + fileInfo.getFilePath());
            if(fileInfo!=null){
                if(fileInfo.getFileFid()!=0){//是子标签
                    HFileInfo fileInfoFather=commonDao.selectFileByFileid(fileInfo.getFileFid().toString());
                    fileInfoFather.setFilePath(imageUploadPath + fileInfo.getFilePath());
                    if(!(fileInfoFather.getFileUID()!=null && fileInfoFather.getFileUID().length()!=0)){
                        //推送父亲文件
                        com.alibaba.fastjson.JSONObject s = uoloadFilesAndLog(fileInfoFather, fileInfoFather.getObservationUID(),fileInfoFather.getPathNo());
                        if(s==null)
                            result.setCode(300);
                    }
                }
                com.alibaba.fastjson.JSONObject s2 = uoloadFilesAndLog(fileInfo, fileInfo.getObservationUID(),fileInfo.getPathNo());
                if(s2==null)
                    result.setCode(300);
            }else{
                result.setCode(300);
            }
        }catch(Exception e){
            e.printStackTrace();
            result.setCode(300);
        }
        return result;
    }

    public com.alibaba.fastjson.JSONObject uoloadFilesAndLog(HFileInfo fileInfo, String BusinessId,String pathNo) {
        Map<String, Object> map = uploadFilesResp(fileInfo, BusinessId,pathNo);
        com.alibaba.fastjson.JSONObject jsonObject = (com.alibaba.fastjson.JSONObject) map.get("jsonObject");
        logger.info("requestParam:" + jsonObject.toString());
        String fileUid = (String) jsonObject.get("FileUID");
        commonDao.upFileUidById(fileInfo.getFileId(), fileUid);
        return jsonObject;
    }

    public Map<String, Object> uploadFilesResp(HFileInfo fileInfo, String BusinessId,String pathNO) {
        String filePath = fileInfo.getFilePath();
        Map<String, String> pararm = new HashMap<>();
        pararm.put("BusinessId", BusinessId);
        pararm.put("BusinessType", "Exam");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = dateFormat.format(new Date());
        System.out.println("时间" + format);
        pararm.put("BusinessTime", format);
        pararm.put("ClassCode", "Exam");
        pararm.put("TypeCode", fileInfo.getFileType());
        switch (fileInfo.getFileType()) {
            case "ExamImage":
                pararm.put("FormatCode", "SECTION");
                break;
            case "ExamImageROI":
                pararm.put("FormatCode", "SECTIONROI");
                break;
            default:
                pararm.put("FormatCode", "/");
                break;
        }
        pararm.put("Tile", "");
        if (fileInfo.getFileFid() != 0) {
            //获取
            String ss = commonDao.selectFUID(fileInfo.getFileFid());
            pararm.put("ParentFileUID", ss);
        } else {
            pararm.put("ParentFileUID", "");
        }
        pararm.put("OrganizationID", hospitalCode);
        pararm.put("DeleteExist", "0");
        pararm.put("Timestamp", new Date().getTime() + "");
        pararm.put("FileName", fileInfo.getFileName());
        pararm.put("Token", MD5.getMD5String(BusinessId + "Exam" + format + "Exam" + "ExamImage" + "SECTION" + "" + "Y0041800100" + 0 + new Date().getTime()));
        Map<String, String> fileMap = new HashMap<String, String>();
        fileMap.put("upfile", filePath);
        logger.info("SSSSSSSSS" + pararm + fileMap);
        String url = wandaPushFile;
        //记录调用接口信息
        logger.info("requestUrl:" + url);
        logger.info("requestParam:" + url);
        WanDaAPIReq_2 wanDaAPIReq = new WanDaAPIReq_2();
        wanDaAPIReq.setRequestDetails(com.alibaba.fastjson.JSONObject.toJSONString(pararm));
        //String[] urlTemp = url.split("/");
        wanDaAPIReq.setWebAPI("pushfile");
        wanDaAPIReq.setPathId(pathNO);
        wanDaAPIReq.setCreatime(ShenkangUtils.getTime());

        //开始请求
        String s = null;
        try {
            s = formUpload(url, pararm, fileMap);
            wanDaAPIReq.setResponseDetails(JSON.parseObject(s).toJSONString());
            //将信息插入数据库
            if(s.contains("FileSize"))
                wanDaAPIReq.setLogstate("0");
            else
                wanDaAPIReq.setLogstate("2");
            wanDaPushDao.insertIntoWanDaAPIDetails(wanDaAPIReq);
        } catch (Exception e) {
            logger.info("requestParam is failed");
            wanDaAPIReq.setResponseDetails(e.getStackTrace().toString());
            wanDaAPIReq.setLogstate("2");
            wanDaPushDao.insertIntoWanDaAPIDetails(wanDaAPIReq);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("jsonObject", JSON.parseObject(s));
        map.put("wanDaAPIReq", wanDaAPIReq);
        return map;
    }

    public String formUpload(String urlStr, Map<String, String> textMap,
                             Map<String, String> fileMap) {
        String res = "";
        HttpURLConnection conn = null;
        // boundary就是request头和上传文件内容的分隔符
        String BOUNDARY = "---------------------------123821742118716";
        try {
            URL url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(900000000);
            conn.setReadTimeout(900000000);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            // conn.setRequestProperty("User-Agent","Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
            OutputStream out = new DataOutputStream(conn.getOutputStream());
            // text
            if (textMap != null) {
                StringBuffer strBuf = new StringBuffer();
                Iterator iter = textMap.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter.next();
                    String inputName = (String) entry.getKey();
                    String inputValue = (String) entry.getValue();
                    if (inputValue == null) {
                        continue;
                    }
                    strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
                    strBuf.append("Content-Disposition: form-data; name=\"" + inputName + "\"\r\n\r\n");
                    strBuf.append(inputValue);
                }
                out.write(strBuf.toString().getBytes());
            }
            // file
            if (fileMap != null) {
                Iterator iter = fileMap.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter.next();
                    String inputName = (String) entry.getKey();
                    String inputValue = (String) entry.getValue();
                    if (inputValue == null) {
                        continue;
                    }
                    File file = new File(inputValue);
                    String filename = file.getName();
                    StringBuffer strBuf = new StringBuffer();
                    strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
                    strBuf.append("Content-Disposition: form-data; name=\"" + inputName + "\"; filename=\"" + filename + "\"\r\n");
                    strBuf.append("Content-Type:" + "multipart/form-data" + "\r\n\r\n");
                    out.write(strBuf.toString().getBytes());
                    DataInputStream in = new DataInputStream(new FileInputStream(file));
                    int bytes = 0;
                    byte[] bufferOut = new byte[1024];
                    while ((bytes = in.read(bufferOut)) != -1) {
                        out.write(bufferOut, 0, bytes);
                    }
                    in.close();
                }
            }
            byte[] endData = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
            out.write(endData);
            out.flush();
            out.close();
            // 读取返回数据
            StringBuffer strBuf = new StringBuffer();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                strBuf.append(line).append("\n");
            }
            res = strBuf.toString();
            reader.close();
            reader = null;
        } catch (Exception e) {
            System.out.println("发送POST请求出错。" + urlStr);
            e.printStackTrace();
            logger.error("发送POST请求出错。上传文件异常！");
        } finally {
            if (conn != null) {
                conn.disconnect();
                conn = null;
            }
        }

        return res;
    }

    /**
     * @author: 邵梦丽 on 2020/9/1 9:40
     * @param:
     * @return:
     * @Description:设置based_time取材时间，如果based_time有时间，则不做修改， 如果没有时间，则根据reg_time和result_principal_time取值
     * 1.取出reg_time和result_principal_time的平均值
     * 2.取result_principal_time的前十分钟
     * 3.取reg_time的后十分钟
     */
    @Override
    public MedicalInfo setBased_time(MedicalInfo medicalInfo) {
        String based_time = medicalInfo.getBased_time();
        if (based_time == null || based_time.trim().length() == 0) {
            String reg_time = medicalInfo.getReg_time();
            String result_principal_time = medicalInfo.getResult_principal_time();
            long timestamp;
            if (result_principal_time != null && result_principal_time.trim().length() != 0) {
                LocalDateTime localResultPrincipalTime = LocalDateTime.parse(result_principal_time, dtf);
                long localResultPrincipalTimestamp = localResultPrincipalTime.toInstant(ZoneOffset.of("+8")).toEpochMilli();
                if (reg_time != null && reg_time.trim().length() != 0) {
                    LocalDateTime localRegTime = LocalDateTime.parse(reg_time, dtf);
                    long localRegtimestamp = localRegTime.toInstant(ZoneOffset.of("+8")).toEpochMilli();
                    //取中间时间点
                    timestamp = (localRegtimestamp + localResultPrincipalTimestamp) / 2;
                } else {
                    //取十分钟之前的数据
                    timestamp = localResultPrincipalTimestamp - 10 * 60 * 1000;
                }
                LocalDateTime localBased_id = LocalDateTime.ofEpochSecond(timestamp / 1000, 0, ZoneOffset.ofHours(8));
                based_time = dtf.format(localBased_id);
                medicalInfo.setBased_time(based_time);
            } else {
                if (reg_time != null && reg_time.trim().length() != 0) {
                    LocalDateTime localRegTime = LocalDateTime.parse(reg_time, dtf);
                    long localRegtimestamp = localRegTime.toInstant(ZoneOffset.of("+8")).toEpochMilli();
                    //取十分钟之后的据
                    timestamp = localRegtimestamp + 10 * 60 * 1000;
                    LocalDateTime localBased_id = LocalDateTime.ofEpochSecond(timestamp / 1000, 0, ZoneOffset.ofHours(8));
                    based_time = dtf.format(localBased_id);
                    medicalInfo.setBased_time(based_time);
                } else {
                    //不做任何处理
                }
            }
        }
        return medicalInfo;
    }

    public MedicalInfo encryptMedicalInfo(MedicalInfo medicalInfoTemp) {
        MedicalInfo medicalInfo = JSON.parseObject(JSON.toJSONString(medicalInfoTemp), MedicalInfo.class);
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
        if (medicalInfo.getOrganization_id() != null && !"".equals(medicalInfo.getOrganization_id())) {
            medicalInfo.setOrganization_id(hospitalCode);
        }
        return medicalInfo;
    }

    /**
     * @author:
     * @param:
     * @return: 调试  专用
     * @Description:http请求设置
     */
    public Map<String,Object> requestForInterfaceMap(String url, com.alibaba.fastjson.JSONObject obj, String token, Integer state) {
        TypeUtils.compatibleWithJavaBean = true;
        //header参数
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "bearer " + token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        //组装
        HttpEntity<JSONObject> request = new HttpEntity<>(obj, headers);
        //记录调用接口信息
        logger.info("requestUrl:" + url);
        //将信息插入数据库
        ResponseEntity<String> responseEntity = null;
        try {
            responseEntity = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
        } catch (Exception e) {
            logger.error("请求返回异常!" + e.getMessage());
            return null;
        }
        String body = responseEntity.getBody();
        logger.info("responseResult:" + body);
        System.out.println("BODY" + body);
        HashMap<String,Object> map=new HashMap<>();
        map.put("body",body);
        map.put("all",request);
        return map;
    }


    public MedicalInfo desctMedicalInfo(MedicalInfo medicalInfoTemp) throws Exception {
        MedicalInfo medicalInfo = JSON.parseObject(JSON.toJSONString(medicalInfoTemp), MedicalInfo.class);
        if (medicalInfo.getMed_rec_no() != null && !"".equals(medicalInfo.getMed_rec_no())) {
            medicalInfo.setMed_rec_no(DesUtil.decrypt(medicalInfo.getMed_rec_no()));
        }
        if (medicalInfo.getOut_patient_no() != null && !"".equals(medicalInfo.getOut_patient_no())) {
            medicalInfo.setOut_patient_no(DesUtil.decrypt(medicalInfo.getOut_patient_no() + ""));
        }
        if (medicalInfo.getIn_patient_no() != null && !"".equals(medicalInfo.getIn_patient_no())) {
            medicalInfo.setIn_patient_no(DesUtil.decrypt(medicalInfo.getIn_patient_no() + ""));
        }
        if (medicalInfo.getPlacer_order_no() != null && !"".equals(medicalInfo.getPlacer_order_no())) {
            medicalInfo.setPlacer_order_no(DesUtil.decrypt(medicalInfo.getPlacer_order_no() + ""));
        }
        if (medicalInfo.getOrganization_id() != null && !"".equals(medicalInfo.getOrganization_id())) {
            medicalInfo.setOrganization_id(hospitalCode);
        }
        return medicalInfo;
    }

    /**
     * 调试 --------获取token
     *
     * @param uri
     * @param obj
     * @return
     */
    public String HttpPostDataForInterface(String uri, String obj) {
        DebugLog debugLog =  new DebugLog();
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(uri);
            //添加http头信息
            httppost.setHeader("Authorization", "Basic " + Base64.getEncoder().encodeToString(("default" + ":" + "1D812578F93BE288122CE828AA1FBEFD").getBytes("UTF-8"))); //认证token
            httppost.setHeader("Content-Type", "text/plain");
            StringEntity myEntity = new StringEntity(obj, ContentType.DEFAULT_TEXT);
            httppost.setEntity(myEntity);
            HttpResponse response;
            response = httpclient.execute(httppost);
            String s = EntityUtils.toString(response.getEntity());
            //检验状态码，如果成功接收数据
            int code = response.getStatusLine().getStatusCode();
            System.out.println(code + "code");
            System.out.println(s + "ssss");
            //记录日志
            Map<String, Object> map = new HashMap<>();
            map.put("header", JSON.toJSON(httppost.getAllHeaders()));
            map.put("Entity", httppost.getEntity());
            debugLog.setReqjson(map.toString());
            debugLog.setRspjson(s);
            debugLog.setState("0");
            if(!ShenkangUtils.isTokenFormat(com.alibaba.fastjson.JSONObject.parseObject(s))){
                debugLog.setState("2");
            }
            debugLog.setApiname("LoginInfo");
            return s;
        }  catch (Exception e) {
            e.printStackTrace();
            debugLog.setState("1");
        } finally {
            debugLog.setCreatetime(ShenkangUtils.currentTimestampTime());
            debugLogDao.insertDebugLog(debugLog);
        }
        return null;
    }
}
