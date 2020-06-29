package com.kingdom.dao;

import com.kingdom.pojo.User;
import org.apache.ibatis.annotations.Param;


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
     * @param userid
     * @return User
     */
    User selectByIdDemo(Integer userid);


    /**
     * 投资人注册功能，向数据库写进用户名，邮箱,手机号和密码
     * @param userId
     * @param userName
     * @param email
     * @param phoneNumber
     * @param password
     * @return int
     */
    int addUser(@Param("userid")Integer userId,@Param("username")String userName,@Param("email")String email,@Param("phonenumber")String phoneNumber,@Param("password")String password);

    /**
     * 投资人绑卡功能，将姓名和卡号写进数据库
     * @param cardId
     * @param realName
     * @param cardNumber
     */

    int addCard(@Param("cardid") Integer cardId,@Param("realname") String realName,@Param("cardnumber") String cardNumber);
    /**
     * 投资人登录功能，从数据库中查询密码
     * @param email
     * @param phoneNumber
     * @return User
     */
    User selectPasswordByEmailOrPhoneNumber(@Param("email")String email,@Param("phonenumber") String phoneNumber);

}
