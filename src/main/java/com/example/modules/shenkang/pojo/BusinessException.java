package com.example.modules.shenkang.pojo;

import java.util.HashMap;
import java.util.Map;

public class BusinessException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public static Map<Integer,String> map = new HashMap<Integer, String>();
    //传入参数
    public static int PARAM_ERROR_10000 = 10000;
    public static int PARAM_ERROR_10009 = 10009;
    //用户
    public static int PARAM_ERROR_10001 = 10001;
    public static int PARAM_ERROR_10002 = 10002;
    //文件
    public static int PARAM_ERROR_11001 = 11001;
    //数据查询
    public static int PARAM_ERROR_12001 = 12001;
    //数据查询
    public static int PARAM_ERROR_13001 = 13001;

    public static int SYSTEM_ERROR_99999 = 99999;

    static {
        map.put(PARAM_ERROR_10000, "参数格式错误");
        map.put(PARAM_ERROR_10001, "用户名或密码错误");
        map.put(PARAM_ERROR_10002, "您没有此权限");
        map.put(PARAM_ERROR_11001, "文件上传错误");
        map.put(PARAM_ERROR_12001, "查找不到数据");
        map.put(PARAM_ERROR_13001, "数据同步出错");
        map.put(PARAM_ERROR_10009, "输入参数不完整");
        map.put(SYSTEM_ERROR_99999, "系统异常");
    }

    private int code;
    private String msg;

    public BusinessException(String msg) {
        super(msg);
    }

    public BusinessException(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }
    public void setCode(int code) {
        this.code = code;
    }
    public String getMsg() {
        return msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static BusinessException create(int code) {
        return new BusinessException(code,map.get(code));
    }

    public void setRS(Result rs) {
        rs.setCode(this.getCode());
        rs.setMsg(this.getMsg());
    }
}
