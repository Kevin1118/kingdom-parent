package com.kingdom.product.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.kingdom.dao.ProductMapper;
import com.kingdom.dto.product.ProductInitDTO;
import com.kingdom.interfaceservice.product.ProductService;
import com.kingdom.pojo.*;
import com.kingdom.result.ResultCode;
import com.kingdom.vojo.product.ProductPieChart;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

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

    @Override
    /**
     * 针对异常情况的事务开启，需要同时在 controller 层和 impl 加上事务注解
     * @Transactional(rollbackFor = Exception.class)
     */
    public Map<String,Object> initProduct(List<ProductInitDTO> list){

//        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
//        long time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date).getTime();
//        int currentTime = (int) (time / 1000);
        Map<String,Object> result = new HashMap<>();

        Integer productId = list.get(0).getProductId();
        //若产品已经初始化过，则报错
        if(productMapper.existInitProductFromProduct(productId) == 1){
            result.put("ResultCode", ResultCode.INITIAL_PRODUCT_ERROR);
            return result;
        }

        for (ProductInitDTO productInitDTO:list) {
            if(productInitDTO.getStockAlternateId() != null){
                ProductStockDetail productStockDetail = new ProductStockDetail();
                //插入 组合产品id，备选股票id，备选股票比例，备选股票代码，备选股票名称
                productStockDetail.setProductId(productInitDTO.getProductId());
                productStockDetail.setStockAlternateId(productInitDTO.getStockAlternateId());
                productStockDetail.setProportion(productInitDTO.getStockProportion());
                productStockDetail.setStockCode(productInitDTO.getStockCode());
                productStockDetail.setStockName(productInitDTO.getStockName());
                productMapper.initStockAlternate(productStockDetail);
            }
            if(productInitDTO.getFundAlternateId() != null){
                ProductFundDetail productFundDetail = new ProductFundDetail();
                //插入 组合产品id，备选基金id，备选基金比例，备选基金代码，备选基金名称
                productFundDetail.setProductId(productInitDTO.getProductId());
                productFundDetail.setFundAlternateId(productInitDTO.getFundAlternateId());
                productFundDetail.setProportion(productInitDTO.getFundProportion());
                productFundDetail.setFundCode(productInitDTO.getFundCode());
                productFundDetail.setFundName(productInitDTO.getFundName());
                productMapper.initFundAlternate(productFundDetail);
            }
        }
        //初始化成功 更改组合产品状态为 2
        int i = productMapper.updateStatusFromProductAfterInit(productId);
        if(i == 1){
            result.put("ResultCode",ResultCode.SUCCESS);
        }
        return result;
    }

    @Override
    public List<ProductPieChart> selectProportionFromDetail(Integer productId) {
        List<ProductPieChart> list = new ArrayList<>();
        List<ProductStockDetail> stockList = productMapper.selectStockProportionFromDetail(productId);
        List<ProductFundDetail> fundList = productMapper.selectFundProportionFromDetail(productId);
        if(stockList != null){
            for (ProductStockDetail p:stockList) {
                ProductPieChart v = new ProductPieChart();
                v.setProductType("Stock");
                v.setProductCode(p.getStockCode());
                v.setProductName(p.getStockName());
                v.setProportion(p.getProportion());
                list.add(v);
            }
        }
        if(fundList != null){
            for (ProductFundDetail p:fundList) {
                ProductPieChart v = new ProductPieChart();
                v.setProductType("Fund");
                v.setProductCode(p.getFundCode());
                v.setProductName(p.getFundName());
                v.setProportion(p.getProportion());
                list.add(v);
            }
        }
        return list;
    }

    @Override
    public List<StockAlternate> showStockUpAndDown() {
        List<StockAlternate> showStockUpAndDown = productMapper.selectStockUpAndDownFromAlternateOrderByDesc();
        return showStockUpAndDown;
    }

    @Override
    public List<FundAlternate> showFundUpAndDown() {
        List<FundAlternate> showFundUpAndDown = productMapper.selectFundUpAndDownFromAlternateOrderByDesc();
        return showFundUpAndDown;
    }


}
