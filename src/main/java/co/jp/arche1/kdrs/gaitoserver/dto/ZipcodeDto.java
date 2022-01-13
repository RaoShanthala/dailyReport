package co.jp.arche1.kdrs.gaitoserver.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class ZipcodeDto {
    /** ステータス */
    int status;

    /** メッセージ */
    String message;

    /** 郵便番号情報リスト */
    List<ZipcodeResultDto> results = new ArrayList<>();
}
