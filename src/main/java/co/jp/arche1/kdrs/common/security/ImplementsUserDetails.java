package co.jp.arche1.kdrs.common.security;

import java.util.Collection;
import java.util.Objects;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

//import co.jp.arche1.kpms.common.security.repository.LoginUserRepository;

public class ImplementsUserDetails implements UserDetails {
	private static final long serialVersionUID = 1L;

	private Integer id;

	private String username;

	private String companyCode;

	private Byte status;

	private Integer companyId;

	@JsonIgnore
	private String password;

	private Collection<? extends GrantedAuthority> authorities;

	// コンストラクタ
	public ImplementsUserDetails(Integer id, String username, String password, String companyCode,
			Integer companyId, Byte status, Collection<? extends GrantedAuthority> authorities) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.companyCode = companyCode;
		this.companyId = companyId;
		this.status = status;
		this.authorities = authorities;
	}

	public static ImplementsUserDetails build(Integer id, String username, String password, String companyCode,
			Integer companyId, Byte status,Collection<? extends GrantedAuthority> authorities) {

		// コンストラクタを実行してこのクラスのインスタンスを返す
		return new ImplementsUserDetails(id, username, password,companyCode,companyId, status, authorities);

	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	public Integer getId() {
		return id;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	public String getCompanyCode() {
		return companyCode;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public Byte getStatus() {
		return status;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		ImplementsUserDetails user = (ImplementsUserDetails) o;
		return Objects.equals(id, user.id);
	}
}
