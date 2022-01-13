package co.jp.arche1.kdrs.common;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.reflect.MethodSignature;
//import org.aspectj.lang.annotation.Before;
//import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DeadlockLoserDataAccessException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import co.jp.arche1.kdrs.common.exception.LogicalException;
import co.jp.arche1.kdrs.common.exception.OptimisticLockException;
import co.jp.arche1.kdrs.namecollection.mapper.PvResultMessageMapper;
import co.jp.arche1.kdrs.namecollection.repository.PvResultMessageRepository;

@Component
@Aspect
public class ManagingService {

	private static final Logger consoleLogger = LoggerFactory.getLogger(ManagingService.class);
	private static final Logger serviceLogger = LoggerFactory.getLogger("ServiceLog");

	// 引数なしコンストラクタの定義
	public ManagingService() {
	}

	private enum Direction {
		REQ("Request"),
		RES("Response"),
		REQHD("RequestHd"),
		RESHD("ResponseHd"),
		REQDT("$RequestDt"),	// RequestDtは、ListなのでAcualTypeの比較になるため、$を付ける。
		RESDT("$ResponseDt");	// ResponseDtは、ListなのでAcualTypeで比較になるため、$を付ける。
		private final String str;
		private final String string;
		Direction(String string){
			this.str = string.substring(0, 3);
			this.string = string;
		}
	}

/*
    @Pointcut("execution(* *..*Service.*(..))")
    public void myPointcut(){};

	@Before("com.example.common.LoggingAspect.myPointcut()")
	public void startLog(JoinPoint jp) {
		logger.debug("@Before                   : {}", jp.getSignature());
	}
*/
/*
	@After("execution(* *..*Service.*(..))")
	public void completeLog(JoinPoint jp) {
		logger.debug("@After                    : {}", jp.getSignature());

		 //引数を取得しログに出力
        logger.info("引数 : " + getArgStr(jp));
        ////戻り値を取得しログに出力
        //logger.info("戻り値 : " + returnValue);	//@AfterReturningのとき引数で戻り値を取得する。

        //メソッド名を取得しログに出力
        String methodName = ((org.aspectj.lang.reflect.MethodSignature)jp.getSignature()).getMethod().getName();
        logger.info("メソッド名 : " + methodName);

        //リクエストURLを取得しログに出力
        javax.servlet.http.HttpServletRequest request =
         	((org.springframework.web.context.request.ServletRequestAttributes)org.springframework.web.context.request.RequestContextHolder.getRequestAttributes()).getRequest();
        logger.info("リクエストURL : " + request.getRequestURL().toString());

        //終了ログを出力
        String signature = jp.getSignature().toString();
        logger.info("終了ログ : " + signature);
        logger.info("");
	}
*/
	@Autowired
	private PlatformTransactionManager tranMgr;

