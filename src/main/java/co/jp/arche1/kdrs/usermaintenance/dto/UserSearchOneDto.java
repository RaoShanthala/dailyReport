package co.jp.arche1.kdrs.usermaintenance.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import co.jp.arche1.kdrs.common.BaseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper=false)
@Data
public class UserSearchOneDto extends BaseDto {
	// static final long serialVersionUIDが必要
	private static final long serialVersionUID = 1L;

	// 引数なしコンストラクタの定義
	public UserSearchOneDto() {
		reqHd = new RequestHd();
		resHd = new ResponseHd();
		resDt = new ArrayList<ResponseDt>();
//		resDtTitle = new ResponseDtTitle();

    	super.setTranId(this.getClass().getName());
    }

	// プロパティ(メンバ変数)の宣言
	private RequestHd reqHd;
	private ResponseHd resHd;
	private List<ResponseDt> resDt ;
//	private ResponseHdTitle resHdTitle;
//	private ResponseDtTitle resDtTitle;
	private Object resHdTitle;
	private Object resDtTitle;

	@Data
	public static class RequestHd implements Serializable {
	    // static final long serialVersionUIDが必要
	    private static final long serialVersionUID = 1L;

	    // 引数なしコンストラクタの定義
	    public RequestHd() {}

		// プロパティ(メンバ変数)の宣言
		private Integer userId;
	}
	@Data
	public static class ResponseHd implements Serializable {
	    // static final long serialVersionUIDが必要
	    private static final long serialVersionUID = 1L;

	    // 引数なしコンストラクタの定義
	    public ResponseHd() {}

		// プロパティ(メンバ変数)の宣言
		private LocalDate startDate;
		private LocalDate endDate;
		private String loginUser;
		private String name;
		private String password;
		private String email;
		private LocalDateTime updDatetime;
	}
	@Data
	public static class ResponseDt implements Serializable {
	    // static final long serialVersionUIDが必要
	    private static final long serialVersionUID = 1L;

	    // 引数なしコンストラクタの定義
	    public ResponseDt() {}

		// プロパティ(メンバ変数)の宣言
		private Integer roleId;
		private String roleName;
		private Byte roleLevel;
		private String roleLevelNameShort;
		private LocalDate userRoleStartDate;
		private LocalDate userRoleEndDate;
		private LocalDateTime updDatetime;

	}
//	@Data
//	public static class ResponseHdTitle implements Serializable {
//	    // static final long serialVersionUIDが必要
//	    private static final long serialVersionUID = 1L;
//
//	    // 引数なしコンストラクタの定義
//	    public ResponseHdTitle() {}
//
//		// プロパティ(メンバ変数)の宣言
//		private final String startDate = "開始日";
//		private final String endDate = "終了日";
//		private final String loginUser = "ログインユーザ";
//		private final String name = "氏名";
//		private final String password = "パスワード";
//		private final String email = "メールアドレス";
//		private final String updDatetime = "更新日時";
//	}
//	@Data
//	public static class ResponseDtTitle implements Serializable {
//	    // static final long serialVersionUIDが必要
//	    private static final long serialVersionUID = 1L;
//
//	    // 引数なしコンストラクタの定義
//	    public ResponseDtTitle() {}
//
//		// プロパティ(メンバ変数)の宣言
//		private final String roleId = "業務ID";
//		private final String roleName = "業務名";
//		private final String roleLevel = "業務レベル";
//		private final String roleLevelNameShort = "業務レベル名";
//		private final String userRoleStartDate = "ユーザ業務開始日";
//		private final String userRoleEndDate = "ユーザ業務終了日";
//		private final String updDatetime = "更新日時";
//	}
}
