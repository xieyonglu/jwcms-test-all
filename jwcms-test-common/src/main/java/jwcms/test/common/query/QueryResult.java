package jwcms.test.common.query;

import java.io.Serializable;
import java.util.List;

/**
 * <h1>查询结果</h1>
 * 
 * @author yonglu.xie
 * @date 2017/06/27
 *
 * @param <T>
 */
public class QueryResult<T> implements Serializable {

	private static final long serialVersionUID = 1117309620808398677L;

	private List<T> result;
	
	private Long totalCount;

	public List<T> getResult() {
		return result;
	}

	public void setResult(List<T> result) {
		this.result = result;
	}

	public Long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Long totalCount) {
		this.totalCount = totalCount;
	}
	
}
