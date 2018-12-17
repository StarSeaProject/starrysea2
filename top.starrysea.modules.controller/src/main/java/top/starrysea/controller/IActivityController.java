package top.starrysea.controller;

import java.util.Map;

import org.springframework.mobile.device.Device;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import reactor.core.publisher.Mono;
import top.starrysea.object.view.in.ActivityForAdd;
import top.starrysea.object.view.in.ActivityForAll;
import top.starrysea.object.view.in.ActivityForModify;
import top.starrysea.object.view.in.ActivityForOne;
import top.starrysea.object.view.in.FundingForAddList;
import top.starrysea.object.view.in.FundingForRemove;

public interface IActivityController {

	Mono<ModelAndView> queryAllActivityController(ActivityForAll activity, Device device);
	
	Mono<Map<String, Object>> queryAllActivityControllerAjax(ActivityForAll activity);

	Mono<ModelAndView> queryActivityController(ActivityForOne activity, BindingResult bindingResult, Device device);

	Mono<Map<String, Object>> queryActivityControllerAjax(ActivityForOne activity, BindingResult bindingResult);

	Mono<ModelAndView> addActivityController(MultipartFile coverFile, ActivityForAdd activity, BindingResult bindingResult, Device device);

	Mono<ModelAndView> modifyActivityController(ActivityForModify activity, BindingResult bindingResult, Device device);

	Mono<ModelAndView> removeActivityController(ActivityForOne activity, BindingResult bindingResult, Device device);

	Mono<ModelAndView> addFundingController(FundingForAddList fundings, BindingResult bindingResult, Device device);

	Mono<ModelAndView> removeFundingController(FundingForRemove funding, BindingResult bindingResult, Device device);
}
