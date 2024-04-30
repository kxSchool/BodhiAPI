package com.example.modules.walnut.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.modules.walnut.model.WalnutLog;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Toomth
 * @since 2021-03-25
 */
@Service
public interface WalnutLogService extends IService<WalnutLog> {

    Map<String, Object> getLogList(Integer type, String method, String hisId, String startTime, String endTime, Integer pageSize, Integer paN0);
}
