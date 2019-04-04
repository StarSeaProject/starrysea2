package top.starrysea.object.view.in;

import top.starrysea.object.dto.User;

import javax.validation.constraints.NotBlank;

public class UserForLogin {
    @NotBlank(message = "账号不能为空!")
    private String userEmail;
    @NotBlank(message = "密码不能为空!")
    private String userPassword;

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String Useraccount) {
        this.userEmail = Useraccount;
    }

    public String getPassword() {
        return userPassword;
    }

    public void setPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public User toDTO(){
        return new User.Builder().userEmail(userEmail).userPassword(userPassword).build();
    }
}
