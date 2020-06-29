package com.kingdom.user.web;

import com.alibaba.dubbo.config.annotation.Reference;
import com.kingdom.result.*;
import com.kingdom.interfaceservice.user.UserDemoService;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


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


    @ApiOperation("投资人注册功能")
    @PostMapping("/user/registerUser")
    @ResponseBody
    public Result registerUser(int userid,String username,String email,String phonenumber,String password){
        return ResultGenerator.genSuccessResult(userDemoService.registerUser(userid,username,email,phonenumber,password));
    }

    /**
     * 投资人登陆的demo
     * @param email 用户邮箱
     * @param phonenumber 用户手机号
     * @param password 用户密码
     * @return 1代表登陆成功 -1代表登陆失败
     */
    @ApiOperation(("投资人登录"))
    @PostMapping("/user/loginUser")
    @ResponseBody
    public Result loginUser(String email,String phonenumber,String password){
        int i = userDemoService.loginUser(email, phonenumber, password);
        return ResultGenerator.genSuccessResult(i);
    }
    /**
     * 投资人登陆的demo
     * @param cardid 银行卡id
     * @param realname 真实姓名
     * @param phonenumber 用户手机号
     * @return 1代表登陆成功 -1代表登陆失败
     */
    @ApiOperation(("投资人绑定银行卡"))
    @PostMapping("/user/bindCardUser")
    @ResponseBody
    public Result bindCardUser(int cardid,String realname,String phonenumber){
        int i = userDemoService.bindCardUser(cardid, realname, phonenumber);
        return ResultGenerator.genSuccessResult(i);
    }
}
