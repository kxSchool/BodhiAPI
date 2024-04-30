package com.example.utils;

import com.alibaba.fastjson.JSON;

import java.util.Map;

/**
 * @author 作者 : 小布
 * @version 创建时间 : 2019年3月25日 下午4:45:37
 * @explain 类说明 :
 */
public final class JsonXMLUtils {

    public static String obj2json(Object obj) throws Exception {
        return JSON.toJSONString(obj);
    }

    public static <T> T json2obj(String jsonStr, Class<T> clazz) throws Exception {
        return JSON.parseObject(jsonStr, clazz);
    }

    public static <T> Map<String, Object> json2map(String jsonStr) throws Exception {
        return JSON.parseObject(jsonStr, Map.class);
    }

    public static <T> T map2obj(Map<?, ?> map, Class<T> clazz) throws Exception {
        return JSON.parseObject(JSON.toJSONString(map), clazz);
    }

}
