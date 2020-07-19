package com.kingdom.dao;

import com.kingdom.pojo.Consultant;
import com.kingdom.pojo.ConsultantRecord;
import com.kingdom.pojo.SignAccount;

import java.util.List;

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

    /**
     * 插入操作记录
     * @param record 记录
     * @return 操作行数
     */
    int insertRecord(ConsultantRecord record);

    /**
     * 查询交易记录
     * @param orderId 订单编号
     * @return 交易记录
     */
    List<ConsultantRecord> loadRecord(String orderId);


    /**
     * 通过产品id查找所有签约账号
     * @param productId
     * @return
     */
    List<SignAccount> selectSignAccountByProductId(int productId);


    /**
     * 查询不同状态的订单
     * @param consultantId 投顾id
     * @param status 状态
     * @return 数量
     */
    int selectCountsByStatus(int consultantId,int status);

    int selectRecordCounts(int consultantId,int status);
}
