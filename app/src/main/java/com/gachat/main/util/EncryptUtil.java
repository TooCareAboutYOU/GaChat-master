package com.gachat.main.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 加密
 */

public class EncryptUtil {
    public static String shaEncrypt(String strSrc){
        MessageDigest messageDigest=null;
        String strDes=null;
        byte[] bt=strSrc.getBytes();
        try {
            messageDigest=MessageDigest.getInstance("SHA-256");  //  将此换成SHA-1、SHA-512、SHA-384等参数
            messageDigest.update(bt);
            strDes=bytes2Hex(messageDigest.digest()); // to HexString
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }

        return strDes;
    }

    /**
     * byte数组转换为16进制字符串
     *
     * @param bts 数据源
     * @return 16进制字符串
     */
    public static String bytes2Hex(byte[] bts) {
        String des = "";
        String tmp = null;
        for (int i = 0; i < bts.length; i++) {
            tmp = (Integer.toHexString(bts[i] & 0xFF));
            if (tmp.length() == 1) {
                des += "0";
            }
            des += tmp;
        }
        return des;
    }
}
