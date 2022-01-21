package co.jp.arche1.kdrs.usermaintenance.repository;

import lombok.Data;

@Data
public class PtCompanyUserAuthorityRepository {

	private Integer userId;
	private Integer companyId;
	private Byte authorityType;

}
