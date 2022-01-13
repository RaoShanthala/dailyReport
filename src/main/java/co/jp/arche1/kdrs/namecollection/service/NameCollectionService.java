package co.jp.arche1.kdrs.namecollection.service;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.jp.arche1.kdrs.namecollection.dto.NameCollectionManyDto;
import co.jp.arche1.kdrs.namecollection.dto.NameCollectionOneDto;
import co.jp.arche1.kdrs.namecollection.mapper.PvNamecollectionMapper;
import co.jp.arche1.kdrs.namecollection.repository.PvNamecollectionRepository;
import co.jp.arche1.kdrs.usermaintenance.service.UserService;

@Service
public class NameCollectionService {
	final static Logger logger = LoggerFactory.getLogger(UserService.class);

	@Autowired
	PvNamecollectionMapper pvNamecollectionMapper;


	// ネーム集セクション検索
	@Transactional(readOnly = true)
	public void searchMany(NameCollectionManyDto nameCollectionManyDto) throws Exception {
		logger.debug(this.getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName());

		NameCollectionManyDto.RequestHd reqHd = nameCollectionManyDto.getReqHd();
		List<PvNamecollectionRepository> listpvNamecollectionRepository
			= pvNamecollectionMapper.selectMany(reqHd.getNameSection());

		List<NameCollectionManyDto.ResponseDt> listResDt = nameCollectionManyDto.getResDt();
		for(Iterator<PvNamecollectionRepository> it = listpvNamecollectionRepository.iterator(); it.hasNext();) {
			PvNamecollectionRepository pvNamecollectionRepository =  it.next();
			//listtrnActorSearchResDt.add(convertTranActorSearchResDt(rdbActor));

			NameCollectionManyDto.ResponseDt resDt = new NameCollectionManyDto.ResponseDt();
			resDt.setCodeId(pvNamecollectionRepository.getCodeId());
			resDt.setCodeSection(pvNamecollectionRepository.getCodeSection());
			resDt.setCodeType(pvNamecollectionRepository.getCodeType());
			if (pvNamecollectionRepository.getCodeNumeric() != null) {
				resDt.setCodeNumeric(pvNamecollectionRepository.getCodeNumeric());
			}
			if (StringUtils.isNotEmpty(pvNamecollectionRepository.getCodeString())) {
				resDt.setCodeString(pvNamecollectionRepository.getCodeString());
			}
			if (StringUtils.isNotEmpty(pvNamecollectionRepository.getNameAlpha())) {
			resDt.setNameAlpha(pvNamecollectionRepository.getNameAlpha());
			}
			if (StringUtils.isNotEmpty(pvNamecollectionRepository.getNameShort())) {
			resDt.setNameShort(pvNamecollectionRepository.getNameShort());
			}
			if (StringUtils.isNotEmpty(pvNamecollectionRepository.getNameLong())) {
				resDt.setNameLong(pvNamecollectionRepository.getNameLong());
			}
			if (pvNamecollectionRepository.getCreDate() != null) {
				resDt.setCreDate(pvNamecollectionRepository.getCreDate());
			}
			if (pvNamecollectionRepository.getUpdDatetime() != null) {
				resDt.setUpdDatetime(pvNamecollectionRepository.getUpdDatetime());
			}

			listResDt.add(resDt);
		}
		nameCollectionManyDto.setResultCode("000");
		return;
	}

	// ネーム集1件検索
	@Transactional(readOnly = true)
	public void searchOne(NameCollectionOneDto nameCollectionOneDto) throws Exception {
		logger.debug(this.getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName());

		NameCollectionOneDto.RequestHd reqHd = nameCollectionOneDto.getReqHd();

		PvNamecollectionRepository pvNamecollectionRepository = pvNamecollectionMapper.selectOne(reqHd.getNameSection(),
					reqHd.getCodeNumeric(), reqHd.getCodeString());

		NameCollectionOneDto.ResponseHd resHd = new NameCollectionOneDto.ResponseHd();
		resHd.setCodeId(pvNamecollectionRepository.getCodeId());
		resHd.setCodeSection(pvNamecollectionRepository.getCodeSection());
		resHd.setCodeType(pvNamecollectionRepository.getCodeType());
		if (pvNamecollectionRepository.getCodeNumeric() != null) {
			resHd.setCodeNumeric(pvNamecollectionRepository.getCodeNumeric());
		}
		if (StringUtils.isNotEmpty(pvNamecollectionRepository.getCodeString())) {
			resHd.setCodeString(pvNamecollectionRepository.getCodeString());
		}
		if (StringUtils.isNotEmpty(pvNamecollectionRepository.getNameAlpha())) {
			resHd.setNameAlpha(pvNamecollectionRepository.getNameAlpha());
		}
		if (StringUtils.isNotEmpty(pvNamecollectionRepository.getNameShort())) {
			resHd.setNameShort(pvNamecollectionRepository.getNameShort());
		}
		if (StringUtils.isNotEmpty(pvNamecollectionRepository.getNameLong())) {
			resHd.setNameLong(pvNamecollectionRepository.getNameLong());
		}
		if (pvNamecollectionRepository.getCreDate() != null) {
			resHd.setCreDate(pvNamecollectionRepository.getCreDate());
		}
		if (pvNamecollectionRepository.getUpdDatetime() != null) {
			resHd.setUpdDatetime(pvNamecollectionRepository.getUpdDatetime());
		}

		nameCollectionOneDto.setResHd(resHd);
		nameCollectionOneDto.setResultCode("000");

		return;
	}

}
