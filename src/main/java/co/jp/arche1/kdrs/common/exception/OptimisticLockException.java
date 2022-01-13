package co.jp.arche1.kdrs.common.exception;

// OptimisticLockExceptionはミドルで対応するので（必ずAPで対応が必要ではないため）RuntimeExceptionを継承する。
// また、RuntimeExceptionのみロールバックされるので、Exceptionでロールバックが必要な場合は、
// Serviceクラスのメソッドの@Transactionalアノテーションを次のようにする。
// @Transactional(rollbackFor = Exception.class)
public class OptimisticLockException extends RuntimeException {

    // 引数なしコンストラクタの定義
	public OptimisticLockException() {
	}

	public OptimisticLockException(String msg) {
		this.msg = msg;
	}

	private String msg;

	public String getMessage() {
		return this.msg;
	}
}
