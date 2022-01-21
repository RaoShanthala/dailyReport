package co.jp.arche1.kdrs.construction.repository;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class PtPrivconstRepository {

	private Integer privConstId;
	private Integer companyId;
	private String privConstCode;
	private String privConstName;
	private Integer constId;
	private Integer userId;
	private Integer maxReportNo;
	private Integer maxOrderNo;
	private Integer maxWorkNo;
	//private Integer maxImageNo;
	private Byte deleted;
	@JsonFormat(shape = JsonFormat.Shape.ANY, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime createdAt;
	@JsonFormat(shape = JsonFormat.Shape.ANY, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime updatedAt;

}
