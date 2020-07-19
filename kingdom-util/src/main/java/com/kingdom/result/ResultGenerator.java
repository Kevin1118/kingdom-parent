package com.kingdom.result;

import org.omg.PortableInterceptor.USER_EXCEPTION;

/**
 * 响应结果生成工具
 * @author hjc
 */
public class ResultGenerator {
    private static final String DEFAULT_SUCCESS_MESSAGE = "SUCCESS";
    private static final String DEFAULT_FAIL_MESSAGE = "FAIL";

    public static Result genSuccessResult() {
        return new Result()
                .setCode(ResultCode.SUCCESS)
                .setMessage(DEFAULT_SUCCESS_MESSAGE);
    }

    public static Result genSuccessResult(Object data) {
        return new Result()
                .setCode(ResultCode.SUCCESS)
                .setMessage(DEFAULT_SUCCESS_MESSAGE)
                .setData(data);
    }

    public static Result genFailResult(String message) {
        return new Result()
                .setCode(ResultCode.FAIL)
                .setMessage(message);
    }

    public static Result genFailResult(ResultCode code,String message) {
        return new Result()
                .setCode(code)
                .setMessage(message);
    }

    public static Result genFailResult(ResultCode code) {
        return new Result()
                .setCode(code)
                .setMessage(DEFAULT_FAIL_MESSAGE);
    }

}