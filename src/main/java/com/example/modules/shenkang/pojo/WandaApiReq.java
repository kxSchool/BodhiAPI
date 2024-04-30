package com.example.modules.shenkang.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class WandaApiReq implements Serializable {
    private Integer id;

    private String requestdetails;

    private String responsedetails;

    private String webapi;

    private String creatime;

    private String pathid;

    /**
     * 1 整条  0 单条
     */
    private Integer state;

    private String reqUpdatedatetime;

    private String requestuser;

    private Integer totalNum;

    private String logstate;
    private String pushJson;

    private static final long serialVersionUID = 1L;

    public WandaApiReq(String requestdetails, String responsedetails, String webapi, String creatime, String pathid, Integer state, String reqUpdatedatetime, String logstate) {
        this.requestdetails = requestdetails;
        this.responsedetails = responsedetails;
        this.webapi = webapi;
        this.creatime = creatime;
        this.pathid = pathid;
        this.state = state;
        this.reqUpdatedatetime = reqUpdatedatetime;
        this.logstate = logstate;
    }
}
