package com.example.utils.encrypt;


import java.nio.charset.Charset;


public class DesUtil {

    /**
     * 加密
     * @param srcStr
     * @return
     */
    public static String encrypt(String srcStr) {
    	Charset charset = Charset.forName("UTF-8");
    	String sKey= "t0MT@w~1";
        byte[] src = srcStr.getBytes(charset);
        byte[] buf = Des.encrypt(src, sKey);
        return Des.parseByte2HexStr(buf);
    }

    /**
     * 解密
     * @param hexStr
     * @return
     * @throws Exception
     */
    public static String decrypt(String hexStr) throws Exception {
    	Charset charset = Charset.forName("UTF-8");
    	String sKey= "t0MT@w~1";
        byte[] src = Des.parseHexStr2Byte(hexStr);
        byte[] buf = Des.decrypt(src, sKey);
        return new String(buf, charset);
    }

    public static String decrypt(String hexStr,String key) throws Exception {
        Charset charset = Charset.forName("UTF-8");
        String sKey= key;
        byte[] src = Des.parseHexStr2Byte(hexStr);
        byte[] buf = Des.decrypt(src, sKey);
        return new String(buf, charset);
    }

    public static void main(String[] args) throws Exception {
//        String ss = DesUtil.encrypt("张荣");
        String ss = DesUtil.decrypt("E09AA75A183DF3FB");
        System.out.println(ss);
    }

}
