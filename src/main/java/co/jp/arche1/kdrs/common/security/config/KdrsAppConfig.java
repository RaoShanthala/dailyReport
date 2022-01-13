package co.jp.arche1.kdrs.common.security.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "kdrs.app")
public class KdrsAppConfig {

	private String jwtSecret;
    private String jwtExpirationMs;

	public void setJwtSecret(String jwtSecret) {
		this.jwtSecret = jwtSecret;
	}
	public String getJwtSecret() {
		return jwtSecret;
	}

	public void setJwtExpirationMs(String jwtExpirationMs) {
		this.jwtSecret = jwtExpirationMs;
	}
	public String getJwtExpirationMs() {
		return jwtExpirationMs;
	}
}
