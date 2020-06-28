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

    private Integer stockAlternateId;

    private Integer fundAlternateId;

    private BigDecimal stockProportion;

    private BigDecimal fundProportion;

}
