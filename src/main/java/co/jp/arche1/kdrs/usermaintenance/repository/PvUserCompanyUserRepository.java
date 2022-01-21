package co.jp.arche1.kdrs.usermaintenance.repository;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class PvUserCompanyUserRepository {

	private Integer userId;
	private Integer companyId;
	private Byte status;

	private String email;

	private String mei;
	private String sei;
	private String meiKana;
	private String seiKana;
	private String password;

	private String phone;
	private String prefacture;
	private String city;
	private String streetNumber;
	private String buildingName;
	private Byte deleted;

	private byte authorityType;
	private String authorityName;

	@JsonFormat(shape = JsonFormat.Shape.ANY, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime createdAt;
	@JsonFormat(shape = JsonFormat.Shape.ANY, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime updatedAt;
}
