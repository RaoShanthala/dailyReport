package co.jp.arche1.kdrs.construction.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import co.jp.arche1.kdrs.common.BaseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper=false)
@Data
public class ConstructionDto extends BaseDto {
	// static final long serialVersionUIDが必要
	private static final long serialVersionUID = 1L;

	// 引数なしコンストラクタの定義
	public ConstructionDto() {
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
		private String constName;
		private Integer userId;
		private LocalDateTime createdAt;
		private LocalDateTime updatedAt;
	}
}