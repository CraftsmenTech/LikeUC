
package cn.hi321.browser.ui.components;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.TextView;
import cn.hi321.browser.ui.activities.HomeActivity;
import cn.hi321.browser.ui.runnables.FaviconUpdaterRunnable;
import cn.hi321.browser.utils.LogUtil;
import cn.hi321.browser2.R;


public class CustomWebChromeClient extends WebChromeClient{
	
	private static final String TAG = "CustomWebChromeClient";
	
	private HomeActivity mMainActivity = null;
	
	private Bitmap mDefaultVideoPoster = null;
	private View mVideoProgressView = null;
	
	
	private CustomViewCallback mCustomViewCallback;
	private int mOriginalOrientation = 1;
	
	public boolean isUseNew = true;
	
	
	public CustomWebChromeClient (HomeActivity mainActivity){
		super();
		mMainActivity = mainActivity;
	}

	
	// This is an undocumented method, it _is_ used, whatever Eclipse may think :)
	// Used to show a file chooser dialog.
	public void openFileChooser(ValueCallback<Uri> uploadMsg) {
		Log.e(TAG, "openFileChooser()==="+uploadMsg);
//		mUploadMessage = uploadMsg;
		mMainActivity.setUploadMessage(uploadMsg);
		Intent i = new Intent(Intent.ACTION_GET_CONTENT);
		i.addCategory(Intent.CATEGORY_OPENABLE);
		i.setType("*/*");
		mMainActivity.startActivityForResult(
				Intent.createChooser(i,mMainActivity.getString(R.string.Main_FileChooserPrompt)),
				mMainActivity.OPEN_FILE_CHOOSER_ACTIVITY);
	}
	
	
	
	@Override
	public Bitmap getDefaultVideoPoster() {
		Log.e(TAG, "getDefaultVideoPoster()");
		if (mDefaultVideoPoster == null) {
            mDefaultVideoPoster = BitmapFactory.decodeResource(mMainActivity.getResources(), R.drawable.default_video_poster);
        }
		
		
        return mDefaultVideoPoster;
	}

	@Override
	public View getVideoLoadingProgressView() {
		Log.e(TAG, "getVideoLoadingProgressView()");
		if (mVideoProgressView == null) {
            LayoutInflater inflater = LayoutInflater.from(mMainActivity);
            mVideoProgressView = inflater.inflate(R.layout.video_loading_progress, null);
        }
		
        return mVideoProgressView;
	}
	
	@Override
	public void onShowCustomView(View view, WebChromeClient.CustomViewCallback callback) {
		Log.e(TAG, "onShowCustomView()==="+callback);
		
		 mMainActivity.showCustomView(view, callback,false);
		 
    }
	@Override
	public void onShowCustomView(View view, int requestedOrientation,
			CustomViewCallback callback) {
//		 super.onShowCustomView(view, requestedOrientation, callback);
		 mMainActivity.showCustomView(view, callback,false);
	
	}
	@Override
	public void onHideCustomView() {
		 mMainActivity.hideCustomView(false);
	}
	@Override
	public void onProgressChanged(WebView view, int newProgress) {
//		Log.e(TAG, "onProgressChanged()===newProgress==="+newProgress); 			

		if(HomeActivity.isNeedLoadHide){//不需要加载
			 mMainActivity.mProgressBar.setVisibility(View.GONE);
		}else{
			mMainActivity.mProgressBar.setVisibility(View.VISIBLE);
		}
		 mMainActivity.mProgressBar.setProgress(newProgress);
		 
		 if(newProgress == 100){    
				mMainActivity.cancelProgressBar();
		 } else {
			 if (mMainActivity.mCurrentWebView.getTitle()==null||mMainActivity.mCurrentWebView.getUrl()==null) {
				 mMainActivity.mUrlEditText.setText("网页加载中...");}
				 else if(mMainActivity.mCurrentWebView.getTitle()!=null&&mMainActivity.mCurrentWebView.getUrl()!=null){
				mMainActivity.mUrlEditText.setText(mMainActivity.mCurrentWebView.getTitle());
				}else if(mMainActivity.mCurrentWebView.getTitle()==null||mMainActivity.mCurrentWebView.getUrl()!=null) {
				mMainActivity.mUrlEditText.setText(mMainActivity.mCurrentWebView.getUrl());
			 	}
			 
		}
		 
		 
		 
	}
	
 
	 
	
	
	 
				
