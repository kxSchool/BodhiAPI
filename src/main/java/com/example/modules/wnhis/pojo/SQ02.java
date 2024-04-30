package com.example.modules.wnhis.pojo;
/**
* @author  作者 : 小布
* @version 创建时间 : 2019年7月18日 下午1:12:50
* @explain 类说明 : 申请单明细 SQ02
*/
public class SQ02 {

	/*
	 * brlb		病人类别（0：门诊；1：住院）
	 * sqdxh	电子申请单序号
	 */

	/*
	 * {
	 * 		"MsgCode":"SQ02",
	 * 		"SendXml":"<brlb>1</brlb><sqdxh>1708198</sqdxh>"
	 * }
	 * */

	//输出信息：根据HIS系统电子申请单模板设置，返回字段不定。

	private String caption;
	private String value;

	private String Column1;
	private String Column2;

	public String getCaption() {
		return caption;
	}
	public void setCaption(String caption) {
		this.caption = caption;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
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
