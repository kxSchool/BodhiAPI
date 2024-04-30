package com.example.modules.walnut.domain;

/**
 * @author 作者 : 小布
 * @version 创建时间 : 2019年4月28日 下午5:40:47
 * @explain 类说明 :
 */
public class PatientInfoApply {

    private Integer patId;
    private String patNo;
    private String hisId;
    private String patName;
    private String patSex;
    private Integer patAge;
    private String patAgeUnit;
    private String patBirthday;
    private String patTel;
    private String maritalStatus;
    private String patIdcard;
    private String cardNo;
    private String nation;
    private String patAddress;
    private String profession;
    private Integer callHis;

    private String pathId;
    private Integer melibId;
    private String melibName;

    private Integer applyId;
    private String applyNo;
    private Integer reId;
    private Integer applyStatus;
    private Integer inspectUnit;
    private String inspectOffice;
    private String inspectDoctor;
    private String inspectTime;
    private String acceptDoctor;
    private Integer patientType;
    private String patientTypeName;
    private String chargeType;
    private String diseaseDistrict;
    private String diseaseDistrictName;
    private String bedNo;
    private String acceptTime;
    private String whetherMenopause;
    private String lastMenopause;
    private String clinicalDiagnosis;
    private String clinicalData;
    private String dutyDoctor;
    private Integer specimenType;
    private String specimenName;
    private Integer specimenSum;
    private String opsDoctor;
    private String opsTime;
    private String specimenCase;
    private Integer disqualification;
    private String clinicalAdvice;
    private Integer urgent;
    private Integer postpone;
    private String delayCause;
    private String printTime;

    private Integer reType;
    private String template;

    private String imageUrl;
    private String grossFinding;
    private Integer materialsDoctor;
    private String syxh;
    private Integer consultation;

    public Integer getConsultation() {
        return consultation;
    }

    public void setConsultation(Integer consultation) {
        this.consultation = consultation;
    }

    public String getQfyy() {
        return Qfyy;
    }

    public void setQfyy(String qfyy) {
        Qfyy = qfyy;
    }

    private String Qfyy;  //体检团队、散客


    public String getPrintTime() {
        return printTime;
    }

    public void setPrintTime(String printTime) {
        this.printTime = printTime;
    }

    public String getSyxh() {
		return syxh;
	}

	public void setSyxh(String syxh) {
		this.syxh = syxh;
	}

    public String getPatAgeUnit() {
        return patAgeUnit;
    }

    public void setPatAgeUnit(String patAgeUnit) {
        this.patAgeUnit = patAgeUnit;
    }

    public String getPatientTypeName() {
        return patientTypeName;
    }

    public void setPatientTypeName(String patientTypeName) {
        this.patientTypeName = patientTypeName;
    }

    public String getDiseaseDistrictName() {
        return diseaseDistrictName;
    }

    public void setDiseaseDistrictName(String diseaseDistrictName) {
        this.diseaseDistrictName = diseaseDistrictName;
    }

    public Integer getMaterialsDoctor() {
        return materialsDoctor;
    }

    public void setMaterialsDoctor(Integer materialsDoctor) {
        this.materialsDoctor = materialsDoctor;
    }

    public Integer getCallHis() {
        return callHis;
    }

