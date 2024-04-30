package com.example.modules.admin.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.example.modules.admin.model.UmsResourceCategory;


import java.util.List;

/**
 * 后台资源分类管理Service
 * @author Toomth
 * @date 2021/2/3 14:01
 * @explain
 */
public interface UmsResourceCategoryService extends IService<UmsResourceCategory> {

    /**
     * 获取所有资源分类
     */
    List<UmsResourceCategory> listAll();

    /**
     * 创建资源分类
     */
    boolean create(UmsResourceCategory umsResourceCategory);
}
