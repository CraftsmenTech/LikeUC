package cn.hi321.browser.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import cn.hi321.browser.model.PlayHistoryInfo;
import cn.hi321.browser.utils.LogUtil;
/**
 * 	
 * @author yanggf and lushengbing
 *
 */
public class PlayHistoryDao {
	/**
	 * SQLite help class
	 */
	private static SQLiteHelper helper;
	
	private  SQLiteDatabase mUpdateByHashidDb = null;
	private  SQLiteDatabase findByHashidDb = null;
	private  SQLiteDatabase insertDb = null;
	
	private static PlayHistoryDao mInstance = null;
	
	
	public synchronized static PlayHistoryDao getInstance (Context ctx) {
		if (null == mInstance ) {
			mInstance = new PlayHistoryDao(ctx);
		}
		return mInstance;
	}
	
	private PlayHistoryDao(Context ctx) {
		helper = SQLiteHelper.getInstance(ctx);
	}
	
	
	/**
	 * Insert the playback history information
	 */
	public synchronized void insert(PlayHistoryInfo info){
		SQLiteDatabase db = null;
		try {
			db = helper.getWritableDatabase();
			db.beginTransaction();
			String sql = "replace into playhistoryinfos(_mid,_mediatype,_medianame,_hashid,_taskname,_fsp,_playedtimestring,_playedtime,_position,_movieposition,_movieplayedtime,_size,_percent,_purl) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)" ;
			db.execSQL(sql, new Object[]{info.getMid(),info.getMediatype(),info.getMedianame(),info.getHashid(),info.getTaskname(),info.getFsp(),info.getLanguage(),info.getPlayedtime(),info.getPosition(),info.getMovie_position(),info.getMovie_playedtime(),info.getSize(),info.getPercent(),info.getPurl()});
			db.setTransactionSuccessful();
		} catch (Exception e) {
			if (null != db) {
				db.endTransaction();
			}
			e.printStackTrace();
		}finally{
			if (null != db) {
				db.endTransaction();
			}
			if (db != null && !db.isDbLockedByOtherThreads() && db.isOpen())
			db.close();
		}
	}
	
