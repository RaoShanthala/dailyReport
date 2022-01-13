package co.jp.arche1.kdrs.namecollection.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class PvNamecollectionRepository {

	// pt_namecollection
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
