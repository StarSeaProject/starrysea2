package top.starrysea.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import springfox.documentation.annotations.ApiIgnore;
import top.starrysea.common.Common;
import top.starrysea.common.ModelAndViewFactory;
import top.starrysea.common.ServiceResult;
import top.starrysea.object.dto.Activity;
import top.starrysea.object.dto.Funding;
import top.starrysea.object.dto.User;
import top.starrysea.object.view.in.ActivityForAdd;
import top.starrysea.object.view.in.ActivityForAll;
import top.starrysea.object.view.in.ActivityForModify;
import top.starrysea.object.view.in.ActivityForOne;
import top.starrysea.object.view.in.FundingForAdd;
import top.starrysea.object.view.in.FundingForAddList;
import top.starrysea.object.view.in.FundingForParticipate;
import top.starrysea.object.view.in.FundingForRemove;
import top.starrysea.service.IActivityService;
import top.starrysea.trade.PayelvesPayBackParam;
import top.starrysea.trade.PayelvesPayRequest;
import top.starrysea.trade.service.ITradeService;

import static top.starrysea.common.Const.*;
import static top.starrysea.common.ResultKey.*;

@Api(tags = "活动相关api")
@Controller
public class ActivityController {

	@Autowired
	private IActivityService activityService;
	@Resource(name = "payelvesTradeService")
	private ITradeService payelvesTradeService;

	// 查询所有众筹活动
	@GetMapping("/activity")
	public ModelAndView queryAllActivityController(ActivityForAll activity, Device device) {
		ServiceResult serviceResult = activityService.queryAllActivityService(activity.getCondition(),
				activity.toDTO());
		List<Activity> result = serviceResult.getResult(LIST_1);
		Activity newestActivity = serviceResult.getResult(ACTIVITY);
		return new ModelAndView(device.isMobile() ? MOBILE + "all_activity" : "all_activity")
				.addObject("newResult", newestActivity.toVoForAll())
				.addObject("result", result.stream().map(Activity::toVoForAll).collect(Collectors.toList()))
				.addObject("nowPage", serviceResult.getNowPage()).addObject("totalPage", serviceResult.getTotalPage());
	}

	// 查询所有众筹活动
	@PostMapping("/activity/ajax")
	@ResponseBody
	public Map<String, Object> queryAllActivityControllerAjax(@RequestBody ActivityForAll activity) {
		ServiceResult serviceResult = activityService.queryAllActivityService(activity.getCondition(),
				activity.toDTO());
		List<Activity> result = serviceResult.getResult(LIST_1);
		Activity newestActivity = serviceResult.getResult(ACTIVITY);
		Map<String, Object> theResult = new HashMap<>();
		theResult.put("activityName", activity.getActivityName());
		theResult.put("result", result.stream().map(Activity::toVoForAll).collect(Collectors.toList()));
		theResult.put("newest", newestActivity);
		theResult.put("nowPage", serviceResult.getNowPage());
		theResult.put("totalPage", serviceResult.getTotalPage());
		return theResult;
	}

	// 查询一个众筹活动的详情页
	@GetMapping("/activity/{activityId}")
	public ModelAndView queryActivityController(@Valid ActivityForOne activity, BindingResult bindingResult,
			Device device) {
		ServiceResult serviceResult = activityService.queryActivityService(activity.toDTO());
		Activity a = serviceResult.getResult(ACTIVITY);
		return new ModelAndView(device.isMobile() ? MOBILE + "activity_detail" : "activity_detail")
				.addObject("activity", a.toVoForOne()).addObject("fundings", serviceResult.getResult(LIST_1))
				.addObject("fundingFactor", serviceResult.getResult(DOUBLE))
				.addObject("activityId", activity.getActivityId());
	}

