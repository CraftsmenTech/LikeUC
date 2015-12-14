package cn.hi321.browser.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import cn.hi321.browser2.R;

import com.umeng.analytics.MobclickAgent;
import com.umeng.analytics.ReportPolicy;

/**
 * 启动页面
 * 
 * @author yanggf
 * 
 */
public class SplashActivity extends Activity {

	private Handler mHandler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		MobclickAgent.onError(this);// 友盟错误报告

		// 使用在线配置功能
		MobclickAgent.updateOnlineConfig(this);
		// 每次启动发送
		MobclickAgent
				.setDefaultReportPolicy(this, ReportPolicy.BATCH_AT_LAUNCH);
		// 友盟检查更新

		setContentView(R.layout.start_activity);

		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				enterHome();
			}
		}, 1000 * 3);
	}

	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	private void enterHome() {
		Intent intent = new Intent(this, HomeActivity.class);
		startActivity(intent);
		this.finish();
	};


}
