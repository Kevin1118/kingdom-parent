package com.kingdom.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author : long
 * @date : 2020/7/8 16:44
 */
@Data
public class Property implements Serializable {
    private int propertyid;
    private int userid;
    private int signaccountid;
    private String orderid;
    private String type;
    private String code;
    private String propertyname;
    private int amount;
    private int updatetime;
    private int status;
}
