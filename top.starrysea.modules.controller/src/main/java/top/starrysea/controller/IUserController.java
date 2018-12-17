package top.starrysea.controller;

import org.springframework.mobile.device.Device;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

import reactor.core.publisher.Mono;
import top.starrysea.object.view.in.AdminForLogin;

public interface IUserController {

	Mono<ModelAndView> loginController(AdminForLogin admin, BindingResult bindingResult, Device device);
	
	Mono<ModelAndView> exitController(Device device);
}
