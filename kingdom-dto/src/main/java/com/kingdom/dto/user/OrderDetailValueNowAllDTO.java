package com.kingdom.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * <h3>kingdom-parent</h3>
 * <p>总股票市值和总基金净值</p>
 *
 * @author : HuangJingChao
 * @date : 2020-07-18 20:31
 **/
@Data
public class OrderDetailValueNowAllDTO implements Serializable {

    private double stockValueNowAll;

    private double fundValueNowAll;

}
