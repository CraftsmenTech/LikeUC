package cn.hi321.browser.download;

import java.util.ArrayList;
import java.util.HashMap;

import cn.hi321.browser.BrowserApp;

/**
 * Download jobs provider. Handles storing DownloadJobs.
 * 
 */
public class DownloadProvider {
	private static final String TAG = "DownloadProvider";

	private ArrayList<DownloadJob> mQueuedJobs;
	private ArrayList<DownloadJob> mCompletedJobs;
	private DownloadManager mDownloadManager;

	private DownloadDao mDownloadDao;

	@SuppressWarnings("unused")
	private static final String DB_PATH = "/download.db";

	public DownloadProvider(DownloadManager downloadManager) {
		mDownloadManager = downloadManager;

		mQueuedJobs = new ArrayList<DownloadJob>();
		mCompletedJobs = new ArrayList<DownloadJob>();

		mDownloadDao = new DownloadDaoImplWithoutContext("download.db");
		loadOldDownloads(); // TODO:有问题，是否真的重启old的下载:并不是重启旧的下载，而只是load旧的下载，即初始化mQueuedJobs和mQueuedJobs，旧重新开始下;
	}

	private void loadOldDownloads() {
		BrowserApp.getInstance().setDownloadReStart(true);
		ArrayList<DownloadJob> oldDownloads = mDownloadDao.getAllDownloadJobs();
		for (DownloadJob dJob : oldDownloads) {
			if (dJob.getProgress() == 100) {
				mCompletedJobs.add(dJob);
			} else {
				dJob.getEntity().setFromStart(true);
				mDownloadManager.download(dJob.getEntity());
			}
		}
		mDownloadManager.notifyObservers(); // 更新UI界面
	}

	public ArrayList<DownloadJob> getAllDownloads() {
		ArrayList<DownloadJob> allDownloads = new ArrayList<DownloadJob>();
		allDownloads.addAll(mCompletedJobs);
		allDownloads.addAll(mQueuedJobs);
		return allDownloads;
	}

	public ArrayList<DownloadJob> getCompletedDownloads() {
		return mCompletedJobs;
	}

	public ArrayList<DownloadJob> getQueuedDownloads() {
		return mQueuedJobs;
	}

	public void downloadCompleted(DownloadJob job) {
		mQueuedJobs.remove(job);
		mCompletedJobs.add(job);
		mDownloadDao.setStatus(job.getEntity(), 1);
		mDownloadManager.notifyObservers();
	}

	/**
	 * 
	 * @param downloadJob
	 * @return
	 */
	public boolean queueDownload(DownloadJob downloadJob) {
		// 判断是否是第一次下载
		for (DownloadJob dJob : mCompletedJobs) {
			if (dJob.getEntity().getId()
					.equals(downloadJob.getEntity().getId()))
				return false;
		}

		for (DownloadJob dJob : mQueuedJobs) {
			if (downloadJob.getEntity().getId()
					.equals(dJob.getEntity().getId()))
				return false;
		}

		// 若未下载，加入数据库成功后，再开始下载，并更新UI
		if (mDownloadDao.add(downloadJob.getEntity())) {
			mQueuedJobs.add(downloadJob);
			mDownloadManager.notifyObservers();
			return true;
		} else {
			return false;
		}
	}

	public void removeDownload(DownloadJob job) {
		if (job.getProgress() < 100) {
			if (job.getStatus() == DownloadJob.DOWNLOADING)
				job.pause();
			mQueuedJobs.remove(job);
		} else {
			mCompletedJobs.remove(job);
		}

		mDownloadDao.remove(job);

		if (DownloadActivity.mSizeManager != null) {
			DownloadActivity.mSizeManager.ansynHandlerSdcardSize();
		}

		mDownloadManager.notifyObservers();
	}

	public boolean updateDownloadEntity(DownloadJob downloadJob) {
		return mDownloadDao.add(downloadJob.getEntity());
	}

	public boolean IsOnDownloadList(String hashid) {
		// 判断是否是第一次下载
		for (DownloadJob dJob : mCompletedJobs) {
			if (dJob.getEntity().getId().equals(hashid))
				return true;
		}

		for (DownloadJob dJob : mQueuedJobs) {
			if (dJob.getEntity().getId().equals(hashid))
				return true;
		}
		return false;
	}

	public HashMap<String, String> getDownloadEpisode(String medianame) {
		HashMap<String, String> depisode = new HashMap<String, String>();
		for (DownloadJob job : getAllDownloads()) {
			if (job.getEntity().getName().equals(medianame))
				depisode.put(job.getEntity().getName(), medianame);
		}
		return depisode;
	}

	/**
	 * 暂停时设置状态为3;
	 */
	public void setStatus(DownloadEntity entity, int status) {
		mDownloadDao.setStatus(entity, status);
	}
}
