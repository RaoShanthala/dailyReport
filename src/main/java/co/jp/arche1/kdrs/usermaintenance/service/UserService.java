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
import co.jp.arche1.kdrs.usermaintenance.mapper.PtUserMapper;
import co.jp.arche1.kdrs.usermaintenance.mapper.PvUserMonthOrderMapper;
import co.jp.arche1.kdrs.usermaintenance.mapper.PvUserMonthReportMapper;
import co.jp.arche1.kdrs.usermaintenance.repository.PtUserRepository;
import co.jp.arche1.kdrs.usermaintenance.repository.PvUserMonthOrderRepository;
import co.jp.arche1.kdrs.usermaintenance.repository.PvUserMonthReportRepository;

@Service
public class UserService extends BaseService {

	final static Logger logger = LoggerFactory.getLogger(UserService.class);

	@Autowired
	PtUserMapper ptUserMapper;

	@Autowired
	PvUserMonthReportMapper pvUserMonthReportMapper;

	@Autowired
	PvUserMonthOrderMapper pvUserMonthOrderMapper;

	// ユーザ明細検索
	// @Transactional(readOnly = true)
	public void searchMany(UserSearchManyDto userSearchManyDto) throws Exception {
		logger.debug(this.getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName());

	/*	UserSearchManyDto.RequestHd reqHd = userSearchManyDto.getReqHd();
		List<PvUserRoleRepository> listPvUserRoleRepository = pvUserRoleMapper.selectManyUser(reqHd.getLoginUser(),
				reqHd.getName(), reqHd.getRoleId(), reqHd.getRoleLevel(), reqHd.getTargetDate());

		List<UserSearchManyDto.ResponseDt> listResDt = userSearchManyDto.getResDt();
		for (Iterator<PvUserRoleRepository> it = listPvUserRoleRepository.iterator(); it.hasNext();) {
			PvUserRoleRepository pvUserRoleRepository = it.next();
			// listtrnActorSearchResDt.add(convertTranActorSearchResDt(rdbActor));

			UserSearchManyDto.ResponseDt resDt = new UserSearchManyDto.ResponseDt();
			resDt.setUserId(pvUserRoleRepository.getUserId());
			resDt.setStartDate(pvUserRoleRepository.getStartDate());
			resDt.setEndDate(pvUserRoleRepository.getEndDate());
			resDt.setLoginUser(pvUserRoleRepository.getLoginUser());
			resDt.setName(pvUserRoleRepository.getName());
			if (StringUtils.isNotEmpty(pvUserRoleRepository.getEmail())) {
				resDt.setEmail(pvUserRoleRepository.getEmail());
			}

			if (pvUserRoleRepository.getRoleId() != null) {
				// pt_user_role の role_id が存在すれば次の値も存在する。
				resDt.setRoleId(pvUserRoleRepository.getRoleId());
				resDt.setRoleName(pvUserRoleRepository.getRoleName());
				resDt.setRoleLevel(pvUserRoleRepository.getRoleLevel());
				resDt.setRoleLevelNameShort(pvUserRoleRepository.getRoleLevelNameShort());
				resDt.setUserRoleStartDate(pvUserRoleRepository.getUserRoleStartDate());
				resDt.setUserRoleEndDate(pvUserRoleRepository.getUserRoleEndDate());
			}

			listResDt.add(resDt);
		}

		makeResponseTitle(userSearchManyDto);

		if (listResDt.size() > 0) {
			userSearchManyDto.setResultCode("000");
		} else {
			userSearchManyDto.setResultCode("001");
		} */
		return;
	}

