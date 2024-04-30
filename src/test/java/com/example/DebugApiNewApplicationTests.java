package com.example;

import com.example.modules.admin.dto.UmsMenuNode;
import com.example.modules.admin.service.UmsRoleService;
import com.example.modules.walnut.service.DemoService;
import com.example.modules.wnhis.pojo.HisYy01;
import com.example.modules.wnhis.service.HisYy01Service;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class DebugApiNewApplicationTests {

    @Autowired
    HisYy01Service hisYy01Service;

    @Autowired
    UmsRoleService umsRoleService;

    @Autowired
    DemoService demoService;

    @Test
    void contextLoads() {
        List<HisYy01> hisYy01s = hisYy01Service.queryNameByCode("111");
        hisYy01s.forEach(System.out::println);
    }
    @Test
    public void demo(){
        List<UmsMenuNode> menuList = umsRoleService.getMenuList(1L);

    }



}
