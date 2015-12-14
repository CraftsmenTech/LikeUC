/*
 * Copyright (C) 2006 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.hi321.browser.player;

import java.io.IOException;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnSeekCompleteListener;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.MediaController;
import android.widget.MediaController.MediaPlayerControl;


/**
 * modify by yanggf
 * Displays a video file.  The VideoView class
 * can load images from various sources (such as resources or content
 * providers), takes care of computing its measurement from the video so that
 * it can be used in any layout manager, and provides various display options
 * such as scaling and tinting.
 */
public class VideoView extends SurfaceView implements MediaPlayerControl {
    private String TAG = "VideoView";
    // settable by the client
    private Uri         mUri;
    private Map<String, String> mHeaders;
    private int         mDuration;

    // all possible internal states
    private static final int STATE_ERROR              = -1;
    private static final int STATE_IDLE               = 0;
    private static final int STATE_PREPARING          = 1;
    private static final int STATE_PREPARED           = 2;
    private static final int STATE_PLAYING            = 3;
    private static final int STATE_PAUSED             = 4;
    private static final int STATE_PLAYBACK_COMPLETED = 5;
    private static final int STATE_SUSPEND            = 6;
    private static final int STATE_RESUME             = 7;
    private static final int STATE_SUSPEND_UNSUPPORTED = 8;

    // mCurrentState is a VideoView object's current state.
    // mTargetState is the state that a method caller intends to reach.
    // For instance, regardless the VideoView object's current state,
    // calling pause() intends to bring the object to a target state
    // of STATE_PAUSED.
    private int mCurrentState = STATE_IDLE;
    private int mTargetState  = STATE_IDLE;

    // All the stuff we need for playing and showing a video
    private SurfaceHolder mSurfaceHolder = null;
    private MediaPlayer mMediaPlayer = null;
    private int         mVideoWidth;
    private int         mVideoHeight;
    private int         mSurfaceWidth;
    private int         mSurfaceHeight;
    private MediaController mMediaController;
    private MediaPlayer.OnCompletionListener mOnCompletionListener;
    private MediaPlayer.OnPreparedListener mOnPreparedListener;
    private MediaPlayer.OnSeekCompleteListener 	mOnSeekCompleteListener;
    /**
	 * 监听器，是否文件里面有空白数据”
	 */
	private MediaPlayer.OnInfoListener mOnInfoListener;
    /**
	 * add by yanggf 监听器，当视频、音频、视频播放前得缓冲
	 */
	private MediaPlayer.OnBufferingUpdateListener mOnBufferingUpdateListener;
	
	/**
	 * add by yanggf 自定义监听器，用于监听屏蔽大小是否改变
	 */
	private MySizeChangeLinstener mMyChangeLinstener;

	/**
	 * add by yanggf 自定义监听器，用于监听屏蔽大小是否改变
	 */
	interface MySizeChangeLinstener {
		void doMyThings();
	}

	
    private int         mCurrentBufferPercentage;
    private OnErrorListener mOnErrorListener;
    private int         mSeekWhenPrepared;  // recording the seek position while preparing
    private boolean     mCanPause;
    private boolean     mCanSeekBack;
    private boolean     mCanSeekForward;
    private int         mStateWhenSuspended;  //state before calling suspend()
    
    //add by yanggf
    private  Context mContext;
    private static String pathMac = null;

    public VideoView(Context context) {
        super(context);
        this.mContext = context;
        initVideoView();
    }

    public VideoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        this.mContext = context;
        initVideoView();
    }

    public VideoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
        initVideoView();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    	
    	//modify by yanggf
        //Log.i("@@@@", "onMeasure");
