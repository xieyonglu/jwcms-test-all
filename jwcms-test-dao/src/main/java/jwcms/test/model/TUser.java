package jwcms.test.model;

import jwcms.test.common.model.TModel;

/**
 * 数据对象
 * 
 * @since 2017-06-26
 */
public class TUser extends TModel {

	private static final long serialVersionUID = 149846600751777863L;

	private Long id;

	private String a;

	private String b;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getA() {
		return a;
	}

	public void setA(String a) {
		this.a = a;
	}

	public String getB() {
		return b;
	}

	public void setB(String b) {
		this.b = b;
	}

}

