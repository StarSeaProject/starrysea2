package top.starrysea.dao;

import java.util.List;

import top.starrysea.object.dto.Area;
import top.starrysea.object.dto.Province;

public interface IProvinceDao {

	List<Area> getAllProvinceDao();
	
	Province getProvinceByArea(int areaId);
}
