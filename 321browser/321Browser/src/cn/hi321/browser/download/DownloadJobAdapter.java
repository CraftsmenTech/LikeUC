package cn.hi321.browser.download;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.hi321.browser.BrowserApp;
import cn.hi321.browser.model.DownloadInfo;
import cn.hi321.browser.utils.DialogUtil;
import cn.hi321.browser.utils.DownloadUtils;
import cn.hi321.browser.utils.NetworkUtil;
import cn.hi321.browser2.R;

public class DownloadJobAdapter extends ArrayListAdapter<DownloadJob> {
	private static final String TAG = "DownloadJobAdapter";

	private boolean deleteState = false;
	private DownloadManager downloadManager;
	private Context dContext;

	public DownloadJobAdapter(Activity context) {
		super(context);
		dContext = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		ViewHolder holder;

		if (row == null) {
			LayoutInflater inflater = mContext.getLayoutInflater();
			row = inflater.inflate(R.layout.download_row_old, null);

			holder = new ViewHolder();
			holder.downloadName = (TextView) row
					.findViewById(R.id.downloadName);
			holder.downloadLength = (TextView) row
					.findViewById(R.id.downloadLength);
			holder.progressText = (TextView) row.findViewById(R.id.rowProgress);
			holder.progressBar = (ProgressBar) row
					.findViewById(R.id.ProgressBar);
			holder.rlDownLayout = (RelativeLayout) row
					.findViewById(R.id.videolayout_new);

			holder.downloadControl = (Button) row
					.findViewById(R.id.downloadControl);
			holder.btnDelete = (Button) row.findViewById(R.id.btnDelete);
			holder.btnPlay = (Button) row.findViewById(R.id.btnPlay);
			row.setTag(holder);
		} else {
			holder = (ViewHolder) row.getTag();
		}

		DownloadJob job = mList.get(position);

		TouchListener tl = new TouchListener(job);

		DownloadEntity entity = job.getEntity();
		holder.downloadName.setText(entity.getName());
		holder.downloadLength.setText(DownloadUtils.getDownloadedSize(job
				.getDownloadedSize())
				+ "M / "
				+ DownloadUtils.getTotalSize(job.getmTotalSize()) + "M");

		if (mList.get(position).getProgress() == 100) {
			downloadJobCompleted(holder, tl, entity);
		} else {
			downloadJobUnCompleted(holder, job, tl);

			switch (job.getStatus()) {
			case DownloadJob.INIT:
				break;
			case DownloadJob.DOWNLOADING:
				holder.downloadControl
						.setBackgroundResource(R.drawable.download_pausebtn_selector);
				break;
			case DownloadJob.PAUSE:
				holder.progressText.setText("暂停中...");
				showExceptionPause(holder, job);
				holder.downloadControl
						.setBackgroundResource(R.drawable.download_controlbtn_selector);
				break;
			case DownloadJob.WAITING:
				holder.progressText.setText("等待中...");
				holder.downloadControl
						.setBackgroundResource(R.drawable.download_pausebtn_selector);
				if (job.getProgress() == 0)
					holder.downloadLength.setText("0M / "
							+ DownloadUtils.getTotalSize(job.getmTotalSize())
							+ "M");
				break;
			default:
				break;
			}
			holder.downloadControl
					.setOnClickListener(new DownloadControlListener(job, holder));
		}
		return row;
	}

	// 下载未完成的页面显示与处理
	private void downloadJobUnCompleted(ViewHolder holder, DownloadJob job,
			TouchListener tl) {
		holder.progressText.setVisibility(View.VISIBLE);
		holder.downloadControl.setVisibility(View.VISIBLE);
		holder.progressBar.setVisibility(View.VISIBLE);
		holder.progressBar.setMax(100);
		holder.progressBar.setProgress(job.getProgress());
		holder.progressText.setText(job.getRate());
		holder.btnPlay.setVisibility(View.GONE);
		if (deleteState) {
			holder.btnDelete.setVisibility(View.VISIBLE);
			holder.downloadControl.setVisibility(View.GONE);
			holder.rlDownLayout
					.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
		} else {
			holder.btnDelete.setVisibility(View.GONE);
			holder.downloadControl.setVisibility(View.VISIBLE);
			holder.rlDownLayout
					.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
		}
		holder.btnDelete.setOnTouchListener(tl);
		holder.btnPlay.setOnTouchListener(tl);
	}

