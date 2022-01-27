package co.jp.arche1.kdrs.companymaintenance.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import co.jp.arche1.kdrs.companymaintenance.repository.PtCompanyRepository;

@Mapper
public interface PtCompanyMapper {

	List<PtCompanyRepository> selectMany(
			@Param("companyId") Integer companyId,
			@Param("companyCode") String companyCode,
			@Param("companyName") String companyName);

	PtCompanyRepository selectOne(@Param("companyId") Integer companyId);

	Integer insert(PtCompanyRepository ptCompanyRepository);

	Integer update(PtCompanyRepository ptCompanyRepository);

	Integer deleteUpdate(@Param("companyId") Integer companyId,@Param("deleted") Byte deleted);

}
