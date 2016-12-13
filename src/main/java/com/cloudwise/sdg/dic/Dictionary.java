package com.cloudwise.sdg.dic;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 词典类， 包含了dictioinaries目录下的词典文件中的词典数据 词典都是有命名空间的，如果不指定命名空间，则默认为default命名空间
 * 
 * @author www.toushibao.com
 *
 */
public class Dictionary {
	private static Map<String, Properties> dics = new HashMap<String, Properties>();

	public static void addDic(String namespace, Properties dic) {
		Properties d = dics.get(namespace);
		if (d == null) {
			d = new Properties();
			dics.put(namespace, d);
		}
		d.putAll(dic);
	}

	public static void addDic(Properties dic) {
		addDic("default", dic);
	}

	public static void addDic(String namespace, String key, String value) {
		Properties d = dics.get(namespace);
		if (d == null) {
			d = new Properties();
			dics.put(namespace, d);
		}
		d.put(key, value);
	}

	public static void addDic(String key, String value) {
		addDic("default", key, value);
	}

	public static String getDicVal(String namespace, String key) {
		Properties d = dics.get(namespace);
		if (d == null) {
			return null;
		}
		return d.getProperty(key);
	}

	public static String getDicStr(String key) {
		return getDicVal("default", key);
	}

	public static String[] getDicArray(String key) {
		return getDicVal("default", key) == null ? null : getDicVal("default", key).split("\\|\\|\\|");
	}
}
