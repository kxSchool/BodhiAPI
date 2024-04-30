package com.example.modules.shenkang.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.example.modules.shenkang.pojo.HFileInfo;
import com.example.modules.shenkang.pojo.MedicalInfo;
import com.example.modules.shenkang.pojo.PatientInfo;
import com.example.modules.shenkang.pojo.SpecimenInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@DS("slave")
@Mapper
public interface WalnutCommonDao {

    List<HFileInfo> selectFileInfosByWaxId(@Param("parseInt")int parseInt, @Param("medId")Integer medId);

    @Select("SELECT materials_date from wax_info WHERE apply_id =#{applyId} limit 1")
    String selectTime(@Param("applyId") Integer applyId);

    List<SpecimenInfo> selSpecimenListByApplyId(@Param("applyId") Integer applyId);

    @Select("SELECT applyId FROM `task_assembly` WHERE auditTime like CONCAT(#{defaultStartDate},'%') and reType > 107")
    List<Integer> selectApplyIdlist(@Param("defaultStartDate") String defaultStartDate);

    @Select("SELECT patNo FROM task_assembly WHERE applyId =#{applyId}")
    String selectHisIdByApplyId(@Param("applyId")Integer applyId);

    @Select("SELECT pathId FROM task_assembly WHERE applyId=#{applyId}")
    String selectPathIdByApplyId(@Param("applyId") Integer applyId);

    PatientInfo selectPatient02(@Param("patients") String patients);//根据pat_no 查询数据

    MedicalInfo selectMediInfoByApplyId(@Param("applyId") Integer applyId);
}
