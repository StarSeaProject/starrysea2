package top.starrysea.object.view.in;

import top.starrysea.object.dto.User;

import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "登录用对象", description = "登录用对象")
public class UserForLogin {

	@ApiModelProperty(value = "账号", name = "userEmail", example = "test@qq.com")
	@NotBlank(message = "账号不能为空!")
	private String userEmail;

	@ApiModelProperty(value = "密码", name = "userPassword")
	@NotBlank(message = "密码不能为空!")
	private String userPassword;

	@ApiModelProperty(value = "验证码", name = "verifyCode")
	@NotBlank(message = "验证码不能为空!")
	private String verifyCode;

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String Useraccount) {
		this.userEmail = Useraccount;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public String getVerifyCode() {
		return verifyCode;
	}

	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}

	public User toDTO() {
		return new User.Builder().userEmail(userEmail).userPassword(userPassword).build();
	}
}
