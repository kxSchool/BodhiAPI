package com.example.modules.wnhis.pojo;
/**
* @author  作者 : 小布
* @version 创建时间 : 2019年7月18日 下午1:12:02
* @explain 类说明 : 项目确认/取消/退费 QF01
*/
public class QF01 {

	/*
	 * blh		门诊号或者住院号
	 * brlb		0：门诊1：住院 3：体检
	 * xmlb		0临床项目1收费项目(JB03- itemtype)
	 * patid	病人唯一码
	 * syxh		首页序号
	 * zxksdm	确认科室代码(需要接口传入是哪个科室进行确费的)
	 * zxysdm	确认人员代码(需要接口传入是哪个操作员进行确费的)
	 * qqxh		申请序号（无默认为0）
	 * qqmxxh	申请序号（明细序号）[由于这个序号在确费时要求传入，在返回中已经增加了这个字段的输出]
	 * itemcode	项目代码
	 * itemname	项目名称
	 * price	项目单价
	 * itemqty	项目数量
	 * xmstatus	项目状态：0不处理1确认2拒绝3撤销(增加撤销处理)
	 * sfflag	收费状态：0不收费1收费2退费
	 * djlb		单价类别(0:使用原来的单价1：自定义价格)
	 * bgdh		报告单号
	 * bglx		报告类型
	 * tssm		自定义单价时需要给出解释，否则确费不成功 如果需要使用自定义单价则djlb需要设置为1 同时给出说明his会纪录到对应的项目信息的备注上面
	 * tjrybh	体检人员编号
	 */

	/*
	 * {
	 * 		"MsgCode":"QF01",
	 * 		"SendXml":"<blh>2123213123</blh><brlb>1</brlb><xmlb>1</xmlb><patid>65766</patid><syxh>110310</syxh><zxksdm>7009</zxksdm><zxysdm>Y165</zxysdm><qqxh>673</qqxh><qqmxxh>173</qqmxxh><itemcode>0006</itemcode><itemname>病理申请</itemname><price>0.0000</price><itemqty>1.00</itemqty><xmstatus>1</xmstatus><sfflag>1</sfflag><djlb>0</djlb><bgdh></bgdh><bglx></bglx><tssm></tssm><tjrybh></tjrybh>"
	 * }
	 * */

	/*
	 * 	门诊确认收费：xmstatus:1, sfflag:0, brlb:0
	 *	住院确认收费：xmstatus:1, sfflag:1, brlb:1
	 *	住院取消收费： xmstatus:3, sfflag:0, brlb:1
	 *	门诊/住院退费： xmstatus:3, sfflag:2, brlb:0
	 */

	//输出信息：T成功 F失败

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

	@Override
	public String toString() {
		return "QF01{" +
				"Column1='" + Column1 + '\'' +
				", Column2='" + Column2 + '\'' +
				'}';
	}
}
