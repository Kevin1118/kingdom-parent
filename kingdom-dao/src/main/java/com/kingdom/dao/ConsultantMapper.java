package com.kingdom.dao;

import com.kingdom.pojo.Consultant;

/**
 *
 * 投资顾问
 *
 * @author : long
 * @date : 2020/6/18 17:30
 */
public interface ConsultantMapper {

    /**
     * 投顾人注册，将信息添加进数据库
     * @param consultant 投顾人对象
     * @return int 操作行数
     */
    int insertConsultant(Consultant consultant);

    /**
     * 通过手机号查询投顾人信息，可用于登录
     * @param phoneNumber 电话号码
     * @return 投顾人对象信息
     */
    Consultant selectByPhoneNumber(String phoneNumber);




}
