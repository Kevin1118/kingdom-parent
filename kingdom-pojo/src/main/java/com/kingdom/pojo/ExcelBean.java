package com.kingdom.pojo;

import lombok.Data;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;

import java.io.Serializable;

/**
 * <h3>kingdom-parent</h3>
 * <p>用于Excel表操作的实体类</p>
 *
 * @author : HuangJingChao
 * @date : 2020-07-07 16:48
 *
 **/

@Data
public class ExcelBean implements Serializable {
    // 列头（标题）名
    private String headTextName;
    // 对应字段名
    private String propertyName;
    // 合并单元格数
    private Integer cols;

    private XSSFCellStyle cellStyle;

}
