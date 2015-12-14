package cn.hi321.browser.db;

import cn.hi321.browser.utils.SQLUtil;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Create a database with the help of class
 * @author yanggf
 *
 */
public class SQLiteHelper extends SQLiteOpenHelper {
	
	/**
	 * 创建电影列表筛选历史字符串
	 */
	private String createMovieFilterTable = "create table if not exists moviefilterhistory(hashid VARCHAR(100) primary key,_inserttime INTEGER,"+
			"filter_cate_key VARCHAR(20),filter_cate_title VARCHAR(20),filter_region_key VARCHAR(20),filter_region_title VARCHAR(20)," +
			"filter_rdate_key VARCHAR(20),filter_rdate_title VARCHAR(20),filter_karma_key VARCHAR(20),filter_karma_title VARCHAR(20)," +
			"filter_clarity_key VARCHAR(20),filter_clarity_title VARCHAR(20),filter_udate_key VARCHAR(20),filter_udate_title VARCHAR(20)," +
			"filter_hotrank_key VARCHAR(20),filter_hotrank_title VARCHAR(20))";
	
	/**
	 * 创建电视剧筛选历史字符串
	 */
	private String createTvFilterTable = "create table if not exists tvfilterhistory(hashid VARCHAR(100) primary key,_inserttime INTEGER,"+
			"filter_cate_key VARCHAR(20),filter_cate_title VARCHAR(20),filter_region_key VARCHAR(20),filter_region_title VARCHAR(20)," +
			"filter_rdate_key VARCHAR(20),filter_rdate_title VARCHAR(20),filter_karma_key VARCHAR(20),filter_karma_title VARCHAR(20)," +
			"filter_udate_key VARCHAR(20),filter_udate_title VARCHAR(20)," +
			"filter_hotrank_key VARCHAR(20),filter_hotrank_title VARCHAR(20))";
	
	/**
	 *创建动漫筛选历史字符串
	 */
	private String createCartoonTable = "create table if not exists cartoonfilterhistory(hashid VARCHAR(100) primary key,_inserttime INTEGER," +
			"filter_cate_key VARCHAR(20),filter_cate_title VARCHAR(20),filter_region_key VARCHAR(20),filter_region_title VARCHAR(20)," +
			"filter_rdate_key VARCHAR(20),filter_rdate_title VARCHAR(20),filter_karma_key VARCHAR(20),filter_karma_title VARCHAR(20)," +
			"filter_udate_key VARCHAR(20),filter_udate_title VARCHAR(20)," +
			"filter_hotrank_key VARCHAR(20),filter_hotrank_title VARCHAR(20))";
	
	/**
	 * 创建综艺筛选历史字符串
	 */
	private String createVarietyTable = "create table if not exists varietyfilterhistory(hashid VARCHAR(100) primary key,_inserttime INTEGER," +
			"filter_cate_key VARCHAR(20),filter_cate_title VARCHAR(20),filter_region_key VARCHAR(20),filter_region_title VARCHAR(20)," +
			"filter_rdate_key VARCHAR(20),filter_rdate_title VARCHAR(20),filter_karma_key VARCHAR(20),filter_karma_title VARCHAR(20)," +
			"filter_udate_key VARCHAR(20),filter_udate_title VARCHAR(20)," +
			"filter_hotrank_key VARCHAR(20),filter_hotrank_title VARCHAR(20))";

	private String createBrowserTable = "create table if not exists searchs(_id VARCHAR(100) primary key," +
			"search VARCHAR(20) ,data INTEGER)";
	
	/**
	 * 获得微视频筛选历史的数据库表
	 * 2013-5-29下午2:00:13
	 */
	private String getMicroVideoTable (String  tableName) {
		return "create table if not exists "+ tableName+"(hashid VARCHAR(100) primary key, " +
			"_inserttime INTEGER,filter_cate_key VARCHAR(20),filter_cate_title VARCHAR(20),filter_date_key VARCHAR(20)," +
			"filter_date_title VARCHAR(20))";
	}
	
	public static SQLiteHelper mInstance = null;
	
	public static synchronized SQLiteHelper getInstance (Context context) {
		if (null == mInstance ) {
			mInstance = new  SQLiteHelper(context);
		}
		return mInstance;
	}

	public SQLiteHelper(Context context) {
		super(context, SQLUtil.DB_NAME, null, SQLUtil.DB_VERSION);
	}

