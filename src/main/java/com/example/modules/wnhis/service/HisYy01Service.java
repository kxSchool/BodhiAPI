package com.example.modules.wnhis.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.example.modules.wnhis.pojo.HisYy01;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Toomth
 * @since 2021-03-17
 */
public interface HisYy01Service extends IService<HisYy01> {

    /**
     * 删除缓存表
     */
    Boolean delete();
    /**
     * 查询是否存在  code
     */
    List<HisYy01> queryNameByCode(String code);

}
