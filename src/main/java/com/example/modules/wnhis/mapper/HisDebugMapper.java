package com.example.modules.wnhis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.modules.admin.model.UmsResource;
import com.example.modules.wnhis.pojo.HisExtractionlog;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Toomth
 * @date 2021/3/16 14:30
 * @explain
 */
@Mapper
public interface HisDebugMapper extends BaseMapper<HisExtractionlog> {
}
