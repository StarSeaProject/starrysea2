package top.starrysea.controller;

import java.util.Map;

import org.springframework.mobile.device.Device;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

import reactor.core.publisher.Mono;
import top.starrysea.object.view.in.OnlineForAdd;
import top.starrysea.object.view.in.OnlineForMassMail;
import top.starrysea.object.view.in.OnlineForModify;

public interface IOnlineController {

	Mono<ModelAndView> addOnlineController(OnlineForAdd online, BindingResult bindingResult, Device device);

	Mono<Map<String, Object>> modifyOnlineController(OnlineForModify online, BindingResult bindingResult);

	Mono<ModelAndView> massMailController(OnlineForMassMail online, BindingResult bindingResult, Device device);
}
