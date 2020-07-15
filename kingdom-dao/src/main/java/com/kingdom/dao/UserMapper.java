package com.kingdom.dao;

import com.kingdom.pojo.*;
import org.apache.ibatis.annotations.Param;


/**
 * 测试 demo，接口文件，访问数据库
 * 接口名与xml文件中的 sql id标签对应
 *
 * @author HuangJingChao
 * @date : 2020-06-16 12:08
 */
public interface UserMapper {
    /**
     * 标注功能、简单说明业务逻辑
     * 如有必要，注释接口实现和调用的注意事项
     *
     * @param userid
     * @return User
     */
    User selectUserById(Integer userid);

    /**
     * 投资人注册功能，向数据库写进用户名，邮箱,手机号和密码
     * @param user 投资人对象，写进数据库
     * @return int 操作行数
     */
    int addUser(User user);

    /**
     * 新建独立账户
     * @param independentAccount 独立账户对象
     * @return
     */
    int addIndependentAccount(IndependentAccount independentAccount);

    /**
     * 查询账户
     * @param userid
     * @return
     */
    IndependentAccount selectIndependetAccountById(Integer userid);
    /**
     * 独立账户充值
     * @param userid
     * @param independentbalance
     * @return
     */
    int updateIndependentBalance(Integer userid,double independentbalance);

    /**
     * 根据电话号码查询投资人用户
     * @param phonenumber
     * @return User 查询对象
     */
    User selectUserByPhoneNumber(String phonenumber);

    /**
     * 投资人更换头像
     * @param userid
     * @param avatar
     * @return
     */
    int updateAvatar(Integer userid,String avatar);

    /**
     * 投资人绑卡功能，将姓名和卡号写进数据库
     * @param card
     * @return int 操作行数
     */
    int addCard(Card card);

    /**
     * 投资人登录功能，从数据库中查询密码
     * @param email
     * @param phoneNumber
     * @return User
     */
    User selectPasswordByEmailOrPhoneNumber(@Param("email")String email,@Param("phonenumber") String phoneNumber);

    /**
     * 投资人更新支付密码
     * @param userid
     * @param paypassword
     * @param paypasswordsalt
     * @return
     */
    int updatePayPassword(Integer userid,String paypassword,String paypasswordsalt);

    /**
     * 密码管理
     * @param userid
     * @param password
     * @param salt
     * @return int 操作行数
     */
    int updatePassword(Integer userid,String password,String salt);

    /**
     * 实名认证
     * @param userid
     * @param name
     * @param idnumber
     * @param approvalstatus
     * @param approvaltime
     * @return int 操作行数
     */
    int updateName(Integer userid,String name,String idnumber,String approvalstatus,Integer approvaltime);

    /**
     * 投资人修改手机号
     * @param userid
     * @param phonenumber
     * @return
     */
    int updatePhoneNumber(Integer userid,String phonenumber);

    /**
     * 修改用户名
     * @param userid
     * @param username
     * @return
     */
    int updateUserName(Integer userid,String username);

    /**
     * 添加交易记录
     * @param order
     * @return
     */
    int addOrder(Order order);

    /**
     * 查询产品
     * @param name
     * @return
     */
    Product selectProductByName(String name);

    /**
     * 创建投顾账户
     * @param signAccount
     * @return
     */
    int addSignAccount(SignAccount signAccount);

    /**
     * 根据用户id和产品id查询投顾账户账号
     * @param userid
     * @param productid
     * @return
     */
    SignAccount selectAccountNoByUserIdAndProductId(@Param("userId") Integer userid,@Param("productId") Integer productid);

    /**
     * 根据投顾账号更新账户金额
     * @param signaccountid
     * @param balance
     * @return
     */
    int updateSignAccountBalance(Integer signaccountid,double balance);

}
