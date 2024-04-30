package com.example.modules.walnut.domain.HIS;
/**
* @author  作者 : 小布
* @version 创建时间 : 2019年7月18日 上午9:01:31
* @explain 类说明 : 申请病人信息 JB01
*/
public class JB01 {

	/*
	 * codetype	病人状态
	 * 	1:住院/门诊号指病理号
	 * 	2:卡号
	 * 	3:PatientID HIS中的 patId(门诊)patId(住院)
	 * 	4:CureNohis中的 ghsjh(门诊) 和 syxh(住院)
	 * 	5:条形码
	 * code	类别对应值,用于查找基本信息
	 * brlb	病人类别	0:门诊，1:住院,3:体检
	 */

	/*
	 * {
	 * 		"MsgCode":"JB01",
	 * 		"SendXml":"<codetype>3</codetype><code>89426</code><brlb>0</brlb>"
	 * }
	 * */

	private String Column1;
	private String Column2;

	private String PatientID;	//病人唯一内码
	private String HospNo;		//门诊号(住院则为住院号)
	private String PatName;		//病人名称
	private Integer Sex;		//性别(1:男,2:女,3:其他)
	private String Age;			//年龄
	private String AgeUnit;		//年龄单位(例如岁)
	private String WardOrReg;	//病区标志(干保病人使用=2)
	private String ChargeType;	//医保代码
	private String CureNo;		//病人唯一码
	private String tjrybh;		//体检人员编号(体检人员内部编号，同CureNo字段)(暂时没有提供)
	private String CardNo;		//病人卡号
	private String ApplyDept;	//科室代码
	private String Ward;		//病区代码(住院使用)
	private String BedNo;		//床号
	private String ApplyDoctor;	//医生代码
	private String ClincDesc;	//诊断代码
	private String IDNum;		//身份证号
	private String Phone;		//电话
	private String Address;		//地址
	private String Zip;			//邮编
	private String Career;		//职业
	private String Nation;		//国家
	private String Qfyy;		//区分意义
	private String ToDoc;		//申请医生
	private String SendNo;		//申请单号
	private String Syxh;		//住院首页序号
	private String bqmc;		//病区名称(住院使用)
	private String yexh;		//婴儿序号
	private String DeptName;	//科室名称
	private String Jzlsh;		//就诊流水号
	private String Klx;			//卡类型
	private String zdmc;		//
	private String ClincName;	//诊断名称
	private String ChargetypeName;//医保说明
	private String ToDocName;	//医生姓名
	private String Birthday;	//生日
	private String Marriage;	//婚姻状况
	private String yename;		//住院婴儿名称
	private String yebirth;		//住院婴儿生日
	private String yesex;		//住院婴儿性别

