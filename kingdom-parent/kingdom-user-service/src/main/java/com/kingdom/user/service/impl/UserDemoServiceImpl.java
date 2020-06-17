package com.kingdom.user.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.kingdom.dao.UserMapper;
import com.kingdom.pojo.User;
import com.kingdom.user.service.UserDemoService;
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
    public User selectByIdDemo(Integer id) {
        User user = userMapper.selectByIdDemo(id);
        return user;
    }


}
