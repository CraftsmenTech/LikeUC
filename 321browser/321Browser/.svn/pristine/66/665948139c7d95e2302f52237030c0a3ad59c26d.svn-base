
package cn.hi321.browser.preferences;

import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import cn.hi321.browser.config.Constants;
import cn.hi321.browser2.R;

public class WeaveServerPreferenceActivity extends BaseSpinnerCustomPreferenceActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
	}

	@Override
	protected int getSpinnerPromptId() {		
		return R.string.WeaveServerPreferenceActivity_Prompt;
	}

	@Override
	protected int getSpinnerValuesArrayId() {
		return R.array.WeaveServerValues;
	}

	@Override
	protected void setSpinnerValueFromPreferences() {
		String currentServer = PreferenceManager.getDefaultSharedPreferences(this).getString(Constants.PREFERENCE_WEAVE_SERVER, Constants.WEAVE_DEFAULT_SERVER);
		
		if (currentServer.equals(Constants.WEAVE_DEFAULT_SERVER)) {
			mSpinner.setSelection(0);
			mCustomEditText.setEnabled(false);
			mCustomEditText.setText(Constants.WEAVE_DEFAULT_SERVER);
		} else {
			mSpinner.setSelection(1);
			mCustomEditText.setEnabled(true);
			mCustomEditText.setText(currentServer);					
		}
	}

	@Override
	protected void onSpinnerItemSelected(int position) {
		switch (position) {
		case 0: mCustomEditText.setEnabled(false); mCustomEditText.setText(Constants.WEAVE_DEFAULT_SERVER); break;		
		case 1: {
			mCustomEditText.setEnabled(true);
			
			if (mCustomEditText.getText().toString().equals(Constants.WEAVE_DEFAULT_SERVER)) {					
				mCustomEditText.setText(null);
			}
			break;
		}
		default: mCustomEditText.setEnabled(false); mCustomEditText.setText(Constants.WEAVE_DEFAULT_SERVER); break;
		}
	}

	@Override
	protected void onOk() {
		Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
    	editor.putString(Constants.PREFERENCE_WEAVE_SERVER, mCustomEditText.getText().toString());
    	editor.commit();
	}

}
