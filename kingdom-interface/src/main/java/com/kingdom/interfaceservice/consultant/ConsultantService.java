package com.kingdom.interfaceservice.consultant;


import com.kingdom.pojo.Consultant;
import com.kingdom.pojo.LoginTicket;
import com.kingdom.result.Result;
import com.kingdom.result.ResultCode;

import java.util.List;
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
     * @param consultant 投顾人对象
     * @param oldPayPassword 旧支付密码
     * @param newPayPassword 新支付密码
     * @return 响应码
     */
    ResultCode updatePayPassword(Consultant consultant,String oldPayPassword,String newPayPassword);

    /**
     * 设置支付密码
     * @param consultantId 投顾人id
     * @param payPassword 支付密码
     * @return 响应码
     */
    ResultCode setPayPassword(int consultantId,String payPassword);

    /**
     * 查询投顾所属产品
     * @param pageNum 页码
     * @param pageSize 分页大小
     * @param consultantId 投顾id
     * @return 产品列表
     */
    Map selectProduct(int pageNum, int pageSize,int consultantId);

    /**
     * 查询投顾所属订单
     * @param pageNum 页码
     * @param pageSize 分页大小
     * @param consultantId 投顾id
     * @param type 审批或买卖
     * @return 订单列表
     */
    Map selectOrders(int pageNum,int pageSize,int consultantId,int type);


    /**
     * 更改订单状态
     * @param id 订单id
     * @param status 状态
     * @param productId 产品id
     * @param sum 金额
     * @return 响应码
     */
    ResultCode updateOrderStatus(int id,int status,int productId,float sum);

    /**
     * 买入基金股票
     * @param ids 订单
     * @return 响应码
     */
    ResultCode buyStockAndFund(List<Integer> ids);


    /**
     * 查询资产信息
     * @param pageNum 页码
     * @param pageSize 大小
     * @param orderId 订单号
     * @param consultantId 投顾人id
     * @return 交易记录
     */
    Map selectProperty(int pageNum,int pageSize,String orderId,int consultantId);


    /**
     * 加载风险调仓列表
     * @param consultantId 投资顾问id
     * @return 风险调仓列表
     */
    Map selectRiskList(int consultantId);

}
