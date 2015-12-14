package cn.hi321.browser.player;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Formatter;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnSeekCompleteListener;
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
import cn.hi321.browser.media.entity.Media;
import cn.hi321.browser.media.entity.MediaItem;
import cn.hi321.browser.player.VideoView.MySizeChangeLinstener;
import cn.hi321.browser.ui.activities.HomeActivity;
import cn.hi321.browser.utils.LogUtil;
import cn.hi321.browser.utils.Utils;
import cn.hi321.browser2.R;

import com.umeng.analytics.MobclickAgent;


/**
 * 
 * @author yangguangfu
 * 系统播放器
 */
public class SystemPlayer extends Activity {
	private final static String TAG = "SystemPlayer";

	/**
	 * 播放列表位置
	 */
	private int position;

	private String radia = null;
	
	public static final String netACTION = "android.net.conn.CONNECTIVITY_CHANGE";

	private int mCurrentPosition = 0;

	private VideoView mVideoView = null;

	private SeekBar mPlayerSeekBar = null;

	private SeekBar mSeekBarvolume = null;

	private TextView mEndTime = null;
	private TextView mCurrentTime = null;

	private TextView mLoadingText = null;
	
	private TextView mLoadingRxBytesText = null;

	private TextView mLoadingBufferingText = null;

	private TextView mVideoName = null;
	private TextView mLoadingVideoName = null;

	private ImageView mBatteryState = null;

	private TextView mLastModify = null;


	private AudioManager mAudioManager = null;

	private int currentVolume = 0;
	private Button mDiaplayMode = null;
	private Button mPrevButton = null;
	private Button mPlayOrPause = null;
	private Button mNextButton = null;
	private Button mPlayerVolume = null;

	private static int screenWidth = 0;
	private static int screenHeight = 0;
	private static int controlViewHeight = 0;
	private final static int TIME = 6868;
	private boolean isControllerShow = true;
	private boolean isPaused = false;
	private boolean isHttp = false;
	private boolean isCheckUriBym3u8 = false;
	private boolean isFullScreen = false;
	private boolean isSilent = false;

	// private boolean isOnline = false;

	private boolean isOnCompletion = false;

	private final static int SCREEN_FULL = 0;
	private final static int SCREEN_DEFAULT = 1;

	private final static int HIDE_CONTROLER = 1;

	private final static int PAUSE = 3;

	private final static int EXIT_REPORTE = 4;

	private final static int EXIT_TEXT = 5;
	
	private final static int PROGRESS_CHANGED = 0;

	private final static int BUFFER = 6;

	private final static int BUFFERING_TAG = 7;

	private final static int EXIT = 8;

	private final static int SET_PAUSE_BUTTON = 9;

	private final static int IS_PAUSE_BUTTON = 10;

	private final static int SEEK_BACKWARD = 11;

	private final static int SEEK_FORWARD = 12;

	private final static int IS_INSTALLED = 13;

	private final static int REPLAY = 14;
	
	private final static int CHANGED_RXBYTES = 15;

	private Intent mIntent;

	private Uri uri;

	private Button mBtnSetplay = null;

	private Button mPlayerButtonBack = null;

	private StringBuilder mFormatBuilder;
	private Formatter mFormatter;

	private LinearLayout frame = null;
	private FrameLayout mFrameLayout = null;

	private LinearLayout mPlayerLoading;

	private LinearLayout mVideoBuffer;

    private TextView mVideoBufferText = null;

	private boolean isLocal = false;

	private String mMtype = null;


	private String currentLanguage = null;

	private String mByPlayHistory = null;

	private boolean isLoading = true;

	private int fristBufferOk = -1;

	private long mIndexStartTime = 0;

	private int errorType = 0;

	private int level = 0;
	