    public void setCallHis(Integer callHis) {
        this.callHis = callHis;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getGrossFinding() {
        return grossFinding;
    }

    public void setGrossFinding(String grossFinding) {
        this.grossFinding = grossFinding;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public Integer getPatId() {
        return patId;
    }

    public void setPatId(Integer patId) {
        this.patId = patId;
    }

    public String getPatNo() {
        return patNo;
    }

    public void setPatNo(String patNo) {
        this.patNo = patNo;
    }

    public String getHisId() {
        return hisId;
    }

    public void setHisId(String hisId) {
        this.hisId = hisId;
    }

    public String getPatName() {
        return patName;
    }

    public void setPatName(String patName) {
        this.patName = patName;
    }

    public String getPatSex() {
        return patSex;
    }

    public void setPatSex(String patSex) {
        this.patSex = patSex;
    }

    public Integer getPatAge() {
        return patAge;
    }

    public void setPatAge(Integer patAge) {
        this.patAge = patAge;
    }

    public String getPatBirthday() {
        return patBirthday;
    }

    public void setPatBirthday(String patBirthday) {
        this.patBirthday = patBirthday;
    }

    public String getPatTel() {
        return patTel;
    }

    public void setPatTel(String patTel) {
        this.patTel = patTel;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getPatIdcard() {
        return patIdcard;
    }

    public void setPatIdcard(String patIdcard) {
        this.patIdcard = patIdcard;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getPatAddress() {
        return patAddress;
    }

    public void setPatAddress(String patAddress) {
        this.patAddress = patAddress;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getPathId() {
        return pathId;
    }

    public void setPathId(String pathId) {
        this.pathId = pathId;
    }

    public Integer getMelibId() {
        return melibId;
    }

    public void setMelibId(Integer melibId) {
        this.melibId = melibId;
    }

    public Integer getApplyId() {
        return applyId;
    }

    public void setApplyId(Integer applyId) {
        this.applyId = applyId;
    }

    public String getApplyNo() {
        return applyNo;
    }

    public void setApplyNo(String applyNo) {
        this.applyNo = applyNo;
    }

    public Integer getReId() {
        return reId;
    }

    public void setReId(Integer reId) {
        this.reId = reId;
    }

    public Integer getApplyStatus() {
        return applyStatus;
    }

    public void setApplyStatus(Integer applyStatus) {
        this.applyStatus = applyStatus;
    }

    public Integer getInspectUnit() {
        return inspectUnit;
    }

    public void setInspectUnit(Integer inspectUnit) {
        this.inspectUnit = inspectUnit;
    }

    public String getInspectOffice() {
        return inspectOffice;
    }

    public void setInspectOffice(String inspectOffice) {
        this.inspectOffice = inspectOffice;
    }

    public String getInspectDoctor() {
        return inspectDoctor;
    }

    public void setInspectDoctor(String inspectDoctor) {
        this.inspectDoctor = inspectDoctor;
    }

    public String getInspectTime() {
        return inspectTime;
    }

    public void setInspectTime(String inspectTime) {
        this.inspectTime = inspectTime;
    }

    public String getAcceptDoctor() {
        return acceptDoctor;
    }

    public void setAcceptDoctor(String acceptDoctor) {
        this.acceptDoctor = acceptDoctor;
    }

    public Integer getPatientType() {
        return patientType;
    }

    public void setPatientType(Integer patientType) {
        this.patientType = patientType;
    }

    public String getChargeType() {
        return chargeType;
    }

    public void setChargeType(String chargeType) {
        this.chargeType = chargeType;
    }

    public String getDiseaseDistrict() {
        return diseaseDistrict;
    }

    public void setDiseaseDistrict(String diseaseDistrict) {
        this.diseaseDistrict = diseaseDistrict;
    }

    public String getBedNo() {
        return bedNo;
    }

    public void setBedNo(String bedNo) {
        this.bedNo = bedNo;
    }

    public String getAcceptTime() {
        return acceptTime;
    }

    public void setAcceptTime(String acceptTime) {
        this.acceptTime = acceptTime;
    }

    public String getWhetherMenopause() {
        return whetherMenopause;
    }

    public void setWhetherMenopause(String whetherMenopause) {
        this.whetherMenopause = whetherMenopause;
    }

    public String getLastMenopause() {
        return lastMenopause;
    }

    public void setLastMenopause(String lastMenopause) {
        this.lastMenopause = lastMenopause;
    }

    public String getClinicalDiagnosis() {
        return clinicalDiagnosis;
    }

    public void setClinicalDiagnosis(String clinicalDiagnosis) {
        this.clinicalDiagnosis = clinicalDiagnosis;
    }

    public String getClinicalData() {
        return clinicalData;
    }

    public void setClinicalData(String clinicalData) {
        this.clinicalData = clinicalData;
    }

    public String getDutyDoctor() {
        return dutyDoctor;
    }

    public void setDutyDoctor(String dutyDoctor) {
        this.dutyDoctor = dutyDoctor;
    }

    public Integer getSpecimenType() {
        return specimenType;
    }

    public void setSpecimenType(Integer specimenType) {
        this.specimenType = specimenType;
    }

    public String getSpecimenName() {
        return specimenName;
    }

    public void setSpecimenName(String specimenName) {
        this.specimenName = specimenName;
    }

    public Integer getSpecimenSum() {
        return specimenSum;
    }

    public void setSpecimenSum(Integer specimenSum) {
        this.specimenSum = specimenSum;
    }

    public String getOpsDoctor() {
        return opsDoctor;
    }

    public void setOpsDoctor(String opsDoctor) {
        this.opsDoctor = opsDoctor;
    }

    public String getOpsTime() {
        return opsTime;
    }

    public void setOpsTime(String opsTime) {
        this.opsTime = opsTime;
    }

    public String getSpecimenCase() {
        return specimenCase;
    }

    public void setSpecimenCase(String specimenCase) {
        this.specimenCase = specimenCase;
    }

    public Integer getDisqualification() {
        return disqualification;
    }

    public void setDisqualification(Integer disqualification) {
        this.disqualification = disqualification;
    }

    public String getClinicalAdvice() {
        return clinicalAdvice;
    }

    public void setClinicalAdvice(String clinicalAdvice) {
        this.clinicalAdvice = clinicalAdvice;
    }

    public Integer getUrgent() {
        return urgent;
    }

    public void setUrgent(Integer urgent) {
        this.urgent = urgent;
    }

    public String getDelayCause() {
        return delayCause;
    }

    public void setDelayCause(String delayCause) {
        this.delayCause = delayCause;
    }

    public Integer getReType() {
        return reType;
    }

    public void setReType(Integer reType) {
        this.reType = reType;
    }

    public String getMelibName() {
        return melibName;
    }

    public void setMelibName(String melibName) {
        this.melibName = melibName;
    }

    public Integer getPostpone() {
        return postpone;
    }

    public void setPostpone(Integer postpone) {
        this.postpone = postpone;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }
}
