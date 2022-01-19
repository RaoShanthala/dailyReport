package co.jp.arche1.kdrs.usermaintenance.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.propertyeditors.CustomDateEditor;
//import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.jp.arche1.kdrs.usermaintenance.dto.UserDeleteDto;
import co.jp.arche1.kdrs.usermaintenance.dto.UserInsertDto;
import co.jp.arche1.kdrs.usermaintenance.dto.UserMonthOrderDto;
import co.jp.arche1.kdrs.usermaintenance.dto.UserMonthReportDto;
import co.jp.arche1.kdrs.usermaintenance.dto.UserSearchAllDto;
import co.jp.arche1.kdrs.usermaintenance.dto.UserSearchManyDto;
import co.jp.arche1.kdrs.usermaintenance.dto.UserSearchOneDto;
import co.jp.arche1.kdrs.usermaintenance.dto.UserUpdateDto;
import co.jp.arche1.kdrs.usermaintenance.service.UserService;

@RestController
public class UserMaintenanceController {
	final static Logger logger = LoggerFactory.getLogger(UserMaintenanceController.class);

	//RoleService roleService;

	@Autowired
	UserService userService;

	@Autowired
	MessageSource msg;

	// 役割検索
/*	@RequestMapping(value = "/UserMaintenance/ReferRoleAll", method = RequestMethod.GET)
	@PreAuthorize("hasRole('PM_ADMIN') or hasRole('PM_USER') or hasRole('PM_GUEST')")
	public RoleSearchAllDto referRoleAll() throws Exception {
		logger.debug(this.getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName());

		RoleSearchAllDto roleSearchAllDto = new RoleSearchAllDto();
		roleService.searchAll(roleSearchAllDto);

		// try {
		// svcActor.search(trnActorSearch);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }

		return roleSearchAllDto;
	} */

	// ユーザ明細検索
	@RequestMapping(value = "/UserMaintenance/ReferUserMany", method = RequestMethod.GET)
	//@PreAuthorize("hasRole('PM_ADMIN') or hasRole('PM_USER') or hasRole('PM_GUEST')")
	public UserSearchManyDto referUserMany(@RequestParam(name = "companyId", required = true) Integer companyId,
			@RequestParam(name = "sei", required = false) String sei,
			@RequestParam(name = "mei", required = false) String mei,
			@RequestParam(name = "status", required = false) Byte status,
			@RequestParam(name = "deleted", required = false) Byte deleted) throws Exception {
		logger.debug(this.getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName());

		UserSearchManyDto userSearchManyDto = new UserSearchManyDto();
		UserSearchManyDto.RequestHd regHd = userSearchManyDto.getReqHd();

		regHd.setCompanyId(companyId);
		regHd.setSei(sei);
		regHd.setMei(mei);
		regHd.setStatus(status);
		regHd.setDeleted(deleted);

		userService.searchMany(userSearchManyDto);

		return userSearchManyDto;
	}

	// ユーザ１件検索
	@RequestMapping(value = "/UserMaintenance/ReferUserOne", method = RequestMethod.GET)
	public UserSearchOneDto referUserOne(@RequestParam(name = "companyId", required = true) Integer companyId,
			@RequestParam(name = "userId", required = true) Integer userId)
			throws Exception {
		logger.debug(this.getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName());

		UserSearchOneDto userSearchOneDto = new UserSearchOneDto();
		UserSearchOneDto.RequestHd reqHd = userSearchOneDto.getReqHd();

		reqHd.setUserId(userId);
		reqHd.setCompanyId(companyId);

		userService.searchOne(userSearchOneDto);

		return userSearchOneDto;
	}

	// ユーザ登録
	@RequestMapping(value = "/UserMaintenance/RegistUer", method = RequestMethod.POST)
	//@PreAuthorize("hasRole('PM_ADMIN')")
	public UserInsertDto registUer(@RequestBody UserInsertDto userInsertDto) throws Exception {
		logger.debug(this.getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName());

		userService.insert(userInsertDto);

		return userInsertDto;
	}

	// ユーザ更新
	@RequestMapping(value = "/UserMaintenance/ModifyUer", method = RequestMethod.PUT)
	public UserUpdateDto modifyUer(@RequestBody UserUpdateDto userUpdateDto) throws Exception {
		logger.debug(this.getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName());

		userService.update(userUpdateDto);

		return userUpdateDto;
	}

	// ユーザ削除
	@RequestMapping(value = "/UserMaintenance/RemoveUer", method = RequestMethod.DELETE)
	public UserDeleteDto removetUer(@RequestBody UserDeleteDto userDeleteDto) throws Exception {
		logger.debug(this.getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName());

		userService.delete(userDeleteDto);

		return userDeleteDto;
	}

