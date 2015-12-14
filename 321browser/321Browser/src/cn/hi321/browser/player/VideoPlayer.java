package cn.hi321.browser.player;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.MediaPlayer.OnBufferingUpdateListener;
import io.vov.vitamio.MediaPlayer.OnCompletionListener;
import io.vov.vitamio.MediaPlayer.OnErrorListener;
import io.vov.vitamio.MediaPlayer.OnInfoListener;
import io.vov.vitamio.MediaPlayer.OnPreparedListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Formatter;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import cn.hi321.browser.media.entity.BaiduResolution;
import cn.hi321.browser.media.entity.Media;
import cn.hi321.browser.media.entity.MediaItem;
import cn.hi321.browser.media.http.AsyncHttpClient;
import cn.hi321.browser.media.http.AsyncHttpResponseHandler;
import cn.hi321.browser.ui.activities.HomeActivity;
import cn.hi321.browser.utils.LogUtil;
import cn.hi321.browser.utils.Utils;
import cn.hi321.browser2.R;

import com.umeng.analytics.MobclickAgent;

/**
 * 
 * @author yangguangfu Vitamio播放器
 */
public class VideoPlayer extends Activity {
	private final static String TAG = "VideoPlayer";

	private int position;

	private String radia = null;

	public static final String netACTION = "android.net.conn.CONNECTIVITY_CHANGE";

	private int mCurrentPosition = 0;

	private int fristBufferOk = -1;

	private io.vov.vitamio.widget.VideoView mVideoView = null;

	private SeekBar mPlayerSeekBar = null;

	private SeekBar mSeekBarvolume = null;

	private TextView mEndTime = null;
	private TextView mCurrentTime = null;

	private TextView mLoadingText = null;

	private TextView mLoadingRxBytesText = null;

	private TextView mLoadingVideoName = null;

	private TextView mLoadingBufferingText = null;

	private TextView mVideoNameText = null;
	
	private String mVideoName= null;

	private ImageView mBatteryState = null;

	private TextView mLastModify = null;

	private Button mBtnSetplay = null;

	private AudioManager mAudioManager = null;

	private int currentVolume = 0;
	private Button mDiaplayMode = null;
	private Button mPrevButton = null;
	private Button mPlayOrPause = null;
	private Button mNextButton = null;
	private Button mPlayerVolume = null;

	private static int screenWidth = 0;
	private static int screenHeight = 0;
	private final static int TIME = 6868;
	private boolean isControllerShow = true;
	private boolean isPaused = false;
	private boolean isFullScreen = false;
	private boolean isSilent = false;

	private boolean isOnCompletion = false;

	private final static int SCREEN_FULL = 0;
	private final static int SCREEN_DEFAULT = 1;

	private final static int HIDE_CONTROLER = 1;

	private final static int PAUSE = 3;

	private final static int EXIT_TEXT = 5;
	private final static int PROGRESS_CHANGED = 0;

	private final static int BUFFER = 6;

	private final static int BUFFERING_TAG = 7;

	private final static int EXIT = 8;

	private final static int SET_PAUSE_BUTTON = 9;

	private final static int IS_PAUSE_BUTTON = 10;

	private final static int SEEK_BACKWARD = 11;

	private final static int SEEK_FORWARD = 12;

	private final static int REPLAY = 13;

	private final static int CHANGED_RXBYTES = 15;

	private Intent mIntent;

	private Uri uri;

	private Button mPlayerButtonBack = null;

	private StringBuilder mFormatBuilder;
	private Formatter mFormatter;

	private LinearLayout frame = null;
	private FrameLayout mFrameLayout = null;

	private LinearLayout mPlayerLoading;

	private LinearLayout mVideoBuffer;

	private TextView mVideoBufferText = null;

	private boolean isLocal = false;

	private boolean isLoading = true;

	private int level = 0;
	
	private boolean mIsLive = false;

