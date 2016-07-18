package jp.co.fujixerox.nbd;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public enum HttpError {
    ENTITY_NOT_FOUND(HttpStatus.NOT_FOUND, "入力されたIDは存在しません。ID={0}"),
    ENTITY_CONFLICT(HttpStatus.CONFLICT, "入力されたIDは既に存在しています。ID={0}"),



    ILLEGAL_DATE(HttpStatus.BAD_REQUEST, "入力された日付が不正です。ID={0}"),

    NOT_CHECKOUT(HttpStatus.BAD_REQUEST, "指定された端末は貸出中ではありません。ID={0}"),
    CHECKOUT_CONFLICT(HttpStatus.CONFLICT, "貸出中です。"),

    UNEXPECTED(HttpStatus.INTERNAL_SERVER_ERROR, "想定外のエラー: {0}");

    @Getter
    private HttpStatus status;
    @Getter
    private String message;

    HttpError(HttpStatus status, String message){
        this.status = status;
        this.message = message;
    }
}
