package com.kingdom.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author : long
 * @date : 2020/6/20 16:55
 */
@Data
public class LoginTicket implements Serializable {

    private int id;
    private int userid;
    private String ticket;
    private int status;
    private int expired;
}
