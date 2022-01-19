package co.jp.arche1.kdrs.usermaintenance.dto;

import java.io.Serializable;

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

    	super.setTranId(this.getClass().getName());
    }

	// プロパティ(メンバ変数)の宣言
	private RequestHd reqHd;

	@Data
	public static class RequestHd implements Serializable {
	    // static final long serialVersionUIDが必要
	    private static final long serialVersionUID = 1L;

	    // 引数なしコンストラクタの定義
	    public RequestHd() {}

		// プロパティ(メンバ変数)の宣言
	    private Integer userId;
	    private Integer companyId;
		private String sei;
		private String mei;
		private String seiKana;
		private String meiKana;
		private String password;
		private String email;
		private String phone;
		private String prefacture;
		private String city;
		private String streetNumber;
		private String buildingName;
		private int authority;
	}

}