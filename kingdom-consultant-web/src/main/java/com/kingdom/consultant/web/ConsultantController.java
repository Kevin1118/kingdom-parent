package com.kingdom.consultant.web;

import com.alibaba.dubbo.config.annotation.Reference;
import com.kingdom.consultant.service.ConsultantService;
import com.kingdom.pojo.Consultant;
import com.kingdom.result.Result;
import com.kingdom.result.ResultGenerator;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author : long
 * @date : 2020/6/20 13:15
 */
@Controller
public class ConsultantController {

    @Reference
    private ConsultantService consultantService;

    @ApiOperation("投顾人注册")
    @PostMapping("/consultant/register")
    @ResponseBody
    public Result register(Consultant consultant){
        return ResultGenerator.genSuccessResult(consultantService.register(consultant));
    }
}
