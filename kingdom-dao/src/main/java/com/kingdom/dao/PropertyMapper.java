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

    /**
     * 查询资产表
     * @param signAccountId 签约账户id
     * @param code 资产代码
     * @return 资产表
     */
    Property loadPropertyByCode(int signAccountId,String code);

    /**
     * 更新资产表
     * @param propertyId 资产id
     * @param amount 份额
     * @param updateTime 更新时间
     * @return 操作行数
     */
    int updatePropertyAmount(int propertyId,int amount,int updateTime);

    /**
     *
     * @return
     */
    List<Property> selectPropertyByAccountNo(Integer accountno);

}
