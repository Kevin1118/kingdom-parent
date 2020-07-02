package com.kingdom.vojo.product;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <h3>kingdom-parent</h3>
 * <p>产品饼状图</p>
 *
 * @author : HuangJingChao
 * @date : 2020-06-30 17:30
 **/
@Data
public class ProductPieChart implements Serializable {

    private String productType;

    private String productName;

    private String productCode;

    private BigDecimal proportion;

}
