
package cn.hi321.browser.preferences;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.CookieManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ToggleButton;
import cn.hi321.browser.config.Constants;
import cn.hi321.browser.controllers.Controller;
import cn.hi321.browser.providers.BookmarksProviderWrapper;
import cn.hi321.browser.ui.activities.AboutActivity;
import cn.hi321.browser.ui.activities.HomeActivity;
import cn.hi321.browser.ui.components.CustomWebView;
import cn.hi321.browser.ui.runnables.XmlHistoryBookmarksExporter;
import cn.hi321.browser.ui.runnables.XmlHistoryBookmarksImporter;
import cn.hi321.browser.utils.ApplicationUtils;
import cn.hi321.browser.utils.DateUtils;
import cn.hi321.browser.utils.UserPreference;
import cn.hi321.browser2.R;

import com.umeng.fb.FeedbackAgent;



/**
 * Preferences activity.
 */
public class PreferencesActivity extends Activity implements OnClickListener {
	
	private RelativeLayout mabout;
	private RelativeLayout set_homepage_layout;
	private RelativeLayout set_qingc_layout;
	private RelativeLayout set_ysbh_layout;
	private RelativeLayout set_feedbrack_layout;
	private RelativeLayout set_gglj_layout;
	private RelativeLayout set_txjz_layout;
	private RelativeLayout set_dkck_layoutyout;
	private ToggleButton set_aqbh_checkbox;
	private ToggleButton set_gglj_checkbox;
	private ToggleButton set_txjz_checkbox;
	private ToggleButton set_dkck_checkbox;
	private RelativeLayout set_aqbh_layout;
	private boolean aqbh=true;
	private SharedPreferences msafePreference;
	private Editor mmsafePreferenceEditor;
	private SharedPreferences bgGgaoSharePreference;
	private Editor bgGgaoSharePreferenceEditor;
	private SharedPreferences bgTXSharePreference;
	private Editor bgTXSharePreferenceEditor;
	private SharedPreferences bgCKSharePreference;
	private Editor bgCKSharePreferenceEditor;
	public static final String SAFE_SHAREPREFERENCE="safe";
	public static final String BG_GGAO_CONFIG="gaog";
	public static final String BG_TX_CONFIG="tx";
	public static final String BG_CK_CONFIG="ck";
	boolean isImage ;
	private ImageView setting_gohome;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.setting_activity);
		initComponent() ;
		
		 isImage =UserPreference.read(Constants.PREFERENCES_BROWSER_ENABLE_IMAGES, true);
		if (isImage) {
			set_txjz_checkbox.setChecked(true);
		}
		else {
			set_txjz_checkbox.setChecked(false);
		}
	
	}
	
	private void initComponent() {
		mabout = (RelativeLayout) findViewById(R.id.set_about_layout);
		set_homepage_layout = (RelativeLayout) findViewById(R.id.set_homepage_layout);
		set_qingc_layout = (RelativeLayout) findViewById(R.id.set_qingc_layout);
		set_ysbh_layout = (RelativeLayout) findViewById(R.id.set_ysbh_layout);
		set_feedbrack_layout = (RelativeLayout) findViewById(R.id.set_feedbrack_layout);
		set_aqbh_layout = (RelativeLayout) findViewById(R.id.set_aqbh_layout);
		set_gglj_layout = (RelativeLayout) findViewById(R.id.set_gglj_layout);
		set_txjz_layout = (RelativeLayout) findViewById(R.id.set_txjz_layout);
		set_dkck_layoutyout = (RelativeLayout) findViewById(R.id.set_dkck_layout);
		setting_gohome=(ImageView)findViewById(R.id.setting_gohome);
		
		set_aqbh_checkbox = (ToggleButton) findViewById(R.id.set_aqbh_checkbox);
		set_gglj_checkbox = (ToggleButton) findViewById(R.id.set_gglj_checkbox);
		set_txjz_checkbox = (ToggleButton) findViewById(R.id.set_txjz_checkbox);
		set_dkck_checkbox = (ToggleButton) findViewById(R.id.set_dkck_checkbox);

		setting_gohome.setOnClickListener(this);
		mabout.setOnClickListener(this);
		set_homepage_layout.setOnClickListener(this);
		set_qingc_layout.setOnClickListener(this);
		set_ysbh_layout.setOnClickListener(this);
		set_feedbrack_layout.setOnClickListener(this);
		set_aqbh_layout.setOnClickListener(this);
		set_gglj_layout.setOnClickListener(this);
		set_txjz_layout.setOnClickListener(this);
		set_dkck_layoutyout.setOnClickListener(this);
		
		msafePreference = getSharedPreferences(SAFE_SHAREPREFERENCE, Context.MODE_PRIVATE);
		mmsafePreferenceEditor = msafePreference.edit();
		
		bgGgaoSharePreference = getSharedPreferences(BG_GGAO_CONFIG, Context.MODE_PRIVATE);
		bgGgaoSharePreferenceEditor = bgGgaoSharePreference.edit();
		
		bgTXSharePreference = getSharedPreferences(BG_TX_CONFIG, Context.MODE_PRIVATE);
		bgTXSharePreferenceEditor = bgGgaoSharePreference.edit();
		
		bgCKSharePreference = getSharedPreferences(BG_CK_CONFIG, Context.MODE_PRIVATE);
		bgCKSharePreferenceEditor = bgCKSharePreference.edit();
		
		set_aqbh_checkbox.setChecked(msafePreference.getBoolean(SAFE_SHAREPREFERENCE, true));		
		set_gglj_checkbox.setChecked(bgGgaoSharePreference.getBoolean(BG_GGAO_CONFIG, true));
		set_txjz_checkbox.setChecked(bgTXSharePreference.getBoolean(BG_TX_CONFIG, true));
		set_dkck_checkbox.setChecked(bgCKSharePreference.getBoolean(BG_CK_CONFIG, true));
	

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = null;
		switch (v.getId()) {
		// 点击关于时的处理
		case R.id.set_about_layout:
			intent = new Intent();
			intent.setClass(PreferencesActivity.this, AboutActivity.class);
			PreferencesActivity.this.startActivity(intent);
			overridePendingTransition(R.anim.fade, R.anim.hold);
			break;
		case R.id.set_homepage_layout:
			openHomepageActivity();
			break;
		case R.id.setting_gohome:
			finish();
			overridePendingTransition(R.anim.hold,R.anim.push_bottom_out);
			break;
		case R.id.set_qingc_layout:
			clearHistory();
			break;
		case R.id.set_ysbh_layout:
			
			Toast.makeText(PreferencesActivity.this, "预留", 0).show();
			break;
			
		case R.id.set_feedbrack_layout:
			FeedbackAgent agent = new FeedbackAgent(PreferencesActivity.this);
			agent.startFeedbackActivity();
			overridePendingTransition(R.anim.fade, R.anim.hold);
			break;
			
		case R.id.set_aqbh_layout:
			
			if (set_aqbh_checkbox.isChecked()) {
				set_aqbh_checkbox.setChecked(false);
				mmsafePreferenceEditor.putBoolean(SAFE_SHAREPREFERENCE, false);
			} else {
				set_aqbh_checkbox.setChecked(true);
				mmsafePreferenceEditor.putBoolean(SAFE_SHAREPREFERENCE, true);
			}
			mmsafePreferenceEditor.commit();
			
			
			break;
			
		case R.id.set_gglj_layout:
			if (set_gglj_checkbox.isChecked()) {
				set_gglj_checkbox.setChecked(false);
				bgGgaoSharePreferenceEditor.putBoolean(BG_GGAO_CONFIG, false);
			} else {
				set_gglj_checkbox.setChecked(true);
				bgGgaoSharePreferenceEditor.putBoolean(BG_GGAO_CONFIG, true);
			}
			bgGgaoSharePreferenceEditor.commit();
			
			break;
			
		case R.id.set_txjz_layout:
			
			if (set_txjz_checkbox.isChecked()) {
				set_txjz_checkbox.setChecked(false);
			}
			else {
				set_txjz_checkbox.setChecked(true);
			}
			UserPreference.save(Constants.PREFERENCES_BROWSER_ENABLE_IMAGES, !isImage);
			HomeActivity.INSTANCE.applyPreferences();
			break;
		case R.id.set_dkck_layout:
			
			if (set_dkck_checkbox.isChecked()) {
				set_dkck_checkbox.setChecked(false);
				bgCKSharePreferenceEditor.putBoolean(BG_CK_CONFIG, false);
			} else {
				set_dkck_checkbox.setChecked(true);
				bgCKSharePreferenceEditor.putBoolean(BG_CK_CONFIG, true);
			}
			bgCKSharePreferenceEditor.commit();
			
			
			
			break;
			
			
		}
		
		
		
		
	}

	
	
	
	
	private ProgressDialog mProgressDialog;
