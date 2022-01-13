package co.jp.arche1.kdrs.construction.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import co.jp.arche1.kdrs.construction.repository.PvPrivconstUserRepository;

@Mapper
public interface PvPrivconstUserMapper {

	List<PvPrivconstUserRepository> selectList(@Param("constId") Integer constId,
			@Param("privConstName") String privConstName,
			@Param("userName") String userName,
			@Param("searchType") Byte searchType);

	List<PvPrivconstUserRepository> selectMany(@Param("privConstId") Integer privConstId, @Param("userId") Integer userId,
			@Param("deleted") byte deleted);

}
