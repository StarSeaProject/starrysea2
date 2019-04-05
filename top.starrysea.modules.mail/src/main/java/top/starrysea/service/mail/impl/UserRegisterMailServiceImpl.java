package top.starrysea.service.mail.impl;

import org.springframework.stereotype.Service;
import top.starrysea.common.Common;
import top.starrysea.kql.entity.Entity;
import top.starrysea.mail.Mail;
import top.starrysea.object.dto.User;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

@Service("userRegisterMailService")
public class UserRegisterMailServiceImpl extends MailServiceImpl {
	@Override
	public void sendMailService(Entity entity) {
		List<String> mailList = new ArrayList<>();
		User user = (User) entity;
		String content = MessageFormat.format(contentTemplate, user.getUsername(), user.getUserId());
		mailList.add(user.getUserEmail());
		mailCommon.send(new Mail(mailList, "星之海志愿者公会-用户激活", content));
	}

	@Override
	protected String getHtml() {
		return Common.readEmailHtml("user_register_mail.html");
	}
}
