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
    private String userPassword;
    @NotBlank(message = "昵称不能为空")
    @Size(max = 30, message = "昵称长度不能超过30")
    private String username;
    @NotNull(message = "所推角色不能为空")
    private short osuPerson;
    @NotNull(message = "所推小队不能为空")
    private short osuTeam;
    @NotNull(message = "所推年级不能为空")
    private short osuGrade;
    @NotNull(message = "所推团队不能为空")
    private short osuGroup;
    @NotNull(message = "必须选择是DD或者不是,不允许薛定谔的DD状态")
    private short isDD;

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

    public short getOsuPerson() {
        return osuPerson;
    }

    public void setOsuPerson(short osuPerson) {
        this.osuPerson = osuPerson;
    }

    public short getOsuTeam() {
        return osuTeam;
    }

    public void setOsuTeam(short osuTeam) {
        this.osuTeam = osuTeam;
    }

    public short getOsuGrade() {
        return osuGrade;
    }

    public void setOsuGrade(short osuGrade) {
        this.osuGrade = osuGrade;
    }

    public short getOsuGroup() {
        return osuGroup;
    }

    public void setOsuGroup(short osuGroup) {
        this.osuGroup = osuGroup;
    }

    public short getIsDD() {
        return isDD;
    }

    public void setIsDD(short isDD) {
        this.isDD = isDD;
    }


    public User toDTO() {
        return new User.Builder().userEmail(userEmail).username(username)
                .userPassword(userPassword).osuPerson(osuPerson).osuTeam(osuTeam)
                .osuGrade(osuGrade).osuGroup(osuGroup).isDD(isDD).build();
    }
}
