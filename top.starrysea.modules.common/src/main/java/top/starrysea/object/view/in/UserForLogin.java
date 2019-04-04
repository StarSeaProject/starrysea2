package top.starrysea.object.view.in;

import top.starrysea.object.dto.User;

import javax.validation.constraints.NotBlank;

public class UserForLogin {
    @NotBlank(message = "账号不能为空!")
    private String userEmail;
    @NotBlank(message = "密码不能为空!")
    private String userPassword;

    public String getAdminUseraccount() {
        return userEmail;
    }

    public void setAdminUseraccount(String adminUseraccount) {
        this.userEmail = adminUseraccount;
    }

    public String getAdminPassword() {
        return userPassword;
    }

    public void setAdminPassword(String adminPassword) {
        this.userPassword = adminPassword;
    }

    public User toDTO(){
        return new User.Builder().userEmail(userEmail).userPassword(userPassword).build();
    }
}
