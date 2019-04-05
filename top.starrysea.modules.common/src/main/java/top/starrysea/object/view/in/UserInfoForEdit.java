package top.starrysea.object.view.in;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import top.starrysea.object.dto.User;

public class UserInfoForEdit {

	@NotBlank(message = "昵称不能为空")
	private String username;
	private Short osuPerson = 0;
	private Short osuTeam = 0;
	private Short osuGrade = 0;
	private Short osuGroup = 0;
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

	public Short getOsuGroup() {
		return osuGroup;
	}

	public void setOsuGroup(Short osuGroup) {
		this.osuGroup = osuGroup;
	}

	public Short getIsDD() {
		return isDD;
	}

	public void setIsDD(Short isDD) {
		this.isDD = isDD;
	}

	public User toDTO() {
		return new User.Builder().username(username).osuPerson(osuPerson).osuTeam(osuTeam).osuGrade(osuGrade)
				.osuGroup(osuGroup).build();
	}
}
