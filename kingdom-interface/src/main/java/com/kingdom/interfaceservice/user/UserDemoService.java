package com.kingdom.interfaceservice.user;

import com.kingdom.pojo.User;

/**
 * <h3>kingdom-parent</h3>
 * <p>UserDemo的测试接口</p>
 *
 * @author : HuangJingChao
 * @date : 2020-06-16 23:02
 **/
public interface UserDemoService {

    /**
     * 根据 userid 查询 user 表详情的接口 demo
     *
     * @param userid
     * @return User
     */
    User selectByIdDemo(Integer userid);

    /**
     * 投资人通过邮箱或手机号码进行登陆
     *
     * @Param
     * @return int
     */
    int loginUser(String email, String phoneNumber, String password);

}
