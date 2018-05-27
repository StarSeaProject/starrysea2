package top.starrysea.service.mail.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import top.starrysea.dao.IOnlineDao;
import top.starrysea.mail.Mail;
import top.starrysea.object.dto.Online;

@Service("massMailService")
public class MassMailServiceImpl extends MailServiceImpl {
	@Autowired
	private IOnlineDao onlineDao;

	@Override
	public void sendMailService(String title, String content) {
		List<String> mailList = new ArrayList<>();
		List<Online> onlines = onlineDao.getAllOnlineDao(new Online.Builder().onlineStatus((short) 1).build());
		if (!onlines.isEmpty()) {
			onlines.parallelStream().forEach(online -> mailList.add(online.getOnlineEmail()));
			mailCommon.send(new Mail(mailList, title, content));
		}
	}

	@Override
	protected String getHtml() {
		return null;
	}

}
