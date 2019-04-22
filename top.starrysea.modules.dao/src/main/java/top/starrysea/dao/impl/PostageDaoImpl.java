package top.starrysea.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import top.starrysea.dao.IPostageDao;
import top.starrysea.kql.clause.WhereType;
import top.starrysea.kql.facede.EntitySqlResult;
import top.starrysea.kql.facede.KumaSqlDao;
import top.starrysea.object.dto.Postage;

@Repository
public class PostageDaoImpl implements IPostageDao {

	@Autowired
	private KumaSqlDao kumaSqlDao;

	@Override
	public Postage getPostage(Postage postage) {
		kumaSqlDao.selectMode();
		EntitySqlResult<Postage> result = kumaSqlDao.select("postage_money").from(Postage.class)
				.where("province_id", WhereType.EQUALS, postage.getProvince().getProvinceId())
				.endForObject((rs, row) -> new Postage.Builder().postageMoney(rs.getInt("postage_money")).build());
		return result.getResult();
	}

}
