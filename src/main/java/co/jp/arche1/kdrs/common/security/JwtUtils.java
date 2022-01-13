package co.jp.arche1.kdrs.common.security;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import co.jp.arche1.kdrs.common.security.config.KdrsAppConfig;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;



//JWTの認可処理を使用するためのクラス
@Component
public class JwtUtils {
	private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

	@Autowired
	KdrsAppConfig kdrsAppConfig;

	@Value("${kdrs.app.jwtSecret}")
	private String jwtSecret;

	@Value("${kdrs.app.jwtExpirationMs}")
	private int jwtExpirationMs;

	// 引数のAuthenticationからJwt（トークン）を取得する
	public String generateJwtToken(Authentication authentication) {

		ImplementsUserDetails userPrincipal = (ImplementsUserDetails) authentication.getPrincipal();
		String subject = String.format("%s%s%s",userPrincipal.getUsername().trim(), String.valueOf(Character.LINE_SEPARATOR),userPrincipal.getCompanyCode().trim());

		return Jwts.builder()
				.setSubject(subject)
				.setIssuedAt(new Date())
				.setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
				.signWith(SignatureAlgorithm.HS512, jwtSecret)
				.compact();
	}

	// 引数の文字列（トークン）からユーザ名を取得する
	public String getUserNameFromJwtToken(String token) {
		return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
	}

	// 引数の文字列が正しいトークンであるか確認する
	public boolean validateJwtToken(String authToken) {
		try {
			Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
			return true;
		} catch (SignatureException e) {
			logger.error("Invalid JWT signature: {}", e.getMessage());
		} catch (MalformedJwtException e) {
			logger.error("Invalid JWT token: {}", e.getMessage());
		} catch (ExpiredJwtException e) {
			logger.error("JWT token is expired: {}", e.getMessage());
		} catch (UnsupportedJwtException e) {
			logger.error("JWT token is unsupported: {}", e.getMessage());
		} catch (IllegalArgumentException e) {
			logger.error("JWT claims string is empty: {}", e.getMessage());
		}

		return false;
	}
}
