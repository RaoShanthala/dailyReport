package co.jp.arche1.kdrs.work.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import co.jp.arche1.kdrs.common.BaseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper=false)
@Data
public class WorkDto extends BaseDto {
	// static final long serialVersionUIDが必要
	private static final long serialVersionUID = 1L;

	// 引数なしコンストラクタの定義
	public WorkDto() {
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
		private String orderCode;
		private String workCode;
		private LocalDateTime workDate;
		private String spot;
		private String outline;
		private String danger;
		private String safety;
		private String detail1;
		private String detail2;
		private String detail3;
		private String StaffClient;
		private String leader;
		private String fire;
		private String numPerson;
		private String hours;
		private LocalDateTime createdAt;
		private LocalDateTime updatedAt;
	}
}