	public SQLiteHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);

	}

	@Override
	public void onCreate(SQLiteDatabase db) {
//		db.execSQL("create table if not exists playhistoryinfos(_id integer primary key autoincrement,_mid String,_medianame String,_hashid String,_taskname String,_fsp String,_playedtimestring String,_playedtime Long,_position Long)");
//		db.execSQL("create table if not exists playhistoryinfos(_hashid VARCHAR(20) primary key,_medianame VARCHAR(20),_mid VARCHAR(20),_taskname VARCHAR(20),_fsp VARCHAR(200),_playedtimestring VARCHAR(20),_playedtime INTEGER,_position INTEGER)");
//		db.execSQL("create table if not exists playhistoryinfos(_hashid VARCHAR(20) primary key,_medianame VARCHAR(20),_mid VARCHAR(20),_taskname VARCHAR(20),_fsp VARCHAR(200),_playedtimestring VARCHAR(20),_playedtime INTEGER,_position INTEGER,_movieposition INTEGER,_movieplayedtime INTEGER)");
//		db.execSQL("create table if not exists playhistoryinfos(_hashid VARCHAR(20) primary key,_mediatype VARCHAR(20),_medianame VARCHAR(20),_mid VARCHAR(20),_taskname VARCHAR(20),_fsp VARCHAR(200),_playedtimestring VARCHAR(20),_playedtime INTEGER,_position INTEGER,_movieposition INTEGER,_movieplayedtime INTEGER)");
		db.execSQL("create table if not exists playhistoryinfos(_mid VARCHAR(20) primary key,_mediatype VARCHAR(20),_medianame VARCHAR(20),_hashid VARCHAR(20),_taskname VARCHAR(20),_fsp VARCHAR(200),_playedtimestring VARCHAR(20),_playedtime INTEGER,_position INTEGER,_movieposition INTEGER,_movieplayedtime INTEGER,_size String,_percent String,_purl String)");
		
		
		db.execSQL("create table download(_id integer primary key autoincrement,_state int,_amount int,_filelength int,_url String,_filename String,_medianame String,_taskname String,_picture String,_hashid String,_mid String,_mtype String,_durl String)");
		//创建电影、电视剧、动漫、综艺筛选历史四个数据表
		db.execSQL(createMovieFilterTable);
		db.execSQL(createTvFilterTable);
		db.execSQL(createCartoonTable);
		db.execSQL(createVarietyTable);
		//创建微视频相关数据库表
		db.execSQL(getMicroVideoTable("entertainmentfilterhistory"));
		db.execSQL(getMicroVideoTable("sportfilterfilterhistory"));
		//创建离线缓存各数据表
//		theFifthUpgrade(db);
		//创建浏览器数据库表
		db.execSQL(createBrowserTable);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		switch(arg2) {
		case 1:
		case 2:
		case 3:
		case 4:
			firstFourTimesUpgrade(db);
			break;
		case 5:
			theFifthUpgrade(db);
			break;
		}
	}
	
	//前四次更新数据库的的代码
	private void firstFourTimesUpgrade(SQLiteDatabase db) {
		db.execSQL("create table if not exists download(_id integer primary key autoincrement,_state int,_amount int,_filelength int,_url String,_filename String,_medianame String,_taskname String,_picture String,_hashid String,_mid String,_mtype String,_durl String)");
		db.execSQL("insert into download(_id,_state,_amount,_filelength,_url,_filename,_medianame,_taskname,_picture) select _id,_state,_amount,_filelength,_url,_filename,_medianame,_taskname,_picture from downloadinfos");
		
		db.execSQL("DROP TABLE IF EXISTS playhistoryinfos");
		db.execSQL("DROP TABLE IF EXISTS downloadinfos");
//		onCreate(db);
		
		db.execSQL("create table if not exists playhistoryinfos(_mid VARCHAR(20) primary key,_mediatype VARCHAR(20),_medianame VARCHAR(20),_hashid VARCHAR(20),_taskname VARCHAR(20),_fsp VARCHAR(200),_playedtimestring VARCHAR(20),_playedtime INTEGER,_position INTEGER,_movieposition INTEGER,_movieplayedtime INTEGER,_size String,_percent String,_purl String)");
		
		//创建电影、电视剧、动漫、综艺筛选历史四个数据表
		db.execSQL(createMovieFilterTable);
		db.execSQL(createTvFilterTable);
		db.execSQL(createCartoonTable);
		db.execSQL(createVarietyTable);
		db.execSQL(getMicroVideoTable("entertainmentfilterhistory"));
		db.execSQL(getMicroVideoTable("sportfilterfilterhistory"));
	}
	
	//第五次数据库版本升级的操作
	private void theFifthUpgrade(SQLiteDatabase dataBase) {
		dataBase.execSQL("create table if not exists mediaids(_id integer primary key autoincrement,_mid String,_mediatype String)");
		dataBase.execSQL("create table if not exists listpagemediainfos(_mid integer primary key not null,_pic String" +
				",_programtype String,_name String,_tinf String,_vinf String,_karma String,_country String" +
				",_rtime String, _cate String" + ",_upinfo String,_tvstation String)");
		dataBase.execSQL("create table if not exists layerpagemediainfos(_mid integer primary key not null" +
				",_mtype String,_name String,_nameen String,_nameot String,_programtype String,_picture String" +
				",_karma String,_votenum String,_lactor String, _compere String,_director String,_cates String" +
				",_rinfo String,_plots String,_behind String,_shareurl String, _totalplaynum String" +
				",_pinfos String,_stars String,_torrentdesc String,_relatemedia String)");
	}

}
