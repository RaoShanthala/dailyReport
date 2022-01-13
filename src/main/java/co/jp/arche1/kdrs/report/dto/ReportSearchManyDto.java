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

public class ReportSearchManyDto extends BaseDto{

	// static final long serialVersionUIDが必要
		private static final long serialVersionUID = 1L;

		// 引数なしコンストラクタの定義
		public ReportSearchManyDto() {
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
		    private Integer userId; //added
			private Integer reportNo;
			private Integer privConstId;
			private Byte deleted;
		}
		@Data
		public static class ResponseDt implements Serializable {
		    // static final long serialVersionUIDが必要
		    private static final long serialVersionUID = 1L;

		    // 引数なしコンストラクタの定義
		    public ResponseDt() {}

			// プロパティ(メンバ変数)の宣言
			private Integer privConstId;

			//added
			private String privConstCode;
			private String privConstName;
			private Integer userId;
			private String userName;
			private String loginUser;

			private Integer reportNo;
			private String reportCode;
			private String personCode;
			private LocalDateTime reportDate;
			private String detail;
			private String constType;
			private String Staff;
			private String numPerson;
			private String hours;
			private String earlyHours;
			private String overHours;
			private String deleted;
			private LocalDateTime createdAt;
			private LocalDateTime updatedAt;
		}

	}


