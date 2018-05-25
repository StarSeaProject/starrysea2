package top.starrysea.service.mail.impl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import top.starrysea.common.Common;
import top.starrysea.kql.entity.Entity;
import top.starrysea.mail.Mail;
import top.starrysea.object.dto.Orders;

@Service("sendOrderMailService")
public class SendOrderMailServiceImpl extends MailServiceImpl {

	@Override
	public void sendMailService(Entity entity) {
		List<String> mailList = new ArrayList<>();
		Orders order = (Orders) entity;
		String content = MessageFormat.format(contentTemplate, order.getOrderExpressnum(), order.getOrderNum(),
				order.getOrderNum());
		mailList.add(order.getOrderEMail());
		mailCommon.send(new Mail(mailList, "星之海志愿者公会", content));
	}

	@Override
	protected String getHtml() {
		return Common.readEmailHtml("send_order_mail.html");
	}

}
