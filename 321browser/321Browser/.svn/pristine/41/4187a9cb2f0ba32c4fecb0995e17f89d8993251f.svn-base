package cn.hi321.browser.player;

import android.net.TrafficStats;

/**
 * 
 * @author yanggf 流量统计工具类
 */
public class TrafficStatsUtil {

	private static final String TAG = "TrafficStatsUtil";

	private static int UNSUPPORTED = -1;
	/**
	 * 首次接受到的字节数
	 */
	private static long mPreRxBytes = UNSUPPORTED;

	/**
	 * 计算当前网速
	 */
	public static String countCurRate() {
		String curRate = "-1B/s";
		try {
			// 当前接受到的字节总数
			long curRxBytes = TrafficStats.getTotalRxBytes();
			// LogUtil.v(TAG, "接收到的总字节数"+curRxBytes);
			// 这次接受到的字节数
			if (mPreRxBytes == UNSUPPORTED) {
				mPreRxBytes = curRxBytes;
			}
			long curRateBytes = curRxBytes - mPreRxBytes;
			// 更新上一次接收到的字节总数
			mPreRxBytes = curRxBytes;
			if (curRateBytes < 1024) {
				// LogUtil.v(TAG, "当前网速" + curRateBytes + "B/s");
				curRate = curRateBytes + "B/s";
			}
			if (curRateBytes >= 1024 && curRateBytes < 1024 * 1024) {
				// LogUtil.v(TAG, "当前网速" + curRateBytes / 1024 + "K/s");
				curRate = curRateBytes / 1024 + "K/s";
			}
			if (curRateBytes >= 1024 * 1024) {
				// LogUtil.v(TAG, "当前网速" + curRateBytes / (1024 * 1024) +
				// "M/s");
				curRate = curRateBytes / (1024 * 1024) + "M/s";
			}

		} catch (NoClassDefFoundError e) {
			mPreRxBytes = UNSUPPORTED;
			e.printStackTrace();
			return "";
		} catch (Exception e) {
			mPreRxBytes = UNSUPPORTED;
			e.printStackTrace();
			return "";
		}
		return curRate;
	}

	/**
	 * 计算当前的网速
	 * 
	 * @param startPos
	 * @param endtime
	 * @return
	 */
	public static long getAverageRateSpeed(long startPos, long endtime) {
		long averageRateSpeed;
		try {
			// 当前接受到的字节总数
			long curRateBytes = TrafficStats.getTotalRxBytes() - startPos;
			double time = ((double) System.currentTimeMillis() - (double) endtime);
			averageRateSpeed = (int) ((double) curRateBytes / time);
		} catch (NoClassDefFoundError e) {
			e.printStackTrace();
			return -1;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
		return averageRateSpeed;
	}

}
