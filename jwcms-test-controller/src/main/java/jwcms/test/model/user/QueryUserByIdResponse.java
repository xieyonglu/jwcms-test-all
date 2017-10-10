package jwcms.test.model.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value="QueryUserByIdResponse", description="根据用户ID查询用户响应值")
public class QueryUserByIdResponse {
	
	@ApiModelProperty("主键ID")
	private Long id;

	@ApiModelProperty("属性A")
	private String a;

	@ApiModelProperty("属性B")
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
