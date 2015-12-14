package cn.hi321.browser.media.entity;

import java.io.Serializable;

/**
 * 
 * @author yanggf
 *
 *代表电视剧、综艺、动漫、某一集；
 *代表电视台某一台；
 *代码本地视频列表，某一视频
 */
public class MediaItem implements Serializable{
//	 title: "仁心解码国语",
//     url: "http://v.youku.com/v_show/id_XNTI4ODYyNzEy.html",
//     is_play: "1",
//     episode: "1",
//     img_url: "http://t3.baidu.com/it/u=1827644315,2744314791&fm=20",
//     tvid: "0",
//     download: "1",
//     sec: 29019,
//     di: "8a75e669d357d33f"
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2872872605249209107L;

	private String episode;//表示当前是第几集
	
	private String is_play;//表示当前正在播放的是第几集 
	
	//播放地址，但需要百度重定向
	private String  sourceUrl;
	
	//直接可以播放的播放地址
	private String url;
	
	//影片名称、电台名称、本地视频名称
	private String title;
	
	//是否是电视台直播
	private boolean isLive = false;
	
	private int flags;
	
	private String image;
	
	//文件大小
	private float fileSize;
	
	
	public String getSourceUrl() {
		return sourceUrl;
	}
	public void setSourceUrl(String sourceUrl) {
		this.sourceUrl = sourceUrl;
	}
	public float getFileSize() {
		return fileSize;
	}
	public void setFileSize(float fileSize) {
		this.fileSize = fileSize;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public int getFlags() {
		return flags;
	}
	public void setFlags(int flags) {
		this.flags = flags;
	}
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public boolean isLive() {
		return isLive;
	}
	public void setLive(boolean isLive) {
		this.isLive = isLive;
	}
	public String getEpisode() {
		return episode;
	}
	public void setEpisode(String episode) {
		this.episode = episode;
	}
	public String getIs_play() {
		return is_play;
	}
	public void setIs_play(String is_play) {
		this.is_play = is_play;
	}

	
	
}
