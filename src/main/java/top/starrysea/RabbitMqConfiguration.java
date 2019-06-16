package top.starrysea;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import static top.starrysea.common.Const.ORIGINAL_FUNDING_QUEUE;
import static top.starrysea.common.Const.CANCEL_FUNDING_QUEUE;
import static top.starrysea.common.Const.FUNDING_EXCHANGE;
import static top.starrysea.common.Const.ORDERS_EXCHANGE;
import static top.starrysea.common.Const.ORIGINAL_ORDER_QUEUE;
import static top.starrysea.common.Const.CANCEL_ORDER_QUEUE;

@Configuration
public class RabbitMqConfiguration {
	@Bean
	public DirectExchange orderExchange() {
		return new DirectExchange(ORDERS_EXCHANGE, true, false);
	}

	@Bean
	public DirectExchange fundingExchange() {
		return new DirectExchange(FUNDING_EXCHANGE, true, false);
	}

	@Bean
	public Queue cancelOrderQueue() {
		return new Queue(CANCEL_ORDER_QUEUE, true);
	}

	@Bean
	public Queue cancelFundingQueue() {
		return new Queue(CANCEL_FUNDING_QUEUE, true);
	}

	@Bean
	public Queue originalFundingQueue() {
		return QueueBuilder.durable(ORIGINAL_FUNDING_QUEUE).withArgument("x-dead-letter-exchange", FUNDING_EXCHANGE)
				.withArgument("x-dead-letter-routing-key", CANCEL_FUNDING_QUEUE).build();
	}

	@Bean
	public Queue originalOrderQueue() {
		return QueueBuilder.durable(ORIGINAL_ORDER_QUEUE).withArgument("x-dead-letter-exchange", ORDERS_EXCHANGE)
				.withArgument("x-dead-letter-routing-key", CANCEL_ORDER_QUEUE).build();
	}

	@Bean
	public Binding originalOrderBinding() {
		return BindingBuilder.bind(originalOrderQueue()).to(orderExchange()).with(ORIGINAL_ORDER_QUEUE);
	}

	@Bean
	public Binding cancelOrderBinding() {
		return BindingBuilder.bind(cancelOrderQueue()).to(orderExchange()).with(CANCEL_ORDER_QUEUE);
	}

	@Bean
	public Binding originalFundingBinding() {
		return BindingBuilder.bind(originalFundingQueue()).to(fundingExchange()).with(ORIGINAL_FUNDING_QUEUE);
	}

	@Bean
	public Binding cancelFundingBinding() {
		return BindingBuilder.bind(cancelFundingQueue()).to(fundingExchange()).with(CANCEL_FUNDING_QUEUE);
	}
}