	// 社員一覧
	@RequestMapping(value = "/User/ReferUserAll", method = RequestMethod.GET)
	@PreAuthorize("hasRole('PM_ADMIN') or hasRole('PM_USER') or hasRole('PM_GUEST')")
	public UserSearchAllDto referUserAll(@RequestParam(name = "userId", required = false) Integer userId,
			@RequestParam(name = "loginUser", required = false) String loginUser,
			@RequestParam(name = "userName", required = false) String userName) throws Exception {

		UserSearchAllDto userSearchAllDto = new UserSearchAllDto();
		UserSearchAllDto.RequestHd reqHd = userSearchAllDto.getReqHd();
		reqHd.setUserId(userId);
		reqHd.setLoginUser(loginUser);
		reqHd.setUserName(userName);

		userService.searchAllUsers(userSearchAllDto);

		return userSearchAllDto;
	}

	// 社員月別日報一覧 UserMonthReport
	@RequestMapping(value = "/User/ReferUserMonthReportMany", method = RequestMethod.GET)
	@PreAuthorize("hasRole('PM_ADMIN') or hasRole('PM_USER') or hasRole('PM_GUEST')")
	public UserMonthReportDto referUserMonthReportMany(@RequestParam(name = "userId", required = true) Integer userId,
			@RequestParam(name = "userName", required = false) String userName,
			// @RequestParam(name = "startDate", required = true) @DateTimeFormat(iso =
			// DateTimeFormat.ISO.DATE) LocalDate startDate,
			// @RequestParam(name = "endDate", required = false) @DateTimeFormat(iso =
			// DateTimeFormat.ISO.DATE) LocalDate eDate,
			@RequestParam(name = "startDate", required = true) Integer startDate,
			@RequestParam(name = "endDate", required = false) Integer endDate,
			@RequestParam(name = "deleted", required = false) String deleted) throws Exception {
		logger.debug(this.getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName());

		UserMonthReportDto userMonthReportDto = new UserMonthReportDto();
		UserMonthReportDto.RequestHd regHd = userMonthReportDto.getReqHd();

		regHd.setUserId(userId);
		regHd.setUserName(userName);
		regHd.setStartDate(startDate);
		regHd.setEndDate(endDate);
		if (deleted != null) {
			if (deleted.equalsIgnoreCase("FALSE")) {
				regHd.setDeleted((byte) 0);
			} else if (deleted.equalsIgnoreCase("TRUE")) {
				regHd.setDeleted((byte) 1);
			}
		} else {
			regHd.setDeleted((byte) 2);
		}
		userService.searchManyUserMonthReport(userMonthReportDto);
		return userMonthReportDto;
	}

	// 社員月別作業一覧 UserMonthOrder
	@RequestMapping(value = "/User/ReferUserMonthOrderMany", method = RequestMethod.GET)
	@PreAuthorize("hasRole('PM_ADMIN') or hasRole('PM_USER') or hasRole('PM_GUEST')")
	public UserMonthOrderDto referUserMonthOrderMany(@RequestParam(name = "userId", required = true) Integer userId,
			@RequestParam(name = "userName", required = false) String userName,
			// @RequestParam(name = "startDate", required = true) @DateTimeFormat(iso =
			// DateTimeFormat.ISO.DATE) LocalDate startDate,
			// @RequestParam(name = "endDate", required = false) @DateTimeFormat(iso =
			// DateTimeFormat.ISO.DATE) LocalDate eDate,
			@RequestParam(name = "startDate", required = true) Integer startDate,
			@RequestParam(name = "endDate", required = false) Integer endDate,
			@RequestParam(name = "deleted", required = false) String deleted) throws Exception {
		logger.debug(this.getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName());

		UserMonthOrderDto userMonthOrderDto = new UserMonthOrderDto();
		UserMonthOrderDto.RequestHd regHd = userMonthOrderDto.getReqHd();

		regHd.setUserId(userId);
		regHd.setUserName(userName);
		regHd.setStartDate(startDate);
		regHd.setEndDate(endDate);
		if (deleted != null) {
			if (deleted.equalsIgnoreCase("FALSE")) {
				regHd.setDeleted((byte) 0);
			} else if (deleted.equalsIgnoreCase("TRUE")) {
				regHd.setDeleted((byte) 1);
			}
		} else {
			regHd.setDeleted((byte) 2);
		}
		userService.searchManyUserMonthOrder(userMonthOrderDto);
		return userMonthOrderDto;
	}

}
