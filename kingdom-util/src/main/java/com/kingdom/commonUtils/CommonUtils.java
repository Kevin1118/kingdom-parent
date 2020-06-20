package com.kingdom.commonUtils;

import org.springframework.util.DigestUtils;

import java.util.UUID;

/**
 * @author : long
 * @date : 2020/6/20 16:45
 */
public class CommonUtils {

    /**
     * 生成随机字符串
     * @return 随机字符串
     */
    public static String generateUUID(){
        return UUID.randomUUID().toString().replaceAll("-","");
    }

    /**
     * 将字符串进行md5加密
     * @param key 需要加密的字符串
     * @return 加密后的字符串
     */
    public static String md5(String key) {
        return DigestUtils.md5DigestAsHex(key.getBytes());
    }
}
