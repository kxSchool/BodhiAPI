package com.example.modules.wnhis.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.modules.wnhis.mapper.HisDetailsMapper;
import com.example.modules.wnhis.pojo.HisDetails;
import com.example.modules.wnhis.pojo.LogDetailsNode;
import com.example.modules.wnhis.service.HisDetailsService;

import com.example.utils.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class HisDetailsServiceImpl extends ServiceImpl<HisDetailsMapper, HisDetails> implements HisDetailsService {


    @Autowired
    HisDetailsMapper hisDetailsMapper;
    @Override
    public List<HisDetails> queryByNo(String no) {
        QueryWrapper<HisDetails> wrapper = new QueryWrapper<>();
        wrapper.eq("no",no);
        return list(wrapper);
    }

    @Override
    public Map<String,Object> queryByMethod(Integer type, String method, String hisId, String startTime, String endTime, Integer pageSize, Integer pageNum) {
        Map<String,Object> result = new HashMap<>();
        Page<HisDetails> page = new Page<>(pageNum,pageSize);
        QueryWrapper<HisDetails> wrapper = new QueryWrapper<>();
        if (type!=null&&type==0){
            wrapper.gt("state","0");
        }
        if (!StringUtils.isEmpty(method)) {
            wrapper.eq("method",method);
        }
        if (!StringUtils.isEmpty(hisId)) {
            wrapper.eq("param",hisId);
        }
        if (!StringUtils.isEmpty(startTime)&&!StringUtils.isEmpty(endTime)){
            Long s= DateUtils.dateParseToLong(startTime);
            Long e= DateUtils.dateParseToLong(endTime);
            wrapper.between("create_time",s,e);
        }
        Page<HisDetails> hisDlsit=page(page,wrapper);
        List<HisDetails> records = hisDlsit.getRecords();
        List<LogDetailsNode> list=new ArrayList<>();
        for (int i = 0; i < records.size(); i++) {
            LogDetailsNode logDetailsNode=new LogDetailsNode();
            logDetailsNode.setTime(DateUtils.getDate(records.get(i).getCreateTime()));
            logDetailsNode.setId(records.get(i).getId());
            logDetailsNode.setNo(records.get(i).getNo());
            logDetailsNode.setHospitalNo(records.get(i).getHospitalNo());
            logDetailsNode.setMethod(records.get(i).getMethod());
            logDetailsNode.setParam(records.get(i).getParam());
            logDetailsNode.setReqParam(records.get(i).getReqParam());
            logDetailsNode.setRespParam(records.get(i).getRespParam());
            logDetailsNode.setState(records.get(i).getState());
            logDetailsNode.setCreateTime(records.get(i).getCreateTime());
            list.add(logDetailsNode);
        }
        result.put("records",list);
        result.put("total",hisDlsit.getTotal());
        result.put("pages",hisDlsit.getPages());
        result.put("size",hisDlsit.getSize());
        return result;
    }

    @Override
    public String parsingXml(Integer id) {
        String resp=hisDetailsMapper.selectRespParamById(id);
        return resp;
    }

}
