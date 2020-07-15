package com.kingdom.pojo;

import lombok.Data;

import java.io.Serializable;
@Data
public class Order implements Serializable {
    private Integer id;
    private String orderid;
    private Integer accountno;
    private double sum;
    private Integer transactiondate;
    private Integer productid;
    private Integer consultantid;
    private Integer status;
}
