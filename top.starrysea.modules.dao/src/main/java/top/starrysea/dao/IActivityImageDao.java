package top.starrysea.dao;

import java.util.List;

import top.starrysea.object.dto.Activity;
import top.starrysea.object.dto.ActivityImage;

public interface IActivityImageDao {

	List<ActivityImage> getAllActivityImageDao(Activity activity);

	void saveActivityImageDao(List<ActivityImage> activityImages);
}
