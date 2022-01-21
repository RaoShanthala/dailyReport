package co.jp.arche1.kdrs.usermaintenance.service;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import co.jp.arche1.kdrs.common.BaseService;
import co.jp.arche1.kdrs.common.exception.OptimisticLockException;
import co.jp.arche1.kdrs.usermaintenance.dto.UserDeleteDto;
import co.jp.arche1.kdrs.usermaintenance.dto.UserInsertDto;
import co.jp.arche1.kdrs.usermaintenance.dto.UserMonthOrderDto;
import co.jp.arche1.kdrs.usermaintenance.dto.UserMonthReportDto;
import co.jp.arche1.kdrs.usermaintenance.dto.UserSearchAllDto;
import co.jp.arche1.kdrs.usermaintenance.dto.UserSearchManyDto;
import co.jp.arche1.kdrs.usermaintenance.dto.UserSearchOneDto;
import co.jp.arche1.kdrs.usermaintenance.dto.UserUpdateDto;
import co.jp.arche1.kdrs.usermaintenance.mapper.PtCompanyUserAuthorityMapper;
import co.jp.arche1.kdrs.usermaintenance.mapper.PtCompanyUserMapper;
import co.jp.arche1.kdrs.usermaintenance.mapper.PtUserMapper;
import co.jp.arche1.kdrs.usermaintenance.mapper.PvUserCompanyUserMapper;
import co.jp.arche1.kdrs.usermaintenance.mapper.PvUserMonthOrderMapper;
import co.jp.arche1.kdrs.usermaintenance.mapper.PvUserMonthReportMapper;
import co.jp.arche1.kdrs.usermaintenance.repository.PtUserRepository;
import co.jp.arche1.kdrs.usermaintenance.repository.PvUserCompanyUserRepository;
import co.jp.arche1.kdrs.usermaintenance.repository.PvUserMonthOrderRepository;
import co.jp.arche1.kdrs.usermaintenance.repository.PvUserMonthReportRepository;

@Service
public class UserService extends BaseService {

	final static Logger logger = LoggerFactory.getLogger(UserService.class);

	@Autowired
	PtUserMapper ptUserMapper;

	@Autowired
	PtCompanyUserMapper ptCompanyUserMapper;

	@Autowired
	PtCompanyUserAuthorityMapper ptCompanyUserAuthorityMapper;

	@Autowired
	PvUserMonthReportMapper pvUserMonthReportMapper;

	@Autowired
	PvUserMonthOrderMapper pvUserMonthOrderMapper;

	@Autowired
	PvUserCompanyUserMapper pvUserCompanyUserMapper;

