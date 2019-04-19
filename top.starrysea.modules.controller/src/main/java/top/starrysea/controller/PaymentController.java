package top.starrysea.controller;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import top.starrysea.common.Common;
import top.starrysea.common.ModelAndViewFactory;
import top.starrysea.common.ServiceResult;
import top.starrysea.object.view.in.PayelvesCallback;
import top.starrysea.service.IActivityService;
import top.starrysea.service.IOrderService;
import top.starrysea.trade.PayelvesPayBackParam;
import top.starrysea.trade.PayelvesPayNotify;
import top.starrysea.trade.service.ITradeService;

@Controller
public class PaymentController {
	@Resource(name = "payelvesTradeService")
	private ITradeService payelvesTradeService;
	@Autowired
	private IOrderService orderService;
	@Autowired
	private IActivityService activityService;

	@RequestMapping("/callback")
	public ModelAndView payelvesPaymentCallback(PayelvesCallback callback, Device device) {
		return ModelAndViewFactory.newSuccessMav("交易成功!", device);
	}

	@RequestMapping("/notify")
	@ResponseBody
	public String payelvesPaymentNotify(PayelvesPayNotify payNotify) {
		try {
			PayelvesPayBackParam bo = Common.toObject(payNotify.getBackPara(), PayelvesPayBackParam.class);
			payNotify.setBackPara(Common.toJson(bo));
			ServiceResult validateResult = payelvesTradeService.validateNotifyParamService(payNotify);
			if (!validateResult.isSuccessed()) {
				return "ERROR";
			}
			ServiceResult serviceResult = ServiceResult.of(false);
			if (bo.getType() == 1) {
				serviceResult = orderService.notifyOrderService(payNotify.toOrders());
			} else if (bo.getType() == 2) {
				serviceResult = activityService.notifyParticipateFundingService(payNotify.toFunding());
			}
			return serviceResult.isSuccessed() ? "SUCCESS" : "ERROR";
		} catch (Exception e) {
			e.printStackTrace();
			return "ERROR";
		}
	}
}
