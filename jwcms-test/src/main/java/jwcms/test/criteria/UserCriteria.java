package jwcms.test.criteria;

import jwcms.test.model.User;


/**
 * <h1>咨询服务查询条件</h1>
 * 
 * @author yonglu.xie
 * @date 2017/06/27
 *
 */
public class UserCriteria extends User {

	private static final long serialVersionUID = -3803331863438751582L;
	
	private Long pageIndex;

	private Integer pageSize;

	public Long getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(Long pageIndex) {
		this.pageIndex = pageIndex;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	} 

}
