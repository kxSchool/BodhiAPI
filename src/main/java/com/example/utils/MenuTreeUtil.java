package com.example.utils;
import com.google.common.collect.Lists;
import java.util.Date;

import com.example.modules.admin.dto.UmsMenuNode;
import com.example.modules.admin.model.UmsMenu;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Toomth
 * @date 2021/2/22 11:10
 * @explain
 */
public class MenuTreeUtil {

    public static Map<String,Object> mapArray = new LinkedHashMap<String, Object>();
    public List<UmsMenu> menuCommon;
    public List<UmsMenuNode> list = new ArrayList<UmsMenuNode>();

    public List<UmsMenuNode> menuList(List<UmsMenu> menu){
        this.menuCommon = menu;
        for (UmsMenu x : menu) {
            if(x.getParentId()==0){
                UmsMenuNode umsNode=new UmsMenuNode();
                umsNode.setId(x.getId());
                umsNode.setParentId(x.getParentId());
                umsNode.setUrl(x.getUrl());
                umsNode.setCreateTime(x.getCreateTime());
                umsNode.setTitle(x.getTitle());
                umsNode.setLevel(x.getLevel());
                umsNode.setSort(x.getSort());
                umsNode.setName(x.getName());
                umsNode.setIcon(x.getIcon());
                umsNode.setHidden(x.getHidden());
                umsNode.setChildren((List<UmsMenuNode>) menuChild(x.getId()));
                list.add(umsNode);
            }
        }
        return list;
    }

    public List<?> menuChild(Long id){
        List<Object> lists = new ArrayList<Object>();
        for(UmsMenu a:menuCommon){
            if(a.getParentId() == id){
                UmsMenuNode umsNode=new UmsMenuNode();
                umsNode.setId(a.getId());
                umsNode.setParentId(a.getParentId());
                umsNode.setUrl(a.getUrl());
                umsNode.setCreateTime(a.getCreateTime());
                umsNode.setTitle(a.getTitle());
                umsNode.setLevel(a.getLevel());
                umsNode.setSort(a.getSort());
                umsNode.setName(a.getName());
                umsNode.setIcon(a.getIcon());
                umsNode.setHidden(a.getHidden());
                umsNode.setChildren((List<UmsMenuNode>) menuChild(a.getId()));
                lists.add(umsNode);
            }
        }
        return lists;
    }
}
