package co.jp.arche1.kdrs.work.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import co.jp.arche1.kdrs.work.repository.PvConstructionWorkUserRepository;

@Mapper
public interface PvConstructionWorkUserMapper {

	List<PvConstructionWorkUserRepository> selectMany(
			@Param("userId") Integer userId,
			@Param("privConstId") Integer privConstId, @Param("orderNo") Integer orderNo,
			@Param("workNo") Integer workNo, @Param("deleted") byte deleted);

}
