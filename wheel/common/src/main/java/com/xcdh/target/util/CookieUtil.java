package com.xcdh.target.util;

import com.xcdh.target.constant.CommonConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * Created by Piao Yongxu Create Date: 2017/4/5 18:44 Description: cookie读取工具类
 */
public class CookieUtil {

    private static final Logger logger = LoggerFactory.getLogger(CookieUtil.class);

    /**
     * Description ： 添加cookie
     *
     * @param response
     * @param name
     * @param value
     */
    public static void addCookie(HttpServletResponse response, String name, String value)
            throws UnsupportedEncodingException {
        value = URLEncoder.encode(value.trim(), "UTF-8");
        Cookie cookie = new Cookie(name.trim(), value);
        cookie.setPath("/");
        cookie.setDomain(CommonConstant.cookieDomain);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }

    /**
     * Description : 获取cookie
     *
     * @param request
     * @param name
     * @return
     */
    public static String getCookie(HttpServletRequest request, String name) throws UnsupportedEncodingException {
        Cookie[] cookies = request.getCookies();
        if (null != cookies) {
//            logger.info("#####cookie.length:"+cookies.length);
            for (Cookie cookie : cookies) {
//                logger.info("cookie&&"+cookie.getName().trim()+":"+URLDecoder.decode(cookie.getValue(), "utf-8"));
                if (name.trim().equals(cookie.getName().trim())) {
                    String cookieValue = URLDecoder.decode(cookie.getValue(), "utf-8");
                    return cookieValue;
                }
            }
        }
        logger.info("#####cookie==null");
        return "";
    }

    /**
     * Description : 删除 cookie
     *
     * @param request
     * @param name
     */
    public static void delCookie(HttpServletRequest request, HttpServletResponse response, String name) {
        Cookie[] cookies = request.getCookies();
        if (null != cookies) {
            for (Cookie cookie : cookies) {
                if (name.trim().equals(cookie.getName().trim())) {
                    cookie.setDomain(CommonConstant.cookieDomain);
                    cookie.setPath("/");
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }
            }
        }
    }

}
