package com.kingdom.dao;

import com.kingdom.dto.product.ProductInitDTO;
import com.kingdom.pojo.*;
import com.kingdom.vojo.product.ProductPieChart;

import java.math.BigDecimal;
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
     * @param productid 产品id
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

    /**
     * 初始化股票备选库，插入比例及产品id到表中
     * @param productStockDetail 组合产品股票明细 po
     * @return 插入的数据条数
     */
    int initStockAlternate(ProductStockDetail productStockDetail);

    /**
     * 初始化基金备选库，插入比例及产品id到表中
     * @param productFundDetail 组合产品基金明细 po
     * @return 插入的数据条数
     */
    int initFundAlternate(ProductFundDetail productFundDetail);

    /**
     * 查询股票名称及其所占比例返
     * @param productId 产品id
     * @return List 股票产品详情
     */
    List<ProductStockDetail> selectStockProportionFromDetail(Integer productId);

    /**
     * 查询基金名称及其所占比例返
     * @param productId 产品id
     * @return List 基金产品详情
     */
    List<ProductFundDetail> selectFundProportionFromDetail(Integer productId);

    /**
     * 查询投顾所拥有产品
     * @param consultantId 投顾id
     * @return 产品列表
     */
    List<Product> selectProductByConsultantId(int consultantId);

    /**
     * 根据基金代码查询基金信息
     * @param fundCodes 基金代码
     * @return 基金备选库
     */
    List<FundAlternate> selectFundAlternate(List<String> fundCodes);

    /**
     * 根据股票代码查询股票信息
     * @param stockCodes 股票代码
     * @return 股票备选库
     */
    List<StockAlternate> selectStockAlternate(List<String> stockCodes);
}
