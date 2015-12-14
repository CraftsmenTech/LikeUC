package cn.hi321.browser.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class SQLUtil {
	public static final String DB_NAME = "321Browser.db";// Database name
	public static final int DB_VERSION = 4;// Database version
	public static SQLiteDatabase mInstance = null;
	/**
	 * 插入电影数据的SQL
	 */
	public final static String MOVIEINSERSQL = "insert into moviefilterhistory ("
			+ "hashid,_inserttime,"
			+ "filter_cate_key,filter_cate_title,filter_region_key,filter_region_title,"
			+ "filter_rdate_key,filter_rdate_title,filter_karma_key,filter_karma_title,"
			+ "filter_clarity_key,filter_clarity_title,filter_udate_key,filter_udate_title,"
			+ "filter_hotrank_key,filter_hotrank_title )values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	/**
	 * 插入电视剧数据的SQL
	 */
	public final static String TVINSERTSQL = "insert into tvfilterhistory ("
			+ "hashid,_inserttime,"
			+ "filter_cate_key ,filter_cate_title ,filter_region_key ,filter_region_title ,"
			+ "filter_rdate_key ,filter_rdate_title ,filter_karma_key ,filter_karma_title, "
			+ "filter_udate_key ,filter_udate_title ,"
			+ "filter_hotrank_key ,filter_hotrank_title )values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	/**
	 * 插入卡通的SQL
	 */
	public final static String CARTOONINSERTSQL = "insert into cartoonfilterhistory ("
			+ "hashid,_inserttime,"
			+ "filter_cate_key ,filter_cate_title ,filter_region_key ,filter_region_title ,"
			+ "filter_rdate_key ,filter_rdate_title ,filter_karma_key ,filter_karma_title, "
			+ "filter_udate_key ,filter_udate_title ,"
			+ "filter_hotrank_key ,filter_hotrank_title ) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	/**
	 * 插入综艺数据的SQL
	 */
	public final static String VARIETYINSERSQL = "insert into varietyfilterhistory ("
			+ "hashid,_inserttime,"
			+ "filter_cate_key ,filter_cate_title ,filter_region_key ,filter_region_title ,"
			+ "filter_rdate_key ,filter_rdate_title ,filter_karma_key ,filter_karma_title, "
			+ "filter_udate_key ,filter_udate_title ,"
			+ "filter_hotrank_key ,filter_hotrank_title ) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	public static String ENTERTAINMENTINSERSQL = "insert into entertainmentfilterhistory(" +
			"hashid,_inserttime,filter_cate_key ,filter_cate_title,filter_date_key ,filter_date_title)values(?,?,?,?,?,?)";
	public static String SPORTSQL = "insert into sportfilterfilterhistory(" +
			"hashid,_inserttime,filter_cate_key ,filter_cate_title,filter_date_key ,filter_date_title)values(?,?,?,?,?,?)";

	public synchronized static SQLiteDatabase get321BrowserDb(Context context) {
		try {
			if (null == mInstance && null != context) {
				mInstance = context.openOrCreateDatabase(SQLUtil.DB_NAME,
						Context.MODE_PRIVATE, null);
			}
			return mInstance;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
