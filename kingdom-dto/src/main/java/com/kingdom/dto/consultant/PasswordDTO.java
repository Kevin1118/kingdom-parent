package com.kingdom.dto.consultant;

import lombok.Data;

import java.io.Serializable;

/**
 * @author : long
 * @date : 2020/7/2 9:46
 */
@Data
public class PasswordDTO implements Serializable {
    private String oldpaypassword;
    private String newpaypassword;
    private String paypassword;
    private String oldpassword;
    private String newpassword;
}
