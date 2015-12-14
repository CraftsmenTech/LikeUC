package cn.hi321.browser.download;

import java.io.File;
import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import cn.hi321.browser.BrowserApp;

public class DownloadDaoImplWithoutContext implements DownloadDao {

	private final static String TAG = "DownloadDaoImplWithoutContext";

	private String mPath;

	private static final String TABLE_DOWNLOAD = "download";

	private static final int DB_VERSION = 1;

	private SQLiteDatabase mDb;

	/**
	 * Default constructor
	 * 
	 * @param path
	 * Database file
	 */
	public DownloadDaoImplWithoutContext(String path) {
		mPath = path;

		// mDb = getDb();
		mDb = getDbInMemory();
		if (mDb == null)
			return;

		if (mDb.getVersion() < DB_VERSION) {
			new UpdaterBuilder().getUpdater(DB_VERSION).update();
		}
	}

	@SuppressWarnings("unused")
	private SQLiteDatabase getDb() {
		File file = new File(DownloadHelper.getDownloadPath());
		if (!file.exists())
			file.mkdirs();
		try {
			return SQLiteDatabase.openDatabase(mPath, null,
					SQLiteDatabase.CREATE_IF_NECESSARY);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	private SQLiteDatabase getDbInMemory() {

		try {
			return BrowserApp.getInstance().openOrCreateDatabase(
					"download.db", Context.MODE_PRIVATE, null);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	private class DatabaseUpdaterV1 extends DatabaseUpdater {

		private static final int VERSION = 1;

		public DatabaseUpdaterV1() {
		}

		@SuppressWarnings("unused")
		public DatabaseUpdaterV1(DatabaseUpdater updater) {
			setUpdater(updater);
		}

		@Override
		public void update() {
			if (getUpdater() != null) {
				getUpdater().update();
			}

			try {
				mDb.execSQL("DROP TABLE " + TABLE_DOWNLOAD + ";");
			} catch (SQLiteException e) {
				Log.v(TAG, "Library table not existing");
			}
			createTables();
			mDb.setVersion(VERSION);
		}

		// modify by donggx 修改数据库字段，增加了媒体类型字段
		private void createTables() {
			mDb.execSQL("CREATE TABLE IF NOT EXISTS "
					+ TABLE_DOWNLOAD
					+ " (hashId VARCHAR UNIQUE, downloaded INTEGER,display_name VARCHAR, media_name VARCHAR,"
					+ " file_length INTEGER, media_mid VARCHAR,download_url VARCHAR,request_downloadInfo_url VARCHAR," 
					+ " media_taskname VARCHAR,media_type VARCHAR);");
		}

	}

	private class UpdaterBuilder {

		public DatabaseUpdater getUpdater(int version) {
			DatabaseUpdater updater = null;

			switch (version) {
			case 1:
				updater = new DatabaseUpdaterV1();
				break;
			case 0:
				updater = null;
				break;
			}

			if (version > mDb.getVersion() + 1) {
				updater.setUpdater(getUpdater(version - 1));
			}
			return updater;
		}
	}

	/**
	 * 没有就插入，有就更新
	 */
	@Override
	public boolean add(DownloadEntity entry) {
		if (mDb == null) {
			// database was not created
			return false;
		}
		// put DownloadEntry data the table
		ContentValues values = new ContentValues();
        if(!entry.isFromStart())
        	values.put("downloaded", entry.getDownloaded());
		values.putAll(new DownloadEntityDBBuilder().deconstruct(entry));

		String[] whereArgs = { "" + entry.getId() };
		// 更新
		long row_count = mDb.update(TABLE_DOWNLOAD, values, "hashId=?",
				whereArgs);

		// 插入
		if (row_count == 0) {
			mDb.insert(TABLE_DOWNLOAD, null, values);
		}

		return row_count != -1l;
	}

	/**
	 * 0：未完成，1：已完成  3：暂停
	 * @param entry
	 * @param i
	 */
	@Override
	public void setStatus(DownloadEntity entry, int status) {
		try {
			if (mDb == null) {
				return;
			}
			
			ContentValues values = new ContentValues();
			values.put("downloaded", status);

			String[] whereArgs = { "" + entry.getId() };
			int row_count = mDb.update(TABLE_DOWNLOAD, values, "hashId=?",
					whereArgs);

			if (row_count == 0) {
				Log.e(TAG, "Failed to update " + TABLE_DOWNLOAD);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public ArrayList<DownloadJob> getAllDownloadJobs() {
		ArrayList<DownloadJob> jobs = new ArrayList<DownloadJob>();
		if (mDb == null)
			return jobs;

		Cursor query = mDb.query(TABLE_DOWNLOAD, null, null, null, null, null,
				null);
		if (query.moveToFirst()) {
			while (!query.isAfterLast()) {
				jobs.add(new DownloadEntityDBBuilder().build(query));
				query.moveToNext();
			}
		}
		query.close();
		return jobs;
	}

	@Override
	public void remove(DownloadJob job) {
		if (mDb == null) {
			return;
		}

		String[] whereArgs = { "" + job.getEntity().getId() };
		mDb.delete(TABLE_DOWNLOAD, "hashId=?", whereArgs);
	}

}

/**
 * Database updater. Build around Decorator pattern.
 * 
 */
abstract class DatabaseUpdater {
	private DatabaseUpdater mUpdater;

	abstract void update();

	public void setUpdater(DatabaseUpdater mUpdater) {
		this.mUpdater = mUpdater;
	}

	public DatabaseUpdater getUpdater() {
		return mUpdater;
	}
}
