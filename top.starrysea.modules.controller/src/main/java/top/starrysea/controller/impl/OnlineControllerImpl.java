package top.starrysea.controller.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import reactor.core.publisher.Mono;
import top.starrysea.common.ServiceResult;
import top.starrysea.controller.IOnlineController;
import top.starrysea.object.dto.Online;
import top.starrysea.object.view.in.OnlineForAdd;
import top.starrysea.object.view.in.OnlineForMassMail;
import top.starrysea.object.view.in.OnlineForModify;
import top.starrysea.service.IOnlineService;
import top.starrysea.service.mail.IMailService;

import static top.starrysea.common.ResultKey.*;
import static top.starrysea.common.ModelAndViewFactory.*;

@Controller
@RequestMapping("/online")
public class OnlineControllerImpl implements IOnlineController {

	@Autowired
	private IOnlineService onlineService;
	@Resource(name = "massMailService")
	private IMailService massMailService;

	@Override
	@PostMapping("/add")
	public Mono<ModelAndView> addOnlineController(OnlineForAdd online, BindingResult bindingResult, Device device) {
		ServiceResult serviceResult = onlineService.addMailService(online.toDTO());
		if (!serviceResult.isSuccessed()) {
			return newErrorMav(serviceResult.getErrInfo(), device);
		}
		// 添加成功则返回成功页面
		return newSuccessMav("订阅成功", device);
	}

	@Override
	@PostMapping("/update")
	@ResponseBody
	public Mono<Map<String, Object>> modifyOnlineController(@RequestBody @Valid OnlineForModify online,
			BindingResult bindingResult) {
		Map<String, Object> theResult = new HashMap<>();
		List<Online> onlines = onlineService.queryOnlineService(online.toDTO()).getResult(LIST_1);
		for (Online o : onlines) {
			if (!o.getOnlineEmail().equals(online.toDTO().getOnlineId())) {
				onlineService.modifyOnlineService(o);
			}
		}
		onlineService.modifyOnlineService(online.toDTO());
		theResult.put("result", true);
		return Mono.justOrEmpty(theResult);
	}

	@Override
	@PostMapping("/mass")
	public Mono<ModelAndView> massMailController(@Valid OnlineForMassMail online, BindingResult bindingResult,
			Device device) {
		massMailService.sendMailService(online.getMailTitle(), online.getMailContent());
		return newSuccessMav("发送成功", device);
	}

}
