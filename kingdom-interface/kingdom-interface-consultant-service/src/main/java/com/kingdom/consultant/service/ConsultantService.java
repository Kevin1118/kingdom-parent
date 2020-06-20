package com.kingdom.consultant.service;


import com.kingdom.pojo.Consultant;

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
}
