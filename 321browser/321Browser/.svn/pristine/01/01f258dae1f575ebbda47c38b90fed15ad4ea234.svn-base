package cn.hi321.browser.view;

 
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;
import cn.hi321.browser.adapter.GirdViewAdapter;
import cn.hi321.browser.config.Constants;
import cn.hi321.browser.ui.activities.HomeActivity;
import cn.hi321.browser.utils.UserPreference;
import cn.hi321.browser2.R;

public class PopMenu {

	private Context context;
	private PopupWindow popupWindow;
	private ViewPager viewPager;
	private ArrayList<View> listViews;
	private int screenwidth;

	private int currentView = 0;// 当前视图
	private int viewOffset = 0;// 动画图片偏移量
	private int imgWidth;// 图片宽度
	private ImageButton iv_cursor;// 动画图片
	private TextView tv_main;
	private TextView tv_utils;
//	private TextView tv_set;
	Handler myHandler;
	public static final int bookmarks = 12;//书签
	public static final int FindPage = 13;//页面查找
	public static final int ShareUrl = 14;//分享
	public static final int SelectText = 15;//选择文本
	public static final int ADD_BOOKMARK = 16;//添加书签
	public static final int Download = 17;//下载
	public static final int Setting = 18;//设置
	public static final int  exit = 19;//关闭
	public static final int  refreshview = 20;//关闭
	public static final int FullSeting = 21;
	public static final int SAVEIMAGE = 23;
	public static final int Update = 24;
	public static final int Fankui = 25;
	public static final int Guanyu = 11;
	
	public static final int SCREEN_LUMINANCE = 22;//屏幕暗亮设置
    private GirdViewAdapter girdViewAdapter ;;
	public PopMenu(Context context,Handler myHandler) {

		// TODO Auto-generated constructor stub
		this.context = context;
		this.myHandler = myHandler;
		LayoutInflater inflater = ((Activity) context).getLayoutInflater();
		View view = inflater.inflate(R.layout.popmenu, null);
  
		tv_main = (TextView) view.findViewById(R.id.tv_main);
		tv_utils = (TextView) view.findViewById(R.id.tv_utils); 
		this.tv_main.setOnClickListener(new myOnClick(0));
		this.tv_utils.setOnClickListener(new myOnClick(1)); 
		iv_cursor = (ImageButton) view.findViewById(R.id.iv_cursor);
		
		
		viewPager = (ViewPager) view.findViewById(R.id.viewPagerw);
		viewPager.setFocusableInTouchMode(true);
		viewPager.setFocusable(true);

		listViews = new ArrayList<View>();
		listViews.add(inflater.inflate(R.layout.grid_menu, null));
		listViews.add(inflater.inflate(R.layout.grid_menu, null)); 
		
		viewPager.setAdapter(new myPagerAdapter());
		viewPager.setOnPageChangeListener(new MyOnPageChangeListener());

		popupWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT, context
				.getResources().getDimensionPixelSize(R.dimen.popmenu_h));
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
//		setCursorWidth();
	    /*设置触摸外面时消失
	     * */  
		popupWindow.setOutsideTouchable(true);   
//		popupWindow.update();  
		popupWindow.setTouchable(true);  
	    /*设置点击menu以外其他地方以及返回键退出*/  
		popupWindow.setFocusable(true);  
	      
