package co.jp.arche1.kdrs.common.security.dto;

import java.util.List;

public class JwtResponseDto {
	private String token;
	private String type = "Bearer";
	private Integer id;
	private String username;
	private String companyCode;
	private Integer companyId;
	//private byte status;
	private List<String> authorities;

	public JwtResponseDto(String accessToken, Integer id, String username, String companyCode,Integer companyId,  List<String> authorities) {
		this.token = accessToken;
		this.id = id;
		this.username = username;
		this.companyCode = companyCode;
		this.companyId = companyId;
		//this.status = status;
		this.authorities = authorities;
	}

	public String getAccessToken() {
		return token;
	}

	public void setAccessToken(String accessToken) {
		this.token = accessToken;
	}

	public String getTokenType() {
		return type;
	}

	public void setTokenType(String tokenType) {
		this.type = tokenType;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getCompanyCode() {
		return companyCode;
	}

	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	/*public byte getStatus() {
		return status;
	}

	public void setStatus(byte status) {
		this.status = status;
	} */

	public List<String> getAuthorities() {
		return authorities;
	}
}
