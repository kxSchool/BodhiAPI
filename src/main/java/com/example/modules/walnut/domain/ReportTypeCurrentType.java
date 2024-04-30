package com.example.modules.walnut.domain;
/**
* @author  作者 : 小布
* @version 创建时间 : 2019年6月6日 下午2:39:22
* @explain 类说明 :
*/
public class ReportTypeCurrentType {

	private Integer reType;
	private Integer currentType;
	private Integer visitDoctor;
	private Integer reDoctor;
	private Integer auditDoctor;
	private Integer andtodo;
	private Integer reContent;
	private String reContentDetail;
	private Integer send;
	private String grossFinding;


	public String getReContentDetail() {
		return reContentDetail;
	}

	public void setReContentDetail(String reContentDetail) {
		this.reContentDetail = reContentDetail;
	}

	public String getGrossFinding() {
		return grossFinding;
	}

	public void setGrossFinding(String grossFinding) {
		this.grossFinding = grossFinding;
	}

	public Integer getSend() {
		return send;
	}
	public void setSend(Integer send) {
		this.send = send;
	}
	public Integer getReDoctor() {
		return reDoctor;
	}
	public void setReDoctor(Integer reDoctor) {
		this.reDoctor = reDoctor;
	}
	public Integer getReType() {
		return reType;
	}
	public void setReType(Integer reType) {
		this.reType = reType;
	}
	public Integer getCurrentType() {
		return currentType;
	}
	public void setCurrentType(Integer currentType) {
		this.currentType = currentType;
	}
	public Integer getAndtodo() {
		return andtodo;
	}
	public void setAndtodo(Integer andtodo) {
		this.andtodo = andtodo;
	}
	public Integer getReContent() {
		return reContent;
	}
	public void setReContent(Integer reContent) {
		this.reContent = reContent;
	}
	public Integer getVisitDoctor() {
		return visitDoctor;
	}
	public void setVisitDoctor(Integer visitDoctor) {
		this.visitDoctor = visitDoctor;
	}
	public Integer getAuditDoctor() {
		return auditDoctor;
	}
	public void setAuditDoctor(Integer auditDoctor) {
		this.auditDoctor = auditDoctor;
	}

}
