package top.starrysea.service.mail.impl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import top.starrysea.common.Common;
import top.starrysea.dao.IOnlineDao;
import top.starrysea.kql.entity.Entity;
import top.starrysea.mail.Mail;
import top.starrysea.object.dto.Activity;
import top.starrysea.object.dto.Online;

@Service("massMailForActivityService")
public class MassMailForActivityServiceImpl extends MailServiceImpl {
	@Autowired
	private IOnlineDao onlineDao;

	@Override
	public void sendMailService(Entity entity) {
		List<String> mailList = new ArrayList<>();
		Activity activity = (Activity) entity;
		String content = MessageFormat.format(contentTemplate, activity.getActivityId(), activity.getActivityName(),
				activity.getActivityCover(), activity.getActivitySummary());
		List<Online> onlines = onlineDao.getAllOnlineDao(new Online.Builder().onlineStatus((short) 1).build());
		if (!onlines.isEmpty()) {
			onlines.parallelStream().forEach(online -> mailList.add(online.getOnlineEmail()));
			mailCommon.send(new Mail(mailList, "星之海志愿者公会", content));
		}
	}

	@Override
	protected String getHtml() {
		return Common.readEmailHtml("activity_mass_mail.html");
	}

}