	// ユーザ1件検索
	// @Transactional(readOnly = true)
	public void searchOne(UserSearchOneDto userSearchOneDto) throws Exception {
		logger.debug(this.getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName());

	/*	UserSearchOneDto.RequestHd reqHd = userSearchOneDto.getReqHd();

		List<PvUserRoleRepository> listPvUserRoleRepository = pvUserRoleMapper.selectOneUser(reqHd.getUserId(),
				(String) null);
		List<UserSearchOneDto.ResponseDt> listResDt = userSearchOneDto.getResDt();

		for (Iterator<PvUserRoleRepository> it = listPvUserRoleRepository.iterator(); it.hasNext();) {
			PvUserRoleRepository pvUserRoleRepository = it.next();

			if (listResDt.isEmpty()) {
				// 1件目のみ処理する
				UserSearchOneDto.ResponseHd resHd = userSearchOneDto.getResHd();
				resHd.setStartDate(pvUserRoleRepository.getStartDate());
				resHd.setEndDate(pvUserRoleRepository.getEndDate());
				resHd.setLoginUser(pvUserRoleRepository.getLoginUser());
				resHd.setName(pvUserRoleRepository.getName());
				resHd.setPassword(pvUserRoleRepository.getPassword());
				if (StringUtils.isNotEmpty(pvUserRoleRepository.getEmail())) {
					resHd.setEmail(pvUserRoleRepository.getEmail());
				}
				resHd.setUpdDatetime(pvUserRoleRepository.getUpdDatetime());
			}
			// 全件処理する
			UserSearchOneDto.ResponseDt resDt = new UserSearchOneDto.ResponseDt();
			if (pvUserRoleRepository.getRoleId() != null) {
				// pt_user_role の role_id が存在すれば次の値も存在する。
				resDt.setRoleId(pvUserRoleRepository.getRoleId());
				resDt.setRoleName(pvUserRoleRepository.getRoleName());
				resDt.setUpdDatetime(pvUserRoleRepository.getUserRoleUpdDatetime());
				resDt.setRoleLevel(pvUserRoleRepository.getRoleLevel());
				resDt.setRoleLevelNameShort(pvUserRoleRepository.getRoleLevelNameShort());
				resDt.setUserRoleStartDate(pvUserRoleRepository.getUserRoleStartDate());
				resDt.setUserRoleEndDate(pvUserRoleRepository.getUserRoleEndDate());
				listResDt.add(resDt);
			}
		}

		userSearchOneDto.setResultCode("000"); */

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
	/*	ptUserRepository.setStartDate(reqHd.getStartDate());
		ptUserRepository.setEndDate(reqHd.getEndDate());
		ptUserRepository.setLoginUser(reqHd.getLoginUser());
		ptUserRepository.setName(reqHd.getName());
		ptUserRepository.setStartDate(reqHd.getStartDate()); */
		ptUserRepository.setPassword(reqHd.getPassword());
		if (reqHd.getEmail() != null) {
			ptUserRepository.setEmail(reqHd.getEmail());
		}
		try {
			// ユーザの登録
			ptUserMapper.insert(ptUserRepository);
		} catch (DuplicateKeyException ex) {
			userInsertDto.setResultCode("002");
			userInsertDto.setResultMessage("（操作：insert、テーブル名：pt_user、ログインユーザ：" + reqHd.getLoginUser() + "）");
			return;
		}

		// 登録したユーザの検索
		ptUserRepository = ptUserMapper.selectOne(reqHd.getLoginUser());

	/*	List<UserInsertDto.RequestDt> listReqDt = userInsertDto.getReqDt();
		PtUserRoleRepository ptUserRoleRepository = new PtUserRoleRepository();
		for (Iterator<UserInsertDto.RequestDt> it = listReqDt.iterator(); it.hasNext();) {
			UserInsertDto.RequestDt reqDt = it.next();

			ptUserRoleRepository.setUserId(ptUserRepository.getUserId());

			ptUserRoleRepository.setRoleId(reqDt.getRoleId());
			ptUserRoleRepository.setRoleLevel(reqDt.getRoleLevel());

			ptUserRoleRepository.setStartDate(reqDt.getUserRoleStartDate());
			ptUserRoleRepository.setEndDate(reqDt.getUserRoleEndDate());
			// ユーザ／業務の登録
			ptUserRoleMapper.insert(ptUserRoleRepository);
		}*/

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
	/*	ptUserRepository.setStartDate(reqHd.getStartDate());
		ptUserRepository.setEndDate(reqHd.getEndDate());
		ptUserRepository.setLoginUser(reqHd.getLoginUser());
		ptUserRepository.setName(reqHd.getName());
		ptUserRepository.setStartDate(reqHd.getStartDate()); */
		ptUserRepository.setPassword(reqHd.getPassword());
		if (reqHd.getEmail() != null) {
			ptUserRepository.setEmail(reqHd.getEmail());
		}

		// ユーザの更新
		int cnt = ptUserMapper.update(ptUserRepository, reqHd.getUpdDatetime());
		if (cnt == 0) {
			throw new OptimisticLockException("（操作：update、テーブル名：pt_user）");
		}

	/*	List<UserUpdateDto.RequestDt> listReqDt = userUpdateDto.getReqDt();
		PtUserRoleRepository ptUserRoleRepository = new PtUserRoleRepository();
		for (Iterator<UserUpdateDto.RequestDt> it = listReqDt.iterator(); it.hasNext();) {
			UserUpdateDto.RequestDt reqDt = it.next();

			ptUserRoleRepository.setUserId(ptUserRepository.getUserId());

			ptUserRoleRepository.setRoleId(reqDt.getRoleId());
			ptUserRoleRepository.setRoleLevel(reqDt.getRoleLevel());

			ptUserRoleRepository.setStartDate(reqDt.getUserRoleStartDate());
			ptUserRoleRepository.setEndDate(reqDt.getUserRoleEndDate());

			switch (reqDt.getAction().byteValue()) {
			case 1: // ユーザ／業務の登録
				cnt = ptUserRoleMapper.insert(ptUserRoleRepository);
				break;
			case 2: // ユーザ／業務の更新
					cnt = ptUserRoleMapper.update(ptUserRoleRepository, reqDt.getUpdDatetime());
				if (cnt == 0) {
					throw new OptimisticLockException("（操作：update、テーブル名：pt_user）");
				}
				break;
			case 3: // ユーザ／業務の削除
				cnt = ptUserRoleMapper.delete(reqHd.getUserId(), reqDt.getRoleId(), reqDt.getUpdDatetime());
				if (cnt == 0) {
					throw new OptimisticLockException("（操作：delete、テーブル名：pt_user）");
				}
				break;
			}
		} */
		userUpdateDto.setResultCode("000");
		return;
	}

