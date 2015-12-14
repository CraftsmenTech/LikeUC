package cn.hi321.browser.download;

import java.util.HashMap;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;
import cn.hi321.browser.BrowserApp;
import cn.hi321.browser.db.PlayHistoryDao;
import cn.hi321.browser.model.DownloadInfo;
import cn.hi321.browser.model.PlayData;
import cn.hi321.browser.model.PlayHistoryInfo;
import cn.hi321.browser.utils.DownloadUtils;
import cn.hi321.browser.utils.NetworkUtil;
import cn.hi321.browser.utils.Utils;
import cn.hi321.browser2.R;


public class DownloadService extends Service {

	public static final String ACTION_ADD_TO_DOWNLOAD = "add_to_download";

	public static final String EXTRA_MEDIAITEM_ENTRY = "mediaItem";

	private NotificationManager mNotificationManager = null;

	private DownloadProvider mDownloadProvider;
	private static final int DOWNLOAD_NOTIFY_ID = 667668;
	private Notification n;
	private HashMap<Integer, Notification> notifications;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		mNotificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
		if(notifications!=null){
			for(Integer integer :notifications.keySet()){
				mNotificationManager.cancel(integer);
				stopSelf();
			}
		}
		mNotificationManager.cancelAll();
		mDownloadProvider = BrowserApp.getInstance()
				.getDownloadManager().getProvider();
		super.onCreate();
		
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		if (intent != null) {

			String action = intent.getAction();

			if (action.equals(ACTION_ADD_TO_DOWNLOAD)) {
				DownloadEntity entry = (DownloadEntity) intent
						.getSerializableExtra(EXTRA_MEDIAITEM_ENTRY);
				addToDownloadQueue(entry, startId);
			}
		}