//        int width = getDefaultSize(mVideoWidth, widthMeasureSpec);
//        int height = getDefaultSize(mVideoHeight, heightMeasureSpec);
//        if (mVideoWidth > 0 && mVideoHeight > 0) {
//            if ( mVideoWidth * height  > width * mVideoHeight ) {
//                //Log.i("@@@", "image too tall, correcting");
//                height = width * mVideoHeight / mVideoWidth;
//            } else if ( mVideoWidth * height  < width * mVideoHeight ) {
//                //Log.i("@@@", "image too wide, correcting");
//                width = height * mVideoWidth / mVideoHeight;
//            } else {
//                //Log.i("@@@", "aspect ratio is correct: " +
//                        //width+"/"+height+"="+
//                        //mVideoWidth+"/"+mVideoHeight);
//            }
//        }
//        //Log.i("@@@@@@@@@@", "setting size: " + width + 'x' + height);
//        setMeasuredDimension(width, height);
    	
    	int width = getDefaultSize(mVideoWidth, widthMeasureSpec);
		int height = getDefaultSize(mVideoHeight, heightMeasureSpec);
		setMeasuredDimension(width, height);
    }

    public int resolveAdjustedSize(int desiredSize, int measureSpec) {
        int result = desiredSize;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize =  MeasureSpec.getSize(measureSpec);

        switch (specMode) {
            case MeasureSpec.UNSPECIFIED:
                /* Parent says we can be as big as we want. Just don't be larger
                 * than max size imposed on ourselves.
                 */
                result = desiredSize;
                break;

            case MeasureSpec.AT_MOST:
                /* Parent says we can be as big as we want, up to specSize.
                 * Don't be larger than specSize, and don't be larger than
                 * the max size imposed on ourselves.
                 */
                result = Math.min(desiredSize, specSize);
                break;

            case MeasureSpec.EXACTLY:
                // No choice. Do what we are told.
                result = specSize;
                break;
        }
        return result;
}
    
    
    /**
	 *  add by yanggf
	 *  设置视频规模,是否全屏
	 * 
	 * @param width
	 * @param height
	 */
	public void setVideoScale(int width, int height) {
		LayoutParams lp = getLayoutParams();
		lp.height = height;
		lp.width = width;
		setLayoutParams(lp);
	}

    private void initVideoView() {
        mVideoWidth = 0;
        mVideoHeight = 0;
        getHolder().addCallback(mSHCallback);
        getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        setFocusable(true);
        setFocusableInTouchMode(true);
        requestFocus();
        mCurrentState = STATE_IDLE;
        mTargetState  = STATE_IDLE;
    }
    

    public void setVideoPath(String path) {
        setVideoURI(Uri.parse(path));
    }

    public void setVideoURI(Uri uri) {
        setVideoURI(uri, null);
    }

    /**
     * @hide
     */
    public void setVideoURI(Uri uri, Map<String, String> headers) {
        mUri = uri;
        mHeaders = headers;
        mSeekWhenPrepared = 0;
        openVideo();
        requestLayout();
        invalidate();
    }

    public void stop() {
    	try{
    		 if (mMediaPlayer != null) {
    	            mMediaPlayer.stop();
    	            mMediaPlayer.release();
    	        }
    	}catch (Exception e) {
			e.printStackTrace();
		}
       
    }
    //add by yanggf
    public synchronized void exitPlayer(boolean b) {
   	 try{
   		if (mMediaPlayer != null) {
   			mMediaPlayer.setOnPreparedListener(null);
            mMediaPlayer.setOnVideoSizeChangedListener(null);
            mMediaPlayer.setOnCompletionListener(null);
            mMediaPlayer.setOnErrorListener(null);
            mMediaPlayer.setOnBufferingUpdateListener(null);
			mMediaPlayer.setOnInfoListener(null);
			mMediaPlayer.setOnSeekCompleteListener(null);
   			    if(b){
   			    	new Thread(new Runnable() {
						@Override
						public void run() {
						 if (mMediaPlayer != null) 
							mMediaPlayer.release();
		    	            mMediaPlayer = null;
		    	            mCurrentState = STATE_IDLE;
						}
					}).start();
   			      
   			    }
   	           
   	        }
   		}catch (Exception e) {
   			e.printStackTrace();
   		}
       
   }
    

   
    public void stopPlayback() {
    	try{
    		 if (mMediaPlayer != null) {
    	            mMediaPlayer.stop();
    	            mMediaPlayer.release();
    	            mMediaPlayer = null;
    	            mCurrentState = STATE_IDLE;
    	            mTargetState  = STATE_IDLE;
    	        }
    	}catch (Exception e) {
			e.printStackTrace();
		}
       
    }

    private void openVideo() {
        if (mUri == null || mSurfaceHolder == null) {
            // not ready for playback just yet, will try again later
            return;
        }
        // Tell the music playback service to pause
        // TODO: these constants need to be published somewhere in the framework.
        Intent i = new Intent("com.android.music.musicservicecommand");
        i.putExtra("command", "pause");
        mContext.sendBroadcast(i);

        // we shouldn't clear the target state, because somebody might have
        // called start() previously
        release(false);
        try {
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setOnPreparedListener(mPreparedListener);
            mMediaPlayer.setOnVideoSizeChangedListener(mSizeChangedListener);
            mDuration = -1;
            mMediaPlayer.setOnCompletionListener(mCompletionListener);
            mMediaPlayer.setOnErrorListener(mErrorListener);
            mMediaPlayer.setOnBufferingUpdateListener(mBufferingUpdateListener);
            //add by yanggf
			mMediaPlayer.setOnInfoListener(mInfoListener);
			//add by yanggf
			mMediaPlayer.setOnSeekCompleteListener(mSeekCompleteListener);
            mCurrentBufferPercentage = 0;
            //modify by yanggf
//          mMediaPlayer.setDataSource(mContext, mUri, mHeaders);
            mMediaPlayer.setDataSource(mContext, mUri);
            mMediaPlayer.setDisplay(mSurfaceHolder);
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.setScreenOnWhilePlaying(true);
            mMediaPlayer.prepareAsync();
            // we don't set the target state here either, but preserve the
            // target state that was there before.
            mCurrentState = STATE_PREPARING;
            attachMediaController();
        } catch (IOException ex) {
            Log.w(TAG, "Unable to open content: " + mUri, ex);
            mCurrentState = STATE_ERROR;
            mTargetState = STATE_ERROR;
            mErrorListener.onError(mMediaPlayer, MediaPlayer.MEDIA_ERROR_UNKNOWN, 0);
            return;
        } catch (IllegalArgumentException ex) {
            Log.w(TAG, "Unable to open content: " + mUri, ex);
            mCurrentState = STATE_ERROR;
            mTargetState = STATE_ERROR;
            mErrorListener.onError(mMediaPlayer, MediaPlayer.MEDIA_ERROR_UNKNOWN, 0);
            return;
        }
    }

    public void setMediaController(MediaController controller) {
        if (mMediaController != null) {
            mMediaController.hide();
        }
        mMediaController = controller;
        attachMediaController();
    }

    private void attachMediaController() {
        if (mMediaPlayer != null && mMediaController != null) {
            mMediaController.setMediaPlayer(this);
            View anchorView = this.getParent() instanceof View ?
                    (View)this.getParent() : this;
            mMediaController.setAnchorView(anchorView);
            mMediaController.setEnabled(isInPlaybackState());
        }
    }

    MediaPlayer.OnVideoSizeChangedListener mSizeChangedListener =
        new MediaPlayer.OnVideoSizeChangedListener() {
            public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                mVideoWidth = mp.getVideoWidth();
                mVideoHeight = mp.getVideoHeight();
             // add by yanggf
    			if (mMyChangeLinstener != null) {
    				mMyChangeLinstener.doMyThings();
    			}
                if (mVideoWidth != 0 && mVideoHeight != 0) {
                    getHolder().setFixedSize(mVideoWidth, mVideoHeight);
                }
            }
    };

    MediaPlayer.OnPreparedListener mPreparedListener = new MediaPlayer.OnPreparedListener() {
        public void onPrepared(MediaPlayer mp) {
            mCurrentState = STATE_PREPARED;

            // Get the capabilities of the player for this stream
            //modify by yanggf
//            Metadata data = mp.getMetadata(MediaPlayer.METADATA_ALL,
//                                      MediaPlayer.BYPASS_METADATA_FILTER);
//
//            if (data != null) {
//                mCanPause = !data.has(Metadata.PAUSE_AVAILABLE)
//                        || data.getBoolean(Metadata.PAUSE_AVAILABLE);
//                mCanSeekBack = !data.has(Metadata.SEEK_BACKWARD_AVAILABLE)
//                        || data.getBoolean(Metadata.SEEK_BACKWARD_AVAILABLE);
//                mCanSeekForward = !data.has(Metadata.SEEK_FORWARD_AVAILABLE)
//                        || data.getBoolean(Metadata.SEEK_FORWARD_AVAILABLE);
//            } else {
//                mCanPause = mCanSeekBack = mCanSeekForward = true;
//            }

            if (mOnPreparedListener != null) {
                mOnPreparedListener.onPrepared(mMediaPlayer);
            }
            if (mMediaController != null) {
                mMediaController.setEnabled(true);
            }
            mVideoWidth = mp.getVideoWidth();
            mVideoHeight = mp.getVideoHeight();

            int seekToPosition = mSeekWhenPrepared;  // mSeekWhenPrepared may be changed after seekTo() call
            if (seekToPosition != 0) {
                seekTo(seekToPosition);
            }
            if (mVideoWidth != 0 && mVideoHeight != 0) {
                //Log.i("@@@@", "video size: " + mVideoWidth +"/"+ mVideoHeight);
                getHolder().setFixedSize(mVideoWidth, mVideoHeight);
                if (mSurfaceWidth == mVideoWidth && mSurfaceHeight == mVideoHeight) {
                    // We didn't actually change the size (it was already at the size
                    // we need), so we won't get a "surface changed" callback, so
                    // start the video here instead of in the callback.
                    if (mTargetState == STATE_PLAYING) {
                        start();
                        if (mMediaController != null) {
                            mMediaController.show();
                        }
                    } else if (!isPlaying() &&
                               (seekToPosition != 0 || getCurrentPosition() > 0)) {
                       if (mMediaController != null) {
                           // Show the media controls when we're paused into a video and make 'em stick.
                           mMediaController.show(0);
                       }
                   }
                }
            } else {
                // We don't know the video size yet, but should start anyway.
                // The video size might be reported to us later.
                if (mTargetState == STATE_PLAYING) {
                    start();
                }
            }
        }
    };

    private MediaPlayer.OnCompletionListener mCompletionListener =
        new MediaPlayer.OnCompletionListener() {
        public void onCompletion(MediaPlayer mp) {
            mCurrentState = STATE_PLAYBACK_COMPLETED;
            mTargetState = STATE_PLAYBACK_COMPLETED;
            if (mMediaController != null) {
                mMediaController.hide();
            }
            if (mOnCompletionListener != null) {
                mOnCompletionListener.onCompletion(mMediaPlayer);
            }
        }
    };

    private MediaPlayer.OnErrorListener mErrorListener =
        new MediaPlayer.OnErrorListener() {
        public boolean onError(MediaPlayer mp, int framework_err, int impl_err) {
            Log.d(TAG, "Error: " + framework_err + "," + impl_err);
            mCurrentState = STATE_ERROR;
            mTargetState = STATE_ERROR;
            if (mMediaController != null) {
                mMediaController.hide();
            }

            /* If an error handler has been supplied, use it and finish. */
            if (mOnErrorListener != null) {
                if (mOnErrorListener.onError(mMediaPlayer, framework_err, impl_err)) {
                    return true;
                }
            }

            /* Otherwise, pop up an error dialog so the user knows that
             * something bad has happened. Only try and pop up the dialog
             * if we're attached to a window. When we're going away and no
             * longer have a window, don't bother showing the user an error.
             */
            if (getWindowToken() != null) {
                Resources r = mContext.getResources();
                int messageId;
                
                //add by yanggf

//                if (framework_err == MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK) {
//                    messageId = com.android.internal.R.string.VideoView_error_text_invalid_progressive_playback;
//                } else {
//                    messageId = com.android.internal.R.string.VideoView_error_text_unknown;
//                }
//
//                new AlertDialog.Builder(mContext)
//                        .setTitle(com.android.internal.R.string.VideoView_error_title)
//                        .setMessage(messageId)
//                        .setPositiveButton(com.android.internal.R.string.VideoView_error_button,
//                                new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int whichButton) {
//                                        /* If we get here, there is no onError listener, so
//                                         * at least inform them that the video is over.
//                                         */
//                                        if (mOnCompletionListener != null) {
//                                            mOnCompletionListener.onCompletion(mMediaPlayer);
//                                        }
//                                    }
//                                })
//                        .setCancelable(false)
//                        .show();
            }
            return true;
        }
    };

    private MediaPlayer.OnBufferingUpdateListener mBufferingUpdateListener = new MediaPlayer.OnBufferingUpdateListener() {
        public void onBufferingUpdate(MediaPlayer mp, int percent) {
            mCurrentBufferPercentage = percent;
            //add by yanggf
            if (mOnBufferingUpdateListener != null) {
				mOnBufferingUpdateListener.onBufferingUpdate(mMediaPlayer,percent);
			}
        }
    };
    
    
    private MediaPlayer.OnSeekCompleteListener mSeekCompleteListener  = new MediaPlayer.OnSeekCompleteListener() {
		
		@Override
		public void onSeekComplete(MediaPlayer mp) {
			// TODO Auto-generated method stub
			if(mOnSeekCompleteListener != null){
				mOnSeekCompleteListener.onSeekComplete(mMediaPlayer);
			}
			
		}
	};
    

    /**
     * Register a callback to be invoked when the media file
     * is loaded and ready to go.
     *
     * @param l The callback that will be run
     */
    public void setOnPreparedListener(MediaPlayer.OnPreparedListener l)
    {
        mOnPreparedListener = l;
    }
    
    public void setOnSeekCompleteListener(OnSeekCompleteListener l) {
		mOnSeekCompleteListener = l;
	}
    
    public void setOnBufferingUpdateListener(OnBufferingUpdateListener l) {
		mOnBufferingUpdateListener = l;
	}

    /**
     * Register a callback to be invoked when the end of a media file
     * has been reached during playback.
     *
     * @param l The callback that will be run
     */
    public void setOnCompletionListener(OnCompletionListener l)
    {
        mOnCompletionListener = l;
    }

    /**
     * Register a callback to be invoked when an error occurs
     * during playback or setup.  If no listener is specified,
     * or if the listener returned false, VideoView will inform
     * the user of any errors.
     *
     * @param l The callback that will be run
     */
    public void setOnErrorListener(OnErrorListener l)
    {
        mOnErrorListener = l;
    }
    
    
    /**
     * add by yanggf
	 * 播放空的内容做相关处理
	 */
	private MediaPlayer.OnInfoListener mInfoListener = new MediaPlayer.OnInfoListener() {

		@Override
		public boolean onInfo(MediaPlayer mp, int what, int extra) {
			
			if (mOnInfoListener != null) {
				mOnInfoListener.onInfo(mp, what, extra);
			}
			
			return true;
		}

	};

	public void setOnInfoListener(OnInfoListener listener) {
		mOnInfoListener = listener;
	}
	
	/**
	 * add by yanggf 自定义监听器，用于监听屏蔽大小是否改变,用于提供给其他类调用。
	 */
	public void setMySizeChangeLinstener(MySizeChangeLinstener l) {
		mMyChangeLinstener = l;
	}


    SurfaceHolder.Callback mSHCallback = new SurfaceHolder.Callback()
    {
        public void surfaceChanged(SurfaceHolder holder, int format,
                                    int w, int h)
        {
            mSurfaceWidth = w;
            mSurfaceHeight = h;
            boolean isValidState =  (mTargetState == STATE_PLAYING);
            boolean hasValidSize = (mVideoWidth == w && mVideoHeight == h);
            if (mMediaPlayer != null && isValidState && hasValidSize) {
                if (mSeekWhenPrepared != 0) {
                    seekTo(mSeekWhenPrepared);
                }
                start();
                if (mMediaController != null) {
                    if (mMediaController.isShowing()) {
                        // ensure the controller will get repositioned later
                        mMediaController.hide();
                    }
                    mMediaController.show();
                }
            }
        }

        public void surfaceCreated(SurfaceHolder holder)
        {
            mSurfaceHolder = holder;
            //resume() was called before surfaceCreated()
            if (mMediaPlayer != null && mCurrentState == STATE_SUSPEND
                   && mTargetState == STATE_RESUME) {
                mMediaPlayer.setDisplay(mSurfaceHolder);
                //moify by yanggf
//                resume();
            } else {
                openVideo();
            }
        }

        public void surfaceDestroyed(SurfaceHolder holder)
        {
            // after we return from this we can't use the surface any more
            mSurfaceHolder = null;
            if (mMediaController != null) mMediaController.hide();
            if (mCurrentState != STATE_SUSPEND) {
                release(true);
            }
        }
    };

    /*
     * release the media player in any state
     */
    private void release(boolean cleartargetstate) {
    	 try{
    		 if (mMediaPlayer != null) {
    	            mMediaPlayer.reset();
    	            mMediaPlayer.release();
    	            mMediaPlayer = null;
    	            mCurrentState = STATE_IDLE;
    	            if (cleartargetstate) {
    	                mTargetState  = STATE_IDLE;
    	            }
    	        }
    		}catch (Exception e) {
    			e.printStackTrace();
    		}
        
    }
    
   
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (isInPlaybackState() && mMediaController != null) {
            toggleMediaControlsVisiblity();
        }
        return false;
    }

    @Override
    public boolean onTrackballEvent(MotionEvent ev) {
        if (isInPlaybackState() && mMediaController != null) {
            toggleMediaControlsVisiblity();
        }
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        boolean isKeyCodeSupported = keyCode != KeyEvent.KEYCODE_BACK &&
                                     keyCode != KeyEvent.KEYCODE_VOLUME_UP &&
                                     keyCode != KeyEvent.KEYCODE_VOLUME_DOWN &&
                                     keyCode != KeyEvent.KEYCODE_MENU &&
                                     keyCode != KeyEvent.KEYCODE_CALL &&
                                     keyCode != KeyEvent.KEYCODE_ENDCALL;
        if (isInPlaybackState() && isKeyCodeSupported && mMediaController != null) {
            if (keyCode == KeyEvent.KEYCODE_HEADSETHOOK ||
                    keyCode == KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE) {
                if (mMediaPlayer.isPlaying()) {
                    pause();
                    mMediaController.show();
                } else {
                    start();
                    mMediaController.hide();
                }
                return true;
            } else if (keyCode == KeyEvent.KEYCODE_MEDIA_STOP
                    && mMediaPlayer.isPlaying()) {
                pause();
                mMediaController.show();
            } else {
                toggleMediaControlsVisiblity();
            }
        }

        return super.onKeyDown(keyCode, event);
    }

    private void toggleMediaControlsVisiblity() {
    	try{
    		if (mMediaController.isShowing()) {
                mMediaController.hide();
            } else {
                mMediaController.show();
            }
    	}catch(Exception e){
    		e.printStackTrace();
    	}
        
    }

    public void start() {
    	try{
    		if (isInPlaybackState()) {
                mMediaPlayer.start();
                mCurrentState = STATE_PLAYING;
            }
            mTargetState = STATE_PLAYING;
    	}catch (Exception e) {
			// TODO: handle exception
    		e.printStackTrace();
		}
        
    }

    public void pause() {
    	try{
    		 if (isInPlaybackState()) {
    	            if (mMediaPlayer.isPlaying()) {
    	                mMediaPlayer.pause();
    	                mCurrentState = STATE_PAUSED;
    	            }
    	        }
    	        mTargetState = STATE_PAUSED;
    	}catch (Exception e) {
			// TODO: handle exception
    		e.printStackTrace();
		}
       
    }

    //modify by yanggf
