package com.kingdom.pojo;

import java.io.Serializable;

/**
 * <h3>kingdom-parent</h3>
 * <p>UserDemoForPojo</p>
 *
 * @author : HuangJingChao
 * @date : 2020-06-17 10:53
 **/

public class User implements Serializable {
    private Integer id;

    private String email;

    private String username;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
