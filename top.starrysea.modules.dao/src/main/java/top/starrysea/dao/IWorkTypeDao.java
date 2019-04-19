package top.starrysea.dao;

import java.util.List;

import top.starrysea.object.dto.Orders;
import top.starrysea.object.dto.WorkType;

public interface IWorkTypeDao {

	List<WorkType> getAllWorkTypeDao(WorkType workType);

	int getWorkTypeStockDao(WorkType workType);

	WorkType getWorkTypeNameDao(WorkType workType);

	void saveWorkTypeDao(List<WorkType> workTypes);

	void deleteWorkTypeDao(WorkType workType);

	void updateWorkTypeStockDao(WorkType workType);

	void reduceWorkTypeStockDao(WorkType workType);

	void updateWorkTypeStockDao(Orders order);

	List<WorkType> getAllWorkTypeForShoppingCarDao(List<WorkType> workTypes);

	void increaseWorkTypeStockDao(WorkType workType);
}
