package com.example.modules.wnhis.pojo;
/**
* @author  作者 : 小布
* @version 创建时间 : 2019年7月18日 下午1:11:48
* @explain 类说明 : 申请病人列表 JB02
*/
public class JB02 {

	/*
	 * ksrq		开始日期
	 * jsrq		结束日期
	 * zxks		科室代码（如B超等）
	 * brlb		0:门诊1：住院3：体检
	 * blh		病历号（blh）/体检号
	 * fph		发票号码
	 * codetype	查询方式：0磁卡；1体检号
	 */

	/*
	 * {
	 * 		"MsgCode":"JB02",
	 * 		"SendXml":"<ksrq>20180101</ksrq><jsrq>20181231</jsrq><zxks></zxks><brlb></brlb><blh></blh><fph>0</fph><codetype>4</codetype>"
	 * }
	 * */

	private String Column1;
	private String Column2;

	private String blh;		//门诊号或者住院号
	private String brlb;	//0：门诊1：住院3：体检
	private String patid;	//病人唯一码
	private String syxh;	//首页序号
	private String qqrq;	//申请日期
	private String qqks;	//请求科室代码
	private String ysmc;	//医生名称
	private String qqxh;	//请求序号
	private String hzxm;	//病人姓名
	private String tjrybh;	//体检人员编号
	private String bqdm;	//病区代码
	private String cwdm;	//床位代码
	private String bqmc;	//病区名称
	private String sqdxh;	//申请单序号
	private String kdysks;	//开单医生科室名称

	public String getBlh() {
		return blh;
	}
	public void setBlh(String blh) {
		this.blh = blh;
	}
	public String getBrlb() {
		return brlb;
	}
	public void setBrlb(String brlb) {
		this.brlb = brlb;
	}
	public String getPatid() {
		return patid;
	}
	public void setPatid(String patid) {
		this.patid = patid;
	}
	public String getSyxh() {
		return syxh;
	}
	public void setSyxh(String syxh) {
		this.syxh = syxh;
	}
	public String getQqrq() {
		return qqrq;
	}
	public void setQqrq(String qqrq) {
		this.qqrq = qqrq;
	}
	public String getQqks() {
		return qqks;
	}
	public void setQqks(String qqks) {
		this.qqks = qqks;
	}
	public String getYsmc() {
		return ysmc;
	}
	public void setYsmc(String ysmc) {
		this.ysmc = ysmc;
	}
	public String getQqxh() {
		return qqxh;
	}
	public void setQqxh(String qqxh) {
		this.qqxh = qqxh;
	}
	public String getHzxm() {
		return hzxm;
	}
	public void setHzxm(String hzxm) {
		this.hzxm = hzxm;
	}
	public String getTjrybh() {
		return tjrybh;
	}
	public void setTjrybh(String tjrybh) {
		this.tjrybh = tjrybh;
	}
	public String getBqdm() {
		return bqdm;
	}
	public void setBqdm(String bqdm) {
		this.bqdm = bqdm;
	}
	public String getCwdm() {
		return cwdm;
	}
	public void setCwdm(String cwdm) {
		this.cwdm = cwdm;
	}
	public String getBqmc() {
		return bqmc;
	}
	public void setBqmc(String bqmc) {
		this.bqmc = bqmc;
	}
	public String getSqdxh() {
		return sqdxh;
	}
	public void setSqdxh(String sqdxh) {
		this.sqdxh = sqdxh;
	}
	public String getKdysks() {
		return kdysks;
	}
	public void setKdysks(String kdysks) {
		this.kdysks = kdysks;
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
