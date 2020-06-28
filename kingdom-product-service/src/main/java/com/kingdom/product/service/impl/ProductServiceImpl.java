package com.kingdom.product.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.kingdom.dao.ProductMapper;
import com.kingdom.interfaceservice.product.ProductService;
import com.kingdom.pojo.AlternateRule;
import com.kingdom.pojo.FundAlternate;
import com.kingdom.pojo.Product;
import com.kingdom.pojo.StockAlternate;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <h3>kingdom-parent</h3>
 * <p>产品接口的实现类</p>
 *
 * @author : HuangJingChao
 * @date : 2020-06-20 19:29
 **/
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductMapper productMapper;

    @Override
    public Product selectProductById(Integer productid) {
        return productMapper.selectProductById(productid);
    }

    /**
     * 带分页功能的查询产品明细
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public Map selectProductAll(Integer pageNum, Integer pageSize) {
        Page<Object> pageObject = PageHelper.startPage(pageNum, pageSize);
        List<Product> selectProductAll = productMapper.selectProductAll();
        Map res = new HashMap();
        res.put("total", pageObject.getTotal());
        res.put("data", selectProductAll);
        return res;
    }

    @Override
    public Map selectAlternateRuleAll(Integer pageNum, Integer pageSize) {
        Page<Object> pageObject = PageHelper.startPage(pageNum, pageSize);
        List<AlternateRule> selectRuleAllList = productMapper.selectAlternateRuleAll();
        Map res = new HashMap();
        res.put("total", pageObject.getTotal());
        res.put("data", selectRuleAllList);
        return res;
    }

    @Override
    public Map selectStockAlternateAll(Integer pageNum, Integer pageSize) {
        Page<Object> pageObject = PageHelper.startPage(pageNum, pageSize);
        List<StockAlternate> selectStockAlternateAll = productMapper.selectStockAlternateAll();
        Map res = new HashMap();
        res.put("total", pageObject.getTotal());
        res.put("data", selectStockAlternateAll);
        return res;
    }

    @Override
    public Map selectFundAlternateAll(Integer pageNum, Integer pageSize) {
        Page<Object> pageObject = PageHelper.startPage(pageNum, pageSize);
        List<FundAlternate> selectFundAlternateAll = productMapper.selectFundAlternateAll();
        Map res = new HashMap();
        res.put("total", pageObject.getTotal());
        res.put("data", selectFundAlternateAll);
        return res;
    }

}
