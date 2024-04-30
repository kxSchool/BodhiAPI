package com.example.modules.wnhis.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.modules.wnhis.pojo.HisDetails;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Toomth
 * @since 2021-03-18
 */
@Mapper
public interface HisDetailsMapper extends BaseMapper<HisDetails> {

    @Select("select respParam from his_details where id=#{id}")
    String selectRespParamById(@Param("id") Integer id);
}
