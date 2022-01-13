package co.jp.arche1.kdrs.gaitoserver.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.jp.arche1.kdrs.gaitoserver.dto.ZipcodeDto;
import co.jp.arche1.kdrs.gaitoserver.service.GaitoServerService;

@RestController
public class GaitoserverController {

	@Autowired
    GaitoServerService zipService;

	final static Logger logger = LoggerFactory.getLogger(GaitoserverController.class);

	@RequestMapping(value = "/GaitoServer/GetAddress", method = RequestMethod.GET)
	@PreAuthorize("hasRole('PM_ADMIN') or hasRole('PM_USER') or hasRole('PM_GUEST')")
	public ZipcodeDto getAddress(@RequestParam(name = "zipcode", required = true) String zipcode)
			throws Exception {

		logger.debug(this.getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName());

		System.out.println("Code == " + zipcode);

		ZipcodeDto zipCodeDto =  zipService.getAddress(zipcode);

		System.out.println("DTO " + zipCodeDto.getResults().toString());

		return zipCodeDto;
	}

}
