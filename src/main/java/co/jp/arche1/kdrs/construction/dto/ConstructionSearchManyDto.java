package co.jp.arche1.kdrs.construction.dto;

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

public class ConstructionSearchManyDto extends BaseDto{
/*
	// static final long serialVersionUIDが必要
		private static final long serialVersionUID = 1L;

		// 引数なしコンストラクタの定義
		public ConstructionSearchManyDto() {
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
			private Integer  constId;
			private String constCode;
			private String constName;
			private Byte deleted;
		}
		@Data
		public static class ResponseDt implements Serializable {
		    // static final long serialVersionUIDが必要
		    private static final long serialVersionUID = 1L;

		    // 引数なしコンストラクタの定義
		    public ResponseDt() {}

			// プロパティ(メンバ変数)の宣言
			private Integer constId;
			private String constCode;
			private String constName;
			private Integer userId;
			private String deleted;
			private LocalDate startDate;
			private LocalDate endDate;
			private LocalDateTime createdAt;
			private LocalDateTime updatedAt;
		} */

	}


