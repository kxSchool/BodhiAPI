package com.example.modules.shenkang.pojo;

import lombok.Data;

import java.io.Serializable;

@Data
public class WalnutCommon implements Serializable {

    private Integer medId;

    private Integer patId;

    private String pathNo;

    private String searchDate;

    private String medicalInfo;

    private String patientInfo;

    private String createTime;

    private String state;
    private String logstate;



}
