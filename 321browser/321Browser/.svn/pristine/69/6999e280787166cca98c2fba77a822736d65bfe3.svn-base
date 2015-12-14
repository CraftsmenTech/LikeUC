package cn.hi321.browser.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import cn.hi321.browser2.R;
/**
 * 
 * @author yanggf
 *
 */
public class NetworkUtil {
	private final static String TAG = "NetworkUtils";
	
	public static final String CODE_HTTP_SUCCEED = "200";//Networking success 
	public static final String CODE_HTTPS_RECONNECT = "002";
	public static final String CODE_SESSION_EXPIRED = "2000";//Session expires
	public static final String CODE_STOP_SERVER = "5000";//Server to stop the service
	public static final String SERVER_NOT_RESPONDING = "10000";//Server did not respond
	public static final String CODE_PAGE_NOT_FOUND = "404";//Did not find interface
	public static final String SERVER_ERROR = "500";
	public static final String CODE_CONNECTION_EXCEPTION = "502";
	public static final String CODE_HTTP_RESTART_CLIENT = "4000";//Restart the client

	public static final String NET_WORK_INVAILABLE = "netInvailable";
	public final static String CMWAP = "cmwap";
	public final static String WAP_3G = "3gwap";
	public final static String UNIWAP = "uniwap";
	public final static String CTWAP = "ctwap";
	public final static String CTNET = "ctnet";
	
    public final static int TYPE_NET_WORK_DISABLED = 0;// 网络不可用
    public final static int TYPE_MNO_CM = 1;// 移动
    public final static int TYPE_MNO_CU = 2;// 联通
    public final static int TYPE_MNO_CT = 3;// 电信
    public final static int TYPE_CM_CU_WAP = 4;// 移动联通wap10.0.0.172
    public final static int TYPE_CT_WAP = 5;// 电信wap 10.0.0.200
    public final static int TYPE_OTHER_NET = 6;// 电信,移动,联通,wifi 等net网络
    public final static int TYPE_RESERVATION_7DAY = 1;
    public final static int TYPE_PLACE = 2;
    public final static int TYPE_HOTEL = 1;
   
    public final static int TYPE_WIFI = 1;
    public final static int TYPE_MOBILE = 2;
    public final static int TYPE_ERROR = -1;

	public static final int MOBILE_STATE = 1;
	public static final int WIFI_STATE = 2;
	public static int CURRENT_STATE = WIFI_STATE;
	
	public static final String TYPE_HTTP = "http";
	public static final String TYPE_MMS = "mms";
	public static final String TYPE_RTSP = "rtsp";
	public static final String TYPE_WWW = "www";
	
	public static boolean isWIFISTATE = false;//当前网络状态是否为WIFI
	public static boolean is2Gor3G = false;//当前网络状态是否为2G
	
