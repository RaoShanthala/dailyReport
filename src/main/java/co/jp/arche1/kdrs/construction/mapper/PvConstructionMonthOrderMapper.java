package co.jp.arche1.kdrs.construction.mapper;

import java.time.LocalDate;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import co.jp.arche1.kdrs.construction.repository.PvConstructionMonthOrderRepository;

@Mapper
public interface PvConstructionMonthOrderMapper {

	List<PvConstructionMonthOrderRepository> selectMany(@Param("constId") Integer constId,
			@Param("startDate") LocalDate startDate,
			@Param("endDate") LocalDate endDate,
			@Param("deleted") byte deleted);

}
