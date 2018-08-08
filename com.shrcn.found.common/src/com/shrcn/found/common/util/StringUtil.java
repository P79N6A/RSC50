/*
 * @(#)StringUtil.java
 *
 * Copyright (c) 2007, 2008 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application 
 * based Visual Device Develop System.
 */
package com.shrcn.found.common.util;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.shrcn.found.common.Constants;
import com.shrcn.found.common.log.SCTLogger;

/**
 * 字符串工具
 *
 * @author 刘静
 * @version 1.0, 2009-4-15
 */
public class StringUtil {

	private static final String NONE = "无";
	private static final String EMPTY = "空";
	public static final String DESC_WARN = "描述值只允许为：中英文、数字、空格及、 ， ： 。 ？ _ - \\ / # @之一！";
	
	private StringUtil() {}
	
	/**
	 * 获取外部信号FC类型
	 * @param outSignal
	 * @return
	 */
	public static String getFCFromOutSignal(String outSignal) {
		String[] infos = outSignal.split("/|\\$");
		return infos[2];
	}
	
	/**
	 * 转换字符串
	 * 
	 * @param str		字符串
	 * @return String	字符串
	 */
	public static String nullToEmpty(String str) {
		return str == null ? "" : str;
	}
    
	/**
	 * 空字符串转换为null
	 * @param str        字符串
	 * @return String    字符串
	 */
	public static String emptyToNull(String str) {
		return "".equals(str) ? null : str;
	}
	
	/**
	 * 将字符串转换为整形
	 * 
	 * @param str		字符串
	 * @param defaultv  默认值，如果该字符串不能转换为整型，返回默认值
	 * @return int		整数
	 */
	public static int nullToInt(String str, int defaultv){
		if (str == null || str.length() == 0)
			return defaultv;
		else {
			return transferToInt(str, defaultv);
		}
	}
	
	/**
	 * 将字符串转换为整型
	 * 
	 * @param str
	 * @return
	 */
	private static int transferToInt(String str, int defaultv){
		for(int index = 0; index < str.length(); ++index){
			if(!Character.isDigit(str.charAt(index))){
				return defaultv;
			}
		}
		return Integer.parseInt(str);
	}

	/**
	 * 是否是空，1、null。2、“”。
	 * 
	 * @param str		字符串
	 * @return boolean	是否为空
	 */
	public static boolean isEmpty(String str){
		return str == null || str.length() == 0;
	}

	/**
	 * 判断是否为空
	 * @param str
	 * @return
	 */
	public static boolean isNull(String str){
		return str == null || "null".equals(str);
	}
	
	/**
	 * 定义并创建一个验证监听器。功能：只能输入数字
	 */
	public static boolean verifyNum(String text) {
		boolean isDigit = true;
		char[] txt = text.toCharArray();
		for (char c : txt) {
			if (!java.lang.Character.isDigit(c)){
				isDigit = false;
				break;
			}
		}
		return isDigit;
	}
	
	/**
	 * 去掉前缀
	 * @param src
	 * @param prefix
	 * @return
	 */
	public static String trimPrefix(String src, String prefix) {
		return src.substring(prefix.length());
	}
	
	/**
	 * 去掉小数点后的零
	 * @param value
	 * @return
	 */
	public static String trimZero(String value) {
		int p = value.indexOf('.');
		String str = null;
		if (p > -1) {
			String dotNum = value.substring(p + 1);
			char[] chars = dotNum.toCharArray();
			for (int i=chars.length-1;i>-1;i--){
				if(chars[i]!='0'){
					p=i+p+2;
					break;
				}
			}
			str = value.substring(0, p);
		}else {
			str = value;
		}
		return str;
	}
	
