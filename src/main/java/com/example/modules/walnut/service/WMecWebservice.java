package com.example.modules.walnut.service;
import com.alibaba.fastjson.JSON;
import com.example.modules.walnut.domain.HIS.HISLogger;
import com.example.modules.walnut.mapper.MasterMapper;
import com.example.modules.walnut.model.WalnutHisLog;
import com.example.utils.DateUtils;
import com.example.utils.analysis.JSONObject;
import com.example.utils.analysis.XML;
import com.example.utils.analysis.XMLToMap;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author  作者 : refun
 * @version 创建时间 : 2019年12月12日 上午9:49:01
 * @explain 类说明 : his 体检收费接口对接
 */
@Service
public class WMecWebservice {
    @Autowired
    private MasterMapper masterMapper;

    @Autowired
    private WalnutHisLogService walnutHisLogService;

    /**
     * 调取体检webService
     * @param sendBody
     * @param method
     * @return
     * @throws IOException
     */
    public  Map<String, Object> getMecWebService(String sendBody, String method,String bno,String hisId) throws IOException {
        long startTime=System.currentTimeMillis();
        Map<String, Object> mp = new HashMap<>();
        WalnutHisLog walnut=new WalnutHisLog();

        URL url = new URL("http://172.30.12.225:8200/TJInterface.asmx");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("content-type", "text/xml;charset=utf-8");
        connection.setDoInput(true);
        connection.setDoOutput(true);
        String soapXML = getXML(sendBody);
        String resultXML = "";
        OutputStream os = connection.getOutputStream();
        os.write(soapXML.getBytes());
        int responseCode = connection.getResponseCode();
        if(200 == responseCode){
            InputStream is = connection.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, "UTF-8");
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String temp = null;
            while(null != (temp = br.readLine())){
                sb.append(temp);
            }
            is.close();
            isr.close();
            br.close();
            resultXML=sb.toString();
            JSONObject xmlJSONObj = XML.toJSONObject(sb.toString());
            String jsonPrettyPrintString = xmlJSONObj.toString();
            Gson gson = new Gson();
            Map<String, Object> data = new ConcurrentHashMap<>(10000000);
            data = gson.fromJson(jsonPrettyPrintString, data.getClass());
            if(data != null) {
                Map<String, Object> data0 = (Map<String, Object>)data.get("soap:Envelope");
                if(data0 != null) {
                    Map<String, Object> data1 = (Map<String, Object>)data0.get("soap:Body");
                    if(data1 != null) {
                        Map<String, Object> data2 = (Map<String, Object>)data1.get(method+"Response");
                        if(data2 !=null){
                            String  data3 = String.valueOf(data2.get(method+"Result")) ;
                            if(("TJ_GetPatInfo_NEW").equals(method)){
                                ByteArrayInputStream reader = new ByteArrayInputStream(data3.getBytes("UTF-8"));
                                Map<String, Object> data4 = XMLToMap.mainMethod(reader);
                                Map<String, Object> data5 = (Map<String, Object>)data4.get("PersonInfo");
                                mp.put("PersonInfo", data5);
                            }else if(("TJ_ConfirmItems").equals(method) || ("TJ_ReleaseRisReport").equals(method) || ("TJ_RetrieveReport").equals(method)){
                                mp.put("result", data3);
                            }
                        }
                    }
                }
            }
            walnut.setState("0");
        }else{
            walnut.setState("2");
        }
        os.close();
        walnut.setNo(bno);
        walnut.setMethod(method);
        walnut.setHisId(hisId);
        walnut.setReqParam(soapXML);
        walnut.setRespParam(resultXML);
        walnut.setCreateTime(DateUtils.currentTimestampTime());
        walnutHisLogService.save(walnut);
        return mp;
    }

    public  String getXML(String sendBody){
        String soapXML = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">\n" +
                "  <soap12:Body>\n" +sendBody +
                "  </soap12:Body>\n" +
                "</soap12:Envelope>";
        return soapXML;
    }

    /**
     * 体检项目收费确认
     * @param param
     * @return 成功返回 T，失败返回对应的错误信息
     * @throws IOException
     */
    public  String TJ_ConfirmItems(Map<String, Object> param,String bno,String hisId) throws IOException{
        String sendBody="<TJ_ConfirmItems xmlns=\"http://winning.com.cn/tjgl\">\n" +
                "      <code>"+param.get("code")+"</code>\n" +
                "      <logno>"+param.get("logono")+"</logno>\n" +
                "      <status>"+param.get("status")+"</status>\n" +
                "      <bgdh>"+param.get("bgdh")+"</bgdh>\n" +
                "    </TJ_ConfirmItems>";
        Map<String, Object> result =getMecWebService(sendBody,"TJ_ConfirmItems",bno,hisId);
        return (String) result.get("result");
    }

    /**
     * 同步体检报告
     * @param param
     * @return
     * @throws IOException
     */
    public  String TJ_ReleaseRisReport(Map<String, Object> param,String bno,String hisId) throws IOException{
        String sendBody="<TJ_ReleaseRisReport xmlns=\"http://winning.com.cn/tjgl\">\n" +
                "<strXml><![CDATA[<RisReport>\n" +
                "<repno>"+param.get("repno")+"</repno>\n" +
                "<logno>"+param.get("logno")+"</logno>\n" +
                "<patid>"+param.get("patid")+"</patid>\n" +
                "<replb>bl</replb>\n" +
//                "<replbmc>病理报告</replbmc>\n" +
                "<replbmc>"+param.get("lbmc")+"</replbmc>\n" +
                "<reprq>"+param.get("reprq")+"</reprq>\n" +
                "<jcbw>"+param.get("jcbw")+"</jcbw>\n" +
                "<jcsj>"+param.get("jcsj")+"</jcsj>\n" +
                "<jcjl>"+param.get("jcjl")+"</jcjl>\n" +
                "<sjjl></sjjl>\n" +
                "<imagepath></imagepath>\n" +
                "<jcysdm>"+param.get("jcysdm")+"</jcysdm>\n" +
                "<jcysxm>"+param.get("jcysxm")+"</jcysxm>\n" +
                "<jcksdm>"+param.get("jcksdm")+"</jcksdm>\n" +
                "<jcksmc>"+param.get("jcksmc")+"</jcksmc>\n" +
                "<shysxm>"+param.get("shysxm")+"</shysxm>\n" +
                "<shysdm>"+param.get("shysdm")+"</shysdm>\n" +
                "<wjbz></wjbz>\n" +
                "</RisReport>]]></strXml>\n" +
                "</TJ_ReleaseRisReport>";
        System.out.println(sendBody);
        Map<String, Object> result =getMecWebService(sendBody,"TJ_ReleaseRisReport",bno,hisId);
        return (String) result.get("result");
    }

    /**
     * 体检回传报告
     * @param param
     * @return 成功返回 T，失败返回对应的错误信息
     * @throws IOException
     */
    public  String TJ_RetrieveReport(Map<String, Object> param,String bno,String hisId) throws IOException{
        String sendBody="<TJ_RetrieveReport xmlns=\"http://winning.com.cn/tjgl\">\n" +
                "      <code>"+param.get("code")+"</code>\n" +
                "      <repno>"+param.get("repno")+"</repno>\n" +
                "      <replb>bl</replb>\n" +
                "    </TJ_RetrieveReport>";
        Map<String, Object> result =getMecWebService(sendBody,"TJ_RetrieveReport",bno,hisId);
        return (String) result.get("result");
    }


    public static void main(String[] args) throws Exception{
        Map<String, Object> map =new HashMap();
//        String sendBody="<TJ_GetPatInfo_NEW xmlns=\"http://winning.com.cn/tjgl\">\n" +
//                "      <code>1912100064</code>\n" +
//                "      <logno>1</logno>\n" +
//                "      <status></logno>\n" +
//                "    </TJ_GetPatInfo_NEW>";
//        map.put("sendBody", sendBody);
//        Map<String, Object> result = getMecWebService(sendBody, "TJ_GetPatInfo_NEW");
//        result=(Map<String, Object>) result.get("PersonInfo");
//        String items = JSONArray.toJSONString(result.get("Items_0"));
//        com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(items);
//        Map chargeItems = jsonObject;
//        for (int i=0;i<chargeItems.size();i++) {
//            String str = JSONArray.toJSONString(chargeItems.get("Item_" + i));
//            MECCharge mecCharge = JSON.parseObject(str, MECCharge.class);
//            System.out.println(mecCharge.getLogNo());
//        }

//        String sendBody="<TJ_ConfirmItems xmlns=\"http://winning.com.cn/tjgl\">\n" +
//                "      <code>1912100064</code>\n" +
//                "      <logno>2704</logno>\n" +
//                "      <status>0</status>\n" +
//                "      <bgdh>111</bgdh>\n" +
//                "    </TJ_ConfirmItems>";
//        Map<String, Object> result =getMecWebService(sendBody,"TJ_ConfirmItems");
//        System.out.println(result);
        Map<String, Object> report =new HashMap();
        report.put("repno","110111");
//        report.put("logno","270200004");
//        report.put("patid","1912100064");
        report.put("logno","");
        report.put("patid","11111111111111111111");

        Date date02 = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String string = sdf.format(date02);
        report.put("reprq",string);
        report.put("jcbw","肠镜活检");
        report.put("jcsj","灰白粟米大组织1粒。");
        report.put("jcjl","（距肛50cm）增生性息肉。");
        report.put("jcysdm","");
        report.put("jcysxm","super");
        report.put("jcksdm","7009");
        report.put("jcksmc","病理科");
        report.put("shysxm","super");
        report.put("shysdm","");
        report.put("wjbz","00");

//        String result = TJ_ReleaseRisReport(report);
        System.out.println(string);
//        System.out.println(result);
//        report.put("code","1912100064");
//        report.put("repno","110110");
//        report.put("replb","bl");
//        String result = TJ_RetrieveReport(report);







    }
}
