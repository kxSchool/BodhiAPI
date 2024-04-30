package com.example.modules.wnhis.pojo;
/**
* @author  作者 : 小布
* @version 创建时间 : 2019年7月18日 下午1:11:54
* @explain 类说明 : 申请项目信息 JB03
*/
public class JB03 {

	/*
	 * blh		病历号(代码过滤了该字段，只做日志记录)
	 * brlb		0：门诊1：住院 3:体检
	 * patid	病人唯一码
	 * syxh		住院首页序号、门诊挂号序号
	 * qqxh		申请序号（无默认为0）
	 * tjrybh	体检人员编号（非体检人员默认为0）
	 * rq1		开始日期
	 * rq2		结束日期
	 * zxks		执行科室代码
	 * sqdxh	申请单序号（无默认为0）
	 * fph		默认为0（门诊使用）
	 * yexh		婴儿序号（查询大人时传0
	 */

	/*
	 * {
	 * 		"MsgCode":"JB03",
	 * 		"SendXml":"<blh>115438</blh><brlb>1</brlb><patid>65127</patid><syxh>118496</syxh><qqxh>0</qqxh><tjrybh>0</tjrybh><rq1></rq1><rq2></rq2><zxks></zxks><sqdxh></sqdxh><fph></fph><yexh></yexh>"
	 * }
	 * */

	private String Column1;
	private String Column2;

	private String blh;		//门诊号或者住院号/体检号
	private String brlb;	//0：门诊1：住院 3：体检
	private String patid;	//病人唯一吗
	private String syxh;	//首页序号
	private String qqxh;	//申请序号（序号）
	private String qqmxxh;	//申请序号（明细序号）[由于这个序号在确费时要求传入，因此增加了这个字段的输出]
	private String qqksmc;	//请求科室名称
	private String ysmc;	//医生名称
	private String qqrq;	//请求日期
	private String itemcode;//项目代码
	private String itemname;//项目名称
	private String price;	//项目价格
	private double itemqty;//项目数量
	private String itemunit;//项目单位
	private String url;		//申请单信息路径url
	private Integer itemtype;//项目类别（0临床项目1收费项目）
	private String jzbz;	//0：门诊 1：急诊
	private String ysdm;	//医生代码（门诊返回）
	private String qqlx;	//请求类型
	private String sqdxh;	//申请单序号
	private String yexh;	//住院婴儿序号
	private String BBLX;	//标本类型
	private Integer Status;	//状态（0：未确认，1：已确认，2：已拒绝）（这里都为0）
	private Integer ChargeFlag;//收费标志（0：未收费，1：已收费，2：已退费）（这里住院一般都为0，普通门诊病人为1）
	private Integer AddType;	//增加类型（0：HIS增加，1：医技上传）

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
	public String getQqxh() {
		return qqxh;
	}
	public void setQqxh(String qqxh) {
		this.qqxh = qqxh;
	}
	public String getQqmxxh() {
		return qqmxxh;
	}
	public void setQqmxxh(String qqmxxh) {
		this.qqmxxh = qqmxxh;
	}
	public String getQqksmc() {
		return qqksmc;
	}
	public void setQqksmc(String qqksmc) {
		this.qqksmc = qqksmc;
	}
	public String getYsmc() {
		return ysmc;
	}
	public void setYsmc(String ysmc) {
		this.ysmc = ysmc;
	}
	public String getQqrq() {
		return qqrq;
	}
	public void setQqrq(String qqrq) {
		this.qqrq = qqrq;
	}
	public String getItemcode() {
		return itemcode;
	}
	public void setItemcode(String itemcode) {
		this.itemcode = itemcode;
	}
	public String getItemname() {
		return itemname;
	}
	public void setItemname(String itemname) {
		this.itemname = itemname;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public double getItemqty() {
		return itemqty;
	}
	public void setItemqty(double itemqty) {
		this.itemqty = itemqty;
	}
	public String getItemunit() {
		return itemunit;
	}
	public void setItemunit(String itemunit) {
		this.itemunit = itemunit;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Integer getItemtype() {
		return itemtype;
	}
	public void setItemtype(Integer itemtype) {
		this.itemtype = itemtype;
	}
	public String getJzbz() {
		return jzbz;
	}
	public void setJzbz(String jzbz) {
		this.jzbz = jzbz;
	}
	public String getYsdm() {
		return ysdm;
	}
	public void setYsdm(String ysdm) {
		this.ysdm = ysdm;
	}
	public String getQqlx() {
		return qqlx;
	}
	public void setQqlx(String qqlx) {
		this.qqlx = qqlx;
	}
	public String getSqdxh() {
		return sqdxh;
	}
	public void setSqdxh(String sqdxh) {
		this.sqdxh = sqdxh;
	}
	public String getYexh() {
		return yexh;
	}
	public void setYexh(String yexh) {
		this.yexh = yexh;
	}
	public String getBBLX() {
		return BBLX;
	}
	public void setBBLX(String bBLX) {
		BBLX = bBLX;
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
	public Integer getStatus() {
		return Status;
	}
	public void setStatus(Integer status) {
		Status = status;
	}
	public Integer getChargeFlag() {
		return ChargeFlag;
	}
	public void setChargeFlag(Integer chargeFlag) {
		ChargeFlag = chargeFlag;
	}
	public Integer getAddType() {
		return AddType;
	}
	public void setAddType(Integer addType) {
		AddType = addType;
	}

}
