package co.jp.arche1.kdrs.common.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
//import org.springframework.web.servlet.HandlerExceptionResolver;

// 未認証のユーザーからのアクセスを拒否した際のエラー応答を行うためのハンドラー
// 	@ExceptionHandler(Exception.class)で再スローされてからここでの処理を行う
//
// LoginController.authenticateUserメソッドで例外がスローされるとこのcommenceメソッドが呼び出されるが、
//  例外をスローするのを止めて、authenticateUserメソッドからJSONでエラーを返すように変更して他のエラーとエラー構造を統一した。
//  よって、原則、このcommenceメソッドでの処理はされない。
@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {

	private static final Logger logger = LoggerFactory.getLogger(AuthEntryPointJwt.class);

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		logger.error("Unauthorized error: {}", authException.getMessage());
		response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized: ユーザ名、またはパスワードが不正です。");
	}

/*
	@Autowired
	@Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver resolver;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
    		AuthenticationException exception) throws IOException, ServletException {
        resolver.resolveException(request, response, null, exception);
    }
*/

}

