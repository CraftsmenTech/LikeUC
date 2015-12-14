package cn.hi321.browser.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import cn.hi321.browser.model.DownloadInfo;
import cn.hi321.browser.ui.activities.HomeActivity;


public class DownloadDao {
	private SQLiteHelper helper;
	
	public DownloadDao(Context ctx) {
		if(null==helper) {
			helper = new SQLiteHelper(ctx);
		}
	}
	
	private synchronized SQLiteDatabase openDB() {
			if(null==helper) {
				helper = new SQLiteHelper(HomeActivity.INSTANCE);
			}
			return helper.getWritableDatabase();
	}
	
	/**
	 * 插入下载信息
	 */
	public void insertDownloadInfo(DownloadInfo info){
		if(null != info){
			SQLiteDatabase db = null;
			try {
				db = openDB();
				String sql = "insert into download(_state,_amount,_filelength,_url,_filename,_medianame,_taskname,_picture,_hashid,_mid,_mtype,_durl) values(?,?,?,?,?,?,?,?,?,?,?,?)" ;
				db.execSQL(sql, new Object[]{info.state,info.amount,info.fileLength,info.url,info.fileName,info.mediaName,info.taskName,info.picture,info.hashId,info.mid,info.mType,info.durl});
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				dbCloseWithTryCatch(db);
			}
		}
	}
	
	/**
	 * 判断是否是首次下载
	 */
//	public boolean isFirst(){
//		SQLiteDatabase db = helper.getWritableDatabase();
//		String sql = "select count(*) as c from downloadinfos" ;
//		Cursor cur = db.rawQuery(sql, null);
//		int count = 0 ;
//		if(cur.moveToNext()){
//			count = cur.getInt(0);
//		}
//		cur.close();
//		db.close();
//		return count == 0?true:false ;
//	}
	
	/**
	 * 判断是否是首次下载
	 */
	public boolean isFirst(String fileName){
		SQLiteDatabase db = null;
		Cursor cur = null;
		int count = 0 ;
		try {
			db = openDB();
			String sql = "select * from download where _filename = ?" ;
			cur = db.rawQuery(sql, new String[]{fileName});
			count = cur.getCount();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(cur != null) {
				cur.close();
			}
			dbCloseWithTryCatch(db);
		}
		return count == 0?true:false;
	}

