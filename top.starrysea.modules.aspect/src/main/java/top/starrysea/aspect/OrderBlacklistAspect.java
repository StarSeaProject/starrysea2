package top.starrysea.aspect;

import java.util.List;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import top.starrysea.common.ServiceResult;
import top.starrysea.object.dto.Online;
import top.starrysea.object.dto.Orders;
import top.starrysea.service.IOnlineService;
import static top.starrysea.common.ResultKey.*;

@Component
@Aspect
public class OrderBlacklistAspect {

	@Autowired
	private IOnlineService onlineService;

	@Around("execution(* top.starrysea.service.impl.OrderServiceImpl.addOrderService(..))")
	public Object isPeopleInBlacklist(ProceedingJoinPoint pjp) throws Throwable {
		Orders order = (Orders) pjp.getArgs()[0];
		List<Online> blackPhoneList = onlineService
				.queryOnlineService(
						new Online.Builder().onlinePhone(order.getOrderPhone()).onlineStatus((short) 2).build())
				.getResult(LIST_1);
		List<Online> blackEmailList = onlineService
				.queryOnlineService(
						new Online.Builder().onlineEmail(order.getOrderEMail()).onlineStatus((short) 2).build())
				.getResult(LIST_1);
		if (!blackEmailList.isEmpty() || !blackPhoneList.isEmpty()) {
			return ServiceResult.of(false).setErrInfo("下单失败，请与管理员联系");
		}
		return pjp.proceed();
	}
}
