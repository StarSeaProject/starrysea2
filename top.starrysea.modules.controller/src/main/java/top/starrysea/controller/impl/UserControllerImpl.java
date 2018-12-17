package top.starrysea.controller.impl;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import reactor.core.publisher.Mono;
import top.starrysea.common.ServiceResult;
import top.starrysea.controller.IUserController;
import top.starrysea.object.dto.Admin;
import top.starrysea.object.view.in.AdminForLogin;
import top.starrysea.service.IUserService;

import static top.starrysea.common.Const.*;
import static top.starrysea.common.ResultKey.ADMIN;

@Controller
@RequestMapping("/user")
public class UserControllerImpl implements IUserController {

	@Autowired
	private IUserService userService;

	@Override
	// 管理员登陆
	@PostMapping("/login")
	public Mono<ModelAndView> loginController(@Valid AdminForLogin admin, BindingResult bindingResult, Device device) {
		ServiceResult serviceResult = userService.loginService(admin.toDTO());
		ModelAndView modelAndView = new ModelAndView();
		if (serviceResult.isSuccessed()) {
			Admin admin1 = serviceResult.getResult(ADMIN);
			// 登陆成功,返回管理员的主页
			modelAndView.addObject(ADMIN_SESSION_KEY, admin1.getAdminId())
					.setViewName(device.isMobile() ? MOBILE + BOSS : BOSS);
		} else {
			// 登陆失败,返回登陆页面
			modelAndView.addObject(ERRINFO, serviceResult.getErrInfo())
					.setViewName(device.isMobile() ? MOBILE + LOGIN_VIEW : LOGIN_VIEW);
		}
		return Mono.justOrEmpty(modelAndView);
	}

	@Override
	@GetMapping("/exit")
	public Mono<ModelAndView> exitController(Device device) {
		return Mono.justOrEmpty(new ModelAndView(device.isMobile() ? MOBILE + LOGIN_VIEW : LOGIN_VIEW));
	}

}