	// ユーザ、ユーザ／業務の削除
	// @Transactional(readOnly = false, rollbackFor = Exception.class, propagation =
	// Propagation.REQUIRES_NEW)
	// @Transactional(readOnly = false, rollbackFor = Exception.class)
	public void delete(UserDeleteDto userDeleteDto) throws Exception {
		logger.debug(this.getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName());

		UserDeleteDto.RequestHd reqHd = userDeleteDto.getReqHd();
		// ユーザの削除
		int cnt = ptUserMapper.delete(reqHd.getUserId(), reqHd.getUpdDatetime());
		if (cnt == 0) {
			throw new OptimisticLockException("delete pt_user");
		}

		List<UserDeleteDto.RequestDt> listReqDt = userDeleteDto.getReqDt();
		for (Iterator<UserDeleteDto.RequestDt> it = listReqDt.iterator(); it.hasNext();) {
			UserDeleteDto.RequestDt reqDt = it.next();
			// ユーザ／業務の削除
		/*R	cnt = ptUserRoleMapper.delete(userDeleteDto.getReqHd().getUserId(), reqDt.getRoleId(),
					reqDt.getUpdDatetime()); */
			if (cnt == 0) {
				throw new OptimisticLockException("delete pt_user_role");
			}
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
	/*		resDt.setStartDate(ptUserRepository.getStartDate());
			resDt.setEndDate(ptUserRepository.getEndDate());
			resDt.setLoginUser(ptUserRepository.getLoginUser());
			resDt.setUserName(ptUserRepository.getName()); */

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

}