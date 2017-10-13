package jwcms.test.model.user;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jwcms.test.validator.Phone;

@ApiModel(value="User", description="用户")
public class CreateUserRequest {

	@ApiModelProperty("属性A")
	@NotNull(message = "A不能为空")
	private String a;
	
	@ApiModelProperty("属性B")
	@NotNull(message = "B不能为空")
	private String b;
	
	@NotBlank
	@Phone
	private String phone;
	
	@Min(value = 1, message="id值必须大于0")
    private long id;

    @NotBlank
    @Length(min=6, max=12, message="昵称长度为6到12位")
    private String nickname;

    @Min(value=18, message="{validation.param.age}")
    private int age;

    @NotBlank
    @Email(message="{validation.common.format.error}")
    private String email;

    @NotBlank
    @Pattern(regexp="^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,20}$", message="由6-21字母和数字组成，不能是纯数字或纯英文")
    private String password;

    @NotBlank
    @Length(min=3, max=10, message="{validation.common.not.range}")
    private String username;

    @NotBlank
    @Pattern(regexp="^((13[0-9])|(15[^4,\\D])|(18[0,3-9]))\\d{8}$", message="手机号格式不正确")
    private String phone1;

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

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
}
