package co.jp.arche1.kdrs.order.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import co.jp.arche1.kdrs.common.BaseService;
import co.jp.arche1.kdrs.construction.mapper.PtPrivconstMapper;
import co.jp.arche1.kdrs.construction.repository.PtPrivconstRepository;
import co.jp.arche1.kdrs.order.dto.OrderDto;
import co.jp.arche1.kdrs.order.dto.OrderSearchManyDto;
import co.jp.arche1.kdrs.order.mapper.PtOrderMapper;
import co.jp.arche1.kdrs.order.mapper.PvConstructionOrderUserMapper;
import co.jp.arche1.kdrs.order.repository.PtOrderRepository;
import co.jp.arche1.kdrs.order.repository.PvConstructionOrderUserRepository;

@Service
public class OrderService extends BaseService {

	@Autowired
	PtOrderMapper ptOrderMapper;
	@Autowired
	PtPrivconstMapper ptPrivconstMapper;
	@Autowired
	PvConstructionOrderUserMapper pvConstructionOrderUserMapper;

	final static Logger logger = LoggerFactory.getLogger(OrderService.class);

	public void createUpdateDelete(OrderDto orderDto) throws Exception {

		logger.debug(this.getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName());

		List<OrderDto.RequestDt> listReqDt = orderDto.getReqDt();

		List<PtOrderRepository> listPtOrderRepositoryInsert = new ArrayList<PtOrderRepository>();
		List<PtOrderRepository> listPtOrderRepositoryUpdate = new ArrayList<PtOrderRepository>();
		List<PtOrderRepository> listPtOrderRepositoryDelete = new ArrayList<PtOrderRepository>();

		List<PtPrivconstRepository> listPtPrivconstRepositoryUpdate = new ArrayList<PtPrivconstRepository>();

		for (int i = 0; i < listReqDt.size(); i++) {
			PtOrderRepository ptOrderRepository = new PtOrderRepository();
			ptOrderRepository.setOrderCode(listReqDt.get(i).getOrderCode());
			ptOrderRepository.setUpdatedAt(listReqDt.get(i).getUpdatedAt());
			// 1.登録、2.更新、3.削除
			if (listReqDt.get(i).getAction() == (byte) 3) { // 削除
				ptOrderRepository.setDeleted((byte) 1);
				listPtOrderRepositoryDelete.add(ptOrderRepository);
			} else {
				ptOrderRepository.setOrderTitle(listReqDt.get(i).getOrderTitle());
				ptOrderRepository.setOrderDate(listReqDt.get(i).getOrderDate());
				ptOrderRepository.setOrderContents(listReqDt.get(i).getOrderContents());
				ptOrderRepository.setOrderCause(listReqDt.get(i).getOrderCause());
				ptOrderRepository.setStaff(listReqDt.get(i).getStaff());
				ptOrderRepository.setStaffClient(listReqDt.get(i).getStaffClient());
				if (listReqDt.get(i).getSignature() != null) {
					if (listReqDt.get(i).getSignature().equalsIgnoreCase("FALSE")) {
						ptOrderRepository.setSignature((byte) 0);
					} else {
						ptOrderRepository.setSignature((byte) 1);
					}
				}
				if (listReqDt.get(i).getAction() == (byte) 1) { // 登録
					int privConstId = ptPrivconstMapper.selectPrivconstId(listReqDt.get(i).getConstCode());
					ptOrderRepository.setPrivConstId(privConstId);
					ptOrderRepository.setCreatedAt(listReqDt.get(i).getCreatedAt());
					listPtOrderRepositoryInsert.add(ptOrderRepository);
					// to Update maxReportNo in PtPrivconst table
					PtPrivconstRepository ptPrivconstRepository = new PtPrivconstRepository();
					ptPrivconstRepository.setPrivConstCode(listReqDt.get(i).getConstCode());
					ptPrivconstRepository.setUpdatedAt(listReqDt.get(i).getUpdatedAt());
					listPtPrivconstRepositoryUpdate.add(ptPrivconstRepository);
				} else if (listReqDt.get(i).getAction() == (byte) 2) { // 更新
					listPtOrderRepositoryUpdate.add(ptOrderRepository);
				}
			}
		}
		try {
			if (listPtOrderRepositoryInsert.size() > 0) {
				for (int i = 0; i < listPtOrderRepositoryInsert.size(); i++) {
					PtOrderRepository ptOrderRepository = listPtOrderRepositoryInsert.get(i);
					ptOrderMapper.insert(ptOrderRepository);
					PtPrivconstRepository ptPrivconstRepository = listPtPrivconstRepositoryUpdate.get(i);
					ptPrivconstMapper.updateOrderNo(ptPrivconstRepository);
				}
			}
			if (listPtOrderRepositoryUpdate.size() > 0) {
				ptOrderMapper.updateAll(listPtOrderRepositoryUpdate);
			}
			if (listPtOrderRepositoryDelete.size() > 0) {
				ptOrderMapper.deleteAll(listPtOrderRepositoryDelete); // Soft delete
			}
		} catch (DataIntegrityViolationException ex) {
			orderDto.setResultCode("002");
			orderDto.setResultMessage("（操作 Data already exist. Cannot insert 、テーブル名：pt_order）" + ex.getMessage());
			return;
		} catch (Exception ex) {
			orderDto.setResultCode("002");
			orderDto.setResultMessage("（操作：Insert, Update, Delete 、テーブル名：pt_order）" + ex.getMessage());
			return;
		}
		orderDto.setResultCode("000");
		return;
	}

