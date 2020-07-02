package com.kingdom.pojo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <h3>kingdom-parent</h3>
 * <p>股票备选库表</p>
 *
 * @author : HuangJingChao
 * @date : 2020-06-30 10:51
 **/
@Data
public class ProductStockDetail implements Serializable {

    private Integer productStockDetailId;

    private Integer productId;

    private String stockCode;

    private BigDecimal proportion;

    private String stockName;

    private Integer userId;

    private Integer stockAlternateId;


}
