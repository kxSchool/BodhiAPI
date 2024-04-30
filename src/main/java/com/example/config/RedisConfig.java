package com.example.config;


import com.example.common.config.BaseRedisConfig;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

/**
 * Redis配置类
 * @author Toomth
 * @date 2021/2/3 14:01
 * @explain
 */
@EnableCaching
@Configuration
public class RedisConfig extends BaseRedisConfig {

}