	public final static int TAG_LOGINCONFIGURATION = 19;
	public final static int REM_GET_ACTIVITY_GALLERYFLOW_BY_SERVER = 20;
	public final static int REM_GET_ACTIVITY_RECOMMEND_LIST_BY_SERVER = 21;
	public final static int GET_MEDIA_BY_SERVER = 23;//Used to identify the media layer
	public final static int MOVIE_GET_CONTENT_DATA = 22;// Used to identify the film list data
	public final static int GET_MAIN_INDEX_ITEM_DATA = 24;//Used to identify the film left of the page Index page button data
	public final static int GET_SEARCH_CONTENT_DATA = 25;//Used to identify the network request is data of the search results
	public final static int GET_PLAY_LIST_DATA = 26;//Used to identify the network requests the playback data
	public final static int GET_SEARCH_KEY_DATA = 27;//Used to identify the network request data prompted by the hot search word
	public final static int REPORTED_MESSAGE_TAG = 28;//Used to identify the reported
	public final static int FEEDBACK_UPLOAD_INFOMATION = 29;//Used to identify at this time deal with the data reported by the feedback page
	public final static int GET_PLAY_HISTORY_LIST_DATA = 30;//The data used to identify the network to request the history of play results
	public final static int REPORTED_ERROR_MESSAGE_TAG = 31;//Used to identify crash information reported
	public final static int GET_MEDIA_HISTORY_BY_SERVER = 32;//Used to identify the request media 
	public final static int REPORTED_PLAY_TIME_TAG = 33;//Identify the player out of the number reported to play cards, etc
	public final static int FRIST_BUFFERRE_PORTED_MESSAGE_TAG = 34;//Used to identify the player for the first time reported to the buffer
	public final static int DRAG_BUFFERRE_PORTED_MESSAGE_TAG = 35;//Used to identify the player to drag the buffer reported
	public final static int STUCK_BUFFERRE_PORTED_MESSAGE_TAG = 36;//Used to identify the player to play the kinds of cards reported
	public final static int ERROR_BUFFERRE_PORTED_MESSAGE_TAG = 37;//Used to identify the player can not play the file reported
	public final static int APP_START_REPORTED_MESSAGE_TAG = 38;//Used to identify the program to start the reported
	public final static int START_APP_TAG_LOGINCONFIGURATION = 39;//Start the upgrade request path identification
	public final static int GET_RANK_LIST_DATA = 41;
	public final static int GET_DOWNLOAD_DATA = 40;//Used to identify the program to start the download
	public final static int GET_DR_DATA_ERROR_DATA = 42;
	public final static int FEATURED_BROWSINGDATA_REPORT_MESSAGE_TAG = 43;//Used to report the pages of featured data
	public final static int GET_MEDIA_PICTURES_DATA = 44;//Used to identify the sitllpicture data
	public final static int GET_MEDIA_COMMENT_LIST_DATA = 45;//媒体精彩评论
	public final static int GET_HOT_WORD_DATA = 46;//获取热词提示
	public final static int REPORT_CRASH_INFO = 47;//上报崩溃日志
	public final static int CACHE_DATA_FEATURED_READ = 48;//读取精选缓存
	public final static int CACHE_DATA_FEATURED_WRITE = 49;//写入精选缓存数据
	public final static int GET_DOWNLOAD_DATA_OFFLINE = 50;
	public final static int UP_LOAD_REPORT = 51;
	public final static int Get_LOG_CONTROL_DATA = 52;
	public final static int GET_PROGRAM_WATCH_FOCUS_TAG_DATA = 53;//Used to identify the data of program watch tag
	public final static int GET_PROGRAM_WATCH_FOCUS_DATA = 54;//Used to identify the data of program watch
	public final static int GET_PROGRAM_DETAIL_BY_SERVER = 55;//获取节目详情数据
	public final static int GET_LIVE_TV_DATA = 56;//live related keywords
	public final static int GET_LIVE_BROADCASTS_DATA = 57;
	public final static int GET_PROGRAM_PAGE_DATA = 58;//获取节目页数据的标志
	public final static int GET_USER_COMMENT_DATA = 59;//Get user comment list
	public final static int GET_MAIN_BUSSINISS_DATA = 60;
	public final static int GET_HOTAPP_PAGE_DATA = 61;
	public final static int GET_PUSH_NOTIFICATION_DATA = 62;
	public final static int GET_SEARCH_VIDEO_DATA = 63;
	/**
	 *获取清晰度对应的播放地址列表  
	 */
	public final static int GET_MPURLS_DATA = GET_PUSH_NOTIFICATION_DATA + 1;

	public final static int GET_SHORT_VIDEO_SPECIAL = GET_MPURLS_DATA + 1;
	
	public final static int GET_MEDIA_LIVE_DATA = GET_SHORT_VIDEO_SPECIAL + 1;// Used to get the data of media live
	
	public final static int GET_PUSH_ORIGIN_TYPE = GET_MEDIA_LIVE_DATA + 1;
	
