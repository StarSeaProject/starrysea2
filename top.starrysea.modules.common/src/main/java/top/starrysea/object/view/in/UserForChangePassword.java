package top.starrysea.object.view.in;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;

@ApiModel(value = "修改密码用对象", description = "修改密码用对象")
public class UserForChangePassword {
	@ApiModelProperty(value="旧密码",name="currentPassword")
	@NotBlank(message = "旧密码不能为空")
	private String currentPassword;
	@ApiModelProperty(value="新密码",name="newPassword")
	@NotBlank(message = "新密码不能为空")
	private String newPassword;

	public String getCurrentPassword() {
		return currentPassword;
	}

	public void setCurrentPassword(String currentPassword) {
		this.currentPassword = currentPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
}
