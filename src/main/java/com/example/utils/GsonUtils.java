package com.example.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
* @author  作者 : 小布
* @version 创建时间 : 2019年7月22日 下午3:18:42
* @explain 类说明 :
*/
public class GsonUtils {

	 private GsonUtils() {

	 }

    public static <T>T fromJson(String json,Class<T> type){
        Gson gson = new Gson();
        return gson.fromJson(json,type);

    }

    public static <T> List<T> listFromJson(String json){
        Gson gson = new Gson();
        return gson.fromJson(json, new TypeToken<List<T>>(){}.getType());
    }

}

