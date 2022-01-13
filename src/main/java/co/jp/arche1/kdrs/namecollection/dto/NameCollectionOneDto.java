package co.jp.arche1.kdrs.namecollection.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

import co.jp.arche1.kdrs.common.BaseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper=false)
@Data
public class NameCollectionOneDto extends BaseDto {
	// static final long serialVersionUIDが必要
	private static final long serialVersionUID = 1L;

	// 引数なしコンストラクタの定義
	public NameCollectionOneDto() {
		reqHd = new RequestHd();
		resHd = new ResponseHd();
		//resHdTitle = new ResponseHdTitle();

    	super.setTranId(this.getClass().getName());
    }

	// プロパティ(メンバ変数)の宣言
	private RequestHd reqHd;
	private ResponseHd resHd ;
	//private ResponseHdTitle resHdTitle;

	@Data
	public static class RequestHd implements Serializable {
	    // static final long serialVersionUIDが必要
	    private static final long serialVersionUID = 1L;

	    // 引数なしコンストラクタの定義
	    public RequestHd() {}

		// プロパティ(メンバ変数)の宣言
		//private Short codeSection;
		private String nameSection;
		//private String codeType;
		private Short codeNumeric;
		private String codeString;
	}

	@Data
	public static class ResponseHd implements Serializable {
	    // static final long serialVersionUIDが必要
	    private static final long serialVersionUID = 1L;

	    // 引数なしコンストラクタの定義
	    public ResponseHd() {}

		// プロパティ(メンバ変数)の宣言
		private Integer codeId;
		private Short codeSection;
		private String codeType;
		private Short codeNumeric;
		private String codeString;
		private String nameAlpha;
		private String nameShort;
		private String nameLong;
		private LocalDate creDate;
		private LocalDateTime updDatetime;
	}
	/*
	@Data
	public static class ResponseHdTitle implements Serializable {
	    // static final long serialVersionUIDが必要
	    private static final long serialVersionUID = 1L;

	    // 引数なしコンストラクタの定義
	    public ResponseHdTitle() {}

		// プロパティ(メンバ変数)の宣言
		private final String  codeId = "コードID";
		private final String  codeSection = "種別";
		private final String  codeType = "コード型";
		private final String  codeNumeric = "コード数値";
		private final String  codeString = "コード文字列";
		private final String  nameAlpha = "英字名称";
		private final String  nameShort = "略式名称";
		private final String  nameLong = "正式名称";
		private final String  creDate = "作成日";
		private final String  updDatetime = "更新日時";
	}
	*/
}