	    /** 1.解决再次点击MENU键无反应问题   
	     *  2.sub_view是PopupWindow的子View 
	     */  
		view.setFocusableInTouchMode(true);  
		view.setOnKeyListener(new OnKeyListener() {  
	        @Override  
	        public boolean onKey(View v, int keyCode, KeyEvent event) {  
	            // TODO Auto-generated method stub  
	            if ((keyCode == KeyEvent.KEYCODE_MENU)&&(popupWindow.isShowing())) {  
	            	popupWindow.dismiss();// 这里写明模拟menu的PopupWindow退出就行  
	                return true;  
	            }  
	            return false;  
	        }  
	    });  
		
	}

	private int bmpW;
	public void setCursorWidth() {
		DisplayMetrics dm = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay() .getMetrics(dm);
		int screenW = dm.widthPixels;
		bmpW = BitmapFactory.decodeResource(((Activity) context).getResources(), R.drawable.img_cursor).getWidth();
 
		System.out.println("bmpw===:"+bmpW);
		viewOffset = (screenW/2  - bmpW) / 2;
		System.out.println("========width: "+screenW/2);
		System.out.println("viewOffset11111111111==="+viewOffset);
		Matrix matrix = new Matrix();
		matrix.postTranslate(viewOffset, 0);
		iv_cursor.setImageMatrix(matrix); 
	} 
	 
	// 下拉式 弹出 pop菜单 parent 右下角
	public void show(View parent) { 
		UserPreference.ensureIntializePreference(context); 
		tv_main.setTextColor(android.graphics.Color.WHITE); 
		tv_utils.setTextColor(android.graphics.Color.GRAY); 
		viewPager.setAdapter(new myPagerAdapter());
	//	popupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 70);// 距离底部的位置 
		setCursorWidth();
		popupWindow.showAsDropDown(parent);
		popupWindow.update(); 
		
		if(girdViewAdapter!=null){
			girdViewAdapter.notifyDataSetChanged();
		} 
	 
	}
	 
	public void dismiss() { 
		popupWindow.dismiss();
	} 
	public boolean isShow(){
		
		if(popupWindow!=null && popupWindow.isShowing()){
			return true;
		}
		return false;
	}


	public class myPagerAdapter extends PagerAdapter {

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {

			((ViewPager) arg0).removeView(listViews.get(arg1));

		}

		@Override
		public int getCount() {

			return listViews.size();

		}

		public Object instantiateItem(View arg0, int arg1) {

			if (arg1 < 2) {
				((ViewPager) arg0).addView(listViews.get(arg1 % 2), 0);

			}
			// 将来添加菜单的时候 新建一个gridviewadapter 然后new个gridview 添加到这里就可以
			girdViewAdapter = new GirdViewAdapter(context,0);
			switch (arg1) {
			case 0:// 选项卡1

				GridView gridView = (GridView) arg0
						.findViewById(R.id.myGridView);
				girdViewAdapter.setFlag(0);
				gridView.setAdapter(girdViewAdapter); 
				gridView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {

						switch (arg2) {
							case 0://加入书签
								if(HomeActivity.INSTANCE.mCurrentWebView!=null&&HomeActivity.INSTANCE.mCurrentWebView.getUrl().
										equals(Constants.URL_ABOUT_START)){//是主页  
								 
								}else{ 
									myHandler.sendEmptyMessage(ADD_BOOKMARK);
									dismiss();
								}
							
								break;
							case 1://书签/历史
								myHandler.sendEmptyMessage(bookmarks);
								dismiss();
								break;
							case 2://刷新
							
								if(HomeActivity.INSTANCE.mCurrentWebView!=null&&HomeActivity.INSTANCE.mCurrentWebView.getUrl().
										equals(Constants.URL_ABOUT_START)){//是主页  
								 
								}else{ 
									myHandler.sendEmptyMessage(refreshview);
									dismiss();
								}
								break;
							case 3://夜间模式 
						        myHandler.sendEmptyMessage(SCREEN_LUMINANCE);
						        dismiss();
								break;
//							case 4://321账户
//								
//								break;
							case 4://下载管理
								myHandler.sendEmptyMessage(Download);
								dismiss();
								break;
							case 5:
								//分享
								myHandler.sendEmptyMessage(ShareUrl);
								dismiss();
								break;
							case 6://是否全屏模式 
								boolean isFullScreen = UserPreference.read("fullScreen", false);
								if(isFullScreen){//表示全屏
									 UserPreference.save("fullScreen", !isFullScreen); 
								}else{//表示非全屏
									 UserPreference.save("fullScreen", !isFullScreen); 
								} 
								girdViewAdapter.setFlag(0);
								girdViewAdapter.notifyDataSetChanged();
								myHandler.sendEmptyMessage(FullSeting); 
								dismiss();
								break;
							case 7://退出
								myHandler.sendEmptyMessage(exit);
								dismiss();
								break;
							default:
  
						}

					}
				});

				break;
			case 1:// 选项卡2
				GridView gridView2 = (GridView) arg0
						.findViewById(R.id.myGridView);
				girdViewAdapter.setFlag(1);
				gridView2.setAdapter(girdViewAdapter);
			
				gridView2.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {

						switch (arg2) {
//						case 0://无迹模式
//							break;
//						case 0://保存页面
//							 // View是你需要截图的View 
//							myHandler.sendEmptyMessage(SAVEIMAGE);
//							break;
						case 0://无图模式
							boolean isImage =UserPreference.read(Constants.PREFERENCES_BROWSER_ENABLE_IMAGES, true);// Controller.getInstance().getPreferences().getBoolean(Constants.PREFERENCES_BROWSER_ENABLE_IMAGES, true);
							UserPreference.save(Constants.PREFERENCES_BROWSER_ENABLE_IMAGES, !isImage);
							HomeActivity.INSTANCE.applyPreferences();
							dismiss();
							break;
						
						case 1://检查更新
							myHandler.sendEmptyMessage(Update);
							dismiss();
							break;
						case 2://查找页面
							myHandler.sendEmptyMessage(FindPage);
							dismiss();
					
							break;
						case 3://设置
							myHandler.sendEmptyMessage(Setting);
							dismiss();
							break;
//						case 4:
//							break;
//						case 5://页面分享
//						
//							break;
						case 4://反馈有礼
							myHandler.sendEmptyMessage(Fankui);
							dismiss();
							break;
						case 5://关于
							myHandler.sendEmptyMessage(Guanyu);
							dismiss();
							break;
						
					
						default:

						
							break;
						}

					}
				});
				break; 
			}

			return listViews.get(arg1);

		}

		public boolean isViewFromObject(View arg0, Object arg1) {

			return arg0 == (arg1);

		}

	}

	
	
	public class MyOnPageChangeListener implements OnPageChangeListener { 
		@Override
		public void onPageSelected(int arg0) {
			 
			int one = viewOffset * 2 + bmpW;
			Animation animation = null;
			animation = new TranslateAnimation(one *currentView, one * arg0 , 0, 0);
			currentView = arg0;
			animation.setFillAfter(true);
			animation.setDuration(300);
			iv_cursor.startAnimation(animation);
 
			if(currentView == 0){ 
				tv_main.setTextColor(android.graphics.Color.WHITE); 
				tv_utils.setTextColor(android.graphics.Color.GRAY); 
				
			}else if(currentView == 1){
				tv_utils.setTextColor(android.graphics.Color.WHITE); 
				tv_main.setTextColor(android.graphics.Color.GRAY); 
			}
//			
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}

	}
 

	/*
	 * 
	 * 对选项卡单击监听的实现方法
	 */
	public class myOnClick implements View.OnClickListener {

		int index = 0;

		public myOnClick(int currentIndex) {

			index = currentIndex;
		}

		public void onClick(View v) {

			viewPager.setCurrentItem(index);
			if(index == 0){ 
				tv_main.setTextColor(android.graphics.Color.WHITE); 
				tv_utils.setTextColor(android.graphics.Color.GRAY); 
			}else if(index == 1){
				tv_utils.setTextColor(android.graphics.Color.WHITE); 
				tv_main.setTextColor(android.graphics.Color.GRAY); 
			}

		}

	}
	
	

}
