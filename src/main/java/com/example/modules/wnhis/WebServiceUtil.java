package com.example.modules.wnhis;


import com.alibaba.fastjson.JSON;
import com.example.utils.analysis.JSONObject;
import com.example.utils.analysis.XML;
import com.example.utils.analysis.XMLToMap;
import com.google.gson.Gson;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
* @author  作者 : 小布
* @version 创建时间 : 2019年7月16日 上午9:49:01
* @explain 类说明 :
*/
@Service
public class WebServiceUtil {

	private final Logger logger= LoggerFactory.getLogger(WebServiceUtil.class);

    @SuppressWarnings({ "unchecked", "unlikely-arg-type" })
	public static Map<String, Object> getWebService(Map<String, Object> param) throws IOException {
    	//获取业务编号
		String number = (String) param.get("number");

		long startTime=System.currentTimeMillis();
		Map<String, Object> mp = new HashMap<>();
		URL url = new URL("http://172.30.12.225:8100/HisInterface.asmx");
//		URL url = new URL("http://172.30.12.225:8099/HisInterface.asmx");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("content-type", "text/xml;charset=utf-8");
        connection.setDoInput(true);
        connection.setDoOutput(true);
        String soapXML = getXML(param);
        //sendXML 展示
		System.out.println(soapXML);
		mp.put("requestParam",soapXML);
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
						Map<String, Object> data2 = (Map<String, Object>)data1.get("SendEmrResponse");
						if(data2 != null) {
							Double SendEmrResult = (Double) data2.get("SendEmrResult");
							if(SendEmrResult.equals(-1) || SendEmrResult.equals(-1.0)) {
								mp.put("code", 1049);
								long endTime=System.currentTimeMillis();
								mp.put("time", (endTime-startTime)+"ms");

//								return mp;
								//return mp;
							}
							String RetXml = (String)data2.get("RetXml");
							if(!RetXml.equals(null) && !RetXml.equals("")) {
								ByteArrayInputStream reader = new ByteArrayInputStream(RetXml.getBytes("UTF-8"));
								data = XMLToMap.mainMethod(reader);
								Map<String, Object> data4 = (Map<String, Object>)data.get("WinResponse");


				            	int size = data4.size();
								if("YY01".equals(param.get("MsgCode")) || "YY02".equals(param.get("MsgCode"))
										|| "YY04".equals(param.get("MsgCode")) || "YY03".equals(param.get("MsgCode"))
										|| "SQ02".equals(param.get("MsgCode")) || "SQ03".equals(param.get("MsgCode"))
										|| "JB02".equals(param.get("MsgCode")) || "JB03".equals(param.get("MsgCode"))) {
					            	List<Object> list = new ArrayList<Object>(1000000);
					            	if(data4 != null) {
					            		for (int i = 0; i < size; i++) {
					            			list.add(data4.get("RowItem_"+i));
										}
					            	}
					            	mp.put("data", list);
					            }else if("AD02".equals(param.get("MsgCode"))){
									List<Object> list = new ArrayList<Object>(1000000);
									if(data4 != null) {
										for (int i = 0; i < size; i++) {
											list.add(data4.get("RowItem_"+i));
											System.out.println(data4.get("RowItem_"+i));
										}
									}
									mp.put("data", JSON.toJSONString(list));
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
        }
        os.close();
        mp.put("code", responseCode);
        long endTime=System.currentTimeMillis();
        mp.put("time", (endTime-startTime)+"ms");
		return mp;
    }


	@SneakyThrows
	@SuppressWarnings({ "unchecked", "unlikely-arg-type" })
    public  Map<String, Object> XMLToMap(String method,String xmlParam) {
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
					Map<String, Object> data2 = (Map<String, Object>)data1.get("SendEmrResponse");
					if(data2 != null) {
						Double SendEmrResult = (Double) data2.get("SendEmrResult");
						if(SendEmrResult.equals(-1) || SendEmrResult.equals(-1.0)) {
							mp.put("code", 1049);
						}
						String RetXml = (String)data2.get("RetXml");
						if(!RetXml.equals(null) && !RetXml.equals("")) {
							ByteArrayInputStream reader = new ByteArrayInputStream(RetXml.getBytes("UTF-8"));
							data = XMLToMap.mainMethod(reader);
							Map<String, Object> data4 = (Map<String, Object>)data.get("WinResponse");
							int size = data4.size();
							if("YY01".equals(method) || "YY02".equals(method)
									|| "YY04".equals(method) || "YY03".equals(method)
									|| "SQ02".equals(method) || "SQ03".equals(method)
									|| "JB02".equals(method) || "JB03".equals(method)) {
								List<Object> list = new ArrayList<Object>(1000000);
								if(data4 != null) {
									for (int i = 0; i < size; i++) {
										list.add(data4.get("RowItem_"+i));
									}
								}
								mp.put("data", list);
							}else if("AD02".equals(method)){
								List<Object> list = new ArrayList<Object>(1000000);
								if(data4 != null) {
									for (int i = 0; i < size; i++) {
										list.add(data4.get("RowItem_"+i));
										System.out.println(data4.get("RowItem_"+i));
									}
								}
								mp.put("data", JSON.toJSONString(list));
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
		return  mp;
	}

	@SuppressWarnings({ "unchecked", "unlikely-arg-type" })
	public Map<String, Object> getWebServiceForDebug(String soapXML) throws IOException {
		long startTime=System.currentTimeMillis();
		Map<String, Object> mp = new HashMap<>();
		URL url = new URL("http://172.30.12.225:8100/HisInterface.asmx");
//		URL url = new URL("http://172.30.12.225:8099/HisInterface.asmx");
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("POST");
		connection.setRequestProperty("content-type", "text/xml;charset=utf-8");
		connection.setDoInput(true);
		connection.setDoOutput(true);
		//sendXML 展示
		System.out.println(soapXML);
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
			//插入数据库 请求 和 响应  xml
			mp.put("responseParam",sb.toString());
			logger.info("返回XML",sb.toString());
		}
		logger.warn("返回Code",responseCode);
		os.close();
		mp.put("code", responseCode);
		long endTime=System.currentTimeMillis();
		mp.put("time", (endTime-startTime)+"ms");
		return mp;
	}
    public static String getXML(Map<String, Object> param){
        String soapXML = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n" +
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
