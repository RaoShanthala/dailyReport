package co.jp.arche1.kdrs.construction.controller;

import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
import co.jp.arche1.kdrs.construction.service.ConstructionService;

@RestController
public class ConstructionController {

	final static Logger logger = LoggerFactory.getLogger(ConstructionController.class);

	@Autowired
	ConstructionService constructionService;

	@Autowired
	MessageSource msg;

	// プライベート工事登録・更新・削除
	@RequestMapping(value = "/Construction/RegisterUpdateDeletePrivConstruction", method = RequestMethod.POST)
	//@PreAuthorize("hasRole('PM_ADMIN') or hasRole('PM_USER') or hasRole('PM_GUEST')")
	public ConstructionDto registerUpdateDeletePrivConstruction(@RequestBody ConstructionDto constructionDto)
			throws Exception {
		logger.debug(this.getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName());
		constructionService.createUpdateDeletePrivConstruction(constructionDto);
		return constructionDto;
	}

	// 物件1件検索
	@RequestMapping(value = "/Construction/ReferConstructionOne", method = RequestMethod.GET)
	//@PreAuthorize("hasRole('PM_ADMIN') or hasRole('PM_USER') or hasRole('PM_GUEST')")
	public ConstructionSearchOneDto referConstructionOne(
			@RequestParam(name = "constId", required = true) Integer constId) throws Exception {
		logger.debug(this.getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName());
		ConstructionSearchOneDto constructionSearchOneDto = new ConstructionSearchOneDto();
		ConstructionSearchOneDto.RequestHd regHd = constructionSearchOneDto.getReqHd();
		regHd.setConstId(constId);
		constructionService.searchConstructionOne(constructionSearchOneDto);
		return constructionSearchOneDto;
	}

	// 工事明細検索
/*	@RequestMapping(value = "/Construction/ReferConstructionMany", method = RequestMethod.GET)
	@PreAuthorize("hasRole('PM_ADMIN') or hasRole('PM_USER') or hasRole('PM_GUEST')")
	public ConstructionSearchManyDto referConstructionMany(
			@RequestParam(name = "constId", required = false) Integer constId,
			@RequestParam(name = "constCode", required = false) String constCode,
			@RequestParam(name = "constName", required = false) String constName,
			@RequestParam(name = "deleted", required = false) String deleted) throws Exception {
		logger.debug(this.getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName());

		ConstructionSearchManyDto constructionSearchManyDto = new ConstructionSearchManyDto();
		ConstructionSearchManyDto.RequestHd regHd = constructionSearchManyDto.getReqHd();

		regHd.setConstId(constId);
		regHd.setConstCode(constCode);
		regHd.setConstName(constName);
		if (deleted != null) {
			if (deleted.equalsIgnoreCase("FALSE")) {
				regHd.setDeleted((byte) 0);
			} else if (deleted.equalsIgnoreCase("TRUE")) {
				regHd.setDeleted((byte) 1);
			}
		} else {
			regHd.setDeleted((byte) 2);
		}
		constructionService.searchManyConstruction(constructionSearchManyDto);
		return constructionSearchManyDto;
	} */

