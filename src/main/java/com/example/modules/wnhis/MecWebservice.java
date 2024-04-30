package com.example.modules.wnhis;



import com.example.utils.analysis.JSONObject;
import com.example.utils.analysis.XML;
import com.example.utils.analysis.XMLToMap;
import com.google.gson.Gson;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @author  作者 : refun
 * @version 创建时间 : 2019年12月12日 上午9:49:01
 * @explain 类说明 : his 体检收费接口对接
 */

@Service
public class MecWebservice {
    /**
     * 调取体检webService
     * @param sendBody
     * @param method
     * @return
     * @throws IOException     @HisLog("TJ01")
     */
    public  Map<String, Object> getMecWebService(String sendBody, String method) throws IOException {
        long startTime=System.currentTimeMillis();
        Map<String, Object> mp = new HashMap<>();
        URL url = new URL("http://172.30.12.225:8200/TJInterface.asmx");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("content-type", "text/xml;charset=utf-8");
        connection.setDoInput(true);
        connection.setDoOutput(true);
        String soapXML = getXML(sendBody);
        OutputStream os = connection.getOutputStream();
        os.write(soapXML.getBytes());
        int responseCode = connection.getResponseCode();
        if(200 == responseCode){
            InputStream is = connection.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String temp = null;
            while(null != (temp = br.readLine())){
                sb.append(temp);
            }
            is.close();
            isr.close();
            br.close();
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
        }
        os.close();

        return mp;
    }


    public  Map<String, Object> getMecWebServiceForDebug(String sendBody) throws IOException {
        long startTime=System.currentTimeMillis();
        Map<String, Object> mp = new HashMap<>();
        URL url = new URL("http://172.30.12.225:8200/TJInterface.asmx");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("content-type", "text/xml;charset=utf-8");
        connection.setDoInput(true);
        connection.setDoOutput(true);
        String soapXML = getXML(sendBody);
        OutputStream os = connection.getOutputStream();
        os.write(soapXML.getBytes());
        int responseCode = connection.getResponseCode();
        if(200 == responseCode){
            InputStream is = connection.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String temp = null;
            while(null != (temp = br.readLine())){
                sb.append(temp);
            }
            is.close();
            isr.close();
            br.close();
            //插入数据库 请求 和 响应  xml
            mp.put("responseParam",sb.toString());
        }
        os.close();

        return mp;
    }


    @SneakyThrows
    public  Map<String, Object> XMLToMap(String method, String xmlParam) {
        Map<String, Object> mp = new HashMap<>();
        JSONObject xmlJSONObj = XML.toJSONObject(xmlParam);
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
                        if(("TJ_01").equals(method)){
                            ByteArrayInputStream reader = new ByteArrayInputStream(data3.getBytes("UTF-8"));
                            Map<String, Object> data4 = XMLToMap.mainMethod(reader);
                            Map<String, Object> data5 = (Map<String, Object>)data4.get("PersonInfo");
                            mp.put("PersonInfo", data5);
                        }else if(("TJ_03").equals(method) || ("TJ_04").equals(method) || ("TJ_05").equals(method)){
                            mp.put("result", data3);
                        }
                    }
                }
            }
        }
        return mp;
    }



    /**
     *
     * @param sendBody
     * @return     @HisLog("TJ02")
     */
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
     * @throws IOException     @HisLog("TJ03")
     */
    public  String TJ_ConfirmItems(Map<String, Object> param) throws IOException{
        String sendBody="<TJ_ConfirmItems xmlns=\"http://winning.com.cn/tjgl\">\n" +
                "      <code>"+param.get("code")+"</code>\n" +
                "      <logno>"+param.get("logono")+"</logno>\n" +
                "      <status>"+param.get("status")+"</status>\n" +
                "      <bgdh>"+param.get("bgdh")+"</bgdh>\n" +
                "    </TJ_ConfirmItems>";
        Map<String, Object> result =getMecWebService(sendBody,"TJ_ConfirmItems");
        return (String) result.get("result");
    }

    /**
     * 同步体检报告
     * @param param
     * @return
     * @throws IOException     @HisLog("TJ03")
     */
    public  String TJ_ReleaseRisReport(Map<String, Object> param) throws IOException{
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
        Map<String, Object> result =getMecWebService(sendBody,"TJ_ReleaseRisReport");
        return (String) result.get("result");
    }

    /**
     * 体检回传报告
     * @param param
     * @return 成功返回 T，失败返回对应的错误信息
     * @throws IOException     @HisLog("TJ05")
     */
    public  String TJ_RetrieveReport(Map<String, Object> param) throws IOException{
        String sendBody="<TJ_RetrieveReport xmlns=\"http://winning.com.cn/tjgl\">\n" +
                "      <code>"+param.get("code")+"</code>\n" +
                "      <repno>"+param.get("repno")+"</repno>\n" +
                "      <replb>bl</replb>\n" +
                "    </TJ_RetrieveReport>";
        Map<String, Object> result =getMecWebService(sendBody,"TJ_RetrieveReport");
        return (String) result.get("result");
    }

}
