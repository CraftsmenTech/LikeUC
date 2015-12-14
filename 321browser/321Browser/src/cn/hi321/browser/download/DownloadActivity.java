package cn.hi321.browser.download;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;
import cn.hi321.browser.BrowserApp;
import cn.hi321.browser.model.DownloadInfo;
import cn.hi321.browser.ui.activities.DownloadSetActivity;
import cn.hi321.browser.ui.activities.HomeActivity;
import cn.hi321.browser.utils.DeviceInfoUtil;
import cn.hi321.browser.utils.DialogUtil;
import cn.hi321.browser.utils.LogUtil;
import cn.hi321.browser.utils.ToastUtil;
import cn.hi321.browser2.R;

import com.umeng.analytics.MobclickAgent;

public class DownloadActivity extends Activity implements DownloadObserver {
	private static final String TAG = "DownloadActivity";

	private final static int ACTIVITYSIZES = 1;
	private final static String LOCALSTART = "4";
	private Handler mHandler;

	private ListView mListView;
	private ViewFlipper mViewFlipper;
	// private GestureOverlayView mGestureOverlayView;
	private View sizeView;

	private Button edit_button;
	private Button set_button;

	private TextView title;
	private RelativeLayout titlebar;
	private ImageView left_button;

	private DownloadManager mDownloadManager;
	private DownloadManager downloadManager;
	public static ContainSizeManager mSizeManager;

	/**
	 * Runnable periodically querying DownloadService about downloads
	 */
	private Runnable mUpdateTimeTask = new Runnable() {
		public void run() {
			DialogUtil.closeWaitingDialog();
			updateListView();
		}
	};

