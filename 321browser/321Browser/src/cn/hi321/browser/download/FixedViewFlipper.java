package cn.hi321.browser.download;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ViewFlipper;

public class FixedViewFlipper extends ViewFlipper {

	public FixedViewFlipper(Context context) {
		super(context);
	}

	public FixedViewFlipper(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onDetachedFromWindow() {

		int apiLevel = Build.VERSION.SDK_INT;

		if (apiLevel >= 7) {
			try {
				super.onDetachedFromWindow();
			} catch (IllegalArgumentException e) {
			} finally {
				super.stopFlipping();
			}
		} else {
			super.onDetachedFromWindow();
		}
	}
}
