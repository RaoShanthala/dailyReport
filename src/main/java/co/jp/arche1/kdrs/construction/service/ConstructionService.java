package co.jp.arche1.kdrs.construction.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import co.jp.arche1.kdrs.common.BaseService;
import co.jp.arche1.kdrs.construction.dto.ConstructionDeleteDto;
import co.jp.arche1.kdrs.construction.dto.ConstructionDto;
import co.jp.arche1.kdrs.construction.dto.ConstructionInsertDto;
import co.jp.arche1.kdrs.construction.dto.ConstructionListDto;
import co.jp.arche1.kdrs.construction.dto.ConstructionMonthOrderDto;
import co.jp.arche1.kdrs.construction.dto.ConstructionMonthReportDto;
//import co.jp.arche1.kdrs.construction.dto.ConstructionSearchManyDto;
import co.jp.arche1.kdrs.construction.dto.ConstructionSearchOneDto;
import co.jp.arche1.kdrs.construction.dto.ConstructionUpdateDto;
import co.jp.arche1.kdrs.construction.dto.PrivConstructionSearchListDto;
import co.jp.arche1.kdrs.construction.dto.PrivConstructionSearchManyDto;
import co.jp.arche1.kdrs.construction.dto.PrivConstructionUpdateConstIdDto;
import co.jp.arche1.kdrs.construction.mapper.PtConstructionMapper;
import co.jp.arche1.kdrs.construction.mapper.PtPrivconstMapper;
import co.jp.arche1.kdrs.construction.mapper.PvConstructionMonthOrderMapper;
import co.jp.arche1.kdrs.construction.mapper.PvConstructionMonthReportMapper;
import co.jp.arche1.kdrs.construction.mapper.PvPrivconstUserMapper;
import co.jp.arche1.kdrs.construction.repository.PtConstructionRepository;
import co.jp.arche1.kdrs.construction.repository.PtPrivconstRepository;
import co.jp.arche1.kdrs.construction.repository.PvConstructionMonthOrderRepository;
import co.jp.arche1.kdrs.construction.repository.PvConstructionMonthReportRepository;
import co.jp.arche1.kdrs.construction.repository.PvPrivconstUserRepository;

@Service
public class ConstructionService extends BaseService {

	@Autowired
	PtPrivconstMapper ptPrivconstMapper;

	@Autowired
	PtConstructionMapper ptConstructionMapper;

	@Autowired
	PvConstructionMonthReportMapper pvConstructionMonthReportMapper;

	@Autowired
	PvConstructionMonthOrderMapper pvConstructionMonthOrderMapper;

	@Autowired
	PvPrivconstUserMapper pvPrivconstUserMapper;

	final static Logger logger = LoggerFactory.getLogger(ConstructionService.class);

