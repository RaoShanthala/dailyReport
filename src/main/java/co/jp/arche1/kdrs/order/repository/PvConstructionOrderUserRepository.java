package co.jp.arche1.kdrs.order.repository;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class PvConstructionOrderUserRepository {

	private Integer privConstId;
	private String privConstCode;
	private String privConstName;
	private Integer userId;
	private String userName;
	private String loginUser;
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


