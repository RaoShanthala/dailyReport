package co.jp.arche1.kdrs.report.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.jp.arche1.kdrs.report.dto.ReportDto;
import co.jp.arche1.kdrs.report.dto.ReportSearchManyDto;
import co.jp.arche1.kdrs.report.service.ReportService;

@RestController
public class ReportController {

	final static Logger logger = LoggerFactory.getLogger(ReportController.class);

	@Autowired
	ReportService reportService;

	@Autowired
	MessageSource msg;

	// 工事登録・更新・削除
	@RequestMapping(value = "/Report/RegisterUpdateDeleteReport", method = RequestMethod.POST)
	@PreAuthorize("hasRole('PM_ADMIN') or hasRole('PM_USER') or hasRole('PM_GUEST')")
	public ReportDto registerUpdateDeleteReport(@RequestBody ReportDto reportDto) throws Exception {
		logger.debug(this.getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName());

		reportService.createUpdateDelete(reportDto);

		return reportDto;
	}

	// 日報明細検索
	@RequestMapping(value = "/Report/ReferReportMany", method = RequestMethod.GET)
	@PreAuthorize("hasRole('PM_ADMIN') or hasRole('PM_USER') or hasRole('PM_GUEST')")
	public ReportSearchManyDto referReportMany(@RequestParam(name = "userId", required = false) Integer userId,
			@RequestParam(name = "reportNo", required = false) Integer reportNo,
			@RequestParam(name = "privConstId", required = false) Integer privConstId,
			@RequestParam(name = "deleted", required = false) String deleted) throws Exception {
		logger.debug(this.getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName());

		ReportSearchManyDto reportSearchManyDto = new ReportSearchManyDto();
		ReportSearchManyDto.RequestHd regHd = reportSearchManyDto.getReqHd();
        
		regHd.setUserId(userId);
		regHd.setReportNo(reportNo);
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
		reportService.searchMany(reportSearchManyDto);
		return reportSearchManyDto;
	}
	
	// 日報明細検索
	/*	@RequestMapping(value = "/Report/ReferReportMany", method = RequestMethod.GET)
		@PreAuthorize("hasRole('PM_ADMIN') or hasRole('PM_USER') or hasRole('PM_GUEST')")
		public ReportSearchManyDto referReportMany(@RequestParam(name = "reportNo", required = false) Integer reportNo,
				@RequestParam(name = "privConstId", required = false) Integer privConstId,
				@RequestParam(name = "deleted", required = false) String deleted) throws Exception {
			logger.debug(this.getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName());

			ReportSearchManyDto reportSearchManyDto = new ReportSearchManyDto();
			ReportSearchManyDto.RequestHd regHd = reportSearchManyDto.getReqHd();

			regHd.setReportNo(reportNo);
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
			reportService.searchMany(reportSearchManyDto);
			return reportSearchManyDto;
		} */



}
