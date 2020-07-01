package com.kingdom.pojo;

import org.springframework.stereotype.Component;

/**
 * @author : long
 * @date : 2020/6/27 9:09
 */
@Component
public class HostHolder {

    private ThreadLocal<User> users = new ThreadLocal<>();

    public void setUser(User user) {
        users.set(user);
    }

    public User getUser() {
        return users.get();
    }

    public void clear() {
        users.remove();
    }

}