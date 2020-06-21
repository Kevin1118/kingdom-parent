package com.kingdom.pojo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <h3>kingdom-parent</h3>
 * <p>备选规则po，存储备选库生成规则的信息</p>
 *
 * @author : HuangJingChao
 * @date : 2020-06-21 11:14
 **/
@Data
public class AlternateRule implements Serializable {
    private Integer id;
    private String name;
    private BigDecimal upanddown;
    private BigDecimal proportion;
    private Integer createdtime;
    private BigDecimal peratio;
    private Integer consultantid;
    private String status;
}
