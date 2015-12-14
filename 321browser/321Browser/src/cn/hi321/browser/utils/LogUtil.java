package cn.hi321.browser.utils;

import java.io.File;
import java.io.RandomAccessFile;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

/**
 * 
 * @author yanggf
 *
 */
public class LogUtil {
	public static boolean isDebug = true;

	public static void v(String tag, String msg) {
		if (isDebug)
			android.util.Log.v(tag, msg);
	}

	public static void v(String tag, String msg, Throwable t) {
		if (isDebug)
			android.util.Log.v(tag, msg, t);
	}

	public static void d(String tag, String msg) {
		if (isDebug)
			android.util.Log.d(tag, msg);
	}

	public static void d(String tag, String msg, Throwable t) {
		if (isDebug)
			android.util.Log.d(tag, msg, t);
	}

	public static void i(String tag, String msg) {
		if (isDebug)
			android.util.Log.i(tag, msg);
	}

	public static void i(String tag, String msg, Throwable t) {
		if (isDebug)
			android.util.Log.i(tag, msg, t);
	}

	public static void w(String tag, String msg) {
		if (isDebug)
			android.util.Log.w(tag, msg);
	}

	public static void w(String tag, String msg, Throwable t) {
		if (isDebug)
			android.util.Log.w(tag, msg, t);
	}

	public static void e(String tag, String msg) {
		if (isDebug)
			android.util.Log.e(tag, msg);
	}

	public static void e(String tag, String msg, Throwable t) {
		if (isDebug)
			android.util.Log.e(tag, msg, t);
	}
	
	/*
	 * ===============以下部分由张学东 移植播放器增加============================================
	 */
	
	
	/** 服务器停止服务 */ 
	/** 重启客户端 */
	/** 文件保存路径sdcard/321/ */

	public static final String SAVE_FILE_PATH_DIRECTORY = Environment
			.getExternalStorageDirectory().getAbsolutePath() + "/" + "321";
	public static final String TAG = "321Browser";
	public static boolean DEBUG = true;
	private static boolean PRINTLOG = false;
	private static boolean IS_DEBUG = false;
	public static final String LOG_PATH = SAVE_FILE_PATH_DIRECTORY + "/321.txt";

	public static void i(String msg) {
		if (DEBUG) {
			Log.i(TAG, msg);
		}
		if (PRINTLOG) {
			writeFile(msg);
		}
	}



	public static void v(String msg) {
		if (DEBUG || IS_DEBUG) {
			Log.v(TAG, msg);
		}
		if (PRINTLOG) {
			writeFile(msg);
		//	FileHelper.WriteStringToFile(msg + "\r\n", LOG_PATH, true);
		}
	}

/*	public static void v(String tag, String msg) {
		if (DEBUG || IS_DEBUG) {
			Log.v(tag, msg);
		}
		if (PRINTLOG) {
			writeFile(msg);
			FileHelper.WriteStringToFile(msg + "\r\n", LOG_PATH, true);
		}
	}
*/
	public static void e(String msg) {
		if (DEBUG) {
			Log.e(TAG, msg);
		}
		if (PRINTLOG) {
			writeFile(msg);
		}
	}

/*	public static void e(String tag, String msg) {
		if (DEBUG) {
			Log.e(tag, msg);
		}
		if (PRINTLOG) {
			writeFile(msg);
		}
	}*/

/*	public static void e(String tag, String msg, Throwable tr) {
		if (DEBUG) {
			Log.e(tag, msg, tr);
		}
		if (PRINTLOG) {
			writeFile(msg);
		}
	}*/
 
	public static File file = new File(LOG_PATH);
	/**
	 * 写入文件
	 * 
	 * @param str
	 */
	public synchronized static void writeFile(String content) {
		if (TextUtils.isEmpty(content) || !Utils.isSDcardExist()) {
			return;
		}
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			RandomAccessFile raf = new RandomAccessFile(file, "rw");
			long len = raf.length();
			raf.seek(len);
			raf.writeBytes(content);
			raf.close();
		} catch (Exception e) {
			LogUtil.e(TAG, e.toString());
		}
	}

	public static boolean isDEBUG() {
		return DEBUG;
	}

	public static void setDEBUG(boolean debug) {
		DEBUG = debug;
	}

	public static boolean isPrintlog() {
		return PRINTLOG;
	}

	public static boolean isPRINTLOG() {
		return PRINTLOG;
	}

	public static void setPRINTLOG(boolean pRINTLOG) {
		PRINTLOG = pRINTLOG;
	}
	
	
	public static void Logger(String msg) {
		if (IS_DEBUG) {
			Log.e(TAG, msg);
		}
		if (PRINTLOG) {
//			FileHelper.WriteStringToFile(msg + "\r\n", LOG_PATH, true);
		}
	}

	
	
	
	
}
