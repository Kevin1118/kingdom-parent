package com.kingdom.dao;

import com.kingdom.pojo.Property;

import java.util.List;

/**
 * @author : long
 * @date : 2020/7/8 16:49
 */
public interface PropertyMapper {

    /**
     * 增加资产
     * @param property 资产
     * @return 操作行数
     */
    int insertProperty(Property property);

    /**
     * 查询资产表
     * @param orderId 订单号
     * @return 资产表
     */
    List<Property> loadProperty(String orderId);

}
