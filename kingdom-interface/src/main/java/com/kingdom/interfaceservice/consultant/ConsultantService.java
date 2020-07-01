package com.kingdom.interfaceservice.consultant;


import com.kingdom.pojo.Consultant;
import com.kingdom.pojo.LoginTicket;
import com.kingdom.result.ResultCode;

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
     * @return 响应码
     */
    ResultCode register(Consultant consultant);


    /**
     * 投顾人登录功能
     * @param email 邮箱号
     * @param password 密码
     * @return 投顾人信息
     */
    Map<String,Object> login(String email,String password);

    /**
     * 更新头像
     * @param consultantId 投顾人id
     * @param avatarUrl 头像链接
     * @return 响应码
     */
    ResultCode updateAvatar(int consultantId,String avatarUrl);


    /**
     * 根据投顾id查询投顾信息
     * @param consultantId 投顾id
     * @return 投顾对象
     */
    Consultant findConsultantById(int consultantId);

    /**
     * 查询登录凭证
     * @param loginTicket 登录凭证值
     * @return 登录凭证对象
     */
    LoginTicket findLoginTicket(String loginTicket);

    /**
     * 实名认证，更新姓名和身份证
     * @param consultantId 投顾人id
     * @param name 姓名
     * @param idNumber 身份证号
     * @return 响应码
     */
    ResultCode updateNameAndId(int consultantId,String name,String idNumber);

    /**
     * 更新支付密码
     * @param consultantId 投顾人对象
     * @param oldPayPassword 旧支付密码
     * @param newPayPassword 新支付密码
     * @return 操作行数
     */
    int updatePayPassword(int consultantId,String oldPayPassword,String newPayPassword);

    /**
     * 设置支付密码
     * @param consultantId 投顾人id
     * @param payPassword 支付密码
     * @return 操作行数
     */
    int setPayPassword(int consultantId,String payPassword);
}