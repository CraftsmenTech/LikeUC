package cn.hi321.browser.service;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import cn.hi321.browser.utils.StreamTools;
import cn.hi321.browser2.R;

public class Service {

	/**
	 * 采用http的get方式提交数据到服务器
	 * 
	 * @param name
	 * @return
	 */
	public static String loginByHttpGet(Context context, String name) {
		String result = null;
		try {
			String basepath = context.getResources().getString(
					R.string.serverurl);

			String newname = URLEncoder.encode(name);
			String path = basepath + newname ;
			URL url = new URL(path);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(5000);

			if (conn.getResponseCode() == 200) {
				InputStream is = conn.getInputStream();
				byte[] data = StreamTools.getBytes(is);// 服务器端返回的数据是gbk的数据
				result = new String(data,"GBK");
			} else {
				result = "服务器内部错误";
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = "连接服务器异常";
		}

		return result;
	}

	

	/**
	 * 采用httpclient的方式 提交数据到服务器
	 * 
	 * @param context
	 * @return
	 */
	public static String loginByHttpClientGet(Context context, String name) {
		String result = null;
		// 1.打开一个浏览器.
		HttpClient client = new DefaultHttpClient();
		// 2.输入一个地址.
		String basepath = context.getResources().getString(R.string.serverurl);

		String newname = URLEncoder.encode(name);
		String path = basepath + newname ;
		HttpGet httpGet = new HttpGet(path);

		// 3.敲回车
		try {
			HttpResponse response = client.execute(httpGet);
			int code = response.getStatusLine().getStatusCode();
			if (code == 200) {
				InputStream is = response.getEntity().getContent();
				result = new String(StreamTools.getBytes(is),"GBK");
			} else {
				result = "服务器状态错误";
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = "获取数据失败";
		}
		return result;
	}

	
}
