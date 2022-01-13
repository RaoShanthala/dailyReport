package co.jp.arche1.kdrs.namecollection.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import co.jp.arche1.kdrs.namecollection.repository.PvResultMessageRepository;

@Mapper
public interface PvResultMessageMapper {

	PvResultMessageRepository selectOne(@Param("dtoNameParam") String dtoName, @Param("resultCodeParam") String resultCode);

}