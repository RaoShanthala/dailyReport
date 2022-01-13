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
public class UserUpdateDto extends BaseDto {
	// static final long serialVersionUIDが必要
	private static final long serialVersionUID = 1L;

	// 引数なしコンストラクタの定義
	public UserUpdateDto() {
		reqHd = new RequestHd();
		reqDt = new ArrayList<RequestDt>();

    	super.setTranId(this.getClass().getName());
    }

	// プロパティ(メンバ変数)の宣言
	private RequestHd reqHd;
	private List<RequestDt> reqDt ;

	@Data
	public static class RequestHd implements Serializable {
	    // static final long serialVersionUIDが必要
	    private static final long serialVersionUID = 1L;

	    // 引数なしコンストラクタの定義
	    public RequestHd() {}

		// プロパティ(メンバ変数)の宣言
		private Integer userId;
		private LocalDate startDate;
		private LocalDate endDate;
		private String loginUser;
		private String name;
		private String password;
		private String email;
		private LocalDateTime updDatetime;
	}
	@Data
	public static class RequestDt implements Serializable {
	    // static final long serialVersionUIDが必要
	    private static final long serialVersionUID = 1L;

	    // 引数なしコンストラクタの定義
	    public RequestDt() {}

		// プロパティ(メンバ変数)の宣言
		private Byte action;
		private Integer roleId;
		private Byte roleLevel;
		private LocalDate userRoleStartDate;
		private LocalDate userRoleEndDate;
		private LocalDateTime updDatetime;
	}
}