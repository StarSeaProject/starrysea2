package top.starrysea.controller;

import java.util.Map;

import org.springframework.mobile.device.Device;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import reactor.core.publisher.Mono;
import top.starrysea.object.view.in.WorkForAdd;
import top.starrysea.object.view.in.WorkForAll;
import top.starrysea.object.view.in.WorkForOne;
import top.starrysea.object.view.in.WorkTypeForModify;
import top.starrysea.object.view.in.WorkTypeForRemove;

public interface IWorkController {

	Mono<ModelAndView> queryAllWorkController(WorkForAll work, Device device);

	Mono<Map<String, Object>> queryAllWorkControllerAjax(WorkForAll work);

	Mono<ModelAndView> queryWorkController(WorkForOne work, BindingResult bindingResult, Device device);

	Mono<Map<String, Object>> queryWorkControllerAjax(WorkForOne work, BindingResult bindingResult);

	Mono<ModelAndView> addWorkController(MultipartFile coverFile, MultipartFile[] imageFiles, WorkForAdd work,
			BindingResult bindingResult, Device device);

	Mono<ModelAndView> removeWorkController(WorkForOne work, BindingResult bindingResult, Device device);

	Mono<ModelAndView> removeWorkTypeController(WorkTypeForRemove workType, BindingResult bindingResult, Device device);

	Mono<ModelAndView> modifyWorkTypeController(WorkTypeForModify workType, BindingResult bindingResult, Device device);
}
