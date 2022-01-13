package co.jp.arche1.kdrs.work.service;

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
import co.jp.arche1.kdrs.order.mapper.PtOrderMapper;
import co.jp.arche1.kdrs.work.dto.WorkDto;
import co.jp.arche1.kdrs.work.dto.WorkSearchManyDto;
import co.jp.arche1.kdrs.work.mapper.PtWorkMapper;
import co.jp.arche1.kdrs.work.mapper.PvConstructionWorkUserMapper;
import co.jp.arche1.kdrs.work.repository.PtWorkRepository;
import co.jp.arche1.kdrs.work.repository.PvConstructionWorkUserRepository;

@Service
public class WorkService extends BaseService {

	@Autowired
	PtWorkMapper ptWorkMapper;
	@Autowired
	PtOrderMapper ptOrderMapper;
	@Autowired
	PtPrivconstMapper ptPrivconstMapper;
	@Autowired
	PvConstructionWorkUserMapper pvConstructionWorkUserMapper;

	final static Logger logger = LoggerFactory.getLogger(WorkService.class);

	public void createUpdateDelete(WorkDto workDto) throws Exception {

		logger.debug(this.getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName());

		List<WorkDto.RequestDt> listReqDt = workDto.getReqDt();

		List<PtWorkRepository> listPtWorkRepositoryInsert = new ArrayList<PtWorkRepository>();
		List<PtWorkRepository> listPtWorkRepositoryUpdate = new ArrayList<PtWorkRepository>();
		List<PtWorkRepository> listPtWorkRepositoryDelete = new ArrayList<PtWorkRepository>();

		List<PtPrivconstRepository> listPtPrivconstRepositoryUpdate = new ArrayList<PtPrivconstRepository>();

		for (int i = 0; i < listReqDt.size(); i++) {
			PtWorkRepository ptWorkRepository = new PtWorkRepository();
			ptWorkRepository.setWorkCode(listReqDt.get(i).getWorkCode());
			ptWorkRepository.setUpdatedAt(listReqDt.get(i).getUpdatedAt());
			// 1.登録、2.更新、3.削除
			if (listReqDt.get(i).getAction() == (byte) 3) { // 削除
				ptWorkRepository.setDeleted((byte) 1);
				listPtWorkRepositoryDelete.add(ptWorkRepository);
			} else {
				int orderNo = ptOrderMapper.selectOrderNo(listReqDt.get(i).getOrderCode());
				ptWorkRepository.setOrderNo(orderNo);
				ptWorkRepository.setWorkDate(listReqDt.get(i).getWorkDate());
				ptWorkRepository.setSpot(listReqDt.get(i).getSpot());
				ptWorkRepository.setOutline(listReqDt.get(i).getOutline());
				ptWorkRepository.setDanger(listReqDt.get(i).getDanger());
				ptWorkRepository.setSafety(listReqDt.get(i).getSafety());
				ptWorkRepository.setDetail1(listReqDt.get(i).getDetail1());
				ptWorkRepository.setDetail2(listReqDt.get(i).getDetail2());
				ptWorkRepository.setDetail3(listReqDt.get(i).getDetail3());
				ptWorkRepository.setStaffClient(listReqDt.get(i).getStaffClient());
				ptWorkRepository.setLeader(listReqDt.get(i).getLeader());
				if (listReqDt.get(i).getFire() != null) {
					if (listReqDt.get(i).getFire().equalsIgnoreCase("FALSE")) {
						ptWorkRepository.setFire((byte) 0);
					} else {
						ptWorkRepository.setFire((byte) 1);
					}
				}
				ptWorkRepository.setNumPerson(listReqDt.get(i).getNumPerson());
				ptWorkRepository.setHours(listReqDt.get(i).getHours());

				if (listReqDt.get(i).getAction() == (byte) 1) { // 登録
					int privConstId = ptPrivconstMapper.selectPrivconstId(listReqDt.get(i).getConstCode());
					ptWorkRepository.setPrivConstId(privConstId);
					ptWorkRepository.setCreatedAt(listReqDt.get(i).getCreatedAt());
					listPtWorkRepositoryInsert.add(ptWorkRepository);
					// to Update maxWorkNo in PtPrivconst table
					PtPrivconstRepository ptPrivconstRepository = new PtPrivconstRepository();
					ptPrivconstRepository.setPrivConstCode(listReqDt.get(i).getConstCode());
					ptPrivconstRepository.setUpdatedAt(listReqDt.get(i).getUpdatedAt());
					listPtPrivconstRepositoryUpdate.add(ptPrivconstRepository);
				} else if (listReqDt.get(i).getAction() == (byte) 2) { // 更新
					listPtWorkRepositoryUpdate.add(ptWorkRepository);
				}
			}
		}
		try {
			if (listPtWorkRepositoryInsert.size() > 0) {
				for (int i = 0; i < listPtWorkRepositoryInsert.size(); i++) {
					PtWorkRepository ptWorkRepository = listPtWorkRepositoryInsert.get(i);
					ptWorkMapper.insert(ptWorkRepository);
					PtPrivconstRepository ptPrivconstRepository = listPtPrivconstRepositoryUpdate.get(i);
					ptPrivconstMapper.updateWorkNo(ptPrivconstRepository);
				}
			}
			if (listPtWorkRepositoryUpdate.size() > 0) {
				ptWorkMapper.updateAll(listPtWorkRepositoryUpdate);
			}
			if (listPtWorkRepositoryDelete.size() > 0) {
				ptWorkMapper.deleteAll(listPtWorkRepositoryDelete); // Soft delete
			}
		} catch (DataIntegrityViolationException ex) {
			workDto.setResultCode("002");
			workDto.setResultMessage("（操作 Data already exist. Cannot insert 、テーブル名：pt_work）" + ex.getMessage());
			return;
		} catch (Exception ex) {
			workDto.setResultCode("002");
			workDto.setResultMessage("（操作：Insert, Update, Delete 、テーブル名：pt_work）" + ex.getMessage());
			return;
		}
		workDto.setResultCode("000");
		return;
	}

