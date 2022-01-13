package co.jp.arche1.kdrs.report.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import co.jp.arche1.kdrs.report.repository.PvConstructionReportUserRepository;

@Mapper
public interface PvConstructionReportUserMapper {

	List<PvConstructionReportUserRepository> selectMany(
			@Param("userId") Integer userId,
			@Param("privConstId") Integer privConstId,
			@Param("reportNo") Integer reportNo,
			@Param("deleted") byte deleted );

}
