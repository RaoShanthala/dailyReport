package co.jp.arche1.kdrs.work.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import co.jp.arche1.kdrs.work.repository.PtWorkRepository;

@Mapper
public interface PtWorkMapper {

	void insert(@Param("ptWorkRepository") PtWorkRepository ptWorkRepository);

	void updateAll(List<PtWorkRepository> ptWorkRepositoryList);

	void deleteAll(List<PtWorkRepository> ptWorkRepositoryList);

	/*List<PtWorkRepository> selectMany(@Param("privConstId") Integer privConstId, @Param("orderNo") Integer orderNo,
			@Param("workNo") Integer workNo, @Param("deleted") byte deleted);*/

}


