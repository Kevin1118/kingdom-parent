package com.kingdom.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 *
 * <h3>kingdom-parent</h3>
 * <p>订单表</p>
 * @author : long
 * @date : 2020/7/1 17:22
 */
@Data
public class Order implements Serializable {

    private int id;
    private String orderid;
    private int accountno;
    private float sum;
    private int transactiondate;
    private int productid;
    private int consultantid;
    private int status;
}
