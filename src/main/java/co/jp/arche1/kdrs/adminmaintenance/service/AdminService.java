package co.jp.arche1.kdrs.adminmaintenance.service;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import co.jp.arche1.kdrs.adminmaintenance.dto.AdminDeleteDto;
import co.jp.arche1.kdrs.adminmaintenance.dto.AdminInsertDto;
import co.jp.arche1.kdrs.adminmaintenance.dto.AdminSearchManyDto;
import co.jp.arche1.kdrs.common.BaseService;
import co.jp.arche1.kdrs.common.exception.OptimisticLockException;
import co.jp.arche1.kdrs.usermaintenance.mapper.PtCompanyUserAuthorityMapper;
import co.jp.arche1.kdrs.usermaintenance.mapper.PtCompanyUserMapper;
import co.jp.arche1.kdrs.usermaintenance.mapper.PtUserMapper;
import co.jp.arche1.kdrs.usermaintenance.mapper.PvUserCompanyUserMapper;
import co.jp.arche1.kdrs.usermaintenance.repository.PtUserRepository;
import co.jp.arche1.kdrs.usermaintenance.repository.PvUserCompanyUserRepository;

@Service
public class AdminService extends BaseService {

	final static Logger logger = LoggerFactory.getLogger(AdminService.class);

	@Autowired
	PvUserCompanyUserMapper pvUserCompanyUserMapper;

	@Autowired
	PtUserMapper ptUserMapper;

	@Autowired
	PtCompanyUserMapper ptCompanyUserMapper;

	@Autowired
	PtCompanyUserAuthorityMapper ptCompanyUserAuthorityMapper;

	// 管理者明細検索
	// @Transactional(readOnly = true)
	public void searchManyAdmin(AdminSearchManyDto adminSearchManyDto) throws Exception {
		logger.debug(this.getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName());

		AdminSearchManyDto.RequestHd reqHd = adminSearchManyDto.getReqHd();
		//get only active admins (not deleted)
		List<PvUserCompanyUserRepository> listPvUserCompanyUserRepository = pvUserCompanyUserMapper.selectManyUsers
				(reqHd.getCompanyId(), reqHd.getSei(), reqHd.getMei(),(byte)0, (byte)2 , (byte)0);

		List<AdminSearchManyDto.ResponseDt> listResDt = adminSearchManyDto.getResDt();

		for (Iterator<PvUserCompanyUserRepository> it = listPvUserCompanyUserRepository.iterator(); it.hasNext();) {
			PvUserCompanyUserRepository pvUserCompanyUserRepository = it.next();
			// listtrnActorSearchResDt.add(convertTranActorSearchResDt(rdbActor));

			AdminSearchManyDto.ResponseDt resDt = new AdminSearchManyDto.ResponseDt();
			resDt.setAuthorityName(pvUserCompanyUserRepository.getAuthorityName());

			resDt.setUserId(pvUserCompanyUserRepository.getUserId());
			resDt.setCompanyId(pvUserCompanyUserRepository.getCompanyId());
			if (StringUtils.isNotEmpty(pvUserCompanyUserRepository.getEmail())) {
				resDt.setEmail(pvUserCompanyUserRepository.getEmail());
			}
			resDt.setMei(pvUserCompanyUserRepository.getMei());
			resDt.setSei(pvUserCompanyUserRepository.getSei());
			resDt.setMeiKana(pvUserCompanyUserRepository.getMeiKana());
			resDt.setSeiKana(pvUserCompanyUserRepository.getSeiKana());
			// resDt.setDeleted(pvUserCompanyUserRepository.getDeleted());
			resDt.setCity(pvUserCompanyUserRepository.getCity());
			resDt.setPhone(pvUserCompanyUserRepository.getPhone());
			resDt.setPrefacture(pvUserCompanyUserRepository.getPrefacture());
			resDt.setStreetNumber(pvUserCompanyUserRepository.getStreetNumber());
			resDt.setBuildingName(pvUserCompanyUserRepository.getBuildingName());
			resDt.setCreatedAt(pvUserCompanyUserRepository.getCreatedAt());
			resDt.setUpdatedAt(pvUserCompanyUserRepository.getUpdatedAt());

			listResDt.add(resDt);
		}

		makeResponseTitle(adminSearchManyDto);

		if (listResDt.size() > 0) {
			adminSearchManyDto.setResultCode("000");
		} else {
			adminSearchManyDto.setResultCode("001");
		}
		return;
	}

	public void insert(AdminInsertDto adminInsertDto) throws Exception {
		logger.debug(this.getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName());

		int userId;

		AdminInsertDto.RequestHd reqHd = adminInsertDto.getReqHd();
		PtUserRepository ptUserRepository = new PtUserRepository();
		ptUserRepository.setSei(reqHd.getSei());
		ptUserRepository.setMei(reqHd.getMei());
		ptUserRepository.setSeiKana(reqHd.getSeiKana());
		ptUserRepository.setMeiKana(reqHd.getMeiKana());
		ptUserRepository.setPassword(reqHd.getPassword());
		ptUserRepository.setEmail(reqHd.getEmail());
		ptUserRepository.setPhone(reqHd.getPhone());
		ptUserRepository.setPrefacture(reqHd.getPrefacture());
		ptUserRepository.setCity(reqHd.getCity());
		ptUserRepository.setStreetNumber(reqHd.getStreetNumber());
		ptUserRepository.setBuildingName(reqHd.getBuildingName());
		ptUserRepository.setDeleted((byte) 0);

		try {
			// 管理者の登録
			ptUserMapper.insert(ptUserRepository);
			userId = ptUserRepository.getUserId();

		} catch (DuplicateKeyException ex) {
			adminInsertDto.setResultCode("002");
			adminInsertDto.setResultMessage("（操作：insert、テーブル名：pt_user、メールアドレス：" + reqHd.getEmail() + "）");
			return;
		}
		ptCompanyUserMapper.insert(userId, reqHd.getCompanyId());
		ptCompanyUserAuthorityMapper.insert(userId, reqHd.getCompanyId(), (byte) 0, (byte) reqHd.getAuthority());
		adminInsertDto.setResultCode("000");
		return;
	}

	// 管理者(論理削除)
	public void delete(AdminDeleteDto adminDeleteDto) throws Exception {
		logger.debug(this.getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName());

		AdminDeleteDto.RequestHd reqHd = adminDeleteDto.getReqHd();
		// 管理者の削除(
		int cnt = ptUserMapper.deleteUpdate(reqHd.getUserId(), (byte) 1);
		if (cnt == 0) {
			throw new OptimisticLockException("delete pt_user");
		}
		adminDeleteDto.setResultCode("000");
		return;
	}

}
