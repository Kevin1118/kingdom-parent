package com.kingdom.dto.consultant;

import lombok.Data;

import java.io.Serializable;

/**
 * @author : long
 * @date : 2020/7/19 15:10
 */
@Data
public class RiskDTO implements Serializable {
    private Integer productid;
    private String oldstockcode;
    private String newstockcode;
}
