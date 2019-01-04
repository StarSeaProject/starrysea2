package top.starrysea.object.view.in;

import javax.validation.constraints.NotBlank;

import top.starrysea.object.dto.Admin;

public class AdminForLogin {

	@NotBlank(message = "账号不能为空!")
	private String adminUseraccount;
	@NotBlank(message = "密码不能为空!")
	private String adminPassword;

	public String getAdminUseraccount() {
		return adminUseraccount;
	}

	public void setAdminUseraccount(String adminUseraccount) {
		this.adminUseraccount = adminUseraccount;
	}

	public String getAdminPassword() {
		return adminPassword;
	}

	public void setAdminPassword(String adminPassword) {
		this.adminPassword = adminPassword;
	}

	public Admin toDTO() {
		return new Admin.Builder().adminUseraccount(adminUseraccount).adminPassword(adminPassword).build();
	}
}
