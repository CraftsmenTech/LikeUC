
package cn.hi321.browser.db;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import cn.hi321.browser.ui.activities.HomeActivity;
import cn.hi321.browser.utils.LogUtil;

public  class BrowserDao {

	private SQLiteHelper helper;
	public BrowserDao(Context context) {
		if(null==helper) {
			helper = new SQLiteHelper(context);
		}
	}
	private synchronized SQLiteDatabase openDB() {
		if(null==helper) {
			helper = new SQLiteHelper(HomeActivity.INSTANCE);
		}
		return helper.getWritableDatabase();
	}
	/**
	 * 插入搜索记录信息
	 */
	public void insertSearchsInfo(String search){
		if(null != search){
			SQLiteDatabase db = null;
			try {
				db = openDB();
				String sql = "select * from searchs where search =?";
				Cursor cur = db.rawQuery(sql, new String[]{search});
				if(cur.getCount()>0)
					sql = "update searchs set data=? where search=?" ;
				else
					sql = "insert into searchs(data,search) values(?,?)";
				db.execSQL(sql, new Object[]{System.currentTimeMillis(),search});
				LogUtil.i("insert");
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				dbCloseWithTryCatch(db);
			}
		}
	}
	/**
	 * 清除数据
	 */
	public void clearData() {
//		SQLiteDatabase db = helper.getWritableDatabase();
		SQLiteDatabase db = openDB();
		try {
			String sql = "delete from searchs" ;
			db.execSQL(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbCloseWithTryCatch(db);
		}
	}
	
	public synchronized List<String> getSearch(int count,String where){
//		String[] searchs = new String[10];
		List<String> searchs = new ArrayList<String>();
		
//		int i=0;
		Cursor cur = null;
		SQLiteDatabase db = openDB();
		try {
			String sql = "select * from searchs ";
			if(where!=null)
				sql+=" where "+where;
			sql+=" order by data desc limit ?";
			cur = db.rawQuery(sql, new String[]{count+""});
			while(cur.moveToNext()){
				String search = cur.getString(cur.getColumnIndex("search"));
//				long data = cur.getLong(cur.getColumnIndex("data"));
				searchs.add(search);
//				i++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(cur != null){
				cur.close();
			}
			dbCloseWithTryCatch(db);
		}
		return searchs;
	}

	private synchronized void dbCloseWithTryCatch(SQLiteDatabase targetDb) {
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
