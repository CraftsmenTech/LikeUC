package cn.hi321.browser.ui.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.FilterQueryProvider;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.SimpleCursorAdapter.CursorToStringConverter;
import cn.hi321.browser.config.Constants;
import cn.hi321.browser.model.adapters.UrlSuggestionCursorAdapter;
import cn.hi321.browser.providers.BookmarksProviderWrapper;
import cn.hi321.browser2.R;

public class SearchActivity extends Activity implements OnClickListener {
	private TextWatcher mUrlTextWatcher;
	private Button mGoButton;
	private AutoCompleteTextView mUrlEditText;
	private ImageView delectButton;
	protected InputMethodManager imm;
	protected Handler mHandler = new Handler();
	private ScrollView mScrollView;
	private Button btn_xiexian;
	private Button btn_dian;
	private Button btn_com;
	private Button btn_cn;
	private Button btn_net;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.searchactivity);
		mUrlEditText = (AutoCompleteTextView) findViewById(R.id.UrlText);
		mGoButton = (Button) findViewById(R.id.GoBtn);
		delectButton = (ImageView) findViewById(R.id.delectid);
		mScrollView = (ScrollView) findViewById(R.id.scroll);

		btn_xiexian = (Button) findViewById(R.id.btn_xiexian);
		btn_dian = (Button) findViewById(R.id.btn_dian);
		btn_com = (Button) findViewById(R.id.btn_com);
		btn_cn = (Button) findViewById(R.id.btn_cn);
		btn_net = (Button) findViewById(R.id.btn_net);

		btn_xiexian.setOnClickListener(this);
		btn_dian.setOnClickListener(this);
		btn_com.setOnClickListener(this);
		btn_cn.setOnClickListener(this);
		btn_net.setOnClickListener(this);
		delectButton.setVisibility(View.GONE);
		mUrlEditText.setThreshold(1);
		String url = getIntent().getStringExtra("url");
		if (url != null) {
			if ("about:start".equals(url))
				mUrlEditText.setText("");
			else
				mUrlEditText.setText(url);
		}
		String[] from = new String[] {
				UrlSuggestionCursorAdapter.URL_SUGGESTION_TITLE,
				UrlSuggestionCursorAdapter.URL_SUGGESTION_URL };
		int[] to = new int[] { R.id.AutocompleteTitle, R.id.AutocompleteUrl };

		final UrlSuggestionCursorAdapter adapter = new UrlSuggestionCursorAdapter(
				this, R.layout.url_autocomplete_line, null, from, to);

		adapter.setCursorToStringConverter(new CursorToStringConverter() {
			@Override
			public CharSequence convertToString(Cursor cursor) {
				String aColumnString = cursor.getString(cursor
						.getColumnIndex(UrlSuggestionCursorAdapter.URL_SUGGESTION_URL));
				return aColumnString;
			}
		});

		adapter.setFilterQueryProvider(new FilterQueryProvider() {
			@Override
			public Cursor runQuery(CharSequence constraint) {
				if ((constraint != null) && (constraint.length() > 0)) {

					return BookmarksProviderWrapper.getUrlSuggestions(
							getContentResolver(),
							constraint.toString(),
							PreferenceManager.getDefaultSharedPreferences(
									SearchActivity.this).getBoolean(
									Constants.PREFERENCE_USE_WEAVE, false));
				} else {

					return BookmarksProviderWrapper.getUrlSuggestions(
							getContentResolver(),
							null,
							PreferenceManager.getDefaultSharedPreferences(
									SearchActivity.this).getBoolean(
									Constants.PREFERENCE_USE_WEAVE, false));
				}
			}
		});
		mUrlEditText.setAdapter(adapter);

		mUrlEditText.setOnKeyListener(new View.OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_ENTER) {
					// navigateToUrl();
					InputMethodManager imm = (InputMethodManager) v
							.getContext().getSystemService(
									Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
					return true;
				}

				return false;
			}

		});

		mUrlTextWatcher = new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
			}

			@Override
			public void afterTextChanged(Editable arg0) {
				if (TextUtils.isEmpty(mUrlEditText.getText().toString())) {
					delectButton.setVisibility(View.GONE);
					mGoButton.setText("取消");
				} else {
					delectButton.setVisibility(View.VISIBLE);
					mGoButton.setText("前往");

				}

				// updateGoButton();
			}
		};

		mUrlEditText.addTextChangedListener(mUrlTextWatcher);

		mUrlEditText.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				String url = mUrlEditText.getText().toString();
				if (!TextUtils.isEmpty(url)) {
					Intent intent = new Intent();
					intent.putExtra("url", url);
					intent.setAction("search");
					SearchActivity.this.sendBroadcast(intent);

				}
				finish();
			}

		});
		mUrlEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// Select all when focus gained.
				imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

				if (hasFocus) {
					if (TextUtils.isEmpty(mUrlEditText.getText().toString())) {
						delectButton.setVisibility(View.GONE);
						mGoButton.setText("取消");
					} else {
						delectButton.setVisibility(View.VISIBLE);
						mGoButton.setText("前往");

					}

					// imm.showSoftInputFromInputMethod(mUrlEditText.getWindowToken()
					// , InputMethodManager.SHOW_FORCED);
					imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
					mUrlEditText.setSelection(0, mUrlEditText.getText()
							.length());
					// 这里必须要给一个延迟，如果不加延迟则没有效果。我现在还没想明白为什么
					mHandler.postDelayed(new Runnable() {

						@Override
						public void run() {
							// 将ScrollView滚动到底
							mScrollView.fullScroll(View.FOCUS_DOWN);
						}
					}, 100);
				} else {// 否则就隐藏
					try {
						imm.hideSoftInputFromWindow(
								mUrlEditText.getWindowToken(), 0);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});

		mUrlEditText.setCompoundDrawablePadding(5);

		mGoButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				String url = mUrlEditText.getText().toString();
				if (!TextUtils.isEmpty(url)) {
					/*
					 * Intent i = new
					 * Intent(SearchActivity.this,MainActivity.class);
					 * i.putExtra("url", url);
					 * SearchActivity.this.startActivityForResult(i, 10);
					 */
					Intent intent = new Intent();
					intent.putExtra("url", url);
					intent.setAction("search");
					SearchActivity.this.sendBroadcast(intent);

				}
				SearchActivity.this.finish();
			}
		});
		delectButton.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mUrlEditText.setText("");
				mGoButton.setText("取消");
			}
		});

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_xiexian:
		case R.id.btn_dian:
		case R.id.btn_com:
		case R.id.btn_cn:
		case R.id.btn_net:
			int index = mUrlEditText.getSelectionStart();// 获取光标所在位置
			int length = mUrlEditText.getText().length();
			String text = ((Button) v).getText().toString();
			// if(index>0){
			StringBuffer url = new StringBuffer(mUrlEditText.getText()
					.toString());
			url = url.insert(index, text);
			mUrlEditText.setText(url);
			mUrlEditText.setSelection(index + text.length());
			// }else
			// mUrlEditText.append(text, 0, text.length());//光标所在位置插入文字
			break;
		}
	}
}

