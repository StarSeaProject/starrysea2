package top.starrysea.dao;

import java.util.List;

import top.starrysea.common.Condition;
import top.starrysea.object.dto.Work;

public interface IWorkDao {
	List<Work> getAllWorkDao(Condition condition, Work work);

	int getWorkCountDao(Condition condition, Work work);

	Work getWorkDao(Work work);

	int saveWorkDao(Work work);

	void deleteWorkDao(Work work);
	
	void addWorkClick(Work work);
	
	List<Work> getWorkByActivityDao(List<Integer> activityIds);

}
