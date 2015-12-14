package cn.hi321.browser.ui.activities;

import java.util.List;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.hi321.browser.config.Constants;
import cn.hi321.browser.db.BrowserDao;
import cn.hi321.browser.service.Service;
import cn.hi321.browser.ui.components.CustomAutoCompleteTextView;
import cn.hi321.browser.utils.DeviceInfoUtil;
import cn.hi321.browser2.R;
public class SearchsActivity extends TabActivity implements OnCheckedChangeListener{
	private RadioGroup mainTab;
	private CustomAutoCompleteTextView mSearchTextView;
	private Button mGoButton;
	private ImageView delectButton;
	private TextWatcher mUrlTextWatcher;
	private ImageView seach_iv;
	private PopupWindow mPopupWindow=null;
	private RelativeLayout search_rl;
	private SharedPreferences sp;
	public int TabID=0;
	public int POP=0;
	
	String [] name1={"百度","谷歌","搜狗"};
	int [] icon1={R.drawable.search_icon_baidu,R.drawable.search_icon_google,R.drawable.search_icon_321};
	
	String [] name2={"易查","百度","阅读"};
	int [] icon2={R.drawable.search_icon_yicha,R.drawable.search_icon_baidu,R.drawable.search_icon_yuedu};
	
	String [] name3={"百度","宜搜","搜狗"};
	int [] icon3={R.drawable.search_icon_baidu,R.drawable.search_icon_easou,R.drawable.search_icon_321};

