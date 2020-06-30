package com.kingdom.pojo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <h3>kingdom-parent</h3>
 * <p>基金备选库表</p>
 *
 * @author : HuangJingChao
 * @date : 2020-06-30 10:53
 **/
@Data
public class ProductFundDetail implements Serializable {
    private Integer productFundDetailId;

    private Integer productId;

    private String fundCode;

    private BigDecimal proportion;

    private String fundName;

    private Integer userId;

    private Integer fundAlternateId;
}
