package cn.hi321.browser.download;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import cn.hi321.browser.utils.FileUtil;

public class DownloadHelper {

	public static String getDownloadPath() {
		return FileUtil.SAVE_FILE_PATH_DIRECTORY + "/" + "download";
	}

	public static String getAbsolutePath(DownloadEntity entity,
			String destination) {
		String path = entity.getSrcPath();
		return path;
	}

	/**
	 * 返回下载文件的大小
	 * 
	 * @return
	 */
	public static long getDownloadedFileSize(DownloadEntity entity) {
		String path = entity.getSrcPath();// DownloadHelper.getAbsolutePath(entity,DownloadHelper.getDownloadPath());
		File file = new File(path);
		return file.length();
	}

	// 过滤特殊字符
	private static String StringFilter(String str)
			throws PatternSyntaxException {
		String regEx = "[`~!@#$%^&*()+=|{}':;',//[//].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		return m.replaceAll("").trim();
	}

	public static String changeHashidToNumStr(String hashId) {
		String numStr = hashId;
		numStr = numStr.replace("a", "1");
		numStr = numStr.replace("b", "2");
		numStr = numStr.replace("c", "3");
		numStr = numStr.replace("d", "4");
		numStr = numStr.replace("e", "5");
		numStr = numStr.replace("f", "6");
		return numStr;
	}
}