	/**
	 * 过滤转义字符
	 * @param xmlStr
	 * @return
	 */
	public static String toXMLChars(String xmlStr) {
		StringReader reader = new StringReader(xmlStr);
		StringWriter writer = new StringWriter(xmlStr.length());
		int c = -1;
		try {
			c = reader.read();
		} catch (IOException e) {
			e.printStackTrace();
		}
		while (c != -1) {
			switch (c) {
			case 0x0A:
				writer.write(c);
				break;

			case '<':
				writer.write("&lt;");
				break;

			case '>':
				writer.write("&gt;");
				break;

			case '&':
				writer.write("&amp;");
				break;

			case '\'':
				writer.write("&apos;");
				break;

			case '"':
				writer.write("&quot;");
				break;

			default:
//				if (((c < ' ') || (c > 0x7E))) {
				if (c < ' ') {
					writer.write("&#x");
					String s = Integer.toString(c, 16);
					if (s.length()<2)
						s = "0" + s;
					writer.write(s);
					writer.write(';');
				} else {
					writer.write(c);
				}
			}
			try {
				c = reader.read();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return writer.toString();
	}
	
	/**
	 * 由于引号,'{','}'属于XQuery语法特殊字符范畴，
	 * 故XQuery中xml字符串带有这三个个字符时需进行特殊处理。
	 * @param xmlStr
	 * @return
	 */
	public static String clearGrammarChars(String xmlStr) {
		String temp = 
			xmlStr.replaceAll("'", "&apos;");
		temp.replaceAll("\"", "'");
		temp = temp.replaceAll("\\{", "{{");
		temp = temp.replaceAll("\\}", "}}");
		return temp;
	}
	
	/**
	 * 删除字符串中减号。
	 * @param str
	 * @return
	 */
	public static String clearMinus(String str) {
		String[] infos = str.split("-");
		StringBuilder sbinfo = new StringBuilder();
		for (String info : infos) {
			sbinfo.append(info);
		}
		return sbinfo.toString();
	}
		
	/**字符串转 Boolean型,空或者Null字符串则返回false
	 * @param value
	 * @return
	 */
	public static Boolean string2Boolean(String value) {
		Boolean def = false;
		if (!isEmpty(value)) {
			def = Boolean.valueOf(value);
		}
		return def;
	}

	/**
	 * 字符串转 BigInteger型,空或者Null字符串则返回BigInteger("-1")
	 * @param value
	 * @return
	 */
	public static BigInteger string2BigInteger(String value) {
		BigInteger def = new BigInteger("-1");
		if (!isEmpty(value)) {
			def = new BigInteger(value);
		}
		return def;
	}
	
	/**
	 * 字符串转 BigInteger型,空或者Null字符串则返回BigInteger("-1")
	 * @param value
	 * @return
	 */
	public static Integer string2Integer(String value) {
		Integer def = new Integer("-1");
		if (!isEmpty(value)) {
			def = new Integer(value);
		}
		return def;
	}
	
	/**字符串转int,如果不能转则返回-1
	 * @param v
	 * @return
	 */
	public static int str2int(String v) {
		int i = 0;
		try {
			i = Integer.valueOf(v);
		} catch (Exception e) {
			i = -1;
		}
		return i;
	}
	
	/**
	 * 得到格式化输出的字符串
	 * @param v
	 * @param format
	 * @return
	 */
	public static String getFormatStr(int v, String format) {
		return String.format(format, v);
	}
	
	/**
	 * 得到布尔字符串
	 * @param v
	 * @param format
	 * @return
	 */
	public static String getBooleanStr(boolean v) {
		return v ? "True" : "False";
	}
	
	/**
	 * 得到字符串布尔值
	 * @param str
	 * @return
	 */
	public static boolean getBoolean(String str) {
		return "True".equals(str);
	}
	
	/**
	 * 拼接name、desc
	 * @param name
	 * @param desc
	 * @return
	 */
	public static String getLabel(String name, String desc) {
		return name + (isEmpty(desc)?"":Constants.COLON + desc); //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**
	 * 根据label获取name值
	 * @param label
	 * @return
	 */
	public static String getName(String label) {
		int pos = label.indexOf(Constants.COLON);
		return (pos > -1) ? label.substring(0, pos) : label;
	}

	/**
	 * 判断有没有汉字
	 * 
	 * @param name
	 * @return
	 */
	public static boolean hasChinese(String name) {
		int len = name.length();
		String[] s = new String[len];
		for (int i = 0; i < len; i++) {
			char c = name.charAt(i);
			String valueOf = String.valueOf(c);
			s[i] = Integer.toString(c, 16);
			boolean matches = valueOf.matches("[\u4e00-\u9fa5]+"); //$NON-NLS-1$
			if (matches) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 判断是否为合法的描述
	 * @param name
	 * @return
	 */
	public static boolean isValidDesc(String name) {
//		/*中文、英文、数字、下划线、空格、正反斜杠，冒号，以及中文标点符号，-，#*/
//		return name.matches("[\\w-#@\u4e00-\u9fa5\\s\\\\////\\/:\u3002\uff1b\uff0c\uff1a\u201c\u201d\uff08\uff09\u3001\uff1f\u300a\u300b]*");
		return isValidXml(name);
	}
	
	/**
	 * 判断是否为合法的名称字符串
	 * @param name
	 * @return
	 */
	public static boolean isValidName(String name) {
		if (!isEmpty(name)) {
			String first = name.substring(0, 1);
			if (first.matches("[0-9]"))
				return false;
		}
		return name.matches("[\\s-\\w\u4e00-\u9fa5]+");
	}
	
	public static boolean isValidXml(String value) {
		if (!isEmpty(value)) {
			return !value.matches("[/\\w<>\\\\&\\\"]+");
		}
		return true;
	}
	
	/**
	 * 返回标签文本
	 * @param text
	 * @return
	 */
	public static String getLabelWithDefault(String text) {
		return isEmpty(text) ? NONE : text;
	}
	
	/**
	 * 获取当前时间字符串
	 * @param pattern
	 * @return
	 */
	public static String getCurrentTime(String pattern) {
		return getDateStr(new Date(), pattern);
	}
	
	/**
	 * 得到中文事件字串。
	 * @return
	 */
	public static String getChineseTime() {
		return getCurrentTime(Constants.CHINA_DATE_FORMAT);
	}
	
	/**
	 * 根据日期字符串和格式获取时间对象
	 * @param datetime
	 * @param pattern
	 * @return
	 */
	public static Date getDate(String datetime, String pattern) {
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		try {
			return format.parse(datetime);
		} catch (ParseException e) {
			SCTLogger.error("", e);
		}
		return null;
	}
	
	/**
	 * 根据时间对象和格式获取日期字符串
	 * @param pattern
	 * @return
	 */
	public static String getDateStr(Date date, String pattern) {
		SimpleDateFormat format = new SimpleDateFormat(pattern);
	    return format.format(date);
	}
	
	/**
	 * 得到'.'字符个数
	 * @param name
	 * @return
	 */
	public static int getNamePoint(String name){
		int num=0;
		if(name!=null){
			for(int i=0;i<name.length();i++){
				if(new Character(name.charAt(i)).toString().equals(".")){ //$NON-NLS-1$
					num++;
				}
			}
		}
		return num;
	}

	/**
	 * 获取'.'号分隔字符串末尾一级字符串
	 * @param name
	 * @return
	 */
	public static String getLastName(String name){
		int num=getNamePoint(name);
		if(num<=0){
			return name;
		}
		String reverse=new StringBuffer(name).reverse().toString();
		int point=reverse.indexOf("."); //$NON-NLS-1$
		String temp=reverse.substring(0, point);
		return new StringBuffer(temp).reverse().toString();
	}
	
	/**
	 * 获取'.'号分隔字符串第一级字符串
	 * @param doName
	 * @return
	 */
	public static String getFirstName(String doName){
		int num=doName.indexOf("."); //$NON-NLS-1$
		String name=null;
		if(num>0){
			name=doName.substring(0,num);
		}else{
			name=doName;
		}
		return name;
	}
	
	/**
	 * 比较名称
	 * @param name1
	 * @param name2
	 * @return
	 */
	public static int compare(String name1, String name2) {
		if(name1 == null || name2==null ) {
			return 0;
		}
		if (name1.length() == name2.length()) {
			return name1.compareTo(name2);
		} else {
			return name1.length() > name2.length() ? 1 : -1;
		}
	}
	
	/**
	 * 得到DO路径
	 * 
	 * @param intAddr
	 *            inputs下intAddr格式
	 * @return
	 */
	public static String getDoXpath(String intAddr) {
		int p = intAddr.indexOf(':');
		if (p > -1) {
			intAddr = intAddr.substring(p + 1);
		}
		int dotPos = intAddr.indexOf('.');
		String lnRef = intAddr.substring(0, dotPos + 1);
		String daRef = intAddr.substring(dotPos + 1);
		//若intAddr只到DO这一级
		if (daRef.indexOf('.') < 0) {
            return  lnRef + daRef;
		} else {
			String doName = daRef.substring(0, daRef.indexOf('.'));
			return lnRef + doName;
		}
	}
	
	/**
	 * 得到SMV的MAC默认地址的最后四位
	 * @return
	 */
	public static String getFixMAC(String macAddress) {
		String[] address = macAddress.split("-");
		return address[4] + "-" + address[5];
	}
	
	/**
	 * 判断desc是否以"/"结束，若是，返回"/"之前的内容
	 * @param desc
	 * @return
	 */
	public static String getCleanDesc(String desc) {
		if (desc.endsWith("/")) {
			return desc.substring(0, desc.length()-1);
		} else {
			return desc;
		}
	}
	
	/**
	 * Translates the given String into ASCII code.
	 * 
	 * @param input the input which contains native characters like umlauts etc
	 * @return the input in which native characters are replaced through ASCII code
	 */
	public static String nativeToAscii(String input) {
		if (input == null) {
			return null;
		}
		StringBuffer buffer = new StringBuffer(input.length() + 60);
		for (int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);
			if (c <= 0x7E) {
				buffer.append(c);
			} else {
				buffer.append("\\u");
				String hex = Integer.toHexString(c);
				for (int j = hex.length(); j < 4; j++) {
					buffer.append('0');
				}
				buffer.append(hex);
			}
		}
		return buffer.toString();
	}

	/**
	 * Translates the given String into ASCII code.
	 * 
	 * @param input the input which contains native characters like umlauts etc
	 * @return the input in which native characters are replaced through ASCII code
	 */
	public static String asciiToNative(String input) {
		if (input == null) {
			return null;
		}
		StringBuffer buffer = new StringBuffer(input.length());
		boolean precedingBackslash = false;
		for (int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);
			if (precedingBackslash) {
				switch (c) {
				case 'f':
					c = '\f';
					break;
				case 'n':
					c = '\n';
					break;
				case 'r':
					c = '\r';
					break;
				case 't':
					c = '\t';
					break;
				case 'u':
					String hex = input.substring(i + 1, i + 5);
					c = (char) Integer.parseInt(hex, 16);
					i += 4;
				}
				precedingBackslash = false;
			} else {
				precedingBackslash = (c == '\\');
			}
			if (!precedingBackslash) {
				buffer.append(c);
			}
		}
		return buffer.toString();
	}

	
	/**
	 * 过滤字符串
	 * @param in 输入字符串
	 * @param tabsWidth Tab键宽度值
	 * @return 格式化后的字符串
	 */
	public static  String replaceTabWithBlank(String in, int[] tabsWidth) {
		StringBuffer buf = new StringBuffer();
		int index = 0;
		int width = 0;
		char[] chs = in.toCharArray();
		for(char ch : chs) {
			if(ch == '\r' || ch == '\n') {
				width = 0;
				index = 0;
			} else if(ch == '\t') {
				int tabWidth = index < tabsWidth.length ? tabsWidth[index] : 8;
				for(int w=width; w<tabWidth; ++w) {
					buf.append(' ');
				}
				width = 0;
				index++;
				continue;
			} else if(ch > 256) {
				width += 2;
			} else {
				width++;
			}
			buf.append(ch);
		}
		return buf.toString();
	}
	
	/**
	 * 去除字符串中的空格、回车、换行符、制表符
	 * @param str
	 * @return
	 */
	public static String replaceBlank(String str) {
		String dest = "";
		if (str!=null) {
			Pattern p = Pattern.compile("\\s*|\t|\r|\n");
			Matcher m = p.matcher(str);
			dest = m.replaceAll("");
		}
		return dest;
	}
	
	/**
	 * 右侧补充空格至指定长度
	 * @param str
	 * @param len
	 * @return
	 */
	public static String fitLength(String str, int len) {
		int length = str.getBytes().length;
		if (length >= len)
			return str;
		for (int i=0; i<len-length; i++)
			str += " ";
		return str;
	}
	
	/**
	 * 获取指定字符串MD5码
	 * 
	 * @param str
	 * @return
	 */
	public static String getMD5CodeForStr(String str) {
		String md5 = null;
		try {
			MessageDigest digest = MessageDigest.getInstance("MD5");
			digest.update(str.getBytes());
			byte[] md5sum = digest.digest();
			md5 = ByteUtil.bytes2HexString(md5sum);
		} catch (NoSuchAlgorithmException e) {
			SCTLogger.error("初始化失败，MessageDigest不支持MD5", e);
		}
		return md5;
	}
	
	/**
	 * 判断两个字符串是否类似
	 * @param x
	 * @param y
	 * @return
	 */
	public static boolean isSimilar(String x, String y) {
		String lcs = SimilarUtil.getLCS(x.trim(), y.trim());
		double p = (double) (lcs.length() / x.length());
		int d = x.length()-lcs.length();
		return p>=0.6 || d <4;
	}
	
	public static double getSimilarByLCS(String x, String y) {
		String lcs = SimilarUtil.getLCS(x.trim(), y.trim());
		return (double)lcs.length() / Math.max(x.length(), y.length());
	}
	
	public static double getSimilarByLeven(String x, String y) {
		int dif = SimilarUtil.getDiff(x, y);
		return 1 - (double) dif / Math.max(x.length(), y.length());
	}
	
	public static double getSimilarity(String doc1, String doc2) {
        if (doc1 != null && doc1.trim().length() > 0 && doc2 != null
                && doc2.trim().length() > 0) {
             
            Map<Integer, int[]> AlgorithmMap = new HashMap<Integer, int[]>();
             
            //将两个字符串中的中文字符以及出现的总数封装到，AlgorithmMap中
            for (int i = 0; i < doc1.length(); i++) {
                char d1 = doc1.charAt(i);
                if(isHanZi(d1)){
                    int charIndex = getGB2312Id(d1);
                    if(charIndex != -1){
                        int[] fq = AlgorithmMap.get(charIndex);
                        if(fq != null && fq.length == 2){
                            fq[0]++;
                        }else {
                            fq = new int[2];
                            fq[0] = 1;
                            fq[1] = 0;
                            AlgorithmMap.put(charIndex, fq);
                        }
                    }
                }
            }
 
            for (int i = 0; i < doc2.length(); i++) {
                char d2 = doc2.charAt(i);
                if(isHanZi(d2)){
                    int charIndex = getGB2312Id(d2);
                    if(charIndex != -1){
                        int[] fq = AlgorithmMap.get(charIndex);
                        if(fq != null && fq.length == 2){
                            fq[1]++;
                        }else {
                            fq = new int[2];
                            fq[0] = 0;
                            fq[1] = 1;
                            AlgorithmMap.put(charIndex, fq);
                        }
                    }
                }
            }
             
            Iterator<Integer> iterator = AlgorithmMap.keySet().iterator();
            double sqdoc1 = 0;
            double sqdoc2 = 0;
            double denominator = 0; 
            while(iterator.hasNext()){
                int[] c = AlgorithmMap.get(iterator.next());
                denominator += c[0]*c[1];
                sqdoc1 += c[0]*c[0];
                sqdoc2 += c[1]*c[1];
            }
             
            return denominator / Math.sqrt(sqdoc1*sqdoc2);
        } else {
            throw new NullPointerException(
                    " the Document is null or have not cahrs!!");
        }
    }
 
    public static boolean isHanZi(char ch) {
        // 判断是否汉字
        return (ch >= 0x4E00 && ch <= 0x9FA5);
 
    }
 
    /**
     * 根据输入的Unicode字符，获取它的GB2312编码或者ascii编码，
     * 
     * @param ch
     *            输入的GB2312中文字符或者ASCII字符(128个)
     * @return ch在GB2312中的位置，-1表示该字符不认识
     */
    public static short getGB2312Id(char ch) {
        try {
            byte[] buffer = Character.toString(ch).getBytes("GB2312");
            if (buffer.length != 2) {
                // 正常情况下buffer应该是两个字节，否则说明ch不属于GB2312编码，故返回'?'，此时说明不认识该字符
                return -1;
            }
            int b0 = (int) (buffer[0] & 0x0FF) - 161; // 编码从A1开始，因此减去0xA1=161
            int b1 = (int) (buffer[1] & 0x0FF) - 161; // 第一个字符和最后一个字符没有汉字，因此每个区只收16*6-2=94个汉字
            return (short) (b0 * 94 + b1);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return -1;
    }
	
	/* 
     * 计算两个字符串(英文字符)的相似度，简单的余弦计算，未添权重 
     */  
     public static double getSimilarDegree(String str1, String str2)  
     {  
        //创建向量空间模型，使用map实现，主键为词项，值为长度为2的数组，存放着对应词项在字符串中的出现次数  
         Map<String, int[]> vectorSpace = new HashMap<String, int[]>();  
         int[] itemCountArray = null;//为了避免频繁产生局部变量，所以将itemCountArray声明在此  
           
         //以空格为分隔符，分解字符串  
         String strArray[] = str1.split(" ");  
         for(int i=0; i<strArray.length; ++i)  
         {  
             if(vectorSpace.containsKey(strArray[i]))  
                 ++(vectorSpace.get(strArray[i])[0]);  
             else  
             {  
                 itemCountArray = new int[2];  
                 itemCountArray[0] = 1;  
                 itemCountArray[1] = 0;  
                 vectorSpace.put(strArray[i], itemCountArray);  
             }  
         }  
           
         strArray = str2.split(" ");  
         for(int i=0; i<strArray.length; ++i)  
         {  
             if(vectorSpace.containsKey(strArray[i]))  
                 ++(vectorSpace.get(strArray[i])[1]);  
             else  
             {  
                 itemCountArray = new int[2];  
                 itemCountArray[0] = 0;  
                 itemCountArray[1] = 1;  
                 vectorSpace.put(strArray[i], itemCountArray);  
             }  
         }  
           
         //计算相似度  
         double vector1Modulo = 0.00f;//向量1的模  
         double vector2Modulo = 0.00f;//向量2的模  
         double vectorProduct = 0.00f; //向量积  
         Iterator<?> iter = vectorSpace.entrySet().iterator();  
           
         while(iter.hasNext())  
         {  
             Map.Entry<String, int[]> entry = (Map.Entry<String, int[]>)iter.next();  
             itemCountArray = (int[])entry.getValue();  
               
             vector1Modulo += itemCountArray[0]*itemCountArray[0];  
             vector2Modulo += itemCountArray[1]*itemCountArray[1];  
               
             vectorProduct += itemCountArray[0]*itemCountArray[1];  
         }  
           
         vector1Modulo = Math.sqrt(vector1Modulo);  
         vector2Modulo = Math.sqrt(vector2Modulo);  
           
         //返回相似度  
        return (vectorProduct/(vector1Modulo*vector2Modulo));  
     }  
	
	/**
	 * 判断字符串是否为整数
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str) {
		for (int i = str.length(); --i >= 0;) {
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}
	
	public static int getNumeric(String str) {
		int j = -1;
		int size = str.length();
		for (int i = 1; i <= size; i++) {
			String subStr = str.substring(size - i);

			if (isNumeric(subStr)) {
				j = Integer.parseInt(subStr);
			}
		}
		return j;
	}
	
	public static int getIntegerValue(String v) {
		if (v == null) {
			return 0;
		}
		try {
			return new Integer(v).intValue();
		} catch (NumberFormatException e) {
		}

		return 0;
	}
	
	public static String checkBooleanValid(String newText) {
		Pattern pattern = Pattern.compile("[01]");
		Matcher matcher = pattern.matcher(newText);

		if (!matcher.matches()) {
			return "请输入0, 或 1 代表布尔值.";
		}

		return null;
	}

	public static String checkIntegerValid(String newText) {
		Pattern pattern = Pattern.compile("^-?\\d+$");
		Matcher matcher = pattern.matcher(newText);

		if (!matcher.matches()) {
			return "请输入整数.";
		}

		return null;
	}

	public static String checkFloatValid(String newText) {
		Pattern pattern = Pattern.compile("^(-?\\d+)(\\.\\d+)?$");
		Matcher matcher = pattern.matcher(newText);

		if (!matcher.matches()) {
			return "请输入浮点数.";
		}

		return null;
	}
	
	public static final String up = "_u";
	
	public static String string2Unicode(String string) {
		if (string==null || !StringUtil.hasChinese(string)){
			return string;
		}
		
        StringBuffer unicode = new StringBuffer();
        for (int i = 0; i < string.length(); i++) {
            // 取出每一个字符
            char c = string.charAt(i);
            // 转换为unicode
            String code = Integer.toHexString(c);
            if (code.length() < 4) {
	            for (int j=0; j<5-code.length(); j++) {
	            	code = "0" + code;
	            }
            }
			unicode.append(up + code);
        }
        return unicode.toString();
    }
	
	public static String unicode2String(String utfString){
		if (utfString==null || utfString.indexOf(up)<0) {
			return utfString;
		}
		
		StringBuilder sb = new StringBuilder();
		int i = -1;
		int pos = 0;
		
		while((i=utfString.indexOf(up, pos)) != -1){
			sb.append(utfString.substring(pos, i));
			if(i+5 < utfString.length()){
				pos = i+6;
				String code = utfString.substring(i+2, i+6);
				sb.append((char)Integer.parseInt(code.trim(), 16));
			}
		}
		
		return sb.toString();
	}
	
	/**
	 * 获取字符串描述
	 * @param str
	 * @return
	 */
	public static String getStrDesc(String str){
		if(str == null)
			return NONE;
		else if("".equals(str))
			return EMPTY;
		else
			return "'" + str + "'";
	}
	
	/**
	 * 获取对象新名称
	 * @param objs
	 * @param prefix
	 * @return
	 */
	public static String getNewName(List<?> objs, String prefix) {
		int max = 0;
		for (Object obj : objs) {
			String oName = (String) ObjectUtil.getProperty(obj, "name");
			if (oName.startsWith(prefix)) {
				String num = oName.substring(prefix.length());
				if (StringUtil.isNumeric(num)) {
					int netIdx = Integer.parseInt(num);
					if (max < netIdx) {
						max = netIdx;
					}
				}
			}
		}
		return prefix + (max+1);
	}
	
	/**
	 * 得到16进制整数值
	 * @param value16
	 * @return
	 */
	public static int hex2Integer(String value16) {
		if (StringUtil.isEmpty(value16)) {
			return 0;
		}
		if (value16.endsWith("H") || value16.endsWith("h")) {
			value16 = value16.substring(0, value16.length()-1);
		}
		return Integer.parseInt(value16, 16);
	}
	
	/**
	 * 得到16进制字符串
	 * @param value10
	 * @param suffix
	 * @return
	 */
	public static String integer2Hex(int value10, String suffix) {
		return Integer.toHexString(value10) + suffix;
	}
	
	/**
	 * 将字符串数组合并成字符串
	 * @param strs
	 * @return
	 */
	public static String arrayToString(String[] strs, String sep) {
		String result = "";
		for (String str : strs) {
			if (!"".equals(result)) {
				result += sep;
			}
			result += str;
		}
		return result;
	}
	
	/**
	 * 右补齐字符串
	 * @param str
	 * @param len
	 * @return
	 */
	public static String fillRight(String str, int len) {
		String tmp = null;
		int strLen = str.length();
		if (strLen > len) {
			tmp = str.substring(0, len);
		} else {
			tmp = str;
			if (strLen < len) {
				for (int i = 0; i < (len - strLen); i++) {
					tmp += " ";
				}
			}
		}
		return tmp;
	}
}
