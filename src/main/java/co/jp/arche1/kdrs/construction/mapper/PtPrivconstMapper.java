package co.jp.arche1.kdrs.construction.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import co.jp.arche1.kdrs.construction.repository.PtPrivconstRepository;

@Mapper
public interface PtPrivconstMapper {

	void insertAll(List<PtPrivconstRepository> ptPrivconstRepositoryList);

	void updateAll(List<PtPrivconstRepository> ptPrivconstRepositoryList);

	void deleteAll(List<PtPrivconstRepository> ptPrivconstRepositoryList);

	void updateReportNo(@Param("ptPrivconstRepository") PtPrivconstRepository ptPrivconstRepository);

	void updateOrderNo(@Param("ptPrivconstRepository") PtPrivconstRepository ptPrivconstRepository);

	void updateWorkNo(@Param("ptPrivconstRepository") PtPrivconstRepository ptPrivconstRepository);

	void updateConstIdToNull(@Param("constId") Integer constId);

	Integer selectPrivconstId(@Param("privconstCode") String privconstCode);

	/*List<PtPrivconstRepository> selectMany(@Param("privConstId") Integer privConstId, @Param("userId") Integer userId,
		@Param("deleted") byte deleted); */

	void updateAllConstId(List<PtPrivconstRepository> ptPrivconstRepositoryList);

//	List<PtPrivconstRepository> selectMany();

}