	public void createUpdateDeletePrivConstruction(ConstructionDto constructionDto) throws Exception {
		logger.debug(this.getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName());

		List<ConstructionDto.RequestDt> listReqDt = constructionDto.getReqDt();

		List<PtPrivconstRepository> listPtPrivconstRepositoryInsert = new ArrayList<PtPrivconstRepository>();
		List<PtPrivconstRepository> listPtPrivconstRepositoryUpdate = new ArrayList<PtPrivconstRepository>();
		List<PtPrivconstRepository> listPtPrivconstRepositoryDelete = new ArrayList<PtPrivconstRepository>();

		for (int i = 0; i < listReqDt.size(); i++) {
			PtPrivconstRepository ptPrivconstRepository = new PtPrivconstRepository();
			ptPrivconstRepository.setPrivConstCode(listReqDt.get(i).getConstCode());
			ptPrivconstRepository.setUpdatedAt(listReqDt.get(i).getUpdatedAt());
			// 1.登録、2.更新、3.削除
			if (listReqDt.get(i).getAction() == (byte) 3) { // 削除
				ptPrivconstRepository.setDeleted((byte) 1);
				listPtPrivconstRepositoryDelete.add(ptPrivconstRepository);
			} else {
				ptPrivconstRepository.setPrivConstName(listReqDt.get(i).getConstName());
				ptPrivconstRepository.setUserId(listReqDt.get(i).getUserId());
				if (listReqDt.get(i).getAction() == (byte) 1) { // 登録
					ptPrivconstRepository.setCreatedAt(listReqDt.get(i).getCreatedAt());
					listPtPrivconstRepositoryInsert.add(ptPrivconstRepository);
				} else if (listReqDt.get(i).getAction() == (byte) 2) { // 更新
					listPtPrivconstRepositoryUpdate.add(ptPrivconstRepository);
				}
			}
		}
		try {
			if (listPtPrivconstRepositoryInsert.size() > 0) {
				ptPrivconstMapper.insertAll(listPtPrivconstRepositoryInsert);
			}
			if (listPtPrivconstRepositoryUpdate.size() > 0) {
				ptPrivconstMapper.updateAll(listPtPrivconstRepositoryUpdate);
			}
			if (listPtPrivconstRepositoryDelete.size() > 0) {
				ptPrivconstMapper.deleteAll(listPtPrivconstRepositoryDelete); // Soft delete
			}
		} catch (DataIntegrityViolationException ex) {
			constructionDto.setResultCode("002");
			constructionDto
					.setResultMessage("（操作 Data already exist. Cannot insert 、テーブル名：pt_privconst）" + ex.getMessage());
			return;
		} catch (Exception ex) {
			constructionDto.setResultCode("002");
			constructionDto.setResultMessage("（操作：Insert, Update, Delete 、テーブル名：pt_privconst）" + ex.getMessage());
			return;
		}
		constructionDto.setResultCode("000");
		return;
	}

	/*public void searchManyConstruction(ConstructionSearchManyDto constructionSearchManyDto) throws Exception {
		logger.debug(this.getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName());

		ConstructionSearchManyDto.RequestHd reqHd = constructionSearchManyDto.getReqHd();
		List<PtConstructionRepository> listPtConstructionRepository = ptConstructionMapper
				.selectMany(reqHd.getConstId(), reqHd.getConstCode(), reqHd.getConstName(), reqHd.getDeleted());

		// List<PtPrivconstRepository> listPtPricconstRepository =
		// ptPrivconstMapper.selectMany(0,0,(byte)0);

		List<ConstructionSearchManyDto.ResponseDt> listResDt = constructionSearchManyDto.getResDt();
		for (Iterator<PtConstructionRepository> it = listPtConstructionRepository.iterator(); it.hasNext();) {
			PtConstructionRepository ptConstructionRepository = it.next();

			ConstructionSearchManyDto.ResponseDt resDt = new ConstructionSearchManyDto.ResponseDt();
			resDt.setConstId(ptConstructionRepository.getConstId());
			resDt.setConstCode(ptConstructionRepository.getConstCode());
			resDt.setConstName(ptConstructionRepository.getConstName());
			resDt.setUserId(ptConstructionRepository.getUserId());
			resDt.setStartDate(ptConstructionRepository.getStartDate());
			resDt.setEndDate(ptConstructionRepository.getEndDate());
			resDt.setCreatedAt(ptConstructionRepository.getCreatedAt());
			resDt.setUpdatedAt(ptConstructionRepository.getUpdatedAt());
			if (ptConstructionRepository.getDeleted() == (byte) 0) {
				resDt.setDeleted("FALSE");
			} else if (ptConstructionRepository.getDeleted() == (byte) 1) {
				resDt.setDeleted("TRUE");
			}
			listResDt.add(resDt);
		}

		makeResponseTitle(constructionSearchManyDto);

		if (reqHd.getDeleted() == (byte) 2) {
			reqHd.setDeleted(null);
		}

		if (listResDt.size() > 0) {
			constructionSearchManyDto.setResultCode("000");
		} else {
			constructionSearchManyDto.setResultCode("001");
		}
		return;
	} */

