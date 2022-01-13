package co.jp.arche1.kdrs.common.security.config;

import org.springframework.security.crypto.password.PasswordEncoder;

public final class MyNoOpPasswordEncoder implements PasswordEncoder {

	public String encode(CharSequence rawPassword) {
		return rawPassword.toString();
	}

	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		return rawPassword.toString().equals(encodedPassword);
	}

	//
	// Get the singleton {@link NoOpPasswordEncoder}.
	//
	public static PasswordEncoder getInstanceA() {
		return INSTANCE;
		//return new MyNoOpPasswordEncoder();
	}

	private static final PasswordEncoder INSTANCE = new MyNoOpPasswordEncoder();

	public MyNoOpPasswordEncoder() {
	}

}
