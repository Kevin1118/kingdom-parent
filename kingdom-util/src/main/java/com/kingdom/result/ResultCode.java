package com.kingdom.result;

/**
 * 响应码枚举，参考HTTP状态码的语义
 */
public enum ResultCode {
    //成功
    SUCCESS(200),
    //失败
    FAIL(400),
    //未认证（签名错误）
    UNAUTHORIZED(401),
    //接口不存在
    NOT_FOUND(404),
    //服务器内部错误
    INTERNAL_SERVER_ERROR(500),
    //注册邮箱错误,邮箱已注册
    REGISTER_EMAIL_ERROR(6001),
    //登录错误，账号不存在或者账号已停用
    LOGIN_EMAIL_ERROR(6002),
    //账号被停用
    LOGIN_STATUS_ERROR(6003),
    //登录错误，密码错误
    LOGIN_PWD_ERROR(6004),
    //未登录，或登录过期
    NOT_LOGGED_IN(6005),
    //空值
    EMPTY_ARG(6006),
    //文件格式错误
    FILE_SUFFIX_ERROR(6007),
    //数据库操作错误
    MYSQL_CURD_ERROR(6008),
    //更改密码失败，密码错误
    UPDATE_PWD_ERROR(6009),
    ;


    private final int code;

    ResultCode(int code) {
        this.code = code;
    }

    public int code() {
        return code;
    }
}
