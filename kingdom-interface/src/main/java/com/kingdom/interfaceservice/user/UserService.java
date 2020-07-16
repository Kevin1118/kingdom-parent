package com.kingdom.interfaceservice.user;

import com.kingdom.pojo.*;

import java.util.Map;

/**
 * <h3>kingdom-parent</h3>
 * <p>UserDemo的测试接口</p>
 *
 * @author : HuangJingChao
 * @date : 2020-06-16 23:02
 **/
public interface UserService {

    /**
     * 根据 userid 查询 user 表详情的接口 demo
     *
     * @param userid
     * @return User
     */
    User selectUserById(Integer userid);

    /**
     * 投资人注册个人信息
     * @param user
     * @return int
     */
    int registerUser(User user, IndependentAccount independentAccount);

    /**
     * 投资人绑定银行卡
     * @param card
     * @return int
     */
    int bindCardUser(Card card);

    /**
     * 投资人登录
     * @param phoneNumber 手机号
     * @param password 密码
     * @return 投资人信息
     */
    Map<String,Object> loginUser(String phoneNumber, String password);

    /**
     * 更新头像
     * @param userId 投资人id
     * @param avatarUrl 头像链接
     * @return 操作行数
     */
    int updateAvatarUser(int userId,String avatarUrl);
    /**
     * 查询登录凭证
     * @param loginTicket 登录凭证值
     * @return 登录凭证对象
     */
    LoginTicket findLoginTicket(String loginTicket);

    /**
     * 设置支付密码，并存储
     * @param userId
     * @param payPassword
     * @return
     */
    int setPayPasswordUser(int userId,String payPassword);

    /**
     * 投资人充值，充值到自主账户
     * @param userid
     * @param topUpMoney 充值金额
     * @return
     */
    int topUpUser(Integer userid,double topUpMoney);
    /**
     * 密码管理，修改密码
     * @param user
     * @param oldPassword
     * @param newPassword
     * @return
     */
    int passwordManageUser(User user,String oldPassword,String newPassword);
    /**
     * 投资人实名认证
     * @param user
     * @param name
     * @param idNumber
     * @return
     */
    int certificationUser(User user,String name,String idNumber);

    /**
     * 投资人修改手机号
     * @param userId
     * @param phoneNumber
     * @return int
     */
    int changePhoneNumberUser(int userId,String phoneNumber);

    /**
     * 投资人修改用户名
     * @param userId
     * @param userName
     * @return
     */
    int changeUserName(int userId,String userName);

    /**
     * 查询独立账户余额
     * @param userId
     * @return
     */
    IndependentAccount selectAccountByUserId(int userId);
    /**
     * 投资人买入
     * @param order
     * @param name
     * @param userId
     * @param sum
     * @return
     */
    int investUser(Order order,int userId,String name,double sum);

    /**
     * 投资人卖出
     * @param order
     * @param userId
     * @param name
     * @param sum
     * @return
     */
    int sellUser(Order order,int userId,String name,double sum);
}
