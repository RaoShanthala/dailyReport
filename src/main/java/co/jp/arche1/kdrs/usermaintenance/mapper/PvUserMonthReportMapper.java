package co.jp.arche1.kdrs.usermaintenance.mapper;

import java.time.LocalDate;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import co.jp.arche1.kdrs.usermaintenance.repository.PvUserMonthReportRepository;

@Mapper
public interface PvUserMonthReportMapper {

	List<PvUserMonthReportRepository> selectMany(
			@Param("companyId") Integer companyId,
			@Param("userId") Integer userId,
			@Param("constId") Integer constId,
			@Param("startDate") LocalDate startDate,
			@Param("endDate") LocalDate endDate,
			@Param("deleted") byte deleted);

}
