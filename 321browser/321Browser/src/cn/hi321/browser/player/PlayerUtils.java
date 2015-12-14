package cn.hi321.browser.player;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * 
 * @author yanggf
 *
 */
public class PlayerUtils {
	
	private static final String TIP_3G_KEY = "tip_3g_key";
	private static final String TIP_3G_KEY_CONTENT= "tip_3g_key_content";
	
	public  static void setTip3GNework(Context context,boolean flag) {
		SharedPreferences  preference = context.getSharedPreferences(TIP_3G_KEY,context.MODE_PRIVATE );
		Editor editor = preference.edit();
		editor.putBoolean(TIP_3G_KEY_CONTENT, flag);
		editor.commit();
	}
	
	public static boolean  getTip3GNework(Context context) {
		SharedPreferences  preference = context.getSharedPreferences(TIP_3G_KEY,context.MODE_PRIVATE );
        return preference.getBoolean(TIP_3G_KEY_CONTENT, false);
	}
}
