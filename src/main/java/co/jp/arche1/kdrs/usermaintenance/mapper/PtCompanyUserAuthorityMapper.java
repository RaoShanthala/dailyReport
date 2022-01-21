package co.jp.arche1.kdrs.usermaintenance.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PtCompanyUserAuthorityMapper {

	Integer insert(@Param("userId") Integer userId,
			@Param("companyId") Integer companyId,
			@Param("status") Byte status,
			@Param("authorityType") Byte authorityType);

	Integer delete (@Param("userId") Integer userId,
			@Param("companyId") Integer companyId);

}
