package com.example.modules.wnhis.pojo;

import com.alibaba.fastjson.JSON;

import com.example.utils.analysis.JSONObject;
import com.example.utils.analysis.XML;
import com.example.utils.analysis.XMLToMap;
import com.google.gson.Gson;
import lombok.Data;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author:
 * @date:
 * @explain:
 * @description:
 */
@Data
public class WebServiceUtil2 {

    private HttpURLConnection connection;

    private OutputStream os;

    private String soapXML;

    private long startTime;

    private StringBuilder sb;

    public void closeUrl() throws IOException {
        os.close();
        connection.disconnect();
    }

    public void setUrl(String urlString) throws IOException {
        startTime=System.currentTimeMillis();
        URL url = new URL(urlString);
        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("content-type", "text/xml;charset=utf-8");
        connection.setDoInput(true);
        connection.setDoOutput(true);
    }

    public int getResponseCode() throws IOException {
        os = connection.getOutputStream();
        os.write(soapXML.getBytes());
        return connection.getResponseCode();
    }

    public Map<String, Object> getWebService(Map<String, Object> param,String urlString) throws IOException {
        this.setXML(param);
        this.setUrl(urlString);
        int responseCode=this.getResponseCode();
        Map<String, Object> mp = new HashMap<>();
        if(200 == responseCode){
            sb=this.getInputStreamBuilder();
            Map<String, Object> data = this.sbToMap(sb);
            mp = this.getDataMap(data,param);
        }
        this.setMapCodeAndTime(mp,responseCode);
        this.closeUrl();
        return mp;
    }

    public void setMapCodeAndTime( Map<String, Object> mp,int responseCode){
        mp.put("code", responseCode);
        long endTime=System.currentTimeMillis();
        mp.put("time", (endTime-startTime)+"ms");
    }

    public Map<String, Object> getDataMap(Map<String, Object> data,Map<String, Object> param) throws UnsupportedEncodingException {
        Map<String, Object> mp = new HashMap<>();
        if(data != null) {
            Map<String, Object> data0 = (Map<String, Object>)data.get("soap:Envelope");
            if(data0 != null) {
                Map<String, Object> data1 = (Map<String, Object>)data0.get("soap:Body");
                if(data1 != null) {
                    Map<String, Object> data2 = (Map<String, Object>)data1.get("SendEmrResponse");
                    if(data2 != null) {
                        Double SendEmrResult = (Double) data2.get("SendEmrResult");
                        if(SendEmrResult.equals(-1) || SendEmrResult.equals(-1.0)) {
                            this.setMapCodeAndTime(mp,1049);
                        }
                        String RetXml = (String)data2.get("RetXml");
                        if(!RetXml.equals(null) && !RetXml.equals("")) {
                            ByteArrayInputStream reader = new ByteArrayInputStream(RetXml.getBytes("UTF-8"));
                            data = XMLToMap.mainMethod(reader);
                            Map<String, Object> data4 = (Map<String, Object>)data.get("WinResponse");
                            if("YY01".equals(param.get("MsgCode")) || "YY02".equals(param.get("MsgCode"))
                                    || "YY04".equals(param.get("MsgCode")) || "YY03".equals(param.get("MsgCode"))
                                    || "SQ02".equals(param.get("MsgCode")) || "SQ03".equals(param.get("MsgCode"))
                                    || "JB02".equals(param.get("MsgCode")) || "JB03".equals(param.get("MsgCode"))) {
                                mp.put("data", this.getData4List(data4));
                            }else if("AD02".equals(param.get("MsgCode"))){
                                mp.put("data", JSON.toJSONString(this.getData4List(data4)));
                            } else  {
                                String str = null;
                                if(data4 != null) {
                                    str = JSON.toJSONString(data4.get("RowItem_0"));
                                }
                                mp.put("data", str);
                            }
                        }
                    }
                }
            }
        }
        return mp;
    }

    public static List<Object> getData4List(Map<String, Object> data4){
        int size = data4.size();
        List<Object> list = new ArrayList<Object>(1000000);
        if(data4 != null) {
            for (int i = 0; i < size; i++) {
                list.add(data4.get("RowItem_"+i));
            }
        }
        return list;
    }



    public static Map<String,Object> sbToMap(StringBuilder sb){
        JSONObject xmlJSONObj = XML.toJSONObject(sb.toString());
        String jsonPrettyPrintString = xmlJSONObj.toString();
        Gson gson = new Gson();
        Map<String, Object> data = new ConcurrentHashMap<>(10000000);
        data = gson.fromJson(jsonPrettyPrintString, data.getClass());
        return data;
    }

    public StringBuilder getInputStreamBuilder() throws IOException {
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
        return sb;
    }

    public String setXML(Map<String, Object> param){
         soapXML = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n" +
                "<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">\r\n" +
                "  <soap12:Body>\r\n" +
                "    <SendEmr xmlns=\"http://www.winning.com.cn/\">\r\n" +
                "      <MsgCode>"+param.get("MsgCode")+"</MsgCode>\r\n" +
                "      <SendXml>\r\n" +
                "       <![CDATA[<NewDataSet><Table1>"+param.get("SendXml")+"</Table1></NewDataSet>]]>\r\n" +
                "      </SendXml>\r\n" +
                "      <UserCode>kingstar</UserCode>\r\n" +
                "    </SendEmr>\r\n" +
                "  </soap12:Body>\r\n" +
                "</soap12:Envelope>";
        return soapXML;
    }

}
