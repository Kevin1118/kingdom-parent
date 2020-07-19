package com.kingdom.interfaceservice.user;

import com.kingdom.dto.user.OrderDetailByUserDTO;
import com.kingdom.dto.user.OrderDetailDTO;
import com.kingdom.dto.user.OrderDetailValueNowAllDTO;
import com.kingdom.dto.user.ReturnDetailDTO;
import com.kingdom.pojo.*;
import com.kingdom.result.ResultCode;
import com.kingdom.vojo.user.CardNumber;

import java.util.List;
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
    int bindCardUser(Card card,int userId);

    /**
     * 查询银行卡接口
     * 使用userId，获取该Id绑定的卡号
     * @param userId
     * @return List<Card> 包含所有产品信息的 list
     */
     List<Card> loadCardUser(int userId);

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
    ResultCode setPayPasswordUser(int userId,String payPassword);

    /**
     * 校验支付密码
     * @param user
     * @param payPassword
     * @return
     */
    ResultCode checkPayPasswordUser(User user,String payPassword);

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
    ResultCode investUser(Order order,int userId,String name,double sum);

    /**
     * 投资人卖出
     * @param order
     * @param userId
     * @param name
     * @param percent
     * @return
     */
    int sellUser(Order order,int userId,String name,String percent);

    /**
     * 投资人转出金额
     * @param userId
     * @param withdrawMoney  需要转出的金额
     * @return 操作行数
     */

    ResultCode withdrawUser(int userId, double withdrawMoney);
    /**
     * 投资人查询收益详情
     * @param userId
     * @return List<ReturnDetailDTO> 收益详情列表
     * @author HuangJingchao
     * @date 2020/7/17 20:04
     **/
    List<ReturnDetailDTO> searchUserReturnDetail(Integer userId);

    /**
     * 退出登录
     * @return
     */
    void exit(int userId);

    /**
     * 投资人查询交易明细
     * @param userId
     * @return List<OrderDetailDTO>
     */
    List<OrderDetailDTO> searchUserOrderDetail(Integer userId);

    /**
     * 投资人资产概览，总持仓情况
     * @param userId
     * @return DTO
     */
    OrderDetailValueNowAllDTO searchValueNowAll(Integer userId);

    /**
     * 投资人查询交易明细
     * @param userId
     * @return List<OrderDetailDTO>
     */
    List<OrderDetailByUserDTO> searchOrderDetailByUser01(Integer userId);
}
