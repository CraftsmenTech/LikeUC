package cn.hi321.browser.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.hi321.browser.utils.DeviceInfoUtil;
import cn.hi321.browser2.R;

import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;

public class AboutActivity extends Activity implements OnClickListener {

	private ImageView mBack;
	private TextView mWeibo;
	private TextView tv_about_version;
	
//	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);

		// 自动更新
		UmengUpdateAgent.update(this);
		UmengUpdateAgent.setUpdateAutoPopup(false);

		mBack = (ImageView) findViewById(R.id.about_imageview_gohome);
		mWeibo = (TextView) findViewById(R.id.about_textview_weibo);
		tv_about_version = (TextView)findViewById(R.id.tv_about_version);
		tv_about_version.setText("版本："+DeviceInfoUtil.getAppVersionName(this));
		mWeibo.setOnClickListener(this);
		mBack.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		int id = v.getId();

		switch (id) {

		case R.id.about_imageview_gohome:
			finish();
			overridePendingTransition(R.anim.hold, R.anim.push_bottom_out);
			break;
		case R.id.about_textview_weibo:

			Intent intentWoBo = new Intent(AboutActivity.this,
					HomeActivity.class);
			intentWoBo.setData(Uri.parse("http://weibo.cn/321browser"));
			startActivity(intentWoBo);
			break;

		default:
			break;
		}

	}

	/*
	 * 友盟自动更新
	 */
	UmengUpdateListener updateListener = new UmengUpdateListener() {
		@Override
		public void onUpdateReturned(int updateStatus, UpdateResponse updateInfo) {
			switch (updateStatus) {
			case 0: // has update
				Log.i("--->", "callback result");
				UmengUpdateAgent.showUpdateDialog(AboutActivity.this,
						updateInfo);
				break;
			case 1: // has no update
				Toast.makeText(AboutActivity.this, "没有更新", Toast.LENGTH_SHORT)
						.show();
				break;
			case 2: // none wifi
				Toast.makeText(AboutActivity.this, "没有wifi连接， 只在wifi下更新",
						Toast.LENGTH_SHORT).show();
				break;
			case 3: // time out
				Toast.makeText(AboutActivity.this, "请求超时", Toast.LENGTH_SHORT)
						.show();
				break;
			case 4: // is updating
				/*
				 * Toast.makeText(mContext, "正在下载更新...", Toast.LENGTH_SHORT)
				 * .show();
				 */
				break;
			}

		}
	};

}
