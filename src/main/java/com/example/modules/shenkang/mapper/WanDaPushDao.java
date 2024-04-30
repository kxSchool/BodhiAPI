package com.example.modules.shenkang.mapper;

import com.example.modules.shenkang.pojo.Result;
import com.example.modules.shenkang.pojo.WanDaAPIReq_2;
import com.example.modules.shenkang.pojo.WandaApiReq;
import com.example.modules.shenkang.service.impl.WanDaPushServiceImpl;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

@Mapper
public interface WanDaPushDao {

    /**
     * @author: 邵梦丽 on 2020/9/10 16:56
     * @param:
     * @return:
     * @Description:更新向万达的请求信息
     */
    Integer updateWanDaAPIDetails(WanDaAPIReq_2 wanDaAPIReq);

    Integer insertIntoWandaApiReqIDetails(WandaApiReq wanDaAPIReq);
    /**
     * @author: 邵梦丽 on 2020/9/10 16:56
     * @param:
     * @return:
     * @Description:向数据库中插入推送万达的请求信息
     */
    Integer insertIntoWanDaAPIDetails(WanDaAPIReq_2 wanDaAPIReq);
    /**
     * @author: 邵梦丽 on 2020/9/1 9:40
     * @param:
     * @return:
     * @Description:提供万达反向校验
     */
}
