package com.kingdom.user.web;

import com.alibaba.dubbo.config.annotation.Reference;
import com.kingdom.result.*;
import com.kingdom.user.service.UserDemoService;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * <h3>kingdom-parent</h3>
 * <p>test demo</p>
 *
 * @author : HuangJingChao
 * @date : 2020-06-16 12:08
 **/
@Controller
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

}
