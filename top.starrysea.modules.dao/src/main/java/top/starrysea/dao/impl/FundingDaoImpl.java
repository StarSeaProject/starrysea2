package top.starrysea.dao.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.stereotype.Repository;

import top.starrysea.dao.IFundingDao;
import top.starrysea.kql.clause.WhereType;
import top.starrysea.kql.facede.KumaSqlDao;
import top.starrysea.kql.facede.ListSqlResult;
import top.starrysea.object.dto.Funding;

@Repository("fundingDao")
public class FundingDaoImpl implements IFundingDao {

	@Autowired
	private KumaSqlDao kumaSqlDao;

	@Override
	public List<Funding> getAllFundingDao(Funding funding) {
		kumaSqlDao.selectMode();
		ListSqlResult<Funding> theResult = kumaSqlDao.select("funding_id").select("funding_name")
				.select("funding_money").select("funding_message").from(Funding.class)
				.where("activity_id", WhereType.EQUALS, funding.getActivity().getActivityId())
				.endForList((rs, row) -> new Funding.Builder().fundingId(rs.getInt("funding_id"))
						.fundingName(rs.getString("funding_name")).fundingMoney(rs.getDouble("funding_money"))
						.fundingMessage(rs.getString("funding_message")).activity(funding.getActivity()).build());
		return theResult.getResult();
	}

	@Override
	public void saveFundingDao(List<Funding> fundings) {
		kumaSqlDao.insertMode();
		kumaSqlDao.insert("activity_id").insert("funding_name").insert("funding_money").insert("funding_message")
				.insert("user_id").table(Funding.class).batchEnd(new BatchPreparedStatementSetter() {

					@Override
					public void setValues(PreparedStatement ps, int i) throws SQLException {
						ps.setInt(1, fundings.get(i).getActivity().getActivityId());
						ps.setString(2, fundings.get(i).getFundingName());
						ps.setDouble(3, fundings.get(i).getFundingMoney());
						ps.setString(4, fundings.get(i).getFundingMessage());
						ps.setString(5, fundings.get(i).getUser().getUserId());
					}

					@Override
					public int getBatchSize() {
						return fundings.size();
					}
				});
	}

	@Override
	public void deleteFundingDao(Funding funding) {
		kumaSqlDao.deleteMode();
		kumaSqlDao.table(Funding.class).where("funding_id", WhereType.EQUALS, funding.getFundingId()).end();
	}

}
