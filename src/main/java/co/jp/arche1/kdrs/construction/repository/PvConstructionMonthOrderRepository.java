package co.jp.arche1.kdrs.construction.repository;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class PvConstructionMonthOrderRepository {

	private Integer privConstId;
	private Integer companyId;
	private String privConstName;
	private Integer userId;
	private String sei;
	private String mei;
	private Integer orderNo;
	private String orderCode;
	private String orderTitle;
	@JsonFormat(shape = JsonFormat.Shape.ANY, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime orderDate;
	private String orderContents;
	private String orderCause;
	private String staff;
	private String staffClient;
    private Byte signature;
	private Byte deleted;
	@JsonFormat(shape = JsonFormat.Shape.ANY, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime createdAt;
	@JsonFormat(shape = JsonFormat.Shape.ANY, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime updatedAt;

}
