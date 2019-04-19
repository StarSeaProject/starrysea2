package top.starrysea.mq;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/***
 * 消息发送工具类
 * 
 * @author liuyang
 *
 */
@Component("messageSender")
public class MessageSender {
	@Autowired
	private RabbitTemplate rabbitTemplate;

	public void sendMessage(String exchange, String route, String message) {
		rabbitTemplate.convertAndSend(exchange, route, message);
	}

	public void sendMessage(String exchange, String route, String message, long ttl) {
		rabbitTemplate.convertAndSend(exchange, route, message, new DelayMessagePostProcessor(ttl));
	}
}

class DelayMessagePostProcessor implements MessagePostProcessor {
	private long ttl = 0;

	public DelayMessagePostProcessor(long ttl) {
		this.ttl = ttl;
	}

	@Override
	public Message postProcessMessage(Message message) throws AmqpException {
		message.getMessageProperties().setExpiration(Long.toString(ttl));
		return message;
	}

}