	// 作業詳細明細検索
	public void searchMany(WorkSearchManyDto workSearchManyDto) throws Exception {
		logger.debug(this.getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName());

		WorkSearchManyDto.RequestHd reqHd = workSearchManyDto.getReqHd();
		/*List<PtWorkRepository> listPtWorkRepository = ptWorkMapper.selectMany(reqHd.getPrivConstId(),
				reqHd.getOrderNo(), reqHd.getWorkNo(), reqHd.getDeleted());*/

		List<PvConstructionWorkUserRepository> listPvConstructionWorkUserRepository = pvConstructionWorkUserMapper.selectMany(
				reqHd.getUserId(), reqHd.getPrivConstId(),
				reqHd.getOrderNo(), reqHd.getWorkNo(), reqHd.getDeleted());

		List<WorkSearchManyDto.ResponseDt> listResDt = workSearchManyDto.getResDt();
		for (Iterator<PvConstructionWorkUserRepository> it = listPvConstructionWorkUserRepository.iterator(); it.hasNext();) {
			PvConstructionWorkUserRepository pvConstructionWorkUserRepository = it.next();

			WorkSearchManyDto.ResponseDt resDt = new WorkSearchManyDto.ResponseDt();
			resDt.setPrivConstId(pvConstructionWorkUserRepository.getPrivConstId());
			resDt.setPrivConstCode(pvConstructionWorkUserRepository.getPrivConstCode());
			resDt.setPrivConstName(pvConstructionWorkUserRepository.getPrivConstName());
			resDt.setUserId(pvConstructionWorkUserRepository.getUserId());
			resDt.setUserName(pvConstructionWorkUserRepository.getUserName());
			resDt.setLoginUser(pvConstructionWorkUserRepository.getLoginUser());
			resDt.setWorkNo(pvConstructionWorkUserRepository.getWorkNo());
			resDt.setOrderNo(pvConstructionWorkUserRepository.getOrderNo());
			resDt.setWorkCode(pvConstructionWorkUserRepository.getWorkCode());
			resDt.setWorkDate(pvConstructionWorkUserRepository.getWorkDate());
			resDt.setSpot(pvConstructionWorkUserRepository.getSpot());
			resDt.setOutline(pvConstructionWorkUserRepository.getOutline());
			resDt.setDanger(pvConstructionWorkUserRepository.getDanger());
			resDt.setSafety(pvConstructionWorkUserRepository.getSafety());
			resDt.setDetail1(pvConstructionWorkUserRepository.getDetail1());
			resDt.setDetail2(pvConstructionWorkUserRepository.getDetail2());
			resDt.setDetail3(pvConstructionWorkUserRepository.getDetail3());
			resDt.setStaffClient(pvConstructionWorkUserRepository.getStaffClient());
			resDt.setLeader(pvConstructionWorkUserRepository.getLeader());
			if (pvConstructionWorkUserRepository.getFire() == (byte) 0) {
				resDt.setFire("FALSE");
			} else if (pvConstructionWorkUserRepository.getFire() == (byte) 1) {
				resDt.setFire("TRUE");
			}
			resDt.setNumPerson(pvConstructionWorkUserRepository.getNumPerson());
			resDt.setHours(pvConstructionWorkUserRepository.getHours());
			resDt.setCreatedAt(pvConstructionWorkUserRepository.getCreatedAt());
			resDt.setUpdatedAt(pvConstructionWorkUserRepository.getUpdatedAt());
			if (pvConstructionWorkUserRepository.getDeleted() == (byte) 0) {
				resDt.setDeleted("FALSE");
			} else if (pvConstructionWorkUserRepository.getDeleted() == (byte) 1) {
				resDt.setDeleted("TRUE");
			}
			listResDt.add(resDt);
		}
		makeResponseTitle(workSearchManyDto);

		if (reqHd.getDeleted() == (byte)2) {
			reqHd.setDeleted(null);
		}

		if (listResDt.size() > 0) {
			workSearchManyDto.setResultCode("000");
		} else {
			workSearchManyDto.setResultCode("001");
		}
		return;
	}

}