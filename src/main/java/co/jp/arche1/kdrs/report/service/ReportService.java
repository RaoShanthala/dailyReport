package co.jp.arche1.kdrs.report.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import co.jp.arche1.kdrs.common.BaseService;
import co.jp.arche1.kdrs.construction.mapper.PtPrivconstMapper;
import co.jp.arche1.kdrs.construction.repository.PtPrivconstRepository;
import co.jp.arche1.kdrs.report.dto.ReportDto;
import co.jp.arche1.kdrs.report.dto.ReportSearchManyDto;
import co.jp.arche1.kdrs.report.mapper.PtReportMapper;
import co.jp.arche1.kdrs.report.mapper.PvConstructionReportUserMapper;
import co.jp.arche1.kdrs.report.repository.PtReportRepository;
import co.jp.arche1.kdrs.report.repository.PvConstructionReportUserRepository;

@Service
public class ReportService extends BaseService {

	@Autowired
	PtReportMapper ptReportMapper;
	@Autowired
	PvConstructionReportUserMapper pvConstructionReportUserMapper;
	@Autowired
	PtPrivconstMapper ptPrivconstMapper;

	final static Logger logger = LoggerFactory.getLogger(ReportService.class);

	public void createUpdateDelete(ReportDto reportDto) throws Exception {

		logger.debug(this.getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName());

		List<ReportDto.RequestDt> listReqDt = reportDto.getReqDt();

		List<PtReportRepository> listPtReportRepositoryInsert = new ArrayList<PtReportRepository>();
		List<PtReportRepository> listPtReportRepositoryUpdate = new ArrayList<PtReportRepository>();
		List<PtReportRepository> listPtReportRepositoryDelete = new ArrayList<PtReportRepository>();

		List<PtPrivconstRepository> listPtPrivconstRepositoryUpdate = new ArrayList<PtPrivconstRepository>();

		for (int i = 0; i < listReqDt.size(); i++) {
			PtReportRepository ptReportRepository = new PtReportRepository();

			ptReportRepository.setReportCode(listReqDt.get(i).getReportCode());
			ptReportRepository.setUpdatedAt(listReqDt.get(i).getUpdatedAt());
			// 1.登録、2.更新、3.削除
			if (listReqDt.get(i).getAction() == (byte) 3) { // 削除
				ptReportRepository.setDeleted((byte) 1);
				listPtReportRepositoryDelete.add(ptReportRepository);
			} else {
				ptReportRepository.setPersonCode(listReqDt.get(i).getPersonCode());
				ptReportRepository.setReportDate(listReqDt.get(i).getReportDate());
				ptReportRepository.setDetail(listReqDt.get(i).getDetail());
				ptReportRepository.setConstType(listReqDt.get(i).getConstType());
				ptReportRepository.setStaff(listReqDt.get(i).getStaff());
				ptReportRepository.setNumPerson(listReqDt.get(i).getNumPerson());
				ptReportRepository.setHours(listReqDt.get(i).getHours());
				ptReportRepository.setEarlyHours(listReqDt.get(i).getEarlyHours());
				ptReportRepository.setOverHours(listReqDt.get(i).getOverHours());
				if (listReqDt.get(i).getAction() == (byte) 1) { // 登録
					int privConstId = ptPrivconstMapper.selectPrivconstId(listReqDt.get(i).getConstCode());
					ptReportRepository.setPrivConstId(privConstId);
					ptReportRepository.setCreatedAt(listReqDt.get(i).getCreatedAt());
					listPtReportRepositoryInsert.add(ptReportRepository);
					// to Update maxReportNo in PtPrivconst table
					PtPrivconstRepository ptPrivconstRepository = new PtPrivconstRepository();
					ptPrivconstRepository.setPrivConstCode(listReqDt.get(i).getConstCode());
					ptPrivconstRepository.setUpdatedAt(listReqDt.get(i).getUpdatedAt());
					listPtPrivconstRepositoryUpdate.add(ptPrivconstRepository);
				} else if (listReqDt.get(i).getAction() == (byte) 2) { // 更新
					listPtReportRepositoryUpdate.add(ptReportRepository);
				}
			}
		}
		try {
			if (listPtReportRepositoryInsert.size() > 0) {
				for (int i = 0; i < listPtReportRepositoryInsert.size(); i++) {
					PtReportRepository ptReportRepository = listPtReportRepositoryInsert.get(i);
					ptReportMapper.insert(ptReportRepository);
					PtPrivconstRepository ptPrivconstRepository = listPtPrivconstRepositoryUpdate.get(i);
					ptPrivconstMapper.updateReportNo(ptPrivconstRepository);
				}
			}
			if (listPtReportRepositoryUpdate.size() > 0) {
				ptReportMapper.updateAll(listPtReportRepositoryUpdate);
			}
			if (listPtReportRepositoryDelete.size() > 0) {
				ptReportMapper.deleteAll(listPtReportRepositoryDelete); // Soft delete
			}
		} catch (DataIntegrityViolationException ex) {
			reportDto.setResultCode("002");
			reportDto.setResultMessage("（操作 Data already exist. Cannot insert 、テーブル名：pt_report）" + ex.getMessage());
			return;
		} catch (Exception ex) {
			reportDto.setResultCode("002");
			reportDto.setResultMessage("（操作：Insert, Update, Delete 、テーブル名：pt_report）" + ex.getMessage());
			return;
		}
		reportDto.setResultCode("000");
		return;
	}

