package com.kingdom.pojo;

import lombok.Data;

import java.io.Serializable;

@Data
public class IndependentAccount implements Serializable {
    private Integer userid;
    private double independentbalance;
    private int accountamount;
    private String status;
}
