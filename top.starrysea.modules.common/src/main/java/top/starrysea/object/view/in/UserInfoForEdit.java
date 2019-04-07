package top.starrysea.object.view.in;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import top.starrysea.object.dto.User;

@ApiModel(value = "修改个人信息用对象", description = "修改个人信息用对象")
public class UserInfoForEdit {

	@ApiModelProperty(value = "昵称", name = "username")
	@NotBlank(message = "昵称不能为空")
	private String username;

	@ApiModelProperty(value = "所推角色", name = "osuPerson")
	private Short osuPerson = 0;

	@ApiModelProperty(value = "所推小队", name = "osuTeam")
	private Short osuTeam = 0;

	@ApiModelProperty(value = "所推年级", name = "osuGrade")
	private Short osuGrade = 0;

	@ApiModelProperty(value = "是否DD", name = "isDD")
	@NotNull(message = "是否DD不能为空")
	private Short isDD;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Short getOsuPerson() {
		return osuPerson;
	}

	public void setOsuPerson(Short osuPerson) {
		this.osuPerson = osuPerson;
	}

	public Short getOsuTeam() {
		return osuTeam;
	}

	public void setOsuTeam(Short osuTeam) {
		this.osuTeam = osuTeam;
	}

	public Short getOsuGrade() {
		return osuGrade;
	}

	public void setOsuGrade(Short osuGrade) {
		this.osuGrade = osuGrade;
	}

	public Short getIsDD() {
		return isDD;
	}

	public void setIsDD(Short isDD) {
		this.isDD = isDD;
	}

	public User toDTO() {
		return new User.Builder().username(username).osuPerson(osuPerson).osuTeam(osuTeam).osuGrade(osuGrade).build();
	}
}
