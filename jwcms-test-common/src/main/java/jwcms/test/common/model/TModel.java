package jwcms.test.common.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * <h1>Dao层基础Model</h1>
 * 
 * @author yonglu.xie
 * @date 2017/07/21
 * @version 1.0.0
 *
 */
public class TModel implements Serializable {

	private static final long serialVersionUID = -8794707252731558851L;

	private String isDelete;

	private String creator;

	private Timestamp createdAt;

	private String modifier;

	private Timestamp updatedAt;

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

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public String getModifier() {
		return modifier;
	}

	public void setModifier(String modifier) {
		this.modifier = modifier;
	}

	public Timestamp getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}

}
