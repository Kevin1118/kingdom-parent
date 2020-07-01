package com.kingdom.pojo;

import lombok.Data;

import java.io.Serializable;



/**
 *
 * 投顾人pojo
 *
 * @author :longyiping
 * @date :2020/06/18 17:03
 */

@Data
public class Consultant implements Serializable {

    private Integer consultantid;
    private String phonenumber;
    private String email;
    private String activationcode;
    private String avatar;
    private String description;
    private String password;
    private String passwordsalt;
    private String paypasswordsalt;
    private String paypassword;
    private String name;
    private String idnumber;
    private Integer createtime;
    private Integer status;

}
