package com.kingdom.pojo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <h3>kingdom-parent</h3>
 * <p>基金备选库表</p>
 *
 * @author : HuangJingChao
 * @date : 2020-06-27 23:17
 **/
@Data
public class FundAlternate implements Serializable {
    private Integer fundAlternateId;
    private String fundType;
    private String name;
    private String code;
    private String riskType;
    private BigDecimal valueNow;
    private BigDecimal upAndDown;
    private BigDecimal buyingRate;
    private Integer createdTime;
    private Integer updatedTime;
    private Integer ruleId;
    private BigDecimal peRatio;
    private BigDecimal prevClose;
    private BigDecimal open;
    private String corporation;
}
