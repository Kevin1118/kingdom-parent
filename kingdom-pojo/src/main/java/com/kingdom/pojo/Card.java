package com.kingdom.pojo;

import lombok.Data;

import java.io.Serializable;

@Data
public class Card implements Serializable {
    private Integer cardid;
    private Integer userid;
    private String realname;
    private String cardnumber;
    private Integer createdtime;
    private String status;
}
