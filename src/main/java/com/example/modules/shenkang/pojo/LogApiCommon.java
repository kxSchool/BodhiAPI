package com.example.modules.shenkang.pojo;

import lombok.Data;

import java.util.List;


@Data
public class LogApiCommon<T>{
    private Integer totalNum;
    private List<T> list;
}
