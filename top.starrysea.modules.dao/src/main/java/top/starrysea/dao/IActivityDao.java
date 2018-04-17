package top.starrysea.dao;

import java.util.List;

import top.starrysea.common.Condition;
import top.starrysea.object.dto.Activity;

public interface IActivityDao {

	Activity getNewestActivityDao();

	List<Activity> getAllActivityDao(Condition condition, Activity activity);

	int getActivityCountDao(Condition condition, Activity activity);

	Activity getActivityDao(Activity activity);

	int saveActivityDao(Activity activity);

	void updateActivityDao(Activity activity);

	void updateAddActivityMoneyDao(List<Activity> activitys);

	void updateReduceActivityMoneyDao(Activity activity);

	void deleteActivityDao(Activity activity);
}
