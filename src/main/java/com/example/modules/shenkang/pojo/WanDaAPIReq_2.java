package com.example.modules.shenkang.pojo;

public class WanDaAPIReq_2 {
    private String id;

    private String pathId;
    private String requestDetails;
    private String responseDetails;
    private String webAPI;
    private String creatime;
    private String req_updateDateTime;
    private Integer state;

    private String logstate;

    public String getLogstate() {
        return logstate;
    }

    public void setLogstate(String logstate) {
        this.logstate = logstate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRequestDetails() {
        return requestDetails;
    }

    public void setRequestDetails(String requestDetails) {
        this.requestDetails = requestDetails;
    }

    public String getResponseDetails() {
        return responseDetails;
    }

    public void setResponseDetails(String responseDetails) {
        this.responseDetails = responseDetails;
    }

    public String getWebAPI() {
        return webAPI;
    }

    public void setWebAPI(String webAPI) {
        this.webAPI = webAPI;
    }

    public String getCreatime() {
        return creatime;
    }

    public void setCreatime(String creatime) {
        this.creatime = creatime;
    }

    public String getPathId() {
        return pathId;
    }

    public void setPathId(String pathId) {
        this.pathId = pathId;
    }

    public String getReq_updateDateTime() {
        return req_updateDateTime;
    }

    public void setReq_updateDateTime(String req_updateDateTime) {
        this.req_updateDateTime = req_updateDateTime;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
}
