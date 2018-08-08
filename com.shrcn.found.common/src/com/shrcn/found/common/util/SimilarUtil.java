/**
 * Copyright (c) 2007-2015 上海思弘瑞电力控制技术有限公司.
 * All rights reserved. This program is an eclipse Rich Client Application
 * based on IEC61850 SCT.
 */
package com.shrcn.found.common.util;

 /**
 * 
 * @author 陈春(mailto:cchun@shrcn.com)
 * @version 1.0, 2016-2-17
 */
public class SimilarUtil {
	/**
	 * 得到最大公共字串
	 * @param x
	 * @param y
	 * @return
	 */
	public static String getLCS(String x, String y) {
		int substringLength1 = x.length();
        int substringLength2 = y.length();
        // 构造二维数组记录子问题x[i]和y[i]的LCS的长度
        int[][] opt = new int[substringLength1 + 1][substringLength2 + 1];
        // 动态规划计算所有子问题
        for (int i = substringLength1 - 1; i >= 0; i--){
            for (int j = substringLength2 - 1; j >= 0; j--){
            	if (charEqual(x.charAt(i), y.charAt(j)))
                    opt[i][j] = opt[i + 1][j + 1] + 1;                                
                else
                    opt[i][j] = Math.max(opt[i + 1][j], opt[i][j + 1]);        
            }
        }
        int i = 0, j = 0;
        StringBuilder lcs = new StringBuilder();
        while (i < substringLength1 && j < substringLength2){
			if (charEqual(x.charAt(i), y.charAt(j))) {
            	lcs.append(x.charAt(i));
                i++;
                j++;
            } else if (opt[i + 1][j] >= opt[i][j + 1])
                i++;
            else
                j++;
        }
        return lcs.toString();
	}

	/**
	 * 不区分大小写
	 * @param chX
	 * @param chY
	 * @return
	 */
	private static boolean charEqual(char chX, char chY) {
		return (chX == chY) || Math.abs(chX-chY)==Math.abs('a'-'A');
	}
	
	/**
	 * Levenshtein Distance 算法实现计算两个字符串的差异步骤
	 * @param str1
	 * @param str2
	 * @return
	 */
	public static int getDiff(String str1, String str2) {
		//计算两个字符串的长度。
		int len1 = str1.length();
		int len2 = str2.length();
		//建立上面说的数组，比字符长度大一个空间
		int[][] dif = new int[len1 + 1][len2 + 1];
		//赋初值，步骤B。
		for (int a = 0; a <= len1; a++) {
			dif[a][0] = a;
		}
		for (int a = 0; a <= len2; a++) {
			dif[0][a] = a;
		}
		//计算两个字符是否一样，计算左上的值
		int temp;
		for (int i = 1; i <= len1; i++) {
			for (int j = 1; j <= len2; j++) {
				if (charEqual(str1.charAt(i - 1), str2.charAt(j - 1))) {
					temp = 0;
				} else {
					temp = 1;
				}
				//取三个值中最小的
				dif[i][j] = min(dif[i - 1][j - 1] + temp, dif[i][j - 1] + 1,
						dif[i - 1][j] + 1);
			}
		}
		return dif[len1][len2];
	}
	
	//得到最小值
	private static int min(int... is) {
		int min = Integer.MAX_VALUE;
		for (int i : is) {
			if (min > i) {
				min = i;
			}
		}
		return min;
	}
}

