package co.jp.arche1.kdrs.common.security.dto;

public class Response {

	private Status status;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setStatus(String status) {
        this.status = new Status(status);
    }

    public static ErrorResponse createErrorResponse(Exception ex){
        ErrorResponse errorResponse = new ErrorResponse();
        if (ex.getClass().getSimpleName().equals("AccessDeniedException")) {
	        errorResponse.setStatus(new ErrorStatus(ex.getClass().getSimpleName(), "権限がありません。"));
        } else {
	        // ex.getMessage():アクセスが拒否されました。
	        errorResponse.setStatus(new ErrorStatus(ex.getClass().getSimpleName(), ex.getMessage()));
        }
        return errorResponse;
    }

    public static SuccessResponse createSuccessResponse(Object data){
        SuccessResponse successResponse = new SuccessResponse();
        successResponse.setStatus(Status.CODE_OK);
        successResponse.setResult(data);
        return successResponse;
    }
}
