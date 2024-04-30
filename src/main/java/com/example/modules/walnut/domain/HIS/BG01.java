package com.example.modules.walnut.domain.HIS;
/**
* @author  作者 : 小布
* @version 创建时间 : 2019年7月18日 下午1:12:31
* @explain 类说明 : 报告发布 BG01
*/
public class BG01 {

	/*
	 * repno		报告单申请号
	 * reqno		原始申请号
	 * syxh			首页序号
	 * patid		病人内码
	 * blh			病历号(体检人员编号)
	 * cardno		卡号
	 * hzxm			患者姓名
	 * sex			性别
	 * age			年龄
	 * sjksdm		送检科室
	 * sjksmc		送检科室名称
	 * bqdm			病区编码
	 * bqmc			病区名称
	 * cwdm			床位号
	 * sjysdm		送检医生
	 * sjysxm		送检医生姓名
	 * sjrq			送检日期
	 * replb		报告单类别编码
	 * replbmc		报告单类别名称
	 * reprq		报告单日期
	 * xtbz			系统标志 0：门诊 1：住院 3：体检
	 * jcbw			检查部位
	 * jcysdm		检查医生代码
	 * jcysxm		检查医生姓名
	 * jcksdm		检查科室代码
	 * jcksmc		检查科室名称
	 * pubtime		发布日期
	 * crbz			传染标志0： 非传染病1： 传染病报告
	 * instname		设备名称
	 * wjbz			危机标志0：正常 1：危机
	 * lrysdm		书写医生代码
	 * lrysxm		书写医生姓名
	 * shysdm		审核医生代码
	 * shysxm		审核医生姓名
	 * yexh			婴儿序号：默认0;母婴同床传婴儿的xh
	 */

	//输出信息：无

	/*
	 * {
	 * 		"MsgCode":"BG01",
	 * 		"SendXml":"<repno>1736599</repno><reqno>1736599</reqno><syxh>120558</syxh><patid>71792</patid><blh>116624</blh><cardno>01080325203</cardno><hzxm>麻小琴</hzxm><sex>女</sex><age>22</age><sjksdm>1904</sjksdm><sjksmc>内科</sjksmc><bqdm>27</bqdm><bqmc>胸外科病区</bqmc><cwdm>014</cwdm><sjysdm>00</sjysdm><sjysxm>送医生</sjysxm><sjrq>2016071316</sjrq><replb>bl</replb><replbmc>病理</replbmc><reprq></reprq><xtbz>0</xtbz><jcbw>血液</jcbw><jcysdm>00</jcysdm><jcysxm></jcysxm><jcksdm>7009</jcksdm><cjksmc>病理科</cjksmc><pubtime>2019072216</pubtime><crbz>0</crbz><instname>0</instname>"
	 * }
	 * */

	private String Column1;
	private String Column2;

	private String repno;
	private String reqno;
	private String syxh;
	private String patid;
	private String blh;
	private String cardno;
	private String hzxm;
	private String sex;
	private String age;
	private String sjksdm;
	private String sjksmc;
	private String bqdm;
	private String bqmc;
	private String cwdm;
	private String sjysdm;
	private String sjysxm;
	private String sjrq;
	private String replb;
	private String replbmc;
	private String reprq;
	private String xtbz;
	private String jcbw;
	private String jcysdm;
	private String jcysxm;
	private String jcksdm;
	private String jcksmc;
	private String pubtime;
	private String crbz;
	private String instname;
	private String wjbz;
	private String lrysdm;
	private String lrysxm;
	private String shysdm;
	private String shysxm;
	private String yexh;

