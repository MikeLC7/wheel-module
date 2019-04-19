package com.xcdh.target.util;

import java.security.MessageDigest;

/**
 * Project: ARK
 *
 * Description:
 *
 * @author: MikeLC
 *
 * @date: 2019/4/10 14:40
 **/
public class PasswordHelper {
    public PasswordHelper() {
    }


    public static String EncryptByMd5(String password) {
        char[] hexDigits = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

        try {
            byte[] btInput = password.getBytes();
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            mdInst.reset();
            mdInst.update(btInput);
            byte[] md = mdInst.digest();
            int j = md.length;
            char[] str = new char[j * 2];
            int k = 0;

            for(int i = 0; i < j; ++i) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 15];
                str[k++] = hexDigits[byte0 & 15];
            }

            return new String(str);
        } catch (Exception var10) {
            return null;
        }
    }
}