//	
//	private OnSharedPreferenceChangeListener mPreferenceChangeListener;
//	
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		
//		addPreferencesFromResource(R.layout.preferences_activity);
//
//		PreferenceCategory browserPreferenceCategory = (PreferenceCategory) findPreference("BrowserPreferenceCategory");
//		Preference enablePluginsEclair = (Preference) findPreference(Constants.PREFERENCES_BROWSER_ENABLE_PLUGINS_ECLAIR);
//		Preference enablePlugins = (Preference) findPreference(Constants.PREFERENCES_BROWSER_ENABLE_PLUGINS);
//		
//		if (Build.VERSION.SDK_INT <= 7) {
//			browserPreferenceCategory.removePreference(enablePlugins);
//		} else {
//			browserPreferenceCategory.removePreference(enablePluginsEclair);
//		}
//				
////		Preference userAgentPref = (Preference) findPreference(Constants.PREFERENCES_BROWSER_USER_AGENT);
////		userAgentPref.setOnPreferenceClickListener(new OnPreferenceClickListener() {
////			@Override
////			public boolean onPreferenceClick(Preference preference) {
////				openUserAgentActivity();
////				return true;
////			}
////		});				
//		
//		 
//		//全屏设置
//		Preference fullScreenPref = (Preference) findPreference(Constants.PREFERENCES_SHOW_FULL_SCREEN);
//		fullScreenPref.setOnPreferenceClickListener(new OnPreferenceClickListener() {
//			
//			@Override
//			public boolean onPreferenceClick(Preference preference) {
////				askForRestart();
//				boolean isFullScreen = UserPreference.read("fullScreen", false);
//				if(isFullScreen){//表示全屏
//					 UserPreference.save("fullScreen", !isFullScreen); 
//				}else{//表示非全屏
//					 UserPreference.save("fullScreen", !isFullScreen); 
//				} 
//				return true;
//			}
//		});
//		//是否隐藏标题栏
////		Preference hideTitleBarPref = (Preference) findPreference(Constants.PREFERENCES_GENERAL_HIDE_TITLE_BARS);
////		hideTitleBarPref.setOnPreferenceClickListener(new OnPreferenceClickListener() {
////			
////			@Override
////			public boolean onPreferenceClick(Preference preference) {
////				askForRestart();
////				return true;
////			}
////		});
////		
//		Preference searchUrlPref = (Preference) findPreference(Constants.PREFERENCES_GENERAL_SEARCH_URL);
//		searchUrlPref.setOnPreferenceClickListener(new OnPreferenceClickListener() {
//			@Override
//			public boolean onPreferenceClick(Preference preference) {
//				openSearchUrlActivity();
//				return true;
//			}
//		});
//		
//		Preference homepagePref = (Preference) findPreference(Constants.PREFERENCES_GENERAL_HOME_PAGE);
//		homepagePref.setOnPreferenceClickListener(new OnPreferenceClickListener() {
//			@Override
//			public boolean onPreferenceClick(Preference preference) {
//				openHomepageActivity();
//				return true;
//			}
//		});
//		
////		Preference weaveServerPref = (Preference) findPreference(Constants.PREFERENCE_WEAVE_SERVER);
////		weaveServerPref.setOnPreferenceClickListener(new OnPreferenceClickListener() {
////			@Override
////			public boolean onPreferenceClick(Preference preference) {
////				openWeaveServerActivity();
////				return true;
////			}
////		});
//		
//		Preference aboutPref = (Preference) findPreference("About");
//		aboutPref.setOnPreferenceClickListener(new OnPreferenceClickListener() {
//			@Override
//			public boolean onPreferenceClick(Preference preference) {
//				openAboutActivity();
//				return true;
//			}
//		});
//		
////		Preference changelogPref = (Preference) findPreference("Changelog");
////		changelogPref.setOnPreferenceClickListener(new OnPreferenceClickListener() {			
////			@Override
////			public boolean onPreferenceClick(Preference preference) {
////				openChangelogActivity();
////				return true;
////			}
////		});
//		
//		Preference mobileViewPref = (Preference) findPreference("MobileViewList");
//		mobileViewPref.setOnPreferenceClickListener(new OnPreferenceClickListener() {
//			@Override
//			public boolean onPreferenceClick(Preference preference) {
//				openMobileViewListActivity();
//				return true;
//			}			
//		});
//		
//		Preference whiteListPref = (Preference) findPreference("AdBlockerWhiteList");
//		whiteListPref.setOnPreferenceClickListener(new OnPreferenceClickListener() {
//			@Override
//			public boolean onPreferenceClick(Preference preference) {
//				openWhiteListActivity();
//				return true;
//			}			
//		});
//		
//		Preference clearHistoryPref = (Preference) findPreference("PrivacyClearHistory");
//		clearHistoryPref.setOnPreferenceClickListener(new OnPreferenceClickListener() {
//			@Override
//			public boolean onPreferenceClick(Preference preference) {
//				clearHistory();
//				return true;
//			}			
//		});
//		
//		Preference clearformDataPref = (Preference) findPreference("PrivacyClearFormData");
//		clearformDataPref.setOnPreferenceClickListener(new OnPreferenceClickListener() {
//			@Override
//			public boolean onPreferenceClick(Preference preference) {
//				clearFormData();
//				return true;
//			}			
//		});
//		
//		Preference clearCachePref = (Preference) findPreference("PrivacyClearCache");
//		clearCachePref.setOnPreferenceClickListener(new OnPreferenceClickListener() {
//			@Override
//			public boolean onPreferenceClick(Preference preference) {
//				clearCache();
//				return true;
//			}			
//		});
//		
//		Preference clearCookiesPref = (Preference) findPreference("PrivacyClearCookies");
//		clearCookiesPref.setOnPreferenceClickListener(new OnPreferenceClickListener() {
//			@Override
//			public boolean onPreferenceClick(Preference preference) {
//				clearCookies();
//				return true;
//			}			
//		});
//		
//		Preference exportHistoryBookmarksPref = (Preference) findPreference("ExportHistoryBookmarks");
//		exportHistoryBookmarksPref.setOnPreferenceClickListener(new OnPreferenceClickListener() {
//			@Override
//			public boolean onPreferenceClick(Preference preference) {
//				exportHistoryBookmarks();
//				return true;
//			}			
//		});
//		
//		Preference importHistoryBookmarksPref = (Preference) findPreference("ImportHistoryBookmarks");
//		importHistoryBookmarksPref.setOnPreferenceClickListener(new OnPreferenceClickListener() {
//			@Override
//			public boolean onPreferenceClick(Preference preference) {
//				importHistoryBookmarks();
//				return true;
//			}			
//		});
//		
//		Preference clearHistoryBookmarksPref = (Preference) findPreference("ClearHistoryBookmarks");
//		clearHistoryBookmarksPref.setOnPreferenceClickListener(new OnPreferenceClickListener() {
//			@Override
//			public boolean onPreferenceClick(Preference preference) {
//				clearHistoryBookmarks();
//				return true;
//			}			
//		});
//		
//		mPreferenceChangeListener = new OnSharedPreferenceChangeListener() {			
//			@Override
//			public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
//				MainActivity.INSTANCE.applyPreferences();
//			}
//		};
//		
//		PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(mPreferenceChangeListener);
//	}
//	
////	/**
////	 * Ask user to restart the app. Do it if click on "Yes".
////	 */
////	private void askForRestart() {
////		ApplicationUtils.showYesNoDialog(this,
////				android.R.drawable.ic_dialog_alert,
////				R.string.PreferencesActivity_RestartDialogTitle,
////				R.string.PreferencesActivity_RestartDialogMessage,
////				new DialogInterface.OnClickListener() {
////
////			@Override
////			public void onClick(DialogInterface dialog, int which) {
////				MainActivity.INSTANCE.restartApplication();				
////			}
////			
////		});
////	}
//	
//	/**
//	 * Display the homepage preference dialog.
//	 */
	private void openHomepageActivity() {
		Intent i = new Intent(this, HomepagePreferenceActivity.class);
		startActivity(i);
		overridePendingTransition(R.anim.fade, R.anim.hold);
	}
