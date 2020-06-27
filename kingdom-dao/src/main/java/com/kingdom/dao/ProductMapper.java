package com.kingdom.dao;

import com.kingdom.pojo.AlternateRule;
import com.kingdom.pojo.FundAlternate;
import com.kingdom.pojo.Product;
import com.kingdom.pojo.StockAlternate;

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
     * @param productid
     * @return Product
     */
    Product selectProductById(Integer productid);

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

    /**
     * 查询所有股票备选库，带分页
     * @return List<StockAlternate>
     */
    List<StockAlternate> selectStockAlternateAll();

    /**
     * 查询所有股票备选库，带分页
     * @return List<FundAlternate>
     */
    List<FundAlternate> selectFundAlternateAll();
}
