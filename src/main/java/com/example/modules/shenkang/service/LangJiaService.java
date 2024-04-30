package com.example.modules.shenkang.service;

import com.example.modules.shenkang.mapper.CommonDao;
import com.example.modules.shenkang.mapper.DebugLogDao;
import com.example.modules.shenkang.pojo.DebugLog;
import com.example.modules.shenkang.pojo.HFileInfo;
import com.example.modules.shenkang.pojo.MedicalInfo;
import com.example.modules.shenkang.pojo.PatientJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LangJiaService {
    @Autowired
    private DebugLogDao debugLogDao;

    @Autowired
    private CommonDao commonDao;

    public int insertDegbugLog(DebugLog debugLog){
        return debugLogDao.insertDebugLog(debugLog);
    }

    public List<HFileInfo> getFileInfoByPathNo(String pathNo){
        MedicalInfo medicalInfo=debugLogDao.getMedicalidByPathNo(pathNo);
        List<HFileInfo> fileInfosTemp=new ArrayList<>();
        if(medicalInfo!=null){
            List<HFileInfo> fileInfos = commonDao.selectFileIdByApplyID03(medicalInfo.getId(), "ExamImage");
            List<HFileInfo> fileInfos02 = commonDao.selectFileIdByApplyID03(medicalInfo.getId(), "ExamImageROI");
            if(fileInfos!=null&&fileInfos.size()!=0){
                for (int i = 0; i < fileInfos.size(); i++) {
                    HFileInfo fileInfo = fileInfos.get(i);
                    fileInfosTemp.add(fileInfo);
                }
            }
            if(fileInfos02!=null&&fileInfos02.size()!=0){
                for (int j = 0; j < fileInfos02.size(); j++) {
                    HFileInfo fileInfoROI = fileInfos02.get(j);
                    fileInfosTemp.add(fileInfoROI);
                }
            }
        }
        return fileInfosTemp;
    }
}
