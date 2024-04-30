package com.example.modules.walnut.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.modules.walnut.mapper.WalnutLogMapper;
import com.example.modules.walnut.model.WalnutLog;
import com.example.modules.walnut.model.WalnutLogNode;
import com.example.modules.walnut.service.WalnutLogService;
import com.example.modules.wnhis.pojo.LogDetailsNode;
import com.example.utils.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Toomth
 * @since 2021-03-25
 */
@Service
public class WalnutLogServiceImpl extends ServiceImpl<WalnutLogMapper, WalnutLog> implements WalnutLogService {

    @Override
    public Map<String, Object> getLogList(Integer type, String method, String hisId, String startTime, String endTime, Integer pageSize, Integer pageNum) {
        Map<String,Object> result = new HashMap<>();
        Page<WalnutLog> page = new Page<>(pageNum,pageSize);
        QueryWrapper<WalnutLog> wrapper = new QueryWrapper<>();
        if (type!=null&&type==0){
            wrapper.gt("state",type);
        }
        if (!StringUtils.isEmpty(method)) {
            wrapper.eq("method",method);
        }
        if (!StringUtils.isEmpty(hisId)) {
            wrapper.eq("hisId",hisId);
        }
        if (!StringUtils.isEmpty(startTime)&&!StringUtils.isEmpty(endTime)){
            Long s= DateUtils.dateParseToLong(startTime);
            Long e= DateUtils.dateParseToLong(endTime);
            wrapper.between("create",s,e);
        }
        Page<WalnutLog> hisDlsit=page(page,wrapper);
        List<WalnutLog> records = hisDlsit.getRecords();
        List<WalnutLogNode> list=new ArrayList<>();
        for (int i = 0; i < records.size(); i++) {
            WalnutLogNode logDetailsNode=new WalnutLogNode();
            logDetailsNode.setHisId(records.get(i).getHisId());
            logDetailsNode.setTime(DateUtils.getDate(records.get(i).getCreatetime()));
            logDetailsNode.setId(records.get(i).getId());
            logDetailsNode.setNo(records.get(i).getNo());
            logDetailsNode.setMethod(records.get(i).getMethod());
            logDetailsNode.setReqParam(records.get(i).getReqParam());
            logDetailsNode.setRespParam(records.get(i).getRespParam());
            list.add(logDetailsNode);
        }
        result.put("list",list);
        result.put("total",hisDlsit.getTotal());
        result.put("pages",hisDlsit.getPages());
        result.put("size",hisDlsit.getSize());
        return result;
    }
}
