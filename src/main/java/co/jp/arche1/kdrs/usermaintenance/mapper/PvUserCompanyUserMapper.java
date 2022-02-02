package co.jp.arche1.kdrs.usermaintenance.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import co.jp.arche1.kdrs.usermaintenance.repository.PvUserCompanyUserRepository;

@Mapper
public interface PvUserCompanyUserMapper {

	List<PvUserCompanyUserRepository> selectManyUsers(@Param("companyId") Integer companyId,
			@Param("sei") String sei,
			@Param("mei") String mei,
			@Param("status") byte status,
			@Param("deleted") byte deleted);

	List<PvUserCompanyUserRepository> selectOneUser(@Param("companyId") Integer companyId,
			@Param("userId") Integer userId);

	List<PvUserCompanyUserRepository> selectManyAdmins(@Param("companyId") Integer companyId,
			@Param("sei") String sei,
			@Param("mei") String mei);


}
