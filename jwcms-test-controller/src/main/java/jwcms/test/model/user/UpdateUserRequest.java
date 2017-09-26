package jwcms.test.model.user;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value="User", description="用户")
public class UpdateUserRequest {
	
	@ApiModelProperty("用户ID")
	@NotNull(message = "用户Id不能为空")
	private Long id;

	@ApiModelProperty("主键ID")
	@NotNull(message = "A不能为空")
	private String a;
	
	@ApiModelProperty("属性B")
	@NotNull(message = "B不能为空")
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

