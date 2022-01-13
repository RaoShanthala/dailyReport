package co.jp.arche1.kdrs.common.exception;

// NotFoundItemExceptionはミドルで対応するので（必ずAPで対応が必要ではないため）RuntimeExceptionを継承する。
// また、RuntimeExceptionのみロールバックされるので、Exceptionでロールバックが必要な場合は、
// Serviceクラスのメソッドの@Transactionalアノテーションを次のようにする。
// @Transactional(rollbackFor = Exception.class)
public class NotFoundItemException extends RuntimeException {

    public NotFoundItemException(String message) {
        super(message);
    }
}
