package com.example.modules.walnut.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * @since 2021-03-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("walnut_his_log")
@ApiModel(value="WalnutHisLog对象", description="")
public class WalnutHisLog implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "业务编号")
    private String no;

    @ApiModelProperty(value = "医院代码")
    @TableField("hospitalNo")
    private String hospitalNo;

    @ApiModelProperty(value = "方法")
    private String method;

    @TableField("hisId")
    private String hisId;

    @ApiModelProperty(value = "请求参数")
    @TableField("reqParam")
    private String reqParam;

    @ApiModelProperty(value = "响应参数")
    @TableField("respParam")
    private String respParam;

    @ApiModelProperty(value = "状态标识（'0:正常  1:系统异常  2:请求接口异常  3:系统忽略异常 4:接口忽略异常）")
    private String state;

    @ApiModelProperty(value = "创建时间")
    private Long createTime;


}
