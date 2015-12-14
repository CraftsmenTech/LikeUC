package cn.hi321.browser.utils;

import cn.hi321.browser.ui.activities.HomeActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ProxyChangeReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent) 
    {
    	Log.d("ProxyChangeReceiver", "Proxy change receiver called: " + intent.toString());
    	
    	if (HomeActivity.INSTANCE != null)
    	{
    		Log.d("ProxyChangeReceiver", "Refresh system preferences");
    		HomeActivity.INSTANCE.applyPreferences();
    	}
    }
}
