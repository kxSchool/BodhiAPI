package com.example.modules.shenkang.pojo;

/**
 * @author Toomth
 * @date 2020/5/7 11:49
 * @explain 统一返回结构
 */
public class Result {

    /**
     * 200 成功
     */
    public int code = 2000;

    /**
     * 提示信息
     */
    private String msg;

    private Object data;




    public Result() {
    }

    public Result(int code) {
        this.code = code;
        this.msg = BusinessException.map.get(code);
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

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public void setException(Exception e) {
        if(e instanceof BusinessException) {
            BusinessException businessException = (BusinessException) e;
            this.setCode(businessException.getCode());
            this.setMsg(BusinessException.map.get(businessException.getCode()));
        } else {
            this.setCode(BusinessException.SYSTEM_ERROR_99999);
            this.setMsg(BusinessException.map.get(BusinessException.SYSTEM_ERROR_99999));
        }
    }
}
