package top.starrysea.object.view.in;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import top.starrysea.object.dto.Question;

public class QuestionForAsk {
	@NotBlank(message = "问题或意见不能为空")
	@Size(max = 150, message = "质问长度不能超过150")
	private String question;

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public Question toDTO() {
		return new Question.Builder().question(question).build();
	}
}
