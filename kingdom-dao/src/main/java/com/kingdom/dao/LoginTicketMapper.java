package com.kingdom.dao;

import com.kingdom.pojo.LoginTicket;

/**
 * 登录凭证
 * @author : long
 * @date : 2020/6/24 10:38
 */
public interface LoginTicketMapper {

    /**
     * 插入登录凭证
     * @param loginTicket 登录凭证对象
     * @return 操作行数
     */
    int insertLoginTicket(LoginTicket loginTicket);

    /**
     * 通过登录凭证查询详细信息
     * @param ticket 凭证字符串
     * @return 登录凭证对象
     */
    LoginTicket selectByTicket(String ticket);

    /**
     * 更新状态
     * @param ticket 凭证字符串
     * @param status 状态
     * @return 操作行数
     */
    int updateStatus(String ticket,int status);
}
