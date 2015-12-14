package cn.hi321.browser.utils;

import java.lang.reflect.Field;

import android.content.Context;

/**
 * This class provides methods to get info relate to android system,
 *  such as the height of status bar;
 * 
 * @author Wang Chenxi
 *
 */
public class SystemInfoUtil {
	private final static String TAG = "SystemInfoUtil";

	private static int statusBarHeight = -1;
	
	public static int getStatusBarHeight(Context context) {
		if (statusBarHeight < 0) {
			measureStatusBarHeight(context);
		}
		return statusBarHeight;
	}
	
	private synchronized static void measureStatusBarHeight(Context context) {
		try {
			Class<?> c = Class.forName("com.android.internal.R$dimen");
			Object obj = c.newInstance();  
			Field field = c.getField("status_bar_height");  
			int x = Integer.parseInt(field.get(obj).toString());  
			statusBarHeight = context.getResources().getDimensionPixelSize(x);  
			
			LogUtil.v(TAG, "get mStatusBarHeight: "+ statusBarHeight);
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
}