		return super.onStartCommand(intent, flags, startId);
	}

	public void addToDownloadQueue(DownloadEntity entry, int startId) {

		String downloadPath = DownloadHelper.getDownloadPath();
		DownloadJob downloadJob = new DownloadJob(entry, downloadPath, startId);
		if (mDownloadProvider.queueDownload(downloadJob)) {
			downloadJob.setListener(mDownloadJobListener);
			if(!entry.isFromStart())
				downloadJob.notifyDownloadStarted();
			if(downloadJob.getStatus()!=DownloadJob.PAUSE){
				downloadJob.start();
			}else{		
				downloadJob.setUserPause(true);
			}
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	private DownloadJobListener mDownloadJobListener = new DownloadJobListener() {

		@Override
		public void downloadEnded(DownloadJob job) {
			Toast.makeText(
					DownloadService.this,
					job.getEntity().getName()+ " 已下载完成",
					Toast.LENGTH_SHORT).show();
			mDownloadProvider.downloadCompleted(job);
			int icon = R.drawable.notification_start;
			int contentTitle = R.string.download_complete;
			String notificationMessage = job.getEntity().getName();
			displayNotifcationOnCompleted(icon, contentTitle,
					notificationMessage, job);
			if (DownloadActivity.mSizeManager != null) {
				DownloadActivity.mSizeManager.ansynHandlerSdcardSize();
			}
//			updateTipHandler.post(downloadRunable);
		}

		@Override
		public void downloadOnDownloading(DownloadJob job) {
			if (notifications == null)
				notifications = new HashMap<Integer, Notification>();
			Notification n = new Notification();
			notifications.put(job.getNotifyIdByHashId(), n);
			n.icon = R.drawable.notification_app;
			n.flags = Notification.FLAG_NO_CLEAR;
			Intent pIntent = new Intent(DownloadService.this,
					DownloadActivity.class);	
			pIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
					| Intent.FLAG_ACTIVITY_NEW_TASK);
			PendingIntent contentIntent = PendingIntent.getActivity(
					DownloadService.this, R.string.app_name, pIntent,
					PendingIntent.FLAG_UPDATE_CURRENT);
			n.contentIntent = contentIntent;
			RemoteViews contentView = new RemoteViews(getPackageName(),
					R.layout.item_download_notify);

			DownloadEntity entity = job.getEntity();
			String mediaName = entity.getName();
			String taskName = entity.getName();
			if (mediaName != null && taskName != null
					&& mediaName.equals(taskName)) {
				taskName = "";
			}
			contentView
					.setTextViewText(R.id.lblVideoName, mediaName);
			n.contentView = contentView;
			n.contentView.setViewVisibility(R.id.tv_downCount, View.GONE);
			n.contentView.setTextViewText(R.id.tv_already_download,
					DownloadUtils.getDownloadedSize(job.getDownloadedSize())+ "M");
			n.contentView.setTextViewText(R.id.tv_download_speed,
					getString(R.string.down_state_start));
			n.contentView.setTextViewText(R.id.tv_total_download,
					"/" + DownloadUtils.getTotalSize(job.getmTotalSize())+ "MB - ");
			n.contentView.setProgressBar(R.id.ProgressBarDown, 100,
					job.getProgress(), false);
			if(NetworkUtil.isNetworkAvailable(DownloadService.this)||job.getEntity().getStatus()!= DownloadJob.PAUSE)
				mNotificationManager.notify(job.getNotifyIdByHashId(), n);
		}

		@Override
		public void downloadOnPause(DownloadJob job) {
			if(job == null||notifications==null)
				return;
			notifications.remove(job.getNotifyIdByHashId());
			mNotificationManager.cancel(job.getNotifyIdByHashId());
			mNotificationManager.cancel((int) job.getmTotalSize()); // 删除下载完成的任务时，清除相应的下载完成通知提示
		}

		@Override
		public void downloadStarted(DownloadJob job) {
			int icon = R.drawable.notification_app;
			int contentTitle = R.string.app_name;
			String mediaName = job.getEntity().getName();
			String taskName = job.getEntity().getName();
			if (mediaName != null && taskName != null
					&& mediaName.equals(taskName)) {
				taskName = "";
			}

			String tickerText = mediaName ;
			String notificationMessage = tickerText
					+ getString(R.string.add_download_notification);
			displayNotifcation(icon, contentTitle, notificationMessage);
		}

		@Override
		public void downloadPaused(DownloadJob job) {
			mNotificationManager.cancel(DOWNLOAD_NOTIFY_ID);

		}

		@Override
		public void updateNotifyOnDownloading(DownloadJob job) {
			try {
				Notification n = notifications.get(job.getNotifyIdByHashId());
				if(n==null)
					return;
				RemoteViews remoteView = new RemoteViews(getPackageName(),R.layout.item_download_notify);
				remoteView.setTextViewText(R.id.tv_already_download,DownloadUtils.getDownloadedSize(job.getDownloadedSize()) + "M");
				remoteView.setTextViewText(R.id.tv_total_download,"/" + DownloadUtils.getTotalSize(job.getmTotalSize())+ "MB - ");
				remoteView.setTextViewText(R.id.tv_download_speed,getString(R.string.down_state_start));
				remoteView.setProgressBar(R.id.ProgressBarDown, 100,job.getProgress(), false);
				n.contentView = remoteView;
				mNotificationManager.notify(job.getNotifyIdByHashId(), n);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	protected void displayNotifcation(int icon, int contentTitle,
			String notificationMessage) {
		Notification notification = new Notification(icon, notificationMessage,
				System.currentTimeMillis());

		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				new Intent(this, DownloadActivity.class), 0);

		notification.setLatestEventInfo(this, getString(contentTitle),
				notificationMessage, contentIntent);
		notification.flags |= Notification.FLAG_NO_CLEAR;
		mNotificationManager.notify(DOWNLOAD_NOTIFY_ID, notification);
		mNotificationManager.cancel(DOWNLOAD_NOTIFY_ID);
	}

	private void displayNotifcationOnCompleted(int icon, int contentTitle,
			String notificationMessage, DownloadJob job) {
		n = new Notification(icon, notificationMessage,
				System.currentTimeMillis());
		n.tickerText = notificationMessage
				+ getString(R.string.download_complete);
		n.contentView = new RemoteViews(getPackageName(),
				R.layout.item_download_notify);
		n.contentView.setImageViewResource(R.id.iv_notify_start,
				R.drawable.download_notify_complete);
		n.contentView.setViewVisibility(R.id.tv_already_download, View.GONE);
		n.contentView
				.setViewVisibility(R.id.tv_complete_download, View.VISIBLE);
		n.contentView.setViewVisibility(R.id.linearLayProcess, View.GONE);
		n.contentView.setViewVisibility(R.id.tv_download_speed, View.GONE);
		n.contentView.setViewVisibility(R.id.tv_total_download, View.GONE);
		n.contentView.setViewVisibility(R.id.tv_downCount, View.GONE);
		n.contentView.setTextViewText(R.id.lblVideoName, notificationMessage);
		n.contentView.setImageViewResource(R.id.iv_notify_start,
				R.drawable.download_notify_complete);
		n.flags = Notification.FLAG_AUTO_CANCEL;
		String path = "file://"
				+ DownloadHelper.getAbsolutePath(job.getEntity(),
						DownloadHelper.getDownloadPath());
		String name = null;
		if (job.getEntity().getName() != null) {
			name = job.getEntity().getName();
		} else {
			name = job.getEntity().getName();
		}
		String size = job.getmTotalSize() + "";
		double downPercent = (double) job.getDownloadedSize()
				/ job.getmTotalSize();
		int notifyId = (int) job.getmTotalSize();
		playFromNotify(DownloadService.this, path, name, size, downPercent,
				job, notifyId);
		mNotificationManager.notify(notifyId, n);

	}

	private void playFromNotify(Context context, String path, String name,
			String size, double downPercent, DownloadJob job, int notifyId) {
		PlayData data = new PlayData();
		data.setUrl(path);
		data.setName(name);
		data.setSize(size);
		data.setWatchablePercent(downPercent);
		data.setLocalFile(true);
		// 在下载完成的通知栏中进行本地播放历史的相关处理 
		DownloadInfo downloadInfo = new DownloadInfo();
		downloadInfo.setMid(job.getEntity().getId());
		downloadInfo.setmType(job.getEntity().getMimetype());
		String mediaName = job.getEntity().getName();
		String taskName = job.getEntity().getName();
		downloadInfo.setMediaName(mediaName);
		downloadInfo.setTaskName(taskName);
		downloadInfo.setHashId(job.getEntity().getId());
		PlayHistoryDao playHistoryDao = PlayHistoryDao.getInstance(context);
		// 通过Mid查找播放历史
		PlayHistoryInfo loclaPlayHistory = playHistoryDao
				.findByHashid(downloadInfo.getMid());
		if (loclaPlayHistory == null) {
			loclaPlayHistory = new PlayHistoryInfo();
		} else {
			if (!loclaPlayHistory.getHashid().equals(
					job.getEntity().getId())) {
				loclaPlayHistory.setPosition(0);
			}
		}
		// 将相关信息写入本地播放历史 
		DownloadManager.setHistoryInfo(downloadInfo, loclaPlayHistory, path,
				size, downPercent);
		Intent intent = new Intent(context, DownloadActivity.class);
		Bundle mBundle = new Bundle();
		mBundle.putSerializable(Utils.DOWNLOAD_KEY, data);
		// 将播放历史的相关信息发送给播放器 
		mBundle.putSerializable(Utils.PLAY_HISTORY_KEY, loclaPlayHistory);
		//add by 解决从通知栏播放完后不提示进入风行的问题
		Uri uriPath = Uri.parse(path);
		intent.setData(uriPath);
		intent.putExtras(mBundle);
		PendingIntent contentIntent = PendingIntent.getActivity(context,
				notifyId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		n.contentIntent = contentIntent;
	}

//	Runnable downloadRunable = new Runnable() {
//		@Override
//		public void run() {
//			if (mDownloadProvider != null) {
//				int downCount = mDownloadProvider.getQueuedDownloads().size();
//				MainActivity view = MainActivity.INSTANCE;
//				if (view != null) {
//					TextView tip = (TextView) view
//							.findViewById(R.id.new_download_tip);
//					if (downCount > 0) {
//						tip.setText("" + downCount);
//						tip.setVisibility(View.VISIBLE);
//					} else {
//						tip.setVisibility(View.INVISIBLE);
//					}
//				}
//			}
//		}
//	};

	Handler updateTipHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
		}
	};

}