// /*
// * 321 Browser for Android
// *
// * Copyright (C) 2010 - 2011 J. Devauchelle and contributors.
// *
// * This program is free software; you can redistribute it and/or
// * modify it under the terms of the GNU General Public License
// * version 3 as published by the Free Software Foundation.
// *
// * This program is distributed in the hope that it will be useful,
// * but WITHOUT ANY WARRANTY; without even the implied warranty of
// * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// * GNU General Public License for more details.
// */
//
// package cn.hi321.browser.ui.activities;
//
// import android.app.Activity;
// import android.content.Intent;
// import android.database.Cursor;
// import android.graphics.Rect;
// import android.graphics.drawable.Drawable;
// import android.os.Bundle;
// import android.preference.PreferenceManager;
// import android.text.Editable;
// import android.text.TextUtils;
// import android.text.TextWatcher;
// import android.view.KeyEvent;
// import android.view.MotionEvent;
// import android.view.View;
// import android.widget.AdapterView;
// import android.widget.AdapterView.OnItemClickListener;
// import android.widget.AutoCompleteTextView;
// import android.widget.Button;
// import android.widget.FilterQueryProvider;
// import android.widget.ImageView;
// import android.widget.SimpleCursorAdapter.CursorToStringConverter;
// import cn.hi321.browser.R;
// import cn.hi321.browser.config.Constants;
// import cn.hi321.browser.model.adapters.UrlSuggestionCursorAdapter;
// import cn.hi321.browser.providers.BookmarksProviderWrapper;
//
// public class SearchActivity extends Activity{
// private TextWatcher mUrlTextWatcher;
// private Button mGoButton;
// private AutoCompleteTextView mUrlEditText;
// private ImageView delectButton;
// @Override
// protected void onCreate(Bundle savedInstanceState) {
// // TODO Auto-generated method stub
// super.onCreate(savedInstanceState);
// setContentView(R.layout.searchactivity);
// delectButton= (ImageView)findViewById(R.id.delectid);
// delectButton.setVisibility(View.GONE);
// mUrlEditText = (AutoCompleteTextView) findViewById(R.id.UrlText);
// mGoButton = (Button) findViewById(R.id.GoBtn);
// mUrlEditText.setThreshold(1);
// String[] from = new String[]
// {UrlSuggestionCursorAdapter.URL_SUGGESTION_TITLE,
// UrlSuggestionCursorAdapter.URL_SUGGESTION_URL};
// int[] to = new int[] {R.id.AutocompleteTitle, R.id.AutocompleteUrl};
//
// final UrlSuggestionCursorAdapter adapter = new
// UrlSuggestionCursorAdapter(this, R.layout.url_autocomplete_line, null, from,
// to);
//
// adapter.setCursorToStringConverter(new CursorToStringConverter() {
// @Override
// public CharSequence convertToString(Cursor cursor) {
// String aColumnString =
// cursor.getString(cursor.getColumnIndex(UrlSuggestionCursorAdapter.URL_SUGGESTION_URL));
// return aColumnString;
// }
// });
//
// adapter.setFilterQueryProvider(new FilterQueryProvider() {
// @Override
// public Cursor runQuery(CharSequence constraint) {
// if ((constraint != null) &&
// (constraint.length() > 0)) {
//
// return BookmarksProviderWrapper.getUrlSuggestions(getContentResolver(),
// constraint.toString(),
// PreferenceManager.getDefaultSharedPreferences(SearchActivity.this).getBoolean(Constants.PREFERENCE_USE_WEAVE,
// false));
// } else {
//
// return BookmarksProviderWrapper.getUrlSuggestions(getContentResolver(),
// null,
// PreferenceManager.getDefaultSharedPreferences(SearchActivity.this).getBoolean(Constants.PREFERENCE_USE_WEAVE,
// false));
// }
// }
// });
// mUrlEditText.setAdapter(adapter);
//
// mUrlEditText.setOnKeyListener(new View.OnKeyListener() {
//
// @Override
// public boolean onKey(View v, int keyCode, KeyEvent event) {
// if (keyCode == KeyEvent.KEYCODE_ENTER) {
// // navigateToUrl();
// return true;
// }
//
// return false;
// }
//
// });
//
//
// mUrlTextWatcher = new TextWatcher() {
// @Override
// public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
// }
//
// @Override
// public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int
// arg3) { }
//
// @Override
// public void afterTextChanged(Editable arg0) {
// if(TextUtils.isEmpty(mUrlEditText.getText().toString())) {
// delectButton.setVisibility(View.GONE);
// mGoButton.setText("取消");
// }else {
// delectButton.setVisibility(View.VISIBLE);
// mGoButton.setText("前往");
//
// }
//
// // updateGoButton();
// }
// };
//
// mUrlEditText.addTextChangedListener(mUrlTextWatcher);
//
// mUrlEditText.setOnItemClickListener(new OnItemClickListener() {
//
// @Override
// public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
// long arg3) {
// String url = mUrlEditText.getText().toString();
// if(!TextUtils.isEmpty(url)){
// Intent intent = new Intent();
// intent.putExtra("url", url);
// intent.setAction("search");
// SearchActivity.this.sendBroadcast(intent);
//
// }
// finish();
// }
//
// });
// mUrlEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//
// @Override
// public void onFocusChange(View v, boolean hasFocus) {
// // Select all when focus gained.
// if (hasFocus) {
// mUrlEditText.setSelection(0, mUrlEditText.getText().length());
// }
// }
// });
//
// mUrlEditText.setCompoundDrawablePadding(5);
//
// mGoButton.setOnClickListener(new View.OnClickListener() {
// public void onClick(View view) {
// String url = mUrlEditText.getText().toString();
// if(!TextUtils.isEmpty(url)){
// Intent i = new Intent(SearchActivity.this,MainActivity.class);
// i.putExtra("url", url);
// SearchActivity.this.startActivityForResult(i, 10);
//
// }
// SearchActivity.this.finish();
// }
// });
//
// delectButton.setOnClickListener(new ImageView.OnClickListener() {
//
// @Override
// public void onClick(View v) {
// // TODO Auto-generated method stub
// mUrlEditText.setText("");
// mGoButton.setText("取消");
// }
// });
//
// }
//
// }
