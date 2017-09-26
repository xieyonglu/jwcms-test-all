package jwcms.common.util;

public class StringUtils {

	private StringUtils() {
	}

	public static boolean isEmpty(String... strs) {
		if (strs == null || strs.length == 0) {
			return true;
		}
		for (String str : strs) {
			if (str == null || str.length() == 0) {
				return true;
			}
		}
		return false;
	}

	public static boolean isNotEmpty(String... strs) {
		if (strs == null || strs.length == 0) {
			return false;
		}
		for (String str : strs) {
			if (str == null || str.length() == 0) {
				return false;
			}
		}
		return true;
	}

	public static String convertMobile(String mobile) {
		if (isEmpty(mobile) || mobile.length() != 11) {
			return mobile;
		}
		return mobile.substring(0, 3) + "****" + mobile.substring(7);
	}

}
