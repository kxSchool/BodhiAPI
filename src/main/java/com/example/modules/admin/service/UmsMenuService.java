package com.example.modules.admin.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.modules.admin.dto.UmsMenuNode;
import com.example.modules.admin.model.Node;
import com.example.modules.admin.model.UmsMenu;


import java.util.List;

/**
 * 后台菜单管理Service
 * @author Toomth
 * @date 2021/2/3 14:01
 * @explain
 */
public interface UmsMenuService extends IService<UmsMenu> {
    /**
     * 创建后台菜单
     */
    boolean create(UmsMenu umsMenu);

    /**
     * 修改后台菜单
     */
    boolean update(Long id, UmsMenu umsMenu);

    /**
     * 分页查询后台菜单
     */
    Page<UmsMenu> list(Long parentId, Integer pageSize, Integer pageNum);

    /**
     * 返回所有菜单列表
     */
    List<UmsMenu> treeList();
    /**
     * 树形结构返回所有菜单列表
     */
    List<Node> treeList2(Long roleId);

    /**
     * 修改菜单显示状态
     */
    boolean updateHidden(Long id, Integer hidden);
}
