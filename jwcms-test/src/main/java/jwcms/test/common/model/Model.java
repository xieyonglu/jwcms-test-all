package jwcms.test.common.model;

import java.io.Serializable;

/**
 * <h1>Service层基础Model</h1>
 * 
 * @author yonglu.xie
 * @date 2017/07/21
 * @version 1.0.0
 *
 */
public class Model implements Serializable {

	private static final long serialVersionUID = -8794707252731558851L;

	private String isDelete;

	private String creator;

	private Long createdAt;

	private String modifier;

	private Long updatedAt;

	public String getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(String isDelete) {
		this.isDelete = isDelete;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Long getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Long createdAt) {
		this.createdAt = createdAt;
	}

	public String getModifier() {
		return modifier;
	}

	public void setModifier(String modifier) {
		this.modifier = modifier;
	}

	public Long getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Long updatedAt) {
		this.updatedAt = updatedAt;
	}

}
