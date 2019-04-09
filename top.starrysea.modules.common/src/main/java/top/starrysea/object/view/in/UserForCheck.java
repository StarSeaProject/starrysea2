package top.starrysea.object.view.in;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import top.starrysea.object.dto.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@ApiModel(value = "用户检验用对象", description = "用户检验用对象")
public class UserForCheck {

	@ApiModelProperty(value="注册邮箱",name="userEmail")
	@NotBlank(message = "邮箱不能为空")
	@Size(max = 100, message = "邮箱长度不能超过100")
	private String userEmail;

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public User toDTO() {
		return new User.Builder().userEmail(userEmail).build();
	}

}