//	
////	private void openWeaveServerActivity() {
////		Intent i = new Intent(this, WeaveServerPreferenceActivity.class);
////		startActivity(i);
////	}
//	
//	/**
//	 * Display the search url preference dialog.
//	 */
//	private void openSearchUrlActivity() {
//		Intent i = new Intent(this, SearchUrlPreferenceActivity.class);
//		startActivity(i);
//	}
//	
////	/**
////	 * Display the user agent preference dialog.
////	 */
////	private void openUserAgentActivity() {
////		Intent i = new Intent(this, UserAgentPreferenceActivity.class);
////		startActivity(i);
////	}
//	
//	/**
//	 * Display the about dialog.
//	 */
//	private void openAboutActivity() {
//		Intent i = new Intent(this, AboutActivity.class);
//		startActivity(i);
//	}
//	
//	/**显示的更新日志对话框。
//	 * Display the changelog dialog.
//	 */
////	private void openChangelogActivity() {
////		Intent i = new Intent(this, ChangelogActivity.class);
////		startActivity(i);
////	}
//	
//	/**
//	 * Display the mobile view list activity.
//	 */
//	private void openMobileViewListActivity() {
//		Intent i = new Intent(this, MobileViewListActivity.class);
//		startActivity(i);
//	}
//	
//	/**
//	 * Display the ad blocker white list activity.
//	 */
//	private void openWhiteListActivity() {
//		Intent i = new Intent(this, AdBlockerWhiteListActivity.class);
//		startActivity(i);
//	}
//	
//	/**
//	 * Clear the history.
//	 */
	private void doClearHistory() {
    	mProgressDialog = ProgressDialog.show(this,
    			this.getResources().getString(R.string.Commons_PleaseWait),
    			this.getResources().getString(R.string.Commons_ClearingHistory));
    	
    	new HistoryClearer();
    }
