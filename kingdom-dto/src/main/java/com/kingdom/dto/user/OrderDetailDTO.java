package com.kingdom.dto.user;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <h3>kingdom-parent</h3>
 * <p>交易明细DTO</p>
 *
 * @author : HuangJingChao
 * @date : 2020-07-18 19:57
 **/
@Data
public class OrderDetailDTO implements Serializable {
    
    private String type;

    private Integer date;

    private Integer status;

    //组合产品名称
    private String productName;

    //股票或基金代码
    private String code;

    //股票或基金名称
    private String propertyName;

    //买入金额
    private Double amount;

}
