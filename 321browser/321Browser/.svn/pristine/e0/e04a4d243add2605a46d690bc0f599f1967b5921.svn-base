package cn.hi321.browser.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import cn.hi321.browser.download.DownloadHelper;


public class FileUtil {
	public static String TAG  = "FileUtil";
	public final static int POOL_SIZE = 5; 	// 单个CPU线程池大小 
	public final static int MB = 1024 * 1024; 
	public final static int CACHE_SIZE = 3; // 限制apk文件包缓存图片目录大小最大为3M
	public final static int SD_CACHE_SIZE = 10; // 限制SD卡文件图片缓存目录大小最大为10M
	public final static String SDCARD_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
	public final static String SAVE_FILE_PATH_DIRECTORY = SDCARD_PATH + "/"+ "321Browser";
	public final static String EXTERNAL_SDCARD_PATH = SDCARD_PATH+ "/external_sd";
	public final static String EXTERNAL_321BROWSER_PATH = EXTERNAL_SDCARD_PATH+ "/321Browser";
	public final static String FEATURED_CACHE_PATH = SAVE_FILE_PATH_DIRECTORY+ CacheUtils.featuredBasePath + CacheUtils.featuredStr;
	public final static String NEW_FEATURED_CACHE_PATH = SAVE_FILE_PATH_DIRECTORY+ CacheUtils.featuredBasePath+ CacheUtils.NEW_FEATURE_BASE_PATH;
	public final static String SPREAD_CACHE_PATH = SAVE_FILE_PATH_DIRECTORY+ CacheUtils.spreadBasePath + CacheUtils.spreadStr;
	public final static String SHORTMEDIA_PATH = SAVE_FILE_PATH_DIRECTORY+ CacheUtils.shortBasePath;
	public final static String LIVEMEDIA_PATH = SAVE_FILE_PATH_DIRECTORY+ CacheUtils.liveBasePath;
	public final static String CACHE_IMG_DIR_PATH = "/imgfiles/";//Cache directory of the picture 
	public final static String FINAL_SAVE_MEDIA_PATH = SAVE_FILE_PATH_DIRECTORY;
	public final static String SAVE_STATE_TO_FILE_PATH = SDCARD_PATH + "/" +"SYSTEM_FUNSHION.ini";
	public final static String CACHE_IMAGES_PATH = "/321Browser" + CACHE_IMG_DIR_PATH;
	
	public final static String PLAT_LOGIN_CACHE_PATH = FileUtil.SAVE_FILE_PATH_DIRECTORY + CacheUtils.plat_login_path + "platlogin";
	public final static String PLAT_BOUND_CACHE_PATH = FileUtil.SAVE_FILE_PATH_DIRECTORY+CacheUtils.plat_login_path+"platbound";
	/** Upgrade request path identification **/
	private static final String LOG_PATH = SAVE_FILE_PATH_DIRECTORY+ "/fslog.txt";
	public static File file = new File(LOG_PATH);
	
