package com.kingdom.user.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.kingdom.commonutils.CommonUtils;
import com.kingdom.commonutils.RedisKeyUtil;
import com.kingdom.dao.ProductMapper;
import com.kingdom.dao.UserMapper;
import com.kingdom.dto.user.OrderDetailDTO;
import com.kingdom.dto.user.OrderDetailValueNowAllDTO;
import com.kingdom.dto.user.ReturnDetailDTO;
import com.kingdom.interfaceservice.user.UserService;
import com.kingdom.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.*;
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
    private ProductMapper productMapper;

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
        User u = userMapper.selectUserByPhoneNumber(user.getPhonenumber());
        if (u != null) {
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
        if (user.getStatus() == "2") {
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
        return userMapper.updateAvatar(userId, avatarUrl);
    }

    @Override
    public int bindCardUser(Card card, int userId) {
        card.setUserid(userId);
        //设置当前时间为绑卡时间
        card.setCreatedtime((int) (System.currentTimeMillis() / 1000));
        //设置状态1，代表激活成功
        card.setStatus("1");
        return userMapper.addCard(card);
    }

    /**
     * 查询银行卡接口
     * 使用userId，获取该Id绑定的卡号
     *
     * @param userId
     * @return List<Card> 包含所有产品信息的 list
     */
    @Override
    public List<Card> loadCardUser(int userId) {
        List<Card> list = userMapper.selectCardNumber(userId);
        return list;
    }


    @Override
    public LoginTicket findLoginTicket(String loginTicket) {

        String redisKey = RedisKeyUtil.getTicketKey(loginTicket);
        return (LoginTicket) redisTemplate.opsForValue().get(redisKey);
    }

    @Override
    public int setPayPasswordUser(int userid, String payPassword) {
        String salt = CommonUtils.generateUUID().substring(0, 5);
        payPassword = CommonUtils.md5(payPassword + salt);
        return userMapper.updatePayPassword(userid, payPassword, salt);
    }

    @Override
    public int topUpUser(Integer userid, double topUpMoney) {
        IndependentAccount independentAccount = userMapper.selectIndependetAccountById(userid);
        double oldIndependentBalance = independentAccount.getIndependentbalance();
        double newIndependentBalance = topUpMoney + oldIndependentBalance;
        return userMapper.updateIndependentBalance(independentAccount.getUserid(), newIndependentBalance);
    }

    @Override
    public int passwordManageUser(User user, String oldPassword, String newPassword) {
        if (CommonUtils.md5(oldPassword + user.getSalt()).equals(user.getPassword())) {
            String salt = CommonUtils.generateUUID().substring(0, 5);
            return userMapper.updatePassword(user.getUserid(), CommonUtils.md5(newPassword + salt), salt);
        } else {
            return -1;
        }
    }

    @Override
    public int certificationUser(User user, String name, String idNumber) {
        String approvalStatus = "1";
        int approvalTime = (int) (System.currentTimeMillis() / 1000);
        user.setApprovalstatus(approvalStatus);
        user.setApprovaltime(approvalTime);
        return userMapper.updateName(user.getUserid(), name, idNumber, approvalStatus, approvalTime);
    }

    @Override
    public int changePhoneNumberUser(int userid, String phoneNumber) {
        return userMapper.updatePhoneNumber(userid, phoneNumber);
    }

    @Override
    public int changeUserName(int userId, String userName) {
        return userMapper.updateUserName(userId, userName);
    }

    @Override
    public IndependentAccount selectAccountByUserId(int userId) {
        return userMapper.selectIndependetAccountById(userId);
    }

    @Override
    public int investUser(Order order, int userId, String name, double sum) {
        Product product = userMapper.selectProductByName(name);
        int productId = product.getProductid();
        int consultantId = product.getConsultantid();
        int transactionDate = (int) (System.currentTimeMillis() / 1000);
        SignAccount sa = userMapper.selectAccountNoByUserIdAndProductId(userId, productId);
        String orderId = transactionDate + "" + userId;
        int accountNo = sa.getSignaccountid();
        //账户不存在择创建投顾账户
        if (sa == null) {
            sa.setUserid(userId);
            sa.setProductid(productId);
            sa.setBalance(sum);
            sa.setSigndate(transactionDate);
            //1代表可用
            sa.setStatus("1");
            userMapper.addSignAccount(sa);
            //账户存在则更新账户余额
        } else {
            double balance = sa.getBalance() + sum;
            userMapper.updateSignAccountBalance(accountNo, balance);
        }
        //更新独立账户资金
        IndependentAccount independentAccount = userMapper.selectIndependetAccountById(userId);
        double independentBalance = independentAccount.getIndependentbalance() - sum;
        userMapper.updateIndependentBalance(userId, independentBalance);

        order.setOrderid(orderId);
        order.setUserid(userId);
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
    public int sellUser(Order order, int userId, String name, String percent) {
        Product product = userMapper.selectProductByName(name);
        int productId = product.getProductid();
        int consultanId = product.getConsultantid();
        SignAccount signAccount = userMapper.selectAccountNoByUserIdAndProductId(userId, productId);
        int accountNo = signAccount.getSignaccountid();
        int transactionDate = (int) (System.currentTimeMillis() / 1000);
        String orderId = transactionDate + "" + userId;
        //更新投顾账户余额
        double balance = signAccount.getBalance();
        userMapper.updateSignAccountBalance(accountNo, balance);

        //更新独立账户余额 ,这里假定用户发起卖出申请，平台直接使用自身资金池转账给账户，不需要在投顾卖出后转账
        IndependentAccount independentAccount = userMapper.selectIndependetAccountById(userId);
        double independentBalance = independentAccount.getIndependentbalance();
        userMapper.updateIndependentBalance(userId, independentBalance);

        order.setOrderid(orderId);
        order.setUserid(userId);
        order.setAccountno(accountNo);
        order.setPercent(percent);
        order.setTransactiondate(transactionDate);
        order.setProductid(productId);
        order.setConsultantid(consultanId);
        //2代表卖出申请
        order.setStatus(2);


        return userMapper.addOrder(order);
    }


    /**
     * 投资人查询收益详情
     * 首先在property表查询持有的资产详情 List 包含 orderId，持有份额 amount，唯一代码 code
     * 拿orderId 到order表查询买入的总金额sum，和产品productId，
     * 再分别到单一产品的 detail 表查询比例，计算各个单一产品的初始持仓金额
     * 最后分别到 alternate 表查询单一产品的当前市值 valueNow，计算收益返回给前端
     *
     * @param userId
     * @return List<ReturnDetailDTO>
     * @author HuangJingchao
     * @date 2020/7/17 19:48
     */
    @Override
    public List<ReturnDetailDTO> searchUserReturnDetail(Integer userId) {
        List<ReturnDetailDTO> list = new ArrayList<>(16);

        List<String> orderIdList = new ArrayList<>(16);

        List<Property> propertyList = userMapper.selectPropertyByUserId(userId);
        //map用来存储股票或基金代码以及对应的份额
        HashMap<String, Integer> map = new HashMap<String, Integer>(16);
        //Set用来对订单号去重
        HashSet<String> set = new HashSet<>(16);
        //

        for (Property p : propertyList) {
            map.put(p.getCode(), p.getAmount());
            set.add(p.getOrderid());
        }

        for (String s : set) {
            String orderId = s;
            Order order = userMapper.selectOrderByOrderId(orderId);
            double sum = order.getSum();
            Integer productId = order.getProductid();

            Product product = productMapper.selectProductById(productId);

            Integer stockAmount = product.getStockamount();
            Integer fundAmount = product.getFundamount();

            //查询出组合产品中 股票所占的份额
            List<ProductStockDetail> productStockDetailList = productMapper.selectStockProportionFromDetail(productId);
            for (ProductStockDetail psd : productStockDetailList) {
                ReturnDetailDTO dto = new ReturnDetailDTO();
                dto.setProductName(product.getName());
                dto.setType("股票");
                dto.setCode(psd.getStockCode());
                dto.setPropertyName(psd.getStockName());
                //持有份额
                dto.setAmount(map.get(dto.getCode()));

                //单一产品买入金额 = 下单金额 * 股票比例 * 单个股票的比例
                double buyInAmount = sum * stockAmount * 0.01 * psd.getProportion().doubleValue();
                //计算单一产品当前持仓
                StockAlternate stockValueNow = userMapper.selectValueNowByStockCode(psd.getStockCode());
                //持有份额 * 当前市值 = 当前持仓金额
                double d = dto.getAmount() * stockValueNow.getValueNow().doubleValue();


                //单一产品当前持仓金额
                dto.setAmountNow(d);

                //计算收益（可能为负）
                dto.setAmountOfReturnOne(d - buyInAmount);

                //计算收益率
                dto.setRateOfReturn(dto.getAmountOfReturnOne() / buyInAmount);

                list.add(dto);
            }

            //查询出组合产品中 基金所占的份额
            List<ProductFundDetail> ProductFundDetailList = productMapper.selectFundProportionFromDetail(productId);
            for (ProductFundDetail pfd : ProductFundDetailList) {
                ReturnDetailDTO dto = new ReturnDetailDTO();
                dto.setProductName(product.getName());
                dto.setType("基金");
                dto.setCode(pfd.getFundCode());
                dto.setPropertyName(pfd.getFundName());
                //持有份额
                dto.setAmount(map.get(pfd.getFundCode()));

                //单一产品买入金额 = 下单金额 * 股票比例 * 单个股票的比例
                double buyInAmount = sum * fundAmount * 0.01 * pfd.getProportion().doubleValue();

                //计算单一产品当前持仓
                FundAlternate fundValueNow = userMapper.selectValueNowByFundCode(pfd.getFundCode());

                //持有份额 * 当前市值 = 当前持仓金额
                double d = dto.getAmount() * fundValueNow.getValueNow().doubleValue();
                //单一产品当前持仓金额
                dto.setAmountNow(d);

                //计算收益（可能为负）
                dto.setAmountOfReturnOne(d - buyInAmount);

                //计算收益率
                dto.setRateOfReturn(dto.getAmountOfReturnOne() / buyInAmount);

                list.add(dto);
            }
        }
        return list;
    }

    @Override
    public List<OrderDetailDTO> searchUserOrderDetail(Integer userId) {
        List<OrderDetailDTO> list = new ArrayList<>(16);
        List<Property> propertyList = userMapper.selectPropertyByUserId(userId);
        //map用来存储股票或基金代码以及对应的份额
        HashMap<String, Integer> map = new HashMap<String, Integer>(16);
        //Set用来对订单号去重
        HashSet<String> set = new HashSet<>(16);

        for (Property p : propertyList) {
            map.put(p.getCode(), p.getAmount());
            set.add(p.getOrderid());
        }

        for (String s : set) {
            String orderId = s;
            Order order = userMapper.selectOrderByOrderId(orderId);
            double sum = order.getSum();
            Integer productId = order.getProductid();

            Product product = productMapper.selectProductById(productId);

            Integer stockAmount = product.getStockamount();
            Integer fundAmount = product.getFundamount();

            //查询出组合产品中 股票所占的份额
            List<ProductStockDetail> productStockDetailList = productMapper.selectStockProportionFromDetail(productId);
            for (ProductStockDetail psd : productStockDetailList) {
                OrderDetailDTO dto = new OrderDetailDTO();
                dto.setProductName(product.getName());
                dto.setType("股票");
                dto.setCode(psd.getStockCode());
                dto.setPropertyName(psd.getStockName());
                //单一产品买入金额 = 下单金额 * 股票比例 * 单个股票的比例
                double buyInAmount = sum * stockAmount * 0.01 * psd.getProportion().doubleValue();
                dto.setAmount(buyInAmount);
                dto.setDate(order.getTransactiondate());
                dto.setStatus(order.getStatus());

                list.add(dto);
            }
            //查询出组合产品中 基金所占的份额
            List<ProductFundDetail> ProductFundDetailList = productMapper.selectFundProportionFromDetail(productId);
            for (ProductFundDetail pfd : ProductFundDetailList) {
                OrderDetailDTO dto = new OrderDetailDTO();
                dto.setProductName(product.getName());
                dto.setType("基金");
                dto.setCode(pfd.getFundCode());
                dto.setPropertyName(pfd.getFundName());

                //单一产品买入金额 = 下单金额 * 股票比例 * 单个股票的比例
                double buyInAmount = sum * fundAmount * 0.01 * pfd.getProportion().doubleValue();
                dto.setAmount(buyInAmount);
                dto.setDate(order.getTransactiondate());
                dto.setStatus(order.getStatus());

                list.add(dto);
            }
        }
        return list;
    }

    @Override
    public OrderDetailValueNowAllDTO searchValueNowAll(Integer userId) {
        OrderDetailValueNowAllDTO result = new OrderDetailValueNowAllDTO();
        List<ReturnDetailDTO> returnDetailDTOList = searchUserReturnDetail(userId);
        double s = 0;
        double f = 0;

        for (ReturnDetailDTO dto : returnDetailDTOList) {
            if (dto.getType().equals("股票")) {
                s += dto.getAmountNow();
            } else if (dto.getType().equals("基金")) {
                f += dto.getAmountNow();
            }
        }

        result.setStockValueNowAll(s);
        result.setFundValueNowAll(f);

        return result;
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
