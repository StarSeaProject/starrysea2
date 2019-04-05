package top.starrysea.object.view.in;

import javax.validation.constraints.NotBlank;

public class UserForActivate {
    @NotBlank(message = "激活码不能为空")
    private String activateCode;

    public String getActivateCode() {
        return activateCode;
    }

    public void setActivateCode(String activateCode) {
        this.activateCode = activateCode;
    }
}
