package cn.hi321.browser.download;

import android.text.TextUtils;
import cn.hi321.browser.BrowserApp;

/**
 * Download job builder
 * 
 */
public class DownloadJob {

	/**
	 * 下载的媒体
	 */
	private DownloadEntity entity;
	/**
	 * 下载的路径：/sdcard/321Browser/download/
	 */
	private String mDestination;

	private DownloadTask mDownloadTask;
	private DownloadJobListener mListener;

	/**
	 * 下载进度：初始化为 0
	 */
	private int mProgress;
	private long mTotalSize;
	public long mDownloadedSize;

	private int mStartId;

	private DownloadManager mDownloadManager;
	private boolean isSupportBreakPoint = true;
	private String mRate;
	private long mOldTime = 0;
	private long mOldBytes = 0;
	private boolean isUserStart = false;
	private boolean isUserPause = false;
	private boolean isOfflianeCachePause = false;
	private int mExceptionType = 0;
	public int dn = 1;
	public int retryNum = 0;

	// job 的各种状态
	public static final int INIT = 5;
	public static final int DOWNLOADING = 2;
	public static final int PAUSE = 3;
	public static final int WAITING = 4;
	public static final int COMPLETE = 1;
	public static final int DELETE = 6;
	public static final int PAUSEONSPEED = 7;
	public static final int PAUSEONOFFLINECACHE = 8;
	public static final int NO_USER_PAUSE = 0;
	private int status = INIT;

	public DownloadJob(DownloadEntity data, String destination, int startId) {
		this.entity = data;
		mDestination = destination;
		mDownloadedSize = DownloadHelper.getDownloadedFileSize(data);
		mTotalSize = data.getContentLength();
		// mProgress = 0;
		mProgress = initProgress();
		status = data.getStatus();
		mStartId = startId;
		mDownloadManager = BrowserApp.getInstance().getDownloadManager();
	}

	public DownloadEntity getEntity() {
		return entity;
	}

	public void setEntity(DownloadEntity entity) {
		this.entity = entity;
	}

	public String getDestination() {
		return mDestination;
	}

	public void setDestination(String mDestination) {
		this.mDestination = mDestination;
	}

	public void setListener(DownloadJobListener listener) {
		this.mListener = listener;
	}

	public int getProgress() {
		return mProgress;
	}

	public void setProgress(int mProgress) {
		this.mProgress = mProgress;
	}

	public long getmTotalSize() {
		return mTotalSize;
	}

	public void setmTotalSize(long mTotalSize) {
		this.mTotalSize = mTotalSize;
	}

	public long getDownloadedSize() {
		return mDownloadedSize;
	}

	public void setDownloadedSize(long mDownloadedSize) {
		this.mDownloadedSize = mDownloadedSize;
		int oldProgress = mProgress;
		if (mTotalSize > 0)
			mProgress = (int) ((mDownloadedSize * 100) / mTotalSize);
		if (mProgress != oldProgress) {
			mDownloadManager.notifyObservers();
			notifyDownloadOnUpdate();
		}
	}

	public int getmStartId() {
		return mStartId;
	}

	public void setmStartId(int mStartId) {
		this.mStartId = mStartId;
	}

	public int getmExceptionType() {
		return mExceptionType;
	}

	public void setmExceptionType(int mExceptionType) {
		this.mExceptionType = mExceptionType;
	}

	public boolean isOfflianeCachePause() {
		return isOfflianeCachePause;
	}

	public void setOfflianeCachePause(boolean isOfflianeCachePause) {
		this.isOfflianeCachePause = isOfflianeCachePause;
	}

	public void start() {
		int num = mDownloadManager.getDownloadNum();
		mExceptionType = 0;
		if (BrowserApp.getInstance().getDownloadManager().downloading_num < num) {
			mDownloadTask = new DownloadTask(this);
			mDownloadTask.execute();
			BrowserApp.getInstance().getDownloadManager().downloading_num++;
			status = DOWNLOADING;
		} else {
			status = WAITING;
		}
		mDownloadManager.getProvider().setStatus(entity, INIT);
	}

	public void notifyDownloadStarted() {
		if (mListener != null)
			mListener.downloadStarted(this);
	}

	public void notifyDownloadEnded() {
		if (!mDownloadTask.isCancelled()) {
			if (mListener != null)
				mListener.downloadEnded(this);
			mProgress = 100;
		}
		// 开始下一个下载任务
		BrowserApp.getInstance().getDownloadManager().downloading_num--;
		mDownloadManager.statNextTask();

	}

