package co.jp.arche1.kdrs.common.security.dto;

//import java.time.LocalDateTime;
//import java.util.LinkedHashMap;
//import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ValidationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import co.jp.arche1.kdrs.common.exception.NotFoundItemException;

//import com.example.common.security.jwt.MyExceptionHandler;

// 例外が発生した際にHttpStatusを返す処理
@RestControllerAdvice
public class ErrorController {
//public class ErrorController extends MyExceptionHandler {

	private static final Logger log = LoggerFactory.getLogger(ErrorController.class);

    /**
     *
     * @param req
     * @param ex
     * @return
     */
    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)	// 400
    public Response handleValidationException(HttpServletRequest req, ValidationException ex){
        return Response.createErrorResponse(ex);
    }

    /**
    * 未認証のユーザーからのアクセスを拒否した際のエラー
    * @param req
    * @param ex
    * @return
    */
   @ExceptionHandler(AuthenticationException.class)
   @ResponseStatus(HttpStatus.UNAUTHORIZED)	// 401
   public Response handleUnAuthenticationException(HttpServletRequest req, ValidationException ex){
       return Response.createErrorResponse(ex);
   }
   // 実行時に ExceptionHandlerExceptionResolver でエラーが発生しこのメソッドで処理できない
   //@ExceptionHandler(AccessDeniedException.class)
   //@ResponseStatus(HttpStatus.UNAUTHORIZED)	// 401
   //public Response handleAccessDeniedException(HttpServletRequest req, ValidationException ex){
   //    return Response.createErrorResponse(ex);
   //}

    /**
     * 見つからなかった場合のエラー
     * @param request
     * @param ex
     * @return
     */
    @ExceptionHandler(NotFoundItemException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)	// 404
    public Response handleNotFoundItemException(HttpServletRequest request, NotFoundItemException ex){
        return Response.createErrorResponse(ex);
    }

    /**
     * 許可されないメソッドの実行
     * @param request
     * @param ex
     * @return
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)	// 403
    public Response handleMethodNotSupportedException(HttpServletRequest request, HttpRequestMethodNotSupportedException ex){
        return Response.createErrorResponse(ex);
    }

    /**
     * 上記で処理していないエラー
     *
     * @param request
     * @param ex
     * @return
     * @throws Exception
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)	// 500
    public Response handleException(HttpServletRequest request, Exception ex) throws Exception {
    //public ResponseEntity<Object> handleException(HttpServletRequest request, Exception ex) throws Exception {
    	if (ex instanceof AccessDeniedException) {
    		// @ExceptionHandler(AccessDeniedException.class)でエラーになり handleAccessDeniedException() で処理できないので
    		// AuthEntryPointJwt.commence()で未認証のユーザーからのアクセスを拒否した際のエラー応答を行うために再スローする
    		log.error("{}: {}", ex.getClass().getSimpleName(), ex.getMessage());
            //return new ResponseEntity<>(getBody(HttpStatus.UNAUTHORIZED, ex, ex.getMessage()), new HttpHeaders(), HttpStatus.UNAUTHORIZED);
    		//throw ex;
    		return Response.createErrorResponse(ex);
    	}
		log.error("{} : {}", ex.toString(), ex.getMessage());
        return Response.createErrorResponse(ex);
        //return new ResponseEntity<>(getBody(HttpStatus.INTERNAL_SERVER_ERROR, ex, ex.getMessage()), new HttpHeaders(), HttpStatus.UNAUTHORIZED);
    }
/*
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Response handleException(HttpServletRequest request, Exception ex){
        log.error(ex.getMessage(),ex);
        return Response.createErrorResponse(ex);
    }
*/
/*
    public Map<String, Object> getBody(HttpStatus status, Exception ex, String message) {

        log.error(message, ex);

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("message", message);
        //body.put("timestamp", new Date());
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("exception", ex.toString());

        Throwable cause = ex.getCause();
        if (cause != null) {
            body.put("exceptionCause", ex.getCause().toString());
        }
        return body;
    }
*/
}
