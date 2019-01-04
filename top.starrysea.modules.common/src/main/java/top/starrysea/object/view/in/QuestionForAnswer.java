package top.starrysea.object.view.in;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import top.starrysea.object.dto.Question;

public class QuestionForAnswer {
	@NotBlank(message = "质问ID不能为空")
	private String questionId;
	@NotBlank(message = "回复内容不能为空")
	@Size(max = 150, message = "回复内容长度不能超过150")
	private String answer;

	public String getQuestionId() {
		return questionId;
	}

	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public Question toDto() {
		return new Question.Builder().questionId(questionId).answer(answer).build();
	}
}