	public void notifyDownloadOnDownloading() {
		if (mListener != null)
			mListener.downloadOnDownloading(this);
	}

	public void notifyDownloadOnUpdate() {
		if (mListener != null)
			mListener.updateNotifyOnDownloading(this);
	}

	public void notifyDownloadOnPause() {
		if (mListener != null)
			mListener.downloadOnPause(this);
	}

	public DownloadTask getDownloadTask() {
		return mDownloadTask;
	}

	/**
	 * Downloading-->Pause
	 * 
	 * @return
	 */
	public boolean pause() {
		setUserPause(true);
		entity.setUserPauseWhen3G(false);
		this.status = PAUSE;
		dn = BrowserApp.getInstance().getDownloadManager().downloading_num;
		BrowserApp.getInstance().getDownloadManager().downloading_num--;
		mDownloadManager.statNextTask();
		mDownloadManager.getProvider().setStatus(entity, PAUSE);
		return mDownloadTask.cancel(true);
	}

	/**
	 * 当限速时的处理
	 */
	public void pauseOnSpeed() {
		this.status = PAUSEONSPEED;
		BrowserApp.getInstance().getDownloadManager().downloading_num--;
		mDownloadTask.cancel(true);
	}

	/**
	 * 当离线缓存时的处理
	 */
	public void pauseOnOfflineCache() {
		this.status = PAUSEONOFFLINECACHE;
		BrowserApp.getInstance().getDownloadManager().downloading_num--;
		mDownloadTask.cancel(true);
		isOfflianeCachePause = true;
	}

	/**
	 * 当wifi下载设置切换的处理
	 */
	public void pauseOnSetWifiChange() {
		this.status = PAUSE;
		BrowserApp.getInstance().getDownloadManager().downloading_num--;
		mDownloadTask.cancel(true);

	}

	/**
	 * 当不开启后台下载退出时的处理
	 */
	public void pauseOnExit() {
		this.status = PAUSEONSPEED;
		BrowserApp.getInstance().getDownloadManager().downloading_num--;
		if (mListener != null)
			mListener.downloadPaused(this);
		mDownloadTask.cancel(true);
	}

	/**
	 * Waiting-->Pause
	 */
	public void cancel() {
		setUserPause(true);
		this.status = PAUSE;
		mDownloadManager.getProvider().setStatus(entity, PAUSE);
	}

	public boolean isSupportBreakPoint() {
		return isSupportBreakPoint;
	}

	public void setSupportBreakPoint(boolean isSupportBreakPoint) {
		this.isSupportBreakPoint = isSupportBreakPoint;
	}

	public boolean isUserStart() {
		return isUserStart;
	}

	public void setUserStart(boolean isUserStart) {
		this.isUserStart = isUserStart;
	}

	public boolean isUserPause() {
		return isUserPause;
	}

	public boolean isDownloadOnlyWifi() {
		return mDownloadManager.IsDownloadOnlyWifi();
	}

	public void setUserPause(boolean isUserPause) {
		this.isUserPause = isUserPause;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public void setRate() {
		long curTime = System.currentTimeMillis();

		if (((curTime - mOldTime) / 1000) >= 2) {

			mOldTime = curTime;
			this.mRate = getRate(mDownloadedSize - mOldBytes);
			mOldBytes = mDownloadedSize;
			mDownloadManager.notifyObservers();
			notifyDownloadOnUpdate();
		}
	}

	public String getRate() {
		if (TextUtils.isEmpty(mRate)) {
			mRate = "0.0kb/s";
		}

		return mRate;
	}

	private String getRate(long l) {
		double rate = l / 1024 / 2;
		if (rate > 2000 || rate < 0.0)
			rate = 0.0;
		return rate + "kb/s";
	}

	public int getNotifyIdByHashId() {
		try {
			String notifyId = DownloadHelper.changeHashidToNumStr(this
					.getEntity().getId());
			if (notifyId != null) {
				return Integer.parseInt(notifyId.substring(0, 6));
			} else {
				return 0;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public int initProgress() {
		return mTotalSize == 0 ? 0
				: (int) ((mDownloadedSize * 100) / mTotalSize);
	}

	@Override
	public String toString() {
		return "DownloadJob [entity=" + entity + ", mProgress=" + mProgress
				+ ", mTotalSize=" + mTotalSize + ", isUserStart=" + isUserStart
				+ ", isUserPause=" + isUserPause + ", isOfflianeCachePause="
				+ isOfflianeCachePause + ", mExceptionType=" + mExceptionType
				+ ", status=" + status + "]";
	}

}
