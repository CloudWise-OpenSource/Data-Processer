package com.cloudwise.sdg.utils;

import java.util.Map;
import java.util.Map.Entry;

import com.cloudwise.sdg.dic.DicInitializer;
import com.cloudwise.sdg.dic.Dictionary;

import java.util.Random;
import java.util.TreeMap;

/**
 * 模版解析工具
 * 
 * @author www.toushibao.com
 *
 */
public class TemplateAnalyseUtils {
	// 函数变量前缀
	private static final String PRE_FUNC = "$Func{";
	// 词典变量前缀
	private static final String PRE_DIC = "$Dic{";
	// 自定义变量前缀
	private static final String PRE_VAR = "$Var{";
	
	private static char eq = 61;  //=等号的ASCII值
	private static char closeBrace = 125; //}闭花括号的ASCII值
	
	private static final Random r = new Random();

	private static void analyse(String template, int startIndex, int type, int itemIndex, Map<String, String> tplVar) {
		boolean hasFunc = template.contains(PRE_FUNC);
		boolean hasDic = template.contains(PRE_DIC);
		boolean hasVar = template.contains(PRE_VAR);
		if (hasFunc && type == 1) { // func
			int funcIndex = template.indexOf(PRE_FUNC);
			int funcEndIndex = template.indexOf("}", funcIndex);
			String func = template.substring(funcIndex, funcEndIndex + 1);
			String funcKey = "F" + StringUtils.formatNumber("" + itemIndex, 3) + "-" + func;
			tplVar.put(funcKey, func);
			//解析var  var需要与func联合使用
			if(hasVar){
				if(template.charAt(funcIndex-1) == eq && template.charAt(funcIndex-2) == closeBrace) {
					int varIndex = template.substring(0,funcIndex).lastIndexOf(PRE_VAR);
					if(varIndex < funcIndex){
						String varName = template.substring(varIndex , funcIndex-1);
						String varKey = "V" + StringUtils.formatNumber("" + itemIndex, 3) + "-" + varName;
						//System.out.println("Func: " + varKey + "===>" + funcKey);
						tplVar.put(varKey, funcKey);
					}
				}
			}
			
			analyse(template.substring(funcEndIndex + 1), startIndex + funcEndIndex + 1, 1, ++itemIndex, tplVar);
		}

		if (hasDic && type == 2) { // dic
			int dicIndex = template.indexOf(PRE_DIC);
			int dicEndIndex = template.indexOf("}", dicIndex);
			String dic = template.substring(dicIndex, dicEndIndex + 1);
			String dicKey = "D" + StringUtils.formatNumber("" + itemIndex, 3) + "-" + dic;
			tplVar.put(dicKey, dic);
			//解析var  var需要与func联合使用
			if(hasVar){
				if(template.charAt(dicIndex-1) == eq && template.charAt(dicIndex-2) == closeBrace) {
					int varIndex = template.substring(0,dicIndex).lastIndexOf(PRE_VAR);
					if(varIndex < dicIndex){
						String varName = template.substring(varIndex , dicIndex-1);
						String varKey = "V" + StringUtils.formatNumber("" + itemIndex, 3) + "-" + varName;
						//System.out.println("Dic : " + varKey + "===>" + dicKey);
						tplVar.put(varKey, dicKey);
					}
				}
			}
			
			analyse(template.substring(dicEndIndex + 1), startIndex + dicEndIndex + 1, 2, ++itemIndex, tplVar);
		}
		
	}

	/**
	 * 抽取模版中的函数,词典,自定义变量等
	 * 
	 * @param template
	 * @return
	 */
	public static Map<String, String> extractVar(String template) {
		Map<String, String> tplVar = new TreeMap<String, String>();
		int itmeIndex = 0;
		boolean hasFunc = template.contains(PRE_FUNC);
		boolean hasDic = template.contains(PRE_DIC);
		if (hasFunc) {
			analyse(template, 0, 1, itmeIndex, tplVar);
		}
		if (hasDic) {
			analyse(template, 0, 2, itmeIndex, tplVar);
		}
		return tplVar;
	}

