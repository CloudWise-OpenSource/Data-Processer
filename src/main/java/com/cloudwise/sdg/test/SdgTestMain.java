package com.cloudwise.sdg.test;

import java.io.File;

import org.apache.commons.io.FileUtils;

import com.cloudwise.sdg.dic.DicInitializer;
import com.cloudwise.sdg.template.TemplateAnalyzer;

public class SdgTestMain {

	public static void main(String[] args) throws Exception {
		//初始化词典
		DicInitializer.init();
		File templates = new File("templates");
		if(templates.isDirectory()){
			File[] tplFiles = templates.listFiles();
			for(File tplFile: tplFiles){
				if(tplFile.isFile()){
					String tpl = FileUtils.readFileToString(tplFile);
					String tplName = tplFile.getName();
					System.out.println("======tplName: "+tplName+", begin===================");
					TemplateAnalyzer testTplAnalyzer = new TemplateAnalyzer(tplName, tpl);
					String abc = testTplAnalyzer.analyse();
					System.out.println(abc);
					System.out.println("======tplName: "+tplName+", end==================");
					System.out.println();
				}
			}
		}
	}
}
