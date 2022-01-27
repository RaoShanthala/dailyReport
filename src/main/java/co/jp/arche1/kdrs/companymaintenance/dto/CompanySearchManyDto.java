package co.jp.arche1.kdrs.companymaintenance.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import co.jp.arche1.kdrs.common.BaseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
public class CompanySearchManyDto extends BaseDto {
	// static final long serialVersionUIDが必要
	private static final long serialVersionUID = 1L;

	// 引数なしコンストラクタの定義
	public CompanySearchManyDto() {
		reqHd = new RequestHd();
		resDt = new ArrayList<ResponseDt>();
//		resDtTitle = new ResponseDtTitle();

		super.setTranId(this.getClass().getName());
	}

	// プロパティ(メンバ変数)の宣言
	private RequestHd reqHd;
	private List<ResponseDt> resDt;
//	private ResponseDtTitle resDtTitle;
	private Object resDtTitle;

	@Data
	public static class RequestHd implements Serializable {
		// static final long serialVersionUIDが必要
		private static final long serialVersionUID = 1L;

		// 引数なしコンストラクタの定義
		public RequestHd() {
		}

		// プロパティ(メンバ変数)の宣言
		private Integer superCompanyId;
		private String companyCode;
		private String companyName;
		//private Byte deleted;
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

