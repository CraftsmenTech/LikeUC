
package cn.hi321.browser.preferences;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import cn.hi321.browser.config.Constants;
import cn.hi321.browser2.R;

public class WeavePreferencesActivity extends PreferenceActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		addPreferencesFromResource(R.layout.weave_preferences_activity);
		
		Preference weaveServerPref = (Preference) findPreference(Constants.PREFERENCE_WEAVE_SERVER);
		weaveServerPref.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				openWeaveServerActivity();
				return true;
			}
		});
	}
	
	private void openWeaveServerActivity() {
		Intent i = new Intent(this, WeaveServerPreferenceActivity.class);
		startActivity(i);
	}

}
