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
public class WWebServiceUtil {

	@Autowired
	private MasterMapper masterMapper;
	@Autowired
	private WalnutHisLogService walnutHisLogService;


    @SuppressWarnings({ "unchecked", "unlikely-arg-type" })
	public Map<String, Object> getWebService(Map<String, Object> param,String bno,String hisId) throws IOException {
		long startTime=System.currentTimeMillis();
		Map<String, Object> mp = new HashMap<>();
		WalnutHisLog walnut=new WalnutHisLog();
		String soapXML1 = getXML(param);
		String method=(String) param.get("MsgCode");
		String resultXML="";
		System.out.println(soapXML1);
		URL url = new URL("http://172.30.12.225:8100/HisInterface.asmx");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("content-type", "text/xml;charset=utf-8");
        connection.setDoInput(true);
        connection.setDoOutput(true);
        String soapXML = getXML(param);
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
								}
//								else if ("BG03".equals(param.get("MsgCode"))){
//									String str = null;
//									if(data4 != null) {
//										str = JSON.toJSONString(data4.get("RowItem_0"));
//									}
//									mp.put("data", str);
//								}
								else  {
					            	String str = null;
					            	if(data4 != null) {
					            		str = JSON.toJSONString(data4.get("RowItem_0"));
					            	}
					            	mp.put("data", str);
					            }
								/*JSONObject xmlJSONObj2 = XML.toJSONObject(RetXml);
					            String jsonPrettyPrintString2 = xmlJSONObj2.toString();
					            Gson gson2 = new Gson();
					            data = gson2.fromJson(jsonPrettyPrintString2, data.getClass());
					            Map<String, Object> data4 = (Map<String, Object>)data.get("WinResponse");
					            if("YY01".equals(param.get("MsgCode")) || "YY02".equals(param.get("MsgCode")) || "YY03".equals(param.get("MsgCode")) || "SQ02".equals(param.get("MsgCode")) || "SQ03".equals(param.get("MsgCode"))) {
					            	List<Object> list = new ArrayList<Object>(1000000);
					            	if(data4.get("RowItem") != null) {
					            		System.err.println(data4.get("RowItem"));
					            		list = (List<Object>)data4.get("RowItem");
					            	}
					            	mp.put("data", list);
					            }else {
					            	String str = null;
					            	if(data4.get("RowItem") != null) {
					            		str = JSON.toJSONString(data4.get("RowItem"));
					            	}
					            	mp.put("data", str);
					            }*/
							}
						}
					}
	            }
			}
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

    public String getXML(Map<String, Object> param){
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


	public static void main(String[] args) throws Exception {

		long startTime=System.currentTimeMillis();
		Map<String, Object> mp = new HashMap<>();
		HISLogger hisLogger=new HISLogger();
		URL url = new URL("http://172.30.12.225:8100/HisInterface.asmx");
//		URL url = new URL("http://172.30.12.225:8099/HisInterface.asmx");
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("POST");
		connection.setRequestProperty("content-type", "text/xml;charset=utf-8");
		connection.setDoInput(true);
		connection.setDoOutput(true);

		StringBuilder soapXML=new StringBuilder();
		soapXML.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
				"<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">\n" +
				"  <soap12:Body>\n" +
				"    <SendEmr xmlns=\"http://www.winning.com.cn/\">\n" +
				"      <MsgCode>BG03</MsgCode>\n" +
				"      <SendXml>\n" +
				"       <![CDATA[<NewDataSet><Table1><repno>361032</repno><replb>bl</replb><xmdm>0006</xmdm><xmmc>病理诊断</xmmc><jgckz>0</jgckz><qqmxxh>0</qqmxxh><xmjg>（胃壁），1cm，（低度风险性），。\n（胃）平坦型腺癌，Ⅱ级，侵及粘膜下层，上、下切缘及网膜组织未见肿瘤累及，周周周围胃粘膜慢性炎伴灶状肠化生，胃小弯淋巴结（0/13）、胃大弯淋巴结（0/12）均未见肿瘤转移。</xmjg><xmdw></xmdw><xjbz>0</xjbz><xjmc></xjmc><kssmc></kssmc><gmjg></gmjg><jgxh>1</jgxh><gdbz></gdbz><crbz></crbz></Table1></NewDataSet>]]>\n" +
				"      </SendXml>\n" +
				"      <UserCode>kingstar</UserCode>\n" +
				"    </SendEmr>\n" +
				"  </soap12:Body>\n" +
				"</soap12:Envelope>");
		OutputStream os = connection.getOutputStream();
		os.write(soapXML.toString().getBytes());
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
								String str = null;
								if(data4 != null) {
									str = JSON.toJSONString(data4.get("RowItem_0"));
								}
								mp.put("data", str);

								/*JSONObject xmlJSONObj2 = XML.toJSONObject(RetXml);
					            String jsonPrettyPrintString2 = xmlJSONObj2.toString();
					            Gson gson2 = new Gson();
					            data = gson2.fromJson(jsonPrettyPrintString2, data.getClass());
					            Map<String, Object> data4 = (Map<String, Object>)data.get("WinResponse");
					            if("YY01".equals(param.get("MsgCode")) || "YY02".equals(param.get("MsgCode")) || "YY03".equals(param.get("MsgCode")) || "SQ02".equals(param.get("MsgCode")) || "SQ03".equals(param.get("MsgCode"))) {
					            	List<Object> list = new ArrayList<Object>(1000000);
					            	if(data4.get("RowItem") != null) {
					            		System.err.println(data4.get("RowItem"));
					            		list = (List<Object>)data4.get("RowItem");
					            	}
					            	mp.put("data", list);
					            }else {
					            	String str = null;
					            	if(data4.get("RowItem") != null) {
					            		str = JSON.toJSONString(data4.get("RowItem"));
					            	}
					            	mp.put("data", str);
					            }*/
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
		//添加logger
		hisLogger.setCode(responseCode+"");
		hisLogger.setCallBack(JSON.toJSONString(mp));
//		hisLogger.setMethod((String) param.get("MsgCode"));
//		hisLogger.setParam(JSON.toJSONString(param));
//        TODO:
//		masterMapper.insertInfoToHisLogger(hisLogger);
//		return mp;
	}

}
