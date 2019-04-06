package top.starrysea.object.view.in;

import javax.validation.constraints.NotBlank;

public class UserForChangePassword {
	@NotBlank(message = "旧密码不能为空")
	private String currentPassword;
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
