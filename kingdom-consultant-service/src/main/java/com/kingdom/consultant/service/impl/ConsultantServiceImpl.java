package com.kingdom.consultant.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.kingdom.commonutils.CommonUtils;
import com.kingdom.commonutils.Constant;
import com.kingdom.commonutils.RedisKeyUtil;
import com.kingdom.dao.*;
import com.kingdom.interfaceservice.consultant.ConsultantService;
import com.kingdom.pojo.*;
import com.kingdom.result.ResultCode;
import com.kingdom.vojo.product.OrderVo;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;


import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;


/**
 * @author : long
 * @date : 2020/6/20 12:48
 */
@Service
public class ConsultantServiceImpl implements ConsultantService, Constant {

    @Autowired
    private ConsultantMapper consultantMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private PropertyMapper propertyMapper;

    @Autowired
    private UserMapper userMapper;


    /**
     * 投顾人注册
     *
     * @param consultant 投顾人信息
     * @return 响应码
     */
    @Override
    public ResultCode register(Consultant consultant) {


        //判断邮箱是否被注册
        Consultant c = consultantMapper.selectByEmail(consultant.getEmail());
        if (c != null) {
            return ResultCode.REGISTER_EMAIL_ERROR;
        }

        //生成密码salt值，取5位
        String salt = CommonUtils.generateUUID().substring(0, 5);
        consultant.setPasswordsalt(salt);
        //设置时间
        consultant.setCreatetime((int) (System.currentTimeMillis() / 1000));
        //设置默认状态为0，未激活
        consultant.setStatus(0);
        //生成激活码
        consultant.setActivationcode(CommonUtils.generateUUID());


        //将密码和salt值拼接后进行md5加密
        consultant.setPassword(CommonUtils.md5(consultant.getPassword() + salt));
        //设置默认头像，使用牛客网默认头像
        consultant.setAvatar(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
        //设置默认简介
        consultant.setDescription("一位神秘的投资精英");
        int result = consultantMapper.insertConsultant(consultant);
        //如果操作行数为1，则返回成功，否则返回数据库操作错误码
        if (result == 1) {
            return ResultCode.SUCCESS;
        } else {
            return ResultCode.MYSQL_CURD_ERROR;
        }
    }

    /**
     * 投顾人登录
     *
     * @param email    邮箱号
     * @param password 密码
     * @return map格式数据，响应码和登录凭证
     */
    @Override
    public Map<String, Object> login(String email, String password) {
        //通过邮箱查询用户
        Consultant consultant = consultantMapper.selectByEmail(email);
        //map存储响应码和loginTicket
        Map<String, Object> map = new HashMap<>(2);

        //检查账号是否存在,账号状态
        if (consultant == null) {
            map.put("resultCode", ResultCode.LOGIN_EMAIL_ERROR);
            return map;
        }
        if (consultant.getStatus() == 1) {
            map.put("resultCode", ResultCode.LOGIN_STATUS_ERROR);
            return map;
        }

        //检查密码
        //密码加salt进行md5加密
        password = CommonUtils.md5(password + consultant.getPasswordsalt());
        if (!consultant.getPassword().equals(password)) {
            map.put("resultCode", ResultCode.LOGIN_PWD_ERROR);
            return map;
        }
        //校验通过，生成登录凭证
        LoginTicket ticket = new LoginTicket();
        ticket.setTicket(CommonUtils.generateUUID());
        ticket.setUserid(consultant.getConsultantid());
        ticket.setStatus(0);
        //凭证有效期十二个小时
        ticket.setExpired((int) (System.currentTimeMillis() / 1000 + 3600 * 12));
        String redisKey = RedisKeyUtil.getTicketKey(ticket.getTicket());
        //将ticket存到redis数据库中
        redisTemplate.opsForValue().set(redisKey, ticket);
        //返回响应码数据和登录凭证
        map.put("resultCode", ResultCode.SUCCESS);
        map.put("loginTicket", ticket);
        return map;
    }


    /**
     * 更新头像
     *
     * @param consultantId 投顾人id
     * @param avatarUrl    头像链接
     * @return 响应码
     */
    @Override
    public ResultCode updateAvatar(int consultantId, String avatarUrl) {
        int rows = consultantMapper.updateAvatar(consultantId, avatarUrl);
        if (rows == 1) {
            //如果操作行数为1，则清除redis缓存，返回成功状态码
            clearCache(consultantId);
            return ResultCode.SUCCESS;
        } else {
            return ResultCode.MYSQL_CURD_ERROR;
        }
    }


    /**
     * 根据登录凭证查找凭证详细信息
     *
     * @param loginTicket 登录凭证值
     * @return 登录凭证对象
     */
    @Override
    public LoginTicket findLoginTicket(String loginTicket) {
        //从redis中取loginTicket对象
        String redisKey = RedisKeyUtil.getTicketKey(loginTicket);
        return (LoginTicket) redisTemplate.opsForValue().get(redisKey);
    }

    /**
     * 根据投顾id查找投顾对象
     *
     * @param consultantId 投顾id
     * @return 投顾对象
     */
    @Override
    public Consultant findConsultantById(int consultantId) {
        //先从redis缓存中查找对象
        Consultant consultant = getCache(consultantId);
        //如果缓存中没有，则调用缓存初始化方法从mysql读取
        if (consultant == null) {
            consultant = initCache(consultantId);
        }
        return consultant;
    }

    /**
     * 实名认证
     *
     * @param consultantId 投顾人id
     * @param name         姓名
     * @param idNumber     身份证号
     * @return 响应码
     */
    @Override
    public ResultCode updateNameAndId(int consultantId, String name, String idNumber) {
        int rows = consultantMapper.updateNameAndId(consultantId, name, idNumber);
        //操作行数为1则清空redis缓存并返回成功响应码
        if (rows == 1) {
            clearCache(consultantId);
            return ResultCode.SUCCESS;
        } else {
            //返回数据库操作错误响应码
            return ResultCode.MYSQL_CURD_ERROR;
        }
    }

    /**
     * 更新支付密码
     *
     * @param consultant     投顾人对象
     * @param oldPayPassword 旧支付密码
     * @param newPayPassword 新支付密码
     * @return 响应码
     */
    @Override
    public ResultCode updatePayPassword(Consultant consultant, String oldPayPassword, String newPayPassword) {
        //将旧密码与旧salt拼接后md5加密，与数据库中原加密后的密码比对
        if (CommonUtils.md5(oldPayPassword + consultant.getPaypasswordsalt()).equals(consultant.getPaypassword())) {
            //生成新的salt值
            String salt = CommonUtils.generateUUID().substring(0, 5);
            int rows = consultantMapper.updatePayPassword(consultant.getConsultantid(), CommonUtils.md5(newPayPassword + salt), salt);
            //更新数据库成功则清空redis并返回成功响应码，否则返回数据库操作失败
            if (rows == 1) {
                clearCache(consultant.getConsultantid());
                return ResultCode.SUCCESS;
            } else {
                return ResultCode.MYSQL_CURD_ERROR;
            }
        } else {
            //返回密码错误
            return ResultCode.UPDATE_PWD_ERROR;
        }

    }

    /**
     * 设置支付密码
     *
     * @param consultantId 投顾人id
     * @param payPassword  支付密码
     * @return 响应码
     */
    @Override
    public ResultCode setPayPassword(int consultantId, String payPassword) {
        //生成salt值
        String salt = CommonUtils.generateUUID().substring(0, 5);
        //加密处理密码
        payPassword = CommonUtils.md5(payPassword + salt);
        int rows = consultantMapper.updatePayPassword(consultantId, payPassword, salt);
        if (rows == 1) {
            clearCache(consultantId);
            return ResultCode.SUCCESS;
        } else {
            return ResultCode.MYSQL_CURD_ERROR;
        }
    }


    /**
     * 查询产品
     *
     * @param pageNum      页码
     * @param pageSize     分页大小
     * @param consultantId 投顾id
     * @return 产品列表
     */
    @Override
    public Map selectProduct(int pageNum, int pageSize, int consultantId) {
        //分页组件
        Page<Object> pageObject = PageHelper.startPage(pageNum, pageSize);
        //查询投顾人所属产品列表
        List<Product> selectProductList = productMapper.selectProductByConsultantId(consultantId);
        //产品统计列表
        List<HashMap> productCountList = new ArrayList<>();
        //查询出每个产品的统计信息
        for (Product product : selectProductList) {
            int productId = product.getProductid();
            HashMap productRedis = getProductCache(productId);
            if (productRedis == null) {
                productRedis = initProductCache(productId);
            }
            productCountList.add(productRedis);
        }
        Map map = new HashMap(3);
        map.put("total", pageObject.getTotal());
        map.put("data", selectProductList);
        map.put("count", productCountList);
        return map;
    }

    /**
     * 从redis缓存中取对象
     *
     * @param consultantId 投顾人id
     * @return 投顾人对象
     */
    private Consultant getCache(int consultantId) {
        String redisKey = RedisKeyUtil.getConsultantKey(consultantId);
        return (Consultant) redisTemplate.opsForValue().get(redisKey);
    }


    /**
     * 缓存中没有投顾对象时初始化缓存对象
     *
     * @param consultantId 投顾人id
     * @return 投顾人对象
     */
    private Consultant initCache(int consultantId) {
        Consultant consultant = consultantMapper.selectById(consultantId);
        String redisKey = RedisKeyUtil.getConsultantKey(consultantId);
        redisTemplate.opsForValue().set(redisKey, consultant, 3600, TimeUnit.SECONDS);
        return consultant;
    }

    /**
     * 当数据变更时清除缓存数据
     *
     * @param consultantId 投顾人id
     */
    private void clearCache(int consultantId) {
        String redisKey = RedisKeyUtil.getConsultantKey(consultantId);
        redisTemplate.delete(redisKey);
    }

    /**
     * 从缓存中获取product对象
     *
     * @param productId productId
     */
    private HashMap getProductCache(int productId) {
        String redisKey = RedisKeyUtil.getProductKey(productId);
        return (HashMap) redisTemplate.opsForValue().get(redisKey);
    }

    /**
     * 当天无购买时初始化0
     *
     * @param productId
     * @return
     */
    private HashMap initProductCache(int productId) {
        Product productMysql = productMapper.selectProductById(productId);
        HashMap product = new HashMap(16);
        product.put("productId", productId);
        product.put("productName", productMysql.getName());
        product.put("stockAmount", productMysql.getStockamount());
        product.put("fundAmount", productMysql.getFundamount());
        product.put("expectedYield", productMysql.getExpectedYield());
        product.put("peopleCount", 0);
        product.put("moneyCount", 0);
        String redisKey = RedisKeyUtil.getProductKey(productId);
        redisTemplate.opsForValue().set(redisKey, product);
        return product;
    }


    /**
     * 查询订单
     *
     * @param pageNum      页码
     * @param pageSize     分页大小
     * @param consultantId 投顾id
     * @return 订单审批列表
     */
    @Override
    public Map selectOrders(int pageNum, int pageSize, int consultantId, int type) {
        //分页
        Page<Object> pageObject = PageHelper.startPage(pageNum, pageSize);
        List<Order> buyList;
        List<Order> sellList;
        Map map = new HashMap(3);
        if (type == APPROVAL) {
            //查询买入审批列表
            buyList = orderMapper.selectOrderByConsultantIdAndStatus(consultantId, APPROVAL_BUY);
            List<OrderVo> buyListVo=new ArrayList<>();
            for (Order order:buyList){
                HashMap product=getProductCache(order.getProductid());
                String productName=product.get("productName").toString();
                BigDecimal expected= (BigDecimal) product.get("expectedYield");
                float expectedYield= (float) (order.getSum()*expected.floatValue()*0.01f);

                OrderVo orderVo=new OrderVo();
                orderVo.setId(order.getId());
                orderVo.setOrderid(order.getOrderid());
                orderVo.setProductid(order.getProductid());
                orderVo.setSum((float) order.getSum());
                orderVo.setProductname(productName);
                orderVo.setExpectedyield(expectedYield);
                orderVo.setStatus(order.getStatus());
                orderVo.setTransactiondate(order.getTransactiondate());
                buyListVo.add(orderVo);
            }
            //查询卖出审批列表
            sellList = orderMapper.selectOrderByConsultantIdAndStatus(consultantId, APPROVAL_SELL);
            List<OrderVo> sellListVo=new ArrayList<>();
            for (Order order:sellList){
                HashMap product=getProductCache(order.getProductid());
                String productName=product.get("productName").toString();
                BigDecimal expected= (BigDecimal) product.get("expectedYield");
                float expectedYield= (float) (order.getSum()*expected.floatValue()*0.01f);
                OrderVo orderVo=new OrderVo();
                orderVo.setId(order.getId());
                orderVo.setOrderid(order.getOrderid());
                orderVo.setProductid(order.getProductid());
                orderVo.setSum((float) order.getSum());
                orderVo.setProductname(productName);
                orderVo.setExpectedyield(expectedYield);
                orderVo.setStatus(order.getStatus());
                orderVo.setTransactiondate(order.getTransactiondate());
                sellListVo.add(orderVo);
            }
            map.put("buyApproval", buyListVo);
            map.put("sellApproval", sellListVo);

        } else {
            buyList = orderMapper.selectOrderByConsultantIdAndStatus(consultantId, WAIT_TO_BUY);
            List<OrderVo> buyTransactionListVo=new ArrayList<>();
            for (Order order:buyList){
                HashMap product=getProductCache(order.getProductid());
                String productName=product.get("productName").toString();
                BigDecimal expected= (BigDecimal) product.get("expectedYield");
                float expectedYield= (float) (order.getSum()*expected.floatValue()*0.01f);
                OrderVo orderVo=new OrderVo();
                orderVo.setId(order.getId());
                orderVo.setOrderid(order.getOrderid());
                orderVo.setProductid(order.getProductid());
                orderVo.setSum((float) order.getSum());
                orderVo.setProductname(productName);
                orderVo.setExpectedyield(expectedYield);
                orderVo.setStatus(order.getStatus());
                orderVo.setTransactiondate(order.getTransactiondate());
                buyTransactionListVo.add(orderVo);
            }
            sellList = orderMapper.selectOrderByConsultantIdAndStatus(consultantId, WAIT_TO_SELL);
            List<OrderVo> sellTransactionListVo=new ArrayList<>();
            for (Order order:sellList){
                HashMap product=getProductCache(order.getProductid());
                String productName=product.get("productName").toString();
                BigDecimal expected= (BigDecimal) product.get("expectedYield");
                float expectedYield= (float) (order.getSum()*expected.floatValue()*0.01f);
                OrderVo orderVo=new OrderVo();
                orderVo.setId(order.getId());
                orderVo.setOrderid(order.getOrderid());
                orderVo.setProductid(order.getProductid());
                orderVo.setSum((float) order.getSum());
                orderVo.setProductname(productName);
                orderVo.setExpectedyield(expectedYield);
                orderVo.setStatus(order.getStatus());
                orderVo.setTransactiondate(order.getTransactiondate());
                sellTransactionListVo.add(orderVo);
            }
            map.put("buyTransaction", buyTransactionListVo);
            map.put("sellTransaction", sellTransactionListVo);
        }

        return map;
    }


    /**
     * 更新订单状态
     *
     * @param id     订单id
     * @param status 状态
     * @return
     */
    @Override
    public ResultCode updateOrderStatus(int id, int status, int productId, float sum) {
        //判断当前审批订单是买入还是卖出，并修改为对应的状态
        int rows;
        if (status == APPROVAL_BUY) {
            rows = orderMapper.updateOrderStatus(id, WAIT_TO_BUY);
            HashMap product = getProductCache(productId);
            if (product != null) {
                product = initProductCache(productId);
                int peopleCount = (int) product.get("peopleCount");
                int moneyCount = (int) product.get("moneyCount");
                product.put("peopleCount", peopleCount + 1);
                product.put("moneyCount", moneyCount + sum);
                redisTemplate.opsForValue().set(RedisKeyUtil.getProductKey(productId), product);
            }
        } else {
            rows = orderMapper.updateOrderStatus(id, WAIT_TO_SELL);
        }
        if (rows == 1) {
            return ResultCode.SUCCESS;
        } else {
            return ResultCode.MYSQL_CURD_ERROR;
        }
    }

    /**
     * 买入基金和股票
     * @param ids 订单
     * @return 响应码
     */
    @Override
    public ResultCode buyStockAndFund(List<Integer> ids) {
        //查出所有订单详情。
        List<Order> orders = orderMapper.selectByIds(ids);
        for (Order order : orders) {
            if (order.getStatus() != WAIT_TO_BUY) {
                continue;
            }
            float oriSum = (float) order.getSum();
            //查询每个订单对应的产品
            Product product = productMapper.selectProductById(order.getProductid());
            //查询每个订单对应的基金和股票明细
            List<ProductStockDetail> stockList = productMapper.selectStockProportionFromDetail(order.getProductid());
            List<ProductFundDetail> fundList = productMapper.selectFundProportionFromDetail(order.getProductid());
//            获取所有股票的代码,并获取股票当前价格
            List<String> stockCodes = new ArrayList<>();
            for (ProductStockDetail detail : stockList) {
                stockCodes.add(detail.getStockCode());
            }
            //如果不为空，查询股票价格
            if (!stockCodes.isEmpty()) {
                List<StockAlternate> stockPrices = productMapper.selectStockAlternate(stockCodes);
                for (int i = 0; i < stockList.size(); i++) {
                    float sum = oriSum * product.getStockamount() * stockList.get(i).getProportion().intValue() * 0.0001f;
                    float price = stockPrices.get(i).getValueNow().floatValue() * STOCK_AMOUNT_LIMIT;
                    if (sum >= price) {
                        int amount = Math.round(sum / price);
                        order.setSum(order.getSum() - amount * price / STOCK_AMOUNT_LIMIT);
                        Property property = new Property();
                        property.setSignaccountid(order.getAccountno());
                        property.setOrderid(order.getOrderid());
                        property.setType("股票");
                        property.setCode(stockList.get(i).getStockCode());
                        property.setPropertyname(stockList.get(i).getStockName());
                        property.setAmount(amount);
                        property.setUpdatetime((int) (System.currentTimeMillis() / 1000));
                        property.setStatus(0);
                        propertyMapper.insertProperty(property);

                    }


                }
            }

            //获取所有基金的代码，并获取基金当前价格
            List<String> fundCodes = new ArrayList<>();
            for (ProductFundDetail detail : fundList) {
                fundCodes.add(detail.getFundCode());
            }
            List<FundAlternate> fundPrices;
            //如果不为空，查询基金价格
            if (!fundCodes.isEmpty()) {
                fundCodes.add(MONEY_FUND_CODE);
                fundPrices = productMapper.selectFundAlternate(fundCodes);
                for (int i = 0; i < fundList.size(); i++) {
                    float sum = oriSum * product.getFundamount() * fundList.get(i).getProportion().floatValue() * 0.0001f;
                    float price = fundPrices.get(i).getValueNow().floatValue() * FUND_AMOUNT_LIMIT;
                    if (sum >= price) {
                        int amount = Math.round(sum / price);
                        order.setSum(order.getSum() - amount * price / FUND_AMOUNT_LIMIT);
                        Property property = new Property();
                        property.setSignaccountid(order.getAccountno());
                        property.setOrderid(order.getOrderid());
                        property.setType("基金");
                        property.setCode(fundList.get(i).getFundCode());
                        property.setPropertyname(fundList.get(i).getFundName());
                        property.setAmount(amount);
                        property.setUpdatetime((int) (System.currentTimeMillis() / 1000));
                        property.setStatus(0);
                        propertyMapper.insertProperty(property);
                    }

                }
            } else {
                fundCodes.add(MONEY_FUND_CODE);
                fundPrices = productMapper.selectFundAlternate(fundCodes);
            }

            //剩余金额购买货币基金
            if (order.getSum() > 0) {
                int amount = (int) Math.round(order.getSum() / fundPrices.get(fundPrices.size() - 1).getValueNow().floatValue());
                Property property = new Property();
                property.setSignaccountid(order.getAccountno());
                property.setOrderid(order.getOrderid());
                property.setType("货币市场型");
                property.setCode(MONEY_FUND_CODE);
                property.setPropertyname(MONEY_FUND_NAME);
                property.setAmount(amount);
                property.setUpdatetime((int) (System.currentTimeMillis() / 1000));
                property.setStatus(0);
                propertyMapper.insertProperty(property);
            }


            orderMapper.updateOrderStatus(order.getId(), FINISH);
        }

        return ResultCode.SUCCESS;
    }


    /**
     * 查询交易记录
     * @param pageNum 页码
     * @param pageSize 大小
     * @param orderId 订单号
     * @param consultantId 投顾人id
     * @return 交易记录列表
     */
    @Override
    public Map selectProperty(int pageNum, int pageSize, String orderId, int consultantId) {
        Map<String, List<Property>> map = new HashMap<>(16);
        if ("0".equals(orderId)) {
            List<Order> orders = orderMapper.selectOrderByConsultantIdAndStatus(consultantId, FINISH);

            if (orders.size() > 0) {

                for (Order order : orders) {
                    List<Property> properties = propertyMapper.loadProperty(order.getOrderid());
                    map.put(order.getOrderid(), properties);
                }
            }

        } else {
            List<Property> properties = propertyMapper.loadProperty(orderId);
        }
        return map;
    }


    @Override
    public Map selectRiskList(int consultantId) {
        Map<String,Object> result=new HashMap<>(4);
        List<StockAlternate> resultList=new ArrayList<>();
        //查询投顾人所属产品列表
        List<Product> selectProductList = productMapper.selectProductByConsultantId(consultantId);
        for (Product product:selectProductList){
            List<ProductStockDetail> stockList = productMapper.selectStockProportionFromDetail(product.getProductid());
            List<String> codes=new ArrayList<>();
            for (ProductStockDetail detail:stockList){
                codes.add(detail.getStockCode());
            }
            List<StockAlternate> alternates=productMapper.selectStockAlternate(codes);
            for (StockAlternate alternate:alternates){
                if (alternate.getUpAndDown().intValue()<ADJUST_THRESHOLD){
                    resultList.add(alternate);
                }
            }
        }
        result.put("adjustList",resultList);
        result.put("adjustThreshold",ADJUST_THRESHOLD);
        return result;
    }

    @Override
    public Map selectProductAndRatio(int consultanId) {
        Map product=new HashMap();
        List<Product> lp=productMapper.selectProductByConsultantId(consultanId);
        int productId=0;
        String name=null;
        int skAmount=0;
        int fdAmount=0;
        for(int i=0;i<lp.size();i++){
            Map content=new HashMap();
            Map content1=new HashMap();
            Map content2=new HashMap();
            productId= lp.get(i).getProductid();
            name=lp.get(i).getName();
            fdAmount=lp.get(i).getFundamount();
            skAmount=lp.get(i).getStockamount();
            List<ProductFundDetail> lpfd=productMapper.selectFundFromDetail(productId);
            String fdName=null;
            String fdCode=null;
            Double fdProportion;
            for (int j=0;j<lpfd.size();j++){
                fdName=lpfd.get(j).getFundName();
                fdCode=lpfd.get(j).getFundCode();
                fdProportion=lpfd.get(j).getProportion().doubleValue()*fdAmount*0.01;
                content1.put(fdName,fdCode+",    "+fdProportion);
            }
            content.put("基金",content1);
            List<ProductStockDetail> lpsk=productMapper.selectStockFromDetail(productId);
            String skName=null;
            String skCode=null;
            Double skProportion;
            for (int j=0;j<lpsk.size();j++){
                skName=lpsk.get(j).getStockName();
                skCode=lpsk.get(j).getStockCode();
                skProportion=lpsk.get(j).getProportion().doubleValue()*fdAmount*0.01;
                content2.put(skName,skCode+",    "+skProportion);
            }
            content.put("股票",content2);
            product.put(name,content);
        }
        return product;
    }

    @Override
    public Map selectProductAndRatioNow(int consultanId,int productId) {
        Map product=new HashMap();
        List<SignAccount> lsa=userMapper.selectAccountByProductID(productId);
        int accountNo=0;
        double sum=0;
        for(int i=0;i<lsa.size();i++){
            accountNo=lsa.get(i).getSignaccountid();
            String code=null;
            Integer amount=0;
            String type=null;
            String name=null;
            double valueNow=0;
            double s=0;
            List<Property> lpy=propertyMapper.selectPropertyByAccountNo(accountNo);
                for (int j=0;j<lpy.size();j++){
                    code=lpy.get(j).getCode();
                    amount=lpy.get(j).getAmount();
                    type=lpy.get(j).getType();
                    //如果是股票，去股票备选库查询现值
                    if(type.equals("股票")){
                        valueNow=consultantMapper.selectStockValue(code).getValueNow().doubleValue();
                    }
                    else{
                        valueNow=consultantMapper.selectFundValue(code).getValueNow().doubleValue();
                    }
                    s=amount*valueNow;
                    System.out.println(s);
                    sum+=s;
                }
                System.out.println(sum);
                double percent=0;
                for (int j=0;j<lpy.size();j++){
                    Map content=new HashMap();
                    String code1=lpy.get(j).getCode();
                    int amount1=lpy.get(j).getAmount();
                    String name1=lpy.get(j).getPropertyname();
                    String type1=lpy.get(j).getType();
                    String type2=null;
                    //如果是股票，去股票备选库查询现值
                    if(type1.equals("股票")){
                        valueNow=consultantMapper.selectStockValue(code1).getValueNow().doubleValue();
                        type2="Stock";
                    }
                    //不是，则取基金备选库查询
                    else{
                        valueNow=consultantMapper.selectFundValue(code1).getValueNow().doubleValue();
                        type2="Fund";
                    }
                    s=amount1*valueNow;
                    percent=s/sum;

                    content.put("propertyType",type2);
                    content.put("propertyName",name1);
                    content.put("propertyCode",code1);
                    content.put("proportion",percent);

                    product.put(name1,content);
                }


        }
        return product;
    }


}
