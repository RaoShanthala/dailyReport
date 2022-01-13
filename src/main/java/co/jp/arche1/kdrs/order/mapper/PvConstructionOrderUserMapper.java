package co.jp.arche1.kdrs.order.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import co.jp.arche1.kdrs.order.repository.PvConstructionOrderUserRepository;

@Mapper
public interface PvConstructionOrderUserMapper {

	List<PvConstructionOrderUserRepository> selectMany(
			@Param("userId") Integer userId,
			@Param("privConstId") Integer privConstId,
			@Param("orderNo") Integer orderNo,
			@Param("deleted") byte deleted);

}
