package cn.hi321.browser.download;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import cn.hi321.browser.BrowserApp;
import cn.hi321.browser.utils.NetworkUtil;

public class DownloadBroadcastReceiver extends BroadcastReceiver {

	public static final String NET_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";
	public static final String SPEED_ACTION = "com.funshion.video.cutdownloadspeed";
	private final static String DOWNLOADONLYWIFI = "com.funshion.video.DOWNLOADONLYWIFI";
	private final static String OFFLINECACHE = "com.funshion.video.OFFLINECACHE";

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		// 网络发生变化时
		if (NET_ACTION.equals(action)) {
			ConnectivityManager manager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo wifi = manager
					.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			// 当切换到只有移动网络，只有当非只在wifi下才自动开启下载
			if (wifi.isConnected()) {
				for (DownloadJob job : BrowserApp.getInstance()
						.getDownloadManager().getQueuedDownloads()) {
					if (!job.isUserPause()
							&& (job.getStatus() == DownloadJob.PAUSE || job
									.getStatus() == DownloadJob.WAITING))
						job.start();
				}
			}
			

		}

		if (SPEED_ACTION.equals(action)) {
			boolean cutSpeed = intent.getBooleanExtra("player", false);
			if (cutSpeed) {
				int dn = BrowserApp.getInstance().getDownloadManager().downloading_num;
				for (DownloadJob job : BrowserApp.getInstance()
						.getDownloadManager().getQueuedDownloads()) {
					if (job.getStatus() == DownloadJob.DOWNLOADING) {
						job.dn = dn;
						job.pauseOnSpeed();
					}
					BrowserApp.getInstance().getDownloadManager()
							.notifyObservers();
				}
			} else {
				for (DownloadJob job : BrowserApp.getInstance()
						.getDownloadManager().getQueuedDownloads()) {
					if (job.getStatus() == DownloadJob.PAUSEONSPEED)
						job.start();
				}
			}
		}

		if (DOWNLOADONLYWIFI.equals(action)) {
			boolean isDownloadOnlyWifi = intent.getBooleanExtra(
					"isDownloadOnlyWifi", true);
			// 当非移动网络时
			if (!(NetworkUtil.reportNetType(BrowserApp.getInstance()) == 2))
				return;
			if (isDownloadOnlyWifi) {
				for (DownloadJob job : BrowserApp.getInstance()
						.getDownloadManager().getQueuedDownloads()) {
					if (job.getStatus() == DownloadJob.DOWNLOADING)
						job.pauseOnSetWifiChange();
				}
			} else {
				for (DownloadJob job : BrowserApp.getInstance()
						.getDownloadManager().getQueuedDownloads()) {
					if (job.getStatus() == DownloadJob.PAUSE) {
						job.getEntity().setUserPauseWhen3G(false);
						job.start();
					}
				}
			}
		}

		if (OFFLINECACHE.equals(action)) {
			boolean isDownload = intent.getBooleanExtra("download", true);
			if (isDownload) {
				for (DownloadJob job : BrowserApp.getInstance()
						.getDownloadManager().getQueuedDownloads()) {
					if (job.getStatus() == DownloadJob.PAUSEONOFFLINECACHE) {
						job.start();
					}
				}
			} else {
				for (DownloadJob job : BrowserApp.getInstance()
						.getDownloadManager().getQueuedDownloads()) {
					if (job.getStatus() == DownloadJob.DOWNLOADING) {
						job.pauseOnOfflineCache();
					}
				}
			}
		}

	}

}
