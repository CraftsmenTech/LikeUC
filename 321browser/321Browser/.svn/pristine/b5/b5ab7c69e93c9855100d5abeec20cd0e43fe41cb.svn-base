package cn.hi321.browser.download;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import cn.hi321.browser.BrowserApp;
import cn.hi321.browser.db.PlayHistoryDao;
import cn.hi321.browser.model.PlayHistoryInfo;
import cn.hi321.browser.ui.activities.HomeActivity;
import cn.hi321.browser.utils.NetworkUtil;
import cn.hi321.browser.utils.StringUtil;
import cn.hi321.browser.utils.Utils;

public class DownloadTask extends AsyncTask<Void, Integer, Boolean> {
	private final String TAG = "DownloadTask";

	DownloadJob mJob;
	int mExceptionType = 0;
	private double tm = System.currentTimeMillis(); // 该任务本次非中断整个生命周期方法调用时间，用于计算一次不中断下载耗时
	private int ev = -1; // ev : 0-完成下载； 1-退出下载；2-系统异常
	private long downlaodSize = 0;
	private int dn = 1;

	public DownloadTask(DownloadJob job) {
		mJob = job;
	}

	@Override
	protected void onPreExecute() {
		tm = System.currentTimeMillis();
		downlaodSize = mJob.getDownloadedSize();
		dn = BrowserApp.getInstance().getDownloadManager().downloading_num;
		mJob.notifyDownloadOnDownloading();
		super.onPreExecute();
	}

	@Override
	protected void onPostExecute(Boolean result) {
		tm = (System.currentTimeMillis() - tm) / 1000;
		downlaodSize = mJob.getDownloadedSize() - downlaodSize;
		dn = BrowserApp.getInstance().getDownloadManager().downloading_num;
		if (result) {
			updatePlayHistoryAtDownloadEnd(mJob);
			mJob.setStatus(DownloadJob.COMPLETE);
			mJob.notifyDownloadOnPause();
			mJob.notifyDownloadEnded();
			ev = 0;
		} else {
			BrowserApp.getInstance().getDownloadManager().downloading_num--;
			if (isCanReTry() && (mExceptionType == 1 || mExceptionType == 0)) {
				mJob.retryNum++;
				mJob.start();
			} else {
				mJob.retryNum = 0;
				mJob.setStatus(DownloadJob.PAUSE);
				mJob.setUserPause(false);
				mJob.notifyDownloadOnPause();
				if (!NetworkUtil.isNetworkAvailable(BrowserApp.getInstance())) {
					mExceptionType = 4; // 2：无网络
				}
				mJob.setmExceptionType(mExceptionType);
				ev = 2;
			}
			BrowserApp.getInstance().getDownloadManager().notifyObservers();
		}

		super.onPostExecute(result);
	}

	private boolean isCanReTry() {
		switch (NetworkUtil.reportNetType(BrowserApp.getInstance())) {
		case 1:
			return mJob.retryNum < 10 ? true : false;
		case 2:
			if (mJob.getEntity().isUserPauseWhen3G())
				return false;
			return !mJob.isDownloadOnlyWifi();
		default:
			return false;
		}
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		try {
			return downloadToFile(mJob);
		} catch (SocketTimeoutException e) {
			mExceptionType = 1; // 1：连接超时异常
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			if (e.toString().contains("No space left on device")) {
				mExceptionType = 2; // 2：sdcard 满
			}
			if (e.toString().contains("java.io.FileNotFoundException")) {
				mExceptionType = 3; // 2：没有sdcard，或者sdcard拔出或者存储器模式
			}
			e.printStackTrace();
			return false;
		}
	}

	@Override
	protected void onCancelled() {
		if (mJob.getStatus() != DownloadJob.PAUSEONOFFLINECACHE)
			mJob.notifyDownloadOnPause();
		BrowserApp.getInstance().getDownloadManager().notifyObservers();
		ev = 1;
		tm = (System.currentTimeMillis() - tm) / 1000;
		downlaodSize = mJob.getDownloadedSize() - downlaodSize;
		dn = mJob.dn;
		super.onCancelled();
	}

	/**
	 * 支持断点下载
	 * 
	 * @param job
	 * @return
	 */

