package co.jp.arche1.kdrs.companymaintenance.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import co.jp.arche1.kdrs.common.BaseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
public class CompanySearchOneDto extends BaseDto {
	private static final long serialVersionUID = 1L;

	// 引数なしコンストラクタの定義
	public CompanySearchOneDto() {
		reqHd = new RequestHd();
		resDt = new ResponseDt();

		super.setTranId(this.getClass().getName());
	}

	// プロパティ(メンバ変数)の宣言
	private RequestHd reqHd;
	private ResponseDt resDt;
	private Object resDtTitle;

	@Data
	public static class RequestHd implements Serializable {
		// static final long serialVersionUIDが必要
		private static final long serialVersionUID = 1L;

		// 引数なしコンストラクタの定義
		public RequestHd() {
		}

		// プロパティ(メンバ変数)の宣言
		private Integer companyId;
	}

	@Data
	public static class ResponseDt implements Serializable {
		// static final long serialVersionUIDが必要
		private static final long serialVersionUID = 1L;

		// 引数なしコンストラクタの定義
		public ResponseDt() {
		}

		// プロパティ(メンバ変数)の宣言
		private Integer companyId;
		private String companyCode;
		private String companyName;
		private String companyNameKana;
		private String phone;
		private String prefacture;
		private String city;
		private String streetNumber;
		private String buildingName;
		private String deleted;
		private LocalDateTime createdAt;
		private LocalDateTime updatedAt;

	}
}