	// 查询一个众筹活动的详情页
	@PostMapping("/activity/detail/ajax")
	@ResponseBody
	public Map<String, Object> queryActivityControllerAjax(@RequestBody @Valid ActivityForOne activity,
			BindingResult bindingResult) {
		ServiceResult serviceResult = activityService.queryActivityService(activity.toDTO());
		Activity a = serviceResult.getResult(ACTIVITY);
		Map<String, Object> theResult = new HashMap<>();
		theResult.put("activityId", activity.getActivityId());
		theResult.put("activity", a.toVoForOne());
		theResult.put("fundings", serviceResult.getResult(LIST_1));
		theResult.put("activityId", activity.getActivityId());
		return theResult;
	}

	// 添加一个众筹活动
	@PostMapping("/activity/add")
	public ModelAndView addActivityController(@RequestParam("coverFile") MultipartFile coverFile,
			@Valid ActivityForAdd activity, BindingResult bindingResult, Device device) {
		activityService.addActivityService(coverFile, activity.toDTO(), activity.toDTOImage());
		return ModelAndViewFactory.newSuccessMav("添加成功！", device);
	}

	// 修改一个众筹活动的状态
	@PostMapping("/activity/modify")
	public ModelAndView modifyActivityController(@Valid ActivityForModify activity, BindingResult bindingResult,
			Device device) {
		activityService.modifyActivityService(activity.toDTO());
		return ModelAndViewFactory.newSuccessMav("修改成功!", device);
	}

	// 删除一个众筹活动
	@PostMapping("/activity/remove")
	public ModelAndView removeActivityController(@Valid ActivityForOne activity, BindingResult bindingResult,
			Device device) {
		activityService.removeActivityService(activity.toDTO());
		return ModelAndViewFactory.newSuccessMav("删除成功!", device);
	}

	@PostMapping("/activity/funding/add")
	public ModelAndView addFundingController(@Valid FundingForAddList fundings, BindingResult bindingResult,
			Device device, HttpSession session) {
		User currentUser = (User) session.getAttribute(USER_SESSION_KEY);
		for (FundingForAdd funding : fundings.getFundings()) {
			funding.setActivityId(fundings.getActivityId());
		}
		activityService.addFundingService(fundings.getFundings().stream().map(FundingForAdd::toDTO).map(funding -> {
			funding.setUser(currentUser);
			return funding;
		}).collect(Collectors.toList()));
		return ModelAndViewFactory.newSuccessMav("添加成功!", device);
	}

	@PostMapping("/activity/funding/remove")
	public ModelAndView removeFundingController(@Valid FundingForRemove funding, BindingResult bindingResult,
			Device device) {
		activityService.removeFundingService(funding.toDTO());
		return ModelAndViewFactory.newSuccessMav("删除成功!", device);
	}

	@PostMapping("/activity/funding/participate")
	@ApiOperation(value = "用户众筹", notes = "用户众筹")
	public ModelAndView participateFundingController(
			@Valid @ApiParam(name = "众筹记录对象", required = true) FundingForParticipate funding,
			@ApiIgnore BindingResult bindingResult, @ApiIgnore HttpSession session, @ApiIgnore Device device) {
		User currentUser = (User) session.getAttribute(USER_SESSION_KEY);
		Funding fundingDTO = funding.toDTO();
		fundingDTO.setUser(currentUser);
		ServiceResult sr = activityService.participateFundingService(fundingDTO);
		if (!sr.isSuccessed()) {
			return ModelAndViewFactory.newErrorMav(sr.getErrInfo(), device);
		}
		Funding f = sr.getResult(FUNDING);
		PayelvesPayBackParam backParam = new PayelvesPayBackParam();
		backParam.setType(2);
		String url = payelvesTradeService.createPaymentRequestRouteService(PayelvesPayRequest.builder()
				.withBackPara(Common.toJson(backParam)).withBody("星之海志愿者公会-众筹").withChannel(2)
				.withOrderId(f.getFundingNum()).withPayType(1).withPrice(funding.getFundingMoney() * 100)
				.withSubject("星之海志愿者公会-众筹" + funding.getFundingMoney()).withUserId(currentUser.getUserId()).build())
				.getResult(STRING);
		return new ModelAndView("redirect:" + url);

	}
}
