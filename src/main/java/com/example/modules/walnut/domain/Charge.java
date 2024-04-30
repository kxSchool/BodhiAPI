package com.example.modules.walnut.domain;
/**
* @author  作者 : 小布
* @version 创建时间 : 2019年4月28日 下午5:23:18
* @explain 类说明 :
*/
public class Charge {

	private Integer chId;			//id
	private String hisId;			//病历号
	private Integer patientType;	//病人类别
	private String patNo;			//内码
	private String syxh;			//首页序号
	private String requireNo;		//请求序号
	private String requireNoItem;	//申请单明细序号
	private String requireType;		//请求类型
	private String applyNo;         //申请单号
	private Integer applyId;
	private String applyDept;		//申请科室
	private String applyDoc;		//申请医生
	private String applyDocCode;	//申请医生代码
	private String applyDeptCode;	//申请科室代码
	private String applyTime;		//申请时间
	private String itemCode;		//项目代码
	private String itemName;		//项目名称
	private String itemPrice;		//项目价格
	private String itemUnit;		//项目单位
	private double itemQty;			//项目数量
	private Integer itemType;		//项目类型
	private String itemStatus;		//项目状态
	private String statusRemark;	//His返回信息
	private String url;				//项目Url
	private String isEmergency;		//急诊标志
	private String babyNo;			//婴儿序号
	private String specimenType;	//标本类型
	private Integer status;			//同步HIS状态
	private Integer chargeFlag;		//收费标志
	private Integer addType;		//增加类型
	private String chargeNo;		//收费序号
	private String chargeCode;		//收费序号
	private double chargePrice;		//收费序号
	private double chargeNum;		//收费序号


	public String getChargeCode() {
		return chargeCode;
	}

	public void setChargeCode(String chargeCode) {
		this.chargeCode = chargeCode;
	}

	public double getChargePrice() {
		return chargePrice;
	}

	public void setChargePrice(double chargePrice) {
		this.chargePrice = chargePrice;
	}

	public double getChargeNum() {
		return chargeNum;
	}

	public void setChargeNum(double chargeNum) {
		this.chargeNum = chargeNum;
	}

	public String getChargeNo() {
		return chargeNo;
	}

	public void setChargeNo(String chargeNo) {
		this.chargeNo = chargeNo;
	}

	public Integer getApplyId() {
		return applyId;
	}

	public void setApplyId(Integer applyId) {
		this.applyId = applyId;
	}

	public Integer getChId() {
		return chId;
	}
	public void setChId(Integer chId) {
		this.chId = chId;
	}
	public String getHisId() {
		return hisId;
	}
	public void setHisId(String hisId) {
		this.hisId = hisId;
	}
	public Integer getPatientType() {
		return patientType;
	}
	public void setPatientType(Integer patientType) {
		this.patientType = patientType;
	}
	public String getPatNo() {
		return patNo;
	}
	public void setPatNo(String patNo) {
		this.patNo = patNo;
	}
	public String getSyxh() {
		return syxh;
	}
	public void setSyxh(String syxh) {
		this.syxh = syxh;
	}
	public String getRequireNo() {
		return requireNo;
	}
	public void setRequireNo(String requireNo) {
		this.requireNo = requireNo;
	}
	public String getRequireNoItem() {
		return requireNoItem;
	}
	public void setRequireNoItem(String requireNoItem) {
		this.requireNoItem = requireNoItem;
	}
	public String getRequireType() {
		return requireType;
	}
	public void setRequireType(String requireType) {
		this.requireType = requireType;
	}
	public String getApplyNo() {
		return applyNo;
	}
	public void setApplyNo(String applyNo) {
		this.applyNo = applyNo;
	}
	public String getApplyDept() {
		return applyDept;
	}
	public void setApplyDept(String applyDept) {
		this.applyDept = applyDept;
	}
	public String getApplyDoc() {
		return applyDoc;
	}
	public void setApplyDoc(String applyDoc) {
		this.applyDoc = applyDoc;
	}
	public String getApplyTime() {
		return applyTime;
	}
	public void setApplyTime(String applyTime) {
		this.applyTime = applyTime;
	}
	public String getApplyDocCode() {
		return applyDocCode;
	}
	public void setApplyDocCode(String applyDocCode) {
		this.applyDocCode = applyDocCode;
	}
	public String getApplyDeptCode() {
		return applyDeptCode;
	}
	public void setApplyDeptCode(String applyDeptCode) {
		this.applyDeptCode = applyDeptCode;
	}
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getItemPrice() {
		return itemPrice;
	}
	public void setItemPrice(String itemPrice) {
		this.itemPrice = itemPrice;
	}
	public String getItemUnit() {
		return itemUnit;
	}
	public void setItemUnit(String itemUnit) {
		this.itemUnit = itemUnit;
	}
	public double getItemQty() {
		return itemQty;
	}
	public void setItemQty(double itemQty) {
		this.itemQty = itemQty;
	}
	public Integer getItemType() {
		return itemType;
	}
	public void setItemType(Integer itemType) {
		this.itemType = itemType;
	}
	public String getItemStatus() {
		return itemStatus;
	}
	public void setItemStatus(String itemStatus) {
		this.itemStatus = itemStatus;
	}
	public String getStatusRemark() {
		return statusRemark;
	}
	public void setStatusRemark(String statusRemark) {
		this.statusRemark = statusRemark;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getIsEmergency() {
		return isEmergency;
	}
	public void setIsEmergency(String isEmergency) {
		this.isEmergency = isEmergency;
	}
	public String getBabyNo() {
		return babyNo;
	}
	public void setBabyNo(String babyNo) {
		this.babyNo = babyNo;
	}
	public String getSpecimenType() {
		return specimenType;
	}
	public void setSpecimenType(String specimenType) {
		this.specimenType = specimenType;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getChargeFlag() {
		return chargeFlag;
	}
	public void setChargeFlag(Integer chargeFlag) {
		this.chargeFlag = chargeFlag;
	}
	public Integer getAddType() {
		return addType;
	}
	public void setAddType(Integer addType) {
		this.addType = addType;
	}

}
