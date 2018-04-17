package top.starrysea.dao;

import java.util.List;

import top.starrysea.common.Condition;
import top.starrysea.object.dto.Question;

public interface IQuestionDao {

	List<Question> getAllQuestionDao(Condition condition, Question question);

	void saveQuestionDao(Question question);

	void updateQuestionDao(Question question);
	
	int getQuestionCountDao(Question question);
}