	public void searchConstructionList(ConstructionListDto constructionListDto) throws Exception{
		logger.debug(this.getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName());

		ConstructionListDto.RequestHd reqHd = constructionListDto.getReqHd();
		System.out.println ("searchConstructionList reqHd.getCompanyId() " + reqHd.getCompanyId());

		List<PtConstructionRepository> listPtConstructionRepository = ptConstructionMapper
				.selectList(reqHd.getCompanyId(), reqHd.getConstCode(), reqHd.getConstName(), reqHd.getTargetState(),
						reqHd.getTargetStartDate(), reqHd.getTargetEndDate());

		List<ConstructionListDto.ResponseDt> listResDt = constructionListDto.getResDt();
		for (Iterator<PtConstructionRepository> it = listPtConstructionRepository.iterator(); it.hasNext();) {
			PtConstructionRepository ptConstructionRepository = it.next();

			ConstructionListDto.ResponseDt resDt = new ConstructionListDto.ResponseDt();
			resDt.setCompanyId(ptConstructionRepository.getCompanyId());
			resDt.setConstId(ptConstructionRepository.getConstId());
			resDt.setConstCode(ptConstructionRepository.getConstCode());
			resDt.setConstName(ptConstructionRepository.getConstName());
			resDt.setUserId(ptConstructionRepository.getUserId());
			resDt.setStartDate(ptConstructionRepository.getStartDate());
			resDt.setEndDate(ptConstructionRepository.getEndDate());

			resDt.setCreatedAt(ptConstructionRepository.getCreatedAt().withNano(0));
			resDt.setUpdatedAt(ptConstructionRepository.getUpdatedAt().withNano(0));
			if (ptConstructionRepository.getDeleted() == (byte) 0) {
				resDt.setDeleted("FALSE");
			} else if (ptConstructionRepository.getDeleted() == (byte) 1) {
				resDt.setDeleted("TRUE");
			}
			listResDt.add(resDt);
		}

		makeResponseTitle(constructionListDto);

		if (listResDt.size() > 0) {
			constructionListDto.setResultCode("000");
		} else {
			constructionListDto.setResultCode("001");
		}
		return;
	}

	public void searchConstructionOne(ConstructionSearchOneDto constructionSearchOneDto) throws Exception {
		logger.debug(this.getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName());

		ConstructionSearchOneDto.RequestHd reqHd = constructionSearchOneDto.getReqHd();
		PtConstructionRepository ptConstructionRepository = ptConstructionMapper.selectOne(
				reqHd.getCompanyId(),reqHd.getConstId());
		ConstructionSearchOneDto.ResponseDt resDt = constructionSearchOneDto.getResDt();
		resDt.setCompanyId(ptConstructionRepository.getCompanyId());
		resDt.setConstId(ptConstructionRepository.getConstId());
		resDt.setConstCode(ptConstructionRepository.getConstCode());
		resDt.setConstName(ptConstructionRepository.getConstName());
		resDt.setUserId(ptConstructionRepository.getUserId());
		resDt.setStartDate(ptConstructionRepository.getStartDate());
		resDt.setEndDate(ptConstructionRepository.getEndDate());
		resDt.setCreatedAt(ptConstructionRepository.getCreatedAt());
		resDt.setUpdatedAt(ptConstructionRepository.getUpdatedAt());
		if (ptConstructionRepository.getDeleted() == (byte) 0) {
			resDt.setDeleted("FALSE");
		} else if (ptConstructionRepository.getDeleted() == (byte) 1) {
			resDt.setDeleted("TRUE");
		}
		makeResponseTitle(constructionSearchOneDto);
		constructionSearchOneDto.setResultCode("000");
		return;
	}