	//@Around("execution(* *..*Service.*(..))")
	// executionの引数の最初の*は戻り値、次のはクラス名とメソッド名、()内がメソッドの引数。「..」は明示しないという指定。
	@Around("execution(* co.jp.arche1.kdrs.*..*Service.*(..))")
	public Object aroundLog(ProceedingJoinPoint jp) throws Throwable {
		Object ret = null;
		//try {

        //クラスを取得
        //Class<? extends MethodSignature> cls = ((org.aspectj.lang.reflect.MethodSignature)jp.getSignature()).getClass();
	    //logger.info("Class : " + cls.toString());
        //メソッドを取得
        //java.lang.reflect.Method method  = ((org.aspectj.lang.reflect.MethodSignature)jp.getSignature()).getMethod();
        //logger.info("Method : " + method.toString());

        Integer retryCount = 0;	boolean retryDeadLock;

        do {

        	DefaultTransactionDefinition tranDef = new DefaultTransactionDefinition();
    		TransactionStatus tranSts = tranMgr.getTransaction(tranDef);
    		LocalDateTime reqDatetime = LocalDateTime.now();
    		//MutableBoolean loginUserService = new MutableBoolean(false);
    		MutableBoolean notInheritBaseDto = new MutableBoolean(false);

    		retryDeadLock = false;

			try {

        		//引数を取得しログに出力
				serviceLogger.info("{} : {} : {}", jp.getSignature(), Direction.REQ.str, getArgStr(jp, Direction.REQ, reqDatetime, notInheritBaseDto));

        		ret = jp.proceed();

        		if (notInheritBaseDto.getValue()) {
        			tranMgr.rollback(tranSts);
        		} else {
	        		String resultCode = getRsultCodeAndSetResultMessage(jp);
	        		if (resultCode.equals("000")) {
	        			tranMgr.commit(tranSts);
	        		} else {
	        			//throw new LogicalException();
	            		tranMgr.rollback(tranSts);
	        		}
        		}

        	} catch (SQLException sqlex) {
        	    if ("40001".equals((sqlex).getSQLState())) {
        	    	// デッドロックは、DeadlockLoserDataAccessExceptionが発生することは確認したが、SQLExceptionが発生しSQLStateが40001になるケースは未確認である。
            		//exceptionLogger.error
        			serviceLogger.info("Dead Lock SQLException ! sqlex.getClass().getSimpleName() = {}, retryCount = {}, sqlex.toString() = {}, sqlex.getStackTrace()[0].toString() = {}",
        					sqlex.getClass().getSimpleName(), retryCount, sqlex.toString(), sqlex.getStackTrace()[0].toString());
        			consoleLogger.info("\njp.getSignature() ==> {} : Dead Lock SQLException ! sqlex.getClass().getSimpleName() = {}, retryCount = {}, sqlex.toString() = {}, sqlex.getStackTrace()[0].toString() = {}",
        					jp.getSignature(), sqlex.getClass().getSimpleName(), retryCount, sqlex.toString(), sqlex.getStackTrace()[0].toString());

        	        // デッドロックが発生すると、DB側でロールバックされているはずだが、DeadlockLoserDataAccessExceptionが発生したときに
        			// rollbackを実行しないとリトライ処理がオートコミットになってしまうのを確認したので、次のrollbackを実行する。
            		tranMgr.rollback(tranSts);
	        		if (retryCount++ < 3) {
	        			retryDeadLock = true;
	        			setRsultToDto(jp, "910", sqlex.getMessage());
	        	    	Thread.sleep(1000);		// 1秒待機
	        		} else {
	        			retryDeadLock = false;
	        			setRsultToDto(jp, "900", sqlex.getMessage());
	        			//setExceptionToDto(jp, sqlex);
	        	    	//throw sqlex;
	        		}
        	    } else  {
            		tranMgr.rollback(tranSts);
        	        //exceptionLogger.error("\njp.getSignature() => {}\nex.toString() => {}\nex.getStackTrace()[0].toString() => {}",
	        		//		jp.getSignature(), sqlex.toString(), sqlex.getStackTrace()[0].toString());
            		consoleLogger.error("\njp.getSignature() => {}\nex.toString() => {}\nex.getStackTrace()[0].toString() => {}",
	        				jp.getSignature(), sqlex.toString(), sqlex.getStackTrace()[0].toString());
        	    	// デッドロック以外SQLException
        			//setRsultToDto(jp, "", sqlex.getMessage());
        			setExceptionToDto(jp, sqlex);
        	    	retryDeadLock = false;
        	    	//throw sqlex;
        	    }
        	} catch (DeadlockLoserDataAccessException deadlockEx) {
        		//exceptionLogger.error
        		serviceLogger.info("DeadlockLoserDataAccessException ! deadlockEx.getClass().getSimpleName() = {}, retryCount = {}, deadlockEx.toString() = {}, deadlockEx.getStackTrace()[0].toString() = {}",
        				deadlockEx.getClass().getSimpleName(), retryCount, deadlockEx.toString(), deadlockEx.getStackTrace()[0].toString());
        		consoleLogger.info("\njp.getSignature() ==> {} : DeadlockLoserDataAccessException ! deadlockEx.getClass().getSimpleName() = {}, retryCount = {}, deadlockEx.toString() = {}, deadlockEx.getStackTrace()[0].toString() = {}",
    					jp.getSignature(), deadlockEx.getClass().getSimpleName(), retryCount, deadlockEx.toString(), deadlockEx.getStackTrace()[0].toString());
    	        // デッドロックが発生すると、DB側でロールバックされているはずだが、次のrollbackを実行しないとリトライ処理がオートコミットになってしまう。
        		tranMgr.rollback(tranSts);
        		if (retryCount++ < 3) {
        			retryDeadLock = true;
        			setRsultToDto(jp, "910", deadlockEx.getMessage());
        	    	Thread.sleep(1000);		// 1秒待機
        		} else {
        			retryDeadLock = false;
        			setRsultToDto(jp, "900", deadlockEx.getMessage());
        			//setExceptionToDto(jp, deadlockEx);
        	    	//throw sqlex;
        		}
        	} catch (OptimisticLockException optLockEx) {
        		tranMgr.rollback(tranSts);
        		setRsultToDto(jp, "901", optLockEx.getMessage());
        	} catch (LogicalException logicalEx) {
        		tranMgr.rollback(tranSts);
        	} catch (Exception ex) {
        		tranMgr.rollback(tranSts);
        		//logger.error("{} : {}", ex.toString(), ex.getMessage());
    	        //exceptionLogger.error("\njp.getSignature() => {}\nex.toString() => {}\nex.getStackTrace()[0].toString() => {}",
        		//		jp.getSignature(), ex.toString(), ex.getStackTrace()[0].toString());
        		consoleLogger.error("\njp.getSignature() => {}\nex.toString() => {}\nex.getStackTrace()[0].toString() => {}",
    	        				jp.getSignature(), ex.toString(), ex.getStackTrace()[0].toString());
        		boolean result = setExceptionToDto(jp, ex);
        		if (result == false) {
        			// BaseDto の ResultCode、ResultMessge に例外情報をセットできなかったときは例外をスローする
        			// 例外をスローすると、co.jp.arche1.kpms.common.security.dto.ErrorController.handleException()で捕捉して、
        			// @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // 500　がクライアントに返される。
        			throw ex;
        		}
        	} finally {
        		//引数を取得しログに出力
        		serviceLogger.info("{} : {} : {}", jp.getSignature(), Direction.RES.str, getArgStr(jp, Direction.RES, reqDatetime, notInheritBaseDto));
        	}
        } while (retryDeadLock == true);

			//logger.debug("AfterReturning by @Around : {} ret:{}", jp.getSignature(), ret);

		//} catch (Throwable t) {
		//	logger.debug("AfterThrowing by @Around  : {}", jp.getSignature(), t);
		//	throw t;
		//} finally {
		//	//logger.debug("After by @Around          : {}", jp.getSignature());
		//}
		return ret;
	}

