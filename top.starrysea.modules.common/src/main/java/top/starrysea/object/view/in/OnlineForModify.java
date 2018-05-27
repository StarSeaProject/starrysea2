package top.starrysea.object.view.in;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;

import top.starrysea.object.dto.Online;

public class OnlineForModify {
	@NotNull(message = "订阅邮箱不能为空")
	@Email(message = "输入的邮箱地址不是合法的")
	private String onlineEmail;
	@NotNull(message = "订阅状态不能为空")
	private short onlineStatus;
	@NotNull(message = "手机号不能为空")
	@Length(max = 15, message = "手机长度不能超过15")
	private String onlinePhone;

	public String getOnlineEmail() {
		return onlineEmail;
	}

	public void setOnlineEmail(String onlineEmail) {
		this.onlineEmail = onlineEmail;
	}

	public short getOnlineStatus() {
		return onlineStatus;
	}

	public void setOnlineStatus(short onlineStatus) {
		this.onlineStatus = onlineStatus;
	}

	public String getOnlinePhone() {
		return onlinePhone;
	}

	public void setOnlinePhone(String onlinePhone) {
		this.onlinePhone = onlinePhone;
	}

	public Online toDTO() {
		return new Online.Builder().onlineEmail(onlineEmail).onlineStatus(onlineStatus).onlinePhone(onlinePhone)
				.build();
	}
}
