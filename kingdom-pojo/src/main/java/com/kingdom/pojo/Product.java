package com.kingdom.pojo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <h3>kingdom-parent</h3>
 * <p>Product产品组合表</p>
 *
 * @author : HuangJingChao
 * @date : 2020-06-20 17:42
 **/
@Data
public class Product implements Serializable {

    private Integer productid;

    private String name;

    private Integer strategyid;

    private Integer consultantid;

    private String risktype;

    private Integer stockamount;

    private Integer fundamount;

    private BigDecimal expectedYield;

    private String status;

    private Integer createdtime;

    private Integer updatedtime;

    private Integer updatedconsultant;

}