	public void insert(ConstructionInsertDto constructionInsertDto) throws Exception {
		logger.debug(this.getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName());

		ConstructionInsertDto.RequestHd reqHd = constructionInsertDto.getReqHd();
		PtConstructionRepository ptConstructionRepository = new PtConstructionRepository();
		ptConstructionRepository.setCompanyId(reqHd.getCompanyId());
		ptConstructionRepository.setConstCode(reqHd.getConstCode());
		ptConstructionRepository.setConstName(reqHd.getConstName());
		ptConstructionRepository.setUserId(reqHd.getUserId());
		ptConstructionRepository.setStartDate(reqHd.getStartDate());
		ptConstructionRepository.setEndDate(reqHd.getEndDate());
		try {
			// 工事の登録
			ptConstructionMapper.insert(ptConstructionRepository);
		} catch (DuplicateKeyException ex) {
			constructionInsertDto.setResultCode("002");
			constructionInsertDto.setResultMessage("（操作：insert、テーブル名：pt_construction）");
			return;
		}
		constructionInsertDto.setResultCode("000");
		return;
	}

	public void updateConstId(PrivConstructionUpdateConstIdDto privConstructionUpdateConstIdDto) throws Exception{
		logger.debug(this.getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName());

		PrivConstructionUpdateConstIdDto.RequestHd reqHd = privConstructionUpdateConstIdDto.getReqHd();
		PrivConstructionUpdateConstIdDto.RequestDt reqDt = privConstructionUpdateConstIdDto.getReqDt();

		List<PtPrivconstRepository> listPtPrivconstRepository = new ArrayList<PtPrivconstRepository>();
		for (int i = 0; i < reqDt.getPrivConstId().length; i++) {
			PtPrivconstRepository ptPrivconstRepository = new PtPrivconstRepository();
			ptPrivconstRepository.setCompanyId(reqHd.getCompanyId());
			if (reqHd.getAction() == 1) { //add
				ptPrivconstRepository.setConstId(reqHd.getConstId());
			}else { // delete
				ptPrivconstRepository.setConstId(null);
			}
			ptPrivconstRepository.setPrivConstId(reqDt.getPrivConstId()[i]);
			listPtPrivconstRepository.add(ptPrivconstRepository);
		}
		try {
			// constIdの追加
			if (listPtPrivconstRepository.size() > 0) {
				ptPrivconstMapper.updateAllConstId(listPtPrivconstRepository);
			}
		} catch (DuplicateKeyException ex) {
			privConstructionUpdateConstIdDto.setResultCode("002");
			privConstructionUpdateConstIdDto.setResultMessage("（操作：update constId、テーブル名：pt_privconst）");
			return;
		}
		privConstructionUpdateConstIdDto.setResultCode("000");
		return;
	}

	public void update(ConstructionUpdateDto constructionUpdateDto) throws Exception {
		logger.debug(this.getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName());

		ConstructionUpdateDto.RequestHd reqHd = constructionUpdateDto.getReqHd();
		PtConstructionRepository ptConstructionRepository = new PtConstructionRepository();
		ptConstructionRepository.setConstId(reqHd.getConstId());
		ptConstructionRepository.setCompanyId(reqHd.getCompanyId());
		ptConstructionRepository.setConstCode(reqHd.getConstCode());
		ptConstructionRepository.setConstName(reqHd.getConstName());
		ptConstructionRepository.setUserId(reqHd.getUserId());  //Needed??
		ptConstructionRepository.setStartDate(reqHd.getStartDate());
		ptConstructionRepository.setEndDate(reqHd.getEndDate());
		try {
			// 工事の更新
			ptConstructionMapper.update(ptConstructionRepository);
		} catch (DuplicateKeyException ex) {
			constructionUpdateDto.setResultCode("002");
			constructionUpdateDto.setResultMessage("（操作：update、テーブル名：pt_construction）");
			return;
		}
		constructionUpdateDto.setResultCode("000");
		return;
	}

