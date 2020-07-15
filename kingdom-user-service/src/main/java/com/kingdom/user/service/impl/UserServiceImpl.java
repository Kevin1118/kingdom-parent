package com.kingdom.user.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.kingdom.commonutils.CommonUtils;
import com.kingdom.commonutils.RedisKeyUtil;
import com.kingdom.dao.UserMapper;
import com.kingdom.interfaceservice.user.UserService;
import com.kingdom.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

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
    private RedisTemplate redisTemplate;


    @Override
    public User selectUserById(Integer userid) {
        User user = getCache(userid);
        //如果缓存中没有，则调用缓存初始化方法从mysql读取
        if (user == null) {
            user = initCache(userid);
        }
        return user;
    }

    @Override
    public int registerUser(User user, IndependentAccount independentAccount) {
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

        independentAccount.setStatus("1");
        independentAccount.setIndependentbalance(0);
        independentAccount.setAccountamount(0);
        userMapper.addIndependentAccount(independentAccount);
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
        String redisKey = RedisKeyUtil.getTicketKey(ticket.getTicket());
        //将ticket存到redis数据库中
        redisTemplate.opsForValue().set(redisKey, ticket);
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

        String redisKey = RedisKeyUtil.getTicketKey(loginTicket);
        return (LoginTicket) redisTemplate.opsForValue().get(redisKey);
    }

    @Override
    public int setPayPasswordUser(int userid, String payPassword) {
        String salt=CommonUtils.generateUUID().substring(0, 5);
        payPassword=CommonUtils.md5(payPassword+salt);
        return userMapper.updatePayPassword(userid,payPassword,salt);
    }

    @Override
    public int topUpUser(Integer userid, double topUpMoney) {
        IndependentAccount independentAccount=userMapper.selectIndependetAccountById(userid);
        double oldIndependentBalance=independentAccount.getIndependentbalance();
        double newIndependentBalance=topUpMoney+oldIndependentBalance;
        return userMapper.updateIndependentBalance(independentAccount.getUserid(),newIndependentBalance);
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
    public int changePhoneNumberUser(int userid, String phoneNumber) {
        return userMapper.updatePhoneNumber(userid,phoneNumber);
    }

    @Override
    public int changeUserName(int userId, String userName) {
        return userMapper.updateUserName(userId,userName);
    }

    @Override
    public IndependentAccount selectAccountByUserId(int userId) {
        return userMapper.selectIndependetAccountById(userId);
    }

    @Override
    public int investUser(Order order,int userId,String name,double sum) {
        Product product=userMapper.selectProductByName(name);
        int productId=product.getProductid();
        int consultantId=product.getConsultantid();
        int transactionDate=(int)(System.currentTimeMillis()/1000);
        SignAccount sa=userMapper.selectAccountNoByUserIdAndProductId(userId,productId);
        String orderId=transactionDate+""+userId;
        int accountNo=sa.getSignaccountid();
        //账户不存在择创建投顾账户
        if (sa==null){
            sa.setUserid(userId);
            sa.setProductid(productId);
            sa.setBalance(sum);
            sa.setSigndate(transactionDate);
            //1代表可用
            sa.setStatus("1");
            userMapper.addSignAccount(sa);
            //账户存在则更新账户余额
        }else{
            double balance=sa.getBalance()+sum;
            userMapper.updateSignAccountBalance(accountNo,balance);
        }
        //更新独立账户资金
        IndependentAccount independentAccount=userMapper.selectIndependetAccountById(userId);
        double independentBalance=independentAccount.getIndependentbalance()-sum;
        userMapper.updateIndependentBalance(userId,independentBalance);

        order.setOrderid(orderId);
        order.setAccountno(accountNo);
        order.setSum(sum);
        order.setTransactiondate(transactionDate);
        order.setProductid(productId);
        order.setConsultantid(consultantId);
        //1表示买入申请
        order.setStatus(1);

        return userMapper.addOrder(order);
    }

    @Override
    public int sellUser(Order order, int userId, String name, double sum) {
        Product product=userMapper.selectProductByName(name);
        int productId=product.getProductid();
        int consultanId=product.getConsultantid();
        SignAccount signAccount=userMapper.selectAccountNoByUserIdAndProductId(userId,productId);
        int accountNo=signAccount.getSignaccountid();
        int transactionDate=(int)(System.currentTimeMillis()/1000);
        String orderId=transactionDate+""+userId;
        //更新投顾账户余额
        double balance=signAccount.getBalance()-sum;
        userMapper.updateSignAccountBalance(accountNo,balance);

        //更新独立账户余额 ,这里假定用户发起卖出申请，平台直接使用自身资金池转账给账户，不需要在投顾卖出后转账
        IndependentAccount independentAccount=userMapper.selectIndependetAccountById(userId);
        double independentBalance=independentAccount.getIndependentbalance()+sum;
        userMapper.updateIndependentBalance(userId,independentBalance);

        order.setOrderid(orderId);
        order.setAccountno(accountNo);
        order.setSum(sum);
        order.setTransactiondate(transactionDate);
        order.setProductid(productId);
        order.setConsultantid(consultanId);
        //2代表卖出申请
        order.setStatus(2);


        return userMapper.addOrder(order);
    }


    private User getCache(int userId) {
        String redisKey = RedisKeyUtil.getUserKey(userId);
        return (User) redisTemplate.opsForValue().get(redisKey);
    }


    /**
     * 缓存中没有投顾对象时初始化缓存对象
     *
     * @param userId 投顾人id
     * @return 投顾人对象
     */
    private User initCache(int userId) {
        User user = userMapper.selectUserById(userId);
        String redisKey = RedisKeyUtil.getUserKey(userId);
        redisTemplate.opsForValue().set(redisKey, user, 3600, TimeUnit.SECONDS);
        return user;
    }

    /**
     * 当数据变更时清除缓存数据
     *
     * @param userId 投顾人id
     */
    private void clearCache(int userId) {
        String redisKey = RedisKeyUtil.getUserKey(userId);
        redisTemplate.delete(redisKey);
    }

}
