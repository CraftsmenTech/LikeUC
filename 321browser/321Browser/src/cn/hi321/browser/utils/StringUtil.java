package cn.hi321.browser.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.text.TextUtils;

public class StringUtil {
	private final static String TAG = "StringUtil";
	private final static char HEX_DIGITS[] = { '0', '1', '2', '3', '4', '5','6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
	public static String ENTERTAINMENT = "entertainments";
	public static String SPORT = "sport";
	public static  String LOCAL_NOTIFICATION_TAG = "showtag";
	
	public static boolean isEmpty(String str) {
		if (TextUtils.isEmpty(str) || Utils.NULL.equals(str)|| (str != null && "".equals(str.trim()))) {
			return true;
		}
		return false;
	}
	
	public static String replaceBlank(String str) {
		String dest = "";
		if (str != null) {
			Pattern p = Pattern.compile("\r|\n");
			Matcher m = p.matcher(str);
			dest = m.replaceAll("");
		}
		return dest;
	}
	
	public static String getString(int resId,Context context) {
		String str = "";
		try {
			str = context.getString(resId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}
	
	private static String toHexString(byte[] b) {
		StringBuilder str = new StringBuilder(b.length * 2);
		for (int i = 0; i < b.length; i++) {
			str.append(HEX_DIGITS[(b[i] & 0xf0) >>> 4]);
			str.append(HEX_DIGITS[b[i] & 0x0f]);
		}
		return str.toString();
	}
	
	/**
	 * 通过传过来的类型返回相应的SQL语句
	 */
	public static String getSQLStr(String type) {
		String sql = "";
		if (type.equals("movie")) {
			sql = SQLUtil.MOVIEINSERSQL;
		}else if ("tv".equals(type)) {
			sql = SQLUtil.TVINSERTSQL;
		}else if ("cartoon".equals(type)) {
			sql = SQLUtil.CARTOONINSERTSQL;
		}else if ("variety".equals(type)) {
			sql = SQLUtil.VARIETYINSERSQL;
		}else if (ENTERTAINMENT.equals(type)) {
			sql = SQLUtil.ENTERTAINMENTINSERSQL;
		}else if (SPORT.equals(type)) {
			sql = SQLUtil.SPORTSQL;
		}
		return sql;
	}

	/**
	 * MD5 Encryption
	 * 
	 * @param str
	 * @return
	 */
	public static String getMD5Str(String str) {
		MessageDigest messageDigest = null;
		try {
			messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.reset();
			messageDigest.update(str.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
			LogUtil.e(TAG, e.toString());
			return null;
		} catch (UnsupportedEncodingException e) {
			LogUtil.e(TAG, e.toString());
			return null;
		}

		final byte[] byteArray = messageDigest.digest();

		final StringBuffer md5StrBuff = new StringBuffer();

		for (int i = 0; i < byteArray.length; i++) {
			if (Integer.toHexString(0xFF & byteArray[i]).length() == 1) {
				md5StrBuff.append("0").append(
						Integer.toHexString(0xFF & byteArray[i]));
			} else {
				md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
			}
		}
		// 6-bit encryption, 9 to 25
		return md5StrBuff.substring(8, 24).toString().toUpperCase();
	}

	
	
	public static String checkFilemd5(String filename) {
		if (filename == null) {
			return "";
		}
		InputStream fis = null;
		byte[] buffer = new byte[1024];
		int numRead = 0;
		MessageDigest md5;
		try {
			fis = new FileInputStream(filename);
			md5 = MessageDigest.getInstance("MD5");
			while ((numRead = fis.read(buffer)) > 0) {
				md5.update(buffer, 0, numRead);
			}
			return toHexString(md5.digest());
		} catch (Exception e) {
			System.out.println("error");
			return null;
		} finally {
			try {
				if (fis != null)
					fis.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static String substringAndAddPrefix(String str, int toCount,String more)  
	{  
		int reInt = 0;  
		String reStr = "";  
		if (str == null)  
			return "";  
		char[] tempChar = str.toCharArray();  
		for (int kk = 0; (kk < tempChar.length && toCount > reInt); kk++) {  
			String s1 = str.valueOf(tempChar[kk]);  
			byte[] b = s1.getBytes();  
			reInt += b.length;  
			reStr += tempChar[kk];  
		}  
		if (toCount == reInt || (toCount == reInt - 1))  
			reStr = more+reStr;  
		return reStr;  
	} 
	
	public static String getTotalVideoTimeFormat(long videoTotalTime){
		int totalSeconds = (int)(videoTotalTime / 1000L);
		int seconds = totalSeconds % 60;
		int minutes = (totalSeconds / 60) % 60;
		int hours = totalSeconds / 3600;
		return (hours > 0 ? String.format("%02d:%02d:%02d", new Object[] {Integer.valueOf(hours), Integer.valueOf(minutes), Integer.valueOf(seconds)}):
						   String.format("%02d:%02d", new Object[] {Integer.valueOf(minutes), Integer.valueOf(seconds)}));
	}
	
	public static String getCurrentVideoTimeFormat(long currentTime,long videoTotalTime){
		boolean  flag = showPlayVideoTimeFormat(videoTotalTime);
		int totalSeconds = (int)(currentTime / 1000L);
		int seconds = totalSeconds % 60;
		int minutes = (totalSeconds / 60) % 60;
		int hours = totalSeconds / 3600;
		return (flag ? String.format("%02d:%02d:%02d", new Object[] {Integer.valueOf(hours), Integer.valueOf(minutes), Integer.valueOf(seconds)}):
						   String.format("%02d:%02d", new Object[] {Integer.valueOf(minutes), Integer.valueOf(seconds)}));
	}
	
	public static boolean showPlayVideoTimeFormat(long totalPlayTime){
		boolean mIsShowHMStimeFormat = false;
		int totalSeconds = (int)(totalPlayTime / 1000L);
		int hours = totalSeconds / 3600;
		if(hours > 0){
			mIsShowHMStimeFormat = true;
		}else{
			mIsShowHMStimeFormat =false;
		}	
		return mIsShowHMStimeFormat;
		
	}
	
}
