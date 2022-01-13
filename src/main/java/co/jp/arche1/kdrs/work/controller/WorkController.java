package co.jp.arche1.kdrs.work.controller;

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

import co.jp.arche1.kdrs.work.dto.WorkDto;
import co.jp.arche1.kdrs.work.dto.WorkSearchManyDto;
import co.jp.arche1.kdrs.work.service.WorkService;

@RestController
public class WorkController {

	final static Logger logger = LoggerFactory.getLogger(WorkController.class);

	@Autowired
	WorkService workService;

	@Autowired
	MessageSource msg;

	// 作業詳細登録・更新・削除
	@RequestMapping(value = "/Work/RegisterUpdateDeleteWork", method = RequestMethod.POST)
	@PreAuthorize("hasRole('PM_ADMIN') or hasRole('PM_USER') or hasRole('PM_GUEST')")
	public WorkDto registerUpdateDeleteWork(@RequestBody WorkDto workDto) throws Exception  {
		logger.debug(this.getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName());

		workService.createUpdateDelete(workDto);

		return workDto;
	}

	// 作業詳細明細検索
			@RequestMapping(value = "/Work/ReferWorkMany", method = RequestMethod.GET)
			@PreAuthorize("hasRole('PM_ADMIN') or hasRole('PM_USER') or hasRole('PM_GUEST')")
			public WorkSearchManyDto referWorkMany(@RequestParam(name = "userId", required = false) Integer userId,
					@RequestParam(name = "orderNo", required = false) Integer orderNo,
					@RequestParam(name = "workNo", required = false) Integer workNo,
					@RequestParam(name = "privConstId", required = false) Integer privConstId,
					@RequestParam(name = "deleted", required = false) String deleted) throws Exception {
				logger.debug(this.getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName());

				WorkSearchManyDto workSearchManyDto = new WorkSearchManyDto();
				WorkSearchManyDto.RequestHd regHd = workSearchManyDto.getReqHd();
				regHd.setUserId(userId);
				regHd.setOrderNo(orderNo);
				regHd.setWorkNo(workNo);
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
				workService.searchMany(workSearchManyDto);
				return workSearchManyDto;
			}

}
