package cn.hi321.browser.utils;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import cn.hi321.browser.db.DownloadDao;
import cn.hi321.browser2.R;

/**
 * 
 * @author yanggf
 *
 */
public class Utils {	
	public static final String TAG = "Utils";
	public static long mAPPstatTime = 0;
	
	public static final String CLIENT = "aphone";
	public static final String PLAY_TYPE = "playtype";
	public static final String LIVEFLAG = "liveflag";
	public static final String DEMANDFSPS = "demandata";
	public static final String FORCE_UPDATE = "forceupdate";
	public static final String CACHE_UPDATE = "cacheupdate";
	
	public static int NUM = 0;// 表示精选页面图片更新的数量
	
	public static boolean isLogined = false;
	public static boolean isPlayerCrashSystem = false;
	public static boolean isPlayerCrashVLC = false;
	public static boolean isUploadBfSuccess = false;//是否已经上报过首次缓冲失败的先关信息 
	public static boolean isTipFilter = false;//是否弹出请求不到筛选的Dialog
	public static boolean isGetData = false;//是否取到了频道页的数据

	public static final String CODE_HTTP_FAIL = "-1";
	public static final String LIVEPOSITION = "liveposition";
	public static final String CODE_ERROR_RIGHT = "0";// No problem of errCode

	public static final String TRUE = "true";
	public static final String FALSE = "false";
	public static final String NULL = "null";

	public final static String MEDIA_KEY = "media_key";
	public final static String MEDIA_ITEM = "media_item";
	public final static String DOWNLOAD_KEY = "download_key";
	public final static String LIVE_DATA = "livedata";
	public final static String VIDEONAME = "videoname";
	public final static String IS_ENTERTAINMENT = "entertainment";
	public final static String ENTERTAINMEN_DRDATA = "entertainment_drdata";//娱乐页容灾相关的数据
	public final static String FILTER_KEY = "filter_key";
	public final static String MEDIA_CHANNEL_KEY = "media_channel_key";
	public final static String MEDIA_OPERATION = "MEDIA_OPERATION";
	public final static String PLAY_HISTORY_KEY = "play_history_info_key";
	public final static String PLAY_LOADING_KEY = "play_loading_key";
	public final static String BY_PLAY_HISTORY_KEY = "by_play_histoty_key";
	public static final String KEY_ERROR_MESSAGE = "errorMessage";//errorMessage
	public static final String THE_LIVE_SHOW_FSPS_INFO = "liveshowfspsinfo";
	public final static String MICROVIDEO_KEY = "MICROVIDEO_KEY";

	public static final byte HANDLER_SESSION_EXPIRED = 1;
	public static final byte HANDLER_DISMISS_PROGRESSDIALOG = 2;
	public static final byte HANDLER_LOGO_HTTP_FAILED = 3;
	public static final byte HANDLER_SHOW_ERRORMESSAGE = 4;
	public static final byte HANDLER_SHOW_ERRORMESSAGE_GOTO = 5;
	public static final byte HANDLER_SHOW_START_ERROR_MESSAGE = 6;
	public static final byte HANDLER_SHOW_ERROR_DATA_TOAST = 7;
	
	public static final String FIRST_FILTER_DATA_CONFIG = "first_filter_data_config";
	/**sharePreferences中预设值的标志*/
	public final static String PRESET_VAULE_FLAG = "preset_value";

	
	/**
	 * 用于动态配置push消息需要用到的url 
	 */
	public static final String PUSH_ORIGIN_URL = "http://update.funshion.com/app/pushconfig/?client=";

	public static boolean isNormalLogin = true;//判断是正常登陆还是其他登陆，默认正常登陆

	/**
	 * 判断是否是第一次下载 add by jiyx at 2012-9-17 11:05:15
	 * @param context
	 * @param fName
	 * @return
	 */
	public static boolean isFirstDownload(Context context, String fName) {
		DownloadDao dao = new DownloadDao(context);
		String fileName = fName;
		return dao.isFirst(fileName);
	}
	
	/**
	 * 判断是否有存储卡，有返回TRUE，否则FALSE
	 * 
	 * @return
	 */
	public static boolean isSDcardExist() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean isNetworkUri(Uri uri) {
		boolean isUri = false;
		try {
			if (!StringUtil.isEmpty(uri.toString())) {
				
				String scheme =uri.getScheme();
				if (scheme != null && (scheme.contains("http")||scheme.contains("www")
						||scheme.contains("rtsp")||scheme.contains("mms"))) {
					isUri = true;
				} else {
					isUri = false;
				}			
			}
		} catch (Exception e) {
			return isUri;
		}
		return isUri;	
	}
	
	
	public static boolean isEmpty(String str) {
		if (TextUtils.isEmpty(str) || Utils.NULL.equals(str)
				|| (str != null && "".equals(str.trim()))) {
			return true;
		}
		return false;
	}

	
	 private static Dialog waitingDialog = null;

