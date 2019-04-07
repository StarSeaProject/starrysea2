package top.starrysea.dao;

import java.util.List;

import top.starrysea.object.dto.Funding;

public interface IFundingDao {

	List<Funding> getAllFundingDao(Funding funding);

	void saveFundingDao(List<Funding> fundings);

	void deleteFundingDao(Funding funding);
	
	List<Funding> getFundingByUserDao(String userId);
}
