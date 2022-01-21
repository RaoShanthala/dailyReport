package co.jp.arche1.kdrs.construction.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import co.jp.arche1.kdrs.construction.repository.PvPrivconstUserRepository;

@Mapper
public interface PvPrivconstUserMapper {

	List<PvPrivconstUserRepository> selectList(@Param("companyId") Integer companyId,
			@Param("constId") Integer constId,
			@Param("privConstName") String privConstName,
			@Param("sei") String sei,
			@Param("mei") String mei,
			@Param("searchType") Byte searchType);

	List<PvPrivconstUserRepository> selectMany(@Param("companyId") Integer companyId,
			@Param("privConstId") Integer privConstId, @Param("userId") Integer userId,
			@Param("deleted") byte deleted);

}