    /**
     * 指定したメソッドの引数の文字列を取得する
     *
     * @param jp 横断的な処理を挿入する場所
     * @param direction Request、Responseを表す
     * @return 指定したメソッドの引数
     */
    private String getArgStr(JoinPoint jp, Direction direction, LocalDateTime reqDatetime, MutableBoolean notInheritBaseDto) {
        StringBuilder sb = new StringBuilder();
        Object[] args = jp.getArgs();

        if(args.length > 0){
	        if (args[0] instanceof BaseDto) {
	        	BaseDto baseDto = (BaseDto)args[0];
	        	if (direction.equals(Direction.REQ)) {
	        		// サービス実行前の要求時刻、ユーザ、端末の設定
	        		baseDto.setReqDatetime(reqDatetime);
	        		// スレッドローカルオブジェクト(SecurityContextHolder)から認証情報を取り出す
	        		SecurityContext ctx = SecurityContextHolder.getContext();
	                if (null != ctx) {
	                	String user = ctx.getAuthentication().getName();
	                    //System.out.println(user);
	                    baseDto.setUser(user);
	                }
	        		// スレッドローカルオブジェクト(RequestContextHolder)からリクエスト情報を取り出す
	                HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
	                baseDto.setTerminal(req.getRemoteAddr());
	        	}
	        	// サービス実行前後の端末、ユーザ、要求時刻の出力準備
	        	sb.append("Terminal=").append(baseDto.getTerminal());
	        	sb.append(", User=").append(baseDto.getUser());
	        	sb.append(", ReqDatetime=").append(baseDto.getReqDatetime()).append(", ");
	        	if (direction.equals(Direction.RES)) {
	        		// サービス実行後の応答時刻、要求からの経過時間、結果コード、結果メッセージの出力準備
	        		baseDto.setResDatetime(LocalDateTime.now());
		        	sb.append("ResDatetime=").append(baseDto.getResDatetime());
		        	sb.append(", ElapsedTime=").append(baseDto.getElapsedTime());
		        	sb.append(", ResultCode=").append(baseDto.getResultCode());
		        	sb.append(", ResultMessage=").append(baseDto.getResultMessage()).append(", ");
	        	}

	            for(Object arg : args) {
	                for (Field field : arg.getClass().getDeclaredFields()) {
	                //for (Field field : arg.getClass().getFields()) {
	                    try {
	                        field.setAccessible(true);
	                        String instanceName = field.getName();
	                        String typeName = field.getType().getSimpleName();
	                        //String sname = field.get(arg).getClass().getSimpleName();
	                        //System.out.println("sname=" + typeName + ", instanceName=" + instanceName);
	                    	if (direction.equals(Direction.REQ)) {
	                    		// サービス実行前
	                            if (typeName.startsWith(Direction.REQ.string)) {
	                            	// オブジェクトの型がRequest
	                        		sb.append(instanceName).append("=").append(field.get(arg)).append(", ");
	                            } else if (typeName.equals(List.class.getSimpleName())) {
	                            	// オブジェクトの型がList
	                            	List<?> objList = (List<?>)field.get(arg);

	                            	if (objList.isEmpty() == false) {
		                            	ParameterizedType paramType = (ParameterizedType)field.getGenericType();
		                            	Type actualType = paramType.getActualTypeArguments()[0];
		                            	if (actualType.toString().lastIndexOf(Direction.REQDT.string) >= 0) {
		                            		sb.append(instanceName).append("=").append(field.get(arg)).append(", ");
		                            	}
	                            	}

	                            	//Type type = field.getGenericType();
	                            	//System.out.println(type + " : " + type.getClass());

	                        		//type = ((GenericArrayType)type).getGenericComponentType();
	                        		//System.out.println(type + " : " + type.getClass());

	                            }
	                    	} else if(direction.equals(Direction.RES)) {
	                    		// サービス実行後
	                            if (typeName.startsWith(Direction.RES.string)) {
		                            if (typeName.startsWith(Direction.RESHD.string)) {
	        	                        sb.append(instanceName).append("=").append(field.get(arg)).append(", ");
		                            } else {
		                            	// オブジェクトの型がResponseDtの場合（通常、ResponseDtのtypeNameはListになるのでこの処理は行わない）
	                                	if (serviceLogger.isDebugEnabled()) {
	                                		// デバッグモードならオブジェクトの内容の出力準備
		        	                        sb.append(instanceName).append("=").append(field.get(arg)).append(", ");
	                                	} else {
		                            		// デバッグモードでないならオブジェクトの内容を省略して出力準備
	                                		String strField = field.get(arg).toString();
	                                		if (strField.length() > 128) {
	                                			strField = strField.substring(0,128);
	                                		}
		    		                        sb.append(instanceName).append(strField).append("...)]").append(", ");
	                                	}
		                            }
                            	} else if (typeName.equals(List.class.getSimpleName())) {
	                            	// オブジェクトの型がListの場合（通常、ResponseDtはtypeNameはListになるのでResponseDtでこの処理を行う）
	                            	List<?> objList = (List<?>)field.get(arg);

	                            	if (objList.isEmpty() == false) {
		                            	ParameterizedType paramType = (ParameterizedType)field.getGenericType();
		                            	Type actualType = paramType.getActualTypeArguments()[0];
		                            	if (actualType.toString().lastIndexOf(Direction.RESDT.string) >= 0) {
		                                	if (serviceLogger.isDebugEnabled()) {
		                                		sb.append(instanceName).append("=").append(field.get(arg)).append(", ");
		                                	} else {
			                            		// デバッグモードでないならオブジェクトの内容を省略して出力準備
		                                		String strField = field.get(arg).toString();
		                                		if (strField.length() > 128) {
		                                			strField = strField.substring(0,128);
		                                		}
			    		                        sb.append(instanceName).append(strField).append("...)]").append(", ");
		                                	}
		                            	}
	                            	}
		                        }
	                    	}
	                    } catch (IllegalAccessException e) {
	                        sb.append(field.getName()).append("=Access Denied").append(", ");
	                    }
	                }
	            }

	        } else {
	        	notInheritBaseDto.setValue(true);
	        	// args[0]がBaseDto型でないのは LoginUserService と見做す
        		// スレッドローカルオブジェクト(SecurityContextHolder)から認証情報はログイン前なので取得出来ない
        		//SecurityContext ctx = SecurityContextHolder.getContext();
        		//String user = "";
                //if (null != ctx) {
                //	if (ctx.getAuthentication() != null) {
                //		user = ctx.getAuthentication().getName();
                //    //System.out.println(user);
                //}
	        	// LoginUserServiceのargs[0]にはログイン名が入っているのでこれを使う
                String user = args[0].toString();
        		// スレッドローカルオブジェクト(RequestContextHolder)からリクエスト情報を取り出す
                HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

	        	//String classSimpleName = jp.getTarget().getClass().getSimpleName();
	        	//if ( classSimpleName.equals("LoginUserService")) {
	        	//	System.out.println("classSimpleName=" + classSimpleName + ", direction=" + direction.toString());
	        	//	if (direction.equals(Direction.REQ)) {
	        	//		String commonName = getCert(req);
	        	//		System.out.println("classSimpleName=" + classSimpleName + ", direction=" + direction.toString() + ", commonName=" + commonName);
	        	//	}
	        	//}

	        	// サービス実行前後の端末、ユーザ、要求時刻の出力準備
	        	sb.append("Terminal=").append(req.getRemoteAddr());
	        	sb.append(", User=").append(user);
	        	sb.append(", ReqDatetime=").append(reqDatetime).append(", ");

	        	if (direction.equals(Direction.RES)) {
	        		// サービス実行後の応答時刻、要求からの経過時間、結果コード、結果メッセージの出力準備
	        		LocalDateTime resDatetime= LocalDateTime.now();
		        	sb.append("ResDatetime=").append(resDatetime);

		        	java.time.Duration duration = java.time.Duration.between(reqDatetime, resDatetime);
		    		//0時0分にDurationを加える
		    		java.time.LocalTime t = java.time.LocalTime.MIDNIGHT.plus(duration);
		    		String s = java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss.SSSSSS").format(t);
		        	sb.append(", ElapsedTime=").append(s).append(", ");
		        	//sb.append(", ResultCode=").append(baseDto.getResultCode());
		        	//sb.append(", ResultMessage=").append(baseDto.getResultMessage()).append(", ");
	        	}
	        }

            if (sb.length() >= 2) {
            	if (sb.substring(sb.length() - 2, sb.length()).equals(", ")) {
            		sb.delete(sb.length() - 2, sb.length());
            	}
            }

        }else{
            sb.append("(引数なし)");
        }
        return sb.toString();
    }