	/**
	 *获取手机精选接口 add by donggx
	 */
	public final static int GET_PHONE_MEDIA_DATA = GET_PUSH_ORIGIN_TYPE + 1;
	
	public final static int GET_ACTIVE_321BROWSER_DATA = GET_PHONE_MEDIA_DATA + 1;	
	public final static int GET_LOGINABLE_LIST_DATA = GET_ACTIVE_321BROWSER_DATA + 1;
	public final static int GET_LOGIN_DATA = GET_LOGINABLE_LIST_DATA + 1;
	public final static int GET_LOGIN_BOUND_APP_DATA = GET_LOGIN_DATA + 1;
	//add by lushengbin
	public final static int REGISTER_USERNAME_CHECKING = GET_LOGIN_BOUND_APP_DATA + 1;
	public final static int SUNMIT_USER_INFOMATION = REGISTER_USERNAME_CHECKING + 1;

	
    private static NetworkInfo getNetworkInfo(Context context){
    	final ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		return connectivityManager.getActiveNetworkInfo();
    }
    
	/**
	 * Detect whether the network address
	 */
	public static boolean isCorrectUri(Context context, Uri uri) {
//		if (null != uri && null != uri.getScheme() && uri.getScheme().contains("http")) {
//			LogUtil.i(TAG, "checkUri()--getScheme()==" + uri.getScheme());
//			return true;	
//		}	
//		return false;
		boolean isUri = false;
		try {
			if (null != uri && !StringUtil.isEmpty(uri.toString())) {
				String scheme =uri.getScheme();
				LogUtil.i(TAG, "---isNetwork(String uri)--getScheme()=="+ scheme);
				if (null != scheme && (scheme.contains(TYPE_HTTP)||scheme.contains(TYPE_WWW)||scheme.contains(TYPE_RTSP)||scheme.contains(TYPE_MMS))) {
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
	 * Detect network is available
	 */
	public static boolean isNetworkAvailable(Context context) {
		boolean isAvailable = false;
		if(null != context){
			NetworkInfo info = getNetworkInfo(context);
			if (info != null && info.isAvailable()) {
				isAvailable = true;
			}
		} 
		return isAvailable;
	}

	public static String getNetMode(Context context) {
		String netMode = "";
		try {
			NetworkInfo info = getNetworkInfo(context);
			if (info == null || !info.isAvailable()) {
				netMode = NET_WORK_INVAILABLE;
			} else {
				int netType = info.getType();
				if (netType == ConnectivityManager.TYPE_WIFI) {
					netMode = info.getTypeName();
				} else if (netType == ConnectivityManager.TYPE_MOBILE) {
					netMode = info.getExtraInfo();
				} 
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if ("epc.tmobile.com".equals(netMode) || "".equals(netMode)) {
				netMode = "3G";
			}
		}
		return netMode;
	}
	
	/**
	 * Return to the type of network: one on behalf of wifi, 2 on behalf of 2G
	 * or 3G, 3 on behalf of other
	 * @param context
	 * @return
	 */
	public static int reportNetType(Context context) {
		int netMode = TYPE_ERROR;
		try {
			NetworkInfo info = getNetworkInfo(context);
			if (info != null && info.isAvailable()) {
				int netType = info.getType();
				if (netType == ConnectivityManager.TYPE_WIFI) {
					netMode = TYPE_WIFI;
				} else if (netType == ConnectivityManager.TYPE_MOBILE) {
					netMode = TYPE_MOBILE;
				}
			}
		} catch (Exception e) {}
		return netMode;
	}
	
	/**
	 * 判断当前网络是否可用
	 */
	public static boolean checkWlan(Context context)
	{
		if (!NetworkUtil.isNetworkAvailable(context))
		{
			ToastUtil.toastPrompt(context, R.string.netdown, 0);
			return false ;
		}
		return true;
	}
}
