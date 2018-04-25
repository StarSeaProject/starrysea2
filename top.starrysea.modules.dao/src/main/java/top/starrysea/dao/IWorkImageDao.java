package top.starrysea.dao;

import java.util.List;

import top.starrysea.object.dto.WorkImage;

public interface IWorkImageDao {

	List<WorkImage> getAllWorkImageDao(WorkImage workImage);

	void saveWorkImageDao(List<WorkImage> workImages);
}