	/**
	 * 执行模版中的函数
	 * 
	 * @param func
	 *            $Func{}
	 * @throws Exception
	 */
	public static String executeFunc(String func) throws Exception {
		if (func == null) {
			return null;
		}

		String result = null;
		if (func.startsWith(PRE_FUNC)) {
			String funcName = func.substring(PRE_FUNC.length(), func.length() - 1);
			// execute func
			String mName = null;
			String pars = null;
			if (funcName != null && funcName.trim().length() > 0) {
				mName = funcName.substring(0, funcName.indexOf("("));
				pars = funcName.substring(funcName.indexOf("(") + 1, funcName.length() - 1);
			}

			if (mName == null || mName.trim().length() == 0) {
				return null;
			}

			Class<?>[] parameterTypes = null;
			Object[] parameter = null;
			if (pars != null && pars.trim().length() > 0) {
				String[] parArray = pars.split(",");
				parameterTypes = new Class<?>[parArray.length];
				parameter = new Object[parArray.length];
				for (int i = 0; i < parArray.length; i++) {
					String parItem = parArray[i].trim();
					if (parItem.startsWith("\"")) {
						parameterTypes[i] = String.class;
						parameter[i] = parItem.substring(1, parItem.length() - 2);
					} else {
						boolean isInt = NumberUtils.isInteger(parItem);
						if (isInt) {
							parameterTypes[i] = Integer.class;
							parameter[i] = Integer.parseInt(parItem);
						}
					}
				}
			}
			result = "" + FunctionUtils.executeMethod(mName, parameterTypes, parameter);
		}

		return result;
	}

	/**
	 * 查找词典数据，随机取出一个
	 * 
	 * @param dic
	 *            $Dic{}
	 * @throws Exception 
	 */
	public static String searchDic(String dic) throws Exception {
		if (dic == null) {
			return null;
		}

		String result = null;
		if (dic.startsWith(PRE_DIC)) {
			String dicItem = dic.substring(PRE_DIC.length(), dic.length() - 1);
			// get dic
			String[] items = Dictionary.getDicArray(dicItem);
			if (items != null) {
				result = items[r.nextInt(items.length)];
			}
			if(result != null && result.trim().startsWith(PRE_FUNC)){
				result = executeFunc(result);
			}
		}

		return result;
	}

	/**
	 * 执行函数 查找词典 并将得到的结果写回map
	 * 
	 * @param funcAndDic
	 * @throws Exception
	 */
	public static Map<String, String> execute(Map<String, String> funcAndDic) throws Exception {
		if (funcAndDic == null) {
			return null;
		}
		Map<String, String> tmpMap = new TreeMap<String, String>();
		
		for (Entry<String, String> entry : funcAndDic.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();

			String[] items = key.split("-");
			if (items[1].startsWith(PRE_FUNC)) {
				String funcRes = executeFunc(items[1]);
				if (funcRes != null) {
					//entry.setValue(funcRes);
					tmpMap.put(key, funcRes);
				}
			}
			if (items[1].startsWith(PRE_DIC)) {
				String dicRes = searchDic(items[1]);
				if (dicRes != null) {
					//entry.setValue(dicRes);
					tmpMap.put(key, dicRes);
				}
			}
			
			if(items[1].startsWith(PRE_VAR)){
				String varRes = tmpMap.get(value);
				if(varRes != null){
					//entry.setValue(varRes);
					tmpMap.put(key, varRes);
				}
			}
		}
		return tmpMap;
	}

	public static String replace(String template, Map<String, String> funcAndDic) {
		if (funcAndDic == null) {
			return template;
		}
		for (Entry<String, String> entry : funcAndDic.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			String[] items = key.split("-");

			String replaceStr = items[1].replaceAll("\\$", "\\\\\\$").replaceAll("\\{", "\\\\\\{")
					.replaceAll("\\}", "\\\\\\}").replaceAll("\\(", "\\\\\\(").replaceAll("\\)", "\\\\\\)");
			value = value.replaceAll("\\$", "\\\\\\$").replaceAll("\\{", "\\\\\\{").replaceAll("\\}", "\\\\\\}")
					.replaceAll("\\(", "\\\\\\(").replaceAll("\\)", "\\\\\\)");

            if(items[1].startsWith(PRE_VAR)){
            	//System.out.println(items[1] + "+++++++++++++++++++" + replaceStr);
            	template = template.replaceAll(replaceStr + "=", "");
            	template = template.replaceAll(replaceStr, value);
			} else {
				template = template.replaceFirst(replaceStr, value);
			}

		}
		funcAndDic.clear();
		funcAndDic = null;
		
		return template;
	}

	public static void main(String[] args) throws Exception {
		DicInitializer.init();
		String tpl = new String(
				"My name is $Dic{name}, His name is $Dic{name},My age is $Func{intRand(18,30)}, Now is $Func{timestamp()}, This is Test $Func{intRand()}, It is last $Func{doubleRand()}");
		Map<String, String> tplVar = extractVar(tpl);

		System.out.println("============begin==============================");
		System.out.println(tpl);
		System.out.println("");
		for (Entry<String, String> entry : tplVar.entrySet()) {
			System.out.println("key==> " + entry.getKey() + ", value==> " + entry.getValue());
		}

		tplVar = execute(tplVar);
		System.out.println("============execute============================");
		for (Entry<String, String> entry : tplVar.entrySet()) {
			System.err.println("key==> " + entry.getKey() + ", value==> " + entry.getValue());
		}
		tpl = replace(tpl, tplVar);
		System.out.println(tpl);
		System.out.println("============finish================================");
	}
}
