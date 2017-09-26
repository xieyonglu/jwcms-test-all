package jwcms.test.exception;

import jwcms.common.exception.Code;
import jwcms.common.exception.Desc;
import jwcms.common.exception.IExceptionCode;

/**
 * <h1>用户信息异常枚举</h1>
 * 
 * @author 谢永路 (yonglu.xie@ele.me)
 */
public enum UserExceptionCode implements IExceptionCode {

	/**
	 * 用户没有被找到
	 */
	@Desc("用户没有被找到")
	@Code(1000)
	USER_NOT_FOUND,

	;
	
}