	// 工事の削除
	public void delete(ConstructionDeleteDto constructionDeleteDto) throws Exception {
		logger.debug(this.getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName());

		ConstructionDeleteDto.RequestHd reqHd = constructionDeleteDto.getReqHd();
		try {
			//update const_id = null for pt_privconst table where const_id = reqHd.getConstId()
			ptPrivconstMapper.updateConstIdToNull(reqHd.getCompanyId(), reqHd.getConstId());
			// 工事の削除
			ptConstructionMapper.delete(reqHd.getCompanyId(),reqHd.getConstId());
		} catch (Exception ex) {
			constructionDeleteDto.setResultCode("002");
			constructionDeleteDto.setResultMessage("（操作：delete、テーブル名：pt_construction）");
			return;
		}
		constructionDeleteDto.setResultCode("000");
		return;
	}

	// プライベート工事List
	public void searchPrivConstructionList(PrivConstructionSearchListDto privConstructionSearchListDto)throws Exception{
		logger.debug(this.getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName());

		PrivConstructionSearchListDto.RequestHd reqHd = privConstructionSearchListDto.getReqHd();
		List<PvPrivconstUserRepository> listPtPricconstRepository = pvPrivconstUserMapper.selectList(reqHd.getCompanyId(),
				reqHd.getConstId(), reqHd.getPrivConstName(), reqHd.getSei(), reqHd.getMei() ,reqHd.getSearchType());
		List<PrivConstructionSearchListDto.ResponseDt> listResDt = privConstructionSearchListDto.getResDt();
		for (Iterator<PvPrivconstUserRepository> it = listPtPricconstRepository.iterator(); it.hasNext();) {
			PvPrivconstUserRepository pvPrivconstUserRepository = it.next();

			PrivConstructionSearchListDto.ResponseDt resDt = new PrivConstructionSearchListDto.ResponseDt();
			resDt.setCompanyId(pvPrivconstUserRepository.getPrivConstId());
			resDt.setPrivConstId(pvPrivconstUserRepository.getPrivConstId());
			//resDt.setPrivConstCode(pvPrivconstUserRepository.getPrivConstCode());
			resDt.setPrivConstName(pvPrivconstUserRepository.getPrivConstName());
		    resDt.setUserName(pvPrivconstUserRepository.getSei() + " " + pvPrivconstUserRepository.getMei());
			listResDt.add(resDt);
		}
		makeResponseTitle(privConstructionSearchListDto);

		if (listResDt.size() > 0) {
			privConstructionSearchListDto.setResultCode("000");
		} else {
			privConstructionSearchListDto.setResultCode("001");
		}
		return;
	}

	// プライベート工事明細検索
	public void searchMany(PrivConstructionSearchManyDto privConstructionSearchManyDto) throws Exception {
		logger.debug(this.getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName());

		PrivConstructionSearchManyDto.RequestHd reqHd = privConstructionSearchManyDto.getReqHd();
		List<PvPrivconstUserRepository> listPtPricconstRepository = pvPrivconstUserMapper.selectMany(
				reqHd.getCompanyId(), reqHd.getPrivConstId(),
				reqHd.getUserId(), reqHd.getDeleted());

		// List<PtPrivconstRepository> listPtPricconstRepository =
		// ptPrivconstMapper.selectMany(0,0,(byte)0);

		List<PrivConstructionSearchManyDto.ResponseDt> listResDt = privConstructionSearchManyDto.getResDt();
		for (Iterator<PvPrivconstUserRepository> it = listPtPricconstRepository.iterator(); it.hasNext();) {
			PvPrivconstUserRepository pvPrivconstUserRepository = it.next();

			PrivConstructionSearchManyDto.ResponseDt resDt = new PrivConstructionSearchManyDto.ResponseDt();
			resDt.setCompanyId(pvPrivconstUserRepository.getCompanyId());
			resDt.setPrivConstId(pvPrivconstUserRepository.getPrivConstId());
			resDt.setUserId(pvPrivconstUserRepository.getUserId());
			resDt.setUserName(pvPrivconstUserRepository.getSei() + " " +pvPrivconstUserRepository.getMei());
			//resDt.setLoginUser(pvPrivconstUserRepository.getLoginUser());
			resDt.setConstId(pvPrivconstUserRepository.getConstId());
			resDt.setPrivConstCode(pvPrivconstUserRepository.getPrivConstCode());
			resDt.setPrivConstName(pvPrivconstUserRepository.getPrivConstName());
			resDt.setMaxReportNo(pvPrivconstUserRepository.getMaxReportNo());
			resDt.setMaxImageNo(pvPrivconstUserRepository.getMaxImageNo());
			resDt.setMaxOrderNo(pvPrivconstUserRepository.getMaxOrderNo());
			resDt.setMaxWorkNo(pvPrivconstUserRepository.getMaxWorkNo());
			resDt.setCreatedAt(pvPrivconstUserRepository.getCreatedAt());
			resDt.setUpdatedAt(pvPrivconstUserRepository.getUpdatedAt());
			if (pvPrivconstUserRepository.getDeleted() == (byte) 0) {
				resDt.setDeleted("FALSE");
			} else if (pvPrivconstUserRepository.getDeleted() == (byte) 1) {
				resDt.setDeleted("TRUE");
			}
			listResDt.add(resDt);
		}

		makeResponseTitle(privConstructionSearchManyDto);

		if (reqHd.getDeleted() == (byte) 2) {
			reqHd.setDeleted(null);
		}

		if (listResDt.size() > 0) {
			privConstructionSearchManyDto.setResultCode("000");
		} else {
			privConstructionSearchManyDto.setResultCode("001");
		}
		return;
	}

