package cn.hi321.browser.media.entity;

import java.io.Serializable;
import java.util.ArrayList;

import android.provider.ContactsContract.Data;

/**
 * 
 * @author Administrator
 *
 */
public class Media implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8628787550419181465L;
	/**
	 * 
	 */
	private int cur_episode;
	private ArrayList<MediaItem> mMediaItemArrayList;
	//movie电影,tvplay电视,comic动漫,tvshow综艺,info新闻,amuse搞笑,music音乐,sport体育，woman美女
	private String mediaType; 
	
//	private boolean isClickPlayButton ;//点击播放按钮的时候isClickPlayButton = true;
	private int position;//表示当前在列表播放的位置

	
	
	private String id;
	private String title;
	private String img_url;
	private String intro; 
	private String duration;
	private String pubtime;
	private ArrayList<String> directorArr;
	private ArrayList<String> actorArr;
	private ArrayList<String> areaArr;
	private int season_num;
	private ArrayList<String> typeArr;
	private String rating;
	private String play_filter;
	private String foreign_ip;
//	private ArrayList<Sites> sitesArr  ; //表示播放视频来源
	
	public int getCur_episode() {
		return cur_episode;
	}
	public void setCur_episode(int cur_episode) {
		this.cur_episode = cur_episode;
	}
	public String getMax_episode() {
		return max_episode;
	}
	public void setMax_episode(String max_episode) {
		this.max_episode = max_episode;
	}
	private String max_episode;
	
	public String getIs_finish() {
		return is_finish;
	}
	public void setIs_finish(String is_finish) {
		this.is_finish = is_finish;
	}
	private String is_finish;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getImg_url() {
		return img_url;
	}
	public void setImg_url(String img_url) {
		this.img_url = img_url;
	}
	public String getIntro() {
		return intro;
	}
	public void setIntro(String intro) {
		this.intro = intro;
	}
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	public String getPubtime() {
		return pubtime;
	}
	public void setPubtime(String pubtime) {
		this.pubtime = pubtime;
	}
	public ArrayList<String> getDirectorArr() {
		return directorArr;
	}
	public void setDirectorArr(ArrayList<String> directorArr) {
		this.directorArr = directorArr;
	}
	public ArrayList<String> getActorArr() {
		return actorArr;
	}
	public void setActorArr(ArrayList<String> actorArr) {
		this.actorArr = actorArr;
	}
	public ArrayList<String> getAreaArr() {
		return areaArr;
	}
	public void setAreaArr(ArrayList<String> areaArr) {
		this.areaArr = areaArr;
	}
	public int getSeason_num() {
		return season_num;
	}
	public void setSeason_num(int season_num) {
		this.season_num = season_num;
	}
	public ArrayList<String> getTypeArr() {
		return typeArr;
	}
	public void setTypeArr(ArrayList<String> typeArr) {
		this.typeArr = typeArr;
	}
	public String getRating() {
		return rating;
	}
	public void setRating(String rating) {
		this.rating = rating;
	}
	public String getPlay_filter() {
		return play_filter;
	}
	public void setPlay_filter(String play_filter) {
		this.play_filter = play_filter;
	}
	public String getForeign_ip() {
		return foreign_ip;
	}
	public void setForeign_ip(String foreign_ip) {
		this.foreign_ip = foreign_ip;
	}
//	 
//	public ArrayList<Sites> getSitesArr() {
//		return sitesArr;
//	}
//	public void setSitesArr(ArrayList<Sites> sitesArr) {
//		this.sitesArr = sitesArr;
//	}
	
	public ArrayList<MediaItem> getMediaItemArrayList() {
		return mMediaItemArrayList;
	}
	public void setMediaItemArrayList(ArrayList<MediaItem> mMediaItemArrayList) {
		this.mMediaItemArrayList = mMediaItemArrayList;
	}
	public String getMediaType() {
		return mediaType;
	}
	public void setMediaType(String mediaType) {
		this.mediaType = mediaType;
	} 
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	@Override
	public String toString() {
		return "Media [cur_episode=" + cur_episode + ", mMediaItemArrayList="
				+ mMediaItemArrayList + ", mediaType=" + mediaType
				+ ", position=" + position + ", id=" + id + ", title=" + title
				+ ", img_url=" + img_url + ", intro=" + intro + ", duration="
				+ duration + ", pubtime=" + pubtime + ", directorArr="
				+ directorArr + ", actorArr=" + actorArr + ", areaArr="
				+ areaArr + ", season_num=" + season_num + ", typeArr="
				+ typeArr + ", rating=" + rating + ", play_filter="
				+ play_filter + ", foreign_ip=" + foreign_ip  
				 + ", max_episode=" + max_episode + ", is_finish="
				+ is_finish + "]";
	}
	
	
	
	 
}
