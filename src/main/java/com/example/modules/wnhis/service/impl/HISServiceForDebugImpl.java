package com.example.modules.wnhis.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.modules.wnhis.mapper.HisDebugMapper;
import com.example.modules.wnhis.pojo.HisExtractionlog;
import com.example.modules.wnhis.service.HISServiceForDebug;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Toomth
 * @date 2021/3/16 11:15
 * @explain
 */
@Service
public class HISServiceForDebugImpl extends ServiceImpl<HisDebugMapper,HisExtractionlog> implements HISServiceForDebug {

    @Autowired
    private HisDebugMapper hisDebugMapper;

    @Override
    public boolean create(HisExtractionlog hisExtractionlog) {
        return false;
    }

    @Override
    public boolean update(String id, HisExtractionlog hisExtractionlog) {
        return false;
    }

    @Override
    public boolean delete(String id) {
        return false;
    }

    /**
     * his调用日志
     * @param hisId hisID
     * @param startTime 开始时间
     * @param endTime  终止时间
     * @param pageSize
     * @param pageNum
     * @return
     */
    @Override
    public Page<HisExtractionlog> list(String hisId, String startTime, String endTime, Integer pageSize, Integer pageNum) {
        Page<HisExtractionlog> page=new Page<>(pageNum,pageSize);
        QueryWrapper<HisExtractionlog> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("createTime");
        if(!StringUtils.isEmpty(hisId)){
            wrapper.likeRight("hisId",hisId);
        }
        return page(page,wrapper);
    }
}
