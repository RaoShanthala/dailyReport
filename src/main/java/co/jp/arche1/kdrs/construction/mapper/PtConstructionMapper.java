package co.jp.arche1.kdrs.construction.mapper;

import java.time.LocalDate;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import co.jp.arche1.kdrs.construction.repository.PtConstructionRepository;

@Mapper
public interface PtConstructionMapper {

	List<PtConstructionRepository> selectMany(@Param("companyId") Integer companyId,
			@Param("constId") Integer constId, @Param("constCode") String constCode,
			@Param("constName") String constName, @Param("deleted") byte deleted);

	List<PtConstructionRepository> selectList(@Param("companyId") Integer companyId,
			@Param("constCode") String constCode,
			@Param("constName") String constName, @Param("targetState") byte targetState,
			@Param("targetStartDt") LocalDate targetStartDate,
			@Param("targetEndDt") LocalDate targetEndDate );

	PtConstructionRepository selectOne(@Param("companyId") Integer companyId,@Param("constId") Integer constId);

	Integer delete(@Param("companyId") Integer companyId,@Param("constId") Integer constId);

    Integer insert(PtConstructionRepository ptConstructionRepository);

    Integer update(PtConstructionRepository ptConstructionRepository);

}
