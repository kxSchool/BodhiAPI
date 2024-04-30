package com.example.modules.wnhis.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.modules.wnhis.pojo.HisExtractionlog;

/**
 * @author Toomth
 * @date 2021/3/16 14:57
 * @explain
 */
public interface HISServiceForDebug extends IService<HisExtractionlog> {
    /**
     * 添加资源
     */
    boolean create(HisExtractionlog hisExtractionlog);

    /**
     * 修改资源
     */
    boolean update(String id, HisExtractionlog hisExtractionlog);

    /**
     * 删除资源
     */
    boolean delete(String id);


     Page<HisExtractionlog> list(String hisId, String startTime, String endTime, Integer pageSize, Integer pageNum);
}
