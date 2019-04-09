package top.starrysea.object.view.in;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;

@ApiModel(value = "用户激活用对象", description = "用户激活用对象")
public class UserForActivate {
    @ApiModelProperty(value="激活码",name="activateCode")
    @NotBlank(message = "激活码不能为空")
    private String activateCode;

    public String getActivateCode() {
        return activateCode;
    }

    public void setActivateCode(String activateCode) {
        this.activateCode = activateCode;
    }
}
