package com.example.modules.wnhis.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author Toomth
 * @since 2021-03-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("his_details")
@ApiModel(value="HisDetails对象", description="")
public class HisDetails implements Serializable {

    private static final long serialVersionUID=1L;

    private Integer id;

    @ApiModelProperty(value = "业务编号")
    private String no;

    @ApiModelProperty(value = "医院代码")
    @TableField("hospitalNo")
    private String hospitalNo;

    @ApiModelProperty(value = "方法")
    private String method;

    private String param;

    @ApiModelProperty(value = "请求参数")
    @TableField("reqParam")
    private String reqParam;

    @ApiModelProperty(value = "响应参数")
    @TableField("respParam")
    private String respParam;

    @ApiModelProperty(value = "状态标识（'0:正常  1:系统异常  2:请求接口异常  3:系统忽略异常 4:接口忽略异常）")
    private String state;

    @ApiModelProperty(value = "状态标识（'0:正常  1:系统异常  2:请求接口异常  3:系统忽略异常 4:接口忽略异常）")
    private Long createTime;

}