	// 工事明細検索
	@RequestMapping(value = "/Construction/ReferConstructionList", method = RequestMethod.GET)
	//@PreAuthorize("hasRole('PM_ADMIN') or hasRole('PM_USER') or hasRole('PM_GUEST')")
	public ConstructionListDto referConstructionList(
			@RequestParam(name = "constCode", required = false) String constCode,
			@RequestParam(name = "constName", required = false) String constName,
			@RequestParam(name = "targetState", required = false) Byte targetState,
			@RequestParam(name = "targetStartDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate targetStartDate,
			@RequestParam(name = "targetEndDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate targetEndDate)
			throws Exception {
		logger.debug(this.getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName());

		ConstructionListDto constructionListDto = new ConstructionListDto();
		ConstructionListDto.RequestHd regHd = constructionListDto.getReqHd();

		regHd.setConstCode(constCode);
		regHd.setConstName(constName);
		regHd.setTargetState(targetState);
		regHd.setTargetStartDate(targetStartDate);
		regHd.setTargetEndDate(targetEndDate);

		constructionService.searchConstructionList(constructionListDto);
		return constructionListDto;
	}

	// 工事登録
	@RequestMapping(value = "/Construction/RegisterConstruction", method = RequestMethod.POST)
	@PreAuthorize("hasRole('PM_ADMIN') or hasRole('PM_USER') or hasRole('PM_GUEST')")
	public ConstructionInsertDto registerConstruction(@RequestBody ConstructionInsertDto constructionInsertDto)
			throws Exception {
		logger.debug(this.getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName());
		constructionService.insert(constructionInsertDto);
		return constructionInsertDto;
	}

	// 工事削除
	@RequestMapping(value = "/Construction/DeleteConstruction", method = RequestMethod.DELETE)
	@PreAuthorize("hasRole('PM_ADMIN')")
	public ConstructionDeleteDto deleteConstruction(@RequestBody ConstructionDeleteDto constructionDeleteDto)
			throws Exception {
		logger.debug(this.getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName());
		constructionService.delete(constructionDeleteDto);
		return constructionDeleteDto;
	}

	// 工事更新
	@RequestMapping(value = "/Construction/UpdateConstruction", method = RequestMethod.POST)
	@PreAuthorize("hasRole('PM_ADMIN') or hasRole('PM_USER') or hasRole('PM_GUEST')")
	public ConstructionUpdateDto updateConstruction(@RequestBody ConstructionUpdateDto constructionUpdateDto)
			throws Exception {
		logger.debug(this.getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName());
		constructionService.update(constructionUpdateDto);
		return constructionUpdateDto;
	}

	// プライベート工事List
	@RequestMapping(value = "/Construction/ReferPrivConstructionList", method = RequestMethod.GET)
	@PreAuthorize("hasRole('PM_ADMIN') or hasRole('PM_USER') or hasRole('PM_GUEST')")
	public PrivConstructionSearchListDto referPrivConstructionList(
			@RequestParam(name = "constId", required = false) Integer constId,
			@RequestParam(name = "privConstName", required = false) String privConstName,
			@RequestParam(name = "userName", required = false) String userName,
			@RequestParam(name = "searchType", required = true) Byte searchType) throws Exception {
		logger.debug(this.getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName());

		PrivConstructionSearchListDto privConstructionSearchListDto = new PrivConstructionSearchListDto();
		PrivConstructionSearchListDto.RequestHd regHd = privConstructionSearchListDto.getReqHd();

		regHd.setConstId(constId);
		regHd.setUserName(userName);
		regHd.setPrivConstName(privConstName);
		regHd.setSearchType(searchType);

		constructionService.searchPrivConstructionList(privConstructionSearchListDto);
		return privConstructionSearchListDto;
	}

	// 工事Id登録
	@RequestMapping(value = "/Construction/AddConstIds", method = RequestMethod.POST)
	@PreAuthorize("hasRole('PM_ADMIN')")
	public PrivConstructionUpdateConstIdDto addConstIds(
			@RequestBody PrivConstructionUpdateConstIdDto privConstructionUpdateConstIdDto) throws Exception {
		logger.debug(this.getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName());
		constructionService.updateConstId(privConstructionUpdateConstIdDto);
		return privConstructionUpdateConstIdDto;
	}

	// 工事Id更新
	@RequestMapping(value = "/Construction/DeleteConstIds", method = RequestMethod.POST)
	@PreAuthorize("hasRole('PM_ADMIN')")
	public PrivConstructionUpdateConstIdDto deleteConstIds(
			@RequestBody PrivConstructionUpdateConstIdDto privConstructionUpdateConstIdDto) throws Exception {
		logger.debug(this.getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName());
		constructionService.updateConstId(privConstructionUpdateConstIdDto);
		return privConstructionUpdateConstIdDto;
	}

	// プライベート工事明細検索  (can be used for users history function as well)
	@RequestMapping(value = "/Construction/ReferPrivConstructionMany", method = RequestMethod.GET)
	@PreAuthorize("hasRole('PM_ADMIN') or hasRole('PM_USER') or hasRole('PM_GUEST')")
	public PrivConstructionSearchManyDto referPrivConstructionMany(
			@RequestParam(name = "userId", required = false) Integer userId,
			@RequestParam(name = "privConstId", required = false) Integer privConstId,
			@RequestParam(name = "deleted", required = false) String deleted) throws Exception {
		logger.debug(this.getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName());

		PrivConstructionSearchManyDto privConstructionSearchManyDto = new PrivConstructionSearchManyDto();
		PrivConstructionSearchManyDto.RequestHd regHd = privConstructionSearchManyDto.getReqHd();

		regHd.setUserId(userId);
		regHd.setPrivConstId(privConstId);
		if (deleted != null) {
			if (deleted.equalsIgnoreCase("FALSE")) {
				regHd.setDeleted((byte) 0);
			} else if (deleted.equalsIgnoreCase("TRUE")) {
				regHd.setDeleted((byte) 1);
			}
		} else {
			regHd.setDeleted((byte) 2);
		}
		constructionService.searchMany(privConstructionSearchManyDto);
		return privConstructionSearchManyDto;
	}

	// 工事月別日報一覧 ConstructionMonthReport
	@RequestMapping(value = "/Construction/ReferConstructionMonthReportMany", method = RequestMethod.GET)
	@PreAuthorize("hasRole('PM_ADMIN') or hasRole('PM_USER') or hasRole('PM_GUEST')")
	public ConstructionMonthReportDto referConstructionMonthReportMany(
			@RequestParam(name = "constId", required = true) Integer constId,
			 @RequestParam(name = "startDate", required = true) @DateTimeFormat(iso =
			 DateTimeFormat.ISO.DATE) LocalDate startDate,
			 @RequestParam(name = "endDate", required = false) @DateTimeFormat(iso =
			 DateTimeFormat.ISO.DATE) LocalDate endDate,
		//	@RequestParam(name = "startDate", required = true) Integer startDate,
		//	@RequestParam(name = "endDate", required = false) Integer endDate,
			@RequestParam(name = "deleted", required = false) String deleted) throws Exception {
		logger.debug(this.getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName());

		ConstructionMonthReportDto constructionMonthReportDto = new ConstructionMonthReportDto();
		ConstructionMonthReportDto.RequestHd regHd = constructionMonthReportDto.getReqHd();

		regHd.setConstId(constId);
		regHd.setStartDate(startDate);
		regHd.setEndDate(endDate);
		System.out.println ("DELETED ==  " + deleted);
		if (deleted != null) {
			if (deleted.equalsIgnoreCase("FALSE")) {
				regHd.setDeleted((byte) 0);
			} else if (deleted.equalsIgnoreCase("TRUE")) {
				regHd.setDeleted((byte) 1);
			}
		} else {
			regHd.setDeleted((byte) 2);
		}
		constructionService.searchManyConstructionMonthReport(constructionMonthReportDto);
		return constructionMonthReportDto;
	}

	// 工事月別作業一覧 ConstructionMonthOrder
	@RequestMapping(value = "/Construction/ReferConstructionMonthOrderMany", method = RequestMethod.GET)
	@PreAuthorize("hasRole('PM_ADMIN') or hasRole('PM_USER') or hasRole('PM_GUEST')")
	public ConstructionMonthOrderDto referConstructionMonthOrderMany(
			@RequestParam(name = "constId", required = true) Integer constId,
			@RequestParam(name = "startDate", required = true) @DateTimeFormat(iso =
			 DateTimeFormat.ISO.DATE) LocalDate startDate,
			 @RequestParam(name = "endDate", required = false) @DateTimeFormat(iso =
			 DateTimeFormat.ISO.DATE) LocalDate endDate,
		//	@RequestParam(name = "startDate", required = true) Integer startDate,
		//	@RequestParam(name = "endDate", required = false) Integer endDate,
			@RequestParam(name = "deleted", required = false) String deleted) throws Exception {
		logger.debug(this.getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName());

		ConstructionMonthOrderDto constructionMonthOrderDto = new ConstructionMonthOrderDto();
		ConstructionMonthOrderDto.RequestHd regHd = constructionMonthOrderDto.getReqHd();

		regHd.setConstId(constId);
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
		constructionService.searchManyConstructionMonthOrder(constructionMonthOrderDto);
		return constructionMonthOrderDto;
	}

}
