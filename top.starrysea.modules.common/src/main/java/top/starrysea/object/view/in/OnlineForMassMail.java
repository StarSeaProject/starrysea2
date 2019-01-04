package top.starrysea.object.view.in;

import javax.validation.constraints.Size;
import javax.validation.constraints.NotBlank;

public class OnlineForMassMail {
	@NotBlank(message = "邮件标题不能为空")
	@Size(max = 30, message = "邮件标题长度不能超过30")
	private String MailTitle;
	@NotBlank(message = "邮件内容不能为空")
	private String mailContent;

	public String getMailTitle() {
		return MailTitle;
	}

	public void setMailTitle(String mailTitle) {
		MailTitle = mailTitle;
	}

	public String getMailContent() {
		return mailContent;
	}

	public void setMailContent(String mailContent) {
		this.mailContent = mailContent;
	}

}
