package com.example.config;


import com.example.common.config.BaseSwaggerConfig;
import com.example.common.domain.SwaggerProperties;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger API文档相关配置
 * @author Toomth
 * @date 2021/2/3 14:01
 * @explain
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig extends BaseSwaggerConfig {

    @Override
    public SwaggerProperties swaggerProperties() {
        return SwaggerProperties.builder()
                .apiBasePackage("com.example.modules")
                .title("admin-demo ")
                .description("admin-demo项目骨架相关接口文档")
                .contactName("Toomth")
                .version("Bate 1.0")
                .enableSecurity(true)
                .build();
    }
}