	private boolean mIsLive = false;
	private BroadcastReceiver batteryReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			level = intent.getIntExtra("level", 0);
			// level加%就是当前电量了
		}
	};

	private boolean isSendURi = false;
	private boolean softecoaded = false;

	private MediaItem videoInfo = null;
	private Media mMedia = null;
	private String filePath = null;
	private SharedPreferences preference = null;
	private int histroyPosition = 0;
	private String histroyUri = null;
	private String[] netUris = null;
	private String[] loacaUris = null;

	private boolean isTrue = false;
	private boolean isAutoNext = false;

	private boolean isNotNet = false;

	private ArrayList<MediaItem> mCurrentPlayList;
	
	private NetCheckReceiver mCheckReceiver;
	private boolean isNetAvailable = true;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		
		initWindow();

		initData();
		
		initView();
		
		mIntent = getIntent();
		
		mMedia = (Media) mIntent.getSerializableExtra("media");
		if(mMedia != null){
			firstStartMediaPlay();
		}else{
			getPlayData();
			getScreenSize();
			initVideoView();
			firstStartPlaye();
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
		getNextUri();
		checkUri();
		setButtonState();
		startPlay();
	}
	
	private void playNextReset(){
		isOnCompletion = false;
		isBuffering = true;
		isBack = false;
		isError = false;
		isLoading = true;
		mCurrentPosition = 0;
		Utils.isErrorNum = false;
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
	
	private void checkNetworkInfo()
    {
        ConnectivityManager conMan = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        State mobile = conMan.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
        State wifi = conMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
        
        if(mobile==State.CONNECTED||mobile==State.CONNECTING){
        	
        	 if(uri!=null&&isHttp&&PlayerUtils.getTip3GNework(SystemPlayer.this)&&isNetAvailable) {
        		 Toast.makeText(SystemPlayer.this, getString(R.string.net_3g), 1).show(); 
        	 }
        	 isNetAvailable = true;
        	 return;
        }
           
        if(wifi==State.CONNECTED||wifi==State.CONNECTING){
        	 isNetAvailable = true;
        	 return;
        }
        
            isNetAvailable = false;
        //添加对本地文件的判断，播放本地文件时不应提示网络中断
        if(uri!=null&&isHttp&&fristBufferOk==0) {
        	Toast.makeText(SystemPlayer.this, getString(R.string.net_outage_tip), 1).show();
        }
        
    }
	
    class NetCheckReceiver extends BroadcastReceiver{
        
        //android 中网络变化时所发的Intent的名字
      
        @Override
        public void onReceive(Context context, Intent intent){
            if(intent.getAction().equals(netACTION)){
                LogUtil.e(TAG, "ACTION:" + intent.getAction());
                checkNetworkInfo();
            }
       
      }
    }
	

	private void startVitamio() {
		boolean key_1 = getHistroyPosition();
		if (key_1 && !isHttp && isLocal) {
			startVideoPlayer();
//			return;
		} 
		
		
	}

	private void initVideoView() {
		
		
		mVideoView.setOnSeekCompleteListener(new OnSeekCompleteListener() {
			@Override
			public void onSeekComplete(MediaPlayer mp) {
				if (mVideoBuffer != null) {
					mVideoBuffer.setVisibility(View.GONE);
				}
				
				
			}
		});
		
		
		mVideoView.setOnErrorListener(new OnErrorListener() {

			public boolean onError(MediaPlayer mp, int what, int extra) {

				isError = true;
				
				if(!isCheckUriBym3u8&&fristBufferOk == 0 &&replayNum<3&&mCurrentPosition > 0){
					replayNum++;
					replay();
					return true;
				}
				
				if (isReplay) {
					return true;
				}
				
				if(fristBufferOk == 0 && mCurrentPosition > 0&&!isReplay) {
					isReplay = true;
					retryDialog();
					return true;
				}

				if (isSendURi) {
					return true;
				}

				if (isError) {
					if (mVideoBuffer != null) {
						mVideoBuffer.setVisibility(View.GONE);
					}
				}
				errorType = what;
				LogUtil.i(TAG, "---onError----fristBufferOk============"
						+ fristBufferOk);
				softecoaded = true;//Utils.checkAPPEcoder(SystemPlayer.this);// checkBrowser("io.vov.vitamio.v7vfpv3");

				LogUtil.i(TAG, "---onError----softecoaded============"
						+ softecoaded);
				LogUtil.e(TAG, "---onError---uri=====" + uri);

				if (uri != null) {
					if (!isSendURi && softecoaded) {
						isSendURi = true;
						startVideoPlayer();
						return true;
					}
				}

					
				LogUtil.e(TAG, "onError: " + what + "," + extra);
				mHandler.removeMessages(SET_PAUSE_BUTTON);
				mHandler.sendEmptyMessage(SET_PAUSE_BUTTON);
				return true;

			}

		});

		mVideoView.setOnBufferingUpdateListener(new OnBufferingUpdateListener() {

					public void onBufferingUpdate(MediaPlayer arg0,
							int bufferingProgress) {

					}
				});

		mVideoView.setOnInfoListener(new OnInfoListener() {

			public boolean onInfo(MediaPlayer mp, int what, int extra) {
				
				switch (what) {
				case MediaPlayer.MEDIA_INFO_BUFFERING_START:
				  if(Utils.getOSVersionSDKINT(SystemPlayer.this)>=9){
						if (mVideoBuffer != null) {
							mVideoBuffer.setVisibility(View.VISIBLE);
						}
					}
					break;
					
				case MediaPlayer.MEDIA_INFO_BUFFERING_END:
					 if(Utils.getOSVersionSDKINT(SystemPlayer.this)>=9){
						if (mVideoBuffer != null) {
							mVideoBuffer.setVisibility(View.GONE);
						}
					 }
					break;

				default:
					break;
				}

				return true;
			}
		});

		mVideoView.setMySizeChangeLinstener(new MySizeChangeLinstener() {

			public void doMyThings() {
				setVideoScale(SCREEN_DEFAULT);
			}

		});

		mPlayerSeekBar
				.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

					public void onProgressChanged(SeekBar seekbar,
							int progress, boolean fromUser) {
						if (fromUser) {
							mVideoView.seekTo(progress);
							cancelDelayHide();
							
							if (mVideoBuffer != null&&isHttp) {
								mVideoBuffer.setVisibility(View.VISIBLE);
							}
						}
					}

					public void onStartTrackingTouch(SeekBar arg0) {
						cancelDelayHide();
					}

					public void onStopTrackingTouch(SeekBar seekBar) {
						if (uri != null && !isPaused&&isHttp) {
							isBuffering = false;
							if (mVideoBuffer != null) {
								mVideoBuffer.setVisibility(View.VISIBLE);
							}
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

						LogUtil.i(TAG, "progress��" + progress + "---fromUser="
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
				isLoading = false;
				isBuffering = true;

				getHistroyPosition();

				setVideoScale(SCREEN_DEFAULT);

				if (!isLoading) {
					hideController();
				}

				int i = mVideoView.getDuration();
				Log.d("onCompletion", "" + i);
				mPlayerSeekBar.setMax(i);
				mEndTime.setText(stringForTime(i));

				String netUri = uri.toString();
				LogUtil.i(TAG, "urill===" + netUri);
				String loacaUri = histroyUri;
				LogUtil.i(TAG, "his===" + loacaUri);
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
						}else{
							if(mCurrentPosition > 0)
							  mVideoView.seekTo((int) histroyPosition);
						}
					}

				}

				mVideoView.start();
				mPlayerLoading.setVisibility(View.GONE);
				mVideoBuffer.setVisibility(View.GONE);
				isOnCompletion = false;
				fristBufferOk = 0;
				replayNum = 0;
				mHandler.sendEmptyMessage(SET_PAUSE_BUTTON);
				if(isCheckUriBym3u8){
					showController() ;
				}
				cancelDelayHide();
				hideControllerDelay();

				mHandler.removeMessages(PROGRESS_CHANGED);
				mHandler.sendEmptyMessageDelayed(PROGRESS_CHANGED, 500);
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
					LogUtil.i(TAG,
							"onCompletion()===============+isOnCompletion");
					isBuffering = false;
					if (uri != null) {
						if (preference != null) {
							SharedPreferences.Editor editor = preference.edit();
							if (editor != null) {

								if (mCurrentPosition > 0) {
									editor.putInt("CurrentPosition", 0);
									if (uri != null) {
										editor.putString("histroyUri", uri
												.toString());
									}

								} else {
									editor.putInt("CurrentPosition", 0);
									if (uri != null) {
										editor.putString("histroyUri", uri
												.toString());
									}

								}
								editor.commit();
							}

						}

						if (!isLocal) {
							ConfirmExit();
						} else {
								if(isCheckUriBym3u8&&mIsLive){
								
								if(replayNum<=3&&fristBufferOk == 0){
									if (mHandler != null) {
										mHandler.removeMessages(REPLAY);
										mHandler.sendEmptyMessage(REPLAY);
									} else {
										replay();
									}
									replayNum++;
									return ;
								}
								
								
								if(isReplay){
									return ;
								}
								//在出错之前从新更新一遍用户存储播放历史的方法
//								updateByHashid();
								if(fristBufferOk == 0 &&!isReplay) {
									isReplay = true;
									retryDialog();
									return ;
								}
								
							}else{
								
								if (!isAutoNext) {
									if (mCurrentPlayList != null
											&& mCurrentPlayList.size() > 1) {

										int n = mCurrentPlayList.size();
										if (++position < n) {
											playNextVideo();
										} else {
											// Toast.makeText(SystemPlayer.this, "",
											// 0).show();
											--position;
											// finish();
											mExitHandler.sendEmptyMessage(EXIT);

										}

									} else {

										if (isHttp) {
											if (mVideoView != null) {
												// finish();
//												mVideoView.pause();
												mExitHandler.sendEmptyMessage(EXIT);
											}

										} else {
											if (mVideoView != null) {
												// finish();
//												mVideoView.pause();
												mExitHandler.sendEmptyMessage(EXIT);
											}

										}

									}
								} else {
//									finish();
									mExitHandler.sendEmptyMessage(EXIT);
								}
								
								
								
							}
							
							
							
						}

						// 

					}
				}
			}
		});
	}

	private void initWindow() {

		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		setContentView(R.layout.system_player);
	}

	private void getPlayData() {
		if (mIntent != null) {
			uri = mIntent.getData();
			if (uri != null) {
				filePath = uri.toString();
				String name = Utils.getFileName(uri.toString());
				mVideoName.setText(name);
				mLoadingVideoName.setText(name);
				mLoadingVideoName.setVisibility(View.GONE);
				isLocal = false;

			} else {
				isLocal = true;
			}
			mCurrentPlayList = (ArrayList<MediaItem>) mIntent
					.getSerializableExtra("MediaIdList");
			position = mIntent.getIntExtra("CurrentPosInMediaIdList", 0);
			radia = mIntent.getStringExtra("radia");
			videoInfo = (MediaItem) mIntent.getSerializableExtra("VideoInfo");
			getUri();
			checkUri();

		}
		setButtonState();
		
	}


	private void checkUri() {
		if (uri != null) {
			isHttp = Utils.checkUri(SystemPlayer.this, uri);
			isCheckUriBym3u8 = Utils.isCheckUriByM3u8(SystemPlayer.this, uri);
		}
	}


	private void getUri() {
		String strLocaluri = null;
		if (videoInfo != null) {
			strLocaluri = videoInfo.getUrl();
			mIsLive = videoInfo.isLive();
		}
		if (strLocaluri == null && mCurrentPlayList != null
				&& mCurrentPlayList.size() > 0) {
			videoInfo = mCurrentPlayList.get(position);
			strLocaluri = videoInfo.getUrl();
			mIsLive = videoInfo.isLive();
		}

		if (strLocaluri != null) {
			filePath = strLocaluri;
			String name = Utils.getFileName(strLocaluri);
			uri = Uri.parse(strLocaluri);

			if (mCurrentPlayList != null
					&& mCurrentPlayList.size() > 1) {
				mLoadingVideoName.setText(mCurrentPlayList.get(position)
						.getTitle());
				mVideoName.setText(mCurrentPlayList.get(position)
						.getTitle());
			} else {
				if (videoInfo != null) {
					mLoadingVideoName.setText(videoInfo.getTitle());
					mVideoName.setText(videoInfo.getTitle());
				} else {
					mLoadingVideoName.setText(name);
					mVideoName.setText(name);
				}
			}

		}
		
		if (uri != null) {
			String content = uri.toString().replace("?", "yangguangfu");
			if (content != null)
				netUris = content.split("yangguangfu");
			if (isCheckUriBym3u8 && mCurrentPlayList != null
					&& mCurrentPlayList.size() > 1) {
				mLoadingVideoName.setText(mCurrentPlayList.get(position)
						.getTitle());
			}
		}
	}
	
	private void getNextUri() {
		String strLocaluri = null;
		if (strLocaluri == null && mCurrentPlayList != null
				&& mCurrentPlayList.size() > 0) {
			videoInfo = mCurrentPlayList.get(position);
			strLocaluri = videoInfo.getUrl();
			mIsLive = videoInfo.isLive();
		}

		if (strLocaluri != null) {
			filePath = strLocaluri;
			String name = Utils.getFileName(strLocaluri);
			uri = Uri.parse(strLocaluri);

			if (mCurrentPlayList != null
					&& mCurrentPlayList.size() > 1) {
				mLoadingVideoName.setText(mCurrentPlayList.get(position)
						.getTitle());
				mVideoName.setText(mCurrentPlayList.get(position)
						.getTitle());
			} else {
				if (videoInfo != null) {
					mLoadingVideoName.setText(videoInfo.getTitle());
					mVideoName.setText(videoInfo.getTitle());
				} else {
					mLoadingVideoName.setText(name);
					mVideoName.setText(name);
				}
			}

		}
		
		if (uri != null) {
			String content = uri.toString().replace("?", "yangguangfu");
			if (content != null)
				netUris = content.split("yangguangfu");
			if (isCheckUriBym3u8 && mCurrentPlayList != null
					&& mCurrentPlayList.size() > 1) {
				mLoadingVideoName.setText(mCurrentPlayList.get(position)
						.getTitle());
			}
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

	private void initData() {
		mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

		currentVolume = mAudioManager
				.getStreamVolume(AudioManager.STREAM_MUSIC);

		mAudioMax = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

		LogUtil.v(TAG, "onCreate()");

		preference = PreferenceManager.getDefaultSharedPreferences(this);

		LogUtil.v(TAG, "The main thread id = " + Thread.currentThread().getId()
				+ "\n");

		registerReceiver(batteryReceiver, new IntentFilter(
				Intent.ACTION_BATTERY_CHANGED));
		
		regListenerNet();
		
		isOnCompletion = false;
		mFormatBuilder = new StringBuilder();
		mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
	}

	private boolean getHistroyPosition() {
		boolean key_1 = false;
		if (preference != null) {
//			key_1 = preference.getBoolean(SettingActivity.key_1, false);
//			isAutoNext = preference.getBoolean(SettingActivity.key_5, false);
			histroyUri = preference.getString("histroyUri", null);
			histroyPosition = preference.getInt("CurrentPosition", 0);
			if (histroyUri != null) {
				String content = histroyUri.replace("?", "yangguangfu");
				if (content != null)
					loacaUris = content.split("yangguangfu");
			}

		}
		return key_1;
	}

	private Builder dialogBuilder;
	private boolean isReplay = false;
	
	private int replayNum = 0;

	private void retryDialog() {
		dialogBuilder = getBuilderInstance();
		if (dialogBuilder != null) {
			dialogBuilder.setTitle(R.string.tips);
			if (!isNetAvailable){
				dialogBuilder.setMessage(R.string.playretry_neterror);
			}else{
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
			dialogBuilder = new Builder(SystemPlayer.this);
		}
		return dialogBuilder;
	}

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

	private void replay() {

		LogUtil.e(TAG, "replay()--------------");
		isBack = false;
		isError = false;
		isOnCompletion = false;
		isReplay = false;
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

	private boolean isUseVideoPlayer = true;

	private void startVideoPlayer() {

		if (!isUseVideoPlayer) {
			return;
		}
		try {
			if (mVideoView != null) {
				mVideoView.exitPlayer(true);
			}
		} catch (Exception e) {
		}
		Intent intent = new Intent(SystemPlayer.this, VideoPlayer.class);
		if (uri != null) {
			if (!isLocal) {
				intent.setData(uri);

			} else {
				if (mCurrentPlayList != null && mCurrentPlayList.size() > 1) {
					Bundle mBundle = new Bundle();
					mBundle
							.putSerializable("MediaIdList",
									mCurrentPlayList);
					intent.putExtras(mBundle);
					intent.putExtra("CurrentPosInMediaIdList", position);
					if (radia != null) {
						intent.putExtra("radia", "radia");
					}

				} else {
					Bundle mBundle = new Bundle();
					if(videoInfo != null)
					mBundle.putSerializable("VideoInfo", videoInfo);
					intent.putExtras(mBundle);
				}

			}

		}else{
			Bundle mBundle = new Bundle();
			mBundle.putSerializable("media", mMedia);
			intent.putExtras(mBundle); 
		}

		SystemPlayer.this.startActivity(intent);
		overridePendingTransition(R.anim.fade, R.anim.hold);
		if(mExitHandler != null){
			 isBack = true;
			 mExitHandler.sendEmptyMessageDelayed(EXIT, 500);
		}
		

	}

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
		 
		mPlayerLayout = (RelativeLayout)findViewById(R.id.playe_layout);

		frame = (LinearLayout) findViewById(R.id.frame);

		mFrameLayout = (FrameLayout) findViewById(R.id.mFrameLayout);

		mPlayerLoading = (LinearLayout) findViewById(R.id.player_loading);

		mVideoBuffer = (LinearLayout) findViewById(R.id.video_buffer);
		mVideoBufferText = (TextView) findViewById(R.id.mediacontrolle_buffer_info_text);
		

		mVideoView = (VideoView) findViewById(R.id.video_view);

		mLoadingText = (TextView) findViewById(R.id.loading_text);
		
		mLoadingRxBytesText = (TextView) findViewById(R.id.loading_rxBytes_text);
		

		mLoadingBufferingText = (TextView) findViewById(R.id.loading_text);

		mLoadingVideoName = (TextView) findViewById(R.id.loading_video_name);

		mVideoName = (TextView) findViewById(R.id.video_name);

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
			mPlayerVolume.setBackgroundDrawable(SystemPlayer.this
					.getResources().getDrawable(R.drawable.btn_voice));
		} else {
			mPlayerVolume.setBackgroundDrawable(SystemPlayer.this
					.getResources().getDrawable(R.drawable.btn_voice));
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

		// hideFoot();
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
					mExitHandler.removeMessages(EXIT_TEXT);
					mExitHandler.sendEmptyMessage(EXIT_TEXT);
				}

				break;
			case R.id.btn_back:
				if (mCurrentPlayList != null && mCurrentPlayList.size() > 1) {
					isBack = false;
					int n = mCurrentPlayList.size();
					if (--position >= 0 && position < n) {
//						MaindUtils.startSystemPlayer(SystemPlayer.this,
//								mCurrentPlayList, position);
//						mExitHandler.sendEmptyMessage(EXIT);
						playNextVideo();
					} else {
						position = 0;
						playNextVideo();
//						MaindUtils.startSystemPlayer(SystemPlayer.this,
//								mCurrentPlayList, position);
//						mExitHandler.sendEmptyMessage(EXIT);

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

//						MaindUtils.startSystemPlayer(SystemPlayer.this,
//								mCurrentPlayList, position);
//						mExitHandler.sendEmptyMessage(EXIT);
						playNextVideo();
					} else {
						if (position > 0) {
							--position;
						}
						playNextVideo();
//						MaindUtils.startSystemPlayer(SystemPlayer.this,
//								mCurrentPlayList, position);
//						mExitHandler.sendEmptyMessage(EXIT);
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

	private Dialog dialog;
	private boolean isCick;

	private void openSetPlay() {
		try {
			dialog = new Dialog(SystemPlayer.this, R.style.player_dialog_list);
			dialog.setCanceledOnTouchOutside(true);
			dialog.setContentView(R.layout.play_video_detail);
			WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
			lp.alpha = 0.95f; // 0.0-1.0
			dialog.getWindow().setAttributes(lp);

			TextView mlinearLanguage = (TextView) dialog
					.findViewById(R.id.set_player_text);
			mlinearLanguage.setText(getString(R.string.setplay_for_soft));

			Button linearGridView = (Button) dialog
					.findViewById(R.id.set_player);
			linearGridView.setText("软解码播放");
			linearGridView.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {

					if (Utils.getOSVersionSDKINT(SystemPlayer.this) >= 7) {
						if (!isCick) {
							isCick = true;
							LogUtil.i(TAG,
									"---openSetPlay----fristBufferOk============"
											+ fristBufferOk);
							startVideoPlayer();
							if (dialog != null)
								dialog.dismiss();
						}
					} else {
						Utils.netNoPlayeDialog(SystemPlayer.this);
						if (dialog != null)
							dialog.dismiss();
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

	
	private boolean isGotoVitamio = false;
	private void startPlay() {

		if (isBack || isNotNet) {
			return;
		}
		
		if(isCheckUriBym3u8&&!mIsLive){
			startVideoPlayer();
			return;
		}
		if(isGotoVitamio){
			startVideoPlayer();
		}else{
			if (uri != null && mVideoView != null) {
				if (mVideoBuffer != null) {
					mVideoBuffer.setVisibility(View.GONE);
				}
				
				LogUtil.i(TAG, "playUri ===" + String.valueOf(uri));
				mVideoView.setVideoURI(uri);

			}
		}

		
	}

	public void setPlaySeekBarEnabled(boolean enabled) {
		if (mPlayerSeekBar != null) {
			mPlayerSeekBar.setEnabled(enabled && mPlayerSeekBar != null);

		}
	}

	public void setNextEnabled(boolean enabled) {
		if (mNextButton != null) {
			mNextButton.setEnabled(enabled);
			if (enabled) {

				if (mCurrentPlayList != null && mCurrentPlayList.size() > 0) {
						mNextButton.setBackgroundDrawable(SystemPlayer.this
								.getResources().getDrawable(
										R.drawable.btn_forward));
				} else {
					mNextButton.setBackgroundDrawable(SystemPlayer.this
							.getResources().getDrawable(
									R.drawable.btn_forward_one));
				}

			} else {
				if (mCurrentPlayList != null && mCurrentPlayList.size() > 0) {
					if (position == (mCurrentPlayList.size() - 1)) {
						mNextButton.setBackgroundDrawable(SystemPlayer.this
								.getResources().getDrawable(
										R.drawable.video_next_btn_bg));
					}
				} else {
					mNextButton.setBackgroundDrawable(SystemPlayer.this
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
						mPrevButton.setBackgroundDrawable(SystemPlayer.this
								.getResources()
								.getDrawable(R.drawable.btn_back));
				} else {
					mPrevButton.setBackgroundDrawable(SystemPlayer.this
							.getResources()
							.getDrawable(R.drawable.btn_back_one));
				}

			} else {
				if (mCurrentPlayList != null && mCurrentPlayList.size() > 0) {
					if (position == 0) {
						mPrevButton.setBackgroundDrawable(SystemPlayer.this
								.getResources().getDrawable(
										R.drawable.video_pre_gray));
					}
				} else {
					mPrevButton.setBackgroundDrawable(SystemPlayer.this
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
			mVideoView.setVideoScale(screenWidth, screenHeight);
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

			mVideoView.setVideoScale(mWidth, mHeight);

			getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
			isFullScreen = false;
			break;
		}
	}

	private void hideControllerDelay() {
		mHandler.sendEmptyMessageDelayed(HIDE_CONTROLER, TIME);
	}

	

	private void hideController() {

		
		if (isLoading && isBuffering) {
			frame.setVisibility(View.GONE);
			mFrameLayout.setVisibility(View.GONE);
		} else if (!isLoading && isBuffering) {
			frame.setVisibility(View.GONE);
			mFrameLayout.setVisibility(View.GONE);
		}

		isControllerShow = false;

	}

	private AlertDialog alertDialog = null;
	private AlertDialog.Builder aler = null;

	private void ConfirmExit() {// 退出确认
		aler = new AlertDialog.Builder(SystemPlayer.this);
		aler.setTitle("提示");

		if (uri != null && isLocal) {

			if (!isOnCompletion) {
				setErrorTyp(errorType);
				aler.setNegativeButton("确定", new OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						alertDialog.hide();
						isBack = true;
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
								isBack = true;
								mExitHandler.sendEmptyMessage(EXIT);
								alertDialog = null;

							}
						});
				aler.setNegativeButton("进入", new OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent();
						intent.setClass(SystemPlayer.this, HomeActivity.class);
						startActivity(intent);
						overridePendingTransition(R.anim.fade, R.anim.hold);
						alertDialog.hide();
						isBack = true;
						mExitHandler.sendEmptyMessage(EXIT);
						alertDialog = null;

					}
				});
			} else {
				aler.setPositiveButton("退出",
						new DialogInterface.OnClickListener() {// 退出按钮

							public void onClick(DialogInterface dialog, int i) {
								alertDialog.hide();
								isBack = true;
								mExitHandler.sendEmptyMessage(EXIT);
								alertDialog = null;

							}
						});
				setErrorTyp(errorType);
				aler.setNegativeButton("进入", new OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {

						Intent intent = new Intent();
						intent.setClass(SystemPlayer.this, HomeActivity.class);
						startActivity(intent);
						overridePendingTransition(R.anim.fade, R.anim.hold);
						alertDialog.hide();
						isBack = true;
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

	private void cancelDelayHide() {
		mHandler.removeMessages(HIDE_CONTROLER);
	}

	private void showController() {

		if (!isLoading && isBuffering) {
			frame.setVisibility(View.VISIBLE);
			mFrameLayout.setVisibility(View.VISIBLE);
		}

		isControllerShow = true;

	}

	private boolean isBuffering = false;

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {

			case PROGRESS_CHANGED:

				if (mVideoView == null||isBack) {
					return;
				}
				
				
				int i = mVideoView.getCurrentPosition();

				LogUtil.i(TAG, "--mVideoView.getCurrentPosition()==...."
						+ i);
				
				if(Utils.getOSVersionSDKINT(SystemPlayer.this)<9){
					if (uri != null && !isBack && isBuffering && !isPaused
							&& !isError && isHttp) {
						int isBuffer = i - mCurrentPosition;
						LogUtil.i(TAG, "--buffering....");

						if (isBuffer > -500 && isBuffer < 500) {
							if (mVideoBuffer != null) {
								mVideoBuffer.setVisibility(View.VISIBLE);
							}

						} else {
							if (mVideoBuffer != null) {
								mVideoBuffer.setVisibility(View.GONE);
							}
						}

					} else {
						if (isPaused || !isHttp) {
							if (mVideoBuffer != null) {
								mVideoBuffer.setVisibility(View.GONE);

							}
						}

					}
				}
				if (isPaused || !isHttp) {
					if (mVideoBuffer != null) {
						mVideoBuffer.setVisibility(View.GONE);

					}
				}
				
				
				if (i > 0)
					mCurrentPosition = i;

				Calendar calendar = Calendar.getInstance();
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

				if (preference != null && !isOnCompletion && fristBufferOk == 0) {
					SharedPreferences.Editor editor = preference.edit();
					if (editor != null) {

						if (mCurrentPosition > 0 && uri != null) {
							editor.putInt("CurrentPosition", mCurrentPosition);
							editor.putString("histroyUri", uri.toString());

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
					int pos = mVideoView.getCurrentPosition();
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

					mVideoView.seekTo(pos);
				}
				cancelDelayHide();
				hideControllerDelay();
				break;

			case SEEK_FORWARD:
				if (mVideoView != null) {
					int pos = mVideoView.getCurrentPosition();

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
				if(mLoadingRxBytesText != null&&isHttp){
					mLoadingRxBytesText.setText(countCurRate);
				}else{
					mLoadingRxBytesText.setVisibility(View.GONE);
				}
				
				if(mVideoBufferText != null&&isHttp){
					
					mVideoBufferText.setText(countCurRate);
				}else{
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
					mLoadingText.setText(SystemPlayer.this.getBaseContext()
							.getResources()
							.getString(R.string.exit_player_text));
					mLoadingBufferingText.setText(SystemPlayer.this
							.getBaseContext().getResources().getString(
									R.string.exit_player_text));
					if (mVideoBuffer != null) {
						mVideoBuffer.setVisibility(View.VISIBLE);
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
   /**
    * 调节音量大小
    * @param index
    */
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
		Display display = getWindowManager().getDefaultDisplay();
		screenHeight = display.getHeight();
		screenWidth = display.getWidth();
		controlViewHeight = screenHeight / 4;

	}

	private void setErrorTyp(int errorType) {
		switch (errorType) {
		case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
			aler.setMessage("抱歉，播放器出错了！");
			break;

		case MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK:
			aler.setMessage("抱歉，该视频无法拖动！");
			break;

		case MediaPlayer.MEDIA_ERROR_UNKNOWN:
			aler.setMessage("抱歉，暂时无法播放该视频！");
			break;
		case MediaPlayer.MEDIA_INFO_BAD_INTERLEAVING:
			aler.setMessage("抱歉，该视频文件格式错误！");
			break;
		case MediaPlayer.MEDIA_INFO_VIDEO_TRACK_LAGGING:
			aler.setMessage("抱歉，解码时出现");
			break;

		default:
			aler.setMessage("抱歉，该视频无法播放！");
			break;
		}
	}
	
	private void firstStartMediaPlay() {
		if(!PlayerUtils.getTip3GNework(SystemPlayer.this)&&mMedia != null&&Utils.reportNetType(SystemPlayer.this)==2){
			PlayerUtils.setTip3GNework(SystemPlayer.this, true);
			checkMobileNetDialog();
		}else{
			startVideoPlayer();
		}
	}
	
	private void firstStartPlaye() {
		if(!PlayerUtils.getTip3GNework(SystemPlayer.this)&&uri != null&&Utils.reportNetType(SystemPlayer.this)==2&&Utils.checkUri(SystemPlayer.this, uri)){
			PlayerUtils.setTip3GNework(SystemPlayer.this, true);
			checkMobileNetDialog();
		}else{
			startPlay();
		}
	}

	
	private void exit() {
		Log.v(TAG, "exit())");
		if(isBack){
			PlayerUtils.setTip3GNework(SystemPlayer.this, false); 
		}else{
			PlayerUtils.setTip3GNework(SystemPlayer.this, true);
		}
		if(mVideoView != null){
			mVideoView.exitPlayer(true);
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
		Log.v(TAG, "onResume()");
		isBack = false;
		if (mVideoView != null && mVideoView.isPlaying()) {
			showController();
			cancelDelayHide();
			hideControllerDelay();
		} else if (mVideoView != null) {
			if (mCurrentPosition > 1000) {
//				mVideoView.seekTo(mCurrentPosition);
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
		super.onDestroy();
		Log.v(TAG, " onDestroy()");
		unregisterReceiver(batteryReceiver);
		unregisterListenerNet();
		mHandler.removeMessages(PROGRESS_CHANGED);
		mHandler.removeMessages(HIDE_CONTROLER);
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		Log.v(TAG, " onRestart()");
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		Log.v(TAG, "onSaveInstanceState()");
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Log.v(TAG, "onStart())");

	}

	@Override
	protected void onStop() {

		super.onStop();
		
//		if(mVideoView != null){
//			mVideoView.stop();
//		}
		
		Log.v(TAG, "onStop()");
	}

	private boolean isBack = false;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			if (!isBack) {
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
	
	

	private void checkMobileNetDialog() {
		
		dialogBuilder = getBuilderInstance();
		if(dialogBuilder!=null) {
//			dialogBuilder.setTitle(R.string.tip);
			dialogBuilder.setMessage(R.string.net_is_3g_or_2g);
			
			dialogBuilder.setPositiveButton(R.string.player_sure_3g_tip, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if(mMedia != null){
						startVideoPlayer();
					}else{
						startPlay();
					}
						

				}
			});
			dialogBuilder.setOnKeyListener(new OnKeyListener() {
				
				@Override
				public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
					// TODO Auto-generated method stub
					if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
						isBack = true;
						mExitHandler.removeMessages(EXIT_TEXT);
						mExitHandler.sendEmptyMessage(EXIT_TEXT);

					}
					return true;
				}

				
			});
			
			dialogBuilder.setNegativeButton(R.string.player_exit_3g_tip, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					isBack = true;
					mExitHandler.removeMessages(EXIT_TEXT);
					mExitHandler.sendEmptyMessage(EXIT_TEXT);

				}
			});
			AlertDialog alertDialog  =	dialogBuilder.create();
			if(alertDialog != null){
				alertDialog.setCanceledOnTouchOutside(false);
				alertDialog.show();
			}
		}
	}

	

}
