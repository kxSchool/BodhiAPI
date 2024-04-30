package com.example.modules.shenkang.pojo;

public class AIJson {
    private Integer id;
    private String pathNo;
    private String creatorTime;
    private String logstate;

    public String getLogstate() {
        return logstate;
    }

    public void setLogstate(String logstate) {
        this.logstate = logstate;
    }

    public String getRspJson() {
        return rspJson;
    }

    public void setRspJson(String rspJson) {
        this.rspJson = rspJson;
    }

    private String rspJson;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPathNo() {
        return pathNo;
    }

    public void setPathNo(String pathNo) {
        this.pathNo = pathNo;
    }

    public String getCreatorTime() {
        return creatorTime;
    }

    public void setCreatorTime(String creatorTime) {
        this.creatorTime = creatorTime;
    }
}
