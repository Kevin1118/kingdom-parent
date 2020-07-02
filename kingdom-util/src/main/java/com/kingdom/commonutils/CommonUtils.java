package com.kingdom.commonutils;

import org.springframework.util.DigestUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
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


    /**
     * 获取cookie
     * @param request 请求
     * @param name cookie名
     * @return cookie值
     */
    public static String getValue(HttpServletRequest request, String name) {
        if (request == null || name == null) {
            throw new IllegalArgumentException("参数为空！");
        }

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    /**
     * 获取当前时间到凌晨的秒数
     * @return 秒数
     */
    public static Long getSecondsNextEarlyMorning() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, 1);
        // 改成这样就好了
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return (cal.getTimeInMillis() - System.currentTimeMillis()) / 1000;
    }
}
