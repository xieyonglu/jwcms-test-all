package jwcms.test.framework;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;

public class ResponseEntity<E> {

	private Integer code;
	private String msg;
	private E data;

	public ResponseEntity(Integer code, String msg, E data) {
		super();
		this.code = code;
		this.msg = msg;
		this.data = data;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public E getData() {
		return data;
	}

	public void setData(E data) {
		this.data = data;
	}

	public static <E> ResponseEntity<E> success() {
		return new ResponseEntity<>(200, "Success", null);
	}

	public static <E> ResponseEntity<E> success(E data) {
		return new ResponseEntity<>(200, "Success", data);
	}

	public static <E> ResponseEntity<E> success(int code, E data) {
		return new ResponseEntity<>(code, "Success", data);
	}

	public static <E> ResponseEntity<E> success(int code) {
		return new ResponseEntity<>(code, "Success", null);
	}

	public static <E> ResponseEntity<E> fail(int code, String msg, E data) {
		return new ResponseEntity<>(code, msg, data);
	}

	public static <E> ResponseEntity<E> fail(int code, String msg) {
		return new ResponseEntity<>(code, msg, null);
	}

	public static <E> ResponseEntity<E> fail(E data) {
		return new ResponseEntity<>(500, "系统开小差啦~~~", data);
	}

	public static <E> ResponseEntity<E> fail(int code, String msg, HttpServletResponse response) {
		response.setStatus(HttpStatus.BAD_REQUEST.value());
		return new ResponseEntity<>(code, msg, null);
	}

	public static <E> ResponseEntity<E> errorToken() {
		return new ResponseEntity<>(401, "error token", null);
	}

	public static <E> ResponseEntity<E> errorCarrier() {
		return new ResponseEntity<>(403, "error carrier", null);
	}

	public static <E> ResponseEntity<E> error(E data) {
		return new ResponseEntity<>(500, "系统开小差啦~~~~~~~~~~~~~~", data);
	}

	public static <E> ResponseEntity<E> error(HttpServletResponse response) {
		response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "系统开小差啦~~~~~~~~~~~~~~", null);
	}

	public static <E> ResponseEntity<E> error(String msg, E data) {
		return new ResponseEntity<>(500, msg, data);
	}
}
