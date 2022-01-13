package co.jp.arche1.kdrs.construction.dto;

import java.io.Serializable;

import co.jp.arche1.kdrs.common.BaseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
public class PrivConstructionUpdateConstIdDto extends BaseDto{
	private static final long serialVersionUID = 1L;

	// 引数なしコンストラクタの定義
	public PrivConstructionUpdateConstIdDto() {
		reqHd = new RequestHd();
		reqDt = new RequestDt();
		super.setTranId(this.getClass().getName());
	}

	// プロパティ(メンバ変数)の宣言
	private RequestHd reqHd;
	private RequestDt reqDt;

	@Data
	public static class RequestHd implements Serializable {
		// static final long serialVersionUIDが必要
		private static final long serialVersionUID = 1L;

		// 引数なしコンストラクタの定義
		public RequestHd() {
		}

		// プロパティ(メンバ変数)の宣言
		private int constId;
		private int action; // 1 for insert and 2 for delete
	}

	@Data
	public static class RequestDt implements Serializable {
	    // static final long serialVersionUIDが必要
	    private static final long serialVersionUID = 1L;

	    // 引数なしコンストラクタの定義
	    public RequestDt() {}

		// プロパティ(メンバ変数)の宣言
		private int[] privConstId;

	}
}
