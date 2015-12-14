package cn.hi321.browser.player;


import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 
 * @author yangguangfu
 *
 */
public class ProxyUtils {
	/**
	 * 获取重定向后的URL，即真正有效的链接
	 * @param urlString
	 * @return
	 */
    public static String getRedirectUrl(String urlString){
    	URL url;
		try {
			url = new URL(urlString);
			HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setInstanceFollowRedirects(false);
			if(urlConnection.getResponseCode()==HttpURLConnection.HTTP_MOVED_PERM)
				return urlConnection.getHeaderField("Location");
			
			if(urlConnection.getResponseCode()==HttpURLConnection.HTTP_MOVED_TEMP)
				return urlConnection.getHeaderField("Location");
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return urlString;
		}
    	
    	return urlString;
    }
    
}
