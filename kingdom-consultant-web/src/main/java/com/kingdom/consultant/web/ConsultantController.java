package com.kingdom.consultant.web;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import com.kingdom.commonutils.CommonUtils;
import com.kingdom.commonutils.Constant;
import com.kingdom.dto.consultant.PasswordDTO;
import com.kingdom.interfaceservice.consultant.ConsultantService;
import com.kingdom.pojo.Consultant;
import com.kingdom.pojo.HostHolder;
import com.kingdom.pojo.LoginTicket;
import com.kingdom.pojo.Order;
import com.kingdom.result.Result;
import com.kingdom.result.ResultCode;
import com.kingdom.result.ResultGenerator;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
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
@CrossOrigin(origins = "*",allowedHeaders = "*")
@RequestMapping("/consultant")
public class ConsultantController implements Constant {

    /**
     * 注入投顾人业务层
     */
    @Reference
    private ConsultantService consultantService;

    /**
     * 注入application中配置的文件上传路径
     */
    @Value("${consultant.path.upload}")
    private String uploadPath;

    /**
     * 注入application中配置的域名
     */
    @Value("${consultant.path.domain}")
    private String domain;

    /**
     * 注入用户持有对象
     */
    @Autowired
    private HostHolder hostHolder;

    /**
     * 注入redis模板类，用于redis数据库的操作
     */
    @Autowired
    private RedisTemplate redisTemplate;

    @ApiOperation("投顾人注册,目前采用邮箱注册")
    @PostMapping("/registerConsultant")
    @ResponseBody
    public Result register(@RequestBody Consultant consultant) {
        //判空处理，如果参数为空，返回空值错误
        if (consultant == null) {
            return ResultGenerator.genFailResult(ResultCode.EMPTY_ARG);
        }
        //使用ResultCode接受业务调用结果，如果成功则生成默认成功结果，失败则返回相应的错误码
        ResultCode code = consultantService.register(consultant);
        if (code.equals(ResultCode.SUCCESS)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(code);
        }
    }

    @ApiOperation("投顾人登录")
    @PostMapping("/loginConsultant")
    @ResponseBody
    public Result login(@RequestBody Consultant consultant, HttpServletResponse response) {
        String email=consultant.getEmail();
        String password=consultant.getPassword();
        //判空处理，参数为空则返回空值错误
        if (StringUtils.isEmpty(email) || StringUtils.isEmpty(password)) {
            return ResultGenerator.genFailResult(ResultCode.EMPTY_ARG);
        }
        //使用map接收业务调用结果，map中数据包括响应码和登录凭证
        Map<String, Object> map = consultantService.login(email, password);
        ResultCode code = (ResultCode) map.get("resultCode");
        //如果响应码为成功，则获取map中的登录凭证
        if (code.equals(ResultCode.SUCCESS)) {
            LoginTicket ticket = (LoginTicket) map.get("loginTicket");
            //将登录凭证使用cookie返回前端
            Cookie cookie = new Cookie("loginTicket", ticket.getTicket());
            //cookie有效时间为十二小时，对应ticket有效时间十二小时
            cookie.setMaxAge(3600 * 12);
            response.addCookie(cookie);
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(code);
        }
    }

    @ApiOperation("头像加载")
    @GetMapping("/consultantAvatar/{fileName}")
    public Result getHeader(@PathVariable("fileName") String filename, HttpServletResponse response) {
        //使用上传路径和文件名拼接出服务器存放路径
        filename = uploadPath + "/" + filename;
        //获取文件后缀
        String suffix = filename.substring(filename.lastIndexOf("."));
        //响应图片
        if(suffix.equals(SUFFIX_JPG)){
            response.setContentType(CONTENT_TYPE_JPG);
        }else {
            response.setContentType(CONTENT_TYPE_PNG);
        }

        try (
                //文件输入流和响应输出流，自动关闭
                FileInputStream fileInputStream = new FileInputStream(filename);
                OutputStream os = response.getOutputStream();
        ) {
            //使用缓冲区，每次1024字节
            byte[] buffer = new byte[1024];
            int b = 0;
            while ((b = fileInputStream.read(buffer)) != -1) {
                //循环将缓冲区内容输出
                os.write(buffer, 0, b);
            }
            return ResultGenerator.genSuccessResult();
        } catch (IOException e) {
            e.printStackTrace();
            return ResultGenerator.genFailResult(e.getMessage());
        }
    }

