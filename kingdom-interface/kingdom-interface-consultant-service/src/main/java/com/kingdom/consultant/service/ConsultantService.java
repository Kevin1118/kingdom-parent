package com.kingdom.consultant.service;


import com.kingdom.pojo.Consultant;

import java.util.Map;

/**
 *
 * 投顾人业务接口
 * @author : long
 * @date : 2020/6/20 12:52
 */
public interface ConsultantService {


    /**
     * 投顾人注册功能
     * @param consultant 投顾人信息
     * @return 操作行数
     */
    int register(Consultant consultant);


    /**
     * 投顾人登录功能
     * @param phoneNumber 手机号
     * @param password 密码
     * @return 投顾人信息
     */
    Map<String,Object> login(String phoneNumber,String password);

}
