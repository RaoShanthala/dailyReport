package co.jp.arche1.kdrs.report.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import co.jp.arche1.kdrs.common.BaseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper=false)
@Data
public class ReportDto extends BaseDto{

	// static final long serialVersionUIDが必要
		private static final long serialVersionUID = 1L;

		// 引数なしコンストラクタの定義
		public ReportDto() {
			reqDt = new ArrayList<RequestDt>();

	    	super.setTranId(this.getClass().getName());
	    }

		// プロパティ(メンバ変数)の宣言
		private List<RequestDt> reqDt ;

		@Data
		public static class RequestDt implements Serializable {
		    // static final long serialVersionUIDが必要
		    private static final long serialVersionUID = 1L;

		    // 引数なしコンストラクタの定義
		    public RequestDt() {}

			// プロパティ(メンバ変数)の宣言
		    private byte action;
		 	private String constCode;
			private String reportCode;
			private String personCode;
			private LocalDateTime reportDate;
			private String detail;
			private String constType;
			private String staff;
			private String numPerson;
			private String hours;
			private String earlyHours;
			private String overHours;
			private LocalDateTime createdAt;
			private LocalDateTime updatedAt;
		}

}
