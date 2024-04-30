package com.example.modules.shenkang.pojo;

public class PatientJson {
    private Integer id;
    private String pathId;

    private String req_updateDateTime;
    private String creatorTime;
    private String rspJson;

    private Integer state;

    private Integer logstate;

    public Integer getLogstate() {
        return logstate;
    }

    public void setLogstate(Integer logstate) {
        this.logstate = logstate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getReq_updateDateTime() {
        return req_updateDateTime;
    }

    public void setReq_updateDateTime(String req_updateDateTime) {
        this.req_updateDateTime = req_updateDateTime;
    }

    public String getCreatorTime() {
        return creatorTime;
    }

    public void setCreatorTime(String creatorTime) {
        this.creatorTime = creatorTime;
    }

    public String getRspJson() {
        return rspJson;
    }

    public void setRspJson(String rspJson) {
        this.rspJson = rspJson;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getPathId() {
        return pathId;
    }

    public void setPathId(String pathId) {
        this.pathId = pathId;
    }

    public PatientJson(String pathId, String req_updateDateTime, String creatorTime, String rspJson, Integer state) {
        this.pathId = pathId;
        this.req_updateDateTime = req_updateDateTime;
        this.creatorTime = creatorTime;
        this.rspJson = rspJson;
        this.state = state;
    }
    public PatientJson(String pathId, String req_updateDateTime, String creatorTime, String rspJson, Integer state,Integer logstate) {
        this.pathId = pathId;
        this.req_updateDateTime = req_updateDateTime;
        this.creatorTime = creatorTime;
        this.rspJson = rspJson;
        this.state = state;
        this.logstate=logstate;
    }

    public PatientJson(String req_updateDateTime, String creatorTime, String rspJson, Integer state) {
        this.req_updateDateTime = req_updateDateTime;
        this.creatorTime = creatorTime;
        this.rspJson = rspJson;
        this.state = state;
    }

    public PatientJson(String req_updateDateTime, String creatorTime, String rspJson) {
        this.req_updateDateTime = req_updateDateTime;
        this.creatorTime = creatorTime;
        this.rspJson = rspJson;
    }


    public PatientJson(String req_updateDateTime, String creatorTime, String rspJson, Integer state, Integer logstate) {
        this.req_updateDateTime = req_updateDateTime;
        this.creatorTime = creatorTime;
        this.rspJson = rspJson;
        this.state = state;
        this.logstate = logstate;
    }

    public PatientJson() {
    }
}
