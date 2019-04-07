package top.starrysea.common;

import java.util.List;
import java.util.Map;

import top.starrysea.object.dto.*;

public enum ResultKey {

	LIST_1(List.class), LIST_2(List.class),

	ACTIVITY(Activity.class), ORDER(Orders.class), ADMIN(Admin.class), WORK(Work.class), ONLINE(Online.class),
	DOUBLE(Double.class), USER(User.class),

	MAP(Map.class);

	private Class<?> clazz;

	ResultKey(Class<?> clazz) {
		this.clazz = clazz;
	}

	public Class<?> getClazz() {
		return clazz;
	}

}
