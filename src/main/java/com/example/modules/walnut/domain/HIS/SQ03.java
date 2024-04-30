package com.example.modules.walnut.domain.HIS;
/**
* @author  作者 : 小布
* @version 创建时间 : 2019年7月18日 下午1:12:56
* @explain 类说明 : 电子申请单列表SQ03
*/
public class SQ03 {

	/*
	 * brlb			病人类别（0：门诊；1：住院）
	 * cardtype		0 (目前只支持0，以后会扩展)
	 * card			病历号（住院） patid （门诊）
	 */

	/*
	 * {
	 * 		"MsgCode":"SQ03",
	 * 		"SendXml":"<brlb>1</brlb><cardtype>0</cardtype><card>110877</card>"
	 * }
	 * */

	private String sqdxh;	//申请单序号
	private String hzxm;	//患者姓名
	private String jcmd;	//检查目的
	private String lczd;	//临床诊断
	private String jcxm;	//检查项目
	private String bszl;	//病史资料

	private String Column1;
	private String Column2;

	public String getSqdxh() {
		return sqdxh;
	}
	public void setSqdxh(String sqdxh) {
		this.sqdxh = sqdxh;
	}
	public String getHzxm() {
		return hzxm;
	}
	public void setHzxm(String hzxm) {
		this.hzxm = hzxm;
	}
	public String getJcmd() {
		return jcmd;
	}
	public void setJcmd(String jcmd) {
		this.jcmd = jcmd;
	}
	public String getLczd() {
		return lczd;
	}
	public void setLczd(String lczd) {
		this.lczd = lczd;
	}
	public String getJcxm() {
		return jcxm;
	}
	public void setJcxm(String jcxm) {
		this.jcxm = jcxm;
	}
	public String getBszl() {
		return bszl;
	}
	public void setBszl(String bszl) {
		this.bszl = bszl;
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