		public static void startWaitingDialog(Context context) {

			try {
				// synchronized(Utils.synchronizeds) {
				if (waitingDialog == null) {
					waitingDialog = new Dialog(context, R.style.waiting);
					// dialog.setCanceledOnTouchOutside(true);
					waitingDialog.setContentView(R.layout.waiting);
//					waitingDialog.setCanceledOnTouchOutside(false);
					// dialog.setCancelable(false);
					waitingDialog.show();
				} else if (waitingDialog != null && !waitingDialog.isShowing()) {
					waitingDialog.setContentView(R.layout.waiting);
					waitingDialog.setCanceledOnTouchOutside(false);
					waitingDialog.show();
				}
				//
				// }
				// }
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		public static void closeWaitingDialog() {
			try {
				// synchronized(Utils.synchronizeds) {
				if (waitingDialog != null) {
					waitingDialog.dismiss();
					waitingDialog = null;
				}
				// }
			} catch (Exception e) {
				// TODO: handle exception
			}

		}

	

	
	public static boolean  getFirstStartForceUpdateVaule(Context context) {
		   if (null == context)
			   return false;
		   SharedPreferences sharedPreferences = context.getSharedPreferences(FORCE_UPDATE, context.MODE_PRIVATE);
		   return sharedPreferences.getBoolean(CACHE_UPDATE, false);
	   }
	   
		 public static void setIsFirstStartForceUpdate (boolean flag ,Context context) {
			   if (null == context)
				   return;
			   SharedPreferences  sharedPreferences = context.getSharedPreferences(FORCE_UPDATE, context.MODE_PRIVATE );
			   Editor edit = sharedPreferences.edit();
			   edit.putBoolean(CACHE_UPDATE, flag);
			   edit.commit();
		   } 
		 
			
		 /**
		  * 判断用户输入的内容是是否为数字
		  * 2013-5-16下午2:11:02
		  * @param str
		  * @return
		  */
	public static boolean isNumber(String str) {
		try {
			Pattern pattern = Pattern.compile("[0-9]*");
			Matcher match = pattern.matcher(str);
			return match.matches();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	} 
	
	public static boolean isMailAddress (String str) {
		try {
			Pattern pattern = Pattern.compile("^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(\\.([a-zA-Z0-9_-])+)+$");
	        Matcher matcher = pattern.matcher(str);
	        return matcher.matches();
		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static boolean isQQNumber (String str) {
		try {
			int length = str.length();
			return ((!str.startsWith("0"))&&isNumber(str) && (length >= 5 && length < 11));
		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	
	public static boolean isPhoneNumber (String str) {
		try {
			Pattern pattern = Pattern.compile("^((13[0-9])|(14[0-9])|(15[0-9])|(18[0-9]))\\d{8}$");
			Matcher matcher = pattern.matcher(str);
			return (isNumber(str) &&str.length() == 11 &&  matcher.matches());
		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Detect whether there are network
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isCheckNetAvailable(Context context) {
		boolean isCheckNet = false;
		try {
			final ConnectivityManager connectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			final NetworkInfo mobNetInfoActivity = connectivityManager
					.getActiveNetworkInfo();
			if (mobNetInfoActivity != null && mobNetInfoActivity.isAvailable()) {
				isCheckNet = true;
				return isCheckNet;
			} else {
				isCheckNet = false;
				return isCheckNet;
			}
		} catch (Exception ex) {
			ex.printStackTrace();

		}
		return isCheckNet;
	}


	public static String getFileName(String uri) {
		String name = uri;
		if (name != null) {
			String[] content = name.split("/");
			if (content != null && content.length > 1) {
				name = content[content.length - 1];
			}
		}

		return name;
	} 
	
	public static boolean isCheckUriByM3u8(Context context, Uri uri) {
		boolean isUri = false;
		try {
			if (uri != null) {
				if (uri.toString() != null && uri.toString().toLowerCase().contains("m3u8")
						||uri.toString().contains("rtsp")) {
					isUri = true;
				} else {
					isUri = false;
				}
			
			}
		} catch (Exception e) {
			return isUri;
		}
		return isUri;

	}
	/**
	 * Detect whether the network address
	 * 
	 * @param context
	 * @param uri
	 * @return
	 */
	public static boolean checkUri(Context context, Uri uri) {
		boolean isUri = false;
		try {
			if (uri != null) {
				if (uri.getScheme().contains("http")
						|| uri.getScheme().equals("rtsp")
						|| uri.getScheme().equals("mms")
						) {
					isUri = true;
				} else {
					isUri = false;
				}
				LogUtil.i(TAG, "---checkUri()--getScheme()==" + uri.getScheme());
			}
			return isUri;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return isUri;

	}
	
	  public static void showToast(Context paramContext, String paramString)
	  {
	    Toast.makeText(paramContext, paramString, 0).show();
	  }
	  
	  public static boolean hasNetwork(Context context) {
			ConnectivityManager connectivityManager = (ConnectivityManager) context
			        .getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo info = connectivityManager.getActiveNetworkInfo(); 
			if (info != null && info.isAvailable()) { 
				 return true ; 
			} 
			return false;
		}
		
	  
	  
		public static boolean isCMWAP(Context context) {
			ConnectivityManager connectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo info = connectivityManager.getActiveNetworkInfo(); 
			if (info == null || !info.isAvailable()) { 
				return false;
			} else if (info.getTypeName().equals("WIFI")) {
				return false;
			} else if (info.getTypeName() != null
					&& info.getTypeName().equals("MOBILE")
					&& info.getExtraInfo() != null
					&& info.getExtraInfo().toLowerCase().equals("cmwap")) {

				return true; 
			}
			return false;
		}
	/**
	 * @author donggx
	 * 
	 * @param view
	 * 
	 * http://stackoverflow.com/questions/4611822/java-lang-outofmemoryerror-bitmap-size-exceeds-vm-budget
	 */
	public static void unbindDrawables(View view) {
		try {
			if (null == view) {
				return;
			}
			if (view.getBackground() != null) {
				view.getBackground().setCallback(null);
			}
			if (view instanceof ViewGroup) {
				for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
					unbindDrawables(((ViewGroup) view).getChildAt(i));
				}
				((ViewGroup) view).removeAllViews();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static boolean  isErrorNum =  false;
	
	/**
	 * 
	 * @param context
	 * @return
	 */
	public static int getOSVersionSDKINT(Context context) {
		int currentapiVersion = android.os.Build.VERSION.SDK_INT;
		return currentapiVersion;
	}

	/**
	 * Access to device resolution and density information to determine its to
	 * belong aphone or apad criterion here is to determine the resolution and
	 * dpi comprehensive comparison, about the standard Greater than 800x480 for
	 * the pad is less than for the phone
	 */
	public static String getDeviceType(Context context) {
		if (null == context) {
			return "";
		}
		String deviceType = "";
		// int widthPixels = getWidthPixels(context);
		// float widthDpis = getWidthDpi(context);

		DisplayMetrics dm = new DisplayMetrics();
		if (null != dm) {
			((Activity) context).getWindowManager().getDefaultDisplay()
					.getMetrics(dm);
			double x = Math.pow(dm.widthPixels, 2);
			double y = Math.pow(dm.heightPixels, 2);
			double screenInches = Math.sqrt(x + y) / (160 * dm.density);

			if (2 < screenInches && 5 > screenInches) {
				deviceType = "aphone";
			} else {
				deviceType = "apad";
			}
		}
		return deviceType;
	}
	/**
	 * Return to the type of network: one on behalf of wifi, 2 on behalf of 2G
	 * or 3G, 3 on behalf of other
	 * 
	 * @param context
	 * @return
	 */
	public static int reportNetType(Context context) {
		int netMode = 0;
		if(context != null) {
			try {
				final ConnectivityManager connectivityManager = (ConnectivityManager) context
						.getSystemService(Context.CONNECTIVITY_SERVICE);
				final NetworkInfo mobNetInfoActivity = connectivityManager
						.getActiveNetworkInfo();
				if (mobNetInfoActivity == null || !mobNetInfoActivity.isAvailable()) {
					netMode = -1;
				} else {
					int netType = mobNetInfoActivity.getType();
					if (netType == ConnectivityManager.TYPE_WIFI) {
						netMode = 1;
					} else if (netType == ConnectivityManager.TYPE_MOBILE) {
						netMode = 2;
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				netMode = 3;
				return netMode;
			}
		}
		return netMode;
	}

	/**
	 * Get phone model
	 */
	public static String getDeviceModel() {
		String model = android.os.Build.MODEL;
		String StrContent = null;
		try {
			StrContent = URLEncoder.encode(model, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return StrContent;
	}
	public static void netNoPlayeDialog(Context context) {// 退出确认
		AlertDialog.Builder ad = new AlertDialog.Builder(context);
		ad.setTitle("提示");
		ad.setMessage("暂时只支持android_2.1以上系统");
		ad.setPositiveButton("确定", new DialogInterface.OnClickListener() {// 退出按钮
					
					public void onClick(DialogInterface dialog, int i) {
					}
				});
		
		ad.show();// 显示对话框
	}
	
}
