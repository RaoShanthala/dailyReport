package co.jp.arche1.kdrs.order.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import co.jp.arche1.kdrs.order.repository.PtOrderRepository;

@Mapper
public interface PtOrderMapper {

	void insert(@Param("ptOrderRepository") PtOrderRepository ptOrderRepository);

	void updateAll(List<PtOrderRepository> ptOrderRepositoryList);

	void deleteAll(List<PtOrderRepository> ptOrderRepositoryList);

	int selectOrderNo(@Param("orderCode") String orderCode);

	//List<PtOrderRepository> selectMany(@Param("privConstId") Integer privConstId, @Param("orderNo") Integer orderNo,
		//	@Param("deleted") byte deleted);

}