    // Dtoのリザルトコードから取得したリザルトメッセージをDtoに設定する。
    private String getRsultCodeAndSetResultMessage(JoinPoint jp) {
        Object[] args = jp.getArgs();
        String resultCode = "";

        if(args.length > 0){
	        if (args[0] instanceof BaseDto) {
	        	BaseDto baseDto = (BaseDto)args[0];
	        	resultCode = baseDto.getResultCode();
	        	if (StringUtils.isNotEmpty(resultCode)) {
	        		String nameLong = getResultMessageRepository(args[0].getClass().getSimpleName(), resultCode);
		        	if (StringUtils.isNotEmpty(nameLong)) {
		        		baseDto.setResultMessage(nameLong);
		        	}
	        	}
	        }
        }
        return resultCode;
    }

    // 引数のリザルトコードから取得したリザルトメッセージに引数の例外メッセージを加えてDtoに設定する。
    private void setRsultToDto(JoinPoint jp, String resultCode, String exMessage) {
        Object[] args = jp.getArgs();

        if(args.length > 0){
	        if (args[0] instanceof BaseDto) {
	        	BaseDto baseDto = (BaseDto)args[0];
	        	if (StringUtils.isNotEmpty(resultCode)) {
		        	baseDto.setResultCode(resultCode);
		        	String nameLong = getResultMessageRepository(args[0].getClass().getSimpleName(), resultCode);
		        	if (StringUtils.isNotEmpty(nameLong)) {
		        		baseDto.setResultMessage(MessageFormat.format(nameLong, exMessage));
		        	} else {
			        	baseDto.setResultMessage(exMessage);
		        	}
	        	} else {
		        	baseDto.setResultMessage(exMessage);
	        	}
	        }
        }
    }

