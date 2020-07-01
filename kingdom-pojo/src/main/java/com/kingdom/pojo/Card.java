package com.kingdom.pojo;

import lombok.Data;

import java.io.Serializable;

@Data
public class Card implements Serializable {
    private int cardid;
    private String realname;
    private String cardnumber;
    private int createdtime;
    private String status;
}
