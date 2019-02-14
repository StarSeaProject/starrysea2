package top.starrysea.mail;

import static top.starrysea.common.Const.CHARSET;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class MailCommon implements InitializingBean {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private ExecutorService threadPool;
	@Autowired
	private JavaMailSenderImpl mailSender;

	@Override
	public void afterPropertiesSet() throws Exception {
		threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
	}

	public void send(Mail mail) {
		threadPool.execute(new MailTask(mail));
	}

	private class MailTask implements Runnable {

		private Mail mail;

		public MailTask(Mail mail) {
			this.mail = mail;
		}

		@Override
		public void run() {
			MimeMessage mimeMessage = mailSender.createMimeMessage();
			MimeMessageHelper mimeMessageHelper;
			try {
				InternetAddress[] mailList = new InternetAddress[mail.getTo().size()];
				for (int i = 0; i < mail.getTo().size(); i++) {
					mailList[i] = new InternetAddress(mail.getTo().get(i));
				}
				mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, CHARSET);
				mimeMessageHelper.setBcc(mailList);
				String nick = MimeUtility.encodeText("星之海志愿者公会");
				mimeMessageHelper.setFrom(new InternetAddress(nick + "<mumuzhizhi@starrysea.top>"));
				mimeMessageHelper.setSubject(mail.getTitle());
				mimeMessageHelper.setText(mail.getContent(), true);
				logger.info("准备发送邮件给" + mail.getTo());
				mailSender.send(mimeMessage);
				logger.info("已经成功发送邮件给" + mail.getTo());
			} catch (MessagingException | UnsupportedEncodingException e) {
				logger.error(e.getMessage(), e);
			}
		}
	}
}
