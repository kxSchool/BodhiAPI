package com.example.modules.wnhis.pojo;
/**
* @author  作者 : 小布
* @version 创建时间 : 2019年7月18日 下午1:12:37
* @explain 类说明 : 报告回收 BG02
*/
public class BG02 {

	/*
	 * repno		报告单号
	 * replb		报告类别
	 * hslb			类别（0：回收1：查询）
	 */

	//输出信息：具体错误信息，如果不能回收显示具体信息。

	/*
	 * {
	 * 		"MsgCode":"BG02",
	 * 		"SendXml":"<repno>17369</repno><replb>bl</replb><hslb>1</hslb>"
	 * }
	 * */

	private String Column1;
	private String Column2;

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
