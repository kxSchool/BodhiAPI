package com.example.modules.admin.service;





import com.example.modules.admin.model.UmsAdmin;
import com.example.modules.admin.model.UmsResource;

import java.util.List;

/**
 * 后台用户缓存管理Service
 * @author Toomth
 * @date 2021/2/3 14:01
 * @explain
 */
public interface UmsAdminCacheService {
    /**
     * 删除后台用户缓存
     */
    void delAdmin(Long adminId);

    /**
     * 删除后台用户资源列表缓存
     */
    void delResourceList(Long adminId);

    /**
     * 当角色相关资源信息改变时删除相关后台用户缓存
     */
    void delResourceListByRole(Long roleId);

    /**
     * 当角色相关资源信息改变时删除相关后台用户缓存
     */
    void delResourceListByRoleIds(List<Long> roleIds);

    /**
     * 当资源信息改变时，删除资源项目后台用户缓存
     */
    void delResourceListByResource(Long resourceId);

    /**
     * 获取缓存后台用户信息
     */
    UmsAdmin getAdmin(String username);

    /**
     * 设置缓存后台用户信息
     */
    void setAdmin(UmsAdmin admin);

    /**
     * 获取缓存后台用户资源列表
     */
    List<UmsResource> getResourceList(Long adminId);

    /**
     * 设置后台后台用户资源列表
     */
    void setResourceList(Long adminId, List<UmsResource> resourceList);
    /**
     * 设置 并发数 计数器
     */
    void setConcurrencyByUserName(String userName,Integer concurrency);
    /**
     * 获取 并发数 计数器
     */
    Integer getConcurrencyByUserName(String userName);

    /**
     * 判断 并发数 计数器
     */
    Boolean isExistConcurrencyByUserName(String userName);

    /**
     * 删除 并发数 计数器
     */
    void delConcurrencyByUserName(String userName);


}
