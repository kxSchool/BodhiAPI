package com.example.modules.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.example.modules.admin.model.UmsMenu;
import com.example.modules.admin.model.UmsMenu2;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 后台菜单表 Mapper 接口
 * </p>
 *
 * @author macro
 * @since 2020-08-21
 */
@Mapper
public interface UmsMenuMapper extends BaseMapper<UmsMenu> {

    /**
     * 根据后台用户ID获取菜单
     */
    List<UmsMenu> getMenuList(@Param("adminId") Long adminId);
    /**
     * 根据角色ID获取菜单
     */
    List<UmsMenu> getMenuListByRoleId(@Param("roleId") Long roleId);

    List<UmsMenu2> getList(@Param("roleId") Long roleId);
    /**
    查询 roleId 对应menuId
    */
    List<Long> queryMenuIdsByRoleId(@Param("roleId") Long roleId);
}
