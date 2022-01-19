package co.jp.arche1.kdrs.usermaintenance.repository;

import lombok.Data;

@Data
public class PvCompanyUserAuthorityRepository {

	private Integer userId;
	private Integer companyId;
    private Byte authorityType;
    private Byte status;
    private String name;

}