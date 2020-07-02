package com.kingdom.product.web;

import com.alibaba.dubbo.config.annotation.Reference;
import com.kingdom.dto.product.ProductInitDTO;
import com.kingdom.interfaceservice.product.ProductService;
import com.kingdom.result.Result;
import com.kingdom.result.ResultGenerator;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
/**
 * <h3>kingdom-parent</h3>
 * <p>产品模块第一个controller</p>
 *
 * @author : HuangJingChao
 * @date : 2020-06-20 17:05
 **/
@Controller
@CrossOrigin(origins = "*",allowedHeaders = "*")
public class ProductController {

    @Reference
    private ProductService productService;

    @ApiOperation("Demo接口 查询所有产品明细，带分页功能")
    @GetMapping("/product/selectProductAll")
    @ResponseBody
    public Result selectProductAll(@RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "10") Integer pageSize)
    {
        return ResultGenerator.genSuccessResult(productService.selectProductAll(pageNum, pageSize));
    }

    @ApiOperation("Demo接口 备选规则,获取备选库生成规则")
    @GetMapping("/product/selectAlternateRuleAll")
    @ResponseBody
    public Result selectAlternateRuleAll(@RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "10") Integer pageSize)
    {
        return ResultGenerator.genSuccessResult(productService.selectAlternateRuleAll(pageNum, pageSize));
    }

    //以下为正式业务

    @ApiOperation("展示股票备选库页面")
    @GetMapping("/product/selectStockAlternateAll")
    @ResponseBody
    public Result selectStockAlternateAll(@RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "10") Integer pageSize)
    {
        return ResultGenerator.genSuccessResult(productService.selectStockAlternateAll(pageNum, pageSize));
    }

    @ApiOperation("展示基金备选库页面")
    @GetMapping("/product/selectFundAlternateAll")
    @ResponseBody
    public Result selectFundAlternateAll(@RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "10") Integer pageSize)
    {
        return ResultGenerator.genSuccessResult(productService.selectFundAlternateAll(pageNum, pageSize));
    }

    @ApiOperation("初始化组合产品")
    @PostMapping("/product/initProduct")
    @ResponseBody
    public Result initProduct(@RequestBody List<ProductInitDTO> listDTO)
    {
        return ResultGenerator.genSuccessResult(productService.initProduct(listDTO));
    }

    @ApiOperation("根据产品id查询出组合产品详情 01")
    @GetMapping("/product/selectProductById")
    @ResponseBody
    public Result selectProductById(@RequestParam Integer id) {
        return ResultGenerator.genSuccessResult(productService.selectProductById(id));
    }

    @ApiOperation("根据产品id查询组合产品详情 饼状图 02")
    @GetMapping("/product/selectProportionFromDetail")
    @ResponseBody
    public Result selectProportionFromDetail(@RequestParam Integer id) {
        return ResultGenerator.genSuccessResult(productService.selectProportionFromDetail(id));
    }
}