	// 工事月別日報一覧
	public void searchManyConstructionMonthReport(ConstructionMonthReportDto constructionMonthReportDto)
			throws Exception {
		logger.debug(this.getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName());

		ConstructionMonthReportDto.RequestHd reqHd = constructionMonthReportDto.getReqHd();
		List<PvConstructionMonthReportRepository> listPvConstructionMonthReportRepository = pvConstructionMonthReportMapper
				.selectMany(reqHd.getConstId(), reqHd.getStartDate(), reqHd.getEndDate(), reqHd.getDeleted());

		List<ConstructionMonthReportDto.ResponseDt> listResDt = constructionMonthReportDto.getResDt();
		for (Iterator<PvConstructionMonthReportRepository> it = listPvConstructionMonthReportRepository.iterator(); it
				.hasNext();) {
			PvConstructionMonthReportRepository pvConstructionMonthReportRepository = it.next();

			ConstructionMonthReportDto.ResponseDt resDt = new ConstructionMonthReportDto.ResponseDt();
			resDt.setPrivConstId(pvConstructionMonthReportRepository.getPrivConstId());
			resDt.setPrivConstName(pvConstructionMonthReportRepository.getPrivConstName());
			resDt.setUserId(pvConstructionMonthReportRepository.getUserId());
			resDt.setUserName(pvConstructionMonthReportRepository.getName());
			resDt.setReportNo(pvConstructionMonthReportRepository.getReportNo());
			resDt.setReportCode(pvConstructionMonthReportRepository.getReportCode());
			resDt.setPersonCode(pvConstructionMonthReportRepository.getPersonCode());
			resDt.setReportDate(pvConstructionMonthReportRepository.getReportDate());
			resDt.setDetail(pvConstructionMonthReportRepository.getDetail());
			resDt.setConstType(pvConstructionMonthReportRepository.getConstType());
			resDt.setStaff(pvConstructionMonthReportRepository.getStaff());
			resDt.setNumPerson(pvConstructionMonthReportRepository.getNumPerson());
			resDt.setHours(pvConstructionMonthReportRepository.getHours());
			resDt.setEarlyHours(pvConstructionMonthReportRepository.getEarlyHours());
			resDt.setOverHours(pvConstructionMonthReportRepository.getOverHours());
			resDt.setCreatedAt(pvConstructionMonthReportRepository.getCreatedAt());
			resDt.setUpdatedAt(pvConstructionMonthReportRepository.getUpdatedAt());
			if (pvConstructionMonthReportRepository.getDeleted() == (byte) 0) {
				resDt.setDeleted("FALSE");
			} else if (pvConstructionMonthReportRepository.getDeleted() == (byte) 1) {
				resDt.setDeleted("TRUE");
			}
			listResDt.add(resDt);
		}

		makeResponseTitle(constructionMonthReportDto);
		if (reqHd.getDeleted() == (byte) 2) {
			reqHd.setDeleted(null);
		}
		if (listResDt.size() > 0) {
			constructionMonthReportDto.setResultCode("000");
		} else {
			constructionMonthReportDto.setResultCode("001");
		}
		return;
	}

