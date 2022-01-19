package co.jp.arche1.kdrs.usermaintenance.repository;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class PvUserCompanyRepository {

	private Integer userId;

//	private LocalDate startDate;
//	private LocalDate endDate;

	private String mei;
	private String sei;
	private String meiKana;
	private String seiKana;
	private String password;
	private String email;

	private String phone;
	private String prefacture;
	private String city;
	private String streetNumber;
	private String buildingName;
	private Byte deleted;

	@JsonFormat(shape = JsonFormat.Shape.ANY, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime createdAt;
	@JsonFormat(shape = JsonFormat.Shape.ANY, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime updatedAt;

	private Integer companyId;
	private Byte status;
	private String companyCode;




}
