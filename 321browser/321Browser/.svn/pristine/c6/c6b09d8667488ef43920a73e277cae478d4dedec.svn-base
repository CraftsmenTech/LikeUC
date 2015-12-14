
package cn.hi321.browser.ui.runnables;


import android.content.Context;
import android.preference.PreferenceManager;
import cn.hi321.browser.config.Constants;
import cn.hi321.browser.providers.BookmarksProviderWrapper;

/**
 * Runnable to update and truncate the history in background.
 */
public class HistoryUpdater implements Runnable {

	private Context mContext;
	private String mTitle;
	private String mUrl;
	private String mOriginalUrl;
	
	/**
	 * Constructor.
	 * @param context The current context.
	 * @param title The title.
	 * @param url The url.
	 */
	public HistoryUpdater(Context context, String title, String url, String originalUrl) {
		mContext = context;
		mTitle = title;
		mUrl = url;
		mOriginalUrl = originalUrl;
		
		if (mUrl.startsWith(Constants.URL_GOOGLE_MOBILE_VIEW_NO_FORMAT)) {
			mUrl = mUrl.substring(Constants.URL_GOOGLE_MOBILE_VIEW_NO_FORMAT.length());
		}
	}
	
	@Override
	public void run() {
		BookmarksProviderWrapper.updateHistory(mContext.getContentResolver(), mTitle, mUrl, mOriginalUrl);
		BookmarksProviderWrapper.truncateHistory(mContext.getContentResolver(),
				PreferenceManager.getDefaultSharedPreferences(mContext).getString(Constants.PREFERENCES_BROWSER_HISTORY_SIZE, "90"));
	}

}
