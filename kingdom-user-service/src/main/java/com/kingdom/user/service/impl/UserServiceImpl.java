package com.kingdom.user.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.kingdom.commonutils.CommonUtils;
import com.kingdom.dao.LoginTicketMapper;
import com.kingdom.dao.UserMapper;
import com.kingdom.interfaceservice.user.UserService;
import com.kingdom.pojo.Card;
import com.kingdom.pojo.LoginTicket;
import com.kingdom.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * <h3>kingdom-parent</h3>
 * <p>UserDemoService的接口实现</p>
 *
 * @author : HuangJingChao
 * @date : 2020-06-17 11:21
 **/
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private LoginTicketMapper loginTicketMapper;

    @Override
    public User selectUserById(Integer userid) {

        User user = userMapper.selectUserById(userid);
        return user;
    }

    @Override
    public int registerUser(User user) {
        User u=userMapper.selectUserByPhoneNumber(user.getPhonenumber());
        if(u!=null){
            return 0;
        }
        String salt = CommonUtils.generateUUID().substring(0, 5);
        user.setSalt(salt);
        //设置时间
        user.setCreatedtime((int) (System.currentTimeMillis() / 1000));
        //设置默认状态为1，激活状态
        user.setStatus("1");
        //将密码和salt值拼接后进行md5加密
        user.setPassword(CommonUtils.md5(user.getPassword() + salt));
        //设置默认头像，使用牛客网默认头像
        user.setAvatar(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
        return userMapper.addUser(user);
    }
    @Override
    public Map<String, Object> loginUser(String phoneNumber, String password) {
        User user = userMapper.selectUserByPhoneNumber(phoneNumber);
        Map<String, Object> map = new HashMap<>(16);
        map.put("loginticket", null);
        //检查账号是否存在
        if (user == null) {
            map.put("loginerrormessage", "账号不存在");
            return map;
        }

        //检查账号状态
        if (user.getStatus() =="2") {
            map.put("loginerrormessage", "账号被停用");
            return map;
        }

        //检查密码
        //密码加salt进行md5加密
        password = CommonUtils.md5(password + user.getSalt());
        if (!user.getPassword().equals(password)) {
            map.put("loginerrormessage", "密码错误");
            return map;
        }
        LoginTicket ticket = new LoginTicket();
        ticket.setTicket(CommonUtils.generateUUID());
        ticket.setUserid(user.getUserid());
        ticket.setStatus(0);
        ticket.setExpired((int) (System.currentTimeMillis() / 1000 + 3600 * 12));
        loginTicketMapper.insertLoginTicket(ticket);
        map.put("loginticket", ticket.getTicket());

        return map;
    }

    @Override
    public int updateAvatarUser(int userId, String avatarUrl) {
        return userMapper.updateAvatar(userId,avatarUrl);
    }

    @Override
    public int bindCardUser(Card card){
        //设置当前时间为绑卡时间
        card.setCreatedtime((int)(System.currentTimeMillis()/1000));
        //设置状态1，代表激活成功
        card.setStatus("1");
        return userMapper.addCard(card);
    }



    @Override
    public LoginTicket findLoginTicket(String loginTicket) {

        return loginTicketMapper.selectByTicket(loginTicket);
    }

    @Override
    public int setPayPasswordUser(int userid, String payPassword) {
        String salt=CommonUtils.generateUUID().substring(0, 5);
        payPassword=CommonUtils.md5(payPassword+salt);
        return userMapper.updatePayPassword(userid,payPassword,salt);
    }

    @Override
    public int passwordManageUser(User user, String oldPassword, String newPassword) {
        if (CommonUtils.md5(oldPassword+user.getSalt()).equals(user.getPassword())){
            String salt=CommonUtils.generateUUID().substring(0,5);
            return userMapper.updatePassword(user.getUserid(),CommonUtils.md5(newPassword+salt),salt);
        }else{
            return -1;
        }
    }

    @Override
    public int certificationUser(User user, String name, String idNumber) {
        String approvalStatus="1";
        int approvalTime=(int)(System.currentTimeMillis()/1000);
        user.setApprovalstatus(approvalStatus);
        user.setApprovaltime(approvalTime);
        return userMapper.updateName(user.getUserid(),name,idNumber,approvalStatus,approvalTime);
    }

    @Override
    public int changePhoneNumberUser(User user, String phoneNumber) {
        return userMapper.updatePhoneNumber(user.getUserid(),phoneNumber);
    }

}
