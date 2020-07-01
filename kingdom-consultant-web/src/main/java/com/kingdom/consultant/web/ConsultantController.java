package com.kingdom.consultant.web;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import com.kingdom.commonutils.CommonUtils;
import com.kingdom.interfaceservice.consultant.ConsultantService;
import com.kingdom.pojo.Consultant;
import com.kingdom.pojo.HostHolder;
import com.kingdom.result.Result;
import com.kingdom.result.ResultGenerator;
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
 * @author : long
 * @date : 2020/6/20 13:15
 */
@Controller
@RequestMapping("/consultant")
public class ConsultantController {

    @Reference
    private ConsultantService consultantService;

    @Value("${consultant.path.upload}")
    private String uploadPath;

    @Value("${consultant.path.domain}")
    private String domain;

    @Autowired
    private HostHolder hostHolder;

    @ApiOperation("投顾人注册")
    @PostMapping("/registerConsultant")
    @ResponseBody
    public Result register(Consultant consultant){
        if(consultantService.register(consultant)==0){
            return ResultGenerator.genFailResult("邮箱已注册");
        }
        return ResultGenerator.genSuccessResult(1);
    }

    @ApiOperation("投顾人登录")
    @PostMapping("/loginConsultant")
    @ResponseBody
    public Result login(String email,String password,HttpServletResponse response){
        Map<String,Object> map=consultantService.login(email,password);
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
    @GetMapping("/consultantAvatar/{fileName}")
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
    @PostMapping("/uploadAvatar")
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
        //http://localhost:9002/consultant/consultantAvatar/xxx.png
        Consultant consultant = hostHolder.getConsultant();
        String avatarUrl = domain + "/consultant/consultantAvatar/" + fileName;
        consultantService.updateAvatar(consultant.getConsultantid(), avatarUrl);
        return ResultGenerator.genSuccessResult();

    }

    @ApiOperation("实名认证")
    @ResponseBody
    @PostMapping("/authentication")
    public Result authentication(String name,String idnumber){
        Consultant consultant=hostHolder.getConsultant();
        return ResultGenerator.genSuccessResult(consultantService.updateNameAndId(consultant.getConsultantid(),name, idnumber));
    }


    @ApiOperation("基本信息加载")
    @ResponseBody
    @GetMapping("/loadConsultant")
    public Result loadConsultant(){
        Consultant consultant=hostHolder.getConsultant();
        if(consultant!=null){
            return ResultGenerator.genSuccessResult(consultant);
        }else {
            return ResultGenerator.genFailResult("加载失败");
        }
    }


    @ApiOperation("设置支付密码")
    @ResponseBody
    @PostMapping("/setPayPassword")
    public Result setPayPassword(String paypassword){
        Consultant consultant=hostHolder.getConsultant();
        return ResultGenerator.genSuccessResult(consultantService.setPayPassword(consultant.getConsultantid(),paypassword));
    }

    @ApiOperation("更新支付密码")
    @ResponseBody
    @PostMapping("/updatePayPassword")
    public Result updatePayPassword(String oldpaypassword,String newpaypassword){
        Consultant consultant=hostHolder.getConsultant();
        int result=consultantService.updatePayPassword(consultant,oldpaypassword,newpaypassword);
        if(result==-1){
            return ResultGenerator.genFailResult("密码错误");
        }
        return ResultGenerator.genSuccessResult();
    }
}