//	
//	/**
//	 * Display confirmation and clear the history.
//	 */
	private void clearHistory() {
		ApplicationUtils.showYesNoDialog(this,
				android.R.drawable.ic_dialog_alert,
				R.string.Commons_ClearHistory,
				R.string.Commons_NoUndoMessage,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
		    			doClearHistory();
					}			
		});
	}
	
//	/**
//	 * Clear form data.
//	 */
	private void doClearFormData() {
    	mProgressDialog = ProgressDialog.show(this,
    			this.getResources().getString(R.string.Commons_PleaseWait),
    			this.getResources().getString(R.string.Commons_ClearingFormData));
    	
    	new FormDataClearer();
    }
//	
//	/**
//	 * Display confirmation and clear form data.
//	 */
	private void clearFormData() {
		ApplicationUtils.showYesNoDialog(this,
				android.R.drawable.ic_dialog_alert,
				R.string.Commons_ClearFormData,
				R.string.Commons_NoUndoMessage,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						doClearFormData();
					}			
		});
	}
//	
//	/**
//	 * Clear the cache.
//	 */
	private void doClearCache() {
    	mProgressDialog = ProgressDialog.show(this,
    			this.getResources().getString(R.string.Commons_PleaseWait),
    			this.getResources().getString(R.string.Commons_ClearingCache));
    	
    	new CacheClearer();
    }
	
