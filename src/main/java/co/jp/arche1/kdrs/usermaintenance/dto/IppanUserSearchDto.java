package co.jp.arche1.kdrs.usermaintenance.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import co.jp.arche1.kdrs.common.BaseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper=false)
@Data
public class IppanUserSearchDto extends BaseDto {
	// static final long serialVersionUIDが必要
	private static final long serialVersionUID = 1L;

	// 引数なしコンストラクタの定義
	public IppanUserSearchDto() {
		reqHd = new RequestHd();
		resDt = new ArrayList<ResponseDt>();
//		resDtTitle = new ResponseDtTitle();

    	super.setTranId(this.getClass().getName());
    }

	// プロパティ(メンバ変数)の宣言
	private RequestHd reqHd;
	private List<ResponseDt> resDt ;
//	private ResponseDtTitle resDtTitle;
	private Object resDtTitle;

	@Data
	public static class RequestHd implements Serializable {
	    // static final long serialVersionUIDが必要
	    private static final long serialVersionUID = 1L;

	    // 引数なしコンストラクタの定義
	    public RequestHd() {}

		// プロパティ(メンバ変数)の宣言
	    private Integer companyId;
		private String sei;
		private String mei;
		private Byte deleted;
	}
	@Data
	public static class ResponseDt implements Serializable {
	    // static final long serialVersionUIDが必要
	    private static final long serialVersionUID = 1L;

	    // 引数なしコンストラクタの定義
	    public ResponseDt() {}

		// プロパティ(メンバ変数)の宣言
		private Integer userId;
		private Integer companyId;
		private String status;

		private String email;
		private String sei;
		private String mei;
		private String seiKana;
		private String meiKana;
		private String phone;
		private String prefacture;
		private String city;
		private String streetNumber;
		private String buildingName;
		private byte deleted;
		private byte authorityType;
		private String authorityName;
		private LocalDateTime createdAt;
		private LocalDateTime updatedAt;

	}
//	@Data
//	public static class ResponseDtTitle implements Serializable {
//	    // static final long serialVersionUIDが必要
//	    private static final long serialVersionUID = 1L;
//
//	    // 引数なしコンストラクタの定義
//	    public ResponseDtTitle() {}
//
//		// プロパティ(メンバ変数)の宣言
//		private final String userId = "ユーザID";
//		private final String startDate = "開始日";
//		private final String endDate = "終了日";
//		private final String loginUser = "ログインユーザ";
//		private final String name = "氏名";
//		private final String email = "メールアドレス";
//		private final String roleId = "業務ID";
//		private final String roleName = "業務名";
//		private final String roleLevel = "権限レベル";
//		private final String roleLevelNameShort = "権限レベル名";
//		private final String userRoleStartDate = "ユーザ業務開始日";
//		private final String userRoleEndDate = "ユーザ業務終了日";
//	}

}
