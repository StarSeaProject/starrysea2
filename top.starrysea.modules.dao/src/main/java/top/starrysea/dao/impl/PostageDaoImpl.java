package top.starrysea.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import top.starrysea.dao.IPostageDao;
import top.starrysea.kql.clause.WhereType;
import top.starrysea.kql.facede.KumaSqlDao;
import top.starrysea.kql.facede.ListSqlResult;
import top.starrysea.object.dto.Postage;

@Repository
public class PostageDaoImpl implements IPostageDao {

	@Autowired
	private KumaSqlDao kumaSqlDao;

	@Override
	public Postage getPostage(Postage postage) {
		kumaSqlDao.selectMode();
		ListSqlResult<Postage> result = kumaSqlDao.select("postage_money").from(Postage.class)
				.where("province_id", WhereType.EQUALS, postage.getProvince().getProvinceId())
				.endForList((rs, row) -> new Postage.Builder().postageMoney(rs.getInt("postage_money")).build());
		if (result == null || result.getResult() == null || result.getResult().isEmpty()) {
			return new Postage.Builder().postageMoney(0).build();
		}
		return result.getResult().get(0);
	}

}