//	/**
//	 * Display confirmation and clear the cache.
//	 */
	private void clearCache() {
		ApplicationUtils.showYesNoDialog(this,
				android.R.drawable.ic_dialog_alert,
				R.string.Commons_ClearCache,
				R.string.Commons_NoUndoMessage,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						doClearCache();
					}			
		});
	}
	
//	/**
//	 * Clear cookies.
//	 */
	private void doClearCookies() {
    	mProgressDialog = ProgressDialog.show(this,
    			this.getResources().getString(R.string.Commons_PleaseWait),
    			this.getResources().getString(R.string.Commons_ClearingCookies));
    	
    	new CookiesClearer();
    }
	
//	/**
//	 * Display confirmation and clear cookies.
//	 */
	private void clearCookies() {
		ApplicationUtils.showYesNoDialog(this,
				android.R.drawable.ic_dialog_alert,
				R.string.Commons_ClearCookies,
				R.string.Commons_NoUndoMessage,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						doClearCookies();
					}			
		});
	}
	
	private void doClearHistoryBookmarks(int choice) {
		mProgressDialog = ProgressDialog.show(this,
    			this.getResources().getString(R.string.Commons_PleaseWait),
    			this.getResources().getString(R.string.Commons_ClearingHistoryBookmarks));
		
		new HistoryBookmarksClearer(choice);
	}
	
