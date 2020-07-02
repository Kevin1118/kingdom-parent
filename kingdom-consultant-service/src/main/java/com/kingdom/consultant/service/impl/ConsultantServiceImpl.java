package com.kingdom.consultant.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.kingdom.commonutils.CommonUtils;
import com.kingdom.commonutils.Constant;
import com.kingdom.commonutils.RedisKeyUtil;
import com.kingdom.dao.OrderMapper;
import com.kingdom.dao.ProductMapper;
import com.kingdom.interfaceservice.consultant.ConsultantService;
import com.kingdom.dao.ConsultantMapper;
import com.kingdom.pojo.*;
import com.kingdom.result.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;


import java.util.*;
import java.util.concurrent.TimeUnit;


/**
 * @author : long
 * @date : 2020/6/20 12:48
 */
@Service
public class ConsultantServiceImpl implements ConsultantService, Constant {

    @Autowired
    private ConsultantMapper consultantMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private RedisTemplate redisTemplate;


    /**
     * 投顾人注册
     * @param consultant 投顾人信息
     * @return 响应码
     */
    @Override
    public ResultCode register(Consultant consultant) {


        //判断邮箱是否被注册
        Consultant c = consultantMapper.selectByEmail(consultant.getEmail());
        if (c != null) {
            return ResultCode.REGISTER_EMAIL_ERROR;
        }

        //生成密码salt值，取5位
        String salt = CommonUtils.generateUUID().substring(0, 5);
        consultant.setPasswordsalt(salt);
        //设置时间
        consultant.setCreatetime((int) (System.currentTimeMillis() / 1000));
        //设置默认状态为0，未激活
        consultant.setStatus(0);
        //生成激活码
        consultant.setActivationcode(CommonUtils.generateUUID());


        //将密码和salt值拼接后进行md5加密
        consultant.setPassword(CommonUtils.md5(consultant.getPassword() + salt));
        //设置默认头像，使用牛客网默认头像
        consultant.setAvatar(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
        //设置默认简介
        consultant.setDescription("一位神秘的投资精英");
        int result = consultantMapper.insertConsultant(consultant);
        //如果操作行数为1，则返回成功，否则返回数据库操作错误码
        if (result == 1) {
            return ResultCode.SUCCESS;
        } else {
            return ResultCode.MYSQL_CURD_ERROR;
        }
    }

    /**
     * 投顾人登录
     * @param email 邮箱号
     * @param password 密码
     * @return map格式数据，响应码和登录凭证
     */
    @Override
    public Map<String, Object> login(String email, String password) {
        //通过邮箱查询用户
        Consultant consultant = consultantMapper.selectByEmail(email);
        //map存储响应码和loginTicket
        Map<String, Object> map = new HashMap<>(2);

        //检查账号是否存在,账号状态
        if (consultant == null) {
            map.put("resultCode", ResultCode.LOGIN_EMAIL_ERROR);
            return map;
        }
        if (consultant.getStatus() == 1){
            map.put("resultCode",ResultCode.LOGIN_STATUS_ERROR);
            return map;
        }

        //检查密码
        //密码加salt进行md5加密
        password = CommonUtils.md5(password + consultant.getPasswordsalt());
        if (!consultant.getPassword().equals(password)) {
            map.put("resultCode", ResultCode.LOGIN_PWD_ERROR);
            return map;
        }
        //校验通过，生成登录凭证
        LoginTicket ticket = new LoginTicket();
        ticket.setTicket(CommonUtils.generateUUID());
        ticket.setUserid(consultant.getConsultantid());
        ticket.setStatus(0);
        //凭证有效期十二个小时
        ticket.setExpired((int) (System.currentTimeMillis() / 1000 + 3600 * 12));
        String redisKey = RedisKeyUtil.getTicketKey(ticket.getTicket());
        //将ticket存到redis数据库中
        redisTemplate.opsForValue().set(redisKey, ticket);
        //返回响应码数据和登录凭证
        map.put("resultCode", ResultCode.SUCCESS);
        map.put("loginTicket", ticket);
        return map;
    }


    /**
     * 更新头像
     * @param consultantId 投顾人id
     * @param avatarUrl 头像链接
     * @return 响应码
     */
    @Override
    public ResultCode updateAvatar(int consultantId, String avatarUrl) {
        int rows = consultantMapper.updateAvatar(consultantId, avatarUrl);
        if(rows==1){
            //如果操作行数为1，则清楚redis缓存，返回成功状态码
            clearCache(consultantId);
            return ResultCode.SUCCESS;
        }else {
            return ResultCode.MYSQL_CURD_ERROR;
        }
    }


    /**
     * 根据登录凭证查找凭证详细信息
     * @param loginTicket 登录凭证值
     * @return 登录凭证对象
     */
    @Override
    public LoginTicket findLoginTicket(String loginTicket) {
        //从redis中取loginTicket对象
        String redisKey = RedisKeyUtil.getTicketKey(loginTicket);
        return (LoginTicket) redisTemplate.opsForValue().get(redisKey);
    }

    /**
     * 根据投顾id查找投顾对象
     * @param consultantId 投顾id
     * @return 投顾对象
     */
    @Override
    public Consultant findConsultantById(int consultantId) {
        //先从redis缓存中查找对象
        Consultant consultant = getCache(consultantId);
        //如果缓存中没有，则调用缓存初始化方法从mysql读取
        if (consultant == null) {
            consultant = initCache(consultantId);
        }
        return consultant;
    }

    /**
     * 实名认证
     * @param consultantId 投顾人id
     * @param name 姓名
     * @param idNumber 身份证号
     * @return 响应码
     */
    @Override
    public ResultCode updateNameAndId(int consultantId, String name, String idNumber) {
        int rows = consultantMapper.updateNameAndId(consultantId, name, idNumber);
        //操作行数为1则清空redis缓存并返回成功响应码
        if(rows==1){
            clearCache(consultantId);
            return ResultCode.SUCCESS;
        }else {
            //返回数据库操作错误响应码
            return ResultCode.MYSQL_CURD_ERROR;
        }
    }

    /**
     * 更新支付密码
     * @param consultant 投顾人对象
     * @param oldPayPassword 旧支付密码
     * @param newPayPassword 新支付密码
     * @return 响应码
     */
    @Override
    public ResultCode updatePayPassword(Consultant consultant, String oldPayPassword, String newPayPassword) {
        //将旧密码与旧salt拼接后md5加密，与数据库中原加密后的密码比对
        if (CommonUtils.md5(oldPayPassword + consultant.getPaypasswordsalt()).equals(consultant.getPaypassword())) {
            //生成新的salt值
            String salt = CommonUtils.generateUUID().substring(0, 5);
            int rows = consultantMapper.updatePayPassword(consultant.getConsultantid(), CommonUtils.md5(newPayPassword + salt), salt);
            //更新数据库成功则清空redis并返回成功响应码，否则返回数据库操作失败
            if(rows==1){
                clearCache(consultant.getConsultantid());
                return ResultCode.SUCCESS;
            }else {
                return ResultCode.MYSQL_CURD_ERROR;
            }
        } else {
            //返回密码错误
            return ResultCode.UPDATE_PWD_ERROR;
        }

    }

    /**
     * 设置支付密码
     * @param consultantId 投顾人id
     * @param payPassword 支付密码
     * @return 响应码
     */
    @Override
    public ResultCode setPayPassword(int consultantId, String payPassword) {
        //生成salt值
        String salt = CommonUtils.generateUUID().substring(0, 5);
        //加密处理密码
        payPassword = CommonUtils.md5(payPassword + salt);
        int rows = consultantMapper.updatePayPassword(consultantId, payPassword, salt);
        if (rows==1){
            clearCache(consultantId);
            return ResultCode.SUCCESS;
        }else {
            return ResultCode.MYSQL_CURD_ERROR;
        }
    }


    /**
     * 查询产品
     * @param pageNum 页码
     * @param pageSize 分页大小
     * @param consultantId 投顾id
     * @return 产品列表
     */
    @Override
    public Map selectProduct(int pageNum, int pageSize, int consultantId) {
        //分页组件
        Page<Object> pageObject = PageHelper.startPage(pageNum, pageSize);
        //查询投顾人所属产品列表
        List<Product> selectProductList = productMapper.selectProductByConsultantId(consultantId);
        //产品统计列表
        List<HashMap> productCountList=new ArrayList<>();
        //查询出每个产品的统计信息
        for (Product product:selectProductList){
            int productId=product.getProductid();
            HashMap productCount=getProductCountCache(productId);
            if (productCount==null){
                productCount=initProductCache(productId);
            }
            productCountList.add(productCount);
        }
        Map map=new HashMap(3);
        map.put("total", pageObject.getTotal());
        map.put("data", selectProductList);
        map.put("count",productCountList);
        return map;
    }

    /**
     * 从redis缓存中取对象
     *
     * @param consultantId 投顾人id
     * @return 投顾人对象
     */
    private Consultant getCache(int consultantId) {
        String redisKey = RedisKeyUtil.getConsultantKey(consultantId);
        return (Consultant) redisTemplate.opsForValue().get(redisKey);
    }


    /**
     * 缓存中没有投顾对象时初始化缓存对象
     *
     * @param consultantId 投顾人id
     * @return 投顾人对象
     */
    private Consultant initCache(int consultantId) {
        Consultant consultant = consultantMapper.selectById(consultantId);
        String redisKey = RedisKeyUtil.getConsultantKey(consultantId);
        redisTemplate.opsForValue().set(redisKey, consultant, 3600, TimeUnit.SECONDS);
        return consultant;
    }

    /**
     * 当数据变更时清除缓存数据
     *
     * @param consultantId 投顾人id
     */
    private void clearCache(int consultantId) {
        String redisKey = RedisKeyUtil.getConsultantKey(consultantId);
        redisTemplate.delete(redisKey);
    }

    /**
     * 从缓存中获取product对象
     * @param productId productId
     */
    private HashMap getProductCountCache(int productId){
        String redisKey=RedisKeyUtil.getProductCountKey(productId);
        return (HashMap) redisTemplate.opsForValue().get(redisKey);
    }

    /**
     * 当天无购买时初始化0
     * @param productId
     * @return
     */
    private HashMap initProductCache(int productId){
        HashMap productCount=new HashMap(2);
        productCount.put("productId",productId);
        productCount.put("peopleCount",0);
        productCount.put("moneyCount",0);
        String redisKey=RedisKeyUtil.getProductCountKey(productId);
        redisTemplate.opsForValue().set(redisKey,productCount,CommonUtils.getSecondsNextEarlyMorning(),TimeUnit.SECONDS);
        return productCount;
    }


    /**
     * 查询订单
     * @param pageNum 页码
     * @param pageSize 分页大小
     * @param consultantId 投顾id
     * @return 订单审批列表
     */
    @Override
    public Map selectOrders(int pageNum,int pageSize,int consultantId,int type){
        //分页
        Page<Object> pageObject=PageHelper.startPage(pageNum,pageSize);
        List<Order> buyList;
        List<Order> sellList;
        Map map=new HashMap(3);
        if (type==APPROVAL){
            //查询买入审批列表
            buyList=orderMapper.selectOrderByConsultantIdAndStatus(consultantId,APPROVAL_BUY);
            //查询卖出审批列表
            sellList=orderMapper.selectOrderByConsultantIdAndStatus(consultantId,APPROVAL_SELL);
            map.put("buyApproval",buyList);
            map.put("sellApproval",sellList);

        }else{
            buyList=orderMapper.selectOrderByConsultantIdAndStatus(consultantId,WAIT_TO_BUY);
            sellList=orderMapper.selectOrderByConsultantIdAndStatus(consultantId,WAIT_TO_SELL);
            map.put("buyTransaction",buyList);
            map.put("sellTransaction",sellList);
        }

        return map;
    }


    /**
     * 更新订单状态
     * @param id 订单id
     * @param status 状态
     * @return
     */
    @Override
    public ResultCode updateOrderStatus(int id,int status) {
        int rows=orderMapper.updateOrderStatus(id,status);
        if (rows==1){
            return ResultCode.SUCCESS;
        }else {
            return ResultCode.MYSQL_CURD_ERROR;
        }
    }
}
