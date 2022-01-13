package co.jp.arche1.kdrs.namecollection.repository;

import lombok.Data;

@Data
public class PvResultMessageRepository {

	// pt_namecollection
	private Byte priority;
	private String dtoNameAlpha;
	private String dtoNameLong;
	private String msgCode;
	private String msgNameAlpha;
	private String msgNameLong;

}
