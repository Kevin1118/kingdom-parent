package com.kingdom.commonutils;

/**
 * @author : long
 * @date : 2020/7/1 13:52
 */
public interface Constant {


    /**
     * jpg图片后缀
     */
    String SUFFIX_JPG=".jpg";

    /**
     * png图片后缀
     */
    String SUFFIX_PNG=".png";

    /**
     * jpg图片响应类型
     */
    String CONTENT_TYPE_JPG="image/jpeg";

    /**
     * png图片响应类型
     */
    String CONTENT_TYPE_PNG="image/png";

    /**
     * 响应码
     */
    String RESULT_CODE="resultCode";

    /**
     * 登录凭证
     */
    String LOGIN_TICKET="loginTicket";

    /**
     * 订单买入待审批状态
     */
    int APPROVAL_BUY=1;

    /**
     * 订单卖出待审批状态
     */
    int APPROVAL_SELL=2;

    /**
     * 订单待买入
     */
    int WAIT_TO_BUY=3;

    /**
     * 订单待卖出
     */
    int WAIT_TO_SELL=4;

    /**
     * 订单已完成
     */
    int FINISH=5;
    /**
     * 审批
     */
    int APPROVAL=0;
    /**
     * 交易
     */
    int TRANSACTION=1;

    /**
     * 默认货币基金
     */
    String MONEY_FUND_CODE="000198";

    /**
     * 默认货币基金名字
     */
    String MONEY_FUND_NAME="天弘余额宝货币";

    /**
     * 股票最少购买数量限制
     */
    Float STOCK_AMOUNT_LIMIT=1.0f;

    /**
     * 基金最少购买数量限制
     */
    Float FUND_AMOUNT_LIMIT=1.0f;
}
