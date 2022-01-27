package co.jp.arche1.kdrs.companymaintenance.dto;

import java.io.Serializable;

import co.jp.arche1.kdrs.common.BaseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper=false)
@Data
public class CompanyInsertDto extends BaseDto {
	// static final long serialVersionUIDが必要
	private static final long serialVersionUID = 1L;

	// 引数なしコンストラクタの定義
	public CompanyInsertDto() {
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
		private String companyCode;
		private String companyName;
		private String companyNameKana;
		private String phone;
		private String prefacture;
		private String city;
		private String streetNumber;
		private String buildingName;
	}

}
