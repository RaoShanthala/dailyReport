package co.jp.arche1.kdrs.usermaintenance.repository;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class PtUserRepository {

	private Integer userId;

	private String mei;
	private String sei;
	private String seiKana;
	private String meiKana;
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

}