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

import com.google.code.kaptcha.impl.DefaultKaptcha;

import top.starrysea.common.ModelAndViewFactory;
import top.starrysea.common.ServiceResult;
import top.starrysea.object.view.in.UserForActivate;
import top.starrysea.object.view.in.UserForAdd;
import top.starrysea.object.view.in.UserForCheck;
import top.starrysea.object.view.in.UserForLogin;
import top.starrysea.object.view.in.UserInfoForEdit;
import top.starrysea.service.IUserService;

import top.starrysea.object.dto.User;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
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

	@Autowired
	private DefaultKaptcha defaultKaptcha;

	@PostMapping("/check")
	@ResponseBody
	public Map<String, Object> checkUserAvailabilityController(@Valid UserForCheck user) {
		ServiceResult serviceResult = userService.checkUserAvailabilityService(user.toDTO());
		Map<String, Object> result = new HashMap<>();
		result.put("userEmail", user.getUserEmail());
		result.put("isAvailable", true);
		result.put(ERRINFO, serviceResult.getErrInfo());
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
		String verifyCode = (String) httpSession.getAttribute(VERIFY_CODE);
		Map<String, Object> loginResult = new HashMap<>();
		if (!verifyCode.equals(user.getVerifyCode())) {
			loginResult.put("userEmail", user.getUserEmail());
			loginResult.put("result", "验证码错误!");
			loginResult.put("resultCode", "1");
			return loginResult;
		}
		ServiceResult serviceResult = userService.userLogin(user.toDTO());
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
		User currentUser = (User) session.getAttribute(USER_SESSION_KEY);
		User user = userService.getUserInfoService(currentUser.getUserId()).getResult(USER);
		mav.addObject("userInfo", user.toVO());
		return mav;
	}

	@PostMapping("/info")
	public ModelAndView editUserInfoController(@Valid UserInfoForEdit user, HttpSession session, Device device) {
		User currentUser = (User) session.getAttribute(USER_SESSION_KEY);
		User editUser = user.toDTO();
		editUser.setUserId(currentUser.getUserId());
		userService.editUserInfoService(editUser);
		return ModelAndViewFactory.newSuccessMav("修改个人信息成功", device);
	}

	@GetMapping("/getVerifyCode")
	public void getVerifyCode(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
			throws IOException {
		byte[] captchaChallengeAsJpeg = null;
		ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();
		try {
			// 生产验证码字符串并保存到session中
			String createText = defaultKaptcha.createText();
			httpServletRequest.getSession().setAttribute(VERIFY_CODE, createText);
			// 使用生产的验证码字符串返回一个BufferedImage对象并转为byte写入到byte数组中
			BufferedImage challenge = defaultKaptcha.createImage(createText);
			ImageIO.write(challenge, "jpg", jpegOutputStream);
		} catch (IllegalArgumentException e) {
			httpServletResponse.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}

		// 定义response输出类型为image/jpeg类型，使用response输出流输出图片的byte数组
		captchaChallengeAsJpeg = jpegOutputStream.toByteArray();
		httpServletResponse.setHeader("Cache-Control", "no-store");
		httpServletResponse.setHeader("Pragma", "no-cache");
		httpServletResponse.setDateHeader("Expires", 0);
		httpServletResponse.setContentType("image/jpeg");
		ServletOutputStream responseOutputStream = httpServletResponse.getOutputStream();
		responseOutputStream.write(captchaChallengeAsJpeg);
		responseOutputStream.flush();
		responseOutputStream.close();
	}

}