	// ユーザ明細検索
	// @Transactional(readOnly = true)
	public void searchMany(UserSearchManyDto userSearchManyDto) throws Exception {
		logger.debug(this.getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName());

		UserSearchManyDto.RequestHd reqHd = userSearchManyDto.getReqHd();
		List<PvUserCompanyUserRepository> listPvUserCompanyUserRepository = pvUserCompanyUserMapper.selectManyUsers(
				reqHd.getCompanyId(), reqHd.getSei(), reqHd.getMei(), reqHd.getStatus(), reqHd.getDeleted());

		List<UserSearchManyDto.ResponseDt> listResDt = userSearchManyDto.getResDt();
        int prevUserId = 0;
		for (Iterator<PvUserCompanyUserRepository> it = listPvUserCompanyUserRepository.iterator(); it.hasNext();) {
			PvUserCompanyUserRepository pvUserCompanyUserRepository = it.next();
			// listtrnActorSearchResDt.add(convertTranActorSearchResDt(rdbActor));

			UserSearchManyDto.ResponseDt resDt = new UserSearchManyDto.ResponseDt();
			if (prevUserId != 0 && prevUserId == pvUserCompanyUserRepository.getUserId()){
				int indexOfLastElement = listResDt.size() - 1;
				listResDt.remove(indexOfLastElement);
				resDt.setAuthorityName("ユーザ、管理者");
			}else {
				resDt.setAuthorityName(pvUserCompanyUserRepository.getAuthorityName());
			}
			prevUserId = pvUserCompanyUserRepository.getUserId();
			resDt.setUserId(pvUserCompanyUserRepository.getUserId());
			resDt.setCompanyId(pvUserCompanyUserRepository.getCompanyId());
			resDt.setStatus(getStatus(pvUserCompanyUserRepository.getStatus()));
			if (StringUtils.isNotEmpty(pvUserCompanyUserRepository.getEmail())) {
				resDt.setEmail(pvUserCompanyUserRepository.getEmail());
			}
			resDt.setMei(pvUserCompanyUserRepository.getMei());
			resDt.setSei(pvUserCompanyUserRepository.getSei());
			resDt.setMeiKana(pvUserCompanyUserRepository.getMeiKana());
			resDt.setSeiKana(pvUserCompanyUserRepository.getMeiKana());
			resDt.setDeleted(pvUserCompanyUserRepository.getDeleted());
			resDt.setCity(pvUserCompanyUserRepository.getCity());
			resDt.setPhone(pvUserCompanyUserRepository.getPhone());
			resDt.setPrefacture(pvUserCompanyUserRepository.getPrefacture());
			resDt.setStreetNumber(pvUserCompanyUserRepository.getStreetNumber());
			resDt.setBuildingName(pvUserCompanyUserRepository.getBuildingName());
			resDt.setCreatedAt(pvUserCompanyUserRepository.getCreatedAt());
			resDt.setUpdatedAt(pvUserCompanyUserRepository.getUpdatedAt());

			listResDt.add(resDt);
		}

		makeResponseTitle(userSearchManyDto);

		if (listResDt.size() > 0) {
			userSearchManyDto.setResultCode("000");
		} else {
			userSearchManyDto.setResultCode("001");
		}
		return;
	}

	// ユーザ1件検索
	// @Transactional(readOnly = true)
	public void searchOne(UserSearchOneDto userSearchOneDto) throws Exception {
		logger.debug(this.getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName());

		UserSearchOneDto.RequestHd reqHd = userSearchOneDto.getReqHd();
		List<PvUserCompanyUserRepository> listPvUserCompanyUserRepository = pvUserCompanyUserMapper
				.selectOneUser(reqHd.getCompanyId(), reqHd.getUserId());
		UserSearchOneDto.ResponseHd resDt = userSearchOneDto.getResHd();
		PvUserCompanyUserRepository pvUserCompanyUserRepository = listPvUserCompanyUserRepository.iterator().next();
		if (listPvUserCompanyUserRepository.size() > 1) {
			resDt.setAuthorityType((byte) 4); // both user and admin
		} else {
			resDt.setAuthorityType(pvUserCompanyUserRepository.getAuthorityType());
		}

		resDt.setUserId(pvUserCompanyUserRepository.getUserId());
		resDt.setCompanyId(pvUserCompanyUserRepository.getCompanyId());
		resDt.setStatus(pvUserCompanyUserRepository.getStatus());
		if (StringUtils.isNotEmpty(pvUserCompanyUserRepository.getEmail())) {
			resDt.setEmail(pvUserCompanyUserRepository.getEmail());
		}
		resDt.setMei(pvUserCompanyUserRepository.getMei());
		resDt.setSei(pvUserCompanyUserRepository.getSei());
		resDt.setMeiKana(pvUserCompanyUserRepository.getMeiKana());
		resDt.setSeiKana(pvUserCompanyUserRepository.getMeiKana());
		resDt.setPassword(pvUserCompanyUserRepository.getPassword());
		// resDt.setDeleted(pvUserCompanyUserRepository.getDeleted());
		resDt.setCity(pvUserCompanyUserRepository.getCity());
		resDt.setPhone(pvUserCompanyUserRepository.getPhone());
		resDt.setPrefacture(pvUserCompanyUserRepository.getPrefacture());
		resDt.setStreetNumber(pvUserCompanyUserRepository.getStreetNumber());
		resDt.setBuildingName(pvUserCompanyUserRepository.getBuildingName());

		userSearchOneDto.setResultCode("000");

		return;
	}

