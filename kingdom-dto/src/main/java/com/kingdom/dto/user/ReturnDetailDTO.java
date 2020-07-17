package com.kingdom.dto.user;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <h3>kingdom-parent</h3>
 * <p>投资人收益详情</p>
 *
 * @author : HuangJingChao
 * @date : 2020-07-17 19:43
 **/
@Data
public class ReturnDetailDTO implements Serializable {

    //产品类型
    private String Type;

    //股票或基金代码
    private String code;

    //股票或基金名称
    private String propertyName;

    //持有份额
    private Integer Amount;

    //单一产品收益金额 1000 也可能是 - 1000
    private Double AmountOfReturnOne;

    //单一产品持仓金额 51000
    private Double AmountNow;

    //单一收益率
    private Double RateOfReturn;

}
