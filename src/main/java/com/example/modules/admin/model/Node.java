package com.example.modules.admin.model;

import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Toomth
 * @date 2021/3/4 17:06
 * @explain
 */
@Data
public class Node {
    /**
     * 用于角色管理--编辑--回显(默认选中)
     */
    private static Map<String, Object> stateMap = new HashMap<String, Object>();

    static {
        stateMap.put("checked", true);
    }
    private  Long id;
    private  String text;
    private List<Node> nodes;
    private Map<String,Object> state;
    public void setState() {
        this.state = stateMap;
    }
}