	/**
	 * Query the database have to play in the history of the total number of records
	 */
	public int findTota(){
		SQLiteDatabase db = null;
		Cursor cur = null;
		int count = 0 ;
		try {
			db = helper.getWritableDatabase();
			String sql = "select count(*) as c from playhistoryinfos" ;
		    cur = db.rawQuery(sql, null);
			if(cur.moveToNext()){
				count = cur.getInt(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return count;
		}finally{
			if(cur != null)
			cur.close();
			if (db != null && !db.isDbLockedByOtherThreads() && db.isOpen())
			db.close();
			
		}
		return count;
	}
	
	
	/**
	 * Query the database, all player history in mind and return a collection of
     * Desc descending order, other default ascending
	 */
	
	public synchronized List<PlayHistoryInfo> findByOrder(String order) {
		SQLiteDatabase db = null;
		Cursor cur = null;
		List<PlayHistoryInfo> list = null;
		try {
			 list = new ArrayList<PlayHistoryInfo>();
			 db = helper.getWritableDatabase();
			 String sql = null;
			 if(order!=null){
				 if("desc".equals(order)){
//					 _mid,_mediatype,_medianame,_hashid,_taskname,_fsp,_playedtimestring,_playedtime,_position,_movieposition,_movieplayedtime) values(?,?,?,?,?,?,?,?,?,?,?)" ;
					 sql ="select _mid,_mediatype,_medianame,_hashid,_taskname,_fsp,_playedtimestring,_playedtime,_position,_movieposition,_movieplayedtime ,_size,_percent,_purl from playhistoryinfos order by _playedtime desc"; 
				 }else{
					 sql ="select _mid,_mediatype,_medianame,_hashid,_taskname,_fsp,_playedtimestring,_playedtime,_position,_movieposition,_movieplayedtime,_size ,_percent ,_purl  from playhistoryinfos order by _playedtime";
				 }
			 }else{
				 sql ="select _mid,_mediatype,_medianame,_hashid,_taskname,_fsp,_playedtimestring,_playedtime,_position,_movieposition,_movieplayedtime,_size ,_percent ,_purl  from playhistoryinfos order by _playedtime";
			 }
			 
			 cur = db.rawQuery(sql, new String[]{});
			
			if (cur != null) {  
				cur.moveToFirst();  
		        for (int i=0; i<cur.getCount(); i++) {  
		        	PlayHistoryInfo info = new PlayHistoryInfo();
					info.setMid(cur.getString(cur.getColumnIndex("_mid")));
					info.setMediatype(cur.getString(cur.getColumnIndex("_mediatype")));
					info.setMedianame(cur.getString(cur.getColumnIndex("_medianame")));
					info.setHashid(cur.getString(cur.getColumnIndex("_hashid")));
					info.setTaskname(cur.getString(cur.getColumnIndex("_taskname")));
					info.setFsp(cur.getString(cur.getColumnIndex("_fsp")));
					info.setLanguage(cur.getString(cur.getColumnIndex("_playedtimestring")));
					info.setPlayedtime(cur.getLong(cur.getColumnIndex("_playedtime")));
					info.setPosition(cur.getLong(cur.getColumnIndex("_position")));
					info.setMovie_position(cur.getInt(cur.getColumnIndex("_movieposition")));
					info.setMovie_playedtime(cur.getLong(cur.getColumnIndex("_movieplayedtime")));
					info.setSize(cur.getString(cur.getColumnIndex("_size")));
					info.setPercent(cur.getString(cur.getColumnIndex("_percent")));
					info.setPurl(cur.getString(cur.getColumnIndex("_purl")));
					list.add(info);
		            cur.moveToNext();  
		        }  
		      
		    }  
			
		} catch (Exception e) {
			e.printStackTrace();
			return list ;
		}finally{
			if(cur!=null)
			cur.close();
			if (db != null && !db.isDbLockedByOtherThreads() && db.isOpen())
			db.close();
		}
		return list ;
	}
	
	
	/**
	 *  The hashid query the database records
	 */
	public  synchronized PlayHistoryInfo findByHashid(String mid) {
		SQLiteDatabase db = null;
	  	Cursor cur  = null;
	  	LogUtil.i("PlayHistoryDao findByHashid()","findMid="+mid);
       try {
    	   db = helper.getReadableDatabase();
    	   cur = db.query("playhistoryinfos", new String[] { "_mid","_mediatype",
  				"_medianame","_hashid","_taskname","_fsp","_playedtimestring","_playedtime","_position","_movieposition","_movieplayedtime","_size ","_percent","_purl"}, "_mid=?",
  				new String[] { String.valueOf(mid) }, null, null, null);
  		if (cur.moveToNext()) {
  			
  			return new PlayHistoryInfo( cur.getString(0),cur.getString(1), cur.getString(2),
  					cur.getString(3),cur.getString(4),cur.getString(5),cur.getString(6), cur.getLong(7),cur.getLong(8),cur.getInt(9),cur.getLong(10),cur.getString(11),cur.getString(12),cur.getString(13));
  		}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}finally{
			if(cur!=null)
				cur.close();
			if (db != null && !db.isDbLockedByOtherThreads() && db.isOpen())
				db.close();
			}
		return null;
	}
	/**
	 *  According to a hashid modify database records
	 */
	public  synchronized  void  updateByHashid(String hashid,PlayHistoryInfo info) {
		SQLiteDatabase db  = null;
     try {
    	 db = helper.getWritableDatabase();
    	 db.beginTransaction();
 		ContentValues contentValues = new ContentValues();
 		contentValues.put("_medianame", info.getMedianame());
 		contentValues.put("_mediatype", info.getMediatype());
 		contentValues.put("_hashid", info.getHashid());
 		contentValues.put("_taskname", info.getTaskname());
 		contentValues.put("_fsp", info.getFsp());
 		contentValues.put("_playedtimestring", info.getLanguage());
 		contentValues.put("_playedtime", System.currentTimeMillis());
 		contentValues.put("_position", info.getPosition());
 		contentValues.put("_movieposition", info.getMovie_position());
 		contentValues.put("_movieplayedtime", info.getMovie_playedtime());
 		contentValues.put("_size", info.getSize());
// 		contentValues.put("_percent", info.getPercent());
 		contentValues.put("_purl", info.getPurl());
 		db.update("playhistoryinfos", contentValues, "_mid=?",
 				new String[] { hashid  });
 		db.setTransactionSuccessful();
		} catch (Exception e) {
			if (null != db) {
				db.endTransaction();
			}
			e.printStackTrace();
		} finally {
			if (null != db) 
				db.endTransaction();
			if (db != null && !db.isDbLockedByOtherThreads() && db.isOpen()) {
				db.close();
			}
		}
		
	}
	/**
	 * 根据mid 更新下载以缓冲的百分比
	 * add by jiyx at 2012-8-31 16:24:28
	 * @param hashid
	 * @param info
	 */
	public  synchronized void updateByHashid(String hashid,String info) {
		SQLiteDatabase db  = null;
     try {
        db = helper.getWritableDatabase();
        db.beginTransaction();
 		ContentValues contentValues = new ContentValues();	
 		if(null != info && !"".equals(info)){
 			contentValues.put("_percent", info);
 		} 		
 		db.update("playhistoryinfos", contentValues, "_mid=?",
 				new String[] { hashid  });
 		db.setTransactionSuccessful();
		} catch (Exception e) {
			if (null != db) {
				db.endTransaction();
			}
			e.printStackTrace();
		}finally{
			if (null != db) {
				db.endTransaction();
			}
			if (db != null && !db.isDbLockedByOtherThreads() && db.isOpen())
			db.close();
		}
		
	}
	
	/**
	 *  According to records in the database mid clear
	 */
	public  synchronized  void delete(String mid) {
		SQLiteDatabase db = null;
	  	LogUtil.i("PlayHistoryDao  delete()","deleteMid="+mid);
		try {
			db = helper.getWritableDatabase();
			String sql = "delete from playhistoryinfos where _mid=?" ;
			db.execSQL(sql, new Object[]{mid});
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if (db != null && !db.isDbLockedByOtherThreads() && db.isOpen())
			 db.close();
			
		}
	}
	
	/**
	 * According to records in the database mid clear   add by donggx
	 * @param hashId
	 */
	public  void deleteHashId(String hashId) {
		SQLiteDatabase db = null;
	  	LogUtil.i("PlayHistoryDao  deleteHashId()","hashId="+hashId);
		try {
			db = helper.getWritableDatabase();
			String sql = "delete from playhistoryinfos where _hashid=?" ;
			db.execSQL(sql, new Object[]{hashId});
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if (db != null && !db.isDbLockedByOtherThreads() && db.isOpen()){
//				db.close();
			}
			 
			
		}
	}
	/**
	 * When more data than the auto bar, clear the table in the auto after all
	 */
	
	public void autoDelete(int auto){
		int autos = auto;
		int tota =findTota();
		if(tota>=autos){
			List<PlayHistoryInfo>  in =	findByOrder("desc");
			for(int i =autos; i<in.size();i++){
				PlayHistoryInfo info =in.get(i);
				String hashid =info.getMid();
				delete(hashid);
				
			}
			
		}
		
	}
	
	
	/**
	 * Clear all data in the table
	 */
	public void deleteAllData(){
		SQLiteDatabase db = null;
		try {
			db = helper.getWritableDatabase();
			String sql = "delete from playhistoryinfos" ;
			db.execSQL(sql);
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(db!=null)
			 db.close();
			
		}
	}
	
	public void closeDb() {
		if (null != mUpdateByHashidDb
				&& !mUpdateByHashidDb.isDbLockedByOtherThreads()
				&& !mUpdateByHashidDb.isDbLockedByCurrentThread()
				&& mUpdateByHashidDb.isOpen()) {
			mUpdateByHashidDb.close();
		}
		
		if (null != findByHashidDb
				&& !findByHashidDb.isDbLockedByCurrentThread()
				&& !findByHashidDb.isDbLockedByOtherThreads()
				&& findByHashidDb.isOpen()) {
			findByHashidDb.close();
		}
		
		if (null != insertDb && !insertDb.isDbLockedByCurrentThread()
				&& !insertDb.isDbLockedByOtherThreads() && insertDb.isOpen()) {
			insertDb.close();
		}
	}
}
