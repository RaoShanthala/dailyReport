package co.jp.arche1.kdrs.construction.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import co.jp.arche1.kdrs.common.BaseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper=false)
@Data

public class PrivConstructionSearchListDto extends BaseDto{

	// static final long serialVersionUIDが必要
		private static final long serialVersionUID = 1L;

		// 引数なしコンストラクタの定義
		public PrivConstructionSearchListDto() {
			reqHd = new RequestHd();
			resDt = new ArrayList<ResponseDt>();
	    	super.setTranId(this.getClass().getName());
	    }

		// プロパティ(メンバ変数)の宣言
		private RequestHd reqHd;
		private List<ResponseDt> resDt ;
		private Object resDtTitle;

		@Data
		public static class RequestHd implements Serializable {
		    // static final long serialVersionUIDが必要
		    private static final long serialVersionUID = 1L;

		    // 引数なしコンストラクタの定義
		    public RequestHd() {}

			// プロパティ(メンバ変数)の宣言
		    private Integer companyId;
			private Integer constId;
			private String privConstName;
		    private String sei;
		    private String mei;
			private byte searchType;
		}
		@Data
		public static class ResponseDt implements Serializable {
		    // static final long serialVersionUIDが必要
		    private static final long serialVersionUID = 1L;

		    // 引数なしコンストラクタの定義
		    public ResponseDt() {}

			// プロパティ(メンバ変数)の宣言
		    private Integer companyId;
			private Integer privConstId;
		//	private String privConstCode;
			private String privConstName;
			private String userName;
		}

	}


