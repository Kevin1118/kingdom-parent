package com.kingdom.interfaceservice.product;

import com.kingdom.dto.product.ProductInitDTO;
import com.kingdom.pojo.FundAlternate;
import com.kingdom.pojo.Product;
import com.kingdom.pojo.StockAlternate;
import com.kingdom.vojo.product.ProductPieChart;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * @author : HuangJingChao
 * @date : 2020-06-20 17:29
 */
public interface ProductService {
    /**
     * 查询接口模板1
     * 根据产品 id 获取组合产品基本信息介绍
     *
     * @param id 产品id
     * @return product 组合产品详情接口01
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
     * 获取备选库生成规则，带分页功能
     *
     * @param pageNum  当前页数
     * @param pageSize 一页大小
     * @return List<AlternateRule> 包含所有备选库生成规则的 list
     */


    //-----以下为正式开发，产品盒子模块的功能

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

    /**
     * 产品初始化功能，迭代插入股票以及基金的比例到 product_details表，
     * @param list 备选股票id、基金id及其比例
     * @return List<?> 暂时返回 null
     */
    Map<String,Object> initProduct(List<ProductInitDTO> list);

    /**
     * 根据产品 id 获取股票比例饼状图
     *
     * @param productId 产品id
     * @return List<ProductPieChart> 组合产品详情接口01
     */
    List<ProductPieChart> selectProportionFromDetail(Integer productId);

    /**
     * 组合产品页，股票涨跌幅排行榜,带分页
     * @return List<StockAlternate> 备选库股票的涨跌幅排行 list
     */
    List<StockAlternate> showStockUpAndDown();

    /**
     * 组合产品页，股票涨跌幅排行榜,带分页
     * @return List<FundAlternate> 备选库基金的涨跌幅排行 list
     */
    List<FundAlternate> showFundUpAndDown();
}
