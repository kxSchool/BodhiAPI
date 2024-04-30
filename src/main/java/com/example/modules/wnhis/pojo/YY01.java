package com.example.modules.wnhis.pojo;
/**
* @author  作者 : 小布
* @version 创建时间 : 2019年7月18日 下午1:12:08
* @explain 类说明 : 获取科室信息 YY01
*/
public class YY01 {

	private String id;		//科室代码
	private String name;	//科室名称
	private String py;		//拼音
	private String wb;		//五笔

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

}
