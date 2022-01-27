package co.jp.arche1.kdrs.companymaintenance.service;

import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import co.jp.arche1.kdrs.common.BaseService;
import co.jp.arche1.kdrs.common.exception.OptimisticLockException;
import co.jp.arche1.kdrs.companymaintenance.dto.CompanyDeleteDto;
import co.jp.arche1.kdrs.companymaintenance.dto.CompanyInsertDto;
import co.jp.arche1.kdrs.companymaintenance.dto.CompanySearchManyDto;
import co.jp.arche1.kdrs.companymaintenance.dto.CompanySearchOneDto;
import co.jp.arche1.kdrs.companymaintenance.dto.CompanyUpdateDto;
import co.jp.arche1.kdrs.companymaintenance.mapper.PtCompanyMapper;
import co.jp.arche1.kdrs.companymaintenance.repository.PtCompanyRepository;
import co.jp.arche1.kdrs.usermaintenance.dto.UserDeleteDto;
import co.jp.arche1.kdrs.usermaintenance.dto.UserUpdateDto;
import co.jp.arche1.kdrs.usermaintenance.repository.PtUserRepository;


@Service
public class CompanyService extends BaseService {

	final static Logger logger = LoggerFactory.getLogger(CompanyService.class);

	@Autowired
	PtCompanyMapper ptCompanyMapper;

	public void searchCompanyOne(CompanySearchOneDto companySearchOneDto) throws Exception {
		logger.debug(this.getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName());

		CompanySearchOneDto.RequestHd reqHd = companySearchOneDto.getReqHd();
		PtCompanyRepository ptCompanyRepository = ptCompanyMapper.selectOne(
				reqHd.getCompanyId());
		CompanySearchOneDto.ResponseDt resDt = companySearchOneDto.getResDt();
		resDt.setCompanyId(ptCompanyRepository.getCompanyId());
		resDt.setCompanyCode(ptCompanyRepository.getCompanyCode());
		resDt.setCompanyName(ptCompanyRepository.getCompanyName());
		resDt.setCompanyNameKana(ptCompanyRepository.getCompanyNameKana());
		resDt.setPhone(ptCompanyRepository.getPhone());
		resDt.setPrefacture(ptCompanyRepository.getPrefacture());
		resDt.setCity(ptCompanyRepository.getCity());
		resDt.setStreetNumber(ptCompanyRepository.getStreetNumber());
		resDt.setBuildingName(ptCompanyRepository.getBuildingName());

		resDt.setCreatedAt(ptCompanyRepository.getCreatedAt());
		resDt.setUpdatedAt(ptCompanyRepository.getUpdatedAt());
		if (ptCompanyRepository.getDeleted() == (byte) 0) {
			resDt.setDeleted("FALSE");
		} else if (ptCompanyRepository.getDeleted() == (byte) 1) {
			resDt.setDeleted("TRUE");
		}
		makeResponseTitle(companySearchOneDto);
		companySearchOneDto.setResultCode("000");
		return;
	}

	public void searchCompanyMany(CompanySearchManyDto companySearchManyDto) throws Exception{
		logger.debug(this.getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName());

		CompanySearchManyDto.RequestHd reqHd = companySearchManyDto.getReqHd();

		List<PtCompanyRepository> listPtCompanyRepository = ptCompanyMapper
				.selectMany(reqHd.getSuperCompanyId(),reqHd.getCompanyCode(), reqHd.getCompanyName() );

		List<CompanySearchManyDto.ResponseDt> listResDt = companySearchManyDto.getResDt();
		for (Iterator<PtCompanyRepository> it = listPtCompanyRepository.iterator(); it.hasNext();) {
			PtCompanyRepository ptCompanyRepository = it.next();

			CompanySearchManyDto.ResponseDt resDt = new CompanySearchManyDto.ResponseDt();
			resDt.setCompanyId(ptCompanyRepository.getCompanyId());
			resDt.setCompanyCode(ptCompanyRepository.getCompanyCode());
			resDt.setCompanyName(ptCompanyRepository.getCompanyName());
			resDt.setCompanyNameKana(ptCompanyRepository.getCompanyNameKana());
			resDt.setPhone(ptCompanyRepository.getPhone());
			resDt.setPrefacture(ptCompanyRepository.getPrefacture());
			resDt.setCity(ptCompanyRepository.getCity());
			resDt.setStreetNumber(ptCompanyRepository.getStreetNumber());
			resDt.setBuildingName(ptCompanyRepository.getBuildingName());

			resDt.setCreatedAt(ptCompanyRepository.getCreatedAt().withNano(0));
			resDt.setUpdatedAt(ptCompanyRepository.getUpdatedAt().withNano(0));
			if (ptCompanyRepository.getDeleted() == (byte) 0) {
				resDt.setDeleted("FALSE");
			} else if (ptCompanyRepository.getDeleted() == (byte) 1) {
				resDt.setDeleted("TRUE");
			}
			listResDt.add(resDt);
		}

		makeResponseTitle(companySearchManyDto);

		if (listResDt.size() > 0) {
			companySearchManyDto.setResultCode("000");
		} else {
			companySearchManyDto.setResultCode("001");
		}
		return;
	}