	// ユーザ、ユーザ／業務の登録
	// @Transactional(readOnly = false, rollbackFor = Exception.class, propagation =
	// Propagation.REQUIRES_NEW)
	// @Transactional(readOnly = false, rollbackFor = Exception.class)
	public void insert(UserInsertDto userInsertDto) throws Exception {
		logger.debug(this.getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName());

		UserInsertDto.RequestHd reqHd = userInsertDto.getReqHd();
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
			// ユーザの登録
			ptUserMapper.insert(ptUserRepository);

		} catch (DuplicateKeyException ex) {
			userInsertDto.setResultCode("002");
			userInsertDto.setResultMessage("（操作：insert、テーブル名：pt_user、メールアドレス：" + reqHd.getEmail() + "）");
			return;
		}
		// 登録したユーザの検索
		ptUserRepository = ptUserMapper.selectOne(reqHd.getEmail());
		if (reqHd.getAuthority() == 2 || reqHd.getAuthority() == 3) { // if admin or user
			ptCompanyUserMapper.insert(ptUserRepository.getUserId(), reqHd.getCompanyId());
			if (reqHd.getAuthority() == 3) { // in case of user
				ptCompanyUserAuthorityMapper.insert(ptUserRepository.getUserId(), reqHd.getCompanyId(),
						(byte)1, (byte)reqHd.getAuthority());
			} else { // in case of admin
				ptCompanyUserAuthorityMapper.insert(ptUserRepository.getUserId(), reqHd.getCompanyId(),
						(byte)0, (byte)reqHd.getAuthority());
     		}
		} else { // if both admin and user
			ptCompanyUserMapper.insert(ptUserRepository.getUserId(), reqHd.getCompanyId());
			//for admin
			ptCompanyUserAuthorityMapper.insert(ptUserRepository.getUserId(), reqHd.getCompanyId(),(byte)0, (byte) 2);
			// for user
			ptCompanyUserAuthorityMapper.insert(ptUserRepository.getUserId(), reqHd.getCompanyId(), (byte)1, (byte) 3);
		}
		userInsertDto.setResultCode("000");
		return;
	}

	// ユーザの更新、ユーザ／業務の登録、更新、削除
	// @Transactional(readOnly = false, rollbackFor = Exception.class)
	public void update(UserUpdateDto userUpdateDto) throws Exception {
		logger.debug(this.getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName());

		UserUpdateDto.RequestHd reqHd = userUpdateDto.getReqHd();
		PtUserRepository ptUserRepository = new PtUserRepository();

		ptUserRepository.setUserId(reqHd.getUserId());

		ptUserRepository.setSei(reqHd.getSei());
		ptUserRepository.setMei(reqHd.getMei());
		ptUserRepository.setSeiKana(reqHd.getSeiKana());
		ptUserRepository.setMeiKana(reqHd.getMeiKana());
		if (! reqHd.getPassword().trim().isEmpty()) {
			ptUserRepository.setPassword(reqHd.getPassword());
		}
		ptUserRepository.setEmail(reqHd.getEmail());
		ptUserRepository.setPhone(reqHd.getPhone());
		ptUserRepository.setPrefacture(reqHd.getPrefacture());
		ptUserRepository.setCity(reqHd.getCity());
		ptUserRepository.setStreetNumber(reqHd.getStreetNumber());
		ptUserRepository.setBuildingName(reqHd.getBuildingName());

		// ユーザの更新
		int cnt = ptUserMapper.update(ptUserRepository);
		if (cnt == 0) {
			throw new OptimisticLockException("（操作：update、テーブル名：pt_user）");
		}

		//First delete the authorities for the given userId and companyId
		ptCompanyUserAuthorityMapper.delete(reqHd.getUserId(), reqHd.getCompanyId());

		//then insert the authorities received from client
		if (reqHd.getAuthority() == 2 || reqHd.getAuthority() == 3) { // if admin or user
			if (reqHd.getAuthority() == 3) { // in case of user
				ptCompanyUserAuthorityMapper.insert(reqHd.getUserId(), reqHd.getCompanyId(),
						(byte)1, (byte)reqHd.getAuthority());
			} else { // in case of admin
				ptCompanyUserAuthorityMapper.insert(reqHd.getUserId(), reqHd.getCompanyId(),
						(byte)0, (byte)reqHd.getAuthority());
     		}
		} else { // if both admin and user
			//for admin
			ptCompanyUserAuthorityMapper.insert(reqHd.getUserId(), reqHd.getCompanyId(),(byte)0, (byte) 2);
			// for user
			ptCompanyUserAuthorityMapper.insert(reqHd.getUserId(), reqHd.getCompanyId(), (byte)1, (byte) 3);
		}


		/*
		 * List<UserUpdateDto.RequestDt> listReqDt = userUpdateDto.getReqDt();
		 * PtUserRoleRepository ptUserRoleRepository = new PtUserRoleRepository(); for
		 * (Iterator<UserUpdateDto.RequestDt> it = listReqDt.iterator(); it.hasNext();)
		 * { UserUpdateDto.RequestDt reqDt = it.next();
		 *
		 * ptUserRoleRepository.setUserId(ptUserRepository.getUserId());
		 *
		 * ptUserRoleRepository.setRoleId(reqDt.getRoleId());
		 * ptUserRoleRepository.setRoleLevel(reqDt.getRoleLevel());
		 *
		 * ptUserRoleRepository.setStartDate(reqDt.getUserRoleStartDate());
		 * ptUserRoleRepository.setEndDate(reqDt.getUserRoleEndDate());
		 *
		 * switch (reqDt.getAction().byteValue()) { case 1: // ユーザ／業務の登録 cnt =
		 * ptUserRoleMapper.insert(ptUserRoleRepository); break; case 2: // ユーザ／業務の更新
		 * cnt = ptUserRoleMapper.update(ptUserRoleRepository, reqDt.getUpdDatetime());
		 * if (cnt == 0) { throw new
		 * OptimisticLockException("（操作：update、テーブル名：pt_user）"); } break; case 3: //
		 * ユーザ／業務の削除 cnt = ptUserRoleMapper.delete(reqHd.getUserId(), reqDt.getRoleId(),
		 * reqDt.getUpdDatetime()); if (cnt == 0) { throw new
		 * OptimisticLockException("（操作：delete、テーブル名：pt_user）"); } break; } }
		 */
		userUpdateDto.setResultCode("000");
		return;
	}

	// ユーザ (論理削除)
	public void delete(UserDeleteDto userDeleteDto) throws Exception {
		logger.debug(this.getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName());

		UserDeleteDto.RequestHd reqHd = userDeleteDto.getReqHd();
		// ユーザの削除(
		int cnt = ptUserMapper.deleteUpdate(reqHd.getUserId(), (byte) 1);
		if (cnt == 0) {
			throw new OptimisticLockException("delete pt_user");
		}
		userDeleteDto.setResultCode("000");
		return;
	}

	public void searchAllUsers(UserSearchAllDto userSearchAllDto) throws Exception {
		logger.debug(this.getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName());

		UserSearchAllDto.RequestHd reqHd = userSearchAllDto.getReqHd();
		List<PtUserRepository> listPtUserRepository = ptUserMapper.selecAll(reqHd.getUserId(), reqHd.getLoginUser(),
				reqHd.getUserName());

		List<UserSearchAllDto.ResponseDt> listResDt = userSearchAllDto.getResDt();
		for (Iterator<PtUserRepository> it = listPtUserRepository.iterator(); it.hasNext();) {
			PtUserRepository ptUserRepository = it.next();

			UserSearchAllDto.ResponseDt resDt = new UserSearchAllDto.ResponseDt();
			resDt.setUserId(ptUserRepository.getUserId());
			resDt.setEmail(ptUserRepository.getEmail());
			/*
			 * resDt.setStartDate(ptUserRepository.getStartDate());
			 * resDt.setEndDate(ptUserRepository.getEndDate());
			 * resDt.setLoginUser(ptUserRepository.getLoginUser());
			 * resDt.setUserName(ptUserRepository.getName());
			 */

			listResDt.add(resDt);
		}
		makeResponseTitle(userSearchAllDto);
		if (listResDt.size() > 0) {
			userSearchAllDto.setResultCode("000");
		} else {
			userSearchAllDto.setResultCode("001");
		}
		return;
	}

	// 社員月別日報一覧
	public void searchManyUserMonthReport(UserMonthReportDto userMonthReportDto) throws Exception {
		logger.debug(this.getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName());

		UserMonthReportDto.RequestHd reqHd = userMonthReportDto.getReqHd();
		List<PvUserMonthReportRepository> listPvUserMonthReportRepository = pvUserMonthReportMapper.selectMany(
				reqHd.getUserId(), reqHd.getUserName(), reqHd.getStartDate(), reqHd.getEndDate(), reqHd.getDeleted());

		List<UserMonthReportDto.ResponseDt> listResDt = userMonthReportDto.getResDt();
		for (Iterator<PvUserMonthReportRepository> it = listPvUserMonthReportRepository.iterator(); it.hasNext();) {
			PvUserMonthReportRepository pvUserMonthReportRepository = it.next();

			UserMonthReportDto.ResponseDt resDt = new UserMonthReportDto.ResponseDt();
			resDt.setConstId(pvUserMonthReportRepository.getConstId());
			resDt.setConstName(pvUserMonthReportRepository.getConstName());
			resDt.setPrivConstId(pvUserMonthReportRepository.getPrivConstId());
			resDt.setPrivConstName(pvUserMonthReportRepository.getPrivConstName());
			resDt.setUserId(pvUserMonthReportRepository.getUserId());
			resDt.setUserName(pvUserMonthReportRepository.getName());
			resDt.setReportNo(pvUserMonthReportRepository.getReportNo());
			resDt.setReportCode(pvUserMonthReportRepository.getReportCode());
			resDt.setPersonCode(pvUserMonthReportRepository.getPersonCode());
			resDt.setReportDate(pvUserMonthReportRepository.getReportDate());
			resDt.setDetail(pvUserMonthReportRepository.getDetail());
			resDt.setConstType(pvUserMonthReportRepository.getConstType());
			resDt.setStaff(pvUserMonthReportRepository.getStaff());
			resDt.setNumPerson(pvUserMonthReportRepository.getNumPerson());
			resDt.setHours(pvUserMonthReportRepository.getHours());
			resDt.setEarlyHours(pvUserMonthReportRepository.getEarlyHours());
			resDt.setOverHours(pvUserMonthReportRepository.getOverHours());
			resDt.setCreatedAt(pvUserMonthReportRepository.getCreatedAt());
			resDt.setUpdatedAt(pvUserMonthReportRepository.getUpdatedAt());
			if (pvUserMonthReportRepository.getDeleted() == (byte) 0) {
				resDt.setDeleted("FALSE");
			} else if (pvUserMonthReportRepository.getDeleted() == (byte) 1) {
				resDt.setDeleted("TRUE");
			}
			listResDt.add(resDt);
		}

		makeResponseTitle(userMonthReportDto);
		if (reqHd.getDeleted() == (byte) 2) {
			reqHd.setDeleted(null);
		}
		if (listResDt.size() > 0) {
			userMonthReportDto.setResultCode("000");
		} else {
			userMonthReportDto.setResultCode("001");
		}
		return;
	}

	// 社員月別作業一覧
	public void searchManyUserMonthOrder(UserMonthOrderDto userMonthOrderDto) throws Exception {
		logger.debug(this.getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName());

		UserMonthOrderDto.RequestHd reqHd = userMonthOrderDto.getReqHd();
		List<PvUserMonthOrderRepository> listPvUserMonthOrderRepository = pvUserMonthOrderMapper.selectMany(
				reqHd.getUserId(), reqHd.getUserName(), reqHd.getStartDate(), reqHd.getEndDate(), reqHd.getDeleted());

		List<UserMonthOrderDto.ResponseDt> listResDt = userMonthOrderDto.getResDt();
		for (Iterator<PvUserMonthOrderRepository> it = listPvUserMonthOrderRepository.iterator(); it.hasNext();) {
			PvUserMonthOrderRepository pvUserMonthOrderRepository = it.next();

			UserMonthOrderDto.ResponseDt resDt = new UserMonthOrderDto.ResponseDt();
			resDt.setConstId(pvUserMonthOrderRepository.getConstId());
			resDt.setConstName(pvUserMonthOrderRepository.getConstName());
			resDt.setPrivConstId(pvUserMonthOrderRepository.getPrivConstId());
			resDt.setPrivConstName(pvUserMonthOrderRepository.getPrivConstName());
			resDt.setUserId(pvUserMonthOrderRepository.getUserId());
			resDt.setUserName(pvUserMonthOrderRepository.getName());
			resDt.setOrderNo(pvUserMonthOrderRepository.getOrderNo());
			resDt.setOrderCode(pvUserMonthOrderRepository.getOrderCode());
			resDt.setOrderTitle(pvUserMonthOrderRepository.getOrderTitle());
			resDt.setOrderDate(pvUserMonthOrderRepository.getOrderDate());
			resDt.setOrderContents(pvUserMonthOrderRepository.getOrderContents());
			resDt.setOrderCause(pvUserMonthOrderRepository.getOrderCause());
			resDt.setStaff(pvUserMonthOrderRepository.getStaff());
			resDt.setStaffClient(pvUserMonthOrderRepository.getStaffClient());
			resDt.setCreatedAt(pvUserMonthOrderRepository.getCreatedAt());
			resDt.setUpdatedAt(pvUserMonthOrderRepository.getUpdatedAt());
			if (pvUserMonthOrderRepository.getDeleted() == (byte) 0) {
				resDt.setDeleted("FALSE");
			} else if (pvUserMonthOrderRepository.getDeleted() == (byte) 1) {
				resDt.setDeleted("TRUE");
			}
			listResDt.add(resDt);
		}

		makeResponseTitle(userMonthOrderDto);
		if (reqHd.getDeleted() == (byte) 2) {
			reqHd.setDeleted(null);
		}
		if (listResDt.size() > 0) {
			userMonthOrderDto.setResultCode("000");
		} else {
			userMonthOrderDto.setResultCode("001");
		}
		return;
	}

	private String getStatus(Byte status) {
		String statusString = "";
		// 1：申請（iphone）、2：承認（管理者）、3：承諾（iphone）、7：承認拒否（管理者）、8：承諾拒否（iphone）、9：削除（管理者）'
		if (status == (byte)1) {
			statusString = "申請（iphone）";
		}else if (status == (byte)2) {
			statusString = "承認（管理者）";
		}else if (status == (byte)3) {
			statusString = "承諾（iphone）";
		}else if (status == (byte)7) {
			statusString = "承認拒否（管理者）";
		}else if (status == (byte)8) {
			statusString = "承諾拒否（iphone）";
		}else if (status == (byte)9) {
			statusString = "削除（管理者）";
		}

		return statusString;
	}

}