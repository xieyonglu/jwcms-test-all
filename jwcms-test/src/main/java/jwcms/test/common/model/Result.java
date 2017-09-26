//package jwcms.common.model;
//
//import java.io.Serializable;
//
//import net.sf.json.JSONObject;
//
//import org.apache.commons.lang.StringUtils;
//
///**
// * Common Result
// * 
// * @param <T>
// */
//public class Result<T> implements Serializable {
//
//	private static final long serialVersionUID = -3398107709903063842L;
//
//	private boolean success = false;
//	private String errorMsg = ""; // 错误信息
//	private String errorCode = ""; // 错误代码
//	private T model; // 业务类型
//	private String attributes; // 额外的属性
//	private transient JSONObject attributesJson; // json cache
//	private transient Exception exception;
//
//	public Result() {
//		this.attributesJson = new JSONObject();
//	}
//
//	public Result(boolean success) {
//		this.success = success;
//		this.attributesJson = new JSONObject();
//	}
//
//	/**
//	 * @param errorMsg
//	 */
//	public Result(String errorMsg) {
//		this.errorMsg = errorMsg;
//	}
//
//	/**
//	 * @param success
//	 * @param errorMsg
//	 */
//	public Result(boolean success, String errorMsg) {
//		this.success = success;
//		this.errorMsg = errorMsg;
//		this.attributesJson = new JSONObject();
//	}
//
//	/**
//	 * @param success
//	 * @param errorMsg
//	 * @param errorCode
//	 */
//	public Result(boolean success, String errorMsg, String errorCode) {
//		this.success = success;
//		this.errorMsg = errorMsg;
//		this.errorCode = errorCode;
//		this.attributesJson = new JSONObject();
//	}
//
//	public boolean isSuccess() {
//		return success;
//	}
//
//	public void setSuccess(boolean success) {
//		this.success = success;
//	}
//
//	public String getErrorMsg() {
//		return errorMsg;
//	}
//
//	public void setErrorMsg(String errorMsg) {
//		this.errorMsg = errorMsg;
//	}
//
//	public String getErrorCode() {
//		return errorCode;
//	}
//
//	public void setErrorCode(String errorCode) {
//		this.errorCode = errorCode;
//	}
//
//	public Exception getException() {
//		return exception;
//	}
//
//	public void setException(Exception exception) {
//		this.exception = exception;
//	}
//
//	public T getModel() {
//		return model;
//	}
//
//	public void setModel(T model) {
//		this.model = model;
//	}
//
//	public String getAttributes() {
//		return attributes;
//	}
//
//	public void setAttributes(String attributes) {
//		this.attributes = attributes;
//	}
//
//	public JSONObject getAttributesJson() {
//		if (null == attributesJson || attributesJson.isEmpty()
//				|| StringUtils.isBlank(attributes)) {
//			return null;
//		}
//		attributesJson = JSONObject.fromObject(attributes);
//		return attributesJson;
//	}
//
//	public Boolean putAttribute(String key, String value) {
//		if (StringUtils.isBlank(key) || StringUtils.isBlank(value))
//			return false;
//		getAttributesJson().put(key, value);
//		setAttributes(getAttributesJson().toString());
//		return null != attributesJson.get(key);
//	}
//
//	public String getAttribute(String key) {
//		return getAttributesJson().get(key).toString();
//	}
//
//	@Override
//	public String toString() {
//		return "Result [success=" + success + ", errorMsg=" + errorMsg
//				+ ", errorCode=" + errorCode + ", exception=" + exception + "]";
//	}
//
//}
