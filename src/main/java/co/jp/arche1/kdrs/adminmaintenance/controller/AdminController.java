package co.jp.arche1.kdrs.adminmaintenance.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.jp.arche1.kdrs.adminmaintenance.dto.AdminDeleteDto;
import co.jp.arche1.kdrs.adminmaintenance.dto.AdminInsertDto;
import co.jp.arche1.kdrs.adminmaintenance.dto.AdminSearchManyDto;
import co.jp.arche1.kdrs.adminmaintenance.service.AdminService;
import co.jp.arche1.kdrs.usermaintenance.controller.UserMaintenanceController;

@RestController
public class AdminController {

	final static Logger logger = LoggerFactory.getLogger(UserMaintenanceController.class);

	@Autowired
	AdminService adminService;

	// 管理者明細検索
	@RequestMapping(value = "/Admin/ReferAdminMany", method = RequestMethod.GET)
	// @PreAuthorize("hasRole('PM_ADMIN') or hasRole('PM_USER') or
	// hasRole('PM_GUEST')")
	public AdminSearchManyDto referAdminMany(@RequestParam(name = "companyId", required = true) Integer companyId,
			@RequestParam(name = "sei", required = false) String sei,
			@RequestParam(name = "mei", required = false) String mei) throws Exception {
		logger.debug(this.getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName());

		AdminSearchManyDto adminSearchManyDto = new AdminSearchManyDto();
		AdminSearchManyDto.RequestHd regHd = adminSearchManyDto.getReqHd();

		regHd.setCompanyId(companyId);
		regHd.setSei(sei);
		regHd.setMei(mei);

		adminService.searchManyAdmin(adminSearchManyDto);

		return adminSearchManyDto;
	}

	// 管理者登録
	@RequestMapping(value = "/Admin/RegisterAdmin", method = RequestMethod.POST)
	public AdminInsertDto registerAdmin(@RequestBody AdminInsertDto adminInsertDto) throws Exception {
		logger.debug(this.getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName());

		adminService.insert(adminInsertDto);

		return adminInsertDto;
	}

	// 管理者削除
	@RequestMapping(value = "/Admin/RemoveAdmin", method = RequestMethod.DELETE)
	public AdminDeleteDto removeAdmin(@RequestBody AdminDeleteDto adminDeleteDto) throws Exception {
		logger.debug(this.getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName());

		adminService.delete(adminDeleteDto);

		return adminDeleteDto;
	}

}