	@Autowired
	PvResultMessageMapper pvResultMessageMapper;

	// リザルトコードからネーム集のリザルトメッセージを返す
    private String getResultMessageRepository(String dtoName, String resultCode) {
    	String nameLong = "";

    	try {
    		PvResultMessageRepository pvResultMessageRepository = pvResultMessageMapper.selectOne(dtoName, resultCode);

	    	if (pvResultMessageRepository != null) {
	    		nameLong = pvResultMessageRepository.getMsgNameLong();
		    	if (nameLong == null) {
		    		nameLong = "";
		    	}
	    	}
    	} catch (Exception ex) {
    		nameLong = ex.toString();
    	}
    	return nameLong;
    }
	// 例外からリザルトコード、リザルトメッセージをセットする
    private boolean setExceptionToDto(JoinPoint jp, Exception ex) {

        boolean result = false;
    	Object[] args = jp.getArgs();

		String exMessage = String.format("%s, %s", ex.toString(), ex.getStackTrace()[0].toString());
        if(args.length > 0){
	        if (args[0] instanceof BaseDto) {
	        	BaseDto baseDto = (BaseDto)args[0];

	        	baseDto.setResultCode("Exception");
	        	baseDto.setResultMessage(exMessage);
	        	result = true;
	        }
        }
    	return result;
    }

}
