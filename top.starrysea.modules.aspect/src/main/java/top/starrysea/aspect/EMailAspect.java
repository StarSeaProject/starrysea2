package top.starrysea.aspect;

import javax.annotation.Resource;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import top.starrysea.common.ServiceResult;
import top.starrysea.dao.IOrderDao;
import top.starrysea.object.dto.Online;
import top.starrysea.object.dto.OrderDetail;
import top.starrysea.object.dto.Orders;
import top.starrysea.object.dto.Work;
import top.starrysea.service.IOnlineService;
import top.starrysea.service.mail.IMailService;

import static top.starrysea.common.ResultKey.*;

import java.util.List;

@Component
@Aspect
public class EMailAspect {

	@Resource(name = "workMailService")
	private IMailService workMailService;
	@Resource(name = "orderMailService")
	private IMailService orderMailService;
	@Resource(name = "sendOrderMailService")
	private IMailService sendOrderMailService;
	@Resource(name = "deleteOrderMailService")
	private IMailService deleteOrderMailService;
	@Resource
	private IOrderDao orderDao;
	@Resource
	private IOnlineService onlineService;

	@AfterReturning(value = "execution(* top.starrysea.service.impl.WorkServiceImpl.addWorkService(..))", returning = "serviceResult")
	public void sendWorkEmail(ServiceResult serviceResult) {
		workMailService.sendMailService((Work) serviceResult.getResult(WORK));
	}

	@AfterReturning(value = "execution(* top.starrysea.service.impl.OrderServiceImpl.addOrderService(..))", returning = "serviceResult")
	public void sendOrderEmail(ServiceResult serviceResult) {
		if (serviceResult.isSuccessed())
			orderMailService.sendMailService((List<OrderDetail>) serviceResult.getResult(LIST_1));
	}

	@AfterReturning(value = "execution(* top.starrysea.service.impl.OrderServiceImpl.modifyOrderService(..))", returning = "serviceResult")
	public void sendSendOrderEmail(ServiceResult serviceResult) {
		sendOrderMailService.sendMailService((Orders) serviceResult.getResult(ORDER));
	}

	@Before(value = "execution(* top.starrysea.service.impl.OrderServiceImpl.removeOrderService(..))")
	public void deleteOrderMail(JoinPoint jp) {
		Orders order = (Orders) jp.getArgs()[0];
		deleteOrderMailService.sendMailService(orderDao.getOrderDao(order));
	}

	@After(value = "execution(* top.starrysea.service.impl.OrderServiceImpl.addOrderService(..))")
	public void addMailOnline(JoinPoint pjp) {
		Orders order = (Orders) pjp.getArgs()[0];
		onlineService.addMailService(new Online.Builder().onlineEmail(order.getOrderEMail()).build());
	}
}
