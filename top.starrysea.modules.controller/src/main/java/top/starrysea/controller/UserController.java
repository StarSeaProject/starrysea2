package top.starrysea.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import top.starrysea.common.ModelAndViewFactory;
import top.starrysea.common.ServiceResult;
import top.starrysea.object.view.in.UserForActivate;
import top.starrysea.object.view.in.UserForAdd;
import top.starrysea.object.view.in.UserForCheck;
import top.starrysea.object.view.in.UserForLogin;
import top.starrysea.service.IUserService;

import top.starrysea.object.dto.User;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import static top.starrysea.common.Const.*;
import static top.starrysea.common.ResultKey.USER;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private IUserService userService;

	@PostMapping("/check")
	@ResponseBody
	public Map<String, Object> checkUserAvailabilityController(@Valid UserForCheck user) {
		ServiceResult serviceResult = userService.checkUserAvailabilityService(user.toDTO());
		Map<String, Object> result = new HashMap<>();
		result.put("userEmail", user.getUserEmail());
		if (serviceResult.isSuccessed()) {
			result.put("isAvailable", true);
		} else {
			result.put("isAvailable", false);
		}
		return result;
	}

	@PostMapping("/register")
	public ModelAndView registerController(@Valid UserForAdd user, Device device) {
		ServiceResult serviceResult = userService.registerService(user.toDTO());
		if (serviceResult.isSuccessed()) {
			return ModelAndViewFactory.newSuccessMav("我们已经发送了验证邮件到您的邮箱,请注意查收", device);
		} else {
			return ModelAndViewFactory.newErrorMav(serviceResult.getErrInfo(), device);
		}
	}

	@PostMapping("/login")
	@ResponseBody
	public Map<String, Object> loginController(@Valid UserForLogin user, BindingResult bindingResult, Device device,
			HttpSession httpSession) {
		ServiceResult serviceResult = userService.userLogin(user.toDTO());
		Map<String, Object> loginResult = new HashMap<>();
		if (serviceResult.isSuccessed()) {
			// 登录成功
			User user1 = serviceResult.getResult(USER);
			httpSession.setAttribute(USER_SESSION_KEY, (Serializable) user1);
			loginResult.put("result", "登录成功");
			loginResult.put("resultCode", "0");
		} else {
			// 登录失败
			loginResult.put("userEmail", user.getUserEmail());
			loginResult.put("result", serviceResult.getErrInfo());
			loginResult.put("resultCode", "1");
		}
		return loginResult;
	}

	@GetMapping("/exit")
	public ModelAndView exitController(Device device, HttpSession session) {
		session.removeAttribute(USER_SESSION_KEY);
		return new ModelAndView(device.isMobile() ? MOBILE + "index" : "index");
	}

	@GetMapping("/activate/{activateCode}")
	public ModelAndView activateController(@Valid UserForActivate user, BindingResult bindingResult, Device device) {
		ServiceResult serviceResult = userService.activateService(user.getActivateCode());
		if (serviceResult.isSuccessed()) {
			return ModelAndViewFactory.newSuccessMav("激活成功!请登录!", device);
		} else {
			return ModelAndViewFactory.newErrorMav(serviceResult.getErrInfo(), device);
		}
	}

	@GetMapping("/info")
	public ModelAndView getUserInfoController(HttpSession session, Device device) {
		ModelAndView mav = new ModelAndView(device.isMobile() ? MOBILE + "userinfo" : "userinfo");
		User user = userService.getUserInfoService((String) session.getAttribute(USER_SESSION_KEY)).getResult(USER);
		mav.addObject("userInfo", user.toVO());
		return mav;
	}

}
