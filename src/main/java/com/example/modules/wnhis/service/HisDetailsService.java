package com.example.modules.wnhis.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.modules.wnhis.pojo.HisDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Toomth
 * @since 2021-03-18
 */
@Service
public interface HisDetailsService extends IService<HisDetails> {

    //根据业务No 查询 具体请求信息
    List<HisDetails> queryByNo(String no);

    //根据方法 查询 具体请求信息
    Map<String,Object> queryByMethod(Integer type, String method, String hisId, String startTime, String endTime, Integer pageSize, Integer pageNum);

    String parsingXml(Integer id);
}

