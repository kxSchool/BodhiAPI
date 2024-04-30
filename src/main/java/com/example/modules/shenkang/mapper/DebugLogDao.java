package com.example.modules.shenkang.mapper;


import com.example.modules.shenkang.pojo.DebugLog;
import com.example.modules.shenkang.pojo.HFileInfo;
import com.example.modules.shenkang.pojo.MedicalInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DebugLogDao {
    @Insert("insert into shenkang_debug_log(reqjson,rspjson,apiName,state,pathNo,reqdate,createTime,paramNo)" +
            "values(#{reqjson},#{rspjson},#{apiname},#{state},#{pathno},#{reqdate},#{createtime},#{paramNo})")
    int insertDebugLog(DebugLog record);
    @Select("select * from shenkang_medical_info where pathNo=#{pathNo}")
    MedicalInfo getMedicalidByPathNo(@Param("pathNo")String pathNo);

    @Select("SELECT file_name as fileName,file_path as filePath,file_type as fileType,file_fid as fileFid,FileUID as FileUID,file_id as fileId,pathNo,ObservationUID,filesize" +
            " from shenkang_file_info where pathNo=#{pathNo}")
    List<HFileInfo> getFileInfosByPathNo(@Param("pathNo")String pathNo);

    @Select("SELECT file_name as fileName,file_path as filePath,file_type as fileType,file_fid as fileFid,FileUID as FileUID,file_id as fileId,pathNo,ObservationUID,filesize" +
            " from shenkang_file_info where pathNo=#{pathNo} and file_type=#{fileType}")
    List<HFileInfo> getFileInfosByPathNoAndType(@Param("pathNo")String pathNo, @Param("fileType")String fileType);

    @Select("update shenkang_medical_info set ObservationUID=#{ObservationUID} where id=#{id}")
    Integer updateMedicalInfoObservationUIDByMedicalId(@Param("id")Integer id,@Param("ObservationUID")String ObservationUID);

    @Insert("insert into shenkang_file_info(file_fid,file_name,file_path,specimen_id,file_type,medical_id,FileUID,pathNo,ObservationUID,filesize)" +
            "values(#{fileFid},#{fileName},#{filePath},#{specimenId},#{fileType},#{medicalId},#{FileUID},#{pathNo},#{ObservationUID},#{filesize})")
    int insertFile(HFileInfo hFileInfo);

}
