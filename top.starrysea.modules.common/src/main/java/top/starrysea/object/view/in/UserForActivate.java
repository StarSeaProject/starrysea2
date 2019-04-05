package top.starrysea.object.view.in;

import top.starrysea.object.dto.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class UserForActivate {
    @NotBlank(message = "用户id不能为空")
    @Size(max = 20, message = "用户id长度不能超过20")
    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public User toDTO() {
        return new User.Builder().userId(userId).build();
    }
}
