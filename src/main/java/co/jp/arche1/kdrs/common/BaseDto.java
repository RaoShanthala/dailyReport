package co.jp.arche1.kdrs.common;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;

//import lombok.EqualsAndHashCode;

//@EqualsAndHashCode(callSuper=false)
@Data
public class BaseDto implements Serializable {
    // static final long serialVersionUIDが必要
    private static final long serialVersionUID = 1L;

    // 引数なしコンストラクタの定義
	public BaseDto() {
//		reqHd = new RequestHd();
/*
		resHd = new ResponseHd();
		reqDt = new ArrayList<RequestDt>();
		resDt = new ArrayList<ResponseDt>();
		resHdTitle = new ResponseHdTitle();
		resDtTitle = new ResponseDtTitle();
*/
    	//this.tranId = this.getClass().getName();
		this.reqDatetime = LocalDateTime.now();
	}
	// プロパティ(メンバ変数)の宣言
//	protected RequestHd reqHd;
	/*
	protected ResponseHd resHd;
	protected List<RequestDt> reqDt ;
	protected List<ResponseDt> resDt ;
	protected ResponseHdTitle resHdTitle;
	protected ResponseDtTitle resDtTitle;
*/
    private String tranId;
    private String resultCode;
    private String resultMessage;
    private String user;
    private String terminal;
    private LocalDateTime reqDatetime;
    private LocalDateTime resDatetime;
/*
	public class RequestHd implements Serializable {
	    // static final long serialVersionUIDが必要
//	    private static final long serialVersionUID = 1L;

	    // 引数なしコンストラクタの定義
	    public RequestHd() {}
	}
*/
	/*
	public class RequestDt implements Serializable {
	    // static final long serialVersionUIDが必要
	    private static final long serialVersionUID = 1L;

	    // 引数なしコンストラクタの定義
	    public RequestDt() {}
	}
	public static class ResponseHd implements Serializable {
	    // static final long serialVersionUIDが必要
	    private static final long serialVersionUID = 1L;

	    // 引数なしコンストラクタの定義
	    public ResponseHd() {}
	}
	public static class ResponseDt implements Serializable {
	    // static final long serialVersionUIDが必要
	    private static final long serialVersionUID = 1L;

	    // 引数なしコンストラクタの定義
	    public ResponseDt() {}
	}

	public static class ResponseDtTitle implements Serializable {
	    // static final long serialVersionUIDが必要
	    private static final long serialVersionUID = 1L;

	    // 引数なしコンストラクタの定義
	    public ResponseDtTitle() {}
	}

	public static class ResponseHdTitle implements Serializable {
	    // static final long serialVersionUIDが必要
	    private static final long serialVersionUID = 1L;

	    // 引数なしコンストラクタの定義
	    public ResponseHdTitle() {}
	}
*/
	/*
	// プロパティのsetter、getter
	protected RequestHd getReqHd() {
		return reqHd;
	}
	*/
/*
	public void setReqHd(RequestHd reqHd) {
		this.reqHd = reqHd;
	}
	public List<RequestDt> getReqDt() {
		return reqDt;
	}
	public void setReqDt(List<RequestDt> reqDt) {
	    this.reqDt = reqDt;
	}

	public ResponseHd getResHd() {
		return resHd;
	}
	public void setResHd(ResponseHd resHd) {
		this.resHd = resHd;
	}
	public List<ResponseDt> getResDt() {
		return resDt;
	}
	public void setResDt(List<ResponseDt> resDt) {
	    this.resDt = resDt;
	}
	public ResponseHdTitle getResHdTitle() {
		return resHdTitle;
	}
	public ResponseDtTitle getResDtTitle() {
		return resDtTitle;
	}
*/
 /*
	// プロパティのsetter、getter
	public String getResultCode() {
		return resultCode;
	}
	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}
	public String getResultMessage() {
		return resultMessage;
	}
	public void setResultMessage(String resultMessage) {
		this.resultMessage = resultMessage;
	}

	public String getTranId() {
		return tranId;
	}
	public void setTranId(String tranId) {
		this.tranId = tranId;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getTerminal() {
		return terminal;
	}
	public void setTerminal(String terminal) {
		this.terminal = terminal;
	}
	public Date getReqDatetime() {
		return reqDatetime;
	}
	public void setReqDatetime(Date reqDatetime) {
		this.reqDatetime = reqDatetime;
	}
	public Date getResDatetime() {
		return resDatetime;
	}
	public void setResDatetime(Date resDatetime) {
		this.resDatetime = resDatetime;
	}
*/
	public String getElapsedTime() {

		//SimpleDateFormat format = new SimpleDateFormat("yy/MM/dd HH:mm:ss.SSS");
		if (this.resDatetime == null) {
			this.resDatetime = LocalDateTime.now();
		}

		//in milliseconds
		//long diff = this.resDatetime.getTime() - this.reqDatetime.getTime();
		//return String.format("%02d:%02d.%03d",
		//		diff /(60 *  1000) % 60,	// diffMinutes
		//		diff /1000 % 60,			// diffSeconds
		//		diff % 1000);				// diffMilliSeconds

		//long diffDays = diff /(24 *  60 *  60 *  1000);
		//long diffHours = diff /(60 *  60 *  1000) % 24;
		//long diffMinutes = diff /(60 *  1000) % 60;
		//long diffSeconds = diff /1000 % 60;
		//long diffMilliSeconds = diff % 1000;

		java.time.Duration duration = java.time.Duration.between(this.reqDatetime, this.resDatetime);

		//0時0分にDurationを加える
		java.time.LocalTime t = java.time.LocalTime.MIDNIGHT.plus(duration);
		String s = java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss.SSSSSS").format(t);
		return s;
	}
	/*
	public String getTranTime() {
		java.time.Duration duration = java.time.Duration.between(this.reqDatetime, this.resDatetime);
		//0時0分にDurationを加える
		java.time.LocalTime t = java.time.LocalTime.MIDNIGHT.plus(duration);

	    String s =String.format("ReqTime = %s, ResTime = %s, Duration = %s",
			    java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss.SSSSSS").format(this.reqDatetime),
			    java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss.SSSSSS").format(this.resDatetime),
			    java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss.SSSSSS").format(t));
	    return s;

	}
	*/
}
