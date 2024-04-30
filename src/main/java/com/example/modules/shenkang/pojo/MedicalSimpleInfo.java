package com.example.modules.shenkang.pojo;

public class MedicalSimpleInfo {
    //返回页面时候的编号
    private Long idTemp;
    // medical_info表格
    private String id;
    //patient_id:患者编号,各个病理系统中的流水号
    private String patient_id;
    //patient_master_id:患者主索引,
    private String patient_master_id;

    private String pullData;

    private String pushData;

    private String dataDate;

    private String pushDate;


    public Long getIdTemp() {
        return idTemp;
    }

    public void setIdTemp(Long idTemp) {
        this.idTemp = idTemp;
    }

    public String getPathNo() {
        return pathNo;
    }

    public void setPathNo(String pathNo) {
        this.pathNo = pathNo;
    }

    private String pathNo;
    public String getState() {
        return state;
    }
    public void setState(String state) {
        this.state = state;
    }
    private String state;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getPatient_id() {
        return patient_id;
    }
    public void setPatient_id(String patient_id) {
        this.patient_id = patient_id;
    }
    public String getPatient_master_id() {
        return patient_master_id;
    }
    public void setPatient_master_id(String patient_master_id) {
        this.patient_master_id = patient_master_id;
    }

    public String getPullData() {
        return pullData;
    }

    public void setPullData(String pullData) {
        this.pullData = pullData;
    }

    public String getPushData() {
        return pushData;
    }

    public void setPushData(String pushData) {
        this.pushData = pushData;
    }

    public String getDataDate() {
        return dataDate;
    }

    public void setDataDate(String dataDate) {
        this.dataDate = dataDate;
    }

    public String getPushDate() {
        return pushDate;
    }

    public void setPushDate(String pushDate) {
        this.pushDate = pushDate;
    }
}
