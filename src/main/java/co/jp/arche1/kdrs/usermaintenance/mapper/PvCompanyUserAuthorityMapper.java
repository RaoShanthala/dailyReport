package co.jp.arche1.kdrs.usermaintenance.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import co.jp.arche1.kdrs.usermaintenance.repository.PvCompanyUserAuthorityRepository;


@Mapper
public interface PvCompanyUserAuthorityMapper {
	/*PtUserAuthRepository selectOne(Integer userId);
	PtUserAuthRepository selectMax();
	List<PtUserAuthRepository> selectMany();
	List<PtUserAuthRepository> selectMany(String keyword);

	void insert(PtUserAuthRepository ptUserAuthRepository);
	Integer update(PtUserAuthRepository ptUserAuthRepository);
	Integer deleteByPrimaryKey(Integer userId, Integer authId);*/

	List<PvCompanyUserAuthorityRepository> selectAllAuthority(@Param("userId") Integer userId,
			@Param("companyId") Integer companyId);
}