package cn.hi321.browser.ui.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ToggleButton;
import cn.hi321.browser.BrowserApp;
import cn.hi321.browser.utils.CommonTipFloatWindowInterface;
import cn.hi321.browser.utils.DeviceInfoUtil;
import cn.hi321.browser.utils.NetworkUtil;
import cn.hi321.browser2.R;

import com.umeng.analytics.MobclickAgent;

public class DownloadSetActivity extends Activity implements
		OnCheckedChangeListener, OnClickListener, CommonTipFloatWindowInterface {

	private static final String TAG = "SetActivity";

	/**
	 * 关于按钮
	 */
	private RelativeLayout mabout = null;

	private RelativeLayout shareFriendsLayout;

	private RelativeLayout bgDownloadLayout = null;

	private RelativeLayout mdownloadOnlyWifiLayout;

	private ToggleButton bgDownloadBox = null;

	private ToggleButton mDownloadOnlyWifiBox;

	private SharedPreferences bgDownloadSharePreference = null;

	private SharedPreferences.Editor bgDownloadEditor = null;

	private TextView title;

	private ImageView leftButton;
	protected RelativeLayout titlebar = null;

	private TextView comment_title;

	private ScrollView mScrollView;

	private SharedPreferences mStateSharePreference;

	private Editor mStateSharePreferenceEditor;

	public static final String BG_DOWNLOAD_CONFIG = "bg_download_config";

	private static final String IS_BACKGROUND_DOWNLOAD = "isBgDownload";

	public static final String IS_RECEIVE_PUSH_NOTIFICATION = "is_receive_push_notification";
	public static final String IS_DOWNLOAD_ONLY_WIFI = "isDownloadOnlyWifi";
	private static final String IS_USE_CACHE_LIBRARY = "isUseCacheLibrary";
	public static final String STATE_SHAREPREFERENCE = "statesharepreference";
	public static final String DOWNLOAD_NUM = "download_num";
	private static final int DIALOG_SINGLE_CHOICE = 1;

	private int mUserClickShareCount = 0;

	/**
	 * 用户点击分享按钮的时间
	 */
	private long mShareClickTime = 0;

	private AlertDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.download_set_layout);

		initUserCommentTitle();
		initComponent();
	}

	@Override
	protected void onDestroy() {
		mabout = null;
		super.onDestroy();
	}

	/**
	 * 初始化相关控件
	 */
	private void initComponent() {
		mabout = (RelativeLayout) findViewById(R.id.setaboutlayout);
		mScrollView = (ScrollView) findViewById(R.id.scrollview);
		shareFriendsLayout = (RelativeLayout) findViewById(R.id.set_activity_share_layout);
		bgDownloadLayout = (RelativeLayout) findViewById(R.id.set_activity_download_layout);
		mdownloadOnlyWifiLayout = (RelativeLayout) findViewById(R.id.set_download_only_wifi_layout);

		bgDownloadBox = (ToggleButton) findViewById(R.id.set_activity_download_switch_checkbox);
		mDownloadOnlyWifiBox = (ToggleButton) findViewById(R.id.set_download_only_wifi_checkbox);

		mabout.setOnClickListener(this);
		shareFriendsLayout.setOnClickListener(this);
		bgDownloadLayout.setOnClickListener(this);
		mdownloadOnlyWifiLayout.setOnClickListener(this);

		mStateSharePreference = getSharedPreferences(STATE_SHAREPREFERENCE,
				Context.MODE_PRIVATE);
		mStateSharePreferenceEditor = mStateSharePreference.edit();
		bgDownloadSharePreference = getSharedPreferences(BG_DOWNLOAD_CONFIG,
				Context.MODE_PRIVATE);
		bgDownloadEditor = bgDownloadSharePreference.edit();

		mDownloadOnlyWifiBox.setChecked(mStateSharePreference.getBoolean(
				IS_DOWNLOAD_ONLY_WIFI, true));
		bgDownloadBox.setChecked(bgDownloadSharePreference.getBoolean(
				IS_BACKGROUND_DOWNLOAD, true));

		bgDownloadBox.setOnCheckedChangeListener(this);
		mDownloadOnlyWifiBox.setOnCheckedChangeListener(this);
	}

	protected void popIfContinueDownloadDialog(boolean isChecked) {
		if (NetworkUtil.reportNetType(this) == 2
				&& !isChecked
				&& BrowserApp.getInstance().getDownloadManager()
						.getQueuedDownloads().size() > 0) {
			checkIfContinueDownloadDialog(isChecked);
		} else {
			sendBroadCast(isChecked);
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		mScrollView.scrollTo(0, 0);
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		// 点击关于时的处理
		case R.id.setaboutlayout:
			intent = new Intent();
			intent.setClass(this, AboutActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.fade, R.anim.hold);
			break;
		case R.id.set_activity_share_layout:
			mUserClickShareCount++;
			if ((System.currentTimeMillis() - mShareClickTime) > 1000
					|| mUserClickShareCount >= 3) {
				mUserClickShareCount = 0;
				shareToFriends();
			}
			mShareClickTime = System.currentTimeMillis();
			break;
		case R.id.set_activity_download_layout:
			if (bgDownloadBox.isChecked()) {
				bgDownloadBox.setChecked(false);
				bgDownloadEditor.putBoolean(IS_BACKGROUND_DOWNLOAD, false);
			} else {
				bgDownloadBox.setChecked(true);
				bgDownloadEditor.putBoolean(IS_BACKGROUND_DOWNLOAD, true);
			}
			bgDownloadEditor.commit();
			break;
		case R.id.set_download_only_wifi_layout:
			if (mDownloadOnlyWifiBox.isChecked()) {
				mDownloadOnlyWifiBox.setChecked(false);
				mStateSharePreferenceEditor.putBoolean(IS_DOWNLOAD_ONLY_WIFI,
						false);
			} else {
				mDownloadOnlyWifiBox.setChecked(true);
				mStateSharePreferenceEditor.putBoolean(IS_DOWNLOAD_ONLY_WIFI,
						true);
			}
			mStateSharePreferenceEditor.commit();
			popIfContinueDownloadDialog(mDownloadOnlyWifiBox.isChecked());
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			finish();
			overridePendingTransition(R.anim.hold, R.anim.push_bottom_out);
			return true;
		}
		return false;
	}

	/**
	 * 调取分享接口API add by donggx
	 */
	private void shareToFriends() {
		Intent intent = new Intent(Intent.ACTION_SEND);
		// intent.setType("image/*");
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_SUBJECT,
				getString(R.string.share_to_friends));
		intent.putExtra(Intent.EXTRA_TEXT,
				getString(R.string.share_to_friends_content));
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(Intent.createChooser(intent, getTitle()));
		overridePendingTransition(R.anim.fade, R.anim.hold);
	}

	private void initUserCommentTitle() {

		titlebar = (RelativeLayout) this.findViewById(R.id.set_titlebar);
		/*
		 * if(titlebar != null) titlebar.setBackgroundColor(Color.WHITE);
		 */
		title = (TextView) findViewById(R.id.set_comment_title);
		leftButton = (ImageView) this.findViewById(R.id.set_left_button);
		leftButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DownloadSetActivity.this.finish();
				overridePendingTransition(R.anim.hold, R.anim.push_bottom_out);
			}
		});
		comment_title = (TextView) this.findViewById(R.id.set_comment_title);

		comment_title.setText(getString(R.string.setting));
		// setTitleSize();

		RelativeLayout setDownlaod = (RelativeLayout) findViewById(R.id.setDownlaod);
		setDownlaod.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				showDialog(DIALOG_SINGLE_CHOICE);
			}
		});
	}

	private void setTitleSize() {
		int deviceWidthPixels = DeviceInfoUtil
				.getWidthPixels(DownloadSetActivity.this);
		if (deviceWidthPixels <= 480 && deviceWidthPixels > 320) {
			setCommentTitleSize(20);
			setTitleBarHeight(80);
			setTitleWidhtAndHight(316, 80);
		} else if (deviceWidthPixels <= 320 && deviceWidthPixels > 0) {
			setCommentTitleSize(18);
			setTitleBarHeight(54);
			setTitleWidhtAndHight(208, 54);
		} else if (deviceWidthPixels <= 720 && deviceWidthPixels > 480) {
			setCommentTitleSize(20);
			setTitleBarHeight(120);
			setTitleWidhtAndHight(460, 120);
			setLeftButtonMargin(17);
		} else if (deviceWidthPixels <= 800 && deviceWidthPixels > 720) {
			setCommentTitleSize(25);
			setTitleBarHeight(136);
			setLeftButtonMargin(17);
			setTitleWidhtAndHight(500, 136);
		} else {
			setCommentTitleSize(25);
			setTitleBarHeight(136);
			setLeftButtonMargin(17);
			setTitleWidhtAndHight(500, 136);
		}

	}

	private void setTitleWidhtAndHight(int width, int height) {
		title.setWidth(width);
		title.setHeight(height);
	}

	private void setLeftButtonMargin(int leftMargin) {
		LinearLayout.LayoutParams lp = (LayoutParams) leftButton
				.getLayoutParams();
		lp.leftMargin = leftMargin;
		leftButton.setLayoutParams(lp);
	}

	private void setTitleBarHeight(int highdip) {
		if (titlebar != null) {
			RelativeLayout.LayoutParams parma = new RelativeLayout.LayoutParams(
					LayoutParams.FILL_PARENT, highdip);
			titlebar.setLayoutParams(parma);
		}
	}

	private void setCommentTitleSize(int size) {
		comment_title.setTextSize(size);
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		/*
		 * case DIALOG_LIST: return new AlertDialog.Builder(this)
		 * .setTitle(R.string.download_num) .setItems(R.array.stream_codecs, new
		 * DialogInterface.OnClickListener() { public void
		 * onClick(DialogInterface dialog, int which) {
		 * 
		 * User clicked so do some stuff String[] items =
		 * getResources().getStringArray(R.array.stream_codecs); new
		 * AlertDialog.Builder(SetActivity.this) .setMessage("You selected: " +
		 * which + " , " + items[which]) .show(); } }) .create();
		 */
		case DIALOG_SINGLE_CHOICE:
			bgDownloadSharePreference = getSharedPreferences(
					BG_DOWNLOAD_CONFIG, Context.MODE_PRIVATE);
			String download_num = bgDownloadSharePreference.getString(
					DOWNLOAD_NUM, "3");
			bgDownloadEditor = bgDownloadSharePreference.edit();
			Dialog dialog = new AlertDialog.Builder(this)
					.setTitle(R.string.download_num)
					.setSingleChoiceItems(R.array.stream_codecs,
							Integer.parseInt(download_num) - 1,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {

									String[] items = getResources()
											.getStringArray(
													R.array.stream_codecs);
									bgDownloadEditor.putString(DOWNLOAD_NUM,
											items[which]);
									bgDownloadEditor.commit();
									dialog.dismiss();
								}
							}).create();
			return dialog;
		}
		return null;
	}

	private void sendBroadCast(boolean isDownloadOnlyWifi) {
		Intent intent = new Intent();
		intent.setAction("com.funshion.video.DOWNLOADONLYWIFI");
		intent.putExtra("isDownloadOnlyWifi", isDownloadOnlyWifi);
		sendBroadcast(intent);
	}

	private void checkIfContinueDownloadDialog(final boolean isChecked) {
		Builder customBuilder = new Builder(this);
		customBuilder
				.setTitle(R.string.tip)
				.setMessage(R.string.wireless_tip)
				.setPositiveButton(R.string.continue_download,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								sendBroadCast(isChecked);
								dialog.dismiss();
							}
						})
				.setNegativeButton(R.string.pause_download,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						}).setOnKeyListener(new OnKeyListener() {
					@Override
					public boolean onKey(DialogInterface dialog, int keyCode,
							KeyEvent event) {
						if (keyCode == KeyEvent.KEYCODE_BACK
								&& event.getRepeatCount() == 0) {
							dialog.dismiss();
						}
						return false;
					}
				});
		if (dialog == null)
			dialog = customBuilder.create();
		dialog.show();
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		switch (buttonView.getId()) {
		case R.id.set_activity_download_switch_checkbox:
			bgDownloadEditor.putBoolean(IS_BACKGROUND_DOWNLOAD, isChecked);
			bgDownloadEditor.commit();
			break;
		case R.id.set_download_only_wifi_checkbox:
			mStateSharePreferenceEditor.putBoolean(IS_DOWNLOAD_ONLY_WIFI,
					isChecked);
			mStateSharePreferenceEditor.commit();
			popIfContinueDownloadDialog(isChecked);
			break;
		default:
			break;
		}
	}

}
