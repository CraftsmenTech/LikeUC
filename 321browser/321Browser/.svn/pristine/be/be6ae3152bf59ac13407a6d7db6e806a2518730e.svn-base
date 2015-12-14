package cn.hi321.browser.model;

import java.io.Serializable;

/**
 * {"url":"http:\/\/192.168.133.122:80\/1111111111111111111111111111111111111111\/bianxingjingang3C.mp4",
 * "size":"160829086"}
 * @author yanggf
 */
public class PlayData implements Serializable {

	private static final long serialVersionUID = 1L;

	private String url;
	
	private String size;
	
	private String name;
	
	private String  videotype;
	
	private String mid;

	private boolean isLocalFile = false; //判断播放视频是否为本地文件
	
	private double watchablePercent;
	
//	private MircoVideoSpecial mircoVideoSpecial;
	
	public PlayData() {
	}

	public PlayData(String url) {
		this.url = url;
	}

	public String getVideotype() {
		return videotype;
	}

	public void setVideotype(String videotype) {
		this.videotype = videotype;
	}

	public String getMid() {
		return mid;
	}

	public void setMid(String mid) {
		this.mid = mid;
	}

	public boolean isLocalFile() {
		return isLocalFile;
	}

	public void setLocalFile(boolean isLocalFiel) {
		this.isLocalFile = isLocalFiel;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getWatchablePercent() {
		return watchablePercent;
	}

	public void setWatchablePercent(double watchablePercent) {
		this.watchablePercent = watchablePercent;
	}

	
	@Override
	public String toString() {
		return "PlayData [url=" + url + ", size=" + size + ", name=" + name
				+ "]";
	}

	

}
