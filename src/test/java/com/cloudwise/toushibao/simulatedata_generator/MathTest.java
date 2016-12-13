package com.cloudwise.toushibao.simulatedata_generator;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.cloudwise.sdg.utils.StringUtils;

public class MathTest {
	public static void main(String[] args) throws IOException {
		short a = -1;
		System.out.println(Long.toBinaryString(a));
		System.out.println(StringUtils.formatNumber(Long.toBinaryString(a),8));
		long b = a >>>1 ;
		System.out.println(StringUtils.formatNumber(Long.toBinaryString(b),8));
		
		
		String text = FileUtils.readFileToString(new File("test/rum.txt"));
		System.out.println(text.length());
		System.out.println(text.replaceAll("\"(.*)\":", ""));
	}
}