	// 変更作業概要検索
	public void searchMany(OrderSearchManyDto orderSearchManyDto) throws Exception {
		logger.debug(this.getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName());

		OrderSearchManyDto.RequestHd reqHd = orderSearchManyDto.getReqHd();
		List<PvConstructionOrderUserRepository> listPtOrderRepository = pvConstructionOrderUserMapper.selectMany(
				reqHd.getUserId(), reqHd.getPrivConstId(),
				reqHd.getOrderNo(), reqHd.getDeleted());

		List<OrderSearchManyDto.ResponseDt> listResDt = orderSearchManyDto.getResDt();
		for (Iterator<PvConstructionOrderUserRepository> it = listPtOrderRepository.iterator(); it.hasNext();) {
			PvConstructionOrderUserRepository pvConstructionOrderUserRepository = it.next();

			OrderSearchManyDto.ResponseDt resDt = new OrderSearchManyDto.ResponseDt();
			resDt.setPrivConstId(pvConstructionOrderUserRepository.getPrivConstId());
			resDt.setPrivConstCode(pvConstructionOrderUserRepository.getPrivConstCode());
			resDt.setPrivConstName(pvConstructionOrderUserRepository.getPrivConstName());
			resDt.setUserId(pvConstructionOrderUserRepository.getUserId());
			resDt.setUserName(pvConstructionOrderUserRepository.getUserName());
			resDt.setLoginUser(pvConstructionOrderUserRepository.getLoginUser());
			resDt.setOrderNo(pvConstructionOrderUserRepository.getOrderNo());
			resDt.setOrderCode(pvConstructionOrderUserRepository.getOrderCode());
			resDt.setOrderTitle(pvConstructionOrderUserRepository.getOrderTitle());
			resDt.setOrderDate(pvConstructionOrderUserRepository.getOrderDate());
			resDt.setOrderContents(pvConstructionOrderUserRepository.getOrderContents());
			resDt.setOrderCause(pvConstructionOrderUserRepository.getOrderCause());
			resDt.setStaff(pvConstructionOrderUserRepository.getStaff());
			resDt.setStaffClient(pvConstructionOrderUserRepository.getStaffClient());
			if (pvConstructionOrderUserRepository.getSignature() == (byte) 0) {
				resDt.setSignature("FALSE");
			} else if (pvConstructionOrderUserRepository.getSignature() == (byte) 1) {
				resDt.setSignature("TRUE");
			}
			resDt.setCreatedAt(pvConstructionOrderUserRepository.getCreatedAt());
			resDt.setUpdatedAt(pvConstructionOrderUserRepository.getUpdatedAt());
			if (pvConstructionOrderUserRepository.getDeleted() == (byte) 0) {
				resDt.setDeleted("FALSE");
			} else if (pvConstructionOrderUserRepository.getDeleted() == (byte) 1) {
				resDt.setDeleted("TRUE");
			}
			listResDt.add(resDt);
		}

		makeResponseTitle(orderSearchManyDto);

		if (reqHd.getDeleted() == (byte)2) {
			reqHd.setDeleted(null);
		}

		if (listResDt.size() > 0) {
			orderSearchManyDto.setResultCode("000");
		} else {
			orderSearchManyDto.setResultCode("001");
		}
		return;
	}

}
