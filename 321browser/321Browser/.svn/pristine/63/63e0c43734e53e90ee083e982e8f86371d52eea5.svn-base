package cn.hi321.browser.download;

import android.content.ContentValues;
import android.database.Cursor;

public class DownloadEntityDBBuilder {

	private static final String DOWNLOADED = "downloaded";

	private final static String HASHID = "hashId";// id
	private final static String MEDIA_MID = "media_mid";// userAgent
	private final static String DISPLAY_NAME = "display_name";// name
	private final static String MEDIA_NAME = "media_name";// srcPath
	private final static String FILE_LENGTH = "file_length";// contentLength
	private final static String DOWNLOAD_URL = "download_url";// uri
	private final static String MEDIA_TASKNAME = "media_taskname";// contentDisposition
	private final static String REQ_DOWNLOAD_URL = "request_downloadInfo_url";
	private final static String SERIALID = "serialid";
	private static final String MEDIA_TYPE = "media_type";// mimetype

	public ContentValues deconstruct(DownloadEntity entity) {
		ContentValues values = new ContentValues();
		values.put(HASHID, entity.getId());
		values.put(MEDIA_MID, entity.getUserAgent());
		values.put(DISPLAY_NAME, entity.getName());
		values.put(MEDIA_NAME, entity.getSrcPath());
		values.put(FILE_LENGTH, entity.getContentLength());
		values.put(DOWNLOAD_URL, entity.getUrl());
		values.put(MEDIA_TASKNAME, entity.getContentDisposition());
		// values.put(REQ_DOWNLOAD_URL, entity.getPurl());

		values.put(MEDIA_TYPE, entity.getMimetype());
		return values;
	}

	public DownloadJob build(Cursor query) {

		DownloadEntity dEntry = buildDownloadEntity(query);

		DownloadJob dJob = new DownloadJob(dEntry,
				DownloadHelper.getDownloadPath(), 0);
		int progress = query.getInt(query.getColumnIndex(DOWNLOADED));
		if (progress == 1) {
			dJob.setProgress(100);
		} else if (progress == DownloadJob.PAUSE) {
			dEntry.setStatus(DownloadJob.PAUSE);
		}
		return dJob;
	}

	private DownloadEntity buildDownloadEntity(Cursor query) {

		int columnHashId = query.getColumnIndex(HASHID);
		int columnMid = query.getColumnIndex(MEDIA_MID);
		int columnDname = query.getColumnIndex(DISPLAY_NAME);
		int columnMname = query.getColumnIndex(MEDIA_NAME);
		int columnFilelength = query.getColumnIndex(FILE_LENGTH);
		int columnUrl = query.getColumnIndex(DOWNLOAD_URL);
		int columnTashname = query.getColumnIndex(MEDIA_TASKNAME);
		// int columnResUrl = query.getColumnIndex(REQ_DOWNLOAD_URL);
		int columnmimetype = query.getColumnIndex(MEDIA_TYPE);
		// int columnEid = query.getColumnIndex(SERIALID);

		DownloadEntity dEntity = new DownloadEntity();

		dEntity.setId(query.getString(columnHashId));
		dEntity.setUserAgent(query.getString(columnMid));
		dEntity.setName(query.getString(columnDname));
		dEntity.setSrcPath(query.getString(columnMname));
		dEntity.setContentLength(query.getInt(columnFilelength));
		dEntity.setUrl(query.getString(columnUrl));
		dEntity.setContentDisposition(query.getString(columnTashname));
		// dEntity.setPurl(query.getString(columnResUrl));
		dEntity.setMimetype(query.getString(columnmimetype));

		return dEntity;
	}

}
