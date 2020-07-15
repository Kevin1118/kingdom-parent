package com.kingdom.pojo;

import org.springframework.stereotype.Component;

/**
 * @author : long
 * @date : 2020/6/27 9:09
 */
@Component
public class HostHolder {

    private ThreadLocal<Consultant> consultants = new ThreadLocal<>();

    private ThreadLocal<User> users=new ThreadLocal<>();

    public void setConsultant(Consultant consultant) {
        consultants.set(consultant);
    }

    public void setUser(User user){
        users.set(user);
    }

    public Consultant getConsultant() {
        return consultants.get();
    }
    public User getUser(){
        return users.get();
    }

    public void clear() {
        consultants.remove();
        users.remove();
    }

}