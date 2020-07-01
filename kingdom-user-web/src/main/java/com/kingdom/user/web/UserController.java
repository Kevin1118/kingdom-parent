package com.kingdom.user.web;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import com.kingdom.commonutils.CommonUtils;
import com.kingdom.pojo.Card;
import com.kingdom.pojo.HostHolder;
import com.kingdom.pojo.User;
import com.kingdom.result.*;
import com.kingdom.interfaceservice.user.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;


/**
 * <h3>kingdom-parent</h3>
 * <p>test demo</p>
 *
 * @author : HuangJingChao
 * @date : 2020-06-16 12:08
 **/
@Controller
@CrossOrigin(origins = "*",allowedHeaders = "*")
public class UserController {

    @Reference
    private UserService userService;

    @Value("${user.path.upload}")
    private String uploadPath;

    @Value("${user.path.domain}")
    private String domain;

    @Autowired
    private HostHolder hostHolder;

    /**
     * 函数注释采用 java doc 形式，函数内部实现如有必要，可以用 // 形式进行注释，以增强代码的可读性
     *
     * @author : HuangJingChao
     * @date : 2020-06-16 12:08
     */

    @ApiOperation("测试user表的demo")
    @GetMapping("/user/selectById")
    @ResponseBody
    public Result selectUserById() {
        return ResultGenerator.genSuccessResult(userService.selectUserById(1));
    }


    @ApiOperation("投资人注册功能")
    @PostMapping("/user/registerUser")
    @ResponseBody
    public Result registerUser(User user){
        if (userService.registerUser(user)==0){
            return(ResultGenerator.genFailResult("该手机号已被注册"));
        }
        return ResultGenerator.genSuccessResult(userService.registerUser(user));
    }


    @ApiOperation(("投资人登录"))
    @PostMapping("/user/loginUser")
    @ResponseBody
    public Result loginUser(String phoneNumber, String password, HttpServletResponse response){
        Map<String,Object> map=userService.loginUser(phoneNumber,password);
        if(map.get("loginticket")!=null){
            Cookie cookie = new Cookie("loginticket", map.get("loginticket").toString());
            cookie.setMaxAge((int)(System.currentTimeMillis()/1000));
            response.addCookie(cookie);
            return ResultGenerator.genSuccessResult(map);
        }else {
            return ResultGenerator.genFailResult(map.get("loginerrormessage").toString());
        }

    }
    @ApiOperation("头像加载")
    @GetMapping("/headerUser/{fileName}")
    public void getHeader(@PathVariable("fileName") String filename, HttpServletResponse response) {
        //服务器存放路径
        filename = uploadPath + "/" + filename;
        //文件后缀
        String suffix = filename.substring(filename.lastIndexOf("."));
        //响应图片
        response.setContentType("/image" + suffix);
        try (
                FileInputStream fileInputStream = new FileInputStream(filename);
                OutputStream os = response.getOutputStream();
        ) {

            byte[] buffer = new byte[1024];
            int b = 0;
            while ((b = fileInputStream.read(buffer)) != -1) {
                os.write(buffer, 0, b);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @ApiOperation("上传头像")
    @ResponseBody
    @PostMapping("/uploadAvatarUser")
    public Result uploadHeader(MultipartFile headerImage) {
        if (headerImage == null) {
            return ResultGenerator.genFailResult("您还没有选择图片！");
        }
        String fileName = headerImage.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        if (StringUtils.isBlank(suffix)) {
            return ResultGenerator.genFailResult("文件格式不正确！");
        }

        //生成随机文件名
        fileName = CommonUtils.generateUUID() + suffix;
        //确定文件存放的路径
        File dest = new File(uploadPath + "/" + fileName);
        try {
            //存储文件
            headerImage.transferTo(dest);
        } catch (IOException e) {
            throw new RuntimeException("上传文件失败，服务器发生异常！", e);
        }
        //更新当前用户头像路径（web访问路径）
        //http://localhost:9001/consultant/consultantAvatar/xxx.png
        User user = hostHolder.getUser();

        String avatarUrl = domain + "/user/headerUser/" + fileName;
        userService.updateAvatarUser(user.getUserid(),avatarUrl);
        return ResultGenerator.genSuccessResult();

    }

    @ApiOperation(("投资人绑定银行卡"))
    @PostMapping("/user/bindCardUser")
    @ResponseBody
    public Result bindCardUser(Card card){
        int i = userService.bindCardUser(card);
        return ResultGenerator.genSuccessResult(i);
    }
}
