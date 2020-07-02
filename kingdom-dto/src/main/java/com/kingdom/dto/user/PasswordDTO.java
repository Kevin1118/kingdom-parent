package com.kingdom.dto.user;

import lombok.Data;

import java.io.Serializable;

@Data
public class PasswordDTO implements Serializable {
    private String oldPassword;
    private String newPassword;
}
