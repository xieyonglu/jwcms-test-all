package jwcms.test.manager;

import java.util.List;

import jwcms.test.model.Function;



public interface FunctionManager {

	public List<Function> listAllFunctions();

	/**
	 * 加载所有函数
	 */
	public void loadAllFunction();

	/**
	 * 删除所有加载的函数
	 */
	public void refreshLoadAllFunction();

	/**
	 * 重新加载函数
	 */
	public void loadFunction(Function f) throws Exception;

	

	/**
	 * 从数据库重新加载函数
	 */
	public void loadFunctionByDb();

	/**
	 * 从zk重新加载函数
	 */
	public void loadFunctionByZk();

	/**
	 * 从zk中移除某个函数
	 */
	void removeFunction(Function f);

}
