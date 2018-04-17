package top.starrysea.dao.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.stereotype.Repository;

import top.starrysea.dao.IWorkTypeDao;
import top.starrysea.kql.clause.UpdateSetType;
import top.starrysea.kql.clause.WhereType;
import top.starrysea.kql.facede.EntitySqlResult;
import top.starrysea.kql.facede.KumaSqlDao;
import top.starrysea.kql.facede.ListSqlResult;
import top.starrysea.object.dto.OrderDetail;
import top.starrysea.object.dto.Orders;
import top.starrysea.object.dto.Work;
import top.starrysea.object.dto.WorkType;

@Repository("workTypeDao")
public class WorkTypeDaoImpl implements IWorkTypeDao {

	@Autowired
	private KumaSqlDao kumaSqlDao;

	@Override
	public List<WorkType> getAllWorkTypeDao(WorkType workType) {
		kumaSqlDao.selectMode();
		ListSqlResult<WorkType> theResult = kumaSqlDao.select("work_type_id").select("name").select("stock")
				.from(WorkType.class).where("work_id", WhereType.EQUALS, workType.getWork().getWorkId())
				.endForList((rs, row) -> new WorkType.Builder().workTypeId(rs.getInt("work_type_id"))
						.name(rs.getString("name")).stock(rs.getInt("stock")).build());
		return theResult.getResult();
	}

	@Override
	public int getWorkTypeStockDao(WorkType workType) {
		kumaSqlDao.selectMode();
		EntitySqlResult<WorkType> theResult = kumaSqlDao.select("stock").from(WorkType.class)
				.leftjoin(Work.class, "w", "work_id", WorkType.class, "work_id")
				.where("work_type_id", WhereType.EQUALS, workType.getWorkTypeId())
				.where("work_id", "w", WhereType.EQUALS, workType.getWork().getWorkId())
				.endForObject((rs, row) -> new WorkType.Builder().stock(rs.getInt("stock")).build());
		return theResult.getResult().getStock();
	}

	@Override
	public WorkType getWorkTypeNameDao(WorkType workType) {
		kumaSqlDao.selectMode();
		EntitySqlResult<WorkType> theResult = kumaSqlDao.select("name").from(WorkType.class)
				.where("work_type_id", WhereType.EQUALS, workType.getWorkTypeId())
				.endForObject((rs, row) -> new WorkType.Builder().name(rs.getString("name")).build());
		WorkType wt = theResult.getResult();
		wt.setWorkTypeId(workType.getWorkTypeId());
		return wt;
	}

	@Override
	public void saveWorkTypeDao(List<WorkType> workTypes) {
		kumaSqlDao.insertMode();
		kumaSqlDao.insert("work_id").insert("name").insert("stock").table(WorkType.class)
				.batchEnd(new BatchPreparedStatementSetter() {

					@Override
					public void setValues(PreparedStatement ps, int i) throws SQLException {
						ps.setInt(1, workTypes.get(i).getWork().getWorkId());
						ps.setString(2, workTypes.get(i).getName());
						ps.setInt(3, workTypes.get(i).getStock());
					}

					@Override
					public int getBatchSize() {
						return workTypes.size();
					}
				});
	}

	@Override
	public void deleteWorkTypeDao(WorkType workType) {
		kumaSqlDao.deleteMode();
		kumaSqlDao.table(WorkType.class).where("work_type_id", WhereType.EQUALS, workType.getWorkTypeId()).end();
	}

	@Override
	public void updateWorkTypeStockDao(WorkType workType) {
		kumaSqlDao.updateMode();
		kumaSqlDao.update("stock", UpdateSetType.ASSIGN, workType.getStock()).table(WorkType.class)
				.where("work_type_id", WhereType.EQUALS, workType.getWorkTypeId()).end();
	}

	@Override
	public void reduceWorkTypeStockDao(WorkType workType) {
		kumaSqlDao.updateMode();
		kumaSqlDao.update("stock", UpdateSetType.REDUCE, workType.getStock()).table(WorkType.class)
				.where("work_type_id", WhereType.EQUALS, workType.getWorkTypeId()).end();
	}

	@Override
	public void updateWorkTypeStockDao(Orders order) {
		kumaSqlDao.selectMode();
		ListSqlResult<Integer> theResult = kumaSqlDao.select("work_type_id").from(OrderDetail.class)
				.where("order_id", WhereType.EQUALS, order.getOrderId()).endForList(Integer.class);
		List<Integer> workTypeIds = theResult.getResult();
		kumaSqlDao.updateMode();
		kumaSqlDao.update("stock", UpdateSetType.ADD).table(WorkType.class)
				.where("work_type_id", WhereType.EQUALS, null).batchEnd(new BatchPreparedStatementSetter() {

					@Override
					public void setValues(PreparedStatement ps, int i) throws SQLException {
						ps.setInt(1, 1);
						ps.setInt(2, workTypeIds.get(i));
					}

					@Override
					public int getBatchSize() {
						return workTypeIds.size();
					}
				});
	}

	@Override
	public List<WorkType> getAllWorkTypeForShoppingCarDao(List<WorkType> workTypes) {
		kumaSqlDao.selectMode();
		ListSqlResult<WorkType> theResult = kumaSqlDao.select("work_type_id").select("name").select("work_id", "w")
				.select("work_name", "w").select("work_cover", "w").select("stock").from(WorkType.class, "wt")
				.innerjoin(Work.class, "w", "work_id", WorkType.class, "work_id")
				.where("work_type_id", WhereType.IN,
						workTypes.stream().map(WorkType::getWorkTypeId).collect(Collectors.toList()))
				.endForList((rs, row) -> new WorkType.Builder().workTypeId(rs.getInt("work_type_id"))
						.name(rs.getString("name")).stock(rs.getInt("stock"))
						.work(new Work.Builder().workId(rs.getInt("w.work_id")).workName(rs.getString("w.work_name"))
								.workCover(rs.getString("w.work_cover")).build())
						.build());
		return theResult.getResult();
	}

}
