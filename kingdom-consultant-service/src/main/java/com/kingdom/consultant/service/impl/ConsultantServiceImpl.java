package com.kingdom.consultant.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.kingdom.commonutils.CommonUtils;
import com.kingdom.dao.LoginTicketMapper;
import com.kingdom.interfaceservice.consultant.ConsultantService;
import com.kingdom.dao.ConsultantMapper;
import com.kingdom.pojo.Consultant;
import com.kingdom.pojo.HostHolder;
import com.kingdom.pojo.LoginTicket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;


/**
 * @author : long
 * @date : 2020/6/20 12:48
 */
@Service
public class ConsultantServiceImpl implements ConsultantService {

    @Autowired
    private ConsultantMapper consultantMapper;

    @Autowired
    private LoginTicketMapper loginTicketMapper;

    @Override
    public int register(Consultant consultant) {


        //判断邮箱是否被注册
        Consultant c = consultantMapper.selectByEmail(consultant.getEmail());
        if (c != null) {
            return 0;
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
        return result;
    }

    @Override
    public Map<String, Object> login(String email, String password) {
        Consultant consultant = consultantMapper.selectByEmail(email);
        Map<String, Object> map = new HashMap<>(16);
        map.put("loginticket", null);
        //检查账号是否存在
        if (consultant == null) {
            map.put("loginerrormessage", "账号不存在");
            return map;
        }

        //检查账号状态
        if (consultant.getStatus() == 1) {
            map.put("loginerrormessage", "账号被停用");
            return map;
        }

        //检查密码
        //密码加salt进行md5加密
        password = CommonUtils.md5(password + consultant.getPasswordsalt());
        if (!consultant.getPassword().equals(password)) {
            map.put("loginerrormessage", "密码错误");
            return map;
        }
        LoginTicket ticket = new LoginTicket();
        ticket.setTicket(CommonUtils.generateUUID());
        ticket.setUserid(consultant.getConsultantid());
        ticket.setStatus(0);
        ticket.setExpired((int) (System.currentTimeMillis() / 1000 + 3600 * 12));
        loginTicketMapper.insertLoginTicket(ticket);

        map.put("loginticket", ticket.getTicket());

        return map;
    }


    @Override
    public int updateAvatar(int consultantId, String avatarUrl) {
        return consultantMapper.updateAvatar(consultantId, avatarUrl);
    }


    @Override
    public LoginTicket findLoginTicket(String loginTicket) {
        return loginTicketMapper.selectByTicket(loginTicket);
    }

    @Override
    public Consultant findConsultantById(int consultantId) {
        return consultantMapper.selectById(consultantId);
    }

    @Override
    public int updateNameAndId(int consultantId, String name, String idNumber) {
        return consultantMapper.updateNameAndId(consultantId, name, idNumber);
    }

    @Override
    public int updatePayPassword(Consultant consultant,String oldPayPassword, String newPayPassword) {


        if (CommonUtils.md5(oldPayPassword + consultant.getPaypasswordsalt()).equals(consultant.getPaypassword())) {
            String salt = CommonUtils.generateUUID().substring(0, 5);
            return consultantMapper.updatePayPassword(consultant.getConsultantid(), CommonUtils.md5(newPayPassword + salt), salt);
        } else {
            return -1;
        }

    }

    @Override
    public int setPayPassword(int consultantId, String payPassword) {
        String salt = CommonUtils.generateUUID().substring(0, 5);
        payPassword=CommonUtils.md5(payPassword+salt);
        return consultantMapper.updatePayPassword(consultantId,payPassword,salt);
    }
}
