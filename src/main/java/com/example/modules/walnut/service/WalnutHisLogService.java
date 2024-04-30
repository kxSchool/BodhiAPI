package com.example.modules.walnut.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.modules.walnut.model.WalnutHisLog;
import com.example.modules.walnut.model.WalnutHisLogNode;
import org.springframework.stereotype.Service;

import java.util.List;
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
public interface WalnutHisLogService extends IService<WalnutHisLog> {

    List<WalnutHisLogNode> getHisLogList(String bno, String hisId);
}