	private Boolean downloadToFile(DownloadJob job) throws IOException {
		// 初始化：
		if (!initDownload()) {
			return false;
		}

		DownloadEntity downloadEntity = job.getEntity();

		String path = job.getDestination();
		String fileName = downloadEntity.getName()/* +".mp4" */;
		// 构建下载目录
		try {
			File file = new File(path);
			if (!file.exists())
				file.mkdirs();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		HttpParams params = new BasicHttpParams();
		// HttpConnectionParams.setStaleCheckingEnabled(params, false);
		HttpConnectionParams.setConnectionTimeout(params, 5 * 1000);
		HttpConnectionParams.setSoTimeout(params, 5 * 1000);
		HttpConnectionParams.setSocketBufferSize(params, 8192 * 5);
		HttpGet httpGet = new HttpGet(downloadEntity.getUrl());

		File file = new File(path, fileName);
		RandomAccessFile randomFile = new RandomAccessFile(file, "rw");
		job.setDownloadedSize(randomFile.length());

		job.setProgress(job.initProgress());
		if (job.getProgress() == 100)
			return true;

		httpGet.addHeader("Range", "bytes=" + randomFile.length() + "-");
		HttpResponse response = new DefaultHttpClient(params).execute(httpGet);
		HttpEntity entity = response.getEntity();

		StatusLine statusLine = response.getStatusLine();
		int respCode = statusLine.getStatusCode();
		Log.i("XJ", "---Http ResponeCode---" + respCode);
		if (HttpURLConnection.HTTP_PARTIAL != respCode)
			return false;

		long length = entity.getContentLength();

		// if(length == 0 || length > 1){
		long totalSize = randomFile.length() + length;
		// if(Math.abs(job.getmTotalSize()-totalSize) < 5000)
		job.setmTotalSize(totalSize);
		// }

		InputStream in = entity.getContent();
		// InputStream in = c.getInputStream();
		if (in == null) {
			return false;
		}

		randomFile.seek(randomFile.length());

		byte[] buffer = new byte[1024];
		int lenght = 0;
		tm = System.currentTimeMillis();
		downlaodSize = job.getDownloadedSize();
		mJob.retryNum = 0;
		while ((lenght = in.read(buffer)) > 0 && !isCancelled()) {
			randomFile.write(buffer, 0, lenght);
			job.setDownloadedSize((job.getDownloadedSize() + lenght));
			job.setRate();
		}
		randomFile.close();
		httpGet.abort();
		// close(in);
		// in.close();

		if (job.getDownloadedSize() < job.getmTotalSize()) { // 解决有时没下载完就返回true，显示下载完成
			mExceptionType = 5;// 网络异常
			return false;
		}
		return true;

	}

	private Boolean downloadFileByAndroidGet(DownloadJob job)
			throws IOException {
		final HttpParams params = new BasicHttpParams();
		HttpConnectionParams.setStaleCheckingEnabled(params, false);
		HttpConnectionParams.setConnectionTimeout(params, 5 * 1000);
		HttpConnectionParams.setSoTimeout(params, 5 * 1000);
		HttpConnectionParams.setSocketBufferSize(params, 8192 * 5);
		HttpGet httpGet = new HttpGet(job.getEntity().getUrl());

		DownloadEntity downloadEntity = job.getEntity();
		String fileName = downloadEntity.getName();
		String path = job.getDestination();
		// 构建下载目录
		try {
			File file = new File(path);
			if (!file.exists())
				file.mkdirs();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		File file = new File(path, fileName);

		RandomAccessFile randomFile = new RandomAccessFile(file, "rw");
		httpGet.addHeader("Range", "bytes=" + randomFile.length() + "-");
		HttpResponse response = new DefaultHttpClient(params).execute(httpGet);
		HttpEntity entity = response.getEntity();
		long length = entity.getContentLength();

		if (job.getProgress() == 100)
			return true;

		if (length == 0 || length > 1) {
			long totalSize = randomFile.length() + length;
			if (Math.abs(job.getmTotalSize() - totalSize) < 5000)
				job.setmTotalSize(totalSize);
		}
		job.setDownloadedSize(randomFile.length());

		InputStream in = entity.getContent();
		if (in == null) {
			return false;
		}

		randomFile.seek(randomFile.length());

		byte[] buffer = new byte[1024];
		int lenght = 0;
		tm = System.currentTimeMillis();
		downlaodSize = job.getDownloadedSize();
		mJob.retryNum = 0;
		while ((lenght = in.read(buffer)) > 0 && !isCancelled()) {
			randomFile.write(buffer, 0, lenght);
			job.setDownloadedSize((job.getDownloadedSize() + lenght));
			job.setRate();
		}
		randomFile.close();
		in.close();
		if (job.getDownloadedSize() < job.getmTotalSize()) { // 解决有时没下载完就返回true，显示下载完成
			mExceptionType = 5;// 网络异常
			return false;
		}
		return true;

	}

	private boolean initDownload() {
		// 3g网络时默认不开启下载，是否允许下载查看设置
		if (NetworkUtil.reportNetType(BrowserApp.getInstance()) != 1
				&& mJob.isDownloadOnlyWifi()
				|| mJob.getEntity().isUserPauseWhen3G()) {
			return false;
		}
		return true;
	}

	private void updatePlayHistoryAtDownloadEnd(final DownloadJob downloadJob) {
		new Thread() {
			@Override
			public void run() {
				super.run();
				DownloadEntity downEntity = downloadJob.getEntity();
				String mid = downEntity.getId();
				if (null != HomeActivity.INSTANCE && !StringUtil.isEmpty(mid)) {
					// 当影片下载完成时，如果当前播放历史中播过该影片则将该影片的可播放百分比置为1
					PlayHistoryDao playHistoryDao = PlayHistoryDao
							.getInstance(HomeActivity.INSTANCE);
					if (null != playHistoryDao) {
						PlayHistoryInfo playHistoryInfo = playHistoryDao
								.findByHashid(mid);
						if (null != playHistoryInfo
								&& !StringUtil.isEmpty(playHistoryInfo
										.getPercent())) {
							// 将可播放百分比设置为100%
							playHistoryDao.updateByHashid(mid, "1");
						}
					}
				}
			}
		}.start();
	}

}
