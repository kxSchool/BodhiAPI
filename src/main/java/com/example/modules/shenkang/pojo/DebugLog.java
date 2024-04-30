package com.example.modules.shenkang.pojo;

import java.io.Serializable;

public class DebugLog implements Serializable {

    //备用字段,用于存放解密后数据
    private String pushJson;

    private Integer id;

    private String reqjson;

    private String rspjson;

    private String apiname;

    private String state;

    private String pathno;

    private String reqdate;

    private Long createtime;

    private String paramNo;

    private String formatCreatetime;

    public String getPushJson() {
        return pushJson;
    }

    public void setPushJson(String pushJson) {
        this.pushJson = pushJson;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getReqjson() {
        return reqjson;
    }

    public void setReqjson(String reqjson) {
        this.reqjson = reqjson;
    }

    public String getRspjson() {
        return rspjson;
    }

    public void setRspjson(String rspjson) {
        this.rspjson = rspjson;
    }

    public String getApiname() {
        return apiname;
    }

    public void setApiname(String apiname) {
        this.apiname = apiname;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPathno() {
        return pathno;
    }

    public void setPathno(String pathno) {
        this.pathno = pathno;
    }

    public String getReqdate() {
        return reqdate;
    }

    public void setReqdate(String reqdate) {
        this.reqdate = reqdate;
    }

    public Long getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Long createtime) {
        this.createtime = createtime;
    }

    public String getParamNo() {
        return paramNo;
    }

    public void setParamNo(String paramNo) {
        this.paramNo = paramNo;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public DebugLog() {
    }

    public String getFormatCreatetime() {
        return formatCreatetime;
    }

    public void setFormatCreatetime(String formatCreatetime) {
        this.formatCreatetime = formatCreatetime;
    }

    private static final long serialVersionUID = 1L;

    public DebugLog(String reqjson, String rspjson, String apiname, String state, String pathno, String reqdate, Long createtime) {
        this.reqjson = reqjson;
        this.rspjson = rspjson;
        this.apiname = apiname;
        this.state = state;
        this.pathno = pathno;
        this.reqdate = reqdate;
        this.createtime = createtime;
    }

    public DebugLog(String reqjson, String rspjson, String apiname, String state, String pathno, String reqdate, Long createtime, String paramNo) {
        this.reqjson = reqjson;
        this.rspjson = rspjson;
        this.apiname = apiname;
        this.state = state;
        this.pathno = pathno;
        this.reqdate = reqdate;
        this.createtime = createtime;
        this.paramNo = paramNo;
    }
}
