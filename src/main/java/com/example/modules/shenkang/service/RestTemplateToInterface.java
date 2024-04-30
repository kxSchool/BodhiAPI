package com.example.modules.shenkang.service;

import com.alibaba.fastjson.JSONObject;
import com.example.modules.shenkang.mapper.CommonDao;
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
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Toomth
 * @date 2020/5/7 16:01
 * @explain 请求第三方接口
 */
@Service
public class RestTemplateToInterface {
    private static final Logger logger = LoggerFactory.getLogger(RestTemplateToInterface.class);

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    RestTemplateToInterface restTemplateToInterface;//数据同步

    //接口统一请求  主动推送
    public String request(String url, String obj) throws UnsupportedEncodingException {
        //header参数
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization","Basic " + Base64.getEncoder().encodeToString(("default" + ":" + "1D812578F93BE288122CE828AA1FBEFD").getBytes("UTF-8")));
        headers.add("Content-Type", "text/plain");
        //组装
        HttpEntity<String> request = new HttpEntity<>(obj,headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
        String body = responseEntity.getBody();
        System.out.println(body.toString());
        return body;
    }
    public String HttpPostData(String uri,String obj) {
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
            System.out.println(code+"code");
            System.out.println(s+"ssss");
            return s;
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    //接口统一请求  主动推送
    public String request(String url, JSONObject obj, String token) {
        //header参数
        HttpHeaders headers = new HttpHeaders();
//        headers.add("Authorization", Base64.getUrlEncoder().encodeToString(("default" + ":" + "1D812578F93BE288122CE828AA1FBEFD").getBytes()));
        headers.add("Authorization", "bearer " + token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        //组装
        HttpEntity<JSONObject> request = new HttpEntity<>(obj, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
        String body = responseEntity.getBody();
        System.out.println("BODY" + body);
        return body;
    }

    @SuppressWarnings("rawtypes")
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
        } finally {
            if (conn != null) {
                conn.disconnect();
                conn = null;
            }
        }
        return res;
    }
}
