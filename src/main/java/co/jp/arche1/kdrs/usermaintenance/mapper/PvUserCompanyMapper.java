package co.jp.arche1.kdrs.usermaintenance.mapper;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import co.jp.arche1.kdrs.usermaintenance.repository.PvUserCompanyRepository;

@Mapper
public interface PvUserCompanyMapper {

	PvUserCompanyRepository selectOne(@Param("email") String email, @Param("companyCode") String companyCode);

}