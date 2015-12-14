package cn.hi321.browser.ui.activities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.DownloadListener;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebIconDatabase;
import android.webkit.WebView;
import android.webkit.WebView.HitTestResult;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;
import cn.hi321.browser.BrowserApp;
import cn.hi321.browser.CrashHandler;
import cn.hi321.browser.config.Constants;
import cn.hi321.browser.controllers.Controller;
import cn.hi321.browser.download.DownloadActivity;
import cn.hi321.browser.download.DownloadHelper;
import cn.hi321.browser.events.EventConstants;
import cn.hi321.browser.events.EventController;
import cn.hi321.browser.events.IDownloadEventsListener;
import cn.hi321.browser.greendroid.QuickAction;
import cn.hi321.browser.greendroid.QuickActionGrid;
import cn.hi321.browser.greendroid.QuickActionWidget;
import cn.hi321.browser.greendroid.QuickActionWidget.OnQuickActionClickListener;
import cn.hi321.browser.model.MediaItem;
import cn.hi321.browser.model.items.DownloadItem;
import cn.hi321.browser.player.SystemPlayer;
import cn.hi321.browser.preferences.PreferencesActivity;
import cn.hi321.browser.providers.BookmarksProviderWrapper;
import cn.hi321.browser.providers.BookmarksProviderWrapper.BookmarksSource;
import cn.hi321.browser.ui.components.CustomWebChromeClient;
import cn.hi321.browser.ui.components.CustomWebView;
import cn.hi321.browser.ui.components.CustomWebViewClient;
import cn.hi321.browser.ui.runnables.HistoryUpdater;
import cn.hi321.browser.utils.AnimationManager;
import cn.hi321.browser.utils.ApplicationUtils;
import cn.hi321.browser.utils.IOUtils;
import cn.hi321.browser.utils.LogUtil;
import cn.hi321.browser.utils.UrlUtils;
import cn.hi321.browser.utils.UserPreference;
import cn.hi321.browser.utils.Utils;
import cn.hi321.browser.view.PopMenu;
import cn.hi321.browser.view.PopWindowAddNewPage;
import cn.hi321.browser2.R;

import com.umeng.analytics.MobclickAgent;
import com.umeng.fb.FeedbackAgent;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;