	// 下载完成的页面显示与处理
	private void downloadJobCompleted(ViewHolder holder, TouchListener tl,
			DownloadEntity entity) {
		holder.progressBar.setVisibility(View.GONE);
		holder.progressText.setVisibility(View.GONE);
		if (DownloadHelper.getDownloadedFileSize(entity) == 0) {
			holder.downloadLength.setText("已完成" + " / " + "该文件可能被人为删除或移动");
			holder.btnPlay.setVisibility(View.GONE);
			holder.btnDelete.setVisibility(View.VISIBLE);
			holder.btnDelete.setOnTouchListener(tl);
		} else {
			holder.downloadLength.setText("已完成"
					+ " / "
					+ DownloadUtils.getTotalSize(DownloadHelper
							.getDownloadedFileSize(entity)) + "M");
			holder.downloadControl.setVisibility(View.GONE);
			if (deleteState) {
				holder.btnDelete.setVisibility(View.VISIBLE);
				holder.btnPlay.setVisibility(View.GONE);
				holder.rlDownLayout
						.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
			} else {
				// 视频
				if (entity != null && entity.getMimetype() != null
						&& entity.getMimetype().toLowerCase().contains("video")) {
					holder.btnDelete.setVisibility(View.GONE);
					holder.btnPlay.setVisibility(View.VISIBLE);
					holder.btnPlay
							.setBackgroundResource(R.drawable.download_playbtn_selector);
					holder.rlDownLayout
							.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);

				}
				// apk
				else if (entity != null
						&& entity.getMimetype() != null
						&& entity.getMimetype().toLowerCase()
								.contains("vnd.android.package-archive")) {
					holder.btnDelete.setVisibility(View.GONE);
					holder.btnPlay.setVisibility(View.VISIBLE);
					holder.btnPlay
							.setBackgroundResource(R.drawable.btn_anzhuang);
					holder.rlDownLayout
							.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);

				} else {

					// 无法识别下载的东西
					holder.btnDelete.setVisibility(View.GONE);
					holder.btnPlay.setVisibility(View.VISIBLE);
					holder.btnPlay.setBackgroundResource(R.drawable.btn_open);
					holder.rlDownLayout
							.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
				}

			}
			holder.btnDelete.setOnTouchListener(tl);
			holder.btnPlay.setOnTouchListener(tl);
		}
	}

	private void showExceptionPause(final ViewHolder holder,
			final DownloadJob job) {
		if (job.getmExceptionType() == 4)
			holder.progressText.setText(R.string.net_shutdown);
		if (job.getmExceptionType() == 3)
			holder.progressText.setText(R.string.no_sdcard_added);
		if (job.getmExceptionType() == 2)
			holder.progressText.setText(R.string.no_space_tip);
	}

	private class DownloadControlListener implements OnClickListener {
		private final DownloadJob job;
		private final ViewHolder holder;

		private DownloadControlListener(DownloadJob job, ViewHolder holder) {
			this.job = job;
			this.holder = holder;
		}

		@Override
		public void onClick(View v) {

			switch (job.getStatus()) {
			case DownloadJob.PAUSE:
				popIfContinueDownloadDialog();
				break;
			case DownloadJob.WAITING:
				holder.downloadControl
						.setBackgroundResource(R.drawable.download_controlbtn_selector);
				job.cancel();
				holder.progressText.setText("暂停中...");
				break;
			case DownloadJob.DOWNLOADING:
				holder.downloadControl
						.setBackgroundResource(R.drawable.download_controlbtn_selector);
				job.pause();
				holder.progressText.setText("暂停中...");
				break;
			}
		}

		private void popIfContinueDownloadDialog() {
			if (NetworkUtil.reportNetType(dContext) == 2
					&& !job.isDownloadOnlyWifi()) {
				checkIfContinueDownloadDialog();
			} else {
				start();
			}
		}

		private void checkIfContinueDownloadDialog() {
			Builder customBuilder = new Builder(dContext);
			customBuilder
					.setTitle(R.string.tip)
					.setMessage(R.string.wireless_tip)
					.setPositiveButton(R.string.continue_download,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									job.getEntity().setUserPauseWhen3G(false);
									start();
									dialog.dismiss();
								}
							})
					.setNegativeButton(R.string.pause_download,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									job.getEntity().setUserPauseWhen3G(true);
									start();
									dialog.dismiss();
								}
							}).setOnKeyListener(new OnKeyListener() {
						@Override
						public boolean onKey(DialogInterface dialog,
								int keyCode, KeyEvent event) {
							if (keyCode == KeyEvent.KEYCODE_BACK
									&& event.getRepeatCount() == 0) {
								job.getEntity().setUserPauseWhen3G(true);
								start();
								dialog.dismiss();
							}
							return false;
						}
					});
			Dialog dialog = customBuilder.create();
			dialog.show();
		}

		private void start() {
			holder.downloadControl
					.setBackgroundResource(R.drawable.download_pausebtn_selector);
			job.setUserStart(true);
			job.setUserPause(false);
			// if(job.isSupportBreakPoint()){
			job.start();
			holder.progressText.setText("0.0kb/s");
			/*
			 * }else{ holder.progressText.setText("您的手机可能不支持断点下载"); }
			 */
		}
	}

	class TouchListener implements OnTouchListener {

		DownloadJob job;

		public TouchListener(DownloadJob job) {
			this.job = job;
		}

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			int id = v.getId();

			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				break;
			case MotionEvent.ACTION_UP:
				switch (id) {
				case R.id.btnPlay:

					// String path = "file://" +
					// DownloadHelper.getAbsolutePath(job.getEntity(),DownloadHelper.getDownloadPath());
					String path = "file://" + job.getEntity().getSrcPath();
					DownloadInfo downloadInfo = new DownloadInfo();

					// modify by donggx
					downloadInfo.setMid(job.getEntity().getId());
					downloadInfo.setmType(job.getEntity().getMimetype());
					downloadInfo.setHashId(job.getEntity().getId());
					String mediaName = job.getEntity().getName();
					String taskName = job.getEntity().getName();
					downloadInfo.setMediaName(mediaName);
					downloadInfo.setTaskName(taskName);
					downloadManager = BrowserApp.getInstance()
							.getDownloadManager();
					// double watchableTime =
					// job.getDownloadedSize()*1.0/job.getmTotalSize();
					double watchableTime = job.getProgress() / 100.0;
					downloadManager.playVideo(dContext, path, job.getEntity()
							.getName(), job.getmTotalSize() + "",
							watchableTime, downloadInfo);
					break;
				case R.id.btnDelete:
					DialogUtil.setContext(mContext);
					DialogUtil.startWaitingDialog();
					BrowserApp.getInstance().getDownloadManager()
							.deleteDownload(job);
					break;
				}
				break;
			}
			return true; // 返回true或者false 确定了onclick和onlongClick的调用顺序
		}
	}

	static class ViewHolder {
		TextView downloadName; // 文件名,如xxxx第几集
		TextView downloadLength;// 大小：15M/88M
		TextView progressText;// 网速/下载中/暂停中/等待中/
		ProgressBar progressBar;// 进度条
		RelativeLayout rlDownLayout;// 整个一个item，用于奇偶变色
		Button downloadControl;// 最右边的控制按钮:下载/暂停
		Button btnDelete;// 删除按钮
		Button btnPlay;// 播放按钮
	}

	public boolean isDeleteState() {
		return deleteState;
	}

	public void setDeleteState(boolean deleteState) {
		this.deleteState = deleteState;
	}
}