	public String getRepno() {
		return repno;
	}
	public void setRepno(String repno) {
		this.repno = repno;
	}
	public String getReqno() {
		return reqno;
	}
	public void setReqno(String reqno) {
		this.reqno = reqno;
	}
	public String getSyxh() {
		return syxh;
	}
	public void setSyxh(String syxh) {
		this.syxh = syxh;
	}
	public String getPatid() {
		return patid;
	}
	public void setPatid(String patid) {
		this.patid = patid;
	}
	public String getBlh() {
		return blh;
	}
	public void setBlh(String blh) {
		this.blh = blh;
	}
	public String getCardno() {
		return cardno;
	}
	public void setCardno(String cardno) {
		this.cardno = cardno;
	}
	public String getHzxm() {
		return hzxm;
	}
	public void setHzxm(String hzxm) {
		this.hzxm = hzxm;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getSjksdm() {
		return sjksdm;
	}
	public void setSjksdm(String sjksdm) {
		this.sjksdm = sjksdm;
	}
	public String getSjksmc() {
		return sjksmc;
	}
	public void setSjksmc(String sjksmc) {
		this.sjksmc = sjksmc;
	}
	public String getBqdm() {
		return bqdm;
	}
	public void setBqdm(String bqdm) {
		this.bqdm = bqdm;
	}
	public String getBqmc() {
		return bqmc;
	}
	public void setBqmc(String bqmc) {
		this.bqmc = bqmc;
	}
	public String getCwdm() {
		return cwdm;
	}
	public void setCwdm(String cwdm) {
		this.cwdm = cwdm;
	}
	public String getSjysdm() {
		return sjysdm;
	}
	public void setSjysdm(String sjysdm) {
		this.sjysdm = sjysdm;
	}
	public String getSjysxm() {
		return sjysxm;
	}
	public void setSjysxm(String sjysxm) {
		this.sjysxm = sjysxm;
	}
	public String getSjrq() {
		return sjrq;
	}
	public void setSjrq(String sjrq) {
		this.sjrq = sjrq;
	}
	public String getReplb() {
		return replb;
	}
	public void setReplb(String replb) {
		this.replb = replb;
	}
	public String getReplbmc() {
		return replbmc;
	}
	public void setReplbmc(String replbmc) {
		this.replbmc = replbmc;
	}
	public String getReprq() {
		return reprq;
	}
	public void setReprq(String reprq) {
		this.reprq = reprq;
	}
	public String getXtbz() {
		return xtbz;
	}
	public void setXtbz(String xtbz) {
		this.xtbz = xtbz;
	}
	public String getJcbw() {
		return jcbw;
	}
	public void setJcbw(String jcbw) {
		this.jcbw = jcbw;
	}
	public String getJcysdm() {
		return jcysdm;
	}
	public void setJcysdm(String jcysdm) {
		this.jcysdm = jcysdm;
	}
	public String getJcysxm() {
		return jcysxm;
	}
	public void setJcysxm(String jcysxm) {
		this.jcysxm = jcysxm;
	}
	public String getJcksdm() {
		return jcksdm;
	}
	public void setJcksdm(String jcksdm) {
		this.jcksdm = jcksdm;
	}
	public String getJcksmc() {
		return jcksmc;
	}
	public void setJcksmc(String jcksmc) {
		this.jcksmc = jcksmc;
	}
	public String getPubtime() {
		return pubtime;
	}
	public void setPubtime(String pubtime) {
		this.pubtime = pubtime;
	}
	public String getCrbz() {
		return crbz;
	}
	public void setCrbz(String crbz) {
		this.crbz = crbz;
	}
	public String getInstname() {
		return instname;
	}
	public void setInstname(String instname) {
		this.instname = instname;
	}
	public String getWjbz() {
		return wjbz;
	}
	public void setWjbz(String wjbz) {
		this.wjbz = wjbz;
	}
	public String getLrysdm() {
		return lrysdm;
	}
	public void setLrysdm(String lrysdm) {
		this.lrysdm = lrysdm;
	}
	public String getLrysxm() {
		return lrysxm;
	}
	public void setLrysxm(String lrysxm) {
		this.lrysxm = lrysxm;
	}
	public String getShysdm() {
		return shysdm;
	}
	public void setShysdm(String shysdm) {
		this.shysdm = shysdm;
	}
	public String getShysxm() {
		return shysxm;
	}
	public void setShysxm(String shysxm) {
		this.shysxm = shysxm;
	}
	public String getYexh() {
		return yexh;
	}
	public void setYexh(String yexh) {
		this.yexh = yexh;
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
