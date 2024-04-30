package com.example.modules.wnhis.pojo;
/**
* @author  作者 : 小布
* @version 创建时间 : 2019年7月18日 下午1:12:24
* @explain 类说明 : 获取项目信息 YY03
*/
public class YY03 {

	/*
	 * codetype		查询类别：0:拼音, 1：五笔 ,2：项目代码
	 * code			查询值
	 */

	/*
	 * {
	 * 		"MsgCode":"YY03",
	 * 		"SendXml":"<codetype>2</codetype><code></code>"
	 * }
	 * */

	private String id;		//项目代码
	private String name;	//项目名称
	private String py;		//拼音
	private String wb;		//五笔
	private String xmdj;	//项目价格
	private String mzbz;	//门诊标志（0门诊不使用1门诊使用）
	private String zybz;	//住院标志（0住院不使用1门诊使用）
	private String type;	//区别yy03 和 yy04

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPy() {
		return py;
	}
	public void setPy(String py) {
		this.py = py;
	}
	public String getWb() {
		return wb;
	}
	public void setWb(String wb) {
		this.wb = wb;
	}
	public String getXmdj() {
		return xmdj;
	}
	public void setXmdj(String xmdj) {
		this.xmdj = xmdj;
	}
	public String getMzbz() {
		return mzbz;
	}
	public void setMzbz(String mzbz) {
		this.mzbz = mzbz;
	}
	public String getZybz() {
		return zybz;
	}
	public void setZybz(String zybz) {
		this.zybz = zybz;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
