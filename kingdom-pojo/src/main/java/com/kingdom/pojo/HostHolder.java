package com.kingdom.pojo;

import org.springframework.stereotype.Component;

/**
 * @author : long
 * @date : 2020/6/27 9:09
 */
@Component
public class HostHolder {

    private ThreadLocal<Consultant> consultants = new ThreadLocal<>();

    public void setConsultant(Consultant consultant) {
        consultants.set(consultant);
    }

    public Consultant getConsultant() {
        return consultants.get();
    }

    public void clear() {
        consultants.remove();
    }

}