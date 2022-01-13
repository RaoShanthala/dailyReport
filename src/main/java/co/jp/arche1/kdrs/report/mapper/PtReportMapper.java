package co.jp.arche1.kdrs.report.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import co.jp.arche1.kdrs.report.repository.PtReportRepository;

@Mapper
public interface PtReportMapper {

	void insert(@Param("ptReportRepository") PtReportRepository ptReportRepository);

	void updateAll(List<PtReportRepository> ptReportRepositoryList);

	void deleteAll(List<PtReportRepository> ptReportRepositoryList);

	//List<PtReportRepository> selectMany(@Param("privConstId") Integer privConstId, @Param("reportNo") Integer reportNo,
			//@Param("deleted") byte deleted);

}

