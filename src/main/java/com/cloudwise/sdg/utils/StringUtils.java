package com.cloudwise.sdg.utils;

/**
 * 字符串工具
 * 
 * @author www.toushibao.com
 *
 */
public class StringUtils {
	public static String formatNumber(String num, int n) {
		if (num != null) {
			while (num.length() < n) {
				num = "0" + num;
			}
		}
		return num;
	}
}