	private TextView start_main;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		// ActivityHolder.getInstance().addActivity(this);
		setContentView(R.layout.download);
		initView();
		mDownloadManager = BrowserApp.getInstance().getDownloadManager();
		downloadManager = BrowserApp.getInstance().getDownloadManager();
		mSizeManager = new ContainSizeManager(this);
		mHandler = new Handler();
		mSizeManager.ansynHandlerSdcardSize();
		// bootStrapUpload();
	}

	public void initView() {
		InitCompentView();

		setOnClickCompentView();
		// 适配
		// initDownloadTitle();
	}

	@Override
	protected void onPause() {
		mHandler.removeCallbacks(mUpdateTimeTask);
		mDownloadManager.deregisterDownloadObserver(this);
		ToastUtil.cancelToast();
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onResume() {
		mDownloadManager.registerDownloadObserver(this);
		mDownloadManager.notifyObservers(); // 解决锁屏或者休眠过程中下载完成，解锁查看页面不刷新的问题；另一种解决办法是注释掉onPause()里的mDownloadManager.deregisterDownloadObserver(this);
		// mGestureOverlayView.setEnabled(false);
		super.onResume();
		 MobclickAgent.onResume(this);
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	private void updateListView() {
		ArrayList<DownloadJob> jobs = mDownloadManager.getAllDownloads();

		if (BrowserApp.getInstance().isDownlaodReStart()) {
			for (DownloadJob job : jobs) {
				DownloadEntity Entity = job.getEntity();
				String path = DownloadHelper.getAbsolutePath(Entity,
						DownloadHelper.getDownloadPath());

				File file = new File(path);
				if (file.exists()) {
					if (job.getDownloadedSize() == 0) {
						job.mDownloadedSize = DownloadHelper
								.getDownloadedFileSize(job.getEntity());
						job.setProgress(job.initProgress());

					}

					BrowserApp.getInstance().setDownloadReStart(false);
				} else {
					LogUtil.i(TAG, "file not exist in updateListView");
				}
			}
		}

		DownloadJobAdapter adapter = (DownloadJobAdapter) mListView
				.getAdapter();

		if (adapter != null && jobs != null
				&& jobs.size() == adapter.getCount()) {
			adapter.notifyDataSetChanged();
			return;
		} else {
			adapter = new DownloadJobAdapter(DownloadActivity.this);
			adapter.setList(jobs);
			mListView.setAdapter(adapter);
		}

		setupListView();

		if (edit_button.getText().equals(getString(R.string.down_btn_ok))) {
			adapter.setDeleteState(true);
		}
	}

	private void setupListView() {
		if (mListView.getCount() > 0) {
			mViewFlipper.setDisplayedChild(0);
		} else {
			sizeView.setVisibility(View.GONE);
			// edit_button.setVisibility(View.GONE);
			mViewFlipper.setDisplayedChild(1);
		}
	}

	private void InitCompentView() {
		mListView = (ListView) findViewById(R.id.DownloadListView);
		mViewFlipper = (ViewFlipper) findViewById(R.id.DownloadViewFlipper);
		// mGestureOverlayView =
		// (GestureOverlayView)findViewById(R.id.gestures);
		sizeView = (View) findViewById(R.id.sizeView);
		start_main = (TextView) findViewById(R.id.download_start_main);
		edit_button = (Button) findViewById(R.id.edit_button);
		set_button = (Button) findViewById(R.id.set_button);
		title = (TextView) findViewById(R.id.download_activity_title);
		titlebar = (RelativeLayout) findViewById(R.id.download_activity_titlebar);
		mListView = (ListView) findViewById(R.id.DownloadListView);
		left_button = (ImageView) findViewById(R.id.download_back);

	}

	private void setOnClickCompentView() {
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				DownloadListView(arg2);

			}
		});

		edit_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				onEditButton();

			}
		});
		set_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(DownloadActivity.this,
						DownloadSetActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.hold, R.anim.push_bottom_out);
			}
		});

		left_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				onBackButton();

			}
		});
		start_main.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(DownloadActivity.this,
						HomeActivity.class);
				startActivity(intent);

			}
		});
	}

	private void playNow(int position) {
		DownloadJob job = getJob(position);
		if (job.getProgress() >= 10) {
			String path = "file://" + job.getEntity().getSrcPath();
			// String path = "file://" +
			// DownloadHelper.getAbsolutePath(job.getEntity(),DownloadHelper.getDownloadPath());
			DownloadInfo downloadInfo = new DownloadInfo();

			DownloadEntity entity = job.getEntity();
			downloadInfo.setMid(entity.getId());
			downloadInfo.setmType(entity.getMimetype());
			downloadInfo.setMediaName(entity.getName());
			downloadInfo.setTaskName(entity.getName());
			downloadInfo.setHashId(entity.getId());
			// double watchableTime =
			// job.getDownloadedSize()*1.0/job.getmTotalSize();
			double watchableTime = job.getProgress() / 100.0;
			downloadManager.playVideo(this, path, entity.getName(),
					job.getmTotalSize() + "", watchableTime, downloadInfo);
		} else {
			Toast.makeText(this, R.string.download_can_play_rate, 0).show();
		}
	}

	private DownloadJob getJob(int position) {
		return (DownloadJob) mListView.getAdapter().getItem(position);
	}

	@Override
	public void onDownloadChanged(DownloadManager manager) {
		mHandler.post(mUpdateTimeTask);
	}

	public void onBackButton() {
		if (BrowserApp.getInstance().ismIsAppExit()) {
			BrowserApp.getInstance().setFSingalActivityToBg(true);
		}
		onBackPressed();
		overridePendingTransition(R.anim.hold, R.anim.push_bottom_out);
	}

	public void onEditButton() {
		DownloadJobAdapter adapter = (DownloadJobAdapter) mListView
				.getAdapter();
		if (adapter.isDeleteState()) {
			adapter.setDeleteState(false);
			edit_button.setText(R.string.down_btn_edit);
		} else {
			adapter.setDeleteState(true);
			edit_button.setText(R.string.down_btn_ok);
		}
		adapter.notifyDataSetChanged();
	}

	public void DownloadListView(int position) {
		playNow(position);
	}

	private void initDownloadTitle() {
		int deviceWidthPixels = DeviceInfoUtil.getWidthPixels(this);
		if (deviceWidthPixels <= 480 && deviceWidthPixels > 320) {
			title.setTextSize(20);
			setTitleBarHeight(80);
		} else if (deviceWidthPixels <= 320 && deviceWidthPixels > 0) {
			title.setTextSize(18);
			setTitleBarHeight(54);
		} else if (deviceWidthPixels <= 720 && deviceWidthPixels > 480) {
			title.setTextSize(20);
			setTitleBarHeight(120);
			setLeftButtonMargin(17);
			setRightButtonMargin(20, 16);
		} else if (deviceWidthPixels <= 800 && deviceWidthPixels > 720) {
			title.setTextSize(25);
			setTitleBarHeight(136);
			setLeftButtonMargin(17);
			setRightButtonMargin(20, 18);
		} else {
			title.setTextSize(25);
			setTitleBarHeight(136);
			setRightButtonMargin(20, 14);
		}
	}

	private void setLeftButtonMargin(int leftMargin) {
		LinearLayout.LayoutParams lp = (android.widget.LinearLayout.LayoutParams) left_button
				.getLayoutParams();
		lp.leftMargin = leftMargin;
		left_button.setLayoutParams(lp);
	}

	private void setTitleBarHeight(int highdip) {
		if (titlebar != null) {
			LinearLayout.LayoutParams parma = new LinearLayout.LayoutParams(
					LayoutParams.FILL_PARENT, highdip);
			titlebar.setLayoutParams(parma);
		}
	}

	private void setRightButtonMargin(int rightMargin, int textSize) {
		LinearLayout.LayoutParams lp = (android.widget.LinearLayout.LayoutParams) edit_button
				.getLayoutParams();
		lp.rightMargin = rightMargin;
		edit_button.setLayoutParams(lp);
		edit_button.setTextSize(textSize);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			if (BrowserApp.getInstance().ismIsAppExit()) {
				BrowserApp.getInstance().setFSingalActivityToBg(true);
			}
			onBackPressed();
			overridePendingTransition(R.anim.hold, R.anim.push_bottom_out);
		}
		return super.onKeyDown(keyCode, event);
	}

	// /**退出客户端后 从通知栏进入是 上报启动上报*/
	// private void bootStrapUpload(){
	// if(ACTIVITYSIZES == ActivityHolder.getInstance().getActivityNumber()){
	// UploadReport uploadReportImpl = new UploadReportImpl(this);
	// uploadReportImpl.doUploadReport("bootstrap",
	// LOCALSTART,ConstantUtil.START_TIME_DEFAULT);
	// UploadUtils.saveStartData(this,
	// Integer.parseInt(LOCALSTART),ConstantUtil.START_TIME_DEFAULT,
	// ConstantUtil.SUCCEED_DEFAULT);
	// }
	// }
	//

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// ActivityHolder.getInstance().removeActivity(this);
	}
}
