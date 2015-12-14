package cn.hi321.browser.download;

import java.io.File;
import java.text.DecimalFormat;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;
import cn.hi321.browser.utils.DeviceInfoUtil;
import cn.hi321.browser.utils.FileUtil;
import cn.hi321.browser2.R;

/**
 * 下载模块的sdCard容量管理
 * 
 * @author yanggf
 * 
 */
public class ContainSizeManager {

	// 容量相关：
	// 视图
	private TextView sdcardView; // 视图
	// 数据
	// private double 321BrowserSize, freeSize; //
	// 总容量，其他程序容量，风行容量，剩余大小(精度：2位,单位：G)
	private String funshionStr, freeStr; // (对应上面的数据的字符串表示)
	private DecimalFormat df;
	private Activity view;

	public ContainSizeManager(Activity view) {
		// 处理精度
		df = new DecimalFormat();
		df.setMinimumFractionDigits(2);
		df.setMaximumFractionDigits(2);
		this.view = view;
	}

	// SDCard是否存在
	public boolean isExistSdcard() {

		return FileUtil.checkSDCard();
	}

	/*
	 * // SDCard总大小(单位：GB,2位精度) public void countTotalSize() {
	 * 
	 * double total = Utils.getTotalExternaMemory() / 1024; totalStr =
	 * df.format(total); }
	 */

	// SDCard剩余空间大小（单位：GB，2位精度）
	public void countFreeSize() {

		double free = DeviceInfoUtil.getAvailableExternalMemory() / 1024;
		if (df != null)
			freeStr = df.format(free);
		// freeSize = Double.valueOf(freeStr);
	}

	// SDCard\321Browser程序所占的空间大小（单位：GB，2位精度）
	public void count321BrowserSize() {

		double browser = 0;
		File browserDir = new File(FileUtil.FINAL_SAVE_MEDIA_PATH);
		if (browserDir.exists() && browserDir.isDirectory()) {

			browser = getFileDirSize(browserDir) / (1024 * 1024 * 1024);
		}
		if (df != null)
			funshionStr = df.format(browser);

	}

	// 递归获取某个文件夹的大小(单位：B)
	private double getFileDirSize(File fileDir) {
		long size = 0;
		File flist[] = fileDir.listFiles();
		for (File file : flist) {
			if (file.isDirectory()) {
				size += getFileDirSize(file);
			} else {
				size += file.length();
			}
		}
		return size;
	}

	public void ansynHandlerSdcardSize() {

		// 如果Sdcard卡存在
		if (isExistSdcard()) {
			anysnUpdateSizeView();
		} else {
			// TODO
		}
	}

	public void anysnUpdateSizeView() {

		new Thread() {
			public void run() {
				// 取得相关值
				count321BrowserSize();
				countFreeSize();
				ansynUpdateHandler.post(ansynUpdateUi);
			}
		}.start();
	}

	Runnable ansynUpdateUi = new Runnable() {

		@Override
		public void run() {
			// 找到视图并赋值
			sdcardView = (TextView) view.findViewById(R.id.sizeShow);
			sdcardView.setText("已下载：" + funshionStr + "G" + " 剩余空间：" + freeStr
					+ "G");

		}
	};

	/*
	 * Runnable ansynUpdate = new Runnable() {
	 * 
	 * @Override public void run() { try { // 取得相关值 count321BrowserSize();
	 * countFreeSize(); // 找到视图并赋值 // 总大小 sdcardView = (TextView)
	 * view.findViewById(R.id.sizeShow); sdcardView.setText(totalStr + "G");
	 * 
	 * } catch (Exception e) { e.printStackTrace(); }
	 * 
	 * } };
	 */

	// 处理容量更新的Handler
	Handler ansynUpdateHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
		}

	};

}
