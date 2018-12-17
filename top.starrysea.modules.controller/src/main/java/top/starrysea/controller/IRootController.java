package top.starrysea.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.mobile.device.Device;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import reactor.core.publisher.Mono;

public interface IRootController {

	Mono<ModelAndView> index(Device device);

	Mono<Void> upload(HttpServletRequest request, HttpServletResponse response, MultipartFile file);
	
}
