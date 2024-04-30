package com.example.modules.walnut.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.modules.walnut.mapper.WalnutHisLogMapper;
import com.example.modules.walnut.model.WalnutHisLog;
import com.example.modules.walnut.model.WalnutHisLogNode;
import com.example.modules.walnut.model.WalnutLog;
import com.example.modules.walnut.service.WalnutHisLogService;
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
public class WalnutHisLogServiceImpl extends ServiceImpl<WalnutHisLogMapper, WalnutHisLog> implements WalnutHisLogService {

    @Override
    public List<WalnutHisLogNode> getHisLogList(String bno, String hisId) {
        Map<String,Object> result = new HashMap<>();
        QueryWrapper<WalnutHisLog> wrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(bno)) {
            wrapper.eq("no",bno);
        }
        if (!StringUtils.isEmpty(hisId)) {
            wrapper.eq("hisId",hisId);
        }
        List<WalnutHisLog> walnutHisLogs=list(wrapper);
        List<WalnutHisLogNode> lists=new ArrayList<>();
        for (int i = 0; i <walnutHisLogs.size(); i++) {
            WalnutHisLogNode wals=new WalnutHisLogNode();
            WalnutHisLog walnutHisLog = walnutHisLogs.get(i);
            wals.setTime(DateUtils.getDate(walnutHisLog.getCreateTime()));
            wals.setId(walnutHisLog.getId());
            wals.setNo(walnutHisLog.getNo());
            wals.setHospitalNo(walnutHisLog.getHospitalNo());
            wals.setMethod(walnutHisLog.getMethod());
            wals.setHisId(walnutHisLog.getHisId());
            wals.setReqParam(walnutHisLog.getReqParam());
            wals.setRespParam(walnutHisLog.getRespParam());
            wals.setState(walnutHisLog.getState());
            lists.add(wals);
        }

        return lists;
    }
}
