package cn.hi321.browser.utils;


public class CacheUtils {
	
	
	/**电影、电视剧、动漫、综艺*/
	public static String[] longMediaType= {"movie","tv","cartoon","variety"};
	
	/**最热、最新、评分、上映*/
	public static String[] longMediaOrder = new String[]{"pl","mo","ka","re"};
	
	/**娱乐，五星体育*/
	public static String[] shortMediaType = {"entertainments","sport"};
	
	/**最热、最新、评分*/
	public static String[] shortMediaOrder = new String[]{"pl","mo","ka"};
	
	/**直播秀：直播节目、电视台*/
	public static String[] liveMediaOrder = {"live_program","tv_program"};
	
	/**直播秀缓存路径*/
	public static String liveBasePath = "/cache/livePage/";
	
	/**长视频媒体列表的第一页数据*/
	public static String longBasePath = "/cache/longMediaPage/";
	
	/**首页推广数据缓存路径*/
	public static String spreadBasePath = "/cache/spread/";
	
	/**精选缓存数据存储路径*/
	public static String featuredBasePath = "/cache/featured/";
	
	/**新版精选缓存数据存储路径*/
	public static String NEW_FEATURE_BASE_PATH = "phonemedia";
	
	public static String spreadStr = "spread_mid";
		
	/**精选*/
	public static String featuredStr = "featured_mid";
	
	/**小视频媒体缓存数据存储路径*/
	public static String shortBasePath = "/cache/shortMediaPage/";
	
	public static String cachePath = "/cache/";
	
	/**
	 * 存储筛选条件的路径
	 */
	public static String indexBasePath = "/cache/mediaIndexPage/";
	
	/**
	 * 存储push消息的路径
	 */
	public static String pushInfoPath = "/cache/pushdata/";
	
	public static long time_out = 60*60*1000; //过期时间：1小时
	
	public static String plat_login_path = "/cache/plat_login/";

}
