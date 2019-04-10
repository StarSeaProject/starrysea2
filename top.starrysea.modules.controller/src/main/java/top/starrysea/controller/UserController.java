package top.starrysea.controller;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import springfox.documentation.annotations.ApiIgnore;
import top.starrysea.common.ModelAndViewFactory;
import top.starrysea.common.ServiceResult;
import top.starrysea.object.dto.User;
import top.starrysea.object.view.in.*;
import top.starrysea.object.view.out.UserFundingInfo;
import top.starrysea.object.view.out.UserOrderInfo;
import top.starrysea.service.IUserService;

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
import java.util.List;
import java.util.Map;

import static top.starrysea.common.Const.*;
import static top.starrysea.common.ResultKey.USER;
import static top.starrysea.common.ResultKey.LIST_1;
import static top.starrysea.common.ResultKey.LIST_2;

@Api(tags = "用户相关api")
@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private IUserService userService;

	@Autowired
	private DefaultKaptcha defaultKaptcha;

	@ApiOperation(value = "检查用户可用性", notes = "检查用户可用性")
	@PostMapping("/check")
	@ResponseBody
	public Map<String, Object> checkUserAvailabilityController(
			@Valid @ApiParam(name = "用户检验用对象", required = true) UserForCheck user) {
		ServiceResult serviceResult = userService.checkUserAvailabilityService(user.toDTO());
		Map<String, Object> result = new HashMap<>();
		result.put("userEmail", user.getUserEmail());
		result.put("isAvailable", serviceResult.isSuccessed());
		result.put(ERRINFO, serviceResult.getErrInfo());
		return result;
	}

	@ApiOperation(value = "注册", notes = "注册")
	@PostMapping("/register")
	public ModelAndView registerController(@Valid @ApiParam(name = "注册用对象", required = true) UserForAdd user,
			@ApiIgnore Device device) {
		ServiceResult serviceResult = userService.registerService(user.toDTO());
		if (serviceResult.isSuccessed()) {
			return ModelAndViewFactory.newSuccessMav("我们已经发送了验证邮件到您的邮箱,请注意查收", device);
		} else {
			return ModelAndViewFactory.newErrorMav(serviceResult.getErrInfo(), device);
		}
	}

	@ApiOperation(value = "登录", notes = "使用电子邮箱和密码(经过sha256后)登录")
	@PostMapping("/login")
	@ResponseBody
	public Map<String, Object> loginController(@Valid @ApiParam(name = "登录用对象", required = true) UserForLogin user,
			@ApiIgnore BindingResult bindingResult, @ApiIgnore Device device, @ApiIgnore HttpSession httpSession) {
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

	@ApiOperation(value = "登出", notes = "登出")
	@GetMapping("/exit")
	public ModelAndView exitController(@ApiIgnore Device device, @ApiIgnore HttpSession session) {
		return new ModelAndView(device.isMobile() ? MOBILE + "index" : "index");
	}

	@ApiOperation(value = "用户激活", notes = "用户激活")
	@GetMapping("/activate/{activateCode}")
	public ModelAndView activateController(@Valid @ApiParam(name = "用户激活用对象", required = true) UserForActivate user,
			@ApiIgnore BindingResult bindingResult, @ApiIgnore Device device) {
		ServiceResult serviceResult = userService.activateService(user.getActivateCode());
		if (serviceResult.isSuccessed()) {
			return ModelAndViewFactory.newSuccessMav("激活成功!请登录!", device);
		} else {
			return ModelAndViewFactory.newErrorMav(serviceResult.getErrInfo(), device);
		}
	}

	@ApiOperation(value = "获取个人信息", notes = "获取个人信息")
	@GetMapping("/info")
	public ModelAndView getUserInfoController(@ApiIgnore HttpSession session, @ApiIgnore Device device) {
		ModelAndView mav = new ModelAndView(device.isMobile() ? MOBILE + "userinfo" : "userinfo");
		User currentUser = (User) session.getAttribute(USER_SESSION_KEY);
		ServiceResult serviceResult = userService.getUserInfoService(currentUser.getUserId());
		User user = serviceResult.getResult(USER);
		List<UserFundingInfo> userFundingInfos = serviceResult.getResult(LIST_1);
		List<UserOrderInfo> userOrderInfos = serviceResult.getResult(LIST_2);
		mav.addObject("orders", userOrderInfos);
		mav.addObject("userInfo", user.toVO());
		mav.addObject("fundings", userFundingInfos);
		return mav;
	}

	@ApiOperation(value = "修改个人信息", notes = "修改获取个人信息")
	@PostMapping("/info")
	public ModelAndView editUserInfoController(
			@Valid @ApiParam(name = "修改个人信息用对象", required = true) UserInfoForEdit user, @ApiIgnore HttpSession session,
			@ApiIgnore Device device) {
		User currentUser = (User) session.getAttribute(USER_SESSION_KEY);
		User editUser = user.toDTO();
		editUser.setUserId(currentUser.getUserId());
		userService.editUserInfoService(editUser);
		return ModelAndViewFactory.newSuccessMav("修改个人信息成功", device);
	}

	@ApiOperation(value = "获取验证码", notes = "获取验证码")
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

	@ApiOperation(value = "用户修改密码", notes = "用户修改密码")
	@PostMapping("/changePassword")
	public ModelAndView changePasswordController(
			@Valid @ApiParam(name = "修改密码用对象", required = true) UserForChangePassword user,
			@ApiIgnore HttpSession session, @ApiIgnore Device device) {
		User userToChange = (User) session.getAttribute(USER_SESSION_KEY);
		userToChange.setUserPassword(user.getCurrentPassword());
		ServiceResult serviceResult = userService.changeUserPasswordService(userToChange, user.getNewPassword());
		if (serviceResult.isSuccessed()) {
			return ModelAndViewFactory.newSuccessMav("修改密码成功", device);
		} else {
			return ModelAndViewFactory.newErrorMav(serviceResult.getErrInfo(), device);
		}
	}

	@ApiOperation(value = "用户修改密码页面", notes = "用户修改密码页面")
	@GetMapping("/changePassword")
	public ModelAndView changePassword(@ApiIgnore Device device) {
		ModelAndView mav = new ModelAndView(device.isMobile() ? MOBILE + "changepassword" : "changepassword");
		return mav;
	}
}