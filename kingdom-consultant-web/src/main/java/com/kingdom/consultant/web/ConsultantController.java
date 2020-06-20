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

import java.util.Map;

/**
 * @author : long
 * @date : 2020/6/20 13:15
 */
@Controller
public class ConsultantController {

    @Reference
    private ConsultantService consultantService;

    @ApiOperation("投顾人注册")
    @PostMapping("/consultant/registerConsultant")
    @ResponseBody
    public Result register(Consultant consultant){
        return ResultGenerator.genSuccessResult(consultantService.register(consultant));
    }

    @ApiOperation("投顾人登录")
    @PostMapping("/consultant/loginConsultant")
    @ResponseBody
    public Result login(String phoneNumber,String password){
        Map<String,Object> map=consultantService.login(phoneNumber,password);
        if(map.containsKey("loginTicket")){
            return ResultGenerator.genSuccessResult(map.get("loginTicket"));
        }else {
            return ResultGenerator.genFailResult(map.get("loginErrorMessage").toString());
        }
    }
}
