/**
 * Copyright (c) 2007-2010 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package org.sf.feeling.swt.win32.extension.util;

import java.io.File;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2012-12-12
 */
/**
 * $Log: CryptUtil.java,v $
 * Revision 1.2  2013/12/05 10:13:19  cchun
 * Update:增加日志
 *
 * Revision 1.1  2013/03/07 06:45:39  cchun
 * Add:加密工具
 *
 * Revision 1.1  2012/12/13 01:18:03  cchun
 * Update:添加配置文件加密处理
 *
 */
public class CryptUtil {
	
	private static final String CHARSET_UTF8 	= "UTF-8"; //$NON-NLS-1$
	private static final String key = "toolkey";
	private static final CryptUtil crypt = new CryptUtil();   
	   
    private CryptUtil() {   
   
    }   
   
    public static CryptUtil getInstance() {   
        return crypt;   
    }   
    
    /**
     * 初始化密钥。
     * @param key
     * @return
     * @throws NoSuchAlgorithmException
     */
    private Key initKeyForAES(String key) throws NoSuchAlgorithmException {   
        if (null == key || key.length() == 0) {   
            throw new NullPointerException("key not is null");   
        }   
        SecretKeySpec key2 = null;   
        try {   
            KeyGenerator kgen = KeyGenerator.getInstance("AES");   
            kgen.init(128, new SecureRandom(key.getBytes()));   
            SecretKey secretKey = kgen.generateKey();   
            byte[] enCodeFormat = secretKey.getEncoded();   
            key2 = new SecretKeySpec(enCodeFormat, "AES");   
        } catch (NoSuchAlgorithmException ex) {   
            throw new NoSuchAlgorithmException();   
        }   
        return key2;   
   
    }   
    
    /**
     * 加密。
     * @param content
     * @param key
     * @return
     */
    public String encryptAES(String content, String key){   
        try{   
            SecretKeySpec secretKey = (SecretKeySpec) initKeyForAES(key);   
            Cipher cipher = Cipher.getInstance("AES");// 创建密码器   
            byte[] byteContent = content.getBytes(CHARSET_UTF8);   
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);// 初始化   
            byte[] result = cipher.doFinal(byteContent);   
            return asHex(result); // 加密   
        } catch (Exception e) {
        	e.printStackTrace();
        }   
        return null;   
    }   
    
    /**
     * 解密。
     * @param content
     * @param key
     * @return
     */
    public String decryptAES(String content, String key){   
        try{   
            SecretKeySpec secretKey = (SecretKeySpec) initKeyForAES(key);   
            Cipher cipher = Cipher.getInstance("AES");// 创建密码器   
            cipher.init(Cipher.DECRYPT_MODE, secretKey);// 初始化   
            byte[] result = cipher.doFinal(asBytes(content));   
            return new String(result); // 加密   
        } catch (Exception e){
        	e.printStackTrace();
        }   
        return null;   
    } 
    
    /**
     * 字节数组转16进制数。
     * @param buf
     * @return
     */
    public String asHex(byte buf[]){   
        StringBuffer strbuf = new StringBuffer(buf.length * 2);   
        int i;   
        for (i = 0; i < buf.length; i++){   
            if (((int) buf[i] & 0xff) < 0x10)   
                strbuf.append("0");   
            strbuf.append(Long.toString((int) buf[i] & 0xff, 16));   
        }   
        return strbuf.toString();   
    }   
    
    /**
     * 16进制数转字节数组。
     * @param hexStr
     * @return
     */
    public byte[] asBytes(String hexStr) {   
        if (hexStr.length() < 1)   
            return null;   
        byte[] result = new byte[hexStr.length() / 2];   
        for (int i = 0; i < hexStr.length() / 2; i++){   
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);   
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2),16);   
            result[i] = (byte) (high * 16 + low);   
        }   
        return result;   
    }
    
    /**
     * 加密文件夹。
     * @param dirpath
     * @param endfix
     */
    public static void encryptDir(String dirpath, String endfix) {
    	List<File> files = FileManager.findFiles(dirpath, endfix);
    	for (File file : files) {
    		encryptFile(file.getPath());
    	}
    }
    
    /**
     * 加密文件。
     * @param path
     */
    public static void encryptFile(String path) {
    	String content = FileManager.readFileByCharset(path, CHARSET_UTF8);
		content = crypt.encryptAES(content, key);
        FileManager.saveTextFile(content, path);
    }
    
    /**
     * 文件夹解密。
     * @param dirpath
     * @param endfix
     */
    public static void decryptDir(String dirpath, String endfix) {
    	List<File> files = FileManager.findFiles(dirpath, endfix);
    	for (File file : files) {
    		decryptFile(file.getPath());
    	}
    }
    
    /**
     * 文件加密。
     * @param path
     */
    public static void decryptFile(String path) {
    	decryptFile(path, path);
    }
    
    /**
     * 文件解密
     * @param path
     */
    public static void decryptFile(String path, String targetPath) {
    	String content = FileManager.readFileByCharset(path, CHARSET_UTF8);
		content = crypt.decryptAES(content, key);
        FileManager.saveTextFile(content, targetPath);
    }
}   
