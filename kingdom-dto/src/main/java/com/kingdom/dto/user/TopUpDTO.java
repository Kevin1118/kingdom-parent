package com.kingdom.dto.user;

import lombok.Data;

import java.io.Serializable;

@Data
public class TopUpDTO implements Serializable {
    private double topUpMoney;
}