//	/**
//	 * Clear the history.
//	 */
//	private void clearHistoryBookmarks() {
//		
//		final String[] choices = new String[] { getString(R.string.Commons_History), getString(R.string.Commons_Bookmarks), getString(R.string.Commons_All) };
//		
//		AlertDialog.Builder builder = new AlertDialog.Builder(this);
//    	builder.setInverseBackgroundForced(true);
//    	builder.setIcon(android.R.drawable.ic_dialog_info);
//    	builder.setTitle(R.string.Commons_ClearHistoryBookmarks);
//    	builder.setSingleChoiceItems(choices, 0, new OnClickListener() {
//			@Override
//			public void onClick(DialogInterface dialog, int which) {												
//				doClearHistoryBookmarks(which);
//				dialog.dismiss();				
//			}    		
//    	});
//    	
//    	builder.setCancelable(true);
//    	builder.setNegativeButton(R.string.Commons_Cancel, null);
//    	
//    	AlertDialog alert = builder.create();
//    	alert.show();
//	}
//	
//	/**
//	 * Import the given file to bookmarks and history.
//	 * @param fileName The file to import.
//	 */
	private void doImportHistoryBookmarks(String fileName) {
		
		if (ApplicationUtils.checkCardState(this, true)) {
			mProgressDialog = ProgressDialog.show(this,
	    			this.getResources().getString(R.string.Commons_PleaseWait),
	    			this.getResources().getString(R.string.Commons_ImportingHistoryBookmarks));
			
			XmlHistoryBookmarksImporter importer = new XmlHistoryBookmarksImporter(this, fileName, mProgressDialog);
			new Thread(importer).start();
		}
		
	}
//	
//	/**
//	 * Ask the user the file to import to bookmarks and history, and launch the import. 
//	 */
//	private void importHistoryBookmarks() {
//		List<String> exportedFiles = IOUtils.getExportedBookmarksFileList();    	
//    	
//    	final String[] choices = exportedFiles.toArray(new String[exportedFiles.size()]);
//    	
//    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
//    	builder.setInverseBackgroundForced(true);
//    	builder.setIcon(android.R.drawable.ic_dialog_info);
//    	builder.setTitle(getResources().getString(R.string.Commons_ImportHistoryBookmarksSource));
//    	builder.setSingleChoiceItems(choices,
//    			0,
//    			new OnClickListener() {
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//								
//				doImportHistoryBookmarks(choices[which]);				
//				
//				dialog.dismiss();				
//			}    		
//    	});    	
////    	
//    	builder.setCancelable(true);
//    	builder.setNegativeButton(R.string.Commons_Cancel, null);
//    	
//    	AlertDialog alert = builder.create();
//    	alert.show();
//	}
////	
//	/**
//	 * Export the bookmarks and history.
//	 */
	private void doExportHistoryBookmarks() {
		if (ApplicationUtils.checkCardState(this, true)) {
			mProgressDialog = ProgressDialog.show(this,
	    			this.getResources().getString(R.string.Commons_PleaseWait),
	    			this.getResources().getString(R.string.Commons_ExportingHistoryBookmarks));
			
			XmlHistoryBookmarksExporter exporter = new XmlHistoryBookmarksExporter(this,
					DateUtils.getNowForFileName() + ".xml",
					BookmarksProviderWrapper.getAllStockRecords(this.getContentResolver()),
					mProgressDialog);
			
			new Thread(exporter).start();
		}
	}
