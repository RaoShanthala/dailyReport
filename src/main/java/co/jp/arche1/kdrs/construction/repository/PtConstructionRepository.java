package co.jp.arche1.kdrs.construction.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@Data
public class PtConstructionRepository {

	private Integer constId;
	private Integer companyId;
	private String constCode;
	private String constName;
	private Byte deleted;
	private Integer userId;

	@JsonFormat(shape = JsonFormat.Shape.ANY, pattern = "yyyy-MM-dd")
	private LocalDate startDate;
	@JsonFormat(shape = JsonFormat.Shape.ANY, pattern = "yyyy-MM-dd")
	private LocalDate endDate;

	//@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	@JsonFormat(shape = JsonFormat.Shape.ANY, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime createdAt;
	//@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	@JsonFormat(shape = JsonFormat.Shape.ANY, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime updatedAt;

}