	public String getPatientID() {
		return PatientID;
	}
	public void setPatientID(String patientID) {
		PatientID = patientID;
	}
	public String getHospNo() {
		return HospNo;
	}
	public void setHospNo(String hospNo) {
		HospNo = hospNo;
	}
	public String getPatName() {
		return PatName;
	}
	public void setPatName(String patName) {
		PatName = patName;
	}
	public Integer getSex() {
		return Sex;
	}
	public void setSex(Integer sex) {
		Sex = sex;
	}
	public String getAge() {
		return Age;
	}
	public void setAge(String age) {
		Age = age;
	}
	public String getAgeUnit() {
		return AgeUnit;
	}
	public void setAgeUnit(String ageUnit) {
		AgeUnit = ageUnit;
	}
	public String getWardOrReg() {
		return WardOrReg;
	}
	public void setWardOrReg(String wardOrReg) {
		WardOrReg = wardOrReg;
	}
	public String getChargeType() {
		return ChargeType;
	}
	public void setChargeType(String chargeType) {
		ChargeType = chargeType;
	}
	public String getTjrybh() {
		return tjrybh;
	}
	public void setTjrybh(String tjrybh) {
		this.tjrybh = tjrybh;
	}
	public String getCardNo() {
		return CardNo;
	}
	public void setCardNo(String cardNo) {
		CardNo = cardNo;
	}
	public String getBedNo() {
		return BedNo;
	}
	public void setBedNo(String bedNo) {
		BedNo = bedNo;
	}
	public String getApplyDoctor() {
		return ApplyDoctor;
	}
	public void setApplyDoctor(String applyDoctor) {
		ApplyDoctor = applyDoctor;
	}
	public String getClincDesc() {
		return ClincDesc;
	}
	public void setClincDesc(String clincDesc) {
		ClincDesc = clincDesc;
	}
	public String getIDNum() {
		return IDNum;
	}
	public void setIDNum(String iDNum) {
		IDNum = iDNum;
	}
	public String getPhone() {
		return Phone;
	}
	public void setPhone(String phone) {
		Phone = phone;
	}
	public String getAddress() {
		return Address;
	}
	public void setAddress(String address) {
		Address = address;
	}
	public String getZip() {
		return Zip;
	}
	public void setZip(String zip) {
		Zip = zip;
	}
	public String getCareer() {
		return Career;
	}
	public void setCareer(String career) {
		Career = career;
	}
	public String getNation() {
		return Nation;
	}
	public void setNation(String nation) {
		Nation = nation;
	}
	public String getQfyy() {
		return Qfyy;
	}
	public void setQfyy(String qfyy) {
		Qfyy = qfyy;
	}
	public String getToDoc() {
		return ToDoc;
	}
	public void setToDoc(String toDoc) {
		ToDoc = toDoc;
	}
	public String getSendNo() {
		return SendNo;
	}
	public void setSendNo(String sendNo) {
		SendNo = sendNo;
	}
	public String getSyxh() {
		return Syxh;
	}
	public void setSyxh(String syxh) {
		Syxh = syxh;
	}
	public String getBqmc() {
		return bqmc;
	}
	public void setBqmc(String bqmc) {
		this.bqmc = bqmc;
	}
	public String getYexh() {
		return yexh;
	}
	public void setYexh(String yexh) {
		this.yexh = yexh;
	}
	public String getDeptName() {
		return DeptName;
	}
	public void setDeptName(String deptName) {
		DeptName = deptName;
	}
	public String getJzlsh() {
		return Jzlsh;
	}
	public void setJzlsh(String jzlsh) {
		Jzlsh = jzlsh;
	}
	public String getKlx() {
		return Klx;
	}
	public void setKlx(String klx) {
		Klx = klx;
	}
	public String getZdmc() {
		return zdmc;
	}
	public void setZdmc(String zdmc) {
		this.zdmc = zdmc;
	}
	public String getClincName() {
		return ClincName;
	}
	public void setClincName(String clincName) {
		ClincName = clincName;
	}
	public String getChargetypeName() {
		return ChargetypeName;
	}
	public void setChargetypeName(String chargetypeName) {
		ChargetypeName = chargetypeName;
	}
	public String getToDocName() {
		return ToDocName;
	}
	public void setToDocName(String toDocName) {
		ToDocName = toDocName;
	}

	public String getBirthday() {
		return Birthday;
	}

	public void setBirthday(String birthday) {
		Birthday = birthday;
	}

	public String getMarriage() {
		return Marriage;
	}
	public void setMarriage(String marriage) {
		Marriage = marriage;
	}
	public String getYename() {
		return yename;
	}
	public void setYename(String yename) {
		this.yename = yename;
	}
	public String getYebirth() {
		return yebirth;
	}
	public void setYebirth(String yebirth) {
		this.yebirth = yebirth;
	}
	public String getYesex() {
		return yesex;
	}
	public void setYesex(String yesex) {
		this.yesex = yesex;
	}
	public String getCureNo() {
		return CureNo;
	}
	public void setCureNo(String cureNo) {
		CureNo = cureNo;
	}
	public String getApplyDept() {
		return ApplyDept;
	}
	public void setApplyDept(String applyDept) {
		ApplyDept = applyDept;
	}
	public String getWard() {
		return Ward;
	}
	public void setWard(String ward) {
		Ward = ward;
	}
	public String getColumn1() {
		return Column1;
	}
	public void setColumn1(String column1) {
		Column1 = column1;
	}
	public String getColumn2() {
		return Column2;
	}
	public void setColumn2(String column2) {
		Column2 = column2;
	}

}
