package com.example.modules.wnhis.controller;


import com.example.modules.wnhis.pojo.WebServiceUtil2;
import com.example.utils.DateUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author:
 * @date:
 * @explain:
 * @description:
 */

@RestController
public class HisLogController {
    @Value("${his.url}")
    private String url;


    @PostMapping("/his/JB01")
    @ResponseBody
    public StringBuilder interfaceHisJB01(@RequestBody Map<String, Object> param){
        String hisId= DateUtils.getStringParam(param,"hisId");
        String brlb=DateUtils.getStringParam(param,"brlb");
        String codeType=DateUtils.getStringParam(param,"codeType");
        WebServiceUtil2 webServiceUtil2=new WebServiceUtil2();
        Map<String, Object> send=getSendJB01("JB01",hisId,brlb,codeType);
        Map<String, Object> mp;
        String state="0";
        StringBuilder sb=null;
        try {
            mp=webServiceUtil2.getWebService(send,url);
            Integer code= (Integer) mp.get("code");
            if(code.equals(200)){
                sb=webServiceUtil2.getSb();
            }else{
                state="2";
            }
            //记录数据库日志,还未写
        } catch (IOException e) {
            e.printStackTrace();
            state="1";
            //记录数据库日志,还未写
        }
        return sb;
    }

    public  Map<String, Object> getSendJB01(String msgCode,String hisId, String brlb, String codeType){
        String sendXML;
        Map<String, Object> send = new HashMap<>();
        send.put("MsgCode", msgCode);
        sendXML = "<codetype>" + codeType + "</codetype><code>" + hisId + "</code><brlb>" + brlb + "</brlb>";
        send.put("SendXml", sendXML);
        return send;
    }

}
