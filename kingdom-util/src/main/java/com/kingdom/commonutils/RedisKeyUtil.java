package com.kingdom.commonutils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * @author : long
 * @date : 2020/6/30 17:10
 */
public class RedisKeyUtil {
    private static final String SPLIT=":";
    private static final String PREFIX_TICKET="ticket";
    private static final String PREFIX_CONSULTANT="consultant";
    private static final String PREFIX_PRODUCT="product";

    /**
     * 生成redis中的指定格式key
     * @param ticket 登录凭证
     * @return 指定格式的key
     */
    public static String getTicketKey(String ticket){
        return PREFIX_TICKET+SPLIT+ticket;
    }

    /**
     * 生成redis中的指定格式的key
     * @param consultantId 投顾人id
     * @return 指定格式的key
     */
    public static String getConsultantKey(int consultantId){
        return PREFIX_CONSULTANT+SPLIT+consultantId;
    }

    /**
     * 生成redis中的指定格式的key
     * @param userId 投顾人id
     * @return 指定格式的key
     */
    public static String getUserKey(int userId){
        return PREFIX_CONSULTANT+SPLIT+userId;
    }
    /**
     * 生成redis中的指定格式的key
     * @param productId 产品id
     * @return 指定格式的key
     */
    public static String getProductKey(int productId){
        String date=new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
        return PREFIX_PRODUCT+SPLIT+productId+date;
    }
}