	@Override
	public void onReceivedIcon(WebView view, Bitmap icon) {
		Log.e(TAG, "onReceivedIcon()===");
		new Thread(new FaviconUpdaterRunnable(mMainActivity, view.getUrl(), view.getOriginalUrl(), icon)).start();
		mMainActivity.updateFavIcon();
		
		super.onReceivedIcon(view, icon);
	}  
	
	@Override
	public boolean onCreateWindow(WebView view, final boolean dialog, final boolean userGesture, final Message resultMsg) {
		Log.e(TAG, "onCreateWindow()===dialog=="+dialog+"----userGesture==="+userGesture);
	
		WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
		 
		mMainActivity.addTab(false, mMainActivity.mViewFlipper.getDisplayedChild());
		transport.setWebView(mMainActivity.mCurrentWebView);
		mMainActivity.setAddNewPageSize();
		resultMsg.sendToTarget();
		
		
		return true;
	}
	
	
	
	
	@Override
	public void onRequestFocus(WebView view) {
		super.onRequestFocus(view);
		Log.e(TAG, "onRequestFocus()====");
	}


	@Override
	public void onCloseWindow(WebView window) {
		super.onCloseWindow(window);
		Log.e(TAG, "onCloseWindow()====");
	}


	@Override
	public void onReceivedTitle(WebView view, String title) {
//		Log.e(TAG, "onReceivedTitle()===");
//		mMainActivity.showProgressBar();
		mMainActivity.setTitle(String.format(mMainActivity.getResources().getString(R.string.ApplicationNameUrl), title)); 
		mMainActivity.startHistoryUpdaterRunnable(title,mMainActivity.mCurrentWebView.getUrl(), mMainActivity.mCurrentWebView.getOriginalUrl());

		if(mMainActivity.mCurrentWebView!=null){ 
			mMainActivity.updateUI();
    	}
		
		super.onReceivedTitle(view, title);
	}

	@Override
	public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
//		Log.e(TAG, "onJsAlert()===");
		new AlertDialog.Builder(mMainActivity)
		.setTitle(R.string.ApplicationName)
		.setMessage(message)
		.setPositiveButton(android.R.string.ok,
				new AlertDialog.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int which) {
				result.confirm();
			}
		})
		.setCancelable(false)
		.create()
		.show();

		return true;
	}

	@Override
	public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
		LogUtil.e(TAG, "onJsConfirm()===");
		new AlertDialog.Builder(mMainActivity)
		.setTitle(R.string.ApplicationName)
		.setMessage(message)
		.setPositiveButton(android.R.string.ok, 
				new DialogInterface.OnClickListener() 
		{
			public void onClick(DialogInterface dialog, int which) {
				result.confirm();
			}
		})
		.setNegativeButton(android.R.string.cancel, 
				new DialogInterface.OnClickListener() 
		{
			public void onClick(DialogInterface dialog, int which) {
				result.cancel();
			}
		})
		.create()
		.show();

		return true;
	}

	@Override
	public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, final JsPromptResult result) {
//		Log.e(TAG, "onJsPrompt()===");
		
		final LayoutInflater factory = LayoutInflater.from(mMainActivity);
        final View v = factory.inflate(R.layout.javascript_prompt_dialog, null);
        ((TextView) v.findViewById(R.id.JavaScriptPromptMessage)).setText(message);
        ((EditText) v.findViewById(R.id.JavaScriptPromptInput)).setText(defaultValue);

        new AlertDialog.Builder(mMainActivity)
            .setTitle(R.string.ApplicationName)
            .setView(v)
            .setPositiveButton(android.R.string.ok,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            String value = ((EditText) v.findViewById(R.id.JavaScriptPromptInput)).getText()
                                    .toString();
                            result.confirm(value);
                        }
                    })
            .setNegativeButton(android.R.string.cancel,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            result.cancel();
                        }
                    })
            .setOnCancelListener(
                    new DialogInterface.OnCancelListener() {
                        public void onCancel(DialogInterface dialog) {
                            result.cancel();
                        }
                    })
            .show();
        
        return true;

	}		
	

}
