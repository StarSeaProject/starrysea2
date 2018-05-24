package top.starrysea.service.mail.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import top.starrysea.dao.IOnlineDao;
import top.starrysea.kql.entity.Entity;
import top.starrysea.mail.Mail;
import top.starrysea.object.dto.Online;
import top.starrysea.object.dto.Work;

//这是用于作品新增时推送的邮件服务
@Service("workMailService")
public class WorkMailServiceImpl extends MailServiceImpl {

	@Autowired
	private IOnlineDao onlineDao;

	@Override
	public void sendMailService(Entity entity) {
		List<String> mailList = new ArrayList<>();
		List<Online> receivers = onlineDao.getAllOnlineDao();
		Work work = (Work) entity;
		receivers.parallelStream().forEach(receiver -> mailList.add(receiver.getOnlineEmail()));
		mailCommon.send(new Mail(mailList, "星之海志愿者公会", work.getWorkPdfpath()));
	}

	@Override
	protected String getHtml() {
		// 暂时还不需要推送作品信息
		return null;
	}

}
