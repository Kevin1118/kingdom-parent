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

    /**
     * 通过邮箱查询投顾人信息，可用于登录
     * @param email 邮箱号
     * @return 投顾人对象
     */
    Consultant selectByEmail(String email);

    /**
     * 更新头像
     * @param consultantId 投顾人id
     * @param avatarUrl 头像链接
     * @return 操作行数
     */
    int updateAvatar(int consultantId,String avatarUrl);


    /**
     * 通过id查询投顾人信息
     * @param consultantId 投顾人id
     * @return 投顾人信息
     */
    Consultant selectById(int consultantId);

    /**
     * 实名认证，更新姓名和身份证号
     * @param name 姓名
     * @param idNumber 身份证号
     * @param consultantId 投顾人id
     * @return 操作行数
     */
    int updateNameAndId(int consultantId,String name,String idNumber);


    /**
     * 修改支付密码
     * @param consultantId 投顾人id
     * @param payPassword 支付密码
     * @param payPasswordSalt 支付密码salt
     * @return 修改行数
     */
    int updatePayPassword(int consultantId,String payPassword,String payPasswordSalt);

}