	public void insert(CompanyInsertDto companyInsertDto) throws Exception {
		logger.debug(this.getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName());
		CompanyInsertDto.RequestHd reqHd = companyInsertDto.getReqHd();
		PtCompanyRepository ptCompanyRepository = new PtCompanyRepository();
		ptCompanyRepository.setCompanyCode(reqHd.getCompanyCode());
		ptCompanyRepository.setCompanyName(reqHd.getCompanyName());
		ptCompanyRepository.setCompanyNameKana(reqHd.getCompanyNameKana());
		ptCompanyRepository.setPhone(reqHd.getPhone());
		ptCompanyRepository.setPrefacture(reqHd.getPrefacture());
		ptCompanyRepository.setCity(reqHd.getCity());
		ptCompanyRepository.setStreetNumber(reqHd.getStreetNumber());
		ptCompanyRepository.setBuildingName(reqHd.getBuildingName());
		ptCompanyRepository.setDeleted((byte) 0);
		try {
			// ユーザの登録
			ptCompanyMapper.insert(ptCompanyRepository);

		} catch (DuplicateKeyException ex) {
			companyInsertDto.setResultCode("002");
			companyInsertDto.setResultMessage("（操作：insert、テーブル名：pt_company、企業名：" + reqHd.getCompanyName() + "）");
			return;
		}
		companyInsertDto.setResultCode("000");
		return;
	}

	// ユーザの更新、ユーザ／業務の登録、更新、削除
		// @Transactional(readOnly = false, rollbackFor = Exception.class)
		public void update(CompanyUpdateDto companyUpdateDto) throws Exception {
			logger.debug(this.getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName());

			CompanyUpdateDto.RequestHd reqHd = companyUpdateDto.getReqHd();
			PtCompanyRepository ptCompanyRepository = new PtCompanyRepository();

			ptCompanyRepository.setCompanyId(reqHd.getCompanyId());
			ptCompanyRepository.setCompanyCode(reqHd.getCompanyCode());
			ptCompanyRepository.setCompanyName(reqHd.getCompanyName());
			ptCompanyRepository.setCompanyNameKana(reqHd.getCompanyNameKana());
			ptCompanyRepository.setPhone(reqHd.getPhone());
			ptCompanyRepository.setPrefacture(reqHd.getPrefacture());
			ptCompanyRepository.setCity(reqHd.getCity());
			ptCompanyRepository.setStreetNumber(reqHd.getStreetNumber());
			ptCompanyRepository.setBuildingName(reqHd.getBuildingName());

			// 企業の更新
			int cnt = ptCompanyMapper.update(ptCompanyRepository);
			if (cnt == 0) {
				throw new OptimisticLockException("（操作：update、テーブル名：pt_company）");
			}
			companyUpdateDto.setResultCode("000");
			return;
		}

		// ユーザ (論理削除)
		public void delete(CompanyDeleteDto companyDeleteDto) throws Exception {
			logger.debug(this.getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName());

			CompanyDeleteDto.RequestHd reqHd = companyDeleteDto.getReqHd();
			// ユーザの削除(
			int cnt = ptCompanyMapper.deleteUpdate(reqHd.getCompanyId(), (byte) 1);
			if (cnt == 0) {
				throw new OptimisticLockException("delete pt_user");
			}
			companyDeleteDto.setResultCode("000");
			return;
		}




}
