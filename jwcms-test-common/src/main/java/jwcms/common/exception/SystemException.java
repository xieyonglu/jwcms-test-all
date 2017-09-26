package jwcms.common.exception;

public class SystemException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	protected int errorCode;
	protected String errorMessage;

    public SystemException(String message) {
        super(message);
    }

    public SystemException(int errorCode, String message, Throwable cause) {
        super(message, cause);
        setErrorCode(errorCode);
    }

    public SystemException(int errorCode, Throwable cause) {
        super(cause);
        setErrorCode(errorCode);
    }

	public SystemException(int errorCode, String errorMessage) {
		setErrorMessage(errorMessage);
		setErrorCode(errorCode);
	}

	public SystemException(IExceptionCode errorCode) {
		super(ExceptionHelper.getMessage(errorCode));
		this.errorCode = ExceptionHelper.getCode(errorCode);
		this.errorMessage = this.getMessage();
	}

	public SystemException(IExceptionCode errorCode, String message, Throwable cause) {
		super(message, cause);
		this.errorCode = ExceptionHelper.getCode(errorCode);
		this.errorMessage = ExceptionHelper.getMessage(errorCode);
	}

	public SystemException(IExceptionCode errorCode, String message) {
		super(message);
		this.errorCode = ExceptionHelper.getCode(errorCode);
		this.errorMessage = ExceptionHelper.getMessage(errorCode);
	}

	public SystemException(IExceptionCode errorCode, Throwable cause) {
		super(ExceptionHelper.getMessage(errorCode), cause);
		this.errorCode = ExceptionHelper.getCode(errorCode);
		this.errorMessage = this.getMessage();
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
}
