package co.jp.arche1.kdrs.construction.repository;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class PvPrivconstUserRepository {

	private Integer companyId;
	private Integer privConstId;
	private String privConstCode;
	private String privConstName;
	private String sei;
	private String mei;

	private Integer constId;
	private Integer userId;
	//private String loginUser;
	private Integer maxReportNo;
	private Integer maxOrderNo;
	private Integer maxWorkNo;
	private Integer maxImageNo;
	private Byte deleted;
	@JsonFormat(shape = JsonFormat.Shape.ANY, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime createdAt;
	@JsonFormat(shape = JsonFormat.Shape.ANY, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime updatedAt;

}
