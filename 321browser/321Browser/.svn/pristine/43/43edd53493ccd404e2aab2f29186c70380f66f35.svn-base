package cn.hi321.browser.download;

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Toast;
import cn.hi321.browser.db.PlayHistoryDao;
import cn.hi321.browser.model.DownloadInfo;
import cn.hi321.browser.model.MediaItem;
import cn.hi321.browser.model.PlayData;
import cn.hi321.browser.model.PlayHistoryInfo;
import cn.hi321.browser.player.SystemPlayer;
import cn.hi321.browser.ui.activities.DownloadSetActivity;
import cn.hi321.browser.utils.LogUtil;
import cn.hi321.browser.utils.NetworkUtil;
import cn.hi321.browser.utils.StringUtil;
import cn.hi321.browser.utils.ToastUtil;
import cn.hi321.browser2.R;

public class DownloadManager {
	private static final String TAG = "DownloadManager";

	private Context mContext;
	private DownloadProvider mProvider;
	private ArrayList<DownloadObserver> mObservers;
	private PlayHistoryDao playHistoryDao;
	public int downloading_num = 0;

	public DownloadManager(Context context) {
		this.mContext = context;
		mObservers = new ArrayList<DownloadObserver>();
		this.mProvider = new DownloadProvider(this); // 和上一句顺序不能颠倒，否则会报空指针异常
		playHistoryDao = PlayHistoryDao.getInstance(context);
	}

	/**
	 * Starts download of MediaItem passed in mediaItem
	 * 
	 * @param mediaItem
	 */
	public void download(MediaItem mediaItem, Context activity) {
		if (mediaItem == null || StringUtil.isEmpty(mediaItem.getId())) {
			Toast.makeText(mContext, R.string.download_url_is_null, 0).show();
			return;
		}
		if (mProvider.IsOnDownloadList(mediaItem.getId())) {
			Toast.makeText(mContext, "已添加", 0).show();
		} else {
			// 通过purl请求真正下载地址列表、文件大小和文件名
			if (StringUtil.isEmpty(mediaItem.getUrl())) {
				Toast.makeText(mContext, R.string.download_url_is_null, 0)
						.show();
				return;
			}

			DownloadEntity entity = new DownloadEntity();

			entity.setId(mediaItem.getId());
			entity.setUrl(mediaItem.getUrl());
			entity.setContentLength(mediaItem.getContentLength());
			entity.setName(mediaItem.getName());
			entity.setUserAgent(mediaItem.getUserAgent());
			entity.setMimetype(mediaItem.getMimetype());
			entity.setSrcPath(mediaItem.getSrcPath());
			entity.setContentDisposition(mediaItem.getContentDisposition());

			// entity.setPurl(mediaItem.getUrl());
			// entity.setHashid(mediaItem.getId());
			// entity.setMid(mediaItem.getId());
			//
			// entity.setMediatype(mediaItem.getMimetype());
			// entity.setMedianame(mediaItem.getName());
			// entity.setTaskname(mediaItem.getName());
			// // entity.setDisplayName(DownloadHelper.constructName(entity));
			// entity.setDisplayName(mediaItem.getName());

			popIfContinueDownloadDialog(entity, activity);
		}
	}

	public void download(DownloadEntity entity) {
		Intent intent = new Intent(mContext, DownloadService.class);
		intent.setAction(DownloadService.ACTION_ADD_TO_DOWNLOAD);
		intent.putExtra(DownloadService.EXTRA_MEDIAITEM_ENTRY, entity);
		mContext.startService(intent);
		if (!entity.isFromStart())
			ToastUtil.toastPrompt(mContext, R.string.movie_add_success, 0);
	}

	public DownloadProvider getProvider() {
		return mProvider;
	}

	public ArrayList<DownloadJob> getAllDownloads() {
		return mProvider.getAllDownloads();
	}

	public ArrayList<DownloadJob> getCompletedDownloads() {
		return mProvider.getCompletedDownloads();
	}

	public ArrayList<DownloadJob> getQueuedDownloads() {
		return mProvider.getQueuedDownloads();
	}

	public synchronized void deregisterDownloadObserver(
			DownloadObserver observer) {
		mObservers.remove(observer);
	}

	public synchronized void registerDownloadObserver(DownloadObserver observer) {
		mObservers.add(observer);
	}

	public synchronized void notifyObservers() {
		for (DownloadObserver observer : mObservers) {
			observer.onDownloadChanged(this);
		}
	}

