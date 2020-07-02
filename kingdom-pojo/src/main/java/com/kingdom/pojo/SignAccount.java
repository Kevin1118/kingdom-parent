package com.kingdom.pojo;

import lombok.Data;

import java.io.Serializable;
@Data
public class SignAccount implements Serializable {
    private int signaccountid;
    private Integer userid;
    private Integer productid;
    private double balance;
    private Integer signdate;
    private String status;

}