	/**
	 * 查询下载信息
	 */
	public ArrayList<DownloadInfo> findDownloadInfos() {
		ArrayList<DownloadInfo> list = new ArrayList<DownloadInfo>();
		DownloadInfo info = null ;
		Cursor cur = null;
		SQLiteDatabase db = null;
		try {
			db = openDB();
			if(isExistTalbe(db)){;  
				String sql = "select _state,_amount,_filelength,_url,_filename,_medianame,_taskname,_picture,_hashid,_mid,_mtype,_durl from download" ;
				cur = db.rawQuery(sql, null);
				while(cur.moveToNext()){
					info = new DownloadInfo();
					info.state = cur.getInt(cur.getColumnIndex("_state"));
					info.amount = cur.getInt(cur.getColumnIndex("_amount"));
					info.fileLength = cur.getInt(cur.getColumnIndex("_filelength"));
					info.url = cur.getString(cur.getColumnIndex("_url"));
					info.fileName = cur.getString(cur.getColumnIndex("_filename"));
					info.mediaName = cur.getString(cur.getColumnIndex("_medianame"));
					info.taskName = cur.getString(cur.getColumnIndex("_taskname"));
					info.picture = cur.getString(cur.getColumnIndex("_picture"));
					info.hashId = cur.getString(cur.getColumnIndex("_hashid"));
					info.mid = cur.getString(cur.getColumnIndex("_mid"));
					info.mType = cur.getString(cur.getColumnIndex("_mtype"));
					info.durl = cur.getString(cur.getColumnIndex("_durl"));
					list.add(info);
				}
				cur.close();
				db.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(cur != null) {
				cur.close();
			}
			dbCloseWithTryCatch(db);
		}
		return list ;
	}

	private boolean isExistTalbe(SQLiteDatabase db) {
		boolean result = false;
		try {
			String sql = "select count(*) as c from Sqlite_master where type ='table' and name ='" + "download" + "' ";
			Cursor cursor = db.rawQuery(sql, null);
			if(null != cursor ){
				if (cursor.moveToNext()) {
					int count = cursor.getInt(0);
					if (count > 0) {
						result = true;
					}
				}
				cursor.close();
			}
			
		} catch (Exception e) {
		}
		return result;
	}

	/**
	 * 初始化下载信息到数据库
	 */
	public void iniDownloadInfos(List<DownloadInfo> downloadInfos) {
//		SQLiteDatabase db = helper.getWritableDatabase();
		if(null != downloadInfos){
			SQLiteDatabase db = openDB();
//		db.beginTransaction();
			try {
				for(DownloadInfo info : downloadInfos ){
					this.insertDownloadInfo(info);
				}
//			db.setTransactionSuccessful();
			} catch (Exception e) {
				e.printStackTrace();
			} finally{
				dbCloseWithTryCatch(db);
			}
		}
	}

	/**
	 * 更新下载信息
	 */
//	public void updateDownloadInfo(int index, int len) {
//		SQLiteDatabase db = helper.getWritableDatabase();
//		String sql = "update downloadinfos set _amount = ? where _index =  ?" ;
//		db.execSQL(sql, new Object[]{len,index});
//		db.close();
//	}
	
	/**
	 * 更新下载信息
	 */
	public void updateDownloadInfo(int len, String fileName) {
		SQLiteDatabase db = openDB();
		try {
			String sql = "update download set _amount = ? where _filename =  ?" ;
			db.execSQL(sql, new Object[]{len,fileName});
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbCloseWithTryCatch(db);
		}
	}
	
	/**
	 * 更新各个任务的下载量的大小
	 */
	public void updateDownloadAmount(ArrayList<DownloadInfo> downloadInfos) {
//		SQLiteDatabase db = helper.getWritableDatabase();
		if(null != downloadInfos){
			SQLiteDatabase db = openDB();
			db.beginTransaction();
			try {
				for(DownloadInfo info : downloadInfos ){
					String sql = "update download set _amount = ? where _filename =  ?" ;
					db.execSQL(sql, new Object[]{info.amount,info.fileName});
				}
				db.setTransactionSuccessful();
			}
			finally{
				dbCloseWithTryCatch(db);
			}
		}
	}
	
	/**
	 * 更新下载项的状态
	 */
	public void updateDownloadState(ArrayList<DownloadInfo> downloadInfos) {
//		SQLiteDatabase db = helper.getWritableDatabase();
		if(null != downloadInfos){
			SQLiteDatabase db = null;
			try {
				db = openDB();
				if(db != null){
					db.beginTransaction();
					for (DownloadInfo info : downloadInfos) {
						String sql = "update download set _state = ? where _filename =  ?";
						if (db.isOpen())
							db.execSQL(sql, new Object[] { info.state, info.fileName });
					}
					if (db.isOpen())
						db.setTransactionSuccessful();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}finally {
				dbCloseWithTryCatch(db);
			}
		}
	}
	
	/**
	 * 更新某一项下载项的状态
	 */
	public void updateState(DownloadInfo downloadInfo) {
//		SQLiteDatabase db = helper.getWritableDatabase();
		if(null != downloadInfo){
			SQLiteDatabase db = openDB();
			try {
				String sql = "update download set _state = ? where _filename =  ?" ;
				db.execSQL(sql, new Object[]{downloadInfo.state,downloadInfo.fileName});
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				dbCloseWithTryCatch(db);
			}
		}
	}
	
	/**
	 * 更新某一项下载项url和文件大小
	 */
	public void updateurl(DownloadInfo downloadInfo) {
		if(null != downloadInfo){
			SQLiteDatabase db = openDB();
			try {
				String sql = "update download set _url = ?,_filelength = ? where _filename =  ?" ;
				db.execSQL(sql, new Object[]{downloadInfo.url,downloadInfo.fileLength,downloadInfo.fileName});
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				dbCloseWithTryCatch(db);
			}
		}
	}
	
	/**
	 * 更新下载条目的索引值
	 */
	public void updateItemIndex(int index) {
//		SQLiteDatabase db = helper.getWritableDatabase();
		SQLiteDatabase db = openDB();
		try {
			String sql = "update download set _index = _index - 1 where _index =  ?" ;
			db.execSQL(sql, new Object[]{index});
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbCloseWithTryCatch(db);
		}
	}
	
	/**
	 * 清除数据
	 */
	public void clearData(){
//		SQLiteDatabase db = helper.getWritableDatabase();
		SQLiteDatabase db = openDB();
		try {
			String sql = "delete from download" ;
			db.execSQL(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbCloseWithTryCatch(db);
		}
	}
	
	/**
	 * 删除下载表
	 */
	public void deleteDownloadTable(){
		SQLiteDatabase db = openDB();
		try {
			db.execSQL("DROP TABLE IF EXISTS download");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(db != null)
				db.close();
		}
	}
	
	/**
	 * 删除数据
	 */
	public void delData(String filename) {
//		SQLiteDatabase db = helper.getWritableDatabase();
		SQLiteDatabase db = openDB();
		try {
			String sql = "delete from download where _filename = ?" ;
			db.execSQL(sql, new Object[]{filename});
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbCloseWithTryCatch(db);
		}
	}
	
	/**
	 * 文件从本地删除后重置数据库中的数据
	 */
	public void resetData(String filename) {
//		SQLiteDatabase db = helper.getWritableDatabase();
		SQLiteDatabase db = openDB();
		try {
			String sql = "update download set _amount = 0 where _filename = ?" ;
			db.execSQL(sql, new Object[]{filename});
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbCloseWithTryCatch(db);
		}
	}
	
	/**
	 * 查询数据库中数据条目的总个数
	 */
	public int totalCount() {
//		SQLiteDatabase db = helper.getWritableDatabase();
		int count = 0;
		Cursor cur = null;
		SQLiteDatabase db = openDB();
		try {
			String sql = "select * from download" ;
			cur = db.rawQuery(sql, null);
			count = cur.getCount();
			return count;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(cur!=null) {
				cur.close();
			}
			dbCloseWithTryCatch(db);
		}
		return count;
	}
	
	public HashMap<String, String> getDownloadEpisode(String medianame){
		HashMap<String, String> depisode = new HashMap<String, String>();
		Cursor cur = null;
		SQLiteDatabase db = openDB();
		try {
			
			String sql = "select * from download where _medianame = ?";
			cur = db.rawQuery(sql, new String[]{medianame});
			while(cur.moveToNext()){
				String mediaName = cur.getString(cur.getColumnIndex("_medianame"));
				String taskName = cur.getString(cur.getColumnIndex("_taskname"));
				depisode.put(taskName, medianame);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(cur != null){
				cur.close();
			}
			dbCloseWithTryCatch(db);
		}
		return depisode;
	}
	
	private void dbCloseWithTryCatch(SQLiteDatabase targetDb) {
		try {
			if (targetDb != null) {
//				targetDb.endTransaction();
				targetDb.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
