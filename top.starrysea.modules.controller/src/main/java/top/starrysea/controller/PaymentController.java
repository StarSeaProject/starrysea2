package top.starrysea.controller;

import javax.annotation.Resource;

import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import top.starrysea.common.Common;
import top.starrysea.common.ModelAndViewFactory;
import top.starrysea.object.view.in.PayelvesCallback;
import top.starrysea.trade.PayelvesPayBackParam;
import top.starrysea.trade.PayelvesPayNotify;
import top.starrysea.trade.service.ITradeService;

@Controller
public class PaymentController {
	@Resource(name = "payelvesTradeService")
	private ITradeService payelvesTradeService;

	@RequestMapping("/callback")
	public ModelAndView payelvesPaymentCallback(PayelvesCallback callback, Device device) {
		System.out.println(Common.toJson(callback));
		return ModelAndViewFactory.newSuccessMav("您已下单成功，之后将会为您派送！", device);
	}

	@RequestMapping("/notify")
	@ResponseBody
	public String payelvesPaymentNotify(PayelvesPayNotify callback) {
		try {
			PayelvesPayBackParam bo = Common.toObject(callback.getBackPara(), PayelvesPayBackParam.class);
			System.out.println(Common.toJson(bo));
		} catch (Exception e) {
		}
		return "SUCCESS";

	}
}
