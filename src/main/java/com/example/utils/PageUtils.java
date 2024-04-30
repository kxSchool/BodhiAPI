package com.example.utils;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author:
 * @date:
 * @explain:
 * @description:
 */
public class PageUtils {
    public static int getPageParam(Object no,int size){
        int num;
        if(no==null){
            num=size;
        }else{
            num=new Integer(no.toString());
            if(num<=0){
                num=size;
            }
        }
        return num;
    }
    public static boolean isSuccessPatientLog(String respJson){
        JSONObject jsonParam= null;
        String msg=null;
        JSONObject response=null;
        String code=null;
        JSONArray details=null;
        JSONArray patientList=null;
        try {
            jsonParam = new JSONObject(respJson);
            response=jsonParam.getJSONObject("Response");
            System.out.println(response.toString());
            msg=response.getString("Msg");
            code=response.getString("Code");
            if(!code.equals("0"))
            {
                return false;
            }
        }catch (Exception e){
            return false;
        }
        return true;
    }
}
