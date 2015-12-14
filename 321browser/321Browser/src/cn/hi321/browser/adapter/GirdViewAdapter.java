package cn.hi321.browser.adapter;



import android.content.Context;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import cn.hi321.browser.config.Constants;
import cn.hi321.browser.controllers.Controller;
import cn.hi321.browser.ui.activities.HomeActivity;
import cn.hi321.browser.utils.UserPreference;
import cn.hi321.browser2.R;

public class GirdViewAdapter extends BaseAdapter {

	private int imgRecouse[];

	private String title[];

	// -------------------

	private LayoutInflater inflater;

	private Context context;
	private int flag = 0;
    private int normal ;
	public GirdViewAdapter(Context context,int flag) {

		this.context = context;
		UserPreference.ensureIntializePreference(context);
		Controller.getInstance().setPreferences(PreferenceManager.getDefaultSharedPreferences(context));       
	      
		inflater = LayoutInflater.from(context);
		this.flag = flag; 
	    normal = Settings.System.getInt(context.getContentResolver(),
				Settings.System.SCREEN_BRIGHTNESS, 255);
		setData();
		

	}
	
	public void setFlag(int flag){
		this.flag = flag;
		setData();
	}

	public int getCount() {

		return imgRecouse.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return imgRecouse[position];
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public View getView(int position, View currentView, ViewGroup arg2) {

		currentView = inflater.inflate(R.layout.imagebutton, null);

		ImageView imageView = (ImageView) currentView
				.findViewById(R.id.imgbtn_img);
		TextView textView = (TextView) currentView
				.findViewById(R.id.imgbtn_text);

		imageView.setBackgroundResource(imgRecouse[position]);
		textView.setText(title[position]);
		textView.setTextColor(Color.parseColor("#FFFFFF")); 


		if(HomeActivity.INSTANCE!=null&&HomeActivity.INSTANCE.mCurrentWebView!=null&&HomeActivity.INSTANCE.mCurrentWebView.getUrl()!=null&&HomeActivity.INSTANCE.mCurrentWebView.getUrl().
				equals(Constants.URL_ABOUT_START)){//是主页  
			if((flag == 0 &&position == 0)||(flag == 0&&position == 2)){  
				textView.setTextColor(Color.parseColor("#878787")); 
			}  
	   } 

		return currentView;
	}
	
	private void setData(){
		if(flag == 0){//表示gridview第一页
			imgRecouse = new int[] { R.drawable.menu_container_add2bookmark_d,
					R.drawable.menu_container_bookmark, R.drawable.menu_container_refresh_d,
					R.drawable.menu_container_nightmode,
					R.drawable.menu_container_download, 
					R.drawable.rd_menubar_share,
					R.drawable.menu_container_fullscreen,
					R.drawable.menu_container_exit };

			title = new String[] { "加入收藏", "书签/历史", "刷新", "夜间模式", "下载/管理",
					"页面分享","全屏模式", "退出" };// "321账户",   R.drawable.menu_container_user_center, 
			
			  if(HomeActivity.INSTANCE.mCurrentWebView!=null&&HomeActivity.INSTANCE.mCurrentWebView.getUrl()!=null&&HomeActivity.INSTANCE.mCurrentWebView.getUrl().
							equals(Constants.URL_ABOUT_START)){//是主页 
					imgRecouse[0] = R.drawable.menu_container_add2bookmark_dis;
					imgRecouse[2] = R.drawable.menu_container_refresh_dis;
					 
			  }else{
					imgRecouse[0] = R.drawable.menu_container_add2bookmark_d;
					imgRecouse[2] = R.drawable.menu_container_refresh_d;
			  }
			
			boolean isFullScreen = UserPreference.read("fullScreen", false);
			//表示全屏
	        if (isFullScreen) {        	
	        	imgRecouse[6] = R.drawable.menu_container_exitfullscreen;
				title[6] = "退出全屏";
	        } else{//表示非全屏 
	        	imgRecouse[6] = R.drawable.menu_container_fullscreen;
				title[6] = "全屏";
	        }
	        if(normal == 255){
	        	//日照模式
	        	imgRecouse[3] = R.drawable.menu_container_daymode;
				title[3] = "日照模式";
	        }else{
	        	//夜间模式
	        	imgRecouse[3] = R.drawable.menu_container_nightmode;
				title[3] = "夜间模式";
	        }
		 
		}else{//R.drawable.menu_container_notrace,"无迹浏览", 
			imgRecouse = new int[] { R.drawable.menu_container_noimagemode_dis,
					 R.drawable.menu_container_checkupdate, R.drawable.menu_container_page_search_n,
					R.drawable.menu_container_setting,R.drawable.menu_container_feedback ,R.drawable.menu_container_user_center_logged};

			title = new String[]{ "无图模式",  "检查更新", "页面查找", "设置", "反馈有礼" , "关于" };
			//"保存网页", R.drawable.menu_container_savepage_d,R.drawable.menu_container_checkupdate, 
			boolean hasImage =UserPreference.read(Constants.PREFERENCES_BROWSER_ENABLE_IMAGES, true);// Controller.getInstance().getPreferences().getBoolean(Constants.PREFERENCES_BROWSER_ENABLE_IMAGES, true);
			if(hasImage){
				//有图模式
				imgRecouse[0] =  R.drawable.menu_container_noimagemode_dis;
				title[0] = "无图模式";
			}else{
				//无图模式
				imgRecouse[0] =  R.drawable.menu_container_imagemode_d;
				title[0] = "有图模式";
			}
		} 
		
	}

}
