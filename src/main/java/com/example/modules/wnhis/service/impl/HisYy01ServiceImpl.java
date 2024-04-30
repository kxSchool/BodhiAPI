package com.example.modules.wnhis.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.modules.wnhis.mapper.HisYy01Mapper;
import com.example.modules.wnhis.pojo.HisYy01;
import com.example.modules.wnhis.service.HisYy01Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Toomth
 * @since 2021-03-17
 */
@Service
public class HisYy01ServiceImpl extends ServiceImpl<HisYy01Mapper, HisYy01> implements HisYy01Service {

    @Autowired
    HisYy01Mapper hisYy01Mapper;

    @Override
    public Boolean delete() {

        return remove(new QueryWrapper<>());
    }

    @Override
    public List<HisYy01> queryNameByCode(String code) {
        QueryWrapper<HisYy01> query = new QueryWrapper<>();
        query.select("name").eq("code",code);
        List<HisYy01> hisYy01s = hisYy01Mapper.selectList(query);
        return hisYy01s;
    }
}