//	
//	/**
//	 * Ask the user to confirm the export. Launch it if confirmed.
//	 */
	private void exportHistoryBookmarks() {
		ApplicationUtils.showYesNoDialog(this,
				android.R.drawable.ic_dialog_info,
				R.string.Commons_HistoryBookmarksExportSDCardConfirmation,
				R.string.Commons_OperationCanBeLongMessage,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						doExportHistoryBookmarks();
					}			
		});
	}
//	
//	/**
//	 * Base class for all clear operations launched as Runnable.
//	 */
	private abstract class AbstractClearer implements Runnable {

		/**
		 * Constructor. Launch itself as a Thread.
		 */
		public AbstractClearer() {
			new Thread(this).start();
		}
		
		protected Handler mHandler = new Handler() {
			public void handleMessage(Message msg) {
				mProgressDialog.dismiss();
			}
		};
	}
	
	private class HistoryBookmarksClearer extends AbstractClearer {

		private int mChoice;
		
		public HistoryBookmarksClearer(int choice) {
			mChoice = choice;
		}
		
		@Override
		public void run() {
			
			switch (mChoice) {
			case 0:
				BookmarksProviderWrapper.clearHistoryAndOrBookmarks(PreferencesActivity.this.getContentResolver(), true, false);
				break;
			case 1:
				BookmarksProviderWrapper.clearHistoryAndOrBookmarks(PreferencesActivity.this.getContentResolver(), false, true);
				break;
			case 2:
				BookmarksProviderWrapper.clearHistoryAndOrBookmarks(PreferencesActivity.this.getContentResolver(), true, true);
				break;
			default: break;
			}
			
			mHandler.sendEmptyMessage(0);
		}		
	}
	
//	/**
//	 * History clearer thread.
//	 */
	private class HistoryClearer implements Runnable {
		
		/**
		 * Constructor.
		 */
		public HistoryClearer() {
			new Thread(this).start();
		}

		@Override
		public void run() {
			// Clear DB History
			BookmarksProviderWrapper.clearHistoryAndOrBookmarks(getContentResolver(), true, false);
			
			// Clear WebViews history
			for (CustomWebView webView : Controller.getInstance().getWebViewList()) {
				webView.clearHistory();
			}
			
			handler.sendEmptyMessage(0);
		}

		private Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				mProgressDialog.dismiss();
			}
		};
	}
//	
//	/**
//	 * Form data clearer thread.
//	 */
	private class FormDataClearer implements Runnable {
		/**
		 * Constructor.
		 */
		public FormDataClearer() {
			new Thread(this).start();
		}
		@Override
		public void run() {
			for (CustomWebView webView : Controller.getInstance().getWebViewList()) {
				webView.clearFormData();
			}

			handler.sendEmptyMessage(0);
		}
		private Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				mProgressDialog.dismiss();
			}
		};
	}
//	
//	/**
//	 * Cache clearer thread.
//	 */
	private class CacheClearer implements Runnable {
		/**
		 * Constructor.
		 */
		public CacheClearer() {
			new Thread(this).start();
		}
		@Override
		public void run() {
			// Only need to clear the cache from one WebView, as it is application-based.
			CustomWebView webView = Controller.getInstance().getWebViewList().get(0);
			webView.clearCache(true);

			handler.sendEmptyMessage(0);
		}
		private Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				mProgressDialog.dismiss();
			}
		};
	}
//	
//	/**
//	 * Cookies clearer thread.
//	 */
	private class CookiesClearer implements Runnable {
		/**
		 * Constructor.
		 */
		public CookiesClearer() {
			new Thread(this).start();
		}
		@Override
		public void run() {
			CookieManager.getInstance().removeAllCookie();
			handler.sendEmptyMessage(0);
		}
		private Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				mProgressDialog.dismiss();
			}
		};
	}

}