	/**
	 * Determine whether there is a memory card, and returns TRUE, otherwise
	 * FALSE
	 * @return
	 */
	public static boolean isSDcardExist() {
		boolean isExist = false;
		try {
			if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
				isExist = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isExist;

	}
	
	public synchronized static void writeFile(String content) {
		if (TextUtils.isEmpty(content) || !isSDcardExist()) {
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
		}
	}
	
	/**
	 * Check the SD card if there
	 * @return
	 */
	public static boolean checkSDCard() {
		try {
			final String status = Environment.getExternalStorageState();
			if (status.equals(Environment.MEDIA_MOUNTED)) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * check file directory exists.
	 * @return boolean
	 */
	public static boolean checkFileDirectory() {
		try {
			final File dir = new File(FINAL_SAVE_MEDIA_PATH);
			if (!dir.exists()) {
				final boolean isMkdirs = dir.mkdirs();
				return isMkdirs;
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static String getAppFilesDirBySDCard(Context context) {
		return SAVE_FILE_PATH_DIRECTORY;
	}
	
	/**
	 * @return files
	 */
	public static String getAppFilesDir(Context context) {

		if (FileUtil.isSDcardExist()) {
			return getAppFilesDirBySDCard(context);
		} else {
			return getAppFilesDirByData(context);
		}
	}
	
	public static void initCacheFileBySDCard(Context context) {
		final String imageDir = getAppFilesDirBySDCard(context) + CACHE_IMG_DIR_PATH;
		final File imageFileDir = new File(imageDir);
		if (!imageFileDir.exists()) {
			imageFileDir.mkdirs();
		}
	}
	
	public static String getAppFilesDirByData(Context context) {
		return context.getFilesDir().getAbsolutePath();
	}
	
	/**
	 * check file directory exists.
	 * 
	 * @return boolean
	 */
	public static boolean checkAppFileDirectory(Context context) {
		try {
			final String imageDir = getAppFilesDirByData(context);
			final File imageFileDir = new File(imageDir);
			if (!imageFileDir.exists()) {
				final boolean isMkdirs = imageFileDir.mkdirs();
				return isMkdirs;
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;

	}
	
	public static void initCacheFile(Context context) {
		if (FileUtil.isSDcardExist()) {
			initCacheFileBySDCard(context);
		} else {
			initCacheFileByData(context);
		}

	}

	/**
	 * Create /data/data/cn.hi321.browser/files/imgfiles Cache folder
	 */
	public static void initCacheFileByData(Context context) {
		final String imageDir = getAppFilesDirByData(context)+ CACHE_IMG_DIR_PATH;
		final File imageFileDir = new File(imageDir);
		if (!imageFileDir.exists()) {
			imageFileDir.mkdirs();
		}
	}
	
	/**
	 * 判断是否存在缓存文件
	 * 
	 * @return add by jiyx at 2012-8-27 16:00:59
	 */
	public static boolean isExist(String path) {
		File fileName = new File(path);
		return fileName.exists();
	}
	
	/**
	 * 判断指定名称的影片是否存在于sdcard中
	 * 2013-3-4下午2:16:53
	 * @param displayName 
	 * @param fileFormat  需要判断的文件格式
	 * @return
	 */
	public static boolean checkFileExist(String displayName ,String fileFormat) {
		File localfile = new File(DownloadHelper.getDownloadPath(),displayName+fileFormat);
		return localfile.exists();
	}
	
	/**
	 * 将字符串写入文件
	 * @param path
	 * @param rptContent
	 */
	public static void writeStateToFile(String path, String rpt) {
		//写入文本文件
		File file = new File(path);		
		BufferedWriter writer = null;
		String str="";
		try{
			if (!file.exists())
				file.createNewFile();
			writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(file)));
			writer.write(rpt);
		}catch (Exception e){
			str=e.getMessage();
		}
		finally{
			try{
				if (writer != null)
					writer.close();
			}catch (IOException e){
				str=str+"保存报文出错,未能正确关闭文件流。";
			}
		}
	}
	
	public static void deleteCache() {
		new Thread() {
			@Override
			public void run() {
				super.run();
				try {
					String pathStr = FileUtil.SAVE_FILE_PATH_DIRECTORY
							+ CacheUtils.shortBasePath;
					LogUtil.e(TAG, "删除文件的路径--------" + pathStr);
					deleteDirectory(pathStr);
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}
	
	/**
	 * 删除单个文件
	 * 
	 * @param sPath
	 *            被删除文件的文件名
	 * @return 单个文件删除成功返回true，否则返回false
	 */
	public static boolean deleteFile(String sPath) {
		boolean flag = false;
		file = new File(sPath);
		// 路径为文件且不为空则进行删除
		if (file.isFile() && file.exists()) {
			file.delete();
			flag = true;
		}
		return flag;
	}

	/**
	 * 删除目录（文件夹）以及目录下的文件
	 * 
	 * @param sPath
	 *            被删除目录的文件路径
	 * @return 目录删除成功返回true，否则返回false
	 */
	public static boolean deleteDirectory(String sPath) {   
	boolean flag = false;
  //如果sPath不以文件分隔符结尾，自动添加文件分隔符   
	    if (!sPath.endsWith(File.separator)) {   
	        sPath = sPath + File.separator;   
	    }   
	    File dirFile = new File(sPath);   
	    //如果dir对应的文件不存在，或者不是一个目录，则退出   
	    if (!dirFile.exists() || !dirFile.isDirectory()) {   
	       return false;   
	    }   
	    flag = true;   
   //删除文件夹下的所有文件(包括子目录)   
	    File[] files = dirFile.listFiles();   
	    for (int i = 0; i < files.length; i++) {   
	        //删除子文件   
	        if (files[i].isFile()) {   
	            flag = deleteFile(files[i].getAbsolutePath());   
	            if (!flag) break;   
	        } //删除子目录   
	        else {   
	          flag = deleteDirectory(files[i].getAbsolutePath());   
	           if (!flag) break;   
	       }   
	   }   
	    if (!flag) return false;   
	    //删除当前目录   
	   return dirFile.delete();
	}
	
}
