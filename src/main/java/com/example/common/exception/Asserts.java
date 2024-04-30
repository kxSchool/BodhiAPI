package com.example.common.exception;


import com.example.common.api.IErrorCode;

/**
 * 断言处理类，用于抛出各种API异常
 * @author Toomth
 * @date 2021/2/3 14:01
 * @explain
 */
public class Asserts {
    public static void fail(String message) {
        throw new ApiException(message);
    }

    public static void fail(IErrorCode errorCode) {
        throw new ApiException(errorCode);
    }
}
