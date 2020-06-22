package com.kingdom.user.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.kingdom.dao.UserMapper;
import com.kingdom.interfaceservice.user.UserDemoService;
import com.kingdom.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <h3>kingdom-parent</h3>
 * <p>UserDemoService的接口实现</p>
 *
 * @author : HuangJingChao
 * @date : 2020-06-17 11:21
 **/
@Service
public class UserDemoServiceImpl implements UserDemoService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User selectByIdDemo(Integer userid) {

        User user = userMapper.selectByIdDemo(userid);
        return user;
    }

    @Override
    public int loginUser(String email, String phoneNumber, String password) {
        User user = userMapper.selectPasswordByEmailOrPhoneNumber(email, phoneNumber);
        String rightPassword = user.getPassword();
        if(rightPassword == null || rightPassword.isEmpty()){
            return -1;
        }else if(rightPassword.equals(password)){
            return 1;
        }else{
            return -1;
        }
    }


}
