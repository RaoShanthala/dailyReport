package co.jp.arche1.kdrs.common.security.dto;

import javax.validation.constraints.NotBlank;

public class LoginRequestDto {
	@NotBlank
	//private String username;
	private String email;

	@NotBlank
	private String password;

	private String companyCode;

	/*public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	} */

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getCompanyCode() {
		return companyCode;
	}

	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}
}
