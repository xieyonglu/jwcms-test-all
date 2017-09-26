package jwcms.common.exception;

public class ServiceException extends Exception {
	
	private static final long serialVersionUID = 1L;
	protected int errorCode;
	protected String errorMessage;
	
	// private String code;

//	    public ServiceException(String message) {
//	        super(message);
//	    }
//
//	    public ServiceException(int codeCode, String message) {
//	        this(message);
//	        setCode(codeCode);
//	    }
//
//	    public ServiceException(String code, String message, Throwable cause) {
//	        super(message, cause);
//	        setCode(code);
//	    }
//
//	    public ServiceException(String code, Throwable cause) {
//	        super(cause);
//	        setCode(code);
//	    }

//	    public void setCode(String code) {
//	        this.code = code;
//	    }
//
//	    public String getCode() {
//	        return code;
//	    }

//	public ServiceException(String errorCode, String errorMessage) {
//		super(errorCode, errorMessage);
//		setErrorMessage(errorMessage);
//		setCode(errorCode);
//	}

	public ServiceException(IExceptionCode errorCode) {
		super(ExceptionHelper.getMessage(errorCode));
		this.errorCode = ExceptionHelper.getCode(errorCode);
		this.errorMessage = this.getMessage();
	}
	
	public ServiceException(IExceptionCode errorCode, Object... args) {
		super(ExceptionHelper.getMessage(errorCode, args));
		this.errorCode = ExceptionHelper.getCode(errorCode);
		this.errorMessage = this.getMessage();
	}

//	public ServiceException(IExceptionCode errorCode, String message) {
//		super(message);
//		this.errorCode = ExceptionHelper.getCode(errorCode);
//		this.errorMessage = ExceptionHelper.getMessage(errorCode);
//	}

//	public ServiceException(IExceptionCode errorCode, Throwable cause) {
//		super(ExceptionHelper.getMessage(errorCode), cause);
//		this.errorCode = ExceptionHelper.getCode(errorCode);
//		this.errorMessage = this.getMessage();
//	}
//	
//	public ServiceException(IExceptionCode errorCode, String message, Throwable cause) {
//		super(message, cause);
//		this.errorCode = ExceptionHelper.getCode(errorCode);
//		this.errorMessage = ExceptionHelper.getMessage(errorCode);
//	}

	public ServiceException(String msg) {
		super(msg);
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
