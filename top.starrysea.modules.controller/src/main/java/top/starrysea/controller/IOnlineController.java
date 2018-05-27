package top.starrysea.controller;

import java.util.Map;

import org.springframework.mobile.device.Device;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

import top.starrysea.object.view.in.OnlineForAdd;
import top.starrysea.object.view.in.OnlineForMassMail;
import top.starrysea.object.view.in.OnlineForModify;

public interface IOnlineController {

	ModelAndView addOnlineController(OnlineForAdd online, BindingResult bindingResult, Device device);

	Map<String, Object> modifyOnlineController(OnlineForModify online, BindingResult bindingResult);

	ModelAndView massMailController(OnlineForMassMail online, BindingResult bindingResult, Device device);
}