	private BroadcastReceiver batteryReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			level = intent.getIntExtra("level", 0);

		}
	};

	private void setBattery(int level) {
		if (level <= 0) {
			mBatteryState.setBackgroundResource(R.drawable.ic_battery_0);
		} else if (0 < level && level <= 10) {
			mBatteryState.setBackgroundResource(R.drawable.ic_battery_10);
		} else if (10 < level && level <= 20) {
			mBatteryState.setBackgroundResource(R.drawable.ic_battery_20);
		} else if (20 < level && level <= 40) {
			mBatteryState.setBackgroundResource(R.drawable.ic_battery_40);
		} else if (40 < level && level <= 60) {
			mBatteryState.setBackgroundResource(R.drawable.ic_battery_60);
		} else if (60 < level && level <= 80) {
			mBatteryState.setBackgroundResource(R.drawable.ic_battery_80);
		} else if (80 < level && level <= 100) {
			mBatteryState.setBackgroundResource(R.drawable.ic_battery_100);
		}

	}

	private SharedPreferences preference = null;
	private int histroyPosition = 0;
	private String histroyUri = null;
	private String[] netUris = null;
	private String[] loacaUris = null;
	private boolean isTrue = false;
	private boolean isAutoNext = false;
	private ArrayList<MediaItem> mCurrentPlayList;
	private MediaItem mMediaItem = null;
	private Media mMedia = null;

	private boolean checkVitamioLibs = false;

	private NetCheckReceiver mCheckReceiver;
	private boolean isNetAvailable = true;
  
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		if (!io.vov.vitamio.LibsChecker.checkVitamioLibs(this))
			return;

		checkVitamioLibs = true;
		LogUtil.e(TAG, " ---onCreate()--");
 
		initWindow();

		initData();

		initView();
		
        mIntent = getIntent();
		mMedia = (Media) mIntent.getSerializableExtra("media");
		firstCheckIsLocal();
		if(mMedia != null){
			position = mMedia.getPosition();
		    mCurrentPlayList = mMedia.getMediaItemArrayList();
		    getNextUri();
		    setMediaName();
		    checkUri();
			setButtonState();
			getScreenSize();
			initVideoView();
			startPlayHtmlUri();
			
		}else{
			getPlayData();
			checkUri();
			setMediaName();
			getScreenSize();
			initVideoView();
			startPlay();
		}
		
		

	}
	private void playNextReset(){
		isOnCompletion = false;
		isBuffering = true;
		isBack = false;
		isError = false;
		isLoading = true;
		Utils.isErrorNum = false;
		mCurrentPosition = 0;
	}
	private void playNextNetVideo(){
		if(mMedia != null){
			playNextReset();
			hideController();
			if (mVideoBuffer != null)
				mVideoBuffer.setVisibility(View.GONE);
			if (mPlayerLoading != null) {
				mPlayerLoading.setVisibility(View.VISIBLE);
			}
			if (mHandler != null) {
				mHandler.sendEmptyMessage(PAUSE);
			}
			getNextUri();
			setMediaName();
		    checkUri();
		    setButtonState();
			startPlayHtmlUri();
		}
	}
	
	private void playNextVideo(){

		playNextReset();
		hideController();
		if (mVideoBuffer != null)
			mVideoBuffer.setVisibility(View.GONE);
		if (mPlayerLoading != null) {
			mPlayerLoading.setVisibility(View.VISIBLE);
		}
		if (mHandler != null) {
			mHandler.sendEmptyMessage(PAUSE);
		}

		LogUtil.e(TAG, " ---getPlayData()--");
			
		getNextVideoUri();
		
		setButtonState();

		getHistroyPosition();
	
		checkUri();
		setMediaName();
		startPlay();
	
	}


	/**
	 * 注册检查网络变化*
	 */
	private void regListenerNet() {
		mCheckReceiver = new NetCheckReceiver();
		IntentFilter intentfilter = new IntentFilter();
		intentfilter.addAction(netACTION);
		this.registerReceiver(mCheckReceiver, intentfilter);
	}

	/**
	 * 取消注册检查网络变化*
	 */
	private void unregisterListenerNet() {
		if (mCheckReceiver != null) {
			unregisterReceiver(mCheckReceiver);

		}

	}

	private void checkNetworkInfo() {
		ConnectivityManager conMan = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

		State mobile = conMan.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
				.getState();
		State wifi = conMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
				.getState();

		if (mobile == State.CONNECTED || mobile == State.CONNECTING) {
			
			if (uri != null && isHttp&&!PlayerUtils.getTip3GNework(VideoPlayer.this)&&isNetAvailable) {
				Toast.makeText(VideoPlayer.this, getString(R.string.net_3g), 1)
						.show();
			}
			isNetAvailable = true;
			return;
		}

		if (wifi == State.CONNECTED || wifi == State.CONNECTING) {
			isNetAvailable = true;
			return;
		}
		isNetAvailable = false;
		// 添加对本地文件的判断，播放本地文件时不应提示网络中断
		if (uri != null && isHttp && fristBufferOk == 0) {
			Toast.makeText(VideoPlayer.this,
					getString(R.string.net_outage_tip), 1).show();
		}

	}

	class NetCheckReceiver extends BroadcastReceiver {


		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(netACTION)) {
				LogUtil.e(TAG, "ACTION:" + intent.getAction());
				checkNetworkInfo();
			}

		}
	}

	private void initVideoView() {

		mVideoView
				.setOnSeekCompleteListener(new io.vov.vitamio.MediaPlayer.OnSeekCompleteListener() {

					@Override
					public void onSeekComplete(MediaPlayer arg0) {
						// TODO Auto-generated method stub
						if (mVideoBuffer != null) {
							mVideoBuffer.setVisibility(View.GONE);
						}
					}
				});

		LogUtil.e(TAG, " ---initVideoView()--");
		mVideoView.setOnErrorListener(new OnErrorListener() {

			public boolean onError(MediaPlayer mp, int what, int extra) {

				LogUtil.e(TAG, " ---出错了Error: " + what + "," + extra);
				isError = true;

				if (!isCheckUriBym3u8&&fristBufferOk == 0 && replayNum < 3
						&& mCurrentPosition > 1000) {
					replayNum++;
					replay();
					return true;
				}

				if (isReplay) {
					return true;
				}
				if (fristBufferOk == 0 && mCurrentPosition > 1000 && !isReplay) {
					isReplay = true;
					retryDialog();
					return true;
				}
				
				if(mMediaItem != null&&!Utils.isEmpty(mMediaItem.getSourceUrl())&&fristBufferOk == -1 && !isReplay){
					    Intent i = new Intent(VideoPlayer.this, GetPlayUriByHtmlActivity.class);
					    Bundle bundle = new Bundle();
						bundle.putSerializable("VideoInfo", mMediaItem);
						i.putExtra("extra", bundle);
						i.setFlags(1);
						startActivity(i);
						overridePendingTransition(R.anim.fade, R.anim.hold);
						finish();
					
					return true;
				}

				if (isCheckUriBym3u8 && !Utils.isErrorNum) {
					LogUtil.i(TAG, " ---再次播放了: " + what + "," + extra);
					Utils.isErrorNum = true;
					// ConfirmExit();
					retryDialog();

				} else {
					LogUtil.i(TAG, " ---要报错了: " + what + "," + extra);
					if (isError) {
						if (mVideoBuffer != null) {
							mVideoBuffer.setVisibility(View.GONE);
						}
					}
					errorType = what;
					LogUtil.i(TAG, "Error: " + what + "," + extra);
					if (uri != null) {
						ConfirmExit();
					}
					mHandler.sendEmptyMessage(SET_PAUSE_BUTTON);
				}

				return true;

			}

		});
		mVideoView
				.setOnBufferingUpdateListener(new OnBufferingUpdateListener() {

					public void onBufferingUpdate(MediaPlayer arg0,
							int bufferingProgress) {

//						 LogUtil.e(TAG,
//						 " ---onBufferingUpdate()--bufferingProgress="+bufferingProgress
//						 );

					}
				});

		mVideoView.setOnInfoListener(new OnInfoListener() {

			public boolean onInfo(MediaPlayer mp, int what, int extra) {

				switch (what) {

				case MediaPlayer.MEDIA_INFO_BUFFERING_START:
					if (LogUtil.DEBUG) {
						// Toast.makeText(VideoPlayer.this, "开始缓冲---",
						// 0).show();
					}
					// if(Utils.getOSVersionSDKINT(VideoPlayer.this)>=9){
					 if (mVideoBuffer != null&&!isCheckUriBym3u8) {
					    mVideoBuffer.setVisibility(View.VISIBLE);
					 }
					// }
					LogUtil.e(TAG, "-开始缓冲----MEDIA_INFO_BUFFERING_START---");

					break;

				case MediaPlayer.MEDIA_INFO_BUFFERING_END:
					if (LogUtil.DEBUG) {
						// Toast.makeText(VideoPlayer.this, "-结束缓冲----",
						// 0).show();
					}
					// if(Utils.getOSVersionSDKINT(VideoPlayer.this)>=9){
					 if (mVideoBuffer != null) {
					   mVideoBuffer.setVisibility(View.GONE);
					 }
					// }
					LogUtil.e(TAG, "--结束缓冲---MEDIA_INFO_BUFFERING_END----");

					break;

				case MediaPlayer.MEDIA_INFO_VIDEO_TRACK_LAGGING:

					// if (mVideoBuffer != null) {
					// mVideoBuffer.setVisibility(View.VISIBLE);
					// }
					if (LogUtil.DEBUG) {
						// Toast.makeText(VideoPlayer.this,
						// " --MEDIA_INFO_VIDEO_TRACK_LAGGING---", 0).show();
					}
					//
					LogUtil.e(TAG, "--MEDIA_INFO_VIDEO_TRACK_LAGGING--");

					break;

//				case MediaPlayer.MEDIA_INFO_DOWNLOAD_RATE_CHANGED:
//					if (LogUtil.DEBUG) {
//						// Toast.makeText(VideoPlayer.this,
//						// " --MEDIA_INFO_DOWNLOAD_RATE_CHANGED---", 0).show();
//					}
//					//
//					LogUtil.e(TAG,
//							"--MEDIA_INFO_DOWNLOAD_RATE_CHANGED--extra=="
//									+ extra);
//					break;

				case MediaPlayer.MEDIA_INFO_NOT_SEEKABLE:
					if (LogUtil.DEBUG) {
						// Toast.makeText(VideoPlayer.this,
						// " --MEDIA_INFO_NOT_SEEKABLE---", 0).show();
					}
					//
					LogUtil.e(TAG, "--MEDIA_INFO_NOT_SEEKABLE--");
					break;

				case MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK:
					if (LogUtil.DEBUG) {
						// Toast.makeText(VideoPlayer.this,
						// " --MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK---",
						// 0).show();
					}
					//
					LogUtil.e(TAG,
							"--MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK--");
					break;

				}

				return true;
			}
		});

		mPlayerSeekBar
				.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

					public void onProgressChanged(SeekBar seekbar,
							int progress, boolean fromUser) {
						if (fromUser) {
							mVideoView.seekTo(progress);
							cancelDelayHide();
						}
					}

					public void onStartTrackingTouch(SeekBar arg0) {
						cancelDelayHide();
					}

					public void onStopTrackingTouch(SeekBar seekBar) {
						if (uri == null && !isPaused) {
							isBuffering = false;
							mHandler.sendEmptyMessageDelayed(BUFFERING_TAG,
									1000);
						}
						mHandler.sendEmptyMessage(SET_PAUSE_BUTTON);
						hideControllerDelay();
					}
				});

		mSeekBarvolume
				.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

					public void onProgressChanged(SeekBar seekbar,
							int progress, boolean fromUser) {
						currentVolume = mAudioManager
								.getStreamVolume(AudioManager.STREAM_MUSIC);

						LogUtil.e(TAG, "progress��" + progress + "---fromUser="
								+ fromUser + "------currentVolume="
								+ currentVolume);
						if (fromUser) {
							if (progress >= 15) {
								isSilent = false;
								updateVolume(15);
							} else if (progress <= 0) {
								isSilent = true;
								updateVolume(0);
							} else {
								isSilent = false;
								updateVolume(progress);
							}

						}

						cancelDelayHide();

					}

					public void onStartTrackingTouch(SeekBar arg0) {

					}

					public void onStopTrackingTouch(SeekBar seekBar) {
						hideControllerDelay();
					}
				});

		mVideoView.setOnPreparedListener(new OnPreparedListener() {

			public void onPrepared(MediaPlayer arg0) {

				mPlayerLoading.setVisibility(View.VISIBLE);
			
				isControllerShow = false;

				isBuffering = true;

				setVideoScale(SCREEN_DEFAULT);

				if (!isLoading) {
					hideController();
				}

				int i = (int) mVideoView.getDuration();
				Log.d("onCompletion", "" + i);
				mPlayerSeekBar.setMax(i);
				mEndTime.setText(stringForTime(i));

				//
				String netUri = uri.toString();
				// LogUtil.i(TAG, "urill==="+netUri);
				String loacaUri = histroyUri;
				// LogUtil.i(TAG, "his==="+loacaUri);
				isTrue = false;

				if (uri != null && loacaUri != null) {

					if (isHttp) {
						if (!isTrue && netUri.equals(loacaUri)) {
							isTrue = true;
							if (histroyPosition > 0)
								mVideoView.seekTo((int) histroyPosition);
						}
						if (!isTrue && netUris[0].equals(loacaUris[0])
								&& netUris[1].equals(loacaUris[1])) {
							isTrue = true;
							if (histroyPosition > 0)
								mVideoView.seekTo((int) histroyPosition);
						}

						if (!isTrue && netUris[0].equals(loacaUris[0])) {
							isTrue = true;
							if (histroyPosition > 0)
								mVideoView.seekTo((int) histroyPosition);
						}
					} else {
						if (!isTrue && netUri.equals(loacaUri)) {
							isTrue = true;
							if (histroyPosition > 0)
								mVideoView.seekTo((int) histroyPosition);
						}
					}

				}

				mVideoView.start();
				mPlayerLoading.setVisibility(View.GONE);
				mVideoBuffer.setVisibility(View.GONE);
				fristBufferOk = 0;
				replayNum = 0;
				LogUtil.e(TAG, " ---播放成功了: -----");
				isLoading = false;
				Utils.isErrorNum = false;
				
				isOnCompletion = false;
				isError = false;
				mHandler.sendEmptyMessage(SET_PAUSE_BUTTON);
				if(isCheckUriBym3u8){
					showController() ;
				}
				cancelDelayHide();
				hideControllerDelay();
				mHandler.removeMessages(PROGRESS_CHANGED);
				mHandler.sendEmptyMessage(PROGRESS_CHANGED);
				mHandler.removeMessages(BUFFER);
				mHandler.sendEmptyMessage(BUFFER);
				

				

			}
		});

		mVideoView.setOnCompletionListener(new OnCompletionListener() {

			public void onCompletion(MediaPlayer arg0) {
				LogUtil.i(TAG, "onCompletion()================="
						+ isOnCompletion);

				if (!isOnCompletion) {
					isOnCompletion = true;
					LogUtil.i(TAG, "onCompletion()========Completion");
					isBuffering = false;
					if (uri != null) {

						if (preference != null) {
							SharedPreferences.Editor editor = preference.edit();
							if (editor != null) {

								if (mCurrentPosition > 0) {
									editor.putInt("CurrentPosition", 0);
									if (uri != null) {
										editor.putString("histroyUri",
												uri.toString());
									}

								} else {
									editor.putInt("CurrentPosition", 0);
									if (uri != null) {
										editor.putString("histroyUri",
												uri.toString());
									}

								}
								editor.commit();
							}

						}

						if (!isLocal) {
							ConfirmExit();
						} else {
							if (isCheckUriBym3u8&&mIsLive) {

								if (replayNum <= 3 && fristBufferOk == 0) {
									if (mHandler != null) {
										mHandler.removeMessages(REPLAY);
										mHandler.sendEmptyMessage(REPLAY);
									} else {
										replay();
									}
									replayNum++;
									return;
								}

								if (isReplay) {
									return;
								}
								// 在出错之前从新更新一遍用户存储播放历史的方法
								// updateByHashid();
								if (fristBufferOk == 0 && !isReplay) {
									isReplay = true;
									retryDialog();
									return;
								}

							} else {
								if (!isAutoNext) {
									if (mCurrentPlayList != null
											&& mCurrentPlayList.size() > 1) {

										int n = mCurrentPlayList.size();
										if (++position < n) {
											
											if(mMedia != null){
												playNextNetVideo();
											}else{
												playNextVideo();
											}
										} else {
											--position;
											if(mMedia != null){
												playNextNetVideo();
											}else{
												playNextVideo();
											}

										}

									} else {

										if (isHttp) {
											if (mVideoView != null) {
												mExitHandler
														.sendEmptyMessage(EXIT);
											}

										} else {
											if (mVideoView != null) {
												mExitHandler
														.sendEmptyMessage(EXIT);
											}

										}

									}
								} else {
//									finish();
									mExitHandler.sendEmptyMessage(EXIT);
								}
							}

						}

					}
				}
			}
		});
	}

	private void getPlayData() {
		LogUtil.e(TAG, " ---getPlayData()--");
		if (mIntent != null) {
			uri = mIntent.getData();
			if(!isLocal){
				String name = Utils.getFileName(uri.toString());
				mVideoNameText.setText(name);
				mLoadingVideoName.setText(name);
				mLoadingVideoName.setVisibility(View.GONE);	
			}
			mCurrentPlayList = (ArrayList<MediaItem>) mIntent
					.getSerializableExtra("MediaIdList");
			position = mIntent.getIntExtra("CurrentPosInMediaIdList", 0);
			radia = mIntent.getStringExtra("radia");
			mMediaItem = (MediaItem) mIntent
					.getSerializableExtra("VideoInfo");
			getUri();
		
		}

		setButtonState();

		getHistroyPosition();

		// uri=Uri.parse("rtsp://live.android.maxlab.cn/maxtv-ln.sdp");
		// uri=Uri.parse("http://cms.doplive.com.cn/video1/index_multi.m3u8?date=20120302220001&uid=0&rnd=2012030222000112069&deviceid=123&key=5311e457e8b47402676dae4cd2368118&count=1330675490");

	
	}
	
	private void firstCheckIsLocal() {
		if (mIntent != null &&mIntent.getData() != null) {
			isLocal = false;
		} else {
			isLocal = true;
		}
		

	}

	private void getUri() {
		if(mIntent != null){
			String strLocaluri = null;
			if (mMediaItem != null) {
				strLocaluri = mMediaItem.getUrl();
				mIsLive = mMediaItem.isLive();
			}
			if (strLocaluri == null && mCurrentPlayList != null) {
				mMediaItem = mCurrentPlayList.get(position);
				strLocaluri = mMediaItem.getUrl();
				mIsLive = mMediaItem.isLive();
				if(Utils.isEmpty(strLocaluri)){
					strLocaluri = mMediaItem.getSourceUrl();
				}
			}

			if (strLocaluri != null) {
				uri = Uri.parse(strLocaluri);
				if (mCurrentPlayList != null
						&& mCurrentPlayList.size() > 1) {
					mVideoName = mMediaItem.getTitle();
				} else {
					if (mMediaItem != null) {
						mVideoName = mMediaItem.getTitle();
					} 
				}
			}
			
			if (uri != null) {
				String content = uri.toString().replace("?", "yangguangfu");
				if (content != null)
					netUris = content.split("yangguangfu");
			}
		}
		
	}
	
	private void getNextVideoUri() {
		if(mIntent != null){
			String strLocaluri = null;
			if (strLocaluri == null && mCurrentPlayList != null) {
				mMediaItem = mCurrentPlayList.get(position);
				strLocaluri = mMediaItem.getUrl();
				mIsLive = mMediaItem.isLive();
				if(Utils.isEmpty(strLocaluri)){
					strLocaluri = mMediaItem.getSourceUrl();
				}
			}

			if (strLocaluri != null) {
				uri = Uri.parse(strLocaluri);
				if (mCurrentPlayList != null
						&& mCurrentPlayList.size() > 1) {
					mVideoName = mMediaItem.getTitle();
				} else {
					if (mMediaItem != null) {
						mVideoName = mMediaItem.getTitle();
					} 
				}
			}
			
			if (uri != null) {
				String content = uri.toString().replace("?", "yangguangfu");
				if (content != null)
					netUris = content.split("yangguangfu");
			}
		}
		
	}
	
	private void setMediaName(){
	
		mLoadingVideoName.setText(mVideoName);
		mVideoNameText.setText(mVideoName);
	}
	private void getNextUri() {
		String strLocaluri = null;
		if (strLocaluri == null && mCurrentPlayList != null) {
			mMediaItem = mCurrentPlayList.get(position);
			strLocaluri = mMediaItem.getSourceUrl();
			mIsLive = mMediaItem.isLive();
			if(Utils.isEmpty(strLocaluri)){
				strLocaluri = mMediaItem.getUrl();
			}
		}

		if (strLocaluri != null) {
			String name = Utils.getFileName(strLocaluri);
			uri = Uri.parse(strLocaluri);

			if (mCurrentPlayList != null
					&& mCurrentPlayList.size() > 1) {
//				movie电影,tvplay电视,comic动漫,tvshow综艺,info新闻,amuse搞笑,music音乐,sport体育，woman美女
				String mediaType =mMedia.getMediaType();
				if(mediaType != null){
					if(mediaType.equals("tvplay")
							||mediaType.equals("comic")
							){
						mVideoName = mMediaItem.getTitle()+"  第"+	mMediaItem.getEpisode()+"集";
					}else if(mediaType.equals("tvshow")){
						mVideoName = mMediaItem.getTitle()+"  "+	mMediaItem.getEpisode();
					}else{
						mVideoName = mMediaItem.getTitle();
					}
				}
				
				
			} else {
				mVideoName = mMediaItem.getTitle();
			}
		}
		if (uri != null) {
			String content = uri.toString().replace("?", "yangguangfu");
			if (content != null)
				netUris = content.split("yangguangfu");
		}
	}
	
	private void checkUri() {
		if (uri != null) {
			isHttp = Utils.checkUri(VideoPlayer.this, uri);
			isCheckUriBym3u8 = Utils.isCheckUriByM3u8(VideoPlayer.this, uri);
		}
	}
	private void setButtonState() {
		if (mCurrentPlayList != null && mCurrentPlayList.size() == 1) {
			setNextEnabled(false);
			setPrevEnabled(false);

		} else if (mCurrentPlayList != null && mCurrentPlayList.size() > 1) {
			if (position == 0) {
				setPrevEnabled(false);
				setNextEnabled(true);
			} else if (position == (mCurrentPlayList.size() - 1)) {
				setPrevEnabled(true);
				setNextEnabled(false);
			} else {
				setPrevEnabled(true);
				setNextEnabled(true);
			}

		} else {
			setPlaySeekBarEnabled(true);
			setNextEnabled(true);
			setPrevEnabled(true);

		}
	}

	private void getHistroyPosition() {
		if (preference != null) {
//			isAutoNext = preference.getBoolean(SettingActivity.key_5, false);
			histroyUri = preference.getString("histroyUri", null);
			histroyPosition = preference.getInt("CurrentPosition", 0);
			if (histroyUri != null) {
				String content = histroyUri.replace("?", "yangguangfu");
				if (content != null)
					loacaUris = content.split("yangguangfu");
			}

		}
	}

	private void initData() {
		LogUtil.e(TAG, " ---initData()--");
		mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

		currentVolume = mAudioManager
				.getStreamVolume(AudioManager.STREAM_MUSIC);
		mAudioMax = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		preference = PreferenceManager.getDefaultSharedPreferences(this);

		LogUtil.v(TAG, "onCreate()");

		LogUtil.v(TAG, getIntent().toString());
		// uri = getIntent().getData();

		LogUtil.v(TAG, "The main thread id = " + Thread.currentThread().getId()
				+ "\n");

		registerReceiver(batteryReceiver, new IntentFilter(
				Intent.ACTION_BATTERY_CHANGED));

		regListenerNet();
		isOnCompletion = false;
		mFormatBuilder = new StringBuilder();
		mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
	}

	private void initWindow() {
		LogUtil.e(TAG, " ---initWindow()--");

		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		// getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.video_player);
	}

	private Builder dialogBuilder;
	private boolean isReplay = false;

	private void retryDialog() {
		dialogBuilder = getBuilderInstance();
		if (dialogBuilder != null) {
			dialogBuilder.setTitle(R.string.tips);
			if (!isNetAvailable) {
				dialogBuilder.setMessage(R.string.playretry_neterror);
			} else {
				dialogBuilder.setMessage(R.string.playretry);
			}
			dialogBuilder.setPositiveButton(R.string.retry,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							if (mHandler != null) {
								mHandler.removeMessages(REPLAY);
								mHandler.sendEmptyMessage(REPLAY);
							} else {
								replay();
							}
						}
					});
			dialogBuilder.setNegativeButton(R.string.player_exit,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							if (!isBack) {
								isBack = true;
								mExitHandler.removeMessages(EXIT_TEXT);
								mExitHandler.sendEmptyMessage(EXIT_TEXT);
							}
							if (dialog != null) {
								dialog.dismiss();
								dialog = null;
							}
						}
					});
			dialogBuilder.create().show();
		}
	}

	public Builder getBuilderInstance() {
		if (dialogBuilder == null) {
			dialogBuilder = new Builder(VideoPlayer.this);
		}
		return dialogBuilder;
	}

	private int errorType = 0;

	public void setPauseButtonImage() {
		if (mVideoView != null) {
			LogUtil.i(TAG, "setPauseButtonImage()=============");
			try {
				if (mVideoView.isPlaying()) {
					mPlayOrPause.setBackgroundResource(R.drawable.player_btn_pause);
				} else {
					mPlayOrPause.setBackgroundResource(R.drawable.player_btn_play);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	private RelativeLayout mPlayerLayout = null;

	private void initView() {
		LogUtil.e(TAG, " ---initView()--");
		mPlayerLayout = (RelativeLayout) findViewById(R.id.playe_layout);

		frame = (LinearLayout) findViewById(R.id.frame);

		mFrameLayout = (FrameLayout) findViewById(R.id.mFrameLayout);

		//
		mPlayerLoading = (LinearLayout) findViewById(R.id.player_loading);

		mVideoBuffer = (LinearLayout) findViewById(R.id.video_buffer);
		
		mVideoBufferText = (TextView) findViewById(R.id.mediacontrolle_buffer_info_text);

		mVideoView = (io.vov.vitamio.widget.VideoView) findViewById(R.id.video_view);

		mLoadingText = (TextView) findViewById(R.id.loading_text);

		mLoadingRxBytesText = (TextView) findViewById(R.id.loading_rxBytes_text);

		mLoadingVideoName = (TextView) findViewById(R.id.loading_video_name);

		mLoadingBufferingText = (TextView) findViewById(R.id.loading_text);

		mVideoNameText = (TextView) findViewById(R.id.video_name);

		mBatteryState = (ImageView) findViewById(R.id.battery_state);

		mLastModify = (TextView) findViewById(R.id.last_modify);

		mBtnSetplay = (Button) findViewById(R.id.btn_setplay);

		mPlayerButtonBack = (Button) findViewById(R.id.btn_exit);

		mPlayerSeekBar = (SeekBar) findViewById(R.id.PlaybackProgressBar);

		mSeekBarvolume = (SeekBar) findViewById(R.id.VioceProgressBar);

		mCurrentTime = (TextView) findViewById(R.id.current_time);

		mEndTime = (TextView) findViewById(R.id.total_time);

		mDiaplayMode = (Button) findViewById(R.id.diaplay_mode);

		mPrevButton = (Button) findViewById(R.id.btn_back);

		mPlayOrPause = (Button) findViewById(R.id.btn_play_pause);

		mNextButton = (Button) findViewById(R.id.btn_forward);

		mPlayerVolume = (Button) findViewById(R.id.btn_voice);

		// mPlayerPlayList = (ImageButton) findViewById(R.id.player_play_list);

		if (currentVolume <= 0) {
			mPlayerVolume.setBackgroundDrawable(VideoPlayer.this.getResources()
					.getDrawable(R.drawable.btn_voice));
		} else {
			mPlayerVolume.setBackgroundDrawable(VideoPlayer.this.getResources()
					.getDrawable(R.drawable.btn_voice));
		}

		mPlayerSeekBar.setThumbOffset(13);
		mPlayerSeekBar.setMax(100);
		mPlayerSeekBar.setSecondaryProgress(0);

		mSeekBarvolume.setThumbOffset(13);
		mSeekBarvolume.setMax(15);
		mSeekBarvolume.setProgress(currentVolume);

		mPlayerButtonBack.setOnClickListener(mListener);

		mPlayOrPause.setOnClickListener(mListener);

		mPrevButton.setOnClickListener(mListener);
		mNextButton.setOnClickListener(mListener);
		// mPlayerPlayList.setOnClickListener(mListener);
		mDiaplayMode.setOnClickListener(mListener);
		mPlayerVolume.setOnClickListener(mListener);

		mBtnSetplay.setOnClickListener(mListener);

		hideController();
		mHandler.sendEmptyMessage(CHANGED_RXBYTES);

	}

	private View.OnClickListener mListener = new View.OnClickListener() {
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_exit:
				isBuffering = false;
				if (!isBack) {
					isBack = true;
					mExitHandler.sendEmptyMessage(EXIT_TEXT);
				}

				break;
			case R.id.btn_back:
				if (mCurrentPlayList != null && mCurrentPlayList.size() > 1) {
					isBack = false;
					int n = mCurrentPlayList.size();
					if (--position >= 0 && position < n) {

						if (mVideoView != null) {
							hideController();
						}
						if(mMedia != null){
							playNextNetVideo();
						}else{
							playNextVideo();
//							Utils.startSystemPlayer(VideoPlayer.this,
//									mCurrentPlayList, position);
//							mExitHandler.sendEmptyMessage(EXIT);
						}
						
					} else {
						position = 0;
						if (position >= 0 && position < n) {
							if (mVideoView != null) {
								hideController();
							}
							if(mMedia != null){
								playNextNetVideo();
							}else{
								playNextVideo();
//								Utils.startSystemPlayer(VideoPlayer.this,
//										mCurrentPlayList, position);
//								mExitHandler.sendEmptyMessage(EXIT);
							}
						}

					}

				} else {
					mHandler.sendEmptyMessage(SEEK_BACKWARD);
				}

				break;

			case R.id.btn_play_pause:
				mHandler.sendEmptyMessage(IS_PAUSE_BUTTON);
				break;
			case R.id.btn_forward:
				if (mCurrentPlayList != null && mCurrentPlayList.size() > 1) {
					isBack = false;

					int n = mCurrentPlayList.size();
					if (++position < n && position >= 0) {
						if (mVideoView != null) {
							hideController();
						}
						if(mMedia != null){
							playNextNetVideo();
						}else{
							playNextVideo();
//							Utils.startSystemPlayer(VideoPlayer.this,
//									mCurrentPlayList, position);
//							mExitHandler.sendEmptyMessage(EXIT);
						}
					} else {
						if (position > 0) {
							--position;
						}
						if (position >= 0 && position < n) {
							if (mVideoView != null) {
								hideController();
							}
							if(mMedia != null){
								playNextNetVideo();
							}else{
								playNextVideo();
//								Utils.startSystemPlayer(VideoPlayer.this,
//										mCurrentPlayList, position);
//								mExitHandler.sendEmptyMessage(EXIT);
							}
						
						}

					}

				} else {
					mHandler.sendEmptyMessage(SEEK_FORWARD);
				}
				break;
			case R.id.btn_voice:
				if (mAudioManager != null) {
					if (isSilent) {
						isSilent = false;
					} else {
						isSilent = true;
					}
					updateVolume(currentVolume);
				}

				break;

			case R.id.diaplay_mode:
				if (isFullScreen) {
					setVideoScale(SCREEN_DEFAULT);
				} else {
					setVideoScale(SCREEN_FULL);
				}

				break;
			case R.id.btn_setplay:

				openSetPlay();

				break;

			}

		}
	};

	private void startSystemPlayer() {

		Intent intent = new Intent(VideoPlayer.this, SystemPlayer.class);
		if (uri != null) {
			if (!isLocal) {
				intent.setData(uri);

			} else {
				if (mCurrentPlayList != null && mCurrentPlayList.size() > 1) {
					if(mMedia != null){
						Bundle mBundle = new Bundle();
						mMedia.setPosition(position);
						mBundle.putSerializable("media", mMedia);
						intent.putExtras(mBundle); 
					}else{
						Bundle mBundle = new Bundle();
						mBundle.putSerializable("MediaIdList", mCurrentPlayList);
						intent.putExtras(mBundle);
						intent.putExtra("CurrentPosInMediaIdList", position);
					}

				} else {
					if(mMedia != null){
						Bundle mBundle = new Bundle();
						mMedia.setPosition(position);
						mBundle.putSerializable("media", mMedia);
						intent.putExtras(mBundle); 
					}else{
						Bundle mBundle = new Bundle();
						if(mMediaItem != null)
						mBundle.putSerializable("VideoInfo", mMediaItem);
						intent.putExtras(mBundle);
					}
					
				} 

			}

		}

		startActivity(intent);
		overridePendingTransition(R.anim.fade, R.anim.hold);
		if(mExitHandler != null){
			mExitHandler.sendEmptyMessage(EXIT);
		}

	}

	private boolean isCick;
	private Dialog dialog;

	private void openSetPlay() {
		try {
			dialog = new Dialog(VideoPlayer.this, R.style.player_dialog_list);
			dialog.setCanceledOnTouchOutside(true);
			dialog.setContentView(R.layout.play_video_detail);
			WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
			lp.alpha = 0.95f; // 0.0-1.0
			dialog.getWindow().setAttributes(lp);

			TextView mlinearLanguage = (TextView) dialog
					.findViewById(R.id.set_player_text);
			mlinearLanguage.setText(getString(R.string.setplay_for_system));
			Button linearGridView = (Button) dialog
					.findViewById(R.id.set_player);
			linearGridView.setText("系统解码播放");

			linearGridView.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					if (!isCick) {
						isCick = true;
						if (dialog != null){
						   dialog.dismiss();
						}
						dialog = null;
						startSystemPlayer();
						
					}
				}
			});

			if (dialog != null && !dialog.isShowing()) {
				dialog.show();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private String stringForTime(int timeMs) {
		int totalSeconds = timeMs / 1000;

		int seconds = totalSeconds % 60;
		int minutes = (totalSeconds / 60) % 60;
		int hours = totalSeconds / 3600;

		mFormatBuilder.setLength(0);
		if (hours > 0) {
			return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds)
					.toString();
		} else {
			return mFormatter.format("%02d:%02d", minutes, seconds).toString();
		}
	}

	AsyncHttpClient client;
	BaiduResolution playDatas ;
	private void startPlayHtmlUri() {
		if (uri != null && mVideoView != null) {
			if (mVideoBuffer != null) {
				mVideoBuffer.setVisibility(View.GONE);
			}
			try {
				if(client == null){
					  client = new AsyncHttpClient(); 
				}
				String videoUrl = uri.toString();
			    playDatas = new BaiduResolution();
				playDatas.setSourceUrl(videoUrl); 
				String baiduService = "http://gate.baidu.com/tc?m=8&video_app=1&ajax=1&src="+videoUrl;
				if(!Utils.isEmpty(baiduService)){ 
//					if(Utils.hasNetwork(VideoPlayer.this)){ 
//						new NetWorkTask().execute(VideoPlayer.this,Utils.GET_PLAY_DATA,
//								baiduService,playDatas,null);
//			  		  }else{
//			  			Utils.showToast(VideoPlayer.this, VideoPlayer.this.getText(R.string.tip_network).toString());
//					 }
//					
					
					 if(Utils.hasNetwork(VideoPlayer.this)){
						 Utils.startWaitingDialog(VideoPlayer.this);
				         client.get(baiduService, new AsyncHttpResponseHandler() {
				               @Override
				               public void onSuccess(String response){ 
					            
//									JSONArray jsonArr = new JSONArray(response);
//									if(jsonArr==null)
//									return;
				            	   playDatas = parserMediaItem(response, playDatas);
				            	   if(playDatas != null){
										String video_source_url =playDatas.getVideo_source_url();
										if(!Utils.isEmpty(video_source_url)){
											uri = Uri.parse(video_source_url);
											mMediaItem.setUrl(uri.toString());
											checkUri();
											startPlay();
										}else{
											String sourceUri = playDatas.getSourceUrl();
											if(!Utils.isEmpty(sourceUri)){
												Intent intent = new Intent();
												intent.setAction(Intent.ACTION_VIEW);
												intent.setData(Uri.parse(sourceUri));
												startActivity(intent);
												finish();
											}else{
												Toast.makeText(VideoPlayer.this, "该视频无法播放", 1).show();
											}
											
										}
										
									}
								 
				               }
				           }); 
					 }else{
						 Utils.showToast(VideoPlayer.this, "亲网络异常,");
						 return;
					 }
				}
			} catch (Exception e) {
				// TODO: handle exception
			}


		} else {
			isBack = true;
			mExitHandler.sendEmptyMessage(EXIT);
		}
	}
	
	public BaiduResolution parserMediaItem(String js  ,BaiduResolution homeRes){
		
		 try { 
			JSONObject jsonOb = new JSONObject(js);
			if(jsonOb !=null){
				String logo = jsonOb.optString("logo");
				homeRes.setLogo(logo);
				
				String STRUCT_PAGE_TYPE = jsonOb.optString("STRUCT_PAGE_TYPE");
				homeRes.setSTRUCT_PAGE_TYPE(STRUCT_PAGE_TYPE);
				
				String video_source_url = jsonOb.optString("video_source_url");
				homeRes.setVideo_source_url(video_source_url);
				
				String video_source_type =jsonOb.optString("video_source_type");
				homeRes.setVideo_source_type(video_source_type);
				
				String video_trans_url =jsonOb.optString("video_trans_url");
				homeRes.setVideo_trans_url(video_trans_url);
				
				
				String content =jsonOb.optString("content");
				homeRes.setContent(content);
				
				
				String src_url_processed =jsonOb.optString("src_url_processed");
				homeRes.setSrc_url_processed(src_url_processed);
				
				String page_title =jsonOb.optString("page_title");
				homeRes.setPage_title(page_title);
				
				String src_url =jsonOb.optString("src_url");
				homeRes.setSrc_url(src_url);
				
				 return homeRes;
				
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 return homeRes;
	  }
	
	private void startPlay() {
		if (uri != null && mVideoView != null) {
			if (mVideoBuffer != null) {
				mVideoBuffer.setVisibility(View.GONE);
			}
			LogUtil.e(TAG, "playUri ===111--" + String.valueOf(uri));
			mVideoView.setVideoURI(uri);
//			mVideoView.setVideoQuality(MediaPlayer.VIDEOQUALITY_LOW);

		} else {
			isBack = true;
			mExitHandler.sendEmptyMessage(EXIT);
		}
	}

	public void setPlaySeekBarEnabled(boolean enabled) {
		if (mPlayerSeekBar != null) {
			mPlayerSeekBar.setEnabled(enabled && mPlayerSeekBar != null);

		}
	}

	public void setNextEnabled(boolean enabled) {
		if (mNextButton != null) {
			mNextButton.setEnabled(enabled && mListener != null);
			if (enabled) {

				if (mCurrentPlayList != null && mCurrentPlayList.size() > 0) {
				
						mNextButton.setBackgroundDrawable(VideoPlayer.this
								.getResources().getDrawable(
										R.drawable.btn_forward));
					
				} else {
					mNextButton.setBackgroundDrawable(VideoPlayer.this
							.getResources().getDrawable(
									R.drawable.btn_forward_one));
				}

				// mNextButton.setBackgroundDrawable(VideoPlayer.this.getResources().getDrawable(R.drawable.videonextbtn_bg));
			} else {
				if (mCurrentPlayList != null && mCurrentPlayList.size() > 0) {
					if (position == (mCurrentPlayList.size() - 1)) {
						mNextButton.setBackgroundDrawable(VideoPlayer.this
								.getResources().getDrawable(
										R.drawable.video_next_btn_bg));
					}
				} else {
					mNextButton.setBackgroundDrawable(VideoPlayer.this
							.getResources().getDrawable(
									R.drawable.btn_forward_one_huise));
				}

			}
		}
	}

	public void setPrevEnabled(boolean enabled) {

		if (mPrevButton != null) {
			mPrevButton.setEnabled(enabled);
			if (enabled) {

				if (mCurrentPlayList != null && mCurrentPlayList.size() > 0) {
					
						mPrevButton.setBackgroundDrawable(VideoPlayer.this
								.getResources()
								.getDrawable(R.drawable.btn_back));
					
				} else {
					mPrevButton.setBackgroundDrawable(VideoPlayer.this
							.getResources()
							.getDrawable(R.drawable.btn_back_one));
				}

			} else {
				if (mCurrentPlayList != null && mCurrentPlayList.size() > 0) {
					if (position == 0) {
						mPrevButton.setBackgroundDrawable(VideoPlayer.this
								.getResources().getDrawable(
										R.drawable.video_pre_gray));
					}
				} else {
					mPrevButton.setBackgroundDrawable(VideoPlayer.this
							.getResources().getDrawable(
									R.drawable.btn_back_one_huise));
				}

			}
		}

	}

	public void setPlayOrPauseEnabled(boolean enabled) {
		if (mPlayOrPause != null) {
			mPlayOrPause.setEnabled(enabled && mPlayOrPause != null);

			if (enabled) {
				// mPlayOrPause.setBackgroundDrawable(VideoPlayer.this.getResources().getDrawable(R.drawable.videonextbtn_bg));
			} else {
				if (!isPaused) {
					mPlayOrPause
							.setBackgroundResource(R.drawable.video_puase_gray);

				} else {
					mPlayOrPause.setBackgroundResource(R.drawable.player_btn_play);
				}

			}
		}
	}

	private void setVideoScale(int flag) {

		switch (flag) {
		case SCREEN_FULL:
			mDiaplayMode.setBackgroundResource(R.drawable.btn_original_size);
			Log.d(TAG, "screenWidth: " + screenWidth + " screenHeight: "
					+ screenHeight);
			// mVideoView.setVideoScale(screenWidth, screenHeight);
//			mVideoView.setVideoLayout(VideoView.VIDEO_LAYOUT_ZOOM, 0);
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
			isFullScreen = true;
			break;

		case SCREEN_DEFAULT:

			mDiaplayMode.setBackgroundResource(R.drawable.btn_full_screen);

			int videoWidth = mVideoView.getVideoWidth();
			int videoHeight = mVideoView.getVideoHeight();
			int mWidth = screenWidth;
			int mHeight = screenHeight - 25;

			if (videoWidth > 0 && videoHeight > 0) {
				if (videoWidth * mHeight > mWidth * videoHeight) {

					mHeight = mWidth * videoHeight / videoWidth;
				} else if (videoWidth * mHeight < mWidth * videoHeight) {

					mWidth = mHeight * videoWidth / videoHeight;
				} else {

				}
			}
//			mVideoView.setVideoLayout(VideoView.VIDEO_LAYOUT_SCALE, 0);
			// mVideoView.setVideoScale(mWidth, mHeight);

			getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
			isFullScreen = false;
			break;
		}
	}

	private void hideControllerDelay() {
		mHandler.sendEmptyMessageDelayed(HIDE_CONTROLER, TIME);
	}

	// private void hideTop(){
	// mPlayerTop.setVisibility(View.GONE);
	// }
	//

	private void hideController() {
		LogUtil.e(TAG, " ---hideController()--");

		if (isLoading && isBuffering) {
			frame.setVisibility(View.GONE);
			mFrameLayout.setVisibility(View.GONE);
		} else if (!isLoading && isBuffering) {
			frame.setVisibility(View.GONE);
			mFrameLayout.setVisibility(View.GONE);
		}

		isControllerShow = false;
		// isSoundShow = false;

	}

	private void cancelDelayHide() {
		mHandler.removeMessages(HIDE_CONTROLER);
	}

	private void showController() {
		LogUtil.e(TAG, " ---showController()--");
		if (!isLoading && isBuffering) {
			frame.setVisibility(View.VISIBLE);
			mFrameLayout.setVisibility(View.VISIBLE);
		}

		isControllerShow = true;

	}

	private boolean isBuffering = false;
	private boolean isSoftBuffering = false;
	private boolean isHttp = false;
	private boolean isCheckUriBym3u8 = false;

	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {

			case PROGRESS_CHANGED:

				if (mVideoView == null||isBack) {
					return;
				}

				int i = (int) mVideoView.getCurrentPosition();

				if (isPaused || !isHttp) {
					if (mVideoBuffer != null) {
						mVideoBuffer.setVisibility(View.GONE);

					}
				}

				if (i > 1000)
					mCurrentPosition = i;
				Calendar calendar = Calendar.getInstance();
				// int year = calendar.get(Calendar.YEAR);
				// int month = calendar.get(Calendar.MONTH);
				// int day = calendar.get(Calendar.DAY_OF_MONTH);
				String hourStr = null;
				String minuteStr = null;
				String timeStr = null;
				int hour = calendar.get(Calendar.HOUR_OF_DAY);
				int minute = calendar.get(Calendar.MINUTE);
				int second = calendar.get(Calendar.SECOND);
				if (hour == 0) {
					hourStr = "00";
				} else if (0 < hour && hour < 10) {
					hourStr = "0" + hour;
				} else {
					hourStr = String.valueOf(hour);
				}

				if (minute == 0) {
					minuteStr = "00";
				} else if (0 < minute && minute < 10) {
					minuteStr = "0" + minute;
				} else {
					minuteStr = String.valueOf(minute);
				}

				if (second == 0) {
					timeStr = "00";
				} else if (0 < second && second < 10) {
					timeStr = "0" + second;
				} else {
					timeStr = String.valueOf(second);
				}
				String time = hourStr + ":" + minuteStr + ":" + timeStr;
				mLastModify.setText(time);

				mPlayerSeekBar.setProgress(i);
				if (isHttp) {
					int j = mVideoView.getBufferPercentage();
					int setSecondaryProgress = j * mPlayerSeekBar.getMax()
							/ 100;
					mPlayerSeekBar.setSecondaryProgress(setSecondaryProgress);

				} else {
					mPlayerSeekBar.setSecondaryProgress(0);
				}
				setBattery(level);
				mCurrentTime.setText(stringForTime(i));
				if (!isOnCompletion && !isLoading) {
					SharedPreferences.Editor editor = preference.edit();
					if (editor != null) {

						if (mCurrentPosition > 0 && uri != null) {
							editor.putInt("CurrentPosition", mCurrentPosition);
							if (uri != null) {
								editor.putString("histroyUri", uri.toString());
							}

						}
						editor.commit();
					}

				}
				if (!isBack && !isError) {
					mHandler.removeMessages(PROGRESS_CHANGED);
					mHandler.sendEmptyMessageDelayed(PROGRESS_CHANGED, 1000);
				}

				break;

			case HIDE_CONTROLER:
				hideController();
				break;
			case BUFFERING_TAG:
				isBuffering = true;
				break;
			case PAUSE:
				if (mVideoView != null) {
					mVideoView.pause();
				}
				break;

			case SET_PAUSE_BUTTON:
				setPauseButtonImage();
				break;

			case IS_PAUSE_BUTTON:
				if (isPaused) {
					mVideoView.start();
					mPlayOrPause.setBackgroundResource(R.drawable.player_btn_pause);
					isBuffering = true;
					cancelDelayHide();
					hideControllerDelay();
				} else {
					mVideoView.pause();
					mPlayOrPause.setBackgroundResource(R.drawable.player_btn_play);
					cancelDelayHide();
					showController();
					isBuffering = false;

				}

				isPaused = !isPaused;
				break;

			case SEEK_BACKWARD:
				if (mVideoView != null) {
					int pos = (int) mVideoView.getCurrentPosition();
					Integer times = 10;
					String key_2 = "10";
					if (preference != null) {
//						key_2 = preference.getString(SettingActivity.key_2,
//								"10");
						if (key_2 != null) {
							times = Integer.valueOf(key_2);
						}

					}
					pos -= (times * 1000);
					// pos -= 15000;
					mVideoView.seekTo(pos);
				}
				cancelDelayHide();
				hideControllerDelay();
				break;

			case SEEK_FORWARD:
				if (mVideoView != null) {
					int pos = (int) mVideoView.getCurrentPosition();
					Integer times = 10;
					String key_2 = "10";
					if (preference != null) {
//						key_2 = preference.getString(SettingActivity.key_2,
//								"10");
						if (key_2 != null) {
							times = Integer.valueOf(key_2);
						}

					}

					pos += (times * 1000);
					// pos += 15000;
					mVideoView.seekTo(pos);
				}
				cancelDelayHide();
				hideControllerDelay();
				break;
			case REPLAY:
				replay();
				break;
			case CHANGED_RXBYTES:
				String countCurRate = TrafficStatsUtil.countCurRate();
				if (mLoadingRxBytesText != null && isHttp) {
					mLoadingRxBytesText.setText(countCurRate);
				} else {
					mLoadingRxBytesText.setVisibility(View.GONE);
				}

				if (mVideoBufferText != null && isHttp) {

					mVideoBufferText.setText(countCurRate);
				} else {
					mVideoBufferText.setVisibility(View.GONE);
				}

				if (!isBack ) {
					mHandler.removeMessages(CHANGED_RXBYTES);
					mHandler.sendEmptyMessageDelayed(CHANGED_RXBYTES, 1000);
				}
				break;

			}

			super.handleMessage(msg);
		}
	};

	Handler mExitHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case EXIT_TEXT:
				if (isBack) {
					mLoadingText.setText(VideoPlayer.this.getBaseContext()
							.getResources()
							.getString(R.string.exit_player_text));
					mLoadingBufferingText.setText(VideoPlayer.this
							.getBaseContext().getResources()
							.getString(R.string.exit_player_text));
					if (mVideoBuffer != null) {
						mVideoBuffer.setVisibility(View.GONE);
					}
				}
				mExitHandler.sendEmptyMessage(EXIT);
				break;

			case EXIT:
				exit();
				break;

			}
		}
	};

	private int replayNum = 0;

	private void replay() {

		LogUtil.e(TAG, "replay()--------------");

		isBack = false;
		isError = false;
		isReplay = false;
		isOnCompletion = false;
		isBuffering = true;
		isLoading = true;
		hideController();
		if (mVideoBuffer != null)
			mVideoBuffer.setVisibility(View.GONE);
		if (mPlayerLoading != null) {
			mPlayerLoading.setVisibility(View.VISIBLE);
		}
	   startPlay();

	}

	private int mAudioMax;
	private int mAudioDisplayRange;
	private float mTouchY, mVol;
	private boolean mIsAudioChanged;
	private String[] mAudioTracks;

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		if (mAudioDisplayRange == 0)
			mAudioDisplayRange = Math.min(getWindowManager()
					.getDefaultDisplay().getWidth(), getWindowManager()
					.getDefaultDisplay().getHeight());

		switch (event.getAction()) {

		case MotionEvent.ACTION_DOWN:
			mTouchY = event.getY();
			mVol = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
			mIsAudioChanged = false;
			break;

		case MotionEvent.ACTION_MOVE:
			float y = event.getY();

			int delta = (int) (((mTouchY - y) / mAudioDisplayRange) * mAudioMax);
			int vol = (int) Math.min(Math.max(mVol + delta, 0), mAudioMax);
			if (delta != 0) {
				updateVolume(vol);
				// mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, vol,
				// AudioManager.FLAG_SHOW_UI);
				mIsAudioChanged = true;
			}
			break;

		case MotionEvent.ACTION_UP:
			if (!mIsAudioChanged) {
				if (!isControllerShow) {
					isControllerShow = false;
					showController();
					cancelDelayHide();
					hideControllerDelay();
				} else {
					isControllerShow = true;
					hideController();
					cancelDelayHide();
				}
			}
			break;
		}
		return mIsAudioChanged;

	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		Log.v(TAG, " onConfigurationChanged()");

		getScreenSize();
		if (isControllerShow) {
			hideController();
			showController();
			cancelDelayHide();
			hideControllerDelay();
		}

		super.onConfigurationChanged(newConfig);
	}

	private void updateVolume(int index) {
		LogUtil.i(TAG, "updateVolume==" + index + "----------currentVolume="
				+ currentVolume);
		if (mAudioManager != null) {
			if (isSilent) {
				mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
				mSeekBarvolume.setProgress(0);
			} else {
				mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, index,
						0);
				mSeekBarvolume.setProgress(index);
				if (index == 0) {
				} else {
				}

			}
			currentVolume = index;
		}
	}

	private void getScreenSize() {
		LogUtil.e(TAG, " ---getScreenSize()--");
		Display display = getWindowManager().getDefaultDisplay();
		screenHeight = display.getHeight();
		screenWidth = display.getWidth();

	}

	private AlertDialog alertDialog = null;
	AlertDialog.Builder aler = null;

	private void ConfirmExit() {// 退出确认
		aler = new AlertDialog.Builder(VideoPlayer.this);
		aler.setTitle("提示");

		if (uri != null && isLocal) {

			if (!isOnCompletion) {
				setErrorTyp(errorType);
				aler.setNegativeButton("确定", new OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						alertDialog.hide();
//						finish();
						mExitHandler.sendEmptyMessage(EXIT);
						alertDialog = null;

					}
				});

			}
		}

		if (uri != null && !isLocal) {

			if (isOnCompletion) {
				aler.setMessage(getString(R.string.play_comper));
				aler.setPositiveButton("退出",
						new DialogInterface.OnClickListener() {// 退出按钮

							public void onClick(DialogInterface dialog, int i) {

								alertDialog.hide();
//								finish();
								mExitHandler.sendEmptyMessage(EXIT);
								alertDialog = null;

							}
						});
				aler.setNegativeButton("进入", new OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent();
						intent.setClass(VideoPlayer.this, HomeActivity.class);
						startActivity(intent);
						overridePendingTransition(R.anim.fade, R.anim.hold);
						alertDialog.hide();
//						finish();
						mExitHandler.sendEmptyMessage(EXIT);
						alertDialog = null;

					}
				});
			} else {
				aler.setPositiveButton("退出",
						new DialogInterface.OnClickListener() {// 退出按钮

							public void onClick(DialogInterface dialog, int i) {
								alertDialog.hide();
//								finish();
								mExitHandler.sendEmptyMessage(EXIT);
								alertDialog = null;

							}
						});
				setErrorTyp(errorType);
				aler.setNegativeButton("进入", new OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {

						Intent intent = new Intent();
						intent.setClass(VideoPlayer.this, HomeActivity.class);
						startActivity(intent);
						overridePendingTransition(R.anim.fade, R.anim.hold);
						alertDialog.hide();
//						finish();
						mExitHandler.sendEmptyMessage(EXIT);
						alertDialog = null;

					}
				});
			}

		}
		if (alertDialog == null) {
			alertDialog = aler.create();
		}
		if (alertDialog != null && !alertDialog.isShowing()) {
			alertDialog.show();
		}

	}

	private void setErrorTyp(int errorType) {
		switch (errorType) {

		case MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK:
			aler.setMessage("抱歉，该视频无法拖动！");
			break;

		case MediaPlayer.MEDIA_ERROR_UNKNOWN:

			aler.setMessage("抱歉，播放出错了!");

			break;
		case MediaPlayer.MEDIA_INFO_VIDEO_TRACK_LAGGING:
			aler.setMessage("抱歉，解码时出现");
			break;

		default:
			aler.setMessage("抱歉，该视频无法播放！");
			break;
		}
	}




	private void exit() {
		Log.v(TAG, "exit())");
		if(isBack){
			PlayerUtils.setTip3GNework(VideoPlayer.this, false); 
		}else{
			PlayerUtils.setTip3GNework(VideoPlayer.this, true);
		}
		if(mVideoView != null){
			if(isBack){
				mVideoView.pause();
			}else{
				mVideoView.pause();
			}
		}
		finish();
		overridePendingTransition(R.anim.fade, R.anim.hold);
	}
	

	private boolean isError = false;

	@Override
	protected void onPause() {
		Log.v(TAG, " onPause()");

		if (mHandler != null && radia == null) {
			mHandler.sendEmptyMessage(PAUSE);
		}

		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onResume() {
		isBack = false;
		LogUtil.v(TAG, "onResume()");
		if (mVideoView != null && mVideoView.isPlaying()) {
			showController();
			cancelDelayHide();
			hideControllerDelay();
		} else if (!isCheckUriBym3u8 && mVideoView != null) {
			if (mCurrentPosition > 1000) {
				// mVideoView.seekTo(mCurrentPosition);
				mVideoView.start();
			}

			showController();
			cancelDelayHide();
			hideControllerDelay();
		}
		super.onResume();
		 MobclickAgent.onResume(this);
	}

	@Override
	protected void onDestroy() {
		LogUtil.v(TAG, " onDestroy()");
		if (checkVitamioLibs) {
			unregisterReceiver(batteryReceiver);
			unregisterListenerNet();
			mHandler.removeMessages(PROGRESS_CHANGED);
			mHandler.removeMessages(HIDE_CONTROLER);
		}
 
		super.onDestroy();
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		LogUtil.v(TAG, " onRestart()");
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		LogUtil.v(TAG, "onSaveInstanceState()");
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

	}

	@Override
	protected void onStop() {

		super.onStop();
//		if (mVideoView != null) {
//			mVideoView.stop();
//		}
		LogUtil.e(TAG, "onStop()");
	}

	private boolean isBack = false;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			if (!isBack) {
				// ActivityHolder.getInstance().removeActivity(this);
				isBack = true;
				mExitHandler.removeMessages(EXIT_TEXT);
				mExitHandler.sendEmptyMessage(EXIT_TEXT);

			}
			return true;

		}
		if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
			if (currentVolume >= 1) {
				currentVolume--;
			}
			updateVolume(currentVolume);

			return super.onKeyDown(keyCode, event);

		} else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {

			if (currentVolume < 15) {
				currentVolume++;
			}

			updateVolume(currentVolume);

			return super.onKeyDown(keyCode, event);

		}
		return false;
	}

	 

}
