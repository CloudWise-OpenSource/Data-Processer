package com.cloudwise.sdg.utils;

import java.lang.reflect.Method;

import com.cloudwise.sdg.function.BuildInFuncs;

public class FunctionUtils {
	private static Class<?> buildInClazz;

	static {
		try {
			buildInClazz = Class.forName(BuildInFuncs.class.getName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static Object executeMethod(String mName, Class<?>[] parameterTypes, Object[] parameter) throws Exception {
		Method method = buildInClazz.getMethod(mName, parameterTypes);
		return method.invoke(null, parameter);
	}

	public static void main(String[] args) throws Exception {
		System.out.println(executeMethod("timestamp", null, null));
		System.out.println(executeMethod("intRand", new Class[] { Integer.class }, new Object[] { 2 }));
	}
}
