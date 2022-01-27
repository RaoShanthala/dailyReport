package co.jp.arche1.kdrs.companymaintenance.repository;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class PtCompanyRepository {

	private Integer companyId;
	private String companyCode;
	private String companyName;
	private String companyNameKana;
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

}
