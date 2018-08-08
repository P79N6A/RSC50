/**
 * Copyright (c) 2007-2017 思源电气股份有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application.
 */
package com.shrcn.found.common.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

 /**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2018-6-27
 */
public class ZipStrUtil {

	/**  
     * 字符串的压缩  
     *   
     * @param str  
     *            待压缩的字符串  
     * @return    返回压缩后的字符串  
     * @throws IOException  
     */  
    public static String compress(String str) throws IOException {  
        if (null == str || str.length() <= 0) {  
            return str;  
        }  
        // 创建一个新的 byte 数组输出流  
        ByteArrayOutputStream out = new ByteArrayOutputStream();  
        // 使用默认缓冲区大小创建新的输出流  
        GZIPOutputStream gzip = new GZIPOutputStream(out);  
        // 将 b.length 个字节写入此输出流  
        gzip.write(str.getBytes());  
        gzip.close();  
        // 使用指定的 charsetName，通过解码字节将缓冲区内容转换为字符串  
        return out.toString("ISO-8859-1");  
    }  
    
    public static void main(String[] args) throws IOException {
    	String b = "PS3502PI/LLN0$GO$gocb0";
		String c = "IL2251ARPIT/LLN0$GO$gocb1";
		System.out.println(b + "\t" + compress(b));
		System.out.println(c + "\t" + compress(c));
	}
}