	// 工事月別作業一覧
	public void searchManyConstructionMonthOrder(ConstructionMonthOrderDto constructionMonthOrderDto) throws Exception {
		logger.debug(this.getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName());

		ConstructionMonthOrderDto.RequestHd reqHd = constructionMonthOrderDto.getReqHd();
		List<PvConstructionMonthOrderRepository> listPvConstructionMonthOrderRepository = pvConstructionMonthOrderMapper
				.selectMany(reqHd.getConstId(), reqHd.getStartDate(), reqHd.getEndDate(), reqHd.getDeleted());

		List<ConstructionMonthOrderDto.ResponseDt> listResDt = constructionMonthOrderDto.getResDt();
		for (Iterator<PvConstructionMonthOrderRepository> it = listPvConstructionMonthOrderRepository.iterator(); it
				.hasNext();) {
			PvConstructionMonthOrderRepository pvConstructionMonthOrderRepository = it.next();

			ConstructionMonthOrderDto.ResponseDt resDt = new ConstructionMonthOrderDto.ResponseDt();
			resDt.setPrivConstId(pvConstructionMonthOrderRepository.getPrivConstId());
			resDt.setPrivConstName(pvConstructionMonthOrderRepository.getPrivConstName());
			resDt.setUserId(pvConstructionMonthOrderRepository.getUserId());
			resDt.setUserName(pvConstructionMonthOrderRepository.getName());
			resDt.setOrderNo(pvConstructionMonthOrderRepository.getOrderNo());
			resDt.setOrderCode(pvConstructionMonthOrderRepository.getOrderCode());
			resDt.setOrderTitle(pvConstructionMonthOrderRepository.getOrderTitle());
			resDt.setOrderDate(pvConstructionMonthOrderRepository.getOrderDate());
			resDt.setOrderContents(pvConstructionMonthOrderRepository.getOrderContents());
			resDt.setOrderCause(pvConstructionMonthOrderRepository.getOrderCause());
			resDt.setStaff(pvConstructionMonthOrderRepository.getStaff());
			resDt.setStaffClient(pvConstructionMonthOrderRepository.getStaffClient());
			resDt.setCreatedAt(pvConstructionMonthOrderRepository.getCreatedAt());
			resDt.setUpdatedAt(pvConstructionMonthOrderRepository.getUpdatedAt());
			if (pvConstructionMonthOrderRepository.getDeleted() == (byte) 0) {
				resDt.setDeleted("FALSE");
			} else if (pvConstructionMonthOrderRepository.getDeleted() == (byte) 1) {
				resDt.setDeleted("TRUE");
			}
			if (pvConstructionMonthOrderRepository.getSignature() == (byte) 0) {
				resDt.setSignature("FALSE");
			} else if (pvConstructionMonthOrderRepository.getSignature() == (byte) 1) {
				resDt.setSignature("TRUE");
			}
			listResDt.add(resDt);
		}

		makeResponseTitle(constructionMonthOrderDto);
		if (reqHd.getDeleted() == (byte) 2) {
			reqHd.setDeleted(null);
		}
		if (listResDt.size() > 0) {
			constructionMonthOrderDto.setResultCode("000");
		} else {
			constructionMonthOrderDto.setResultCode("001");
		}
		return;
	}

}
