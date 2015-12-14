package cn.hi321.browser.utils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class StreamTools {
	/**
	 * 把一个输入流里面的内容 转化存放在一个byte数组里面
	 * @param is
	 * @return
	 * @throws Exception
	 */
	public static byte[] getBytes(InputStream is) throws Exception{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len =0;
		while((len = is.read(buffer))!=-1){
			baos.write(buffer, 0, len);
		}
		is.close();
		return baos.toByteArray();
		
	}
}
