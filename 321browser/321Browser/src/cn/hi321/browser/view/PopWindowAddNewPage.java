package cn.hi321.browser.view;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;
import cn.hi321.browser.ui.activities.HomeActivity;
import cn.hi321.browser.ui.components.CustomWebView;
import cn.hi321.browser.utils.ApplicationUtils;
import cn.hi321.browser.utils.DeviceInfoUtil;
import cn.hi321.browser2.R;

public class PopWindowAddNewPage {

	private ListView mListView;
	private Context context;
	private PopupWindow popupWindow;
	private AddNewItemAdapter mAddNewItemAdapter;
	private Handler handler;
	public static final int ADDNEWPAGE = 30;

	private LayoutInflater inflater;
	public static final int RemoveLabelPage = 26;
	public static final int SELECTPage = 27;
	private LinearLayout addNewPageButton;
	public static List<CustomWebView> mWebViews;
	private View view;
	private int height;
	public static ViewFlipper mViewFlipper;
	public static final int ShowMenuDialog = 35;
	public static final int LoadHomelPage = 37;

	private int listviewHeight;
	private int listHight;
	
	// 下拉式 弹出 pop菜单 parent 右下角
	private CustomWebView mCustomWebView;

	@SuppressLint("ParserError")
	public PopWindowAddNewPage(final Context context, final Handler myHandler,
			List<CustomWebView> mWebView, CustomWebView mCustomWebView,
			ViewFlipper mViewFlipper, View parent) {

		this.context = context;
		this.handler = myHandler;
		this.mWebViews = mWebView;
		this.mViewFlipper = mViewFlipper;
		this.mCustomWebView = mCustomWebView;
		inflater = ((Activity) context).getLayoutInflater();
		view = inflater.inflate(R.layout.addnewpage, null);
		addNewPageButton = (LinearLayout) view.findViewById(R.id.linAddButton);
		mListView = (ListView) view.findViewById(R.id.mListView);
		mListView.setDivider(null);
		listviewHeight = 50;
		listviewHeight = DeviceInfoUtil.dip2px(context, listviewHeight);
		listHight = 0;
		for (int i = 0; i < mWebViews.size(); i++) {
			listHight = mWebViews.size() * listviewHeight;// -
															// (mWebViews.size()-1)*2
															// ;
		}

		// int len =
		// context.getResources().getDimensionPixelSize(R.dimen.mListViewHeight);
		System.out.println("mWebViews.size()==" + mWebViews.size());
		System.out.println("listHight" + listHight);
		height = DeviceInfoUtil.dip2px(context, 82) + listHight;
		popupWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		mAddNewItemAdapter = new AddNewItemAdapter();

		int deviceHeightPixels = DeviceInfoUtil.getHeightPixels(context);
		System.out.println("deviceWidthPixels=======" + deviceHeightPixels);

		if (height > deviceHeightPixels - 100) {
			height = deviceHeightPixels - 100;
		}

		popupWindow.setHeight(height);
		mListView.setAdapter(mAddNewItemAdapter);
		popupWindow.showAsDropDown(parent);// 距离底部的位置, Gravity.BOTTOM, 0, 70
		addNewPageButton.setOnClickListener(new LinearLayout.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mWebViews.size() > 8) {
					Toast.makeText(context, "标签已满", 1).show();
				} else {
					myHandler.sendEmptyMessage(ADDNEWPAGE);
					dismiss();
				}
			}
		});
		mListView.setSelection(mWebViews.size() - 1);
		mListView.setOnItemClickListener(new ListView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (mWebViews.size() > arg2) {

					System.out.println("arg2========" + arg2);
					Message msg = new Message();
					msg.what = SELECTPage;
					msg.obj = arg2;
					myHandler.sendMessage(msg);

				}
				dismiss();
			}
		});

		/* 设置触摸外面时消失 */
		popupWindow.setOutsideTouchable(true);
		// popupWindow.update();
		popupWindow.setTouchable(true);
		/* 设置点击menu以外其他地方以及返回键退出 */
		popupWindow.setFocusable(true);
		popupWindow.update();

		/**
		 * 1.解决再次点击MENU键无反应问题 2.sub_view是PopupWindow的子View
		 */
		view.setFocusableInTouchMode(true);
		view.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				if ((keyCode == KeyEvent.KEYCODE_MENU)
						&& (popupWindow.isShowing())) {
					popupWindow.dismiss();// 这里写明模拟menu的PopupWindow退出就行

					return true;
				}
				return false;
			}
		});

	}

	public void setSelection() {
//		Toast.makeText(context, "selectePosition=="+HomeActivity.selectePosition, 0).show();
		mListView.setSelection(HomeActivity.selectePosition);
	}

	/**
	 * 新添加标签页 addNewViewArrayList 数据保存到
	 * 
	 * removePage 当点击removePage按钮时移除当前addNewViewArrayList的那一条数据，然后发送消息刷新界面，
	 * */
	class AddNewItemAdapter extends BaseAdapter {

		@Override
		public int getCount() {

			return mWebViews.size();
		}

		@Override
		public Object getItem(int position) {
			return mWebViews.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub

			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.popwindow_addnewpage,
						null);
				holder = new ViewHolder();
				holder.pageimage = (ImageView) convertView
						.findViewById(R.id.pageimage);
				holder.removePage = (ImageView) convertView
						.findViewById(R.id.Remove_label_page);
				holder.mTitle = (TextView) convertView
						.findViewById(R.id.pagename);
				holder.pageurl = (TextView) convertView
						.findViewById(R.id.pageurl);
				holder.addviewpage = (RelativeLayout) convertView
						.findViewById(R.id.addviewpage_bg);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			if (mWebViews != null && mWebViews.size() > position) {
				final CustomWebView currentWebView = mWebViews.get(position);
				if (HomeActivity.selectePosition == -1) {
					if (position == mWebViews.size() - 1) {
						holder.addviewpage
								.setBackgroundResource(R.drawable.addpopwindow_secelet);
					} else {
						// holder.addviewpage.setBackgroundResource(R.drawable.addpopwindow_secelet);
						holder.addviewpage
								.setBackgroundResource(R.drawable.addpopwindow_item_bg);// night_button_d
					}
				} else {
					if (HomeActivity.selectePosition == position) {
						holder.addviewpage
								.setBackgroundResource(R.drawable.addpopwindow_secelet);// url_suggest_input_bg
																						// tab_center_btn_d
					} else {
						holder.addviewpage
								.setBackgroundResource(R.drawable.addpopwindow_item_bg);// tab_strip_bg
					}
				}

				String value = (String) currentWebView.getTitle();
				holder.pageurl.setText((String) currentWebView.getUrl());
				if ((value != null) && (value.length() > 0)) {
					// holder.mTitle.setText((String.format(context.getResources().getString(R.string.ApplicationNameUrl),
					// value)));
					holder.mTitle.setText((position + 1) + ". " + value);
				} else {
					holder.mTitle.setText((context.getResources()
							.getString(R.string.ApplicationName)));
				}
				BitmapDrawable favicon = getNormalizedFavicon(currentWebView);

				if (currentWebView.getFavicon() != null) {
					holder.pageimage.setImageDrawable(favicon);
				} else {
					holder.pageimage
							.setImageResource(R.drawable.tab_favicon_default);
				}

				holder.removePage
						.setOnClickListener(new ImageView.OnClickListener() {

							@Override
							public void onClick(View v) {
								removWebView(position, currentWebView);

							}

						});
			}
			return convertView;
		}
	}
	

	public void removWebView(final int position,
			final CustomWebView currentWebView) {
		// TODO Auto-generated method stub
		if (mWebViews != null
				&& mWebViews.size() > position) {

			if (mWebViews.size() * 50 < height) {
				height = height - listviewHeight;
			}

			System.out.println("height==" + height);
			System.out
					.println("listviewheigh============"
							+ listviewHeight);
			if (mWebViews.size() == 1) {
				HomeActivity.selectePosition = -1;
				popupWindow.dismiss();
				handler
						.sendEmptyMessage(LoadHomelPage);
			} else {
				if (position == HomeActivity.selectePosition) {
					HomeActivity.selectePosition = -1;
				}
				currentWebView.doOnPause();
				popupWindow.update(view.getWidth(),	height);
				mAddNewItemAdapter.notifyDataSetChanged();
				mWebViews.remove(position);
				mViewFlipper.removeViewAt(position);
				handler.sendEmptyMessage(RemoveLabelPage);

			}
			setSelection();

		}
	}

	private class ViewHolder {
		ImageView pageimage;
		ImageView removePage;
		TextView mTitle;
		TextView pageurl;
		RelativeLayout addviewpage;
	}

	/**
	 * Get a Drawable of the current favicon, with its size normalized relative
	 * to current screen density.
	 * 
	 * @return The normalized favicon.
	 */
	private BitmapDrawable getNormalizedFavicon(CustomWebView currentWebView) {

		BitmapDrawable favIcon = new BitmapDrawable(context.getResources(),
				currentWebView.getFavicon());

		if (currentWebView.getFavicon() != null) {
			int imageButtonSize = ApplicationUtils
					.getImageButtonSize((Activity) context);
			int favIconSize = ApplicationUtils
					.getFaviconSize((Activity) context);

			Bitmap bm = Bitmap.createBitmap(imageButtonSize, imageButtonSize,
					Bitmap.Config.ARGB_4444);
			Canvas canvas = new Canvas(bm);

			favIcon.setBounds((imageButtonSize / 2) - (favIconSize / 2),
					(imageButtonSize / 2) - (favIconSize / 2),
					(imageButtonSize / 2) + (favIconSize / 2),
					(imageButtonSize / 2) + (favIconSize / 2));
			favIcon.draw(canvas);

			favIcon = new BitmapDrawable(context.getResources(), bm);
		}

		return favIcon;
	}

	


	public void dismiss() {
		popupWindow.dismiss();
	}

	public boolean isShow() {

		if (popupWindow != null && popupWindow.isShowing()) {
			return true;
		}
		return false;
	}
}
