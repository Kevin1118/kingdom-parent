package com.kingdom.consultant.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.kingdom.commonUtils.CommonUtils;
import com.kingdom.consultant.service.ConsultantService;
import com.kingdom.dao.ConsultantMapper;
import com.kingdom.pojo.Consultant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


/**
 * @author : long
 * @date : 2020/6/20 12:48
 */
@Service
public class ConsultantServiceImpl implements ConsultantService {

    @Autowired
    private ConsultantMapper consultantMapper;

    @Override
    public int register(Consultant consultant){

        String salt=CommonUtils.generateUUID().substring(0,5);
        consultant.setCreateTime((int) (System.currentTimeMillis()/1000));
        consultant.setStatus(0);
        consultant.setPasswordSalt(salt);
        consultant.setPassword(CommonUtils.md5(consultant.getPassword()+salt));
        return consultantMapper.insertConsultant(consultant);
    }

    @Override
    public Map<String,Object> login(String phoneNumber, String password){
        Consultant consultant=consultantMapper.selectByPhoneNumber(phoneNumber);
        Map<String,Object> map=new HashMap<>(16);
        //检查账号是否存在
        if(consultant==null){
            map.put("loginErrorMessage","账号不存在");
            return map;
        }

        //检查账号状态
        if(consultant.getStatus()==1){
            map.put("loginErrorMessage","账号被停用");
            return map;
        }

        //检查密码
        //密码加salt进行md5加密
        password= CommonUtils.md5(password+consultant.getPasswordSalt());
        if (!consultant.getPassword().equals(password)){
            map.put("loginErrorMessage","密码错误");
            return map;
        }

        map.put("loginTicket","登录成功");

        return map;
    }
}
