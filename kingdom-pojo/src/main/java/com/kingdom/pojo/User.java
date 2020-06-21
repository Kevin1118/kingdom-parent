package com.kingdom.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * <h3>kingdom-parent</h3>
 * <p>UserDemoForPojo</p>
 *
 * @author : HuangJingChao
 * @date : 2020-06-17 10:53
 **/
@Data
public class User implements Serializable {

    private Integer id;

    private String name ;

    private String email;

    private String phonenumber;

    private String password;

    private String salt;

    private String paypassword;

    private String paypasswordsalt;

    private String usertype;

    private String username;

    private String avatar;

    private String idnumber;

    private String approvalstatus;

    private Integer approvaltime;

    private Integer createdtime;

    private Integer updatedtime;

    private String status;

}
