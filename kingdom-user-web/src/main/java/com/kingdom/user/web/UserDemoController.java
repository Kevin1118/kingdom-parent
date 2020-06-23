package com.kingdom.user.web;

import com.alibaba.dubbo.config.annotation.Reference;
import com.kingdom.result.*;
import com.kingdom.interfaceservice.user.UserDemoService;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * <h3>kingdom-parent</h3>
 * <p>test demo</p>
 *
 * @author : HuangJingChao
 * @date : 2020-06-16 12:08
 **/
@Controller
@CrossOrigin(origins = "*",allowedHeaders = "*")
public class UserDemoController {

    @Reference
    private UserDemoService userDemoService;

    /**
     * 函数注释采用 java doc 形式，函数内部实现如有必要，可以用 // 形式进行注释，以增强代码的可读性
     *
     * @author : HuangJingChao
     * @date : 2020-06-16 12:08
     */

    @ApiOperation("测试user表的demo")
    @GetMapping("/user/selectByIdDemo")
    @ResponseBody
    public Result selectByIdDemo() {
        return ResultGenerator.genSuccessResult(userDemoService.selectByIdDemo(1));
    }

    /**
     * 投资人登陆的demo
     * @param email 用户邮箱
     * @param phonenumber 用户手机号
     * @param password 用户密码
     * @return 1代表登陆成功 -1代表登陆失败
     */
    @ApiOperation(("投资人登陆demo版"))
    @PostMapping("/user/loginUser")
    @ResponseBody
    public Result loginUser(String email,String phonenumber,String password){
        int i = userDemoService.loginUser(email, phonenumber, password);
        return ResultGenerator.genSuccessResult(i);
    }

}
