package co.jp.arche1.kdrs.companymaintenance.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.jp.arche1.kdrs.companymaintenance.dto.CompanyDeleteDto;
import co.jp.arche1.kdrs.companymaintenance.dto.CompanyInsertDto;
import co.jp.arche1.kdrs.companymaintenance.dto.CompanySearchManyDto;
import co.jp.arche1.kdrs.companymaintenance.dto.CompanySearchOneDto;
import co.jp.arche1.kdrs.companymaintenance.dto.CompanyUpdateDto;
import co.jp.arche1.kdrs.companymaintenance.service.CompanyService;

@RestController
public class CompanyController {

	final static Logger logger = LoggerFactory.getLogger(CompanyController.class);

	@Autowired
	CompanyService companyService;

	@RequestMapping(value = "/Company/ReferCompanyOne", method = RequestMethod.GET)
	//@PreAuthorize("hasRole('PM_ADMIN') or hasRole('PM_USER') or hasRole('PM_GUEST')")
	public CompanySearchOneDto referCompanyOne(
			@RequestParam(name = "companyId", required = true) Integer companyId ) throws Exception {
		logger.debug(this.getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName());
		CompanySearchOneDto companySearchOneDto = new CompanySearchOneDto();
		CompanySearchOneDto.RequestHd regHd = companySearchOneDto.getReqHd();
		regHd.setCompanyId(companyId);
		companyService.searchCompanyOne(companySearchOneDto);
		return companySearchOneDto;
	}


	@RequestMapping(value = "/Company/ReferCompanyMany", method = RequestMethod.GET)
	//@PreAuthorize("hasRole('PM_ADMIN') or hasRole('PM_USER') or hasRole('PM_GUEST')")
	public CompanySearchManyDto referCompanMany(
			@RequestParam(name = "superCompanyId", required = true) Integer superCompanyId ,
			@RequestParam(name = "companyCode", required = false) String companyCode ,
			@RequestParam(name = "companyName", required = false) String companyName) throws Exception {

		logger.debug(this.getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName());

		CompanySearchManyDto companySearchManyDto = new CompanySearchManyDto();
		CompanySearchManyDto.RequestHd regHd = companySearchManyDto.getReqHd();
		regHd.setSuperCompanyId(superCompanyId);
		regHd.setCompanyCode(companyCode);
		regHd.setCompanyName(companyName);
		companyService.searchCompanyMany(companySearchManyDto);
		return companySearchManyDto;
	}

	// 企業登録
	@RequestMapping(value = "/Company/RegisterCompany", method = RequestMethod.POST)
	//@PreAuthorize(\"hasRole('PM_ADMIN')\")"
	public CompanyInsertDto registCompany(@RequestBody CompanyInsertDto companyInsertDto) throws Exception {
		logger.debug(this.getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName());

		companyService.insert(companyInsertDto);

		return companyInsertDto;
	}

	// 企業更新
	@RequestMapping(value = "/Company/ModifyCompany", method = RequestMethod.PUT)
	public CompanyUpdateDto modifyUer(@RequestBody CompanyUpdateDto companyUpdateDto) throws Exception {
		logger.debug(this.getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName());
		companyService.update(companyUpdateDto);
		return companyUpdateDto;
	}

	// 企業削除
	@RequestMapping(value = "/Company/RemoveCompany", method = RequestMethod.DELETE)
	public CompanyDeleteDto removetUer(@RequestBody CompanyDeleteDto companyDeleteDto) throws Exception {
		logger.debug(this.getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName());
		companyService.delete(companyDeleteDto);
        return companyDeleteDto;
	}

}
