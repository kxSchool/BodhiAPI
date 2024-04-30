package com.example.modules.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.example.modules.admin.dto.UmsMenuNode;
import com.example.modules.admin.mapper.UmsMenuMapper;
import com.example.modules.admin.model.Node;
import com.example.modules.admin.model.UmsMenu;
import com.example.modules.admin.service.UmsMenuService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 后台菜单管理Service实现类
 * @author Toomth
 * @date 2021/2/3 14:01
 * @explain
 */
@Service
public class UmsMenuServiceImpl extends ServiceImpl<UmsMenuMapper, UmsMenu>implements UmsMenuService {


    @Autowired
    UmsMenuMapper menuMapper;
    @Override
    public boolean create(UmsMenu umsMenu) {
        umsMenu.setCreateTime(new Date());
//        updateLevel(umsMenu);
        return save(umsMenu);
    }

    /**
     * 修改菜单层级
     */
    private void updateLevel(UmsMenu umsMenu) {
        if (umsMenu.getParentId() == 0) {
            //没有父菜单时为一级菜单
            umsMenu.setLevel(0);
        } else {
            //有父菜单时选择根据父菜单level设置
            UmsMenu parentMenu = getById(umsMenu.getParentId());
            if (parentMenu != null) {
                umsMenu.setLevel(parentMenu.getLevel() + 1);
            } else {
                umsMenu.setLevel(0);
            }
        }
    }

    @Override
    public boolean update(Long id, UmsMenu umsMenu) {
        umsMenu.setId(id);
        updateLevel(umsMenu);
        return updateById(umsMenu);
    }

    @Override
    public Page<UmsMenu> list(Long parentId, Integer pageSize, Integer pageNum) {
        Page<UmsMenu> page = new Page<>(pageNum,pageSize);
        QueryWrapper<UmsMenu> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(UmsMenu::getParentId,parentId)
                .orderByDesc(UmsMenu::getSort);
        return page(page,wrapper);
    }

    @Override
    public List<UmsMenu> treeList() {
        List<UmsMenu> menuList = list();
//        List<UmsMenuNode> result = menuList.stream()
//                .filter(menu -> menu.getParentId().equals(0L))
//                .map(menu -> covertMenuNode(menu, menuList)).collect(Collectors.toList());
        return menuList;
    }


    @Override
    public List<Node> treeList2(Long roleId) {

        List<UmsMenu> menuList =list();
        //查询 roleId 对应menuId
        List<Long> menuIds=menuMapper.queryMenuIdsByRoleId(roleId);
        List<UmsMenuNode> result = menuList.stream()
                .filter(menu -> menu.getParentId().equals(0L))
                .map(menu -> covertMenuNode(menu, menuList)).collect(Collectors.toList());
        List<Node> nodeList=new ArrayList<>();
        for (int i = 0; i < result.size(); i++) {
            UmsMenuNode umsMenu=result.get(i);
            Node node=new Node();
            if (menuIds.contains(umsMenu.getId())){
                node.setState();
            }
            node.setId(umsMenu.getId());
            node.setText(umsMenu.getTitle());
            node.setNodes(change(umsMenu.getChildren(),menuIds));
            nodeList.add(node);
        }
        return nodeList;
    }

    public List<Node> change(List<UmsMenuNode> result,List<Long> menuIds){
        List<Node> nodeList=new ArrayList<>();
        for (int i = 0; i < result.size(); i++) {
            UmsMenuNode umsMenu=result.get(i);
            Node node=new Node();
            if (menuIds.contains(umsMenu.getId())){
                node.setState();
            }
            node.setId(umsMenu.getId());
            node.setText(umsMenu.getTitle());
            if (umsMenu.getChildren().size()>0){
                node.setNodes(change(umsMenu.getChildren(),menuIds));
            }else{
                node.setNodes(new ArrayList<>());
            }
            nodeList.add(node);
        }
        return nodeList;
    }

    @Override
    public boolean updateHidden(Long id, Integer hidden) {
        UmsMenu umsMenu = new UmsMenu();
        umsMenu.setId(id);
        umsMenu.setHidden(hidden);
        return updateById(umsMenu);
    }

    /**
     * 将UmsMenu转化为UmsMenuNode并设置children属性
     */
    private UmsMenuNode covertMenuNode(UmsMenu menu, List<UmsMenu> menuList) {
        UmsMenuNode node = new UmsMenuNode();
        BeanUtils.copyProperties(menu, node);
        List<UmsMenuNode> children = menuList.stream()
                .filter(subMenu -> subMenu.getParentId().equals(menu.getId()))
                .map(subMenu -> covertMenuNode(subMenu, menuList)).collect(Collectors.toList());
        node.setChildren(children);
        return node;
    }

}
