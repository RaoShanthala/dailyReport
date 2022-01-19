package co.jp.arche1.kdrs.usermaintenance.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import co.jp.arche1.kdrs.usermaintenance.repository.PtCompanyUserRepository;

@Mapper
public interface PtCompanyUserMapper {

	List<PtCompanyUserRepository> selectManyUserCompany(@Param("userId") Integer userId,
			@Param("companyId") Integer companyId);

	Integer insert(@Param("userId") Integer userId,
			@Param("companyId") Integer companyId);

}
