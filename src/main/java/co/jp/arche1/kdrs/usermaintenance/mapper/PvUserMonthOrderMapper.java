package co.jp.arche1.kdrs.usermaintenance.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import co.jp.arche1.kdrs.usermaintenance.repository.PvUserMonthOrderRepository;

@Mapper
public interface PvUserMonthOrderMapper {

	List<PvUserMonthOrderRepository> selectMany(@Param("userId") Integer userId,
			@Param("userName") String userName,
			@Param("startDate") Integer startDate,
			@Param("endDate") Integer endDate,
			@Param("deleted") byte deleted);

}
