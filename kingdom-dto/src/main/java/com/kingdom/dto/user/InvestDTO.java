package com.kingdom.dto.user;

import lombok.Data;

import java.io.Serializable;
@Data
public class InvestDTO implements Serializable {
    private String name;
    private int sum;
}
