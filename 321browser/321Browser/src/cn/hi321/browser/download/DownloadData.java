package cn.hi321.browser.download;

import java.util.List;

/**
 * durl请求返还的json对象
 * 
 * @author yanggf
 * 
 */
public class DownloadData {

	private String name;
	private int fileSize;
	private List<String> downloadUrl;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getFileSize() {
		return fileSize;
	}

	public void setFileSize(int fileSize) {
		this.fileSize = fileSize;
	}

	public List<String> getDownloadUrl() {
		return downloadUrl;
	}

	public void setDownloadUrl(List<String> downloadUrl) {
		this.downloadUrl = downloadUrl;
	}

	@Override
	public String toString() {
		return "DownloadData [name=" + name + ", fileSize=" + fileSize
				+ ", downloadUrl=" + downloadUrl + "]";
	}

}