    @ApiOperation("上传头像")
    @ResponseBody
    @PostMapping("/uploadAvatar")
    public Result uploadHeader(MultipartFile avatar) {
        //判空处理，如果参数为空则返回空值响应码
        if (avatar == null) {
            return ResultGenerator.genFailResult(ResultCode.EMPTY_ARG);
        }
        //获取文件原名
        String fileName = avatar.getOriginalFilename();
        //获取文件后缀
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        if (StringUtils.isBlank(suffix)) {
            return ResultGenerator.genFailResult(ResultCode.FILE_SUFFIX_ERROR);
        }
        if (!suffix.equals(SUFFIX_JPG) && !suffix.equals(SUFFIX_PNG)){
            return ResultGenerator.genFailResult(ResultCode.FILE_SUFFIX_ERROR);
        }

        //生成随机文件名,防止用户上传文件重名
        fileName = CommonUtils.generateUUID() + suffix;
        //确定文件存放的路径
        File dest = new File(uploadPath + "/" + fileName);
        try {
            //存储文件
            avatar.transferTo(dest);
        } catch (IOException e) {
            throw new RuntimeException("上传文件失败，服务器发生异常！", e);
        }
        //更新当前用户头像路径（web访问路径）
        //http://localhost:9002/consultant/consultantAvatar/xxx.png
        Consultant consultant = hostHolder.getConsultant();
        if (consultant == null) {
            return ResultGenerator.genFailResult(ResultCode.NOT_LOGGED_IN);
        }
        String avatarUrl = domain + "/consultant/consultantAvatar/" + fileName;
        //接收业务层响应码
        ResultCode code = consultantService.updateAvatar(consultant.getConsultantid(), avatarUrl);
        if (code.equals(ResultCode.SUCCESS)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(code);
        }

    }

    @ApiOperation("实名认证")
    @ResponseBody
    @PostMapping("/authentication")
    public Result authentication(@RequestBody Consultant consultant) {
        String name=consultant.getName();
        String idNumber=consultant.getIdnumber();
        //判空处理，如果参数为空，返回空值响应码
        if (StringUtils.isEmpty(name) || StringUtils.isEmpty(idNumber)) {
            return ResultGenerator.genFailResult(ResultCode.EMPTY_ARG);
        }
        Consultant hostHolderConsultant = hostHolder.getConsultant();
        //如果consultant对象为空，则返回未登录响应码
        if (hostHolderConsultant == null) {
            return ResultGenerator.genFailResult(ResultCode.NOT_LOGGED_IN);
        }
        //接收业务层响应码
        ResultCode code = consultantService.updateNameAndId(hostHolderConsultant.getConsultantid(), consultant.getName(), consultant.getIdnumber());
        if (code.equals(ResultCode.SUCCESS)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(code);
        }
    }


    @ApiOperation("基本信息加载")
    @ResponseBody
    @GetMapping("/loadConsultant")
    public Result loadConsultant() {
        Consultant consultant = hostHolder.getConsultant();
        //consultant不为空，则返回对象
        if (consultant != null) {
            //清空登录密码数据
            consultant.setPassword(null);
            consultant.setPasswordsalt(null);
            //清空支付密码数据
            consultant.setPaypassword(null);
            consultant.setPasswordsalt(null);
            return ResultGenerator.genSuccessResult(consultant);
        } else {
            return ResultGenerator.genFailResult(ResultCode.NOT_LOGGED_IN);
        }
    }


    @ApiOperation("设置支付密码")
    @ResponseBody
    @PostMapping("/setPayPassword")
    public Result setPayPassword(@RequestBody PasswordDTO passwordDTO) {
        Consultant consultant=hostHolder.getConsultant();
        if (consultant==null){
            return ResultGenerator.genFailResult(ResultCode.NOT_LOGGED_IN);
        }
        if (StringUtils.isNotEmpty(consultant.getPaypassword())){
            return ResultGenerator.genFailResult(ResultCode.UPDATE_PWD_ERROR);
        }

        String payPassword=passwordDTO.getPaypassword();
        //判空处理，空值返回参数空值响应码
        if (StringUtils.isEmpty(payPassword)) {
            return ResultGenerator.genFailResult(ResultCode.EMPTY_ARG);
        }
        //接受业务层响应码，判断是否为成功响应
        ResultCode code = consultantService.setPayPassword(consultant.getConsultantid(), payPassword);
        if (code.equals(ResultCode.SUCCESS)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(code);
        }
    }