public class HomeActivity extends Activity implements OnTouchListener,
		IDownloadEventsListener {

	public static boolean isNeedLoadHide = false;// 不需要加载
	private static String TAG = "HomeActivity";

	public static HomeActivity INSTANCE = null;

	private static final int FLIP_PIXEL_THRESHOLD = 200;
	private static final int FLIP_TIME_THRESHOLD = 400;

	private static final int CONTEXT_MENU_OPEN = Menu.FIRST + 10;
	private static final int CONTEXT_MENU_OPEN_IN_NEW_TAB = Menu.FIRST + 11;
	private static final int CONTEXT_MENU_DOWNLOAD = Menu.FIRST + 12;
	private static final int CONTEXT_MENU_COPY = Menu.FIRST + 13;
	private static final int CONTEXT_MENU_SEND_MAIL = Menu.FIRST + 14;
	private static final int CONTEXT_MENU_SHARE = Menu.FIRST + 15;

	private static final int OPEN_BOOKMARKS_HISTORY_ACTIVITY = 0;
	private static final int OPEN_DOWNLOADS_ACTIVITY = 1;
	public static final int OPEN_FILE_CHOOSER_ACTIVITY = 2;

	protected static final FrameLayout.LayoutParams COVER_SCREEN_PARAMS = new FrameLayout.LayoutParams(
			ViewGroup.LayoutParams.MATCH_PARENT,
			ViewGroup.LayoutParams.MATCH_PARENT);

	protected LayoutInflater mInflater = null;

	private RelativeLayout mTopBar;
	private LinearLayout mBottomBar;

	private LinearLayout mFindBar;
	private ImageButton mFindPreviousButton;
	private ImageButton mFindNextButton;
	private ImageButton mFindCloseButton;

	private EditText mFindText;

	private Button mMoreButton;

	private Button mSearchButton;
	private ImageButton mToolMenuBtn;
	private Button mToolsButton;
	public TextView mUrlEditText;
	private ImageView delectView;
	public ProgressBar mProgressBar;

	private ImageView mBubbleRightView;
	private ImageView mBubbleLeftView;
	public static int selectePosition = -1;

	public CustomWebView mCurrentWebView;
	private List<CustomWebView> mWebViews;

	private FrameLayout mContentView;

	private ImageButton mPreviousButton;
	private ImageButton mNextButton;

	private ImageButton mNewTabButton;

	private boolean mUrlBarVisible;
	private boolean mToolsActionGridVisible = false;
	private boolean mFindDialogVisible = false;

	private ImageButton returnHomeTabBtn;

	public ViewFlipper mViewFlipper;
	private TextView mNewTabButtonNumber;

	private GestureDetector mGestureDetector;

	private SwitchTabsMethod mSwitchTabsMethod = SwitchTabsMethod.BOTH;

	private QuickActionGrid mToolsActionGrid;

	private ValueCallback<Uri> mUploadMessage;

	private OnSharedPreferenceChangeListener mPreferenceChangeListener;

	private View mCustomView;

	private FrameLayout mFullscreenContainer;

	private WebChromeClient.CustomViewCallback mCustomViewCallback;

	private PopWindowAddNewPage mPopWindowAddNewPage;// 添加新标签页
	private PopMenu popMenu;

	private enum SwitchTabsMethod {
		BUTTONS, FLING, BOTH
	}

	public ValueCallback<Uri> getUploadMessage() {
		return mUploadMessage;
	}

	public void setUploadMessage(ValueCallback<Uri> mUploadMessage) {
		this.mUploadMessage = mUploadMessage;
	}

	private RefreshDataBroadcast mRefreshDataBroadcast;

	// [start]生命周期
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		INSTANCE = HomeActivity.this;
		Controller.getInstance().setPreferences(
				PreferenceManager.getDefaultSharedPreferences(this));
		Constants.initializeConstantsFromResources(this);

		CrashHandler crashHandler = CrashHandler.getInstance();
		crashHandler.init(INSTANCE);

		UserPreference.ensureIntializePreference(HomeActivity.this);

		boolean isFullScreen = UserPreference.read("fullScreen", false);
		if (isFullScreen) {
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
					WindowManager.LayoutParams.FLAG_FULLSCREEN);
		} else {
			getWindow().setFlags(
					WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN,
					WindowManager.LayoutParams.FLAG_FULLSCREEN);
		}
		isNeedLoadHide = false;
		// setSereenLuminance();

		setContentView(R.layout.above_slidingmenu);
		EventController.getInstance().addDownloadListener(this);
		mRefreshDataBroadcast = new RefreshDataBroadcast();
		registerReceiver(mRefreshDataBroadcast, new IntentFilter("search"));

		mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		buildComponents();

		mViewFlipper.removeAllViews();

		updateSwitchTabsMethod();
		updateBookmarksDatabaseSource();

		registerPreferenceChangeListener();

		Intent i = getIntent();
		if (i.getData() != null) {
			// App first launch from another app.
			addTab(false);
			navigateToUrl(i.getDataString());// i.getDataString()获得所安装的包名
		} else {

			boolean lastPageRestored = false;
			if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean(
					Constants.PREFERENCES_BROWSER_RESTORE_LAST_PAGE, false)) {
				if (savedInstanceState != null) {
					String savedUrl = savedInstanceState
							.getString(Constants.EXTRA_SAVED_URL);
					if (savedUrl != null) {
						addTab(false);
						navigateToUrl(savedUrl);
						lastPageRestored = true;
					}
				}
			}

			if (!lastPageRestored) {
				addTab(true);
			}
		}

		initializeWebIconDatabase();
		// isLANDSCAPE = false;

		// 自动更新
		UmengUpdateAgent.update(this);
		UmengUpdateAgent.setUpdateAutoPopup(true);

		// 用户反馈
		/*
		 * FeedbackAgent agent = new FeedbackAgent(MainActivity.this);
		 * agent.sync();
		 */

	}

	/**
	 * Initialize the Web icons database.
	 */
	private void initializeWebIconDatabase() {

		final WebIconDatabase db = WebIconDatabase.getInstance();
		db.open(getDir("icons", 0).getPath());
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		WebIconDatabase.getInstance().close();

		mCurrentWebView.setVisibility(View.GONE);
		if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean(
				Constants.PREFERENCES_PRIVACY_CLEAR_CACHE_ON_EXIT, false)) {
			mCurrentWebView.clearCache(true);
		}

		EventController.getInstance().removeDownloadListener(this);

		PreferenceManager.getDefaultSharedPreferences(this)
				.unregisterOnSharedPreferenceChangeListener(
						mPreferenceChangeListener);

		unregisterReceiver(mRefreshDataBroadcast);
	}

	/**
	 * 连续按两次返回键就退出
	 */

	@Override
	protected void onResume() {
		mCurrentWebView.doOnResume();
		MobclickAgent.onResume(this);
		super.onResume();
	}

	/**
	 * Show a toast alert on tab switch.
	 */
	private void showToastOnTabSwitch() {
		if (Controller
				.getInstance()
				.getPreferences()
				.getBoolean(Constants.PREFERENCES_SHOW_TOAST_ON_TAB_SWITCH,
						true)) {
			String text;
			if (mCurrentWebView.getTitle() != null) {
				text = String.format(
						getString(R.string.Main_ToastTabSwitchFullMessage),
						mViewFlipper.getDisplayedChild() + 1,
						mCurrentWebView.getTitle());
			} else {
				text = String.format(
						getString(R.string.Main_ToastTabSwitchMessage),
						mViewFlipper.getDisplayedChild() + 1);
			}
			// Toast.makeText(this,"测试啊啊啊"+ text, Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * Show the previous tab, if any.
	 */
	private void showPreviousTab(boolean resetToolbarsRunnable) {

		if (mViewFlipper.getChildCount() > 1) {

			if (mFindDialogVisible) {
				closeFindDialog();
			}

			mCurrentWebView.doOnPause();

			mViewFlipper.setInAnimation(AnimationManager.getInstance()
					.getInFromLeftAnimation());
			mViewFlipper.setOutAnimation(AnimationManager.getInstance()
					.getOutToRightAnimation());

			mViewFlipper.showPrevious();

			mCurrentWebView = mWebViews.get(mViewFlipper.getDisplayedChild());

			mCurrentWebView.doOnResume();

			showToastOnTabSwitch();

			updateUI();
		}
	}

	private int mOriginalOrientation;

	public void showCustomView(View view,
			WebChromeClient.CustomViewCallback callback, boolean isUseNew) {
		if (isUseNew) {
			if (mCustomView != null) {
				callback.onCustomViewHidden();
				return;
			}
			setToolbarsVisibility(false);
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
					WindowManager.LayoutParams.FLAG_FULLSCREEN);

			if (Build.VERSION.SDK_INT >= 14) {
				mFullscreenContainer.addView(view);
				mCustomView = view;
				mCustomViewCallback = callback;
				mOriginalOrientation = getRequestedOrientation();
				mContentView.setVisibility(View.INVISIBLE);
				mFullscreenContainer.setVisibility(View.VISIBLE);
				mFullscreenContainer.bringToFront();
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			}
		} else {
			if (mCustomView != null) {
				callback.onCustomViewHidden();
				return;
			}
			mOriginalOrientation = getRequestedOrientation();

			// add by yanggf
			HomeActivity.this.getWindow().getDecorView();
			FrameLayout decor = (FrameLayout) getWindow().getDecorView();
			mFullscreenContainer = new FullscreenHolder(HomeActivity.this);
			mFullscreenContainer.addView(view, COVER_SCREEN_PARAMS);
			decor.addView(mFullscreenContainer, COVER_SCREEN_PARAMS);
			mCustomView = view;
			setStatusBarVisibility(false);
			mCustomViewCallback = callback;
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}

	}

	public void hideCustomView(boolean isUseNew) {

		Log.e(TAG, "onHideCustomView()===");
		if (isUseNew) {

			mContentView.setVisibility(View.VISIBLE);
			if (mCustomView == null) {
				return;
			}

			boolean isFullScreen = UserPreference.read("fullScreen", false);
			if (isFullScreen) {
				setToolbarsVisibility(false);
				getWindow().setFlags(
						WindowManager.LayoutParams.FLAG_FULLSCREEN,
						WindowManager.LayoutParams.FLAG_FULLSCREEN);
			} else {
				setToolbarsVisibility(true);
				getWindow().setFlags(
						WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN,
						WindowManager.LayoutParams.FLAG_FULLSCREEN);
			}
			mCustomView.setVisibility(View.GONE);
			mFullscreenContainer.removeView(mCustomView);
			mFullscreenContainer.setVisibility(View.GONE);
			mCustomView = null;
			try {
				mCustomViewCallback.onCustomViewHidden();
			} catch (Exception e) {
			}
			// Show the content view.

			setRequestedOrientation(mOriginalOrientation);
			if (mCurrentWebView != null) {
				mCurrentWebView.doOnResume();
			}

		} else {
			if (mCustomView == null)
				return;

			setStatusBarVisibility(true);
			try {
				FrameLayout decor = (FrameLayout) getWindow().getDecorView();
				if (decor != null && mFullscreenContainer != null) {
					decor.removeView(mFullscreenContainer);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				mCustomViewCallback.onCustomViewHidden();
			} catch (Exception e) {
				e.printStackTrace();
			}
			setRequestedOrientation(mOriginalOrientation);
			if (mCurrentWebView != null) {
				mCurrentWebView.doOnResume();
			}
			mFullscreenContainer = null;
			mCustomView = null;

		}

	}

	/**
	 * Show the next tab, if any.
	 */
	private void showNextTab(boolean resetToolbarsRunnable) {

		if (mViewFlipper.getChildCount() > 1) {

			if (mFindDialogVisible) {
				closeFindDialog();
			}

			mCurrentWebView.doOnPause();

			mViewFlipper.setInAnimation(AnimationManager.getInstance()
					.getInFromRightAnimation());
			mViewFlipper.setOutAnimation(AnimationManager.getInstance()
					.getOutToLeftAnimation());

			mViewFlipper.showNext();

			mCurrentWebView = mWebViews.get(mViewFlipper.getDisplayedChild());

			mCurrentWebView.doOnResume();
			showToastOnTabSwitch();

			// updatePreviousNextTabViewsVisibility();

			updateUI();
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {

		// hideKeyboard(true);

		boolean isFullScreen = UserPreference.read("fullScreen", false);
		if (isFullScreen) {
			setToolbarsVisibility(false);
		}

		return mGestureDetector.onTouchEvent(event);
	}

	/**
	 * Check if the url is in the AdBlock white list.
	 * 
	 * @param url
	 *            The url to check
	 * @return true if the url is in the white list
	 */
	private boolean checkInAdBlockWhiteList(String url) {

		if (url != null) {
			boolean inList = false;
			Iterator<String> iter = Controller.getInstance()
					.getAdBlockWhiteList(this).iterator();
			while ((iter.hasNext()) && (!inList)) {
				if (url.contains(iter.next())) {
					inList = true;
				}
			}
			return inList;
		} else {
			return false;
		}
	}

	/**  
	 * 网页加载结束执行方法
	 * 
	 * @param url
	 */
	public void onPageFinished(String url) {
		updateUI();
		// updateGoButton();
		// cancelProgressBar();
		boolean isFullScreen = UserPreference.read("fullScreen", false);
		if (isFullScreen) {
			setToolbarsVisibility(false);
		} else {
			setToolbarsVisibility(true);
		}
		if ((Controller.getInstance().getPreferences().getBoolean(
				Constants.PREFERENCES_ADBLOCKER_ENABLE, true))
				&& (!checkInAdBlockWhiteList(mCurrentWebView.getUrl()))) {
			mCurrentWebView.loadAdSweep();
		}

		WebIconDatabase.getInstance().retainIconForPageUrl(
				mCurrentWebView.getUrl());

	}

	/**
	 * 页面加载开始时调用
	 * 
	 * @param url
	 */
	public void onPageStarted(String url) {
		if (mFindDialogVisible) {
			closeFindDialog();
		}

		mNextButton.setEnabled(false);

		updateGoButton();
		updateUI();

		if (url.equals(Constants.URL_ABOUT_HOME)
				|| url.equals(Constants.URL_ABOUT_START)
				|| url.equals(Constants.URL_ABOUT_VIDEO)) {
			mProgressBar.setVisibility(View.GONE);
		} else {
			showProgressBar();
		}

		boolean isFullScreen = UserPreference.read("fullScreen", false);
		if (isFullScreen) {
			setToolbarsVisibility(false);
		} else {
			setToolbarsVisibility(true);
		}
	}

	public void onUrlLoading(String url) {
		setToolbarsVisibility(true);
	}

	public void onMailTo(String url) {
		Intent sendMail = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		startActivity(sendMail);
	}

	public void onExternalApplicationUrl(String url) {
		try {

			Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
			startActivity(i);

		} catch (Exception e) {
			 e.printStackTrace();
			// Notify user that the vnd url cannot be viewed.
			new AlertDialog.Builder(this)
					.setTitle(R.string.Main_VndErrorTitle)
					.setMessage(
							String.format(
									getString(R.string.Main_VndErrorMessage),
									url))
					.setPositiveButton(android.R.string.ok,
							new AlertDialog.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
								}
							}).setCancelable(true).create().show();
		}
	}

	public void setHttpAuthUsernamePassword(String host, String realm,
			String username, String password) {
		mCurrentWebView.setHttpAuthUsernamePassword(host, realm, username,
				password);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {

		if ((item != null) && (item.getIntent() != null)) {
			Bundle b = item.getIntent().getExtras();

			switch (item.getItemId()) {
			case CONTEXT_MENU_OPEN:
				if (b != null) {
					navigateToUrl(b.getString(Constants.EXTRA_ID_URL));
				}
				return true;

			case CONTEXT_MENU_OPEN_IN_NEW_TAB:
				if (b != null) {
					addTab(false, mViewFlipper.getDisplayedChild());
					navigateToUrl(b.getString(Constants.EXTRA_ID_URL));
				}
				return true;

			case CONTEXT_MENU_DOWNLOAD:

				if (mCurrentWebView != null
						&& mCurrentWebView.getUrl() != null
						&& mCurrentWebView.getUrl().equals(
								Constants.URL_ABOUT_START)) {
					Toast.makeText(HomeActivity.this, "此图片不支持下载", 1).show();
				} else {
					if (b != null) {
						doDownloadStart(b.getString(Constants.EXTRA_ID_URL),
								null, null, null, 0);
					}
				}
				return true;
			case CONTEXT_MENU_COPY:
				if (b != null) {
					ApplicationUtils.copyTextToClipboard(this,
							b.getString(Constants.EXTRA_ID_URL),
							getString(R.string.Commons_UrlCopyToastMessage));
				}
				return true;
			case CONTEXT_MENU_SHARE:
				if (mCurrentWebView != null
						&& mCurrentWebView.getUrl() != null
						&& mCurrentWebView.getUrl().equals(
								Constants.URL_ABOUT_START)) {
					ApplicationUtils.sharePage(HomeActivity.this,
							"传递正能量的321浏览器火爆登场 ", " http://www.hi321.cn/");
				} else {
					if (b != null) {
						ApplicationUtils.sharePage(this, "",
								b.getString(Constants.EXTRA_ID_URL));
					}
				}

				return true;
			default:
				return super.onContextItemSelected(item);
			}
		}

		return super.onContextItemSelected(item);
	}

	@Override
	public void onDownloadEvent(String event, Object data) {
		if (event.equals(EventConstants.EVT_DOWNLOAD_ON_FINISHED)) {

			DownloadItem item = (DownloadItem) data;

			if (item.getErrorMessage() == null) {
				Toast.makeText(this,
						getString(R.string.Main_DownloadFinishedMsg),
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(
						this,
						getString(R.string.Main_DownloadErrorMsg,
								item.getErrorMessage()), Toast.LENGTH_SHORT)
						.show();
			}
		}
	}

	/**
	 * Gesture listener implementation.
	 */
	private class GestureListener extends
			GestureDetector.SimpleOnGestureListener {

		@Override
		public boolean onDoubleTap(MotionEvent e) {
			mCurrentWebView.zoomIn();
			return super.onDoubleTap(e);
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			if (isSwitchTabsByFlingEnabled()) {
				if (e2.getEventTime() - e1.getEventTime() <= FLIP_TIME_THRESHOLD) {
					if (e2.getX() > (e1.getX() + FLIP_PIXEL_THRESHOLD)) {

						showPreviousTab(false);
						return false;
					}

					// going forwards: pushing stuff to the left
					if (e2.getX() < (e1.getX() - FLIP_PIXEL_THRESHOLD)) {

						showNextTab(false);
						return false;
					}
				}
			}

			return super.onFling(e1, e2, velocityX, velocityY);
		}

	}

	 class FullscreenHolder extends FrameLayout {

		public FullscreenHolder(Context ctx) {
			super(ctx);
			setBackgroundColor(ctx.getResources().getColor(
					android.R.color.black));
		}

		@Override
		public boolean onTouchEvent(MotionEvent evt) {
			return true;
		}

	}

	@Override
	/**
	 * 创建MENU
	 */
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add("menu");// 必须创建一项
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	/**
	 * 拦截MENU
	 */
	public boolean onMenuOpened(int featureId, Menu menu) {

		boolean isFullScreen = UserPreference.read("fullScreen", false);
		if (isFullScreen) {
			setToolbarsVisibility(isFullScreen);
			if (mPopWindowAddNewPage != null && mPopWindowAddNewPage.isShow()) {
				mPopWindowAddNewPage.dismiss();
			}
			if (popMenu.isShow()) {
				popMenu.dismiss();
			} else {
				popMenu.show(mToolMenuBtn);
			}
		} else {
			if (mPopWindowAddNewPage != null && mPopWindowAddNewPage.isShow()) {
				mPopWindowAddNewPage.dismiss();
			}
			if (popMenu.isShow()) {
				popMenu.dismiss();
			} else {
				popMenu.show(mToolMenuBtn);
			}
		}

		return false;// 返回为true 则显示系统menu
	}

	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putString(Constants.EXTRA_SAVED_URL, mCurrentWebView.getUrl());

		super.onSaveInstanceState(outState);
	}

	/**
	 * 这是屏幕亮暗
	 * */

	private void setSereenLuminance() {
		// 取得当前亮度
		int normal = Settings.System.getInt(getContentResolver(),
				Settings.System.SCREEN_BRIGHTNESS, 255);
		WindowManager.LayoutParams wl = getWindow().getAttributes();
		// 进度条绑定最大亮度，255是最大亮度

		float tmpFloat = (float) normal / 255;
		if (tmpFloat > 0 && tmpFloat <= 1) {
			wl.screenBrightness = tmpFloat;
		}
		getWindow().setAttributes(wl);
	}

	/**
	 * Handle url request from external apps.
	 * 
	 * @param intent
	 *            The intent.
	 */
	@Override
	protected void onNewIntent(Intent intent) {
		if (intent.getData() != null) {
			addTab(false);
			navigateToUrl(intent.getDataString());
		}

		setIntent(intent);

		super.onNewIntent(intent);
	}

	/**
	 * Restart the application.
	 */
	public void restartApplication() {
		PendingIntent intent = PendingIntent.getActivity(this.getBaseContext(),
				0, new Intent(getIntent()), getIntent().getFlags());
		AlarmManager mgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 2000, intent);
		System.exit(2);
	}

	/**
	 * Create main UI.
	 */
	private void buildComponents() {
		popMenu = new PopMenu(HomeActivity.this, handler);
		mToolsActionGrid = new QuickActionGrid(this);
		mToolsActionGrid.addQuickAction(new QuickAction(this,
				R.drawable.ic_btn_home, R.string.QuickAction_Home));
		mToolsActionGrid.addQuickAction(new QuickAction(this,
				R.drawable.ic_btn_share, R.string.QuickAction_Share));
		mToolsActionGrid.addQuickAction(new QuickAction(this,
				R.drawable.ic_btn_find, R.string.QuickAction_Find));
		// mToolsActionGrid.addQuickAction(new QuickAction(this,
		// R.drawable.ic_btn_select, R.string.QuickAction_SelectText));
		mToolsActionGrid.addQuickAction(new QuickAction(this,
				R.drawable.notification_start, R.string.Download));

		mToolsActionGrid
				.setOnQuickActionClickListener(new OnQuickActionClickListener() {
					@Override
					public void onQuickActionClicked(QuickActionWidget widget,
							int position) {
						switch (position) {
						case 0:
							navigateToHome();
							break;
						case 1:
							System.out.println(" mCurrentWebView.getTitle()=="
									+ mCurrentWebView.getTitle());
							if (HomeActivity.INSTANCE.mCurrentWebView != null
									&& HomeActivity.INSTANCE.mCurrentWebView
											.getUrl().equals(
													Constants.URL_ABOUT_START)) {// 是主页
								ApplicationUtils.sharePage(HomeActivity.this,
										"传递正能量的321浏览器火爆登场 ",
										" http://www.hi321.cn/");

							} else {
								ApplicationUtils.sharePage(HomeActivity.this,
										mCurrentWebView.getTitle(),
										mCurrentWebView.getUrl());
							}
							break;
						case 2:
							// Somewhat dirty hack: when the find dialog was
							// shown from a QuickAction,
							// the soft keyboard did not show... Hack is to wait
							// a little before showing
							// the file dialog through a thread.
							startShowFindDialogRunnable();
							break;
						case 3:
							// swithToSelectAndCopyTextMode();//这是文本选取复制
							Intent intent = new Intent(HomeActivity.this,
									DownloadActivity.class);
							startActivity(intent);
							overridePendingTransition(R.anim.hold,
									R.anim.push_bottom_out);

							break;
						case 4:
							String currentUrl = mUrlEditText.getText()
									.toString();

							// Do not reload mobile view if already on it.
							if (!currentUrl
									.startsWith(Constants.URL_GOOGLE_MOBILE_VIEW_NO_FORMAT)) {
								String url = String.format(
										Constants.URL_GOOGLE_MOBILE_VIEW,
										mUrlEditText.getText().toString());
								navigateToUrl(url);
							}
							break;
						}
					}
				});

		mToolsActionGrid
				.setOnDismissListener(new PopupWindow.OnDismissListener() {
					@Override
					public void onDismiss() {
						mToolsActionGridVisible = false;
					}
				});

		mGestureDetector = new GestureDetector(this, new GestureListener());

		mUrlBarVisible = true;

		mWebViews = new ArrayList<CustomWebView>();
		Controller.getInstance().setWebViewList(mWebViews);

		mBubbleRightView = (ImageView) findViewById(R.id.BubbleRightView);
		mBubbleRightView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setToolbarsVisibility(true);
			}
		});
		mBubbleRightView.setVisibility(View.GONE);

		mBubbleLeftView = (ImageView) findViewById(R.id.BubbleLeftView);
		mBubbleLeftView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setToolbarsVisibility(true);
			}
		});
		mBubbleLeftView.setVisibility(View.GONE);

		mViewFlipper = (ViewFlipper) findViewById(R.id.ViewFlipper);
		mTopBar = (RelativeLayout) findViewById(R.id.BarLayout);
		mTopBar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Dummy event to steel it from the WebView, in case of clicking
				// between the buttons.
			}
		});

		mBottomBar = (LinearLayout) findViewById(R.id.BottomBarLayout);
		mBottomBar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Dummy event to steel it from the WebView, in case of clicking
				// between the buttons.
			}
		});

		mFindBar = (LinearLayout) findViewById(R.id.findControls);
		mFindBar.setVisibility(View.GONE);

		mSearchButton = (Button) findViewById(R.id.searchBtn);
		mSearchButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(HomeActivity.this,
						SearchsActivity.class);
				startActivity(intent);
			}
		});
		mUrlEditText = (TextView) findViewById(R.id.UrlText);
		delectView = (ImageView) findViewById(R.id.delectid);
		mUrlEditText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(HomeActivity.this, SearchActivity.class);
				String url = (String) mUrlEditText.getTag();
				if (url != null && url.length() > 6) {
					i.putExtra("url", url);
				}
				HomeActivity.this.startActivity(i);
			}
		});
		delectView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (isDelet) {
					// 表示删除
					mCurrentWebView.stopLoading();
				} else {
					// 刷新
					mCurrentWebView.reload();
				}
			}
		});
		mToolMenuBtn = (ImageButton) findViewById(R.id.ToolMenuBtn);
		mToolMenuBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				popMenu.show(mToolMenuBtn);
			}
		});

		mToolsButton = (Button) findViewById(R.id.ToolsBtn);
		mToolsButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				openAddBookmarkDialog();
			}
		});
		mMoreButton = (Button) findViewById(R.id.moreBtn);
		mMoreButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mToolsActionGridVisible = true;
				mToolsActionGrid.show(v);
			}
		});
		mProgressBar = (ProgressBar) findViewById(R.id.WebViewProgress);
		// mWebViewProgressTop = (SeekBar)findViewById(R.id.WebViewProgressTop);
		// mProgressBar.setMax(100);

		mPreviousButton = (ImageButton) findViewById(R.id.PreviousBtn);
		mNextButton = (ImageButton) findViewById(R.id.NextBtn);

		mPreviousButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				
				if (mCurrentWebView != null
						&& mCurrentWebView.getUrl() != null
						&& (!mCurrentWebView.getUrl().equals(
								Constants.URL_ABOUT_START) )) {
					

					if(!mCurrentWebView	.canGoBack()&& !mCurrentWebView.getUrl().equals(Constants.URL_ABOUT_URL)){
						removWebView(selectePosition, mCurrentWebView);
						setSelecteLabelPage();
					}else if (mCustomView != null) {
						hideCustomView(false);
					} else if (mFindDialogVisible) {
						closeFindDialog();
					} else {
						navigatePrevious();// 上一页
					}
					
				} else if (mUrlEditText.getText().equals("")
						|| mUrlEditText.getText() == null){
					
					if(!mCurrentWebView	.canGoBack()&&mWebViews.size()>1){
						removWebView(selectePosition, mCurrentWebView);
						setSelecteLabelPage();
					}else{
						navigateToHome();
						updateUI();
					}
				}
				
			}
		});

		mNextButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				navigateNext();// 下一页

			}
		});

		mNewTabButton = (ImageButton) findViewById(R.id.NewTabBtn);
		mNewTabButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				if (mPopWindowAddNewPage != null) {
					if (mPopWindowAddNewPage.isShow()) {
						mPopWindowAddNewPage.dismiss();
						mPopWindowAddNewPage = null;
					}
				}
				mPopWindowAddNewPage = new PopWindowAddNewPage(
						HomeActivity.this, handler, mWebViews,
						mCurrentWebView, mViewFlipper, mNewTabButton);

			}
		});

		returnHomeTabBtn = (ImageButton) findViewById(R.id.returnHomeTabBtn);
		returnHomeTabBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (mUrlEditText != null
						&& mUrlEditText.getText().toString()
								.equals(Constants.URL_ABOUT_START)
						|| (mCurrentWebView != null
								&& mCurrentWebView.getUrl() != null && mCurrentWebView
								.getUrl().equals(Constants.URL_ABOUT_START))) {
					Toast.makeText(HomeActivity.this, "已经是主页", 0).show();
					navigateToHome();
					return;
				} else {
					navigateToHome();
				}
				updateUI();

			}
		});

		mNewTabButtonNumber = (TextView) findViewById(R.id.number);

		mFindPreviousButton = (ImageButton) findViewById(R.id.find_previous);
		mFindPreviousButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mCurrentWebView.findNext(false);
				hideKeyboardFromFindDialog();
			}
		});

		mFindNextButton = (ImageButton) findViewById(R.id.find_next);
		mFindNextButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mCurrentWebView.findNext(true);
				hideKeyboardFromFindDialog();
			}
		});

		mFindCloseButton = (ImageButton) findViewById(R.id.find_close);
		mFindCloseButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				closeFindDialog();
			}
		});

		mFindText = (EditText) findViewById(R.id.find_value);
		mFindText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				doFind();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		setAddNewPageSize();
	}

	/**
	 * 显示进度条
	 * */
	public void showProgressBar() {
		if ((mProgressBar != null && Utils.hasNetwork(HomeActivity.this))) {
			mProgressBar.setVisibility(View.VISIBLE);
		} else {
			mProgressBar.setVisibility(View.GONE);
		}
	}

	/**
	 * 关闭进度条
	 * */
	public void cancelProgressBar() {
		// mWebViewProgressTop.setVisibility(View.GONE);
		mProgressBar.setVisibility(View.GONE);
		boolean isFullScreen = UserPreference.read("fullScreen", false);
		if (isFullScreen) {
			setToolbarsVisibility(false);
		} else {
			setToolbarsVisibility(true);
		}
	}

	/**
	 * 设置PreviousBtn 按钮的背景 canGoBack true 表示还可以继续点击前进
	 * */
	public void setPreviousBackground(boolean canGoBack) {
		if (canGoBack) {
			isNeedLoadHide = true;
			String url = Controller
					.getInstance()
					.getPreferences()
					.getString(Constants.PREFERENCES_GENERAL_HOME_PAGE,
							Constants.URL_ABOUT_START);
			if (mCurrentWebView.getUrl().equals(url)) {
				mPreviousButton.setImageResource(R.drawable.menubar_back_dis);
			} else {
				mPreviousButton.setImageResource(R.drawable.menubar_back_d);
			}

		} else {
			isNeedLoadHide = false;
			mPreviousButton.setImageResource(R.drawable.menubar_back_dis);
		}
	}

	/**
	 * 设置NextBtn 按钮的背景 canNextBtn true 表示还可以继续点击
	 * */
	public void setNextBtnBackground(boolean canNextBtn) {
		if (canNextBtn) {
			isNeedLoadHide = true;
			mNextButton.setImageResource(R.drawable.menubar_forward_d);
		} else {
			mNextButton.setImageResource(R.drawable.menubar_forward_dis);
			isNeedLoadHide = false;
		}
	}

	/**
	 * 全屏切换
	 */
	public void fullScreenChange() {

		boolean isFullScreen = UserPreference.read("fullScreen", false);
		if (isFullScreen) {
			setToolbarsVisibility(false);
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
					WindowManager.LayoutParams.FLAG_FULLSCREEN);
		} else {
			setToolbarsVisibility(true);
			getWindow().setFlags(
					WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN,
					WindowManager.LayoutParams.FLAG_FULLSCREEN);
		}

	}

	public void setAddNewPageSize() {
		if (mWebViews != null) {
			int number = mWebViews.size();
			if (number == 0) {
				number = 1;// 默认设置为1
			}
			mNewTabButtonNumber.setText(number + "");
		}
	}

	public Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {

			case PopMenu.bookmarks:// 打开书签
				openBookmarksHistoryActivity();
				break;
			case PopMenu.FindPage:// 页面查找
				startShowFindDialogRunnable();
				break;
			case PopMenu.ShareUrl:// 页面分享

				if (mCurrentWebView != null
						&& mCurrentWebView.getUrl().equals(
								Constants.URL_ABOUT_START)) {// 是主页
					ApplicationUtils.sharePage(HomeActivity.this,
							"传递正能量的321浏览器火爆登场 ", " http://www.hi321.cn/");

				} else {
					ApplicationUtils.sharePage(HomeActivity.this,
							mCurrentWebView.getTitle(),
							mCurrentWebView.getUrl());
				}
				break;
			case PopMenu.SelectText:// 选择文本
				swithToSelectAndCopyTextMode();
				break;
			case PopMenu.ADD_BOOKMARK: // 添加书签
				openAddBookmarkDialog();
				break;
			case PopMenu.Download:// 下载管理
				openDownloadsList();
				break;
			case PopMenu.Setting:// 设置
				openPreferences();
				break;
			case PopMenu.Fankui:// 反馈
				openFeedback();
				break;
			case PopMenu.exit:// 退出
				HomeActivity.this.finish();
				break;
			case PopMenu.refreshview:// 刷新
				if (mCurrentWebView.isLoading()) {
					mCurrentWebView.stopLoading();
				} else {
					mCurrentWebView.reload();
				}
				break;

			case PopMenu.Update:

				// 如果想程序启动时自动检查是否需要更新， 把下面两行代码加在Activity 的onCreate()函数里。
				com.umeng.common.Log.LOG = true;
				UmengUpdateAgent.setUpdateOnlyWifi(false); // 目前我们默认在Wi-Fi接入情况下才进行自动提醒。如需要在其他网络环境下进行更新自动提醒，则请添加该行代码
				UmengUpdateAgent.setUpdateAutoPopup(false);
				UmengUpdateAgent.setUpdateListener(updateListener);

				UmengUpdateAgent.update(HomeActivity.this);

				break;

			case PopMenu.FullSeting:
				// mUrlBarVisible = false;
				fullScreenChange();
				break;
			case PopMenu.Guanyu:
				Intent intent = new Intent(HomeActivity.this,
						AboutActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.fade, R.anim.hold);
				break;
			case PopMenu.SCREEN_LUMINANCE:
				// 取得当前亮度
				int normal = Settings.System.getInt(getContentResolver(),
						Settings.System.SCREEN_BRIGHTNESS, 255);
				if (normal < 255) {
					// 夜间模式
					// 根据当前进度改变亮度
					Settings.System.putInt(getContentResolver(),
							Settings.System.SCREEN_BRIGHTNESS, 255);
				} else {
					Settings.System.putInt(getContentResolver(),
							Settings.System.SCREEN_BRIGHTNESS, 85);
				}
				setSereenLuminance();

				break;
			case PopWindowAddNewPage.ADDNEWPAGE:
				addTab(true);// 新建标签页
				setAddNewPageSize();
				break;
			case PopWindowAddNewPage.RemoveLabelPage:
				setSelecteLabelPage();
				break;
			case PopWindowAddNewPage.LoadHomelPage:
				navigateToHome();
				updateUI();
				break;
			case PopWindowAddNewPage.SELECTPage:// 选择某一页
				int pos = (Integer) msg.obj;
				selectePosition = pos;
				if (mViewFlipper.getChildCount() > pos) {
					mWebViews = PopWindowAddNewPage.mWebViews;
					mViewFlipper = PopWindowAddNewPage.mViewFlipper;
					mCurrentWebView = mWebViews.get(pos);
					mViewFlipper.getChildAt(pos);
					mViewFlipper.setDisplayedChild(pos);
				}

				updateUI();
				setAddNewPageSize();
				//
				break;
			case PopMenu.SAVEIMAGE:
				View view = HomeActivity.this.getWindow().getDecorView();
				view.setDrawingCacheEnabled(true);
				view.buildDrawingCache();
				Bitmap b1 = view.getDrawingCache();
				final String status = Environment.getExternalStorageState();
				if (status.equals(Environment.MEDIA_MOUNTED)) {
					IOUtils.SaveImage(b1);
					Toast.makeText(HomeActivity.this, "保存成功", 1).show();
				} else {
					Toast.makeText(HomeActivity.this, "没有sdcard", 1).show();
				}
				break;

			default:
				break;
			}

		}

		private void openFeedback() {
			FeedbackAgent agent = new FeedbackAgent(HomeActivity.this);
			agent.startFeedbackActivity();
		}

	};
	
	public void removWebView(final int position,
			final CustomWebView currentWebView) {
		if (mWebViews != null
				&& mWebViews.size() > position) {
			currentWebView.doOnPause();
			currentWebView.destroy();
			mWebViews.remove(position);
			mViewFlipper.removeViewAt(position);
			PopWindowAddNewPage.mWebViews = mWebViews ;
			PopWindowAddNewPage.mViewFlipper = mViewFlipper;
		}
	}

	private void setSelecteLabelPage() {
		List<CustomWebView> mWebViewsRemove = PopWindowAddNewPage.mWebViews;
		ViewFlipper mViewFlip = PopWindowAddNewPage.mViewFlipper;
		if (mWebViewsRemove != null && mWebViewsRemove.size() -1>= 0
				&& mViewFlip != null && mViewFlip.getChildCount()-1 >= 0) {
			selectePosition = mWebViewsRemove.size() -1;
			mWebViews = mWebViewsRemove;
			mViewFlipper = mViewFlip;
			mCurrentWebView = mWebViews.get(selectePosition);
			mViewFlipper.getChildAt(selectePosition);
			mViewFlipper.setDisplayedChild(selectePosition);
		} else {
			navigateToHome();
		}
		updateUI();
		setAddNewPageSize();
	}

	private void registerPreferenceChangeListener() {
		mPreferenceChangeListener = new OnSharedPreferenceChangeListener() {
			@Override
			public void onSharedPreferenceChanged(
					SharedPreferences sharedPreferences, String key) {
				if (key.equals(Constants.PREFERENCE_BOOKMARKS_DATABASE)) {
					updateBookmarksDatabaseSource();
				}
			}
		};

		PreferenceManager.getDefaultSharedPreferences(this)
				.registerOnSharedPreferenceChangeListener(
						mPreferenceChangeListener);
	}

	/**
	 * Apply preferences to the current UI objects.
	 */
	public void applyPreferences() {
		// To update to Bubble position.
		// setToolbarsVisibility(false);

		updateSwitchTabsMethod();

		for (CustomWebView view : mWebViews) {
			view.initializeOptions();
		}
	}

	private void updateSwitchTabsMethod() {
		String method = PreferenceManager.getDefaultSharedPreferences(this)
				.getString(Constants.PREFERENCES_GENERAL_SWITCH_TABS_METHOD,
						"buttons");

		if (method.equals("buttons")) {
			mSwitchTabsMethod = SwitchTabsMethod.BUTTONS;
		} else if (method.equals("fling")) {
			mSwitchTabsMethod = SwitchTabsMethod.FLING;
		} else if (method.equals("both")) {
			mSwitchTabsMethod = SwitchTabsMethod.BOTH;
		} else {
			mSwitchTabsMethod = SwitchTabsMethod.BUTTONS;
		}
	}

	private void updateBookmarksDatabaseSource() {
		String source = PreferenceManager.getDefaultSharedPreferences(this)
				.getString(Constants.PREFERENCE_BOOKMARKS_DATABASE, "STOCK");

		if (source.equals("STOCK")) {
			BookmarksProviderWrapper.setBookmarksSource(BookmarksSource.STOCK);
		} else if (source.equals("INTERNAL")) {
			BookmarksProviderWrapper
					.setBookmarksSource(BookmarksSource.INTERNAL);
		}
	}

	private void setStatusBarVisibility(boolean visible) {
		int flag = visible ? 0 : WindowManager.LayoutParams.FLAG_FULLSCREEN;
		getWindow().setFlags(flag, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}

	/**
	 * Initialize a newly created WebView.
	 */
	public void initializeCurrentWebView() {

		mCurrentWebView.setWebViewClient(new CustomWebViewClient(
				HomeActivity.this));
		mCurrentWebView.setWebChromeClient(new CustomWebChromeClient(
				HomeActivity.this));
		mCurrentWebView.setOnTouchListener(this);

		// 调用js意见反馈
		mCurrentWebView.addJavascriptInterface(new Object() {
			public void clickOnAndroid() {
				handler.post(new Runnable() {
					public void run() {
						FeedbackAgent agent = new FeedbackAgent(
								HomeActivity.this);
						agent.startFeedbackActivity();
					}
				});

			}

		}, "feedbrack");

		// mCurrentWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		mCurrentWebView
				.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {

					@Override
					public void onCreateContextMenu(ContextMenu menu, View v,
							ContextMenuInfo menuInfo) {
						HitTestResult result = ((WebView) v).getHitTestResult();

						int resultType = result.getType();
						if ((resultType == HitTestResult.ANCHOR_TYPE)
								|| (resultType == HitTestResult.IMAGE_ANCHOR_TYPE)
								|| (resultType == HitTestResult.SRC_ANCHOR_TYPE)
								|| (resultType == HitTestResult.SRC_IMAGE_ANCHOR_TYPE)) {

							Intent i = new Intent();
							i.putExtra(Constants.EXTRA_ID_URL,
									result.getExtra());

							MenuItem item = menu.add(0, CONTEXT_MENU_OPEN, 0,
									R.string.Main_MenuOpen);
							item.setIntent(i);

							item = menu.add(0, CONTEXT_MENU_OPEN_IN_NEW_TAB, 0,
									R.string.Main_MenuOpenNewTab);
							item.setIntent(i);

							item = menu.add(0, CONTEXT_MENU_COPY, 0,
									R.string.Main_MenuCopyLinkUrl);
							item.setIntent(i);

							item = menu.add(0, CONTEXT_MENU_DOWNLOAD, 0,
									R.string.Main_MenuDownload);
							item.setIntent(i);

							item = menu.add(0, CONTEXT_MENU_SHARE, 0,
									R.string.Main_MenuShareLinkUrl);
							item.setIntent(i);

							menu.setHeaderTitle(result.getExtra());
						} else if (resultType == HitTestResult.IMAGE_TYPE) {
							Intent i = new Intent();
							i.putExtra(Constants.EXTRA_ID_URL,
									result.getExtra());

							MenuItem item = menu.add(0, CONTEXT_MENU_OPEN, 0,
									R.string.Main_MenuViewImage);
							item.setIntent(i);

							item = menu.add(0, CONTEXT_MENU_COPY, 0,
									R.string.Main_MenuCopyImageUrl);
							item.setIntent(i);

							item = menu.add(0, CONTEXT_MENU_DOWNLOAD, 0,
									R.string.Main_MenuDownloadImage);
							item.setIntent(i);

							item = menu.add(0, CONTEXT_MENU_SHARE, 0,
									R.string.Main_MenuShareImageUrl);
							item.setIntent(i);

							menu.setHeaderTitle(result.getExtra());

						} else if (resultType == HitTestResult.EMAIL_TYPE) {

							Intent sendMail = new Intent(Intent.ACTION_VIEW,
									Uri.parse(WebView.SCHEME_MAILTO
											+ result.getExtra()));

							MenuItem item = menu.add(0, CONTEXT_MENU_SEND_MAIL,
									0, R.string.Main_MenuSendEmail);
							item.setIntent(sendMail);

							Intent i = new Intent();
							i.putExtra(Constants.EXTRA_ID_URL,
									result.getExtra());

							item = menu.add(0, CONTEXT_MENU_COPY, 0,
									R.string.Main_MenuCopyEmailUrl);
							item.setIntent(i);

							item = menu.add(0, CONTEXT_MENU_SHARE, 0,
									R.string.Main_MenuShareEmailUrl);
							item.setIntent(i);

							menu.setHeaderTitle(result.getExtra());
						}
					}

				});

		mCurrentWebView.setDownloadListener(new DownloadListener() {

			@Override
			public void onDownloadStart(String url, String userAgent,
					String contentDisposition, String mimetype,
					long contentLength) {

				LogUtil.e("onDownloadStart", "---url===" + url
						+ "----     userAgent===" + userAgent
						+ "----    contentDisposition===" + contentDisposition
						+ "----   mimetype====" + mimetype
						+ "----   contentLength===" + contentLength);

				doDownloadStart(url, userAgent, contentDisposition, mimetype,
						contentLength);
			}

		});

	}

	/**
	 * Select Text in the webview and automatically sends the selected text to
	 * the clipboard. 选择文本并自动发送webview选中的文本到剪贴板
	 */
	public void swithToSelectAndCopyTextMode() {
		try {
			KeyEvent shiftPressEvent = new KeyEvent(0, 0, KeyEvent.ACTION_DOWN,
					KeyEvent.KEYCODE_SHIFT_LEFT, 0, 0);
			shiftPressEvent.dispatch(mCurrentWebView);
		} catch (Exception e) {
			throw new AssertionError(e);
		}
	}

	private void startPlayer(final String url, final DownloadItem item) {
		Intent intent;
		if (mVideoInfo != null) {
			mVideoInfo.setUrl(url);
		} else {
			mVideoInfo = new cn.hi321.browser.media.entity.MediaItem();
			mVideoInfo.setUrl(url);
		}

		try {
			if (item.getFileName().trim().length() > 0)
				mVideoInfo.setTitle(item.getFileName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (url.contains("m3u8")) {
			mVideoInfo.setLive(true);
		} else {
			mVideoInfo.setLive(false);
		}

		intent = new Intent(HomeActivity.this, SystemPlayer.class);
		Bundle mBundle = new Bundle();
		mBundle.putSerializable("VideoInfo", mVideoInfo);
		intent.putExtras(mBundle);
		LogUtil.i(TAG, "startActivity");

		HomeActivity.this.startActivity(intent);
	}

	cn.hi321.browser.media.entity.MediaItem mVideoInfo = null;

	/**
	 * Initiate a download. Check the SD card and start the download runnable.
	 * 
	 * @param url
	 *            The url to download.
	 * @param userAgent
	 *            The user agent.
	 * @param contentDisposition
	 *            The content disposition.
	 * @param mimetype
	 *            The mime type.
	 * @param contentLength
	 *            The content length.
	 */
	private void doDownloadStart(final String url, final String userAgent,
			final String contentDisposition, final String mimetype,
			final long contentLength) {

		if (ApplicationUtils.checkCardState(this, true)) {

			final DownloadItem item = new DownloadItem(this, url);
			// Controller.getInstance().addToDownload(item);
			// item.startDownload();
			if (url != null && url.toLowerCase().contains(".m3u8")
					|| mimetype != null
					&& mimetype.toLowerCase().contains("video")
					|| mimetype != null
					&& mimetype.toLowerCase().contains("audio")) {

				if (url != null && url.toLowerCase().contains(".m3u8")) {
					startPlayer(url, item);
					return;
				}

				String[] items = new String[] { "播放", "下载" };
				new AlertDialog.Builder(HomeActivity.this)
						.setTitle(R.string.app_name)
						.setItems(items, new AlertDialog.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								if (which == 0) {
									startPlayer(url, item);
								} else if (which == 1) {
									MediaItem mediaItem = new MediaItem();
									mediaItem.setId(item.getUrl());
									mediaItem.setUrl(item.getUrl());
									mediaItem.setName(item.getFileName());
									mediaItem.setMimetype(mimetype);
									mediaItem.setContentLength(contentLength);
									mediaItem
											.setContentDisposition(contentDisposition);
									mediaItem.setUserAgent(userAgent);
									mediaItem.setSrcPath(DownloadHelper
											.getDownloadPath()
											+ "/"
											+ item.getFileName());
									BrowserApp
											.getInstance()
											.getDownloadManager()
											.download(mediaItem,
													HomeActivity.this);

									Toast.makeText(
											HomeActivity.this,
											getString(R.string.Main_DownloadStartedMsg),
											Toast.LENGTH_SHORT).show();
								} else {
									startPlayer(url, item);
								}

							}

						})
						.setNegativeButton(R.string.user_cancel,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {

									}
								}).create().show();

				return;
			} else {
				new AlertDialog.Builder(HomeActivity.this)
						.setTitle(R.string.app_name)
						.setMessage(
								HomeActivity.this
										.getString(R.string.add_download_tip)
										+ item.getFileName())
						.setPositiveButton(android.R.string.ok,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										MediaItem mediaItem = new MediaItem();
										mediaItem.setId(item.getUrl());
										mediaItem.setUrl(item.getUrl());
										mediaItem.setName(item.getFileName());
										mediaItem.setMimetype(mimetype);
										mediaItem
												.setContentLength(contentLength);
										mediaItem
												.setContentDisposition(contentDisposition);
										mediaItem.setUserAgent(userAgent);
										mediaItem.setSrcPath(DownloadHelper
												.getDownloadPath()
												+ "/"
												+ item.getFileName());
										BrowserApp
												.getInstance()
												.getDownloadManager()
												.download(mediaItem,
														HomeActivity.this);
										Toast.makeText(
												HomeActivity.this,
												getString(R.string.Main_DownloadStartedMsg),
												Toast.LENGTH_SHORT).show();
									}
								})

						.setNegativeButton(android.R.string.cancel,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										// result.cancel();
									}
								}).create().show();
				return;
			}
			//
		}
	}

	/**
	 * Add a new tab.
	 * 
	 * @param navigateToHome
	 *            If True, will load the user home page.
	 */
	public void addTab(boolean navigateToHome) {
		addTab(navigateToHome, -1);
	}

	/**
	 * Add a new tab.
	 * 
	 * @param navigateToHome
	 *            If True, will load the user home page.
	 * @param parentIndex
	 *            The index of the new tab.
	 */
	public void addTab(boolean navigateToHome, int parentIndex) {
		if (mFindDialogVisible) {
			closeFindDialog();
		}

		RelativeLayout view = (RelativeLayout) mInflater.inflate(
				R.layout.webview, mViewFlipper, false);
		mFullscreenContainer = (FrameLayout) view
				.findViewById(R.id.fullscreen_custom_content);
		mContentView = (FrameLayout) view.findViewById(R.id.main_content);
		mCurrentWebView = (CustomWebView) view.findViewById(R.id.webview);
		mFullscreenContainer.setFocusable(true);

		initializeCurrentWebView();

		synchronized (mViewFlipper) {
			if (parentIndex != -1) {
				mWebViews.add(parentIndex + 1, mCurrentWebView);
				mViewFlipper.addView(view, parentIndex + 1);
			} else {
				mWebViews.add(mCurrentWebView);
				mViewFlipper.addView(view);
			}
			selectePosition = mViewFlipper.indexOfChild(view);
			mViewFlipper.setDisplayedChild(selectePosition);
		}

		if (navigateToHome) {
			navigateToHome();
		}
		updateUI();
		// updatePreviousNextTabViewsVisibility();
		mUrlEditText.clearFocus();

	}

	@Override
	protected void onPause() {
		mCurrentWebView.doOnPause();
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
		System.exit(0);
	}

	private void doFind() {
		CharSequence find = mFindText.getText();
		if (find.length() == 0) {
			mFindPreviousButton.setEnabled(false);
			mFindNextButton.setEnabled(false);
			mCurrentWebView.clearMatches();
		} else {
			int found = mCurrentWebView.findAll(find.toString());
			if (found < 2) {
				mFindPreviousButton.setEnabled(false);
				mFindNextButton.setEnabled(false);
			} else {
				mFindPreviousButton.setEnabled(true);
				mFindNextButton.setEnabled(true);
			}
		}
	}

	private void showFindDialog() {
		setFindBarVisibility(true);
		mCurrentWebView.doSetFindIsUp(true);
		CharSequence text = mFindText.getText();
		if (text.length() > 0) {
			mFindText.setSelection(0, text.length());
			doFind();
		} else {
			mFindPreviousButton.setEnabled(false);
			mFindNextButton.setEnabled(false);
		}

		mFindText.requestFocus();
		showKeyboardForFindDialog();
	}

	private void closeFindDialog() {
		hideKeyboardFromFindDialog();
		mCurrentWebView.doNotifyFindDialogDismissed();
		setFindBarVisibility(false);
	}

	private void setFindBarVisibility(boolean visible) {
		if (visible) {
			mFindBar.startAnimation(AnimationManager.getInstance()
					.getTopBarShowAnimation());
			mFindBar.setVisibility(View.VISIBLE);
			mFindDialogVisible = true;
		} else {
			mFindBar.startAnimation(AnimationManager.getInstance()
					.getTopBarHideAnimation());
			mFindBar.setVisibility(View.GONE);
			mFindDialogVisible = false;
		}
	}

	/**
	 * Change the tool bars visibility.
	 * 
	 * @param visible
	 *            If True, the tool bars will be shown.
	 */
	private void setToolbarsVisibility(boolean visible) {// visible 表示非全屏

		if (visible) {// 非全屏
			if (!mUrlBarVisible) {

				mTopBar.setVisibility(View.VISIBLE);
				mBottomBar.setVisibility(View.VISIBLE);
				mBubbleRightView.setVisibility(View.GONE);
				mBubbleLeftView.setVisibility(View.GONE);
			}
			mUrlBarVisible = true;

		} else {// 全屏
			// modify by yanggf

			String title = mUrlEditText.getText().toString();
			System.out.println("title===" + title);
			if ((mCurrentWebView != null && mCurrentWebView.getUrl() != null && mCurrentWebView
					.getUrl().equals(Constants.URL_ABOUT_START))) {
				mTopBar.setVisibility(View.VISIBLE);
				mBottomBar.setVisibility(View.VISIBLE);
				mBubbleRightView.setVisibility(View.GONE);
				mBubbleLeftView.setVisibility(View.GONE);

			} else {
				if (mUrlBarVisible) {
					mTopBar.setVisibility(View.GONE);
					mBottomBar.setVisibility(View.GONE);
					String bubblePosition = Controller
							.getInstance()
							.getPreferences()
							.getString(
									Constants.PREFERENCES_GENERAL_BUBBLE_POSITION,
									"right");

					if (bubblePosition.equals("right")) {
						mBubbleRightView.setVisibility(View.VISIBLE);
						mBubbleLeftView.setVisibility(View.GONE);
					} else if (bubblePosition.equals("left")) {
						mBubbleRightView.setVisibility(View.GONE);
						mBubbleLeftView.setVisibility(View.VISIBLE);
					} else if (bubblePosition.equals("both")) {
						mBubbleRightView.setVisibility(View.VISIBLE);
						mBubbleLeftView.setVisibility(View.VISIBLE);
					} else {
						mBubbleRightView.setVisibility(View.VISIBLE);
						mBubbleLeftView.setVisibility(View.GONE);
					}
					mUrlBarVisible = false;
				}

			}

		}
	}

	private void showKeyboardForFindDialog() {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(mFindText, InputMethodManager.SHOW_IMPLICIT);
	}

	private void hideKeyboardFromFindDialog() {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(mFindText.getWindowToken(), 0);
	}

	/**
	 * Hide the keyboard.
	 * 
	 * @param delayedHideToolbars
	 *            If True, will start a runnable to delay tool bars hiding. If
	 *            False, tool bars are hidden immediatly.
	 */
	private void hideKeyboard(boolean delayedHideToolbars) {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(mUrlEditText.getWindowToken(), 0);

		if (mUrlBarVisible) {
			if (delayedHideToolbars) {
			} else {

				boolean isFullScreen = UserPreference.read("fullScreen", false);
				if (isFullScreen) {
					setToolbarsVisibility(false);
				}

			}
		}
	}

	/**
	 * Thread to delay the show of the find dialog. This seems to be necessary
	 * when shown from a QuickAction. If not, the keyboard does not show. 50ms
	 * seems to be enough on a Nexus One and on the (rather) slow emulator.
	 * Dirty hack :(
	 */
	private void startShowFindDialogRunnable() {
		new Thread(new Runnable() {

			private Handler mHandler = new Handler() {
				public void handleMessage(Message msg) {
					showFindDialog();
				}
			};

			@Override
			public void run() {
				try {
					Thread.sleep(50);
					mHandler.sendEmptyMessage(0);
				} catch (InterruptedException e) {
					mHandler.sendEmptyMessage(0);
				}

			}
		}).start();
	}

	/**
	 * Hide the tool bars.
	 */
	public void hideToolbars() {
		if (mUrlBarVisible) {
			if ((!mUrlEditText.hasFocus()) && (!mToolsActionGridVisible)) {

				// if (!mCurrentWebView.isLoading()) {
				//
				// setToolbarsVisibility(false);
				// }
			}
		}
		// mHideToolbarsRunnable = null;
	}

	/**
	 * Start a runnable to update history.
	 * 
	 * @param title
	 *            The page title.
	 * @param url
	 *            The page url.
	 */
	public void startHistoryUpdaterRunnable(String title, String url,
			String originalUrl) {
		if ((url != null) && (url.length() > 0)) {
			new Thread(new HistoryUpdater(this, title, url, originalUrl))
					.start();
		}
	}

	/**
	 * Navigate to the given url.
	 * 
	 * @param url
	 *            The url.
	 */
	private void navigateToUrl(String url) {
		// Needed to hide toolbars properly.
		mUrlEditText.clearFocus();
		mCurrentWebView.doOnResume();
		if ((url != null) && (url.length() > 0)) {

			if (UrlUtils.isUrl(url)) {
				url = UrlUtils.checkUrl(url);
			} else {
				url = UrlUtils.getSearchUrl(this, url);
			}

			if (Controller.getInstance().getPreferences()
					.getBoolean(Constants.PREFERENCES_SHOW_FULL_SCREEN, false)
					&& Controller
							.getInstance()
							.getPreferences()
							.getBoolean(
									Constants.PREFERENCES_GENERAL_HIDE_TITLE_BARS,
									true)) {
				hideKeyboard(true);
			}

			if (url.equals(Constants.URL_ABOUT_START)) {

//				mCurrentWebView.loadDataWithBaseURL(
//						"file:///android_asset/startpage/",
//						ApplicationUtils.getStartPage(this), "text/html",
//						"UTF-8", Constants.URL_ABOUT_START);
				
				mCurrentWebView.loadUrl("file:///android_asset/index/index.html");

			} else {

				// If the url is not from GWT mobile view, and is in the mobile
				// view url list, then load it with GWT.
				if ((!url
						.startsWith(Constants.URL_GOOGLE_MOBILE_VIEW_NO_FORMAT))
						&& (UrlUtils.checkInMobileViewUrlList(this, url))) {

					url = String.format(Constants.URL_GOOGLE_MOBILE_VIEW, url);
				}
				mCurrentWebView.loadUrl(url);
			}

		}
	}

	/**
	 * Navigate to the url given in the url edit text.
	 */
	// private void navigateToUrl() {
	// navigateToUrl(mUrlEditText.getText().toString());
	// }

	/**
	 * Navigate to the user home page.
	 */
	private void navigateToHome() {
		isNeedLoadHide = true;
		navigateToUrl(Controller
				.getInstance()
				.getPreferences()
				.getString(Constants.PREFERENCES_GENERAL_HOME_PAGE,
						Constants.URL_ABOUT_START));
	}

	/**
	 * 导航到前一页历史.
	 */
	private void navigatePrevious() {
		// Needed to hide toolbars properly.
		mUrlEditText.clearFocus();
		if (Controller.getInstance().getPreferences()
				.getBoolean(Constants.PREFERENCES_SHOW_FULL_SCREEN, false)
				&& Controller
						.getInstance()
						.getPreferences()
						.getBoolean(
								Constants.PREFERENCES_GENERAL_HIDE_TITLE_BARS,
								true)) {
			hideKeyboard(true);
		}
		if (mCurrentWebView.canGoBack()) {
			String url = Controller
					.getInstance()
					.getPreferences()
					.getString(Constants.PREFERENCES_GENERAL_HOME_PAGE,
							Constants.URL_ABOUT_START);
			String currentUrl = mCurrentWebView.getUrl();
			if (!currentUrl.equals(url)) {
				isNeedLoadHide = true;
				mCurrentWebView.goBack();
			} /*
			 * else { mCurrentWebView.onPause(); }
			 */

			mCurrentWebView.doOnResume();

		}
		updateUI();
	}

	/**
	 * Navigate to the next page in history.
	 */
	private void navigateNext() {
		// Needed to hide toolbars properly.
		mUrlEditText.clearFocus();
		if (Controller.getInstance().getPreferences()
				.getBoolean(Constants.PREFERENCES_SHOW_FULL_SCREEN, false)
				&& Controller
						.getInstance()
						.getPreferences()
						.getBoolean(
								Constants.PREFERENCES_GENERAL_HIDE_TITLE_BARS,
								true)) {
			hideKeyboard(true);
		}

		mCurrentWebView.goForward();
		mCurrentWebView.doOnResume();
		setNextBtnBackground(mCurrentWebView.canGoForward());
	}

	@Override
	public boolean onKeyLongPress(int keyCode, KeyEvent event) {

		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			this.moveTaskToBack(true);
			return true;
		default:
			return super.onKeyLongPress(keyCode, event);
		}
	}
	
	// private boolean isBack = false;
		private long mExitTime;

		@Override
		public boolean onKeyDown(int keyCode, KeyEvent event) {
			// TODO Auto-generated method stub
			if (keyCode == KeyEvent.KEYCODE_BACK) {

				if (mUrlEditText != null
						&& mUrlEditText.getText().toString()
								.equals(Constants.URL_ABOUT_START)
						|| (mCurrentWebView != null
								&& mCurrentWebView.getUrl() != null && mCurrentWebView
								.getUrl().equals(Constants.URL_ABOUT_START))
						|| (mCurrentWebView != null
						&& mCurrentWebView.getUrl() != null && mCurrentWebView
						.getUrl().equals(Constants.URL_ABOUT_URL))
						) {
					
					
					if ((System.currentTimeMillis() - mExitTime) > 2000) {

						Toast.makeText(
								this,
								getResources().getString(R.string.press_again_exit),
								Toast.LENGTH_SHORT).show();
						mExitTime = System.currentTimeMillis();
					}

					else {
						//退出浏览器
						//SplashActivity.removeView();
						HomeActivity.this.finish();
					}
					
					
				} else if (mCurrentWebView != null
						&& mCurrentWebView.getUrl() != null
						&& (!mCurrentWebView.getUrl().equals(
								Constants.URL_ABOUT_START) 
								)) {

					//modfiy by yanggf
//					navigateToHome();
//					updateUI();
					

					if(!mCurrentWebView	.canGoBack()&& !mCurrentWebView.getUrl().equals(Constants.URL_ABOUT_URL)){
						removWebView(selectePosition, mCurrentWebView);
						setSelecteLabelPage();
					}else if (mCustomView != null) {
						hideCustomView(false);
					} else if (mFindDialogVisible) {
						closeFindDialog();
					} else {
						if (mCurrentWebView.canGoBack()) {
							isNeedLoadHide = true;
							mCurrentWebView.goBack();
							mCurrentWebView.doOnResume();
						} else {
							this.moveTaskToBack(true);
						}
					}
					
					return true;
				} else if (mUrlEditText.getText().equals("")
						|| mUrlEditText.getText() == null) {
					
					if(!mCurrentWebView	.canGoBack()&&mWebViews.size()>1){
						removWebView(selectePosition, mCurrentWebView);
						setSelecteLabelPage();
					}else{
						navigateToHome();
						updateUI();
					}
					return true;

				}
				
				

				return true;
			}

			else {

				String volumeKeysBehaviour = PreferenceManager
						.getDefaultSharedPreferences(this).getString(
								Constants.PREFERENCES_UI_VOLUME_KEYS_BEHAVIOUR,
								"DEFAULT");

				if (!volumeKeysBehaviour.equals("DEFAULT")) {
					switch (keyCode) {
					case KeyEvent.KEYCODE_VOLUME_DOWN:

						if (volumeKeysBehaviour.equals("SWITCH_TABS")) {
							showPreviousTab(false);
						} else if (volumeKeysBehaviour.equals("SCROLL")) {
							mCurrentWebView.pageDown(false);
						} else if (volumeKeysBehaviour.equals("HISTORY")) {
							mCurrentWebView.goForward();
							mCurrentWebView.doOnResume();

						} else {
							mCurrentWebView.zoomIn();
						}

						return true;
					case KeyEvent.KEYCODE_VOLUME_UP:

						if (volumeKeysBehaviour.equals("SWITCH_TABS")) {
							showNextTab(false);
						} else if (volumeKeysBehaviour.equals("SCROLL")) {
							mCurrentWebView.pageUp(false);
						} else if (volumeKeysBehaviour.equals("HISTORY")) {
							mCurrentWebView.doOnResume();
							mCurrentWebView.goBack();
						} else {
							mCurrentWebView.zoomOut();
						}

						return true;
					default:
						return super.onKeyDown(keyCode, event);
					}
				}
			}
			return super.onKeyDown(keyCode, event);
		}


	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		switch (keyCode) {
		/*case KeyEvent.KEYCODE_BACK:
			if (mCurrentWebView == null)
				return true;
			if (mCurrentWebView != null
					&& mCurrentWebView.getUrl() != null
					&& !mCurrentWebView.getUrl().equals(
							Constants.URL_ABOUT_START)) {

				if (mCustomView != null) {
					hideCustomView(false);
				} else if (mFindDialogVisible) {
					closeFindDialog();
				} else {
					if (mCurrentWebView.canGoBack()) {
						isNeedLoadHide = true;
						mCurrentWebView.goBack();
						mCurrentWebView.doOnResume();
					} else {
						this.moveTaskToBack(true);
					}
				}
			} else if ((mCurrentWebView.getUrl() != null
					&& !mCurrentWebView.getUrl().equals(
							Constants.URL_ABOUT_START) && !mCurrentWebView
						.canGoBack())) {
				
				removWebView(selectePosition, mCurrentWebView);
				setSelecteLabelPage();
				
//				navigateToHome();
//				updateUI();
//				setAddNewPageSize();

			}
			return true;*/
		case KeyEvent.KEYCODE_SEARCH:
			if (!mFindDialogVisible) {
				showFindDialog();
			}
			return true;

		case KeyEvent.KEYCODE_VOLUME_DOWN:
		case KeyEvent.KEYCODE_VOLUME_UP:
			String volumeKeysBehaviour = PreferenceManager
					.getDefaultSharedPreferences(this).getString(
							Constants.PREFERENCES_UI_VOLUME_KEYS_BEHAVIOUR,
							"DEFAULT");

			if (volumeKeysBehaviour.equals("DEFAULT")) {
				return super.onKeyUp(keyCode, event);
			} else {
				return true;
			}
		default:
			return super.onKeyUp(keyCode, event);
		}
	}

	/**
	 * Set the application title to default.
	 */
	private void clearTitle() {
		this.setTitle(getResources().getString(R.string.ApplicationName));
	}

	/**
	 * Update the application title.
	 */
	private void updateTitle() {
		String value = mCurrentWebView.getTitle();

		if ((value != null) && (value.length() > 0)) {
			this.setTitle(String.format(
					getResources().getString(R.string.ApplicationNameUrl),
					value));
		} else {
			clearTitle();
		}
	}

	/**
	 * Get a Drawable of the current favicon, with its size normalized relative
	 * to current screen density.
	 * 
	 * @return The normalized favicon.
	 */
	private BitmapDrawable getNormalizedFavicon() {

		BitmapDrawable favIcon = new BitmapDrawable(getResources(),
				mCurrentWebView.getFavicon());

		if (mCurrentWebView.getFavicon() != null) {
			int imageButtonSize = ApplicationUtils.getImageButtonSize(this);
			int favIconSize = ApplicationUtils.getFaviconSize(this);

			Bitmap bm = Bitmap.createBitmap(imageButtonSize, imageButtonSize,
					Bitmap.Config.ARGB_4444);
			Canvas canvas = new Canvas(bm);

			favIcon.setBounds((imageButtonSize / 2) - (favIconSize / 2),
					(imageButtonSize / 2) - (favIconSize / 2),
					(imageButtonSize / 2) + (favIconSize / 2),
					(imageButtonSize / 2) + (favIconSize / 2));
			favIcon.draw(canvas);

			favIcon = new BitmapDrawable(getResources(), bm);
		}

		return favIcon;
	}

	/**
	 * Update the "Go" button image.
	 */
	boolean isDelet = false;

	private void updateGoButton() {
		System.out.println("mCurrentWebView.isLoading()----"
				+ mCurrentWebView.isLoading());
		if (mCurrentWebView.isLoading()) {
			delectView.setImageResource(R.drawable.searchdelete);
			isDelet = true;
		} else {
			// if
			// (!mCurrentWebView.isSameUrl(mUrlEditText.getText().toString())) {
			// // delectView.setBackgroundResource(R.drawable.ic_btn_go);
			// } else {
			// System.out.println("11111111111111111");
			delectView.setImageResource(R.drawable.refleshbutton);
			isDelet = false;
			// }

		}
	}

	//
	/**
	 * Update the fav icon display.
	 */
	public void updateFavIcon() {
		BitmapDrawable favicon = getNormalizedFavicon();

		if (mCurrentWebView.getFavicon() != null) {

			mUrlEditText.setCompoundDrawablesWithIntrinsicBounds(favicon, null,
					null, null);
			// mToolsButton.setImageDrawable(favicon);
			// delectView.setImageDrawable(favicon);

		} else {
			mUrlEditText.setCompoundDrawablesWithIntrinsicBounds(null, null,
					null, null);
			// delectView.setImageDrawable(null);
			// mToolsButton.setImageResource(R.drawable.fav_icn_default_toolbar);
		}
	}

	/**
	 * Update the UI: Url edit text, previous/next button state,...
	 */
	public void updateUI() {
		mCurrentWebView.doOnResume();
		// mUrlEditText.removeTextChangedListener(mUrlTextWatcher);
		if (mCurrentWebView.getTitle() == null) {
			mUrlEditText.setText(mCurrentWebView.getUrl());
		} else {
			mUrlEditText.setText(mCurrentWebView.getTitle());
		}

		mUrlEditText.setTag(mCurrentWebView.getUrl());
		// mUrlEditText.addTextChangedListener(mUrlTextWatcher);

		
		mNextButton.setEnabled(mCurrentWebView.canGoForward());

		mProgressBar.setProgress(mCurrentWebView.getProgress());
		
		//非主页的时候
		if (mCurrentWebView.getUrl() != null
				&& (!mCurrentWebView.getUrl().equals(
						Constants.URL_ABOUT_URL) )){
			setPreviousBackground(true);
			mPreviousButton.setEnabled(true);
		//主页的时候
		}/*else if(mCurrentWebView.getUrl() != null
				&& (mCurrentWebView.getUrl().equals(
						Constants.URL_ABOUT_URL))){
			
			mPreviousButton.setEnabled(false);
			setPreviousBackground(false);
		}*/
		else{
			mPreviousButton.setEnabled(mCurrentWebView.canGoBack());
			setPreviousBackground(mCurrentWebView.canGoBack());
		}
		
		setNextBtnBackground(mCurrentWebView.canGoForward());
		
		updateGoButton();

		updateTitle();

		updateFavIcon();

	}

	private boolean isSwitchTabsByFlingEnabled() {
		return (mSwitchTabsMethod == SwitchTabsMethod.FLING)
				|| (mSwitchTabsMethod == SwitchTabsMethod.BOTH);
	}

	//
	// private boolean isSwitchTabsByButtonsEnabled() {
	// return (mSwitchTabsMethod == SwitchTabsMethod.BUTTONS) ||
	// (mSwitchTabsMethod == SwitchTabsMethod.BOTH);
	// }

	/**
	 * Open the "Add bookmark" dialog.
	 */
	private void openAddBookmarkDialog() {
		if (HomeActivity.INSTANCE.mCurrentWebView != null
				&& !HomeActivity.INSTANCE.mCurrentWebView.getUrl().equals(
						Constants.URL_ABOUT_START)) {// 是主页
			Intent i = new Intent(this, EditBookmarkActivity.class);

			i.putExtra(Constants.EXTRA_ID_BOOKMARK_ID, (long) -1);
			i.putExtra(Constants.EXTRA_ID_BOOKMARK_TITLE,
					mCurrentWebView.getTitle());
			i.putExtra(Constants.EXTRA_ID_BOOKMARK_URL,
					mCurrentWebView.getUrl());

			startActivity(i);

		}

	}

	/**
	 * Open the bookmark list.
	 */
	private void openBookmarksHistoryActivity() {
		Intent i = new Intent(this, BookmarksHistoryActivity.class);
		startActivityForResult(i, OPEN_BOOKMARKS_HISTORY_ACTIVITY);
	}

	/**
	 * Open the download list.
	 */
	private void openDownloadsList() {
		// Intent i = new Intent(this, DownloadsListActivity.class);
		// startActivityForResult(i, OPEN_DOWNLOADS_ACTIVITY);
		Intent i = new Intent(this, DownloadActivity.class);
		startActivityForResult(i, OPEN_DOWNLOADS_ACTIVITY);
		// startActivityForResult(new Intent(this, CaptureActivity.class), 0);
	}

	/**
	 * Open preferences.
	 */
	private void openPreferences() {
		Intent preferencesActivity = new Intent(this, PreferencesActivity.class);
		startActivity(preferencesActivity);
	}

	public static boolean isLANDSCAPE = false;// 是横屏吗

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
			// 当前屏幕为横屏
			isLANDSCAPE = true;

		} else {
			// "当前屏幕为竖屏"
			isLANDSCAPE = false;
		}
		System.out.println("popMenu.isShow()===" + popMenu.isShow());

		// popMenu = new PopMenu(MainActivity.this, myHandler);
		if (popMenu != null && popMenu.isShow()) {
			popMenu.dismiss();
			popMenu = new PopMenu(HomeActivity.this, handler);
			popMenu.show(mToolMenuBtn);
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		System.out.println("onActivityResult---------");
		if (requestCode == OPEN_BOOKMARKS_HISTORY_ACTIVITY) {
			if (intent != null) {
				Bundle b = intent.getExtras();
				if (b != null) {
					if (b.getBoolean(Constants.EXTRA_ID_NEW_TAB)) {
						addTab(false);
					}
					navigateToUrl(b.getString(Constants.EXTRA_ID_URL));
				}
			}
		} else if (requestCode == OPEN_FILE_CHOOSER_ACTIVITY) {
			if (mUploadMessage == null) {
				return;
			}

			Uri result = intent == null || resultCode != RESULT_OK ? null
					: intent.getData();
			mUploadMessage.onReceiveValue(result);
			mUploadMessage = null;
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
				UmengUpdateAgent
						.showUpdateDialog(HomeActivity.this, updateInfo);
				break;
			case 1: // has no update
				Toast.makeText(HomeActivity.this, "没有更新", Toast.LENGTH_SHORT)
						.show();
				break;
			case 2: // none wifi
				Toast.makeText(HomeActivity.this, "没有wifi连接， 只在wifi下更新",
						Toast.LENGTH_SHORT).show();
				break;
			case 3: // time out
				Toast.makeText(HomeActivity.this, "请求超时", Toast.LENGTH_SHORT)
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

	// 接收编辑消息页面保存之后的消息
	class RefreshDataBroadcast extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent != null && intent.getAction().equals("search")) {
				String url = intent.getStringExtra("url");

				if (mCurrentWebView.isLoading()) {
					mCurrentWebView.stopLoading();
				} else if (!mCurrentWebView.isSameUrl(url)) {
					navigateToUrl(url);
				} else {
					mCurrentWebView.reload();
				}

			}
		}
	}

}
