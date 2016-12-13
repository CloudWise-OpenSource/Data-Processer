package com.cloudwise.sdg.function;

import java.util.Random;
import java.util.UUID;

/**
 * 内置函数
 * 
 * @author www.toushibao.com
 *
 */
public class BuildInFuncs {
	private static Random random = new Random();

	/**
	 * @return ms
	 */
	public static long timestamp() {
		return System.currentTimeMillis();
	}

	public static int intRand() {
		return Math.abs(random.nextInt());
	}

	public static long longRand() {
		return Math.abs(random.nextLong());
	}

	public static String uuid() {
		return UUID.randomUUID().toString();
	}

	/**
	 * 生成n位随机数
	 * 
	 * @param n
	 * @return
	 */
	public static String numRand(Integer n) {
		StringBuilder strb = new StringBuilder(n);
		for (int i = 0; i < n; i++)
			strb.append((char) ('0' + random.nextInt(10)));
		return strb.toString();
	}

	/**
	 * 生成n位随机字符串,包括数据，大小写字母
	 * 
	 * @param n
	 * @return
	 */
	public static String strRand(Integer n) {
		StringBuilder strb = new StringBuilder(n);
		for (int i = 0; i < n; i++) {
			int type = random.nextInt(3);
			switch (type) {
			// 数字
			case 0:
				strb.append((char) ('0' + random.nextInt(10))); break;

			// 小写
			case 1:
				strb.append((char) ('a' + random.nextInt(26))); break;

			// 大写
			case 2:
				strb.append((char) ('A' + random.nextInt(26))); break;
			}
		}
		return strb.toString();
	}

	/**
	 * @param n
	 * @return 0(included) - n(excluded)
	 */
	public static int intRand(Integer n) {
		return random.nextInt(n);
	}

	/**
	 * 
	 * @param s
	 * @param e
	 * @return
	 */
	public static int intRand(Integer s, Integer e) {
		int interval = e - s;
		return s + random.nextInt(interval);
	}

	/**
	 * 
	 * @return 0 - 1.0
	 */
	public static double doubleRand() {
		return random.nextDouble();
	}

	/**
	 * 
	 * @param s
	 *            最小值
	 * @param e
	 *            最大值
	 * @param num
	 *            位数
	 * @return
	 */
	public static double doubleRand(Integer s, Integer e, Integer n) {
		double randDouble = random.nextDouble();
		int interval = e - s;
		double r = s + (randDouble * interval);
		return Double.parseDouble(String.format("%." + n + "f", r));
	}

	public static void main(String[] args) {
		System.out.println(intRand(10));
		System.out.println(doubleRand());
		System.out.println(doubleRand(10, 100, 3));
		System.out.println(numRand(10));
		System.out.println(strRand(10));
	}
}
