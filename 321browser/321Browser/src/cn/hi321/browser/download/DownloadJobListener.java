package cn.hi321.browser.download;

/**
 * 当下载开始或者完成时，通知栏监听器
 * 
 * @author yanggf
 * 
 */
public interface DownloadJobListener {

	/**
	 * Callback invoked when a download finishes
	 */
	public void downloadEnded(DownloadJob job);

	/**
	 * Callback invoked when a download starts
	 */
	public void downloadStarted(DownloadJob job);

	/**
	 * @param job
	 */
	public void downloadPaused(DownloadJob job);

	/**
	 * 正在下载中的任务通知初始化展示
	 */
	public void downloadOnDownloading(DownloadJob job);

	/**
	 * 更新正在下载中的任务通知
	 */
	public void updateNotifyOnDownloading(DownloadJob job);

	/**
	 * 暂停正在下载中的任务通知消失
	 */
	public void downloadOnPause(DownloadJob job);

}
