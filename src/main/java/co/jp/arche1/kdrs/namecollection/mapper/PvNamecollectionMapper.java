package co.jp.arche1.kdrs.namecollection.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import co.jp.arche1.kdrs.namecollection.repository.PvNamecollectionRepository;

@Mapper
public interface PvNamecollectionMapper {

	List<PvNamecollectionRepository> selectMany(@Param("nameSectionParam") String namSection);

	List<PvNamecollectionRepository> selectResponseDtTitel(@Param("dtoClassName") String dtoClassName,
			@Param("dtoResClassName") String dtoResClassName,
			@Param("isSelect") String isSelect);

	PvNamecollectionRepository selectOne(@Param("nameSectionParam") String namSection,
			@Param("codeNumericParam") Short codeNumeric,
			@Param("codeStringParam") String codeString);

}