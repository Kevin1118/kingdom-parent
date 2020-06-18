package com.kingdom.dao;

import com.kingdom.pojo.User;


/**
 * 测试 demo，接口文件，访问数据库
 * 接口名与xml文件中的 sql id标签对应
 *
 * @author HuangJingChao
 * @date : 2020-06-16 12:08
 */
public interface UserMapper {
    /**
     * 标注功能、简单说明业务逻辑
     * 如有必要，注释接口实现和调用的注意事项
     *
     * @param id
     * @return
     */
    User selectByIdDemo(Integer id);

}
