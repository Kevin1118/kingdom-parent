package com.kingdom.dao;

import com.kingdom.pojo.Order;

import java.util.List;

/**
 * 订单
 * @author : long
 * @date : 2020/7/1 17:27
 */
public interface OrderMapper {

    /**
     * 查询订单列表，根据状态区分
     * @param consultantId 投顾人id
     * @param status 状态
     * @return 订单列表
     */
    List<Order> selectOrderByConsultantIdAndStatus(int consultantId,int status);

    /**
     * 更改订单状态
     * @param id 订单id
     * @param status 订单状态
     * @return 操作行数
     */
    int updateOrderStatus(int id,int status);
}
