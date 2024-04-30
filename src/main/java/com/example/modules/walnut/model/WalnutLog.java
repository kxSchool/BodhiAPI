package com.example.modules.walnut.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
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
@TableName("walnut_log")
@ApiModel(value="WalnutLog对象", description="")
public class WalnutLog implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String no;

    private String method;

    @TableField("hisId")
    private String hisId;

    @TableField("reqParam")
    private String reqParam;

    @TableField("respParam")
    private String respParam;

    private Long createtime;


}
