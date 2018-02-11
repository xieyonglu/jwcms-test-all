package jwcms.test.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.util.Assert;

/**
 * 类FunctionContainer.java的实现描述：自定义函数容器
 * 
 */
public class FunctionContainer {

	static Map<String, String> functions = new ConcurrentHashMap<String, String>();

	/**
	 * @TODO:待实现,自定义函数列表由外部注入
	 * @return
	 */
	public static Map<String, String> getFunctions() {
		return functions;
	}

	/**
	 * @param functionName
	 * @param functionString
	 */
	public static void addFunction(String functionName, String functionString) {
		functions.put(functionName, functionString);
	}

	/**
	 * @param functionName
	 */
	public static void removeFunction(String functionName) {
		functions.remove(functionName);
	}

	public static void addFunctions(Map<String, String> function) {
		Assert.notNull(function);
		functions.putAll(function);
	}

	public static void clearFunction() {
		functions.clear();
	}
}
