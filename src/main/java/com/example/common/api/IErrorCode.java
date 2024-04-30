package com.example.common.api;

/**
 * 封装API的错误码
 * @author Toomth
 * @date 2021/2/3 14:01
 * @explain
 */

public interface IErrorCode {
    long getCode();

    String getMessage();
}
