package com.example.modules.shenkang.mapper;

import com.example.modules.shenkang.pojo.HFileInfo;
import com.example.modules.shenkang.pojo.MedicalInfo;
import com.example.modules.shenkang.pojo.PatientInfo;
import com.example.modules.shenkang.pojo.SpecimenInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface CommonDao {

    List<HFileInfo> selectFileIdByApplyID_H(@Param("applyId") Integer applyId);

    MedicalInfo selectinfoByMedId(@Param("id") Integer id);

    String selectSearchTimeByPathNo(@Param("pathNo") String pathNo);

    void insertToken(@Param("access_token") String access_token);

    Integer insertPatient(@Param("patientInfo") PatientInfo patientInfo,@Param("searchDate")String updateDateTime);

    void insertFileInfo(@Param("fileInfo")List<HFileInfo> fileInfo);

    void insertSpecimenInfo(@Param("specimenInfos") SpecimenInfo specimenInfo, @Param("id") Integer id);

    void deleteSpecimenInfo(@Param("id") Integer id);

    Integer updateMedical(@Param("medicalInfo") MedicalInfo medicalInfo, @Param("searchDate")String updateDateTime);

    Integer insertMedical(@Param("medicalInfo")MedicalInfo medicalInfo,@Param("searchDate")String updateDateTime);

    int selectMed_id(@Param("pathNo") String pathNo);

    @Select("select count(*) from shenkang_medical_info where pathNo=#{pathNo}")
    int selectMedPatient_id(@Param("pathNo") String pathNo);

    void updataPatient(@Param("patientInfo") PatientInfo patientInfo, @Param("searchDate")String updateDateTime);

    @Select("select count(*) from shenkang_patient_info where pathNo=#{pathNo}")
    int selectPatPatient_id(@Param("pathNo") String pathNo);

    @Select("SELECT file_type as document_type,count(*) document_count FROM shenkang_file_info where medical_id=#{id} GROUP BY file_type")
    List<Map<String, Object>> selectFileTypeNum(@Param("id") Integer id);

    List<HFileInfo> selectFileIdByApplyID03(@Param("medId") Integer medId, @Param("fileType") String fileType);

    @Select("select FileUID from shenkang_file_info where file_id =#{fileFid}")
    String selectFUID(@Param("fileFid") Integer fileFid);

    void upFileUidById(@Param("fileId") Integer fileId, @Param("fileUid") String fileUid);

    HFileInfo selectFileByFileid(@Param("fileId") String fileId);

    @Select("select token from shenkang_tokens ORDER BY id desc  LIMIT 1")
    String selecrToken();

    List<HFileInfo> selectFilesIdByMedicalId(@Param("size") Integer pageSize, @Param("num") Integer pageNum, @Param("pathNo") String pathNo, @Param("pushState") String pushState, @Param("fileType") String fileType);

}
