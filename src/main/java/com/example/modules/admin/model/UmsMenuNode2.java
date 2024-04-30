package com.example.modules.admin.model;

import com.example.modules.admin.dto.UmsMenuNode;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author Toomth
 * @date 2021/3/5 13:36
 * @explain
 */
@Getter
@Setter
public class UmsMenuNode2 extends UmsMenu2 {
    @ApiModelProperty(value = "子级菜单")
    private List<UmsMenuNode2> children;
    @ApiModelProperty(value = "子级菜单")
    private Boolean state;
}
