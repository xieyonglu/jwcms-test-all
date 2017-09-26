package jwcms.common.converter;

import java.util.List;

/**
 * <h1>IConvertor 接口转换类</h1>
 * 
 * @author 谢永路 (yonglu.xie@ele.me)
 */
public interface IConverter<A, B> {

	/**
	 * 把Service层的model转换为Dao层的model
	 * 
	 * @param source
	 * @return
	 * @throws Exception
	 */
	public B invert(A source) throws Exception;

	/**
	 * 把Dao层的model转换为Service层的model
	 * 
	 * @param source
	 * @return
	 * @throws Exception
	 */
	public A convert(B source) throws Exception;

	/**
	 * 把Service层的model list转换为Dao层的model list
	 * 
	 * @param source
	 * @return
	 * @throws Exception
	 */
	public List<B> invertList(List<A> source) throws Exception;

	/**
	 * 把Dao层的model list转换为Service层的model list
	 * 
	 * @param source
	 * @return
	 * @throws Exception
	 */
	public List<A> convertList(List<B> source) throws Exception;

}
