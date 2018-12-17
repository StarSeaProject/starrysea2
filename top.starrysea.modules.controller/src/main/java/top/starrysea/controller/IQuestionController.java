package top.starrysea.controller;

import java.util.Map;

import org.springframework.mobile.device.Device;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

import reactor.core.publisher.Mono;
import top.starrysea.object.view.in.QuestionForAll;
import top.starrysea.object.view.in.QuestionForAnswer;
import top.starrysea.object.view.in.QuestionForAsk;

public interface IQuestionController {

	Mono<ModelAndView> queryQuestionController(QuestionForAll question, Device device);

	Mono<Map<String, Object>> queryQuestionControllerAjax(QuestionForAll question);

	Mono<ModelAndView> askQuestionController(QuestionForAsk question, BindingResult bindingResult, Device device);

	Mono<Map<String, Object>> answerQuestionController(QuestionForAnswer question, BindingResult bindingResult);

}
