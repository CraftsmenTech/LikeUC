package cn.hi321.browser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import cn.hi321.browser.ui.activities.HomeActivity;
import cn.hi321.browser.utils.LogUtil;

/**
 * 
 * @author yanggf
 * 
 */
public class CrashHandlerActivity extends Activity {
	public static final String TAG = "CrashHandler";

	protected void onCreate(Bundle state) {
		super.onCreate(state);
		LogUtil.e("CrashHandle onCreate");
		startActivity(new Intent(CrashHandlerActivity.this, HomeActivity.class));
		finish();
	}

}