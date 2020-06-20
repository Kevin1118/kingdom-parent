package com.kingdom.kingdominterfaceservice;

import com.kingdom.pojo.Product;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author : HuangJingChao
 * @date : 2020-06-20 17:29
 */
public interface ProductService {
    /**
     * 查询接口模板1
     * 产品服务组件2
     * 根据产品 id 获取组合产品基本信息介绍
     * @Param id 产品id
     * @Return product 产品组合的所有信息
     */


    Product selectProductById(Integer id);
    //也可以使用 @Param 注解
    /**
     * 查询接口模板2
     * 产品服务组件2.1
     * 无策略，直接获取产品基本信息的list，带分页功能
     * @Return List<Product>
     */
    //获取 管理题目列表 从testpaper_item_v8查
    Map selectProductAll(Integer pageNum, Integer pageSize);

}
