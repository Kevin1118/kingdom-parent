package com.kingdom.product.web;

import com.alibaba.dubbo.config.annotation.Reference;
import com.kingdom.kingdominterfaceservice.ProductService;
import com.kingdom.result.Result;
import com.kingdom.result.ResultGenerator;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <h3>kingdom-parent</h3>
 * <p>产品模块第一个controller</p>
 *
 * @author : HuangJingChao
 * @date : 2020-06-20 17:05
 **/
@Controller
public class ProductController {

    @Reference
    private ProductService productService;

    @ApiOperation("根据产品id查询出产品明细")
    @GetMapping("/product/selectProductById")
    @ResponseBody
    public Result selectProductById(@RequestParam Integer id) {
        return ResultGenerator.genSuccessResult(productService.selectProductById(id));
    }

    @ApiOperation("查询所有产品明细，带分页功能")
    @GetMapping("/product/selectProductAll")
    @ResponseBody
    public Result selectProductAll(@RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "10") Integer pageSize)
    {
        return ResultGenerator.genSuccessResult(productService.selectProductAll(pageNum, pageSize));
    }



}
