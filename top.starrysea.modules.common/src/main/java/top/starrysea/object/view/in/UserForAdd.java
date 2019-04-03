package top.starrysea.object.view.in;

import top.starrysea.object.dto.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UserForAdd {

    @NotBlank(message = "邮箱不能为空")
    @Size(max = 100, message = "邮箱长度不能超过100")
    private String userEmail;
    @NotBlank(message = "密码不能为空")
    @Size(max = 30, message = "密码长度不能超过30")
    private String userPassword;
    @NotBlank(message = "昵称不能为空")
    @Size(max = 30, message = "昵称长度不能超过30")
    private String username;
    @NotNull(message = "主推角色不能为空")
    private short osu1;
    @NotNull(message = "第二推的角色不能为空")
    private short osu2;
    @NotNull(message = "第三推的角色不能为空")
    private short osu3;

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public short getOsu1() {
        return osu1;
    }

    public void setOsu1(short osu1) {
        this.osu1 = osu1;
    }

    public short getOsu2() {
        return osu2;
    }

    public void setOsu2(short osu2) {
        this.osu2 = osu2;
    }

    public short getOsu3() {
        return osu3;
    }

    public void setOsu3(short osu3) {
        this.osu3 = osu3;
    }

    public User toDTO() {
        return new User.Builder().userEmail(userEmail).username(username)
                .userPassword(userPassword).osu1(osu1).osu2(osu2).osu3(osu3).build();
    }
}
