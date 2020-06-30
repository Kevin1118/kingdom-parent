package com.kingdom.dto.product;

import com.kingdom.pojo.FundAlternate;
import com.kingdom.pojo.StockAlternate;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * <h3>kingdom-parent</h3>
 * <p>组合产品初始化DTO</p>
 *
 * @author : HuangJingChao
 * @date : 2020-06-28 11:04
 **/
@Data
public class ProductInitDTO implements Serializable {

    private Integer productId;

    private Integer stockAlternateId;

    private BigDecimal stockProportion;

    private String stockCode;

    private String stockName;

    private Integer fundAlternateId;

    private BigDecimal fundProportion;

    private String fundCode;

    private String fundName;

}
