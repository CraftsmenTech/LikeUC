package cn.hi321.browser;

import java.util.ArrayList;

import android.app.Application;
import android.content.Context;
import cn.hi321.browser.download.DownloadManager;
import cn.hi321.browser.utils.DeviceInfoUtil;

/**
 * 
 * @author yanggf
 * 
 */
public class BrowserApp extends Application {

	private int start = 0;
	private long msreport = 0;
	private String startionStr;
	private String fristBufferbposStr;
	private String mFrisBufferttotalTimeStr;
	private int mIntRetType = 0;
	private String msurl = "";
	private boolean isFirstStart = true;
	private boolean isDownlaodReStart = false;
	/**
	 * Singleton pattern
	 */
	private static BrowserApp instance;
	/**
	 * Provides interface for download related actions.
	 */
	private DownloadManager mDownloadManager;

	private Boolean isReplay = false;

	private long stkpos = 0;

	private long stktime = 0;

	private int state = 0;

	private ArrayList<String> mDownloadUrlList = null;

	private boolean isUsedFgFlag = true;
	private boolean isUsedBgFlag = true;

	private Context mContext = null;
	private boolean isPushToMediaInfo = false;
	private boolean mIsAppExit = false;

	private boolean isAppOnBg = false;

	private boolean isFSingalActivityToBg = false;

	public static BrowserApp getInstance() {
		return instance;
	}

	public boolean isFirstStart() {
		return isFirstStart;
	}

	public void setFirstStart(boolean isFirstStart) {
		this.isFirstStart = isFirstStart;
	}

	public boolean isPushToMediaInfo() {
		return isPushToMediaInfo;
	}

	public void setPushToMediaInfo(boolean isPushToMediaInfo) {
		this.isPushToMediaInfo = isPushToMediaInfo;
	}

	public boolean isDownlaodReStart() {
		return isDownlaodReStart;
	}

	public void setDownloadReStart(boolean isDownlaodReStart) {
		this.isDownlaodReStart = isDownlaodReStart;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		mContext = getApplicationContext();
		/**
		 * modify by yanggf 2012-12-17 动态获取应用程序的版本
		 */
		DeviceInfoUtil.getAppVersionName(mContext);
		DeviceInfoUtil.getMacAddress(mContext);

		start = 0;
		instance = this;
		mDownloadManager = new DownloadManager(this);

	}

	public String getMsurl() {
		return msurl;
	}

	public void setMsurl(String msurl) {
		this.msurl = msurl;
	}

	public ArrayList<String> getmDownloadUrlList() {
		return mDownloadUrlList;
	}

	public void setmDownloadUrlList(ArrayList<String> mDownloadUrlList) {
		this.mDownloadUrlList = mDownloadUrlList;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	// caoqh add begin
	public int getRetType() {
		return mIntRetType;
	}

	public void setRetType(int iRetType) {
		this.mIntRetType = iRetType;
	}

	// caoqh add end

	public long getMsreport() {
		return msreport;
	}

	public void setMsreport(long msreport) {
		this.msreport = msreport;
	}

	public String getStartionStr() {
		return startionStr;
	}

	public void setStartionStr(String startionStr) {
		this.startionStr = startionStr;
	}

	public String getFristBufferbposStr() {
		return fristBufferbposStr;
	}

	public void setFristBufferbposStr(String fristBufferbposStr) {
		this.fristBufferbposStr = fristBufferbposStr;
	}

	public String getmFrisBufferttotalTimeStr() {
		return mFrisBufferttotalTimeStr;
	}

	public void setmFrisBufferttotalTimeStr(String mFrisBufferttotalTimeStr) {
		this.mFrisBufferttotalTimeStr = mFrisBufferttotalTimeStr;
	}

	public long getStktime() {
		return stktime;
	}

	public void setStktime(long stktime) {
		this.stktime = stktime;
	}

	public Boolean getIsReplay() {
		return isReplay;
	}

	public void setIsReplay(Boolean isReplay) {
		this.isReplay = isReplay;
	}

	public long getStkpos() {
		return stkpos;
	}

	public void setStkpos(long stkpos) {
		this.stkpos = stkpos;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public DownloadManager getDownloadManager() {
		return mDownloadManager;
	}

	public boolean isUsedFgFlag() {
		return isUsedFgFlag;
	}

	public void setUsedFgFlag(boolean isUsedFgFlag) {
		this.isUsedFgFlag = isUsedFgFlag;
	}

	public boolean isUsedBgFlag() {
		return isUsedBgFlag;
	}

	public void setUsedBgFlag(boolean isUsedBgFlag) {
		this.isUsedBgFlag = isUsedBgFlag;
	}

	public boolean ismIsAppExit() {
		return mIsAppExit;
	}

	public void setmIsAppExit(boolean mIsAppExit) {
		this.mIsAppExit = mIsAppExit;
	}

	public boolean isAppOnBg() {
		return isAppOnBg;
	}

	public void setAppOnBg(boolean isAppOnBg) {
		this.isAppOnBg = isAppOnBg;
	}

	public boolean isFSingalActivityToBg() {
		return isFSingalActivityToBg;
	}

	public void setFSingalActivityToBg(boolean isFSingalActivityToBg) {
		this.isFSingalActivityToBg = isFSingalActivityToBg;
	}

}
