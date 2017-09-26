package jwcms.test.model;

import jwcms.test.common.model.Model;

/**
 * 数据对象
 * 
 * @since 2017-06-26
 */
//@ApiModel(value="User", description="用户")
public class User extends Model {

	private static final long serialVersionUID = 149846600751777863L;

//	@ApiModelProperty("主键ID")
	private Long id;

//	@ApiModelProperty("属性A")
//	@NotNull(message = "A不能为空")
	private String a;
	
//	@ApiModelProperty("属性B")
//	@NotNull(message = "B不能为空")
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

