package com.kingdom.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author : long
 * @date : 2020/7/19 9:13
 */
@Data
public class ConsultantRecord implements Serializable {
    private int consultantrecordid;
    private String orderid;
    private int signaccountid;
    private String productname;
    private String type;
    private String code;
    private String propertyname;
    private float sum;
    private int amount;
    private int submittime;
    private int updatedtime;
}
