

package cn.hi321.browser.controllers;

import java.util.ArrayList;
import java.util.List;


import cn.hi321.browser.model.DbAdapter;
import cn.hi321.browser.model.items.DownloadItem;
import cn.hi321.browser.ui.components.CustomWebView;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Controller implementation.
 */
public final class Controller {
	
	private SharedPreferences mPreferences;

	private List<CustomWebView> mWebViewList;
	private List<DownloadItem> mDownloadList;
	private List<String> mAdBlockWhiteList = null;
	private List<String> mMobileViewUrlList = null;
	
	/**
	 * Holder for singleton implementation.
	 */
	private static final class ControllerHolder {
		private static final Controller INSTANCE = new Controller();
		/**
		 * Private Constructor.
		 */
		private ControllerHolder() { }
	}
	
	/**
	 * Get the unique instance of the Controller.
	 * @return The instance of the Controller
	 */
	public static Controller getInstance() {
		return ControllerHolder.INSTANCE;
	}
	
	/**
	 * Private Constructor.
	 */
	private Controller() {
		mDownloadList = new ArrayList<DownloadItem>();
	}
		
	/**
	 * Get the list of current WebViews.
	 * @return The list of current WebViews.
	 */
	public List<CustomWebView> getWebViewList() {
		return mWebViewList;
	}
	
	/**
	 * Set the list of current WebViews.
	 * @param list The list of current WebViews.
	 */
	public void setWebViewList(List<CustomWebView> list) {
		mWebViewList = list;
	}
	
	/**
	 * Get a SharedPreferences instance.
	 * @return The SharedPreferences instance.
	 */
	public SharedPreferences getPreferences() {
		return mPreferences;
	}

	/**
	 * Set the SharedPreferences instance.
	 * @param preferences The SharedPreferences instance.
	 */
	public void setPreferences(SharedPreferences preferences) {
		this.mPreferences = preferences;
	}
	
	/**
	 * Get the current download list.
	 * @return The current download list.
	 */
	public List<DownloadItem> getDownloadList() {
		return mDownloadList;
	}
	
	/**
	 * Add an item to the download list.
	 * @param item The new item.
	 */
	public void addToDownload(DownloadItem item) {
		mDownloadList.add(item);
	}
	
	public synchronized void clearCompletedDownloads() {
		List<DownloadItem> newList = new ArrayList<DownloadItem>();
		
		for (DownloadItem item : mDownloadList) {
			if (!item.isFinished()) {
				newList.add(item);
			}
		}
		
		mDownloadList.clear();
		mDownloadList = newList;
	}
	
	/**
	 * Get the list of white-listed url for the AdBlocker.
	 * @param context The current context.
	 * @return A list of String url.
	 */	
	public List<String> getAdBlockWhiteList(Context context) {
		if (mAdBlockWhiteList == null) {
			DbAdapter db = new DbAdapter(context);
			db.open();
			mAdBlockWhiteList = db.getWhiteList();
			db.close();
		}
		return mAdBlockWhiteList;
	}
	
	/**
	 * Reset the AdBlock white list, so that it will be reloaded.
	 */
	public void resetAdBlockWhiteList() {
		mAdBlockWhiteList = null;
	}	
	
	/**
	 * Get the list of mobile view urls.
	 * @param context The current context.
	 * @return A list of String url.
	 */
	public List<String> getMobileViewUrlList(Context context) {
		if (mMobileViewUrlList == null) {
			DbAdapter db = new DbAdapter(context);
			db.open();
			mMobileViewUrlList = db.getMobileViewUrlList();
			db.close();
		}
		return mMobileViewUrlList;
	}
	
	/**
	 * Reset the mobile view url list, so that it will be reloaded.
	 */
	public void resetMobileViewUrlList() {
		mMobileViewUrlList = null;
	}
	
}
