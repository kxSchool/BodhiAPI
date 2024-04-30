package com.example.modules.walnut.mapper;

import com.example.modules.walnut.domain.*;
import com.example.modules.walnut.domain.HIS.*;
import com.example.modules.wnhis.pojo.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
* @author : 小布
* @CreateDate : 2019年3月8日 上午10:35:58
* @explain : 主要的数据源
*/

@Mapper
public interface MasterMapper {

	//查看本库申请单
	String[] queryMyApplyNoInMysql(@Param("patientType")Integer patientType, @Param("hisId")String hisId);

	//查字典送检医生名称
	String queryInspectDoctor(@Param("code")String code);

	//BG01
	BG01 queryBG01ByReId(@Param("reId")Integer reId);

	//根据科室代码判断送检单位
	String queryInspectUnitByInspectOffice(@Param("inspectOffice")String inspectOffice);

	//查看QF01VO
	Charge queryQF01ByReId(@Param("chId")Integer chId);

	//添加收费项目
//	void insertChargeList(@Param("charge")List<Charge> charge,);
//	void insertChargeList(@Param("charge")List<Charge> charge, @Param("applyId")Integer applyId);

	//根据chId修改收费项目
//	void updateChargeByChId(@Param("charge")Charge charge);

	int searchReportIdByApplyNo(@Param("applyId") Integer applyId);

	String selChargeNoByInfo(@Param("chId")Integer chId);

    String selectYY03ByCode(@Param("chargeCode")String chargeCode);

	TaskAssembly selectAllByapplyId(@Param("applyId")Integer applyId);
	String queryDictionaryByCode(@Param("type") String type, @Param("code") String code);


	SysUser selUserInfoById(@Param("userId") Integer userId);
	//根据报告id查看当前状态
	ReportTypeCurrentType queryReCurrentTypeByReId(@Param("reId")Integer reId);

	//根据病理号查看患者信息
	PatientInfoApply queryPatientInfoByPathId(@Param("patNo")String patNo, @Param("applyId")Integer applyId);

	String selectReContByApplyNo(@Param("applyId")Integer applyId);

	String selectAuditTimeByApplyNo(@Param("applyId")Integer applyId);

	//查看体检患者的基础信息
	Map<String, Object> selectMecPatientInfoByReId(@Param("reId")Integer reId);

	String selectFullNameById(@Param("emId")Integer emId);

	//报告里查看报告信息
	Report queryReportContentByPathId(@Param("applyId")Integer applyId);

}
