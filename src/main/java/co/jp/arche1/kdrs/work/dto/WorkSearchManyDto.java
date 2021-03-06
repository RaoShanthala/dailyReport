package co.jp.arche1.kdrs.work.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import co.jp.arche1.kdrs.common.BaseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
public class WorkSearchManyDto extends BaseDto {

	// static final long serialVersionUIDが必要
	private static final long serialVersionUID = 1L;

	// 引数なしコンストラクタの定義
	public WorkSearchManyDto() {
		reqHd = new RequestHd();
		resDt = new ArrayList<ResponseDt>();
		super.setTranId(this.getClass().getName());
	}

	// プロパティ(メンバ変数)の宣言
	private RequestHd reqHd;
	private List<ResponseDt> resDt;
	private Object resDtTitle;

	@Data
	public static class RequestHd implements Serializable {
		// static final long serialVersionUIDが必要
		private static final long serialVersionUID = 1L;

		// 引数なしコンストラクタの定義
		public RequestHd() {
		}

		// プロパティ(メンバ変数)の宣言
		private Integer userId;
		private Integer orderNo;
		private Integer workNo;
		private Integer privConstId;
		private Byte deleted;
	}

	@Data
	public static class ResponseDt implements Serializable {
		// static final long serialVersionUIDが必要
		private static final long serialVersionUID = 1L;

		// 引数なしコンストラクタの定義
		public ResponseDt() {
		}

		// プロパティ(メンバ変数)の宣言
		private Integer privConstId;
		private String privConstCode;
		private String privConstName;
		private Integer userId;
		private String userName;
		private String loginUser;
		private Integer orderNo;
		private Integer workNo;
		private String workCode;
		private LocalDateTime workDate;
		private String spot;
		private String outline;
		private String danger;
		private String safety;
		private String detail1;
		private String detail2;
		private String detail3;
		private String staffClient;
		private String leader;
		private String fire;
		private String numPerson;
		private String hours;
		private String deleted;
		private LocalDateTime createdAt;
		private LocalDateTime updatedAt;
	}

}
