package cn.hi321.browser.utils;

import java.text.SimpleDateFormat;

/**
 * 格式化工具
 * 
 * @author yanggf
 * 
 */
public class FormatUtil {
	private static final String TAG = "FormatUtil";

	public static final class TimeFormat {
		public static final long SECOND = 1000;
		public static final long MINUTE = 60 * 1000;
		public static final long HOUR = 60 * MINUTE;
		public static final long DAY = 24 * HOUR;

		public static String absolute(Long time) {
			SimpleDateFormat df = new SimpleDateFormat("MM-dd");
			return df.format(time);
		}

	}
}
