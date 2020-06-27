package com.kingdom.interfaceservice.product;

import com.kingdom.pojo.Product;

import java.util.Map;

/**
 * @author : HuangJingChao
 * @date : 2020-06-20 17:29
 */
public interface ProductService {
    /**
     * 查询接口模板1
     * 产品服务组件 组件编号 Jc-3
     * 根据产品 id 获取组合产品基本信息介绍
     *
     * @param id 产品id
     * @return product 产品组合的所有信息
     */
    Product selectProductById(Integer id);

    /**
     * 查询接口模板2
     * 无查询条件，直接获取产品基本信息的list，带分页功能
     * 使用Map是为了分页
     *
     * @param pageNum  当前页数
     * @param pageSize 一页大小
     * @return List<Product> 包含所有产品信息的 list
     */
    Map selectProductAll(Integer pageNum, Integer pageSize);

    /**
     * 产品服务组件 组件编号 Jc4
     * 获取备选库生成规则，带分页功能
     *
     * @param pageNum  当前页数
     * @param pageSize 一页大小
     * @return List<AlternateRule> 包含所有备选库生成规则的 list
     */
    Map selectAlternateRuleAll(Integer pageNum, Integer pageSize);

    /**
     * 产品初始化_股票选择_展示股票备选库
     *
     * @param pageNum  当前页数
     * @param pageSize 一页大小
     * @return List<StockAlternate> 包含所有备选库股票的 list
     */
    Map selectStockAlternateAll(Integer pageNum, Integer pageSize);

    /**
     * 产品初始化_股票选择_展示股票备选库
     *
     * @param pageNum  当前页数
     * @param pageSize 一页大小
     * @return List<FundAlternate> 包含所有备选库股票的 list
     */
    Map selectFundAlternateAll(Integer pageNum, Integer pageSize);

}