	public void deleteDownload(DownloadJob job) {
		mProvider.removeDownload(job);

		job.notifyDownloadOnPause();

		ToastUtil.toastPrompt(mContext, R.string.delete_success, 0);
		// 删除播放历史记录
		if (job.getEntity().getId() != null)
			deleteHistoryDb(job.getEntity().getId(), job.getEntity().getId(),
					job);
	}

	private void deleteHistoryDb(final String mid, final String hashid,
			final DownloadJob job) {
		new Thread() {
			public void run() {
				removeDownloadFromDisk(job);

				if (playHistoryDao == null) {
					playHistoryDao = PlayHistoryDao.getInstance(mContext);
				}

				PlayHistoryInfo playHistoryInfo = playHistoryDao
						.findByHashid(mid);

				if (playHistoryInfo != null) {
					LogUtil.e("DGX", "deleteHistoryDb - historyId == "
							+ playHistoryInfo.getHashid() + " , " + hashid);
					String purl = playHistoryInfo.getPurl();
					if (purl != null && !purl.equals("")
							&& !purl.contains("jobsfe.funshion.com")
							&& !purl.contains("p.funshion.com")) {
						playHistoryDao.deleteHashId(hashid);

					}
				}
			};
		}.start();
	}

	private void removeDownloadFromDisk(DownloadJob job) {
		DownloadEntity Entity = job.getEntity();

		String path = DownloadHelper.getAbsolutePath(Entity,
				DownloadHelper.getDownloadPath());

		File file = new File(path);
		if (file.exists()) {
			file.delete();
		} else {
			LogUtil.i(TAG, "file is not exist!");
		}
	}

	// start next task
	public void statNextTask() {
		ArrayList<DownloadJob> queuedDownloads = mProvider.getQueuedDownloads();
		int num = getDownloadNum();
		if (downloading_num < num) {
			for (DownloadJob job : queuedDownloads) {
				if (!job.isUserPause()
						&& (job.getStatus() == DownloadJob.WAITING || job
								.getStatus() == DownloadJob.PAUSE)) {
					job.start();
				}
				if (downloading_num >= num)
					break;
			}
		}
	}

	public int getDownloadNum() {
		SharedPreferences bgDownloadSharePreference = mContext
				.getSharedPreferences("bg_download_config",
						Context.MODE_PRIVATE);
		String download_num = bgDownloadSharePreference.getString(
				"download_num", "3");
		return Integer.parseInt(download_num);
	}

	public boolean IsDownloadOnlyWifi() {
		SharedPreferences bgDownloadSharePreference = mContext
				.getSharedPreferences(
						DownloadSetActivity.STATE_SHAREPREFERENCE,
						Context.MODE_PRIVATE);
		Boolean isDownloadOnlyWifi = bgDownloadSharePreference.getBoolean(
				DownloadSetActivity.IS_DOWNLOAD_ONLY_WIFI, true);
		return isDownloadOnlyWifi;
	}

	private void popIfContinueDownloadDialog(DownloadEntity entity,
			Context activity) {
		if (NetworkUtil.reportNetType(mContext) == 2 && !IsDownloadOnlyWifi()) {
			checkIfContinueDownloadDialog(entity, activity);
		} else {
			download(entity);
		}

	}

