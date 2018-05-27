package top.starrysea.object.view.in;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

public class OnlineForMassMail {
	@NotEmpty(message = "邮件标题不能为空")
	@Length(max = 30, message = "邮件标题长度不能超过30")
	private String MailTitle;
	@NotEmpty(message = "邮件内容不能为空")
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
