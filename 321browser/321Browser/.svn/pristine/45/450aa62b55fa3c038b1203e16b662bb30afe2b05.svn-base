package cn.hi321.browser.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.util.Base64;

/**
 * 
 * @author yanggf
 * 
 */

public class ImageUtil {
	private final static String TAG = "ImageUtils";
	public static int BITMAP_DENSITY = 160;
	private HashMap<String, Drawable> imgCache;
	
//	public static String getBitmapStrBase64(Bitmap bitmap) {
//		ByteArrayOutputStream baos = new ByteArrayOutputStream();
//		bitmap.compress(CompressFormat.PNG, 100, baos);
//		byte[] bytes = baos.toByteArray();
//		return new String(Base64.encode(bytes));
//	}


	public HashMap<String, Drawable> getImgCache() {
		if (imgCache == null) {
			File f = new File("imgs.dat");
			if (!f.exists()) {
				imgCache = new HashMap<String, Drawable>();
				try {
					f.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {

			}
		}
		return imgCache;
	}

	/**
	 * Image Compression
	 * 
	 * @param bitmap
	 *            Source Picture
	 * @param destWidth
	 *            Target width
	 * @param destHeigth
	 *            Target height
	 * @return
	 */
	public static Bitmap imageCompression(Bitmap bitmap, int destWidth,int destHeigth) {
		if (bitmap == null) {
			return null;
		}
		final int w = bitmap.getWidth();
		final int h = bitmap.getHeight();
		/* calculate the scale */
		final float scaleWidth = ((float) destWidth) / w;
		final float scaleHeight = ((float) destHeigth) / h;
		/* create a matrix for the manipulation */
		final Matrix m = new Matrix();
		/* resize the Bitmap */
		m.postScale(scaleWidth, scaleHeight);
		/* recreate the new Bitmap */
		final Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, m,
				true);
		return resizedBitmap;
	}

	/**
	 * Image transparency processing
	 * 
	 * @param sourceImg
	 *            The original image
	 * @param number
	 *            Transparency
	 * @return
	 */
	public static Bitmap setAlpha(Bitmap sourceImg, int number) {
		int[] argb = new int[sourceImg.getWidth() * sourceImg.getHeight()];
		sourceImg.getPixels(argb, 0, sourceImg.getWidth(), 0, 0,
				sourceImg.getWidth(), sourceImg.getHeight());// Pictures ARGB
																// value
		number = number * 255 / 100;
		for (int i = 0; i < argb.length; i++) {
			if ((argb[i] & 0xff000000) != 0x00000000) {// Not deal with
														// transparent color
				argb[i] = (number << 24) | (argb[i] & 0xFFFFFF);// Modify the
																// maximum value
																// of 2
			}
		}
		sourceImg = Bitmap.createBitmap(argb, sourceImg.getWidth(),
				sourceImg.getHeight(), Config.ARGB_8888);
		return sourceImg;
	}

	public static Bitmap drawableToBitmap(Drawable drawable) {

		Bitmap bitmap = Bitmap.createBitmap(
				drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight(),
				drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888: Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		// canvas.setBitmap(bitmap);
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight());
		drawable.draw(canvas);

		return bitmap;
	}
	
	/**
	 * 从网络下载图片
	 * 
	 * @param url
	 * @param file
	 * @throws IOException
	 */
	public static void getImageFromNet(String url, File file)
			throws IOException {
		InputStream is = null;
		URL imageUrl = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
		conn.setConnectTimeout(30000);
		conn.setReadTimeout(30000);
		conn.setInstanceFollowRedirects(true);
		is = conn.getInputStream();
		saveImage(is, file);
	}

	/**
	 * 图片下载到本地文件
	 * @param url
	 * @throws IOException
	 */
	public static void saveImage(InputStream is, File file) throws IOException {
		OutputStream os = null;
		if (!file.exists()) {
			file.createNewFile();
		}
		os = new FileOutputStream(file);
		CopyStream(is, os);
		if (os != null) {
			os.close();
		}
		if (is != null) {
			is.close();
		}
	}

	public static void CopyStream(InputStream is, OutputStream os)
			throws IOException {
		final int buffer_size = 1024;
		byte[] bytes = new byte[buffer_size];
		for (;;) {
			int count = is.read(bytes, 0, buffer_size);
			if (count == -1)
				break;
			os.write(bytes, 0, count);
		}
	}
	
	/**
	 * A BITMAP for access to the source image, no compression, the local
	 * picture
	 * @param sImagePath
	 * @return
	 */
	public static Bitmap getImgCacheFromLocal(String sImagePath) {
		try {
			final File f = new File(sImagePath);
			if (!f.exists()) {
				return null;
			}
			final FileInputStream fis = new FileInputStream(f);
			LogUtil.i(TAG, "fis==" + fis);
			final Bitmap bitmap = BitmapFactory.decodeStream(fis);
			fis.close();
			return bitmap;
		} catch (Exception ex) {
			ex.printStackTrace();
			LogUtil.e(TAG, ex.toString());
			return null;
		}
	}
	
	/**
	 * Save the image
	 * 
	 * @param sOldImagePath
	 * @param sNewImagePath
	 *            Image path formats such as /data/data/com.xxx/1.png
	 * @return
	 */
	public static boolean saveImage(Bitmap oldbitmap, String sNewImagePath) {
		LogUtil.e(TAG, "cunrubendihuancun" + sNewImagePath);
		try {
			final FileOutputStream fileout = new FileOutputStream(sNewImagePath);
			oldbitmap.compress(CompressFormat.PNG, 100, fileout);
			fileout.flush();
			fileout.close();
			// add by yanggf
			System.gc();
			LogUtil.e(TAG, "cunrubendichenggong" + sNewImagePath);
			return true;
		} catch (Exception e) {
			LogUtil.e(TAG, "cunrubendichengshibai" + sNewImagePath);
			e.printStackTrace();
			LogUtil.e(TAG, e.toString());
			return false;
		}
	}
}