	private void checkIfContinueDownloadDialog(final DownloadEntity entity,
			Context activity) {
		Builder customBuilder = new Builder(activity);
		customBuilder
				.setTitle(R.string.tip)
				.setMessage(R.string.wireless_tip)
				.setPositiveButton(R.string.continue_download,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								download(entity);
								dialog.dismiss();
							}
						})
				.setNegativeButton(R.string.pause_download,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								entity.setUserPauseWhen3G(true);
								entity.setDownloaded(DownloadJob.PAUSE);
								download(entity);
								dialog.dismiss();
							}
						}).setOnKeyListener(new OnKeyListener() {
					@Override
					public boolean onKey(DialogInterface dialog, int keyCode,
							KeyEvent event) {
						if (keyCode == KeyEvent.KEYCODE_BACK
								&& event.getRepeatCount() == 0) {
							entity.setUserPauseWhen3G(true);
							download(entity);
							dialog.dismiss();
						}
						return false;
					}
				});
		Dialog dialog = customBuilder.create();
		dialog.show();
	}

	private Context context;
	private String path;
	private String size;
	private double percent;
	private DownloadInfo downInfo;
	private PlayData data;
	private PlayHistoryInfo historyInfo;
	private boolean isSendLocalHistory;

	// 播放影片 同时处理播放历史相关的数据 add DownloadInfo downInfo 
	public boolean playVideo(Context context, String path, String name,
			String size, double percent, DownloadInfo downInfo) {
		if (context != null) {
			PlayData data = new PlayData();
			data.setUrl(path);
			data.setName(name);
			data.setSize(size);
			data.setWatchablePercent(percent);
			data.setLocalFile(true);
			getData(context, path, size, percent, downInfo, data);
			setHistoryInfo(downInfo, historyInfo, path, size, percent);
			startPlayer();
			return true;
		}

		return false;
	}

	private void getData(Context mcontext, String mpath, String msize,
			Double mpercent, DownloadInfo mdownInfo, PlayData mdata) {
		context = mcontext;
		path = mpath;
		size = msize;
		percent = mpercent;
		downInfo = mdownInfo;
		data = mdata;
		// 创建播放历史Dao
		PlayHistoryDao localPlayDao = PlayHistoryDao.getInstance(context);
		if (downInfo.getMid() == null || "".equals(downInfo.getMid())) {
			isSendLocalHistory = false;
		} else {
			isSendLocalHistory = true;
		}
		// 通过mid获取播放历史数据库中的数据
		historyInfo = localPlayDao.findByHashid(downInfo.getMid());
		// 如果当前的类型为电影
		// 判断之前是否有过播放历史
		if (historyInfo == null) {
			historyInfo = new PlayHistoryInfo();
		} else {
			if (!historyInfo.getHashid().equals(downInfo.getHashId())) {
				historyInfo.setPosition(0);
			}
		}
	}

	cn.hi321.browser.media.entity.MediaItem mVideoInfo = null;

	private void startPlayer() {
		Intent intent = null;
		if (historyInfo == null)
			return;

		if (historyInfo != null && historyInfo.getMediatype() != null
				&& historyInfo.getMediatype().toLowerCase().contains("video")) {
			if (mVideoInfo != null) {
				mVideoInfo.setUrl(historyInfo.getPurl());
			} else {
				mVideoInfo = new cn.hi321.browser.media.entity.MediaItem();
				mVideoInfo.setUrl(historyInfo.getPurl());
			}

			try {
				if (historyInfo != null
						&& historyInfo.getMedianame().trim().length() > 0)
					mVideoInfo.setTitle(historyInfo.getMedianame());
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (historyInfo.getPurl().contains("m3u8")) {
				mVideoInfo.setLive(true);
			} else {
				mVideoInfo.setLive(false);
			}

			intent = new Intent(context, SystemPlayer.class);
			Bundle mBundle = new Bundle();
			mBundle.putSerializable("VideoInfo", mVideoInfo);
			intent.putExtras(mBundle);
			LogUtil.i(TAG, "startActivity");

		} else {
			intent = new Intent(Intent.ACTION_VIEW);
			intent.setDataAndType(Uri.parse(historyInfo.getPurl()),
					historyInfo.getMediatype());
		}

		context.startActivity(intent);
	}

	/**
	 * add by yanggf 将本地播放的相关信息放入播放历史中
	 * 
	 * @param downloadInfo
	 *            下载信息
	 * @param playHistoryInfo
	 *            播放历史信息
	 */
	public static void setHistoryInfo(DownloadInfo downloadInfo,
			PlayHistoryInfo playHistoryInfo, String path, String size,
			double percent) {
		if (downloadInfo != null && playHistoryInfo != null) {
			// 设置媒体ID
			playHistoryInfo.setMid(downloadInfo.getMid());
			LogUtil.v(TAG, "媒体ID" + downloadInfo.getMid());
			// 设置媒体名称
			playHistoryInfo.setMedianame(downloadInfo.getMediaName());
			LogUtil.v(TAG, "媒体名称" + downloadInfo.getMediaName());
			// 设置HashId
			playHistoryInfo.setHashid(downloadInfo.getHashId());
			LogUtil.v(TAG, "HashId" + downloadInfo.getHashId());
			// 设置任务名称
			playHistoryInfo.setTaskname(downloadInfo.getTaskName());
			LogUtil.v(TAG, "任务名称" + downloadInfo.getTaskName());
			// 设置媒体类型
			playHistoryInfo.setMediatype(downloadInfo.getmType());
			LogUtil.v(TAG, "媒体类型" + downloadInfo.getmType());
			// 设置播放路径 Local
			playHistoryInfo.setPurl(path);
			LogUtil.v(TAG, "播放路径" + path);
			playHistoryInfo.setSize(size);
			LogUtil.v(TAG, "文件大小" + size);
			playHistoryInfo.setPercent("" + percent);
			LogUtil.v(TAG, "文件下载百分比" + percent);
		}
	}

	// 过滤特殊字符
	public String StringFilter(String str) throws PatternSyntaxException {
		String regEx = "[`~!@#$%^&*()+=|{}':;',//[//].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		return m.replaceAll("").trim();
	}

}
