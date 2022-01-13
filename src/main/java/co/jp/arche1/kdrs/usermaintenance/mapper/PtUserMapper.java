package co.jp.arche1.kdrs.usermaintenance.mapper;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import co.jp.arche1.kdrs.usermaintenance.repository.PtUserRepository;

@Mapper
public interface PtUserMapper {
	PtUserRepository selectOne(String loginUser);

	Integer insert(PtUserRepository ptUserRepository);
	Integer update(@Param("ptUserRepository") PtUserRepository ptUserRepository, @Param("updDatetimeWhenReading") LocalDateTime updDatetimeWhenReading);
	//Integer update(PtUserRepository ptUserRepository);
	Integer delete(@Param("userId") Integer userId, @Param("updDatetimeWhenReading") LocalDateTime updDatetimeWhenReading);

	List<PtUserRepository> selecAll(@Param("userId") Integer userId,@Param("loginUser") String loginUser,@Param("userName") String userName);

	//List<PtUserRepository> selectMany(@Param("email") String email);
}