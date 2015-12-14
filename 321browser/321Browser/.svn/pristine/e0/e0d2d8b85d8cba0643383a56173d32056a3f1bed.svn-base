package cn.hi321.browser.ui.activities;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import cn.hi321.browser.utils.UserPreference;
import cn.hi321.browser2.R;

/**
 * Combined bookmarks and history activity.
 */
public class BookmarksHistoryActivity extends TabActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		UserPreference.ensureIntializePreference(BookmarksHistoryActivity.this);
		fullScreenChange();
		setContentView(R.layout.bookmarks_history_activity);
		setTitle(R.string.BookmarksListActivity_Title);
		Resources res = getResources();
		TabHost tabHost = getTabHost();
		TabHost.TabSpec spec;
		Intent intent;

		// Bookmarks
		intent = new Intent().setClass(this, BookmarksListActivity.class);

		spec = tabHost
				.newTabSpec("bookmarks")
				.setIndicator(res.getString(R.string.Main_MenuShowBookmarks),
						res.getDrawable(R.drawable.ic_tab_bookmarks))
				.setContent(intent);
		tabHost.addTab(spec);

		// History
		intent = new Intent().setClass(this, HistoryListActivity.class);

		spec = tabHost
				.newTabSpec("history")
				.setIndicator(res.getString(R.string.Main_MenuShowHistory),
						res.getDrawable(R.drawable.ic_tab_history))
				.setContent(intent);
		tabHost.addTab(spec);
		//
		// if
		// (PreferenceManager.getDefaultSharedPreferences(this).getBoolean(Constants.PREFERENCE_USE_WEAVE,
		// false)) {
		// // Weave bookmarks
		// intent = new Intent().setClass(this,
		// WeaveBookmarksListActivity.class);
		//
		// spec =
		// tabHost.newTabSpec("weave").setIndicator(res.getString(R.string.WeaveBookmarksListActivity_Title),
		// res.getDrawable(R.drawable.ic_tab_weave))
		// .setContent(intent);
		// tabHost.addTab(spec);
		// }

		tabHost.setCurrentTab(0);

		tabHost.setOnTabChangedListener(new OnTabChangeListener() {
			@Override
			public void onTabChanged(String tabId) {
				if (tabId.equals("bookmarks")) {
					setTitle(R.string.BookmarksListActivity_Title);
				} else if (tabId.equals("history")) {
					setTitle(R.string.HistoryListActivity_Title);
				} else if (tabId.equals("weave")) {
					setTitle(R.string.WeaveBookmarksListActivity_Title);
				} else {
					setTitle(R.string.ApplicationName);
				}
			}
		});
	}

	/**
	 * 全屏切换
	 */
	public void fullScreenChange() {

		boolean isFullScreen = UserPreference.read("fullScreen", false);
		if (isFullScreen) {
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
					WindowManager.LayoutParams.FLAG_FULLSCREEN);
		} else {
			getWindow().setFlags(
					WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN,
					WindowManager.LayoutParams.FLAG_FULLSCREEN);
		}

	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}
}
