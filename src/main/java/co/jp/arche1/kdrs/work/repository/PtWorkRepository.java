package co.jp.arche1.kdrs.work.repository;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class PtWorkRepository {

	private Integer privConstId;
	private Integer workNo;
	private Integer orderNo;
	private String workCode;
	@JsonFormat(shape = JsonFormat.Shape.ANY, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime workDate;
	private String spot;
	private String outline;
	private String danger;
    private String safety;
	private String detail1;
	private String detail2;
	private String detail3;
	private String staffClient;
	private String leader;
	private Byte fire;
	private String numPerson;
	private String hours;
	private Byte deleted;
	@JsonFormat(shape = JsonFormat.Shape.ANY, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime createdAt;
	@JsonFormat(shape = JsonFormat.Shape.ANY, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime updatedAt;

}
