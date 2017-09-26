package jwcms.common.exception;

import java.lang.reflect.Field;
import java.text.MessageFormat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class ExceptionHelper {
	
	private static final Log LOGGER = LogFactory.getLog(ExceptionHelper.class);
	
	private static final String UNKNOWN_EXCEPTION = "UNKNOWN_EXCEPTION";
	
	public static String getMessage(IExceptionCode code, Object... arguments) {
		if (code == null)
			return UNKNOWN_EXCEPTION;
		try {
			Field field = code.getClass().getField(code.name());
			Desc descAnnotation = field.getAnnotation(Desc.class);
			if (descAnnotation == null) {
				return UNKNOWN_EXCEPTION;
			}
			else if (descAnnotation.value() == null || "".equals(descAnnotation.value().trim())) {
				return UNKNOWN_EXCEPTION;
			}
			else {
				if(arguments.length > 0) {
					return MessageFormat.format(descAnnotation.value(), arguments);
				} else {
					return descAnnotation.value();
				}
			}
		} catch (Exception e) {
			LOGGER.error("ExceptionHelper.getMessage error, the error message is {}", e);
			return UNKNOWN_EXCEPTION;
		}
	}

	public static int getCode(IExceptionCode code) {
		if (code == null)
			return -1;
		try {
			Field field = code.getClass().getField(code.name());
			Code codeAnnotation = field.getAnnotation(Code.class);
			if (codeAnnotation == null)
				return -1;
			else
				return codeAnnotation.value();
		} catch (Exception e) {
			LOGGER.error("ExceptionHelper.getMessage error, the error message is {}", e);
			return -1;
		}
	}
	
}
