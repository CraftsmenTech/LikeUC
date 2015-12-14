package cn.hi321.browser.download;

import java.io.Serializable;

/**
 * 数据库下载的实体类，与之相关的类：MediaItem 和 DownloadData
 * 
 * @author yanggf
 * 
 */
public class DownloadEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	// private String mid;
	//
	// private String medianame;
	//
	// private String hashid;
	//
	// private String taskname;

	// private String purl;// 可直接播放的地址

	private int downloaded;

	// private long fileSize;
	//
	// private String downloadUrl;
	//
	// private String mediatype;
	//
	// private String displayName;
	//
	// private String eid;// 分集Id

	private boolean userPauseWhen3G = false;

	private int status = DownloadJob.INIT;

	private boolean fromStart;

	private String id;

	private String contentDisposition;

	private String mimetype;

	private String userAgent;

	private String name;

	private String url;

	private long contentLength;

	private String srcPath;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getContentDisposition() {
		return contentDisposition;
	}

	public void setContentDisposition(String contentDisposition) {
		this.contentDisposition = contentDisposition;
	}

	public String getMimetype() {
		return mimetype;
	}

	public void setMimetype(String mimetype) {
		this.mimetype = mimetype;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public long getContentLength() {
		return contentLength;
	}

	public void setContentLength(long contentLength) {
		this.contentLength = contentLength;
	}

	public String getSrcPath() {
		return srcPath;
	}

	public void setSrcPath(String srcPath) {
		this.srcPath = srcPath;
	}

	// public String getMid() {
	// return mid;
	// }
	//
	// public void setMid(String mid) {
	// this.mid = mid;
	// }
	//
	// public String getMedianame() {
	// return medianame;
	// }

	// public void setMedianame(String medianame) {
	// this.medianame = medianame;
	// }
	//
	// public String getHashid() {
	// return hashid;
	// }
	//
	// public void setHashid(String hashid) {
	// this.hashid = hashid;
	// }
	//
	// public String getTaskname() {
	// return taskname;
	// }
	//
	// public void setTaskname(String taskname) {
	// this.taskname = taskname;
	// }
	//
	// public String getPurl() {
	// return purl;
	// }
	//
	// public void setPurl(String purl) {
	// this.purl = purl;
	// }

	public int getDownloaded() {
		return downloaded;
	}

	public void setDownloaded(int downloaded) {
		this.downloaded = downloaded;
	}

	// public long getFileSize() {
	// return fileSize;
	// }
	//
	// public void setFileSize(long fileSize) {
	// this.fileSize = fileSize;
	// }
	//
	// public String getDownloadUrl() {
	// return downloadUrl;
	// }
	//
	// public void setDownloadUrl(String downloadUrl) {
	// this.downloadUrl = downloadUrl;
	// }
	//
	// public String getMediatype() {
	// return mediatype;
	// }
	//
	// public void setMediatype(String mediatype) {
	// this.mediatype = mediatype;
	// }
	//
	// public String getDisplayName() {
	// return displayName;
	// }
	//
	// public void setDisplayName(String displayName) {
	// this.displayName = displayName;
	// }

	public boolean isUserPauseWhen3G() {
		return userPauseWhen3G;
	}

	public void setUserPauseWhen3G(boolean userPauseWhen3G) {
		this.userPauseWhen3G = userPauseWhen3G;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public boolean isFromStart() {
		return fromStart;
	}

	public void setFromStart(boolean fromStart) {
		this.fromStart = fromStart;
	}

}
