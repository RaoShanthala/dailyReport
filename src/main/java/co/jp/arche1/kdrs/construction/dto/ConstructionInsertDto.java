package co.jp.arche1.kdrs.construction.dto;

import java.io.Serializable;
import java.time.LocalDate;

import co.jp.arche1.kdrs.common.BaseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
public class ConstructionInsertDto extends BaseDto {
	// static final long serialVersionUIDが必要
	private static final long serialVersionUID = 1L;

	// 引数なしコンストラクタの定義
	public ConstructionInsertDto() {
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
		public RequestHd() {
		}

		// プロパティ(メンバ変数)の宣言
		private int companyId;
		private String constCode;
		private String constName;
		private int userId;
		private LocalDate startDate;
		private LocalDate endDate;
	}
}