//    public void suspend() {
//        if (isInPlaybackState()) {
//            if (mMediaPlayer.suspend()) {
//                mStateWhenSuspended = mCurrentState;
//                mCurrentState = STATE_SUSPEND;
//                mTargetState = STATE_SUSPEND;
//            } else {
//                release(false);
//                mCurrentState = STATE_SUSPEND_UNSUPPORTED;
//                Log.w(TAG, "Unable to suspend video. Release MediaPlayer.");
//            }
//        }
//    }

    //modify by yanggf
//    public void resume() {
//        if (mSurfaceHolder == null && mCurrentState == STATE_SUSPEND){
//            mTargetState = STATE_RESUME;
//            return;
//        }
//        if (mMediaPlayer != null && mCurrentState == STATE_SUSPEND) {
//            if (mMediaPlayer.resume()) {
//                mCurrentState = mStateWhenSuspended;
//                mTargetState = mStateWhenSuspended;
//            } else {
//                Log.w(TAG, "Unable to resume video");
//            }
//            return;
//        }
//        if (mCurrentState == STATE_SUSPEND_UNSUPPORTED) {
//            openVideo();
//        }
//    }

   // cache duration as mDuration for faster access
    public int getDuration() {
    	try{
    		 if (isInPlaybackState()) {
    	            if (mDuration > 0) {
    	                return mDuration;
    	            }
    	            mDuration = mMediaPlayer.getDuration();
    	            return mDuration;
    	        }
    	        mDuration = -1;
    	        return mDuration;
    	}catch (Exception e) {
			// TODO: handle exception
    		e.printStackTrace();
		}
    	 return mDuration;
    }

    
    public int getCurrentPosition() {
    	try{
    		 if (isInPlaybackState()) {
    	            return mMediaPlayer.getCurrentPosition();
    	      }
    	      return 0;

   		}catch (Exception e) {
   			e.printStackTrace();
   		}
   	   return 0;
       
    }
    
    
    /**
	 * add by yanggf得到视频的宽
	 */
	public int getVideoWidth() {
		return mVideoWidth;
	}

	/**
	 * 
	 * add by yanggf@return：视频高度
	 */
	public int getVideoHeight() {
		return mVideoHeight;
	}

    public void seekTo(int msec) {
    	try{
    		 if (isInPlaybackState()) {
    	            mMediaPlayer.seekTo(msec);
    	            mSeekWhenPrepared = 0;
    	        } else {
    	            mSeekWhenPrepared = msec;
    	        }
    	}catch (Exception e) {
			e.printStackTrace();
		}
       
    }

    public boolean isPlaying() {
    	try{
    		return isInPlaybackState() && mMediaPlayer.isPlaying();
    	}catch (Exception e) {
			// TODO: handle exception
		}
    	return false;
      
    }

    public int getBufferPercentage() {
    	try{
    		 if (mMediaPlayer != null) {
    	          return mCurrentBufferPercentage;
    	      }
    	}catch (Exception e) {
    		e.printStackTrace();
		}
        return 0;
    }

    private boolean isInPlaybackState() {
    	
     try{
		return (mMediaPlayer != null &&
                mCurrentState != STATE_ERROR &&
                mCurrentState != STATE_IDLE &&
                mCurrentState != STATE_PREPARING);
     }catch (Exception e) {
			// TODO: handle exception
		}
    	return false;
        
    }

    public boolean canPause() {
        return mCanPause;
    }

    public boolean canSeekBackward() {
        return mCanSeekBack;
    }

    public boolean canSeekForward() {
        return mCanSeekForward;
    }
}