	// 日報明細検索
	public void searchMany(ReportSearchManyDto reportSearchManyDto) throws Exception {
		logger.debug(this.getClass().getName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName());

		ReportSearchManyDto.RequestHd reqHd = reportSearchManyDto.getReqHd();
		/*List<PtReportRepository> listPtReportRepository = ptReportMapper.selectMany(reqHd.getPrivConstId(),
				reqHd.getReportNo(), reqHd.getDeleted());*/

		List<PvConstructionReportUserRepository> listPtReportRepository = pvConstructionReportUserMapper.selectMany(
				reqHd.getUserId(), reqHd.getPrivConstId(),
				reqHd.getReportNo(), reqHd.getDeleted());

		List<ReportSearchManyDto.ResponseDt> listResDt = reportSearchManyDto.getResDt();
		for (Iterator<PvConstructionReportUserRepository> it = listPtReportRepository.iterator(); it.hasNext();) {
			PvConstructionReportUserRepository pvConstructionReportUserRepository = it.next();

			ReportSearchManyDto.ResponseDt resDt = new ReportSearchManyDto.ResponseDt();
			resDt.setPrivConstId(pvConstructionReportUserRepository.getPrivConstId());
			resDt.setPrivConstCode(pvConstructionReportUserRepository.getPrivConstCode());
			resDt.setPrivConstName(pvConstructionReportUserRepository.getPrivConstName());
			resDt.setUserId(pvConstructionReportUserRepository.getUserId());
			resDt.setUserName(pvConstructionReportUserRepository.getUserName());
			resDt.setLoginUser(pvConstructionReportUserRepository.getLoginUser());
			resDt.setReportNo(pvConstructionReportUserRepository.getReportNo());
			resDt.setReportCode(pvConstructionReportUserRepository.getReportCode());
			resDt.setPersonCode(pvConstructionReportUserRepository.getPersonCode());
			resDt.setReportDate(pvConstructionReportUserRepository.getReportDate());
			resDt.setDetail(pvConstructionReportUserRepository.getDetail());
			resDt.setConstType(pvConstructionReportUserRepository.getConstType());
			resDt.setStaff(pvConstructionReportUserRepository.getStaff());
			resDt.setNumPerson(pvConstructionReportUserRepository.getNumPerson());
			resDt.setHours(pvConstructionReportUserRepository.getHours());
			resDt.setEarlyHours(pvConstructionReportUserRepository.getEarlyHours());
			resDt.setOverHours(pvConstructionReportUserRepository.getOverHours());
			resDt.setCreatedAt(pvConstructionReportUserRepository.getCreatedAt());
			resDt.setUpdatedAt(pvConstructionReportUserRepository.getUpdatedAt());
			if (pvConstructionReportUserRepository.getDeleted() == (byte) 0) {
				resDt.setDeleted("FALSE");
			} else if (pvConstructionReportUserRepository.getDeleted() == (byte) 1) {
				resDt.setDeleted("TRUE");
			}
			listResDt.add(resDt);
		}
		makeResponseTitle(reportSearchManyDto);
		if (reqHd.getDeleted() == (byte)2) {
			reqHd.setDeleted(null);
		}
		if (listResDt.size() > 0) {
			reportSearchManyDto.setResultCode("000");
		} else {
			reportSearchManyDto.setResultCode("001");
		}
		return;
	}
}
