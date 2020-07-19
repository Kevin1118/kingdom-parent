package com.kingdom.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * <h3>kingdom-parent</h3>
 * <p>新更新的orderDetail</p>
 *
 * @author : HuangJingChao
 * @date : 2020-07-19 22:51
 **/
@Data
public class OrderDetailByUserDTO implements Serializable {

    private String productName;

    private String type;

    private Double sum;

    private Integer submitTime;

    private String code;

    private String propertyName;


}
