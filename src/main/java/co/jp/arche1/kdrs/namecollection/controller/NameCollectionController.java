package co.jp.arche1.kdrs.namecollection.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.jp.arche1.kdrs.namecollection.dto.NameCollectionManyDto;
import co.jp.arche1.kdrs.namecollection.dto.NameCollectionOneDto;
import co.jp.arche1.kdrs.namecollection.service.NameCollectionService;

@RestController
public class NameCollectionController {
	final static Logger logger = LoggerFactory.getLogger(NameCollectionController.class);

	@Autowired
	NameCollectionService nameCollectionService;

	// ネーム集セクション検索
	@RequestMapping(value = "/NameCollection/ReferMany", method = RequestMethod.GET)
	//@PreAuthorize("hasRole('PM_ADMIN') or hasRole('PM_USER') or hasRole('PM_GUEST')")
	public NameCollectionManyDto referMany(@RequestParam(name="nameSection", required = true) String nameSection) throws Exception {

		logger.debug(this.getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName());

		NameCollectionManyDto nameCollectionManyDto = new NameCollectionManyDto();
		NameCollectionManyDto.RequestHd regHd = nameCollectionManyDto.getReqHd();

		regHd.setNameSection(nameSection);

		nameCollectionService.searchMany(nameCollectionManyDto);

	    return nameCollectionManyDto;
	}

	// ネーム集1件検索
	@RequestMapping(value = "/NameCollection/ReferOne", method = RequestMethod.GET)
	//@PreAuthorize("hasRole('PM_ADMIN') or hasRole('PM_USER') or hasRole('PM_GUEST')")
	public NameCollectionOneDto referOne(@RequestParam(name="nameSection", required = true) String nameSection,
											@RequestParam(name="codeNumeric", required = false) Short codeNumeric,
											@RequestParam(name="codeString", required = false) String codeString) throws Exception  {

		logger.debug(this.getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName());

		NameCollectionOneDto nameCollectionOneDto = new NameCollectionOneDto();
		NameCollectionOneDto.RequestHd regHd = nameCollectionOneDto.getReqHd();

		regHd.setNameSection(nameSection);
		regHd.setCodeNumeric(codeNumeric);
		regHd.setCodeString(codeString);

		nameCollectionService.searchOne(nameCollectionOneDto);

	    return nameCollectionOneDto;
	}

}
