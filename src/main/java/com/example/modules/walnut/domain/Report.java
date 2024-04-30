package com.example.modules.walnut.domain;
/**
* @author  作者 : 小布
* @version 创建时间 : 2019年4月28日 下午12:54:58
* @explain 类说明 :
*/
public class Report {

	private Integer reId;
	private Integer reType;
	private Integer reDoctor;
	private String reDoctorName;
	private Integer auditDoctor;
	private String auditDoctorName;
	private Integer visitDoctor;
	private String visitDoctorName;
	private String reTime;
	private String auditTime;
	private String reContent;
	private String auditContent;
	private String reportUrl;
	private Integer send;
	private Integer currentDoctor;
	private String currentDoctorName;
	private Integer currentType;
	private String imageUrl;
	private String grossFinding;
	private Integer andtodo;
	private String visitContent;
	private String createTime;
	private Integer syncHis;
	private String hisResult;

	private String template;
	private String immune;

	private String reTypeName;
	private String pathId;
	private String patName;
	private String printTime;

	public String getPrintTime() {
		return printTime;
	}

	public void setPrintTime(String printTime) {
		this.printTime = printTime;
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
	public Integer getCurrentType() {
		return currentType;
	}
	public void setCurrentType(Integer currentType) {
		this.currentType = currentType;
	}
	public Integer getReId() {
		return reId;
	}
	public void setReId(Integer reId) {
		this.reId = reId;
	}
	public Integer getReType() {
		return reType;
	}
	public void setReType(Integer reType) {
		this.reType = reType;
	}
	public Integer getReDoctor() {
		return reDoctor;
	}
	public void setReDoctor(Integer reDoctor) {
		this.reDoctor = reDoctor;
	}
	public Integer getAuditDoctor() {
		return auditDoctor;
	}
	public void setAuditDoctor(Integer auditDoctor) {
		this.auditDoctor = auditDoctor;
	}
	public String getReTime() {
		return reTime;
	}
	public void setReTime(String reTime) {
		this.reTime = reTime;
	}
	public String getAuditTime() {
		return auditTime;
	}
	public void setAuditTime(String auditTime) {
		this.auditTime = auditTime;
	}
	public String getReContent() {
		return reContent;
	}
	public void setReContent(String reContent) {
		this.reContent = reContent;
	}
	public String getAuditContent() {
		return auditContent;
	}
	public void setAuditContent(String auditContent) {
		this.auditContent = auditContent;
	}
	public String getReportUrl() {
		return reportUrl;
	}
	public void setReportUrl(String reportUrl) {
		this.reportUrl = reportUrl;
	}
	public Integer getSend() {
		return send;
	}
	public void setSend(Integer send) {
		this.send = send;
	}
	public String getReTypeName() {
		return reTypeName;
	}
	public void setReTypeName(String reTypeName) {
		this.reTypeName = reTypeName;
	}
	public String getPathId() {
		return pathId;
	}
	public void setPathId(String pathId) {
		this.pathId = pathId;
	}
	public String getTemplate() {
		return template;
	}
	public void setTemplate(String template) {
		this.template = template;
	}
	public Integer getCurrentDoctor() {
		return currentDoctor;
	}
	public void setCurrentDoctor(Integer currentDoctor) {
		this.currentDoctor = currentDoctor;
	}
	public String getCurrentDoctorName() {
		return currentDoctorName;
	}
	public void setCurrentDoctorName(String currentDoctorName) {
		this.currentDoctorName = currentDoctorName;
	}
	public Integer getAndtodo() {
		return andtodo;
	}
	public void setAndtodo(Integer andtodo) {
		this.andtodo = andtodo;
	}
	public String getAuditDoctorName() {
		return auditDoctorName;
	}
	public void setAuditDoctorName(String auditDoctorName) {
		this.auditDoctorName = auditDoctorName;
	}
	public Integer getVisitDoctor() {
		return visitDoctor;
	}
	public void setVisitDoctor(Integer visitDoctor) {
		this.visitDoctor = visitDoctor;
	}
	public String getVisitDoctorName() {
		return visitDoctorName;
	}
	public void setVisitDoctorName(String visitDoctorName) {
		this.visitDoctorName = visitDoctorName;
	}
	public String getReDoctorName() {
		return reDoctorName;
	}
	public void setReDoctorName(String reDoctorName) {
		this.reDoctorName = reDoctorName;
	}
	public String getVisitContent() {
		return visitContent;
	}
	public void setVisitContent(String visitContent) {
		this.visitContent = visitContent;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getImmune() {
		return immune;
	}
	public void setImmune(String immune) {
		this.immune = immune;
	}
	public String getPatName() {
		return patName;
	}
	public void setPatName(String patName) {
		this.patName = patName;
	}

	public Integer getSyncHis() {
		return syncHis;
	}

	public void setSyncHis(Integer syncHis) {
		this.syncHis = syncHis;
	}

	public String getHisResult() {
		return hisResult;
	}

	public void setHisResult(String hisResult) {
		this.hisResult = hisResult;
	}
}