    @ApiOperation("更新支付密码")
    @ResponseBody
    @PostMapping("/updatePayPassword")
    public Result updatePayPassword(@RequestBody PasswordDTO passwordDTO) {
        String oldPayPassword=passwordDTO.getOldpaypassword();
        String newPayPassword=passwordDTO.getNewpaypassword();
        //判空处理，空值返回参数空值响应码
        if (StringUtils.isEmpty(oldPayPassword) || StringUtils.isEmpty(newPayPassword)) {
            return ResultGenerator.genFailResult(ResultCode.EMPTY_ARG);
        }
        Consultant consultant = hostHolder.getConsultant();
        //对象判空，如果为空返回未登录响应码
        if (consultant == null) {
            return ResultGenerator.genFailResult(ResultCode.NOT_LOGGED_IN);
        }
        //接收业务层响应码，判断是否为成功响应
        ResultCode code = consultantService.updatePayPassword(consultant, oldPayPassword, newPayPassword);
        if (code.equals(ResultCode.SUCCESS)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(code);
        }
    }

    @ApiOperation("查看投顾持有产品")
    @ResponseBody
    @GetMapping("/loadProduct")
    public Result loadProduct(@RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "10") int pageSize){
        Consultant consultant=hostHolder.getConsultant();
        //判断对象是否为空，若空返回未登录的响应码
        if (consultant==null){
            return ResultGenerator.genFailResult(ResultCode.NOT_LOGGED_IN);
        }
        Map map=consultantService.selectProduct(pageNum,pageSize,consultant.getConsultantid());
        return ResultGenerator.genSuccessResult(map);
    }


    @ApiOperation("查看审批列表")
    @ResponseBody
    @GetMapping("/loadApprovalList")
    public Result loadApprovalList(@RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "10") int pageSize){
        Consultant consultant=hostHolder.getConsultant();
        //判断对象是否为空，若空返回未登录
        if (consultant==null){
            return ResultGenerator.genFailResult(ResultCode.NOT_LOGGED_IN);
        }
        Map map=consultantService.selectOrders(pageNum,pageSize,consultant.getConsultantid(),APPROVAL);
        return ResultGenerator.genSuccessResult(map);
    }

    @ApiOperation("买入审批确认")
    @ResponseBody
    @PostMapping("/acceptApproval")
    public Result acceptApproval(@RequestBody Order order){
        int status=order.getStatus();
        int id=order.getId();
        //判断是否登录
        Consultant consultant=hostHolder.getConsultant();
        if (consultant==null){
            return ResultGenerator.genFailResult(ResultCode.NOT_LOGGED_IN);
        }
        ResultCode code;
        //判断当前审批订单是买入还是卖出，并修改为对应的状态
        if (status==APPROVAL_BUY){
            code=consultantService.updateOrderStatus(id,WAIT_TO_BUY);
        }else{
            code=consultantService.updateOrderStatus(id,WAIT_TO_SELL);
        }
        if (code.equals(ResultCode.SUCCESS)){
            return ResultGenerator.genSuccessResult();
        }else {
            return ResultGenerator.genFailResult(code);
        }
    }


    @ApiOperation("交易列表加载")
    @ResponseBody
    @GetMapping("/loadTransactionList")
    public Result loadBuyList(@RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "10") int pageSize){
        Consultant consultant=hostHolder.getConsultant();
        //判断对象是否为空，若空返回未登录
        if (consultant==null){
            return ResultGenerator.genFailResult(ResultCode.NOT_LOGGED_IN);
        }
        Map map=consultantService.selectOrders(pageNum,pageSize,consultant.getConsultantid(),TRANSACTION);
        return ResultGenerator.genSuccessResult(map);
    }
}
