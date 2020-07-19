package com.kingdom.vojo.product;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author : long
 * @date : 2020/7/15 21:06
 */
@Data
public class OrderVo implements Serializable {
    private int id;
    private String orderid;
    private int accountno;
    private float sum;
    private int transactiondate;
    private int productid;
    private int consultantid;
    private int status;
    private String percent;
    private String productname;
    private float expectedyield;
}