	String [] name5={"淘宝","京东","当当"};
	int [] icon5={R.drawable.search_icon_taobao,R.drawable.search_icon_360buy,R.drawable.search_icon_dangdang};
	BrowserDao browserDao = new BrowserDao(SearchsActivity.this);
  	 int searchCount=0;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.searchs_acitivity);
        mainTab=(RadioGroup)findViewById(R.id.main_tab);
        mainTab.setOnCheckedChangeListener(this);
        sp = PreferenceManager.getDefaultSharedPreferences(SearchsActivity.this);
        POP=Integer.valueOf(sp.getString(Constants.PREFERENCES_SEARCH_TABID_POP, "0-0-0-0-0").split("-")[TabID]);
        mSearchTextView = (CustomAutoCompleteTextView) findViewById(R.id.UrlText); 
		mGoButton = (Button) findViewById(R.id.GoBtn); 
		delectButton=  (ImageView)findViewById(R.id.delectid);
		seach_iv=(ImageView)findViewById(R.id.seach_iv);
		search_rl=(RelativeLayout)findViewById(R.id.search_rl);
		search_rl.setOnClickListener(new OnClickListener() {
				
			@Override
			public void onClick(View v) {
				
				String tabid_pops = sp.getString(Constants.PREFERENCES_SEARCH_TABID_POP, "0-0-0-0-0");
				int pop=0;
				if (mPopupWindow==null) {
					switch (TabID) {
					case 0:
						pop=Integer.valueOf(tabid_pops.split("-")[0]);
						showPopupWindow(name1,icon1,pop);
						break;
					case 1:
						pop=Integer.valueOf(tabid_pops.split("-")[1]);
						showPopupWindow(name2,icon2,pop);
						
						break;
					case 2:
						pop=Integer.valueOf(tabid_pops.split("-")[2]);
						showPopupWindow(name3,icon3,pop);
						break;
					case 3:
						pop=Integer.valueOf(tabid_pops.split("-")[3]);
						showPopupWindow(name3,icon3,pop);
						break;
					case 4:
						pop=Integer.valueOf(tabid_pops.split("-")[4]);
						showPopupWindow(name5,icon5,pop);
						
						break;

					default:
						break;
					}
				}
				else {
					dismissPopupWindow();
				}
			}
		});
		
		delectButton.setVisibility(View.GONE);
    	mSearchTextView.setThreshold(1);
		  
    	mSearchTextView.setOnKeyListener(new View.OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {												
				if (keyCode == KeyEvent.KEYCODE_ENTER) {
//					navigateToUrl();
					 InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);  
			            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);  
					return true;
				}
				
				return false;
			}
    	});

     	mUrlTextWatcher = new TextWatcher() {			
    		private List<String> searchs;

			@Override
    		public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) { 
    			System.out.println("onTextChanged");
				
			}

    		@Override
    		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) { 
    			System.out.println("beforeTextChanged");
    		}

    		@Override
    		public void afterTextChanged(Editable arg0) {
    			System.out.println("afterTextChanged");
    			final String name = mSearchTextView.getText().toString().trim();
				showHIS(name);
    			 if(TextUtils.isEmpty(mSearchTextView.getText().toString())) { 
    				 delectButton.setVisibility(View.GONE);
     					mGoButton.setText("取消");
    			 }else  {
    				 delectButton.setVisibility(View.VISIBLE); 
    					mGoButton.setText("搜索"); 
    			 }
    			
//    			updateGoButton();
    		}

			private void showHIS(final String name) {
				new AsyncTask<Void, Void, List<String>>() {
					@Override
					protected void onPostExecute(final List<String> result) {
						super.onPostExecute(result);
						updateAdapter(result);
					}

					@Override
					protected List<String> doInBackground(Void... params) {
						String result = Service.loginByHttpClientGet(
								SearchsActivity.this, name);
						searchs = browserDao.getSearch(10, " search like '"
								+ name + "%'");
						String[] from = null;
						if (result.indexOf("s:[\"") > 0) {
							result = result.substring(result.indexOf("s:[\""));
							result = result.replace("\"]})", "").replace(
									"s:[\"", "");
							from = result.split("\",\"");
						}
						searchCount = searchs.size();// 搜索记录的数量
						if (searchs.size() < 10 && from != null)
							for (int i = 0; i < from.length; i++) {
								if (searchs.contains(from[i]))
									continue;
								searchs.add(from[i]);
								if (searchs.size() >= 10)
									break;
							}
						if (mSearchTextView.getText().length() <= 0) {
							List<String> searchs = browserDao.getSearch(10,
									null);
							searchCount = searchs.size();
							if (searchs.size() > 0) {
								searchs.add("删除搜索记录");
							}
						}
						return searchs;
					}
				}.execute();
			}
    	};

    	mSearchTextView.addTextChangedListener(mUrlTextWatcher);
    
    	mSearchTextView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position,
					long arg3) {
				String keyWord = ((TextView)view).getText().toString();
            	if(!TextUtils.isEmpty(keyWord)){
            		if(keyWord.equals("删除搜索记录")){
            			browserDao.clearData();
            			System.out.println("if");
            			mSearchTextView.setText("");
            		}else{
            			startID(TabID, POP);
	            		finish();
            		}
            	}
			}
		
    	});
    	mSearchTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {

    		@Override
    		public void onFocusChange(View v, boolean hasFocus) {
    			// Select all when focus gained.
    			if (hasFocus) {
//    				mSearchTextView.setDropDownHeight(350); 
//    				mSearchTextView.setSelection(0, mSearchTextView.getText().length());
    				if(mSearchTextView.getText().length()<=0){
	    				 List<String> searchs = browserDao.getSearch(10, null);
	    				 searchCount=searchs.size();
	    				 if(searchs.size()>0){
		    				 searchs.add("删除搜索记录");
		    				 updateAdapter(searchs);
	    				 }
    				}
    			}
    		}
    	});    	
		   	
    	mGoButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	startID(TabID, POP);
            }          
        });
    	delectButton.setOnClickListener(new ImageView.OnClickListener() {
 			
 			@Override
 			public void onClick(View v) {
 				
 				mSearchTextView.setText("");
             	mGoButton.setText("取消");
 			}
 		});
    	
    	String tabid_pops = sp.getString(Constants.PREFERENCES_SEARCH_TABID_POP, "0-0-0-0-0");
		int pop=Integer.valueOf(tabid_pops.split("-")[0]);
		seach_iv.setImageDrawable(getResources().getDrawable(icon1[pop]));
	}
    
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		String tabid_pops = sp.getString(Constants.PREFERENCES_SEARCH_TABID_POP, "0-0-0-0-0");
		int pop=0;
		switch(checkedId){
		case R.id.radio_button0:
			TabID=0;
			pop=Integer.valueOf(tabid_pops.split("-")[TabID]);
			dismissPopupWindow();
			POP=pop;
			seach_iv.setImageDrawable(getResources().getDrawable(icon1[pop]));
			break;
		case R.id.radio_button1:
			TabID=1;
			pop=Integer.valueOf(tabid_pops.split("-")[TabID]);
			dismissPopupWindow();
			POP=pop;
			seach_iv.setImageDrawable(getResources().getDrawable(icon2[pop]));
			break;
		case R.id.radio_button2:
			TabID=2;
			pop=Integer.valueOf(tabid_pops.split("-")[TabID]);
			dismissPopupWindow();
			POP=pop;
			seach_iv.setImageDrawable(getResources().getDrawable(icon3[POP]));
			break;
		case R.id.radio_button3:
			TabID=3;
			pop=Integer.valueOf(tabid_pops.split("-")[TabID]);
			dismissPopupWindow();
			POP=pop;
			seach_iv.setImageDrawable(getResources().getDrawable(icon3[pop]));
			break;
		case R.id.radio_button4:
			TabID=4;
			pop=Integer.valueOf(tabid_pops.split("-")[TabID]);
			dismissPopupWindow();
			POP=pop;
			//seach_iv.setBackgroundDrawable(getResources().getDrawable(R.drawable.search_icon_baidu));
			seach_iv.setImageDrawable(getResources().getDrawable(icon5[pop]));
			break;
		}
	}
    
	/**
	 * 更新搜索引擎适配器
	 * @param result
	 */
	private void updateAdapter(final List<String> result) {
		ArrayAdapter adapter=new ArrayAdapter(SearchsActivity.this, R.layout.search_autocomplete_line,result){

			@Override
			public View getView(int position, View convertView,
					ViewGroup parent) {
				TextView view=(TextView) View.inflate(SearchsActivity.this, R.layout.search_autocomplete_line, null);
				if(getCount()-1==position&&result.get(position).equals("删除搜索记录")){
					view.setTextColor(Color.BLUE);
					view.setHeight(DeviceInfoUtil.dip2px(SearchsActivity.this, 60));
					view.setGravity(Gravity.CENTER);
					view.setText("删除搜索记录");
					return view;
				}
				view.setText(result.get(position));
				
				Drawable drawable= null;
				if(position<searchCount){
					drawable=getResources().getDrawable(R.drawable.suggestion_history_icon);
				}else
					drawable=getResources().getDrawable(R.drawable.suggestion_search_icon);
				/// 这一步必须要做,否则不会显示.  
				drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); 
				view.setCompoundDrawables(drawable, null, null, null); 
				return view;
			}
		};
		mSearchTextView.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}
	public void showPopupWindow(String[] name,int[] icon,int pop) 
	{
		Context mContext = SearchsActivity.this;
		LayoutInflater mLayoutInflater = (LayoutInflater) mContext
				.getSystemService(LAYOUT_INFLATER_SERVICE);
		View popunwindwow = mLayoutInflater.inflate(R.layout.searchs_popwindow, null);
		
	//	mPopupWindow=new PopupWindow(popunwindwow, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, false);
		mPopupWindow=new PopupWindow(popunwindwow, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, false);
			/*mPopupWindow= new PopupWindow(popunwindwow,
					LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);*/
			mPopupWindow.showAsDropDown(search_rl);
			
			final RadioButton search_bt_pop1 = (RadioButton)popunwindwow.findViewById(R.id.search_bt_pop1);
			final RadioButton search_bt_pop2 = (RadioButton)popunwindwow.findViewById(R.id.search_bt_pop2);
			final RadioButton search_bt_pop3 = (RadioButton)popunwindwow.findViewById(R.id.search_bt_pop3);
			search_bt_pop1.setCompoundDrawablesWithIntrinsicBounds(icon[0], 0, 0, 0);
			search_bt_pop2.setCompoundDrawablesWithIntrinsicBounds(icon[1], 0, 0, 0);
			search_bt_pop3.setCompoundDrawablesWithIntrinsicBounds(icon[2], 0, 0, 0);
			search_bt_pop1.setText(name[0]);
			search_bt_pop2.setText(name[1]);
			search_bt_pop3.setText(name[2]);
			seach_iv.setImageDrawable(getResources().getDrawable(icon[pop]));
			switch (pop) {
			case 0:
				search_bt_pop1.setChecked(true);
				break;
			case 1:
				search_bt_pop2.setChecked(true);
				break;
			case 2:
				search_bt_pop3.setChecked(true);
				break;
			}
			search_bt_pop1.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					//获取保存preferences_search_tabid_pop
					String tabid_pops = sp.getString(Constants.PREFERENCES_SEARCH_TABID_POP, "0-0-0-0-0");
					String[] strings = tabid_pops.split("-");
					strings[TabID]=0+"";
					StringBuffer sb=new StringBuffer();
					for (int i = 0; i < strings.length; i++) {
						sb.append("-").append(strings[i]);
					}
					Editor edit = sp.edit();
					edit.putString(Constants.PREFERENCES_SEARCH_TABID_POP, sb.substring(1));
					edit.commit();
					
					POP=0;
					switch (TabID) {
					case 0:
						seach_iv.setImageDrawable(getResources().getDrawable(R.drawable.search_icon_baidu));
						break;
					case 1:
						seach_iv.setImageDrawable(getResources().getDrawable(R.drawable.search_icon_yicha));
						break;
					case 2:
						seach_iv.setImageDrawable(getResources().getDrawable(R.drawable.search_icon_baidu));
						break;
					case 3:
						seach_iv.setImageDrawable(getResources().getDrawable(R.drawable.search_icon_baidu));
						break;
					case 4:
						seach_iv.setImageDrawable(getResources().getDrawable(R.drawable.search_icon_taobao));
						break;
					}
					mPopupWindow.dismiss();

					mPopupWindow = null;
					search_bt_pop3.setChecked(true);
				}
			});
			search_bt_pop2.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					//获取保存preferences_search_tabid_pop
					String tabid_pops = sp.getString(Constants.PREFERENCES_SEARCH_TABID_POP, "0-0-0-0-0");
					String[] strings = tabid_pops.split("-");
					strings[TabID]=1+"";
					StringBuffer sb=new StringBuffer();
					for (int i = 0; i < strings.length; i++) {
						sb.append("-").append(strings[i]);
					}
					Editor edit = sp.edit();
					edit.putString(Constants.PREFERENCES_SEARCH_TABID_POP, sb.substring(1));
					edit.commit();
					POP=1;
					
					switch (TabID) {
					case 0:
						seach_iv.setImageDrawable(getResources().getDrawable(R.drawable.search_icon_google));
						break;
					case 1:
						seach_iv.setImageDrawable(getResources().getDrawable(R.drawable.search_icon_baidu));
						
						break;
					case 2:
						seach_iv.setImageDrawable(getResources().getDrawable(R.drawable.search_icon_easou));
						break;
					case 3:
						seach_iv.setImageDrawable(getResources().getDrawable(R.drawable.search_icon_easou));
						break;
					case 4:
						seach_iv.setImageDrawable(getResources().getDrawable(R.drawable.search_icon_360buy));
						
						break;
					}
					mPopupWindow.dismiss();

					mPopupWindow = null;
					search_bt_pop2.setChecked(true);
				}
			});
			search_bt_pop3.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					//获取保存preferences_search_tabid_pop
					String tabid_pops = sp.getString(Constants.PREFERENCES_SEARCH_TABID_POP, "0-0-0-0-0");
					String[] strings = tabid_pops.split("-");
					strings[TabID]=2+"";
					StringBuffer sb=new StringBuffer();
					for (int i = 0; i < strings.length; i++) {
						sb.append("-").append(strings[i]);
					}
					Editor edit = sp.edit();
					edit.putString(Constants.PREFERENCES_SEARCH_TABID_POP, sb.substring(1));
					edit.commit();
					POP=2;
					
					switch (TabID) {
					case 0:
						seach_iv.setImageDrawable(getResources().getDrawable(R.drawable.search_icon_321));
						break;
					case 1:
						seach_iv.setImageDrawable(getResources().getDrawable(R.drawable.search_icon_yuedu));
						
						break;
					case 2:
						seach_iv.setImageDrawable(getResources().getDrawable(R.drawable.search_icon_321));
						break;
					case 3:
						seach_iv.setImageDrawable(getResources().getDrawable(R.drawable.search_icon_321));
						break;
					case 4:
						seach_iv.setImageDrawable(getResources().getDrawable(R.drawable.search_icon_dangdang));
						
						break;
					}
					mPopupWindow.dismiss();
					mPopupWindow = null;
					search_bt_pop3.setChecked(true);
				}
			});
		}
		
	@Override
	public boolean onTouchEvent(MotionEvent event) {

		

		dismissPopupWindow();

		return super.onTouchEvent(event);

		}

	
	private void startID(int RB,int pop) {
		

		switch (RB) {
		case 0:
			switch (pop) {
			case 0:
				StartIntent("http://m.baidu.com/s?word=");
				
				break;
				
			case 1:
				StartIntent("http://www.google.com.hk/m/search?hl=zh-CN&q=");
				break;
			case 2:
				StartIntent("http://wap.sogou.com/web/searchList.jsp?&keyword=");
	
				break;
			}
			
			
			
			break;
	case 1:
			
		switch (pop) {
		case 0:
			StartIntent("http://tbook.yicha.cn/tb/ss.y?key=");
			
			break;
			
		case 1:
			StartIntent("m.baidu.com/s?bd_page_type=1&st=105541&tn=bdxs&word=");
			break;
		case 2:
			StartIntent("wap.cmread.com/r/p/search.jsp?kw=");

			break;
		}
			break;
		case 2:
			
			switch (pop) {
			case 0:
				StartIntent("m.baidu.com/news?th=bdapisearch&word=");
				
				break;
				
			case 1:
				StartIntent("http://n.easou.com/s.m?wver=c&ct_4=");
				break;
			case 2:
				StartIntent("m.baidu.com/news?th=bdapisearch&word=");

				break;
			}
			
			break;
	
	
		case 3:
			
			
			switch (pop) {
			case 0:
				StartIntent("http://m.baidu.com/img?tn=bdwis&word=");
				
				break;
				
			case 1:
				StartIntent("http://p2.easou.com/s.e?actType=1&wver=c&pic=");
				break;
			case 2:
				StartIntent("http://wap.sogou.com/pic/searchList.jsp?&keyword=");

				break;
			}
	
			break;
		case 4:
			
			
			switch (pop) {
			case 0:
				StartIntent("http://s8.m.taobao.com/munion/search.htm?q=");
				
				break;
				
			case 1:
				StartIntent("m.360buy.com/ware/search.action?keyword=");
				break;
			case 2:
				StartIntent("m.dangdang.com/touch/search.php?keyword=");

				break;
			}
			
			
			break;
		default:
			break;
		}
		
	}
	
	
	public void StartIntent(String url){
		
		
		String searchtext = mSearchTextView.getText().toString();
    	if(!TextUtils.isEmpty(searchtext)){
        	/*Intent i = new Intent(SearchsActivity.this,MainActivity.class);
        	i.putExtra("url", "http://m.baidu.com/s?word="+url);
        	SearchsActivity.this.startActivityForResult(i, 10);*/
    		Intent intent = new Intent();
			intent.putExtra("url",url+searchtext);
			intent.setAction("search");
			SearchsActivity.this.sendBroadcast(intent);
			browserDao.insertSearchsInfo(searchtext);
    	}
    	SearchsActivity.this.finish();
	}
	
	private void dismissPopupWindow() {
		if (mPopupWindow != null && mPopupWindow.isShowing()) {
			mPopupWindow.dismiss();
			mPopupWindow = null;

		}
	}
}