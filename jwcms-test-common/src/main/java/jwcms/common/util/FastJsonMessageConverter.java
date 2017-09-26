//package jwcms.common.utils;
//
//
//public class FastJsonMessageConverter extends AbstractMessageConverter {
//
//	private static Log LOGGER = LogFactory.getLog(FastJsonMessageConverter.class);
//
//	public static final String DEFAULT_CHARSET = "UTF-8";
//
//	private volatile String defaultCharset = DEFAULT_CHARSET;
//
//	public FastJsonMessageConverter() {
//		super();
//	}
//
//	public void setDefaultCharset(String defaultCharset) {
//		this.defaultCharset = (defaultCharset != null) ? defaultCharset : DEFAULT_CHARSET;
//	}
//
//	@Override
//	public Object fromMessage(Message message) throws MessageConversionException {
//		return null;
//	}
//
//	@SuppressWarnings("unchecked")
//	public <T> T fromMessage(Message message, T t) {
//		String json = "";
//		try {
//			json = new String(message.getBody(), defaultCharset);
//		} catch (UnsupportedEncodingException e) {
//			LOGGER.error("FastJsonMessageConverter.fromMessage error, the error message is {}", e);
//		}
//		return (T) JsonConvertUtils.toObject(json, t.getClass());
//	}
//
//	@Override
//	protected Message createMessage(Object objectToConvert, MessageProperties messageProperties) throws MessageConversionException {
//		byte[] bytes = null;
//		try {
//			String jsonString = JsonConvertUtils.toJson(objectToConvert);
//			bytes = jsonString.getBytes(this.defaultCharset);
//		} catch (UnsupportedEncodingException e) {
//			LOGGER.error("FastJsonMessageConverter.createMessage error, the error message is {}", e);
//			throw new MessageConversionException("Failed to convert Message content", e);
//		}
//
//		messageProperties.setContentType(MessageProperties.CONTENT_TYPE_JSON);
//		messageProperties.setContentEncoding(this.defaultCharset);
//		if (bytes != null) {
//			messageProperties.setContentLength(bytes.length);
//		}
//		return new Message(bytes, messageProperties);
//	}
//
//}
