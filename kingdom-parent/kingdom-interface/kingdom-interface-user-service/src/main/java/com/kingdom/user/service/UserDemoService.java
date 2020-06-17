package com.kingdom.user.service;

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
     * 根据 id 查询 user 表详情的接口 demo
     *
     * @param id
     * @return Result
     */
    User selectByIdDemo(Integer id);

}
