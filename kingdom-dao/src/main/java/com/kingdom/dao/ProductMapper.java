package com.kingdom.dao;

import com.kingdom.pojo.AlternateRule;
import com.kingdom.pojo.Product;

import java.util.List;

/**
 * 访问产品数据库的mapper文件
 *
 * @author HuangJingChao
 * @date : 2020-06-20 19:35
 */
public interface ProductMapper {

    /**
     * 根据产品id查询出产品明细
     * @param id
     * @return Product
     */
    Product selectProductById(Integer id);

    /**
     * 查询所有产品明细，带分页功能
     * @return List<Product>
     */
    List<Product> selectProductAll();

    /**
     * 查询所有备选规则，带分页功能
     * @return List<AlternateRule>
     */
    List<AlternateRule> selectAlternateRuleAll();


}
