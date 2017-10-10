package jwcms.test.model.user;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value="PageUserResponse", description="分页查询用户响应值")
public class PageUserResponse {
	
	public class UserResponse {
		
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
	
	@ApiModelProperty("结果")
	private List<UserResponse> result;
	
	@ApiModelProperty("总数量")
	private Long totalCount;

	public List<UserResponse> getResult() {
		return result;
	}

	public void setResult(List<UserResponse> result) {
		this.result = result;
	}

	public Long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Long totalCount) {
		this.totalCount = totalCount;
	}

}
