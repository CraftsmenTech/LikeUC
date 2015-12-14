package cn.hi321.browser.preferences;

import android.app.Activity;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import cn.hi321.browser.config.Constants;
import cn.hi321.browser2.R;

/**
 * Home page preference chooser activity.
 */
public class HomepagePreferenceActivity extends Activity {
	protected Spinner mSpinner;
	protected EditText mCustomEditText;
	private ImageView mImageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.homepage_preference);
		mCustomEditText = (EditText) findViewById(R.id.BaseSpinnerCustomPreferenceEditText);
		mImageView = (ImageView) findViewById(R.id.back_gohome);

		mSpinner = (Spinner) findViewById(R.id.BaseSpinnerCustomPreferenceSpinner);

		mSpinner.setPromptId(getSpinnerPromptId());

		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, getSpinnerValuesArrayId(), R.layout.simple_spinner_item);

		adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
		mSpinner.setAdapter(adapter);

		setSpinnerValueFromPreferences();

		mSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long id) {
				onSpinnerItemSelected(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}

		});

		Button okBtn = (Button) findViewById(R.id.BaseSpinnerCustomPreferenceOk);
		okBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onOk();
				finish();
			}
		});

		Button cancelBtn = (Button) findViewById(R.id.BaseSpinnerCustomPreferenceCancel);
		cancelBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		mImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();

			}
		});
	}

	protected int getSpinnerPromptId() {
		return R.string.HomepagePreferenceActivity_Prompt;
	}

	protected int getSpinnerValuesArrayId() {
		return R.array.HomepageValues;
	}

	protected void onOk() {
		Editor editor = PreferenceManager.getDefaultSharedPreferences(this)
				.edit();
		editor.putString(Constants.PREFERENCES_GENERAL_HOME_PAGE,
				mCustomEditText.getText().toString());
		editor.commit();
	}

	protected void onSpinnerItemSelected(int position) {
		switch (position) {
		case 0:
			mCustomEditText.setEnabled(false);
			mCustomEditText.setText(Constants.URL_ABOUT_START);
			break;
		case 1: {
			mCustomEditText.setEnabled(true);

			if ((mCustomEditText.getText().toString()
					.equals(Constants.URL_ABOUT_START))
					|| (mCustomEditText.getText().toString()
							.equals(Constants.URL_ABOUT_BLANK))) {
				mCustomEditText.setText(null);
			}
			break;
		}
		default:
			mCustomEditText.setEnabled(false);
			mCustomEditText.setText(Constants.URL_ABOUT_START);
			break;
		}
	}

	protected void setSpinnerValueFromPreferences() {
		String currentHomepage = PreferenceManager.getDefaultSharedPreferences(
				this).getString(Constants.PREFERENCES_GENERAL_HOME_PAGE,
				Constants.URL_ABOUT_START);

		if (currentHomepage.equals(Constants.URL_ABOUT_START)) {
			mSpinner.setSelection(0);
			mCustomEditText.setEnabled(false);
			mCustomEditText.setText(Constants.URL_ABOUT_START);
		} else {
			mSpinner.setSelection(1);
			mCustomEditText.setEnabled(true);
			mCustomEditText.setText(currentHomepage);
		}
	}

}
