package cn.hi321.browser.download;

import java.util.ArrayList;

/**
 * 数据库增删改查
 * 
 */
public interface DownloadDao {

	public boolean add(DownloadEntity entry);

	public void setStatus(DownloadEntity entry, int status);

	/**
	 * Pulls all Download Jobs from the database.
	 * 
	 * @return
	 */
	public ArrayList<DownloadJob> getAllDownloadJobs();

	/**
	 * Removes the passed job from the database
	 * 
	 * @param job
	 */
	public void remove(DownloadJob job);

}
