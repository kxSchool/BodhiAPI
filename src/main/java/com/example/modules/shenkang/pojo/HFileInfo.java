package com.example.modules.shenkang.pojo;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class HFileInfo {

    private Integer fileId;
    private Integer fileFid;

    private Integer medicalId;
    private String fileName;
    private String filePath;
    private String specimenId;
    private String fileType;
    private String FileUID;
    private String pathNo;

    private String pushState;

    @JSONField(name="ObservationUID")
    private String ObservationUID;

    private long filesize;

    public long getFilesize() {
        return filesize;
    }

    public void setFilesize(long filesize) {
        this.filesize = filesize;
    }

    public HFileInfo(Integer fileId, Integer fileFid, Integer medicalId, String fileName, String filePath, String specimenId, String fileType, String fileUID, String pathNo, String observationUID) {
        this.fileId = fileId;
        this.fileFid = fileFid;
        this.medicalId = medicalId;
        this.fileName = fileName;
        this.filePath = filePath;
        this.specimenId = specimenId;
        this.fileType = fileType;
        FileUID = fileUID;
        this.pathNo = pathNo;
        ObservationUID = observationUID;
    }
    @JsonProperty("ObservationUID")
    public String getObservationUID() {
        return ObservationUID;
    }

    public void setObservationUID(String observationUID) {
        ObservationUID = observationUID;
    }



    public String getPathNo() {
        return pathNo;
    }

    public void setPathNo(String pathNo) {
        this.pathNo = pathNo;
    }

    public Integer getFileId() {
        return fileId;
    }

    public void setFileId(Integer fileId) {
        this.fileId = fileId;
    }

    public Integer getMedicalId() {
        return medicalId;
    }

    public void setMedicalId(Integer medicalId) {
        this.medicalId = medicalId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getSpecimenId() {
        return specimenId;
    }

    public void setSpecimenId(String specimenId) {
        this.specimenId = specimenId;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public Integer getFileFid() {
        return fileFid;
    }

    public void setFileFid(Integer fileFid) {
        this.fileFid = fileFid;
    }

    public String getFileUID() {
        return FileUID;
    }

    public void setFileUID(String fileUID) {
        FileUID = fileUID;
    }

    public String getPushState() {
        return pushState;
    }

    public void setPushState(String pushState) {
        this.pushState = pushState;
    }
}
