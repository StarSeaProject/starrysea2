package top.starrysea.dao.impl;

import top.starrysea.common.Condition;
import top.starrysea.dao.IWorkDao;
import top.starrysea.kql.clause.OrderByType;
import top.starrysea.kql.clause.SelectClause;
import top.starrysea.kql.clause.UpdateSetType;
import top.starrysea.kql.clause.WhereType;
import top.starrysea.kql.facede.EntitySqlResult;
import top.starrysea.kql.facede.IntegerSqlResult;
import top.starrysea.kql.facede.KumaSqlDao;
import top.starrysea.kql.facede.ListSqlResult;
import top.starrysea.kql.facede.UpdateSqlResult;
import top.starrysea.object.dto.Work;

import static top.starrysea.common.Common.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("workDao")
public class WorkDaoImpl implements IWorkDao {

	@Autowired
	private KumaSqlDao kumaSqlDao;
	// 作品每页显示条数
	public static final int PAGE_LIMIT = 10;

	@Override
	// 查询所有作品
	public List<Work> getAllWorkDao(Condition condition, Work work) {
		kumaSqlDao.selectMode();
		ListSqlResult<Work> theResult = kumaSqlDao.select("work_id").select("work_name").select("work_cover")
				.select("work_summary").from(Work.class).where("work_name", WhereType.FUZZY, work.getWorkName())
				.orderBy("work_uploadtime", OrderByType.DESC).limit((condition.getPage() - 1) * PAGE_LIMIT, PAGE_LIMIT)
				.endForList((rs, row) -> new Work.Builder().workId(rs.getInt("work_id"))
						.workName(rs.getString("work_name")).workCover(rs.getString("work_cover"))
						.workSummary(rs.getString("work_summary")).build());
		return theResult.getResult();
	}

	@Override
	// 查询所有作品的数量，用于分页
	public int getWorkCountDao(Condition condition, Work work) {
		kumaSqlDao.selectMode();
		IntegerSqlResult theResult = kumaSqlDao.select(SelectClause.COUNT).from(Work.class)
				.where("work_name", WhereType.FUZZY, work.getWorkName()).endForNumber();
		return theResult.getResult();
	}

	@Override
	// 查询一个作品的详情页
	public Work getWorkDao(Work work) {
		kumaSqlDao.selectMode();
		EntitySqlResult<Work> theResult = kumaSqlDao.select("work_name").select("work_uploadtime")
				.select("work_pdfpath").select("work_pdf_password").select("work_click").select("work_cover")
				.from(Work.class).where("work_id", WhereType.EQUALS, work.getWorkId())
				.endForObject((rs, row) -> new Work.Builder().workName(rs.getString("work_name"))
						.workUploadTime(date2String(rs.getDate("work_uploadtime")))
						.workPdfpath(rs.getString("work_pdfpath")).workPdfPassword(rs.getString("work_pdf_password"))
						.workClick(rs.getInt("work_click")).workCover(rs.getString("work_cover")).build());
		return theResult.getResult();
	}

	@Override
	// 添加一个作品
	public int saveWorkDao(Work work) {
		kumaSqlDao.insertMode();
		UpdateSqlResult theResult = kumaSqlDao.insert("work_name", work.getWorkName())
				.insert("work_uploadtime", work.getWorkUploadTime()).insert("work_pdfpath", work.getWorkPdfpath())
				.insert("work_cover", work.getWorkCover()).insert("work_summary", work.getWorkSummary())
				.table(Work.class).end();
		return theResult.getKeyHolder().getKey().intValue();
	}

	@Override
	// 管理员删除一个作品
	public void deleteWorkDao(Work work) {
		kumaSqlDao.deleteMode();
		kumaSqlDao.table(Work.class).where("work_id", WhereType.EQUALS, work.getWorkId()).end();
	}

	@Override
	public void addWorkClick(Work work) {
		kumaSqlDao.updateMode();
		kumaSqlDao.update("work_click", UpdateSetType.ADD, 1).where("work_id", WhereType.EQUALS, work.getWorkId())
				.table(Work.class).end();
	}

	@Override
	public List<Work> getWorkByActivityDao(List<Integer> activityIds) {
		kumaSqlDao.selectMode();
		ListSqlResult<Work> theResult = kumaSqlDao.select("work_id").select("work_name").select("activity_id")
				.from(Work.class).where("activity_id", WhereType.IN, activityIds.stream().collect(Collectors.toList()))
				.endForList((rs, row) -> new Work.Builder().workId(rs.getInt("work_id"))
						.workName(rs.getString("work_name")).activityId(rs.getInt("activity_id")).build());
		return theResult.getResult();
	}
}
