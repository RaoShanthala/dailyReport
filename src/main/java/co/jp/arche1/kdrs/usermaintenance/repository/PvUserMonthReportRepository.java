package co.jp.arche1.kdrs.usermaintenance.repository;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class PvUserMonthReportRepository {

	private Integer constId;
	private String constName;
	private Integer privConstId;
	private String privConstName;
	private Integer userId;
	private String sei;
	private String mei;
	private Integer reportNo;
	private String reportCode;
	private String personCode;
	@JsonFormat(shape = JsonFormat.Shape.ANY, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime reportDate;
	private String detail;
	private String constType;
	private String staff;
    private String numPerson;
	private String hours;
	private String earlyHours;
	private String overHours;
	private Byte deleted;
	@JsonFormat(shape = JsonFormat.Shape.ANY, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime createdAt;
	@JsonFormat(shape = JsonFormat.Shape.ANY, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime updatedAt;

}