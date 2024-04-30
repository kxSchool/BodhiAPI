package com.example.modules.wnhis.pojo;
/**
* @author  作者 : 小布
* @version 创建时间 : 2019年7月18日 下午1:12:44
* @explain 类说明 : 报告结果回传 BG03
*/
public class BG03 {

	/*
	 * repno		报告单号(不允许为空)
	 * replb		报告类别(不允许为空)
	 * xmdm			医技项目代码
	 * xmmc			医技项目名称
	 * jgckz		参考值
	 * qqmxxh		请求明细序号（可以为空）JB03.qqxh
	 * xmjg			项目结果
	 * xmdw			项目单位
	 * xjbz			细菌项目标志(不允许为空)
	 * xjmc			细菌名称
	 * kssmc		抗生素名称
	 * gmjg			药敏结果(是否过敏)
	 * jgxh			结果序号
	 * gdbz			高低标志（初值为0）
	 * crbz			传染标志（0：未传染；1：传染）
	 */

	//注：该接口只处理文字报告信息（医技项目为06的功能暂未实现）。

	/*
	 * {
	 * 		"MsgCode":"BG03",
	 * 		"SendXml":"<repno>1736598</repno><replb>bl</replb><xmdm>27020004</xmdm><xmmc>脱落细胞</xmmc><xmjg>70</xmjg><xmdw>例</xmdw><jgckz>0-100</jgckz><qqmxxh></qqmxxh><xjbz>1</xjbz><xjmc>细菌测试</xjmc><kssmc>抗菌素测试</kssmc><gmjg>过敏</gmjg><jgxh>0909</jgxh><gdbz>0</gdbz><crbz>0</crbz>"
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
