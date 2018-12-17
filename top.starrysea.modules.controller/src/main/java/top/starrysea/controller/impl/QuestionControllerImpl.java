package top.starrysea.controller.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import reactor.core.publisher.Mono;
import top.starrysea.common.ServiceResult;
import top.starrysea.controller.IQuestionController;
import top.starrysea.object.dto.Question;
import top.starrysea.object.view.in.QuestionForAll;
import top.starrysea.object.view.in.QuestionForAnswer;
import top.starrysea.object.view.in.QuestionForAsk;
import top.starrysea.service.IQuestionService;

import static top.starrysea.common.Const.*;
import static top.starrysea.common.ResultKey.*;
import static top.starrysea.common.ModelAndViewFactory.*;

@Controller
public class QuestionControllerImpl implements IQuestionController {

	@Autowired
	private IQuestionService questionService;

	@Override
	@GetMapping("/question")
	public Mono<ModelAndView> queryQuestionController(QuestionForAll question, Device device) {
		question.setQuestionStatus((short) 2);
		ServiceResult serviceResult = questionService.queryAllQuestionService(question.getCondition(),
				question.toDTO());
		List<Question> result = serviceResult.getResult(LIST_1);
		List<top.starrysea.object.view.out.QuestionForAll> voResult = result.stream().map(Question::toVoForAll)
				.collect(Collectors.toList());
		return Mono.justOrEmpty(new ModelAndView(QUESTION + "question").addObject("result", voResult)
				.addObject("nowPage", serviceResult.getNowPage()).addObject("totalPage", serviceResult.getTotalPage()));
	}

	@Override
	@PostMapping("/question/ajax")
	@ResponseBody
	public Mono<Map<String, Object>> queryQuestionControllerAjax(@RequestBody QuestionForAll question) {
		ServiceResult serviceResult = questionService.queryAllQuestionService(question.getCondition(),
				question.toDTO());
		List<Question> result = serviceResult.getResult(LIST_1);
		List<top.starrysea.object.view.out.QuestionForAll> voResult = result.stream().map(Question::toVoForAll)
				.collect(Collectors.toList());
		Map<String, Object> theResult = new HashMap<>();
		theResult.put("result", voResult);
		theResult.put("nowPage", serviceResult.getNowPage());
		theResult.put("totalPage", serviceResult.getTotalPage());
		return Mono.justOrEmpty(theResult);
	}

	@Override
	@PostMapping("/question/ask")
	public Mono<ModelAndView> askQuestionController(@Valid QuestionForAsk question, BindingResult bindingResult,
			Device device) {
		questionService.askQuestionService(question.toDTO());
		return newSuccessMav("提问成功！", device);
	}

	@Override
	@PostMapping("/question/answer")
	@ResponseBody
	public Mono<Map<String, Object>> answerQuestionController(@RequestBody @Valid QuestionForAnswer question,
			BindingResult bindingResult) {
		questionService.answerQuestionService(question.toDto());
		Map<String, Object> theResult = new HashMap<>();
		theResult.put("result", true);
		return Mono.justOrEmpty(theResult);
	}

}
