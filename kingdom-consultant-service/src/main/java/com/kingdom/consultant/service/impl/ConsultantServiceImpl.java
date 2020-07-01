package com.kingdom.consultant.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.kingdom.commonutils.CommonUtils;
import com.kingdom.commonutils.RedisKeyUtil;
import com.kingdom.interfaceservice.consultant.ConsultantService;
import com.kingdom.dao.ConsultantMapper;
import com.kingdom.pojo.Consultant;
import com.kingdom.pojo.LoginTicket;
import com.kingdom.result.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;


/**
 * @author : long
 * @date : 2020/6/20 12:48
 */
@Service
public class ConsultantServiceImpl implements ConsultantService {

    @Autowired
    private ConsultantMapper consultantMapper;

    @Autowired
    private RedisTemplate redisTemplate;

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
        return ResultCode.SUCCESS;
    }

    @Override
    public Map<String,Object> login(String email, String password) {
        Consultant consultant = consultantMapper.selectByEmail(email);
        Map<String,Object> map=new HashMap<>(2);

        //检查账号是否存在,账号状态
        if (consultant == null || consultant.getStatus() == 1) {
            map.put("resultCode",ResultCode.LOGIN_EMAIL_ERROR);
            return map;
        }

        //检查密码
        //密码加salt进行md5加密
        password = CommonUtils.md5(password + consultant.getPasswordsalt());
        if (!consultant.getPassword().equals(password)) {
            map.put("resultCode",ResultCode.LOGIN_PWD_ERROR);
            return map;
        }
        LoginTicket ticket = new LoginTicket();
        ticket.setTicket(CommonUtils.generateUUID());
        ticket.setUserid(consultant.getConsultantid());
        ticket.setStatus(0);
        ticket.setExpired((int) (System.currentTimeMillis()/1000+3600*12));
        String redisKey=RedisKeyUtil.getTicketKey(ticket.getTicket());
        redisTemplate.opsForValue().set(redisKey,ticket);
        map.put("resultCode",ResultCode.SUCCESS);
        map.put("loginTicket",ticket);
        return map;
    }


    @Override
    public ResultCode updateAvatar(int consultantId, String avatarUrl) {
        int rows=consultantMapper.updateAvatar(consultantId,avatarUrl);
        clearCache(consultantId);
        return ResultCode.SUCCESS;
    }


    @Override
    public LoginTicket findLoginTicket(String loginTicket) {
        String redisKey=RedisKeyUtil.getTicketKey(loginTicket);
        return (LoginTicket) redisTemplate.opsForValue().get(redisKey);
    }

    @Override
    public Consultant findConsultantById(int consultantId) {
        Consultant consultant=getCache(consultantId);
        if(consultant==null){
            consultant=initCache(consultantId);
        }
        return consultant;
    }

    @Override
    public ResultCode updateNameAndId(int consultantId, String name, String idNumber) {
        int rows=consultantMapper.updateNameAndId(consultantId, name, idNumber);
        clearCache(consultantId);
        return ResultCode.SUCCESS;
    }

    @Override
    public int updatePayPassword(int consultantId,String oldPayPassword, String newPayPassword) {

        Consultant consultant=findConsultantById(consultantId);
        if (CommonUtils.md5(oldPayPassword + consultant.getPaypasswordsalt()).equals(consultant.getPaypassword())) {
            String salt = CommonUtils.generateUUID().substring(0, 5);
            int rows=consultantMapper.updatePayPassword(consultant.getConsultantid(), CommonUtils.md5(newPayPassword + salt), salt);
            clearCache(consultantId);
            return rows;
        } else {
            return -1;
        }

    }

    @Override
    public int setPayPassword(int consultantId, String payPassword) {
        String salt = CommonUtils.generateUUID().substring(0, 5);
        payPassword=CommonUtils.md5(payPassword+salt);
        int rows=consultantMapper.updatePayPassword(consultantId,payPassword,salt);
        clearCache(consultantId);
        return rows;
    }


    /**
     * 从redis缓存中取对象
     * @param consultantId 投顾人id
     * @return 投顾人对象
     */
    private Consultant getCache(int consultantId){
        String redisKey= RedisKeyUtil.getConsultantKey(consultantId);
        return (Consultant) redisTemplate.opsForValue().get(redisKey);
    }


    /**
     * 缓存中没有投顾对象时初始化缓存对象
     * @param consultantId 投顾人id
     * @return 投顾人对象
     */
    private Consultant initCache(int consultantId){
        Consultant consultant=consultantMapper.selectById(consultantId);
        String redisKey=RedisKeyUtil.getConsultantKey(consultantId);
        redisTemplate.opsForValue().set(redisKey,consultant,3600, TimeUnit.SECONDS);
        return consultant;
    }

    /**
     * 当数据变更时清除缓存数据
     * @param consultantId 投顾人id
     */
    private void clearCache(int consultantId){
        String redisKey=RedisKeyUtil.getConsultantKey(consultantId);
        redisTemplate.delete(redisKey);
    